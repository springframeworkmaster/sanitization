package com.tasks.validation.sanitization.components;

import static com.tasks.validation.sanitization.utils.Constants.STRING_PATTERN;

import java.beans.Introspector;
import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.tasks.validation.sanitization.domain.Error;


public class Sanitizer {
    private MethodHandles.Lookup lookup;
    private MethodType arrayMethodType;
    private MethodType listMethodType;
    private MethodType stringMethodType;
    private MethodType nodeMethodType;
    private MethodType nodesMethodType;
    private String regex;
    private HashMap<Class<?>, InputData> class_data = new HashMap<>();

    public Sanitizer(MethodHandles.Lookup lookup, 
    		MethodType arrayMethodType, 
    		MethodType listMethodType, 
    		MethodType stringMethodType, 
    		MethodType nodeMethodType, 
    		MethodType nodesMethodType, 
    		String regex) {
        this.lookup = lookup;
        this.arrayMethodType = arrayMethodType;
        this.listMethodType = listMethodType;
        this.stringMethodType = stringMethodType;
        this.nodeMethodType = nodeMethodType;
        this.nodesMethodType = nodesMethodType;
        this.regex = regex;
    }

    public Sanitizer(MethodHandles.Lookup lookup, 
    		MethodType arrayMethodType, 
    		MethodType listMethodType, 
    		MethodType stringMethodType, 
    		MethodType nodeMethodType, 
    		MethodType nodesMethodType) {
        this.lookup = lookup;
        this.arrayMethodType = arrayMethodType;
        this.listMethodType = listMethodType;
        this.stringMethodType = stringMethodType;
        this.nodeMethodType = nodeMethodType;
        this.nodesMethodType = nodesMethodType;
        this.regex = STRING_PATTERN;
    }

    private ListGetter compile_list_getter(MethodHandles.Lookup lookup, Method method) throws Throwable {
        MethodHandle handle = lookup.unreflect(method);

        CallSite site = LambdaMetafactory.metafactory(
                lookup, "apply",
                listMethodType,
                MethodType.methodType(List.class, Object.class),
                handle, handle.type());

        return (ListGetter) site.getTarget().invoke();
    }

    private ArrayGetter compile_array_getter(MethodHandles.Lookup lookup, Method method) throws Throwable {
        MethodHandle handle = lookup.unreflect(method);

        CallSite site = LambdaMetafactory.metafactory(
                lookup, "apply",
                arrayMethodType,
                MethodType.methodType(String[].class, Object.class),
                handle, handle.type());

        return (ArrayGetter) site.getTarget().invoke();
    }

    private StringGetter compile_string_getter(MethodHandles.Lookup lookup, Method method) throws Throwable {
        MethodHandle handle = lookup.unreflect(method);

        CallSite site = LambdaMetafactory.metafactory(
                lookup, "apply",
                stringMethodType,
                MethodType.methodType(String.class, Object.class),
                handle, handle.type());

        return (StringGetter) site.getTarget().invoke();
    }

    private ObjectGetter compile_node_getter(MethodHandles.Lookup lookup, Method method) throws Throwable {
        MethodHandle handle = lookup.unreflect(method);

        CallSite site = LambdaMetafactory.metafactory(
                lookup, "apply",
                nodeMethodType,
                MethodType.methodType(Object.class, Object.class),
                handle, handle.type());

        return (ObjectGetter) site.getTarget().invoke();
    }

    private ObjectsGetter compile_nodes_getter(MethodHandles.Lookup lookup, Method method) throws Throwable {
        MethodHandle handle = lookup.unreflect(method);

        CallSite site = LambdaMetafactory.metafactory(
                lookup, "apply",
                nodesMethodType,
                MethodType.methodType(Collection.class, Object.class),
                handle, handle.type());

        return (ObjectsGetter) site.getTarget().invoke();
    }

    private static boolean isValidString(String value, String regex) {
        return Pattern.compile(regex).matcher(value).matches();
    }

    public List<Error> validate(Object node, Consumer<Object> visitor, List<Error> errors, String path) throws Throwable {
        visitor.accept(node);
        Class<?> currentClass = node.getClass();

        InputData data = class_data.computeIfAbsent(currentClass, k -> {
            try {
                return new InputData(lookup, k, node);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });

        for (StringGetter getter : data.stringGetters.keySet()) {
            String value = getter.apply(node);
            if (StringUtils.isBlank(value)) continue;
            if (!isValidString(value, regex)) {
                String key = path + data.stringGetters.get(getter);
                errors.add(new Error(key, value + " is invalid"));
            }
        }

        for (ObjectGetter getter : data.getter.keySet()) {
            Object child = getter.apply(node);
            if (child == null) continue;
            String key = path + data.getter.get(getter)+".";
            validate(child, visitor, errors, key);
        }

        for (ObjectsGetter getter : data.getters.keySet()) {
            Collection<Object> children = Utility.cast(getter.apply(node));
            if (children == null || children.isEmpty()) continue;
            int index = 0;
            for (Object child : children) {
                if (child == null) continue;
                String key = path + data.getters.get(getter) + "[" + index + "].";
                validate(child, visitor, errors, key);
                index++;
            }
        }

        for (ListGetter getter : data.listGetters.keySet()) {
            List<String> list = getter.apply(node);
            if (list == null) continue;

            for (int i = 0; i < list.size(); i++) {
                String key = path + data.listGetters.get(getter) + "[" + i + "]";
                String value = list.get(i);
                if (StringUtils.isBlank(value)) continue;
                if (!isValidString(value, regex))
                    errors.add(new Error(key, value + " is invalid"));
            }
        }

        for (ArrayGetter getter : data.arrayGetter.keySet()) {
            String[] array = getter.apply(node);
            if (array == null) continue;

            for (int i = 0; i < array.length; i++) {
                String key = path + data.arrayGetter.get(getter) + "[" + i + "]";
                String value = array[i];
                if (StringUtils.isBlank(value)) continue;
                if (!isValidString(value, regex))
                    errors.add(new Error(key, value + " is invalid"));
            }
        }
        return errors;
    }


    @FunctionalInterface
    public interface StringGetter {
        String apply(Object node);
    }

    @FunctionalInterface
    public interface ListGetter {
        List<String> apply(Object node);
    }

    @FunctionalInterface
    public interface ArrayGetter {
        String[] apply(Object node);
    }

    @FunctionalInterface
    public interface ObjectGetter {
        Object apply(Object node);
    }
    @FunctionalInterface
    public interface ObjectsGetter {
        Collection<? extends Object> apply(Object node);
    }

    class InputData {
        Map<ObjectGetter, String> getter = new HashMap<>();
        Map<ObjectsGetter, String> getters = new HashMap<>();
        Map<ListGetter, String> listGetters = new HashMap<>();
        Map<StringGetter, String> stringGetters = new HashMap<>();
        Map<ArrayGetter, String> arrayGetter = new HashMap<>();

        InputData(MethodHandles.Lookup lookup, Class<?> currentClass, Object node) throws Throwable {
            for (Method m : currentClass.getMethods()) {
                m.setAccessible(true);
                if (m.getParameterCount() != 0) continue;

                if (m.getName().startsWith("get") && !m.getName().startsWith("getClass")) {
                    if (String.class.isAssignableFrom(m.getReturnType())) {
                        stringGetters.put(compile_string_getter(lookup, m), Introspector.decapitalize(m.getName().replace("get","")));
                        continue;
                    }

                    if(Enum.class.isAssignableFrom(m.getReturnType())){
                        continue;
                    }

                    if (String[].class.isAssignableFrom(m.getReturnType())) {
                        String[] array = (String[]) m.invoke(node);
                        if (ArrayUtils.isNotEmpty(array)) {
                            arrayGetter.put(compile_array_getter(lookup, m), Introspector.decapitalize(m.getName().replace("get","")));
                            continue;
                        }
                    }

   
                    if (Object.class.isAssignableFrom(m.getReturnType()) && !Collection.class.isAssignableFrom(m.getReturnType())) {
                        getter.put(compile_node_getter(lookup, m), Introspector.decapitalize(m.getName().replace("get","")));
                        continue;
                    }

                    if (Collection.class.isAssignableFrom(m.getReturnType())) {
                        ParameterizedType ret = ((ParameterizedType) m.getGenericReturnType());
                        Type param = ret.getActualTypeArguments()[0];

                        if (param instanceof Class<?>
                                && Object.class.isAssignableFrom((Class<?>) param))
                            getters.put(compile_nodes_getter(lookup, m), StringUtils.uncapitalize(m.getName().replace("get","")));

                        if (param instanceof Class<?>
                                && String.class.isAssignableFrom((Class<?>) param)) {
                            ArrayList<?> arrayList = (ArrayList<?>) m.invoke(node);
                            if (CollectionUtils.isNotEmpty(arrayList))
                                listGetters.put(compile_list_getter(lookup, m), Introspector.decapitalize(m.getName().replace("get","")));
                        }
                    }
                }
            }
        }
    }
}