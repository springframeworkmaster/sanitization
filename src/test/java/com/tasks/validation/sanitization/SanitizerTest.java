package com.tasks.validation.sanitization;


import com.tasks.validation.sanitization.components.Sanitizer;
import com.tasks.validation.sanitization.domain.Carington;
import com.tasks.validation.sanitization.domain.Error;
import com.tasks.validation.sanitization.domain.Person;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.tasks.validation.sanitization.utils.Constants.STRING_PATTERN;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SanitizerTest {

    private Sanitizer inputValidator;

    private Carington demo;

    private Carington invalidDemo;

    private List<Error> validationErrors;

    private Person p;

    @Before
    public void setup() {

        String[] personNames = {"name1","name2 <><>><><><"};
        p = new Person();
        p.setProfessionalSuffixes(personNames);
        demo = new Carington();
        invalidDemo = new Carington();
        validationErrors = new ArrayList<>();

        demo.setRelativeId(UUID.randomUUID());
        demo.setExternalId("23456789");
        demo.setStreetAddress("123 Main St");
        List<Carington> demos = new ArrayList<>();
        demos.add(new Carington());
        demos.get(0).setRelativeId(UUID.randomUUID());
        demos.get(0).setExternalId("23456789");
        demos.get(0).setStreetAddress("123 Main St");
        demo.setItems(demos);


        invalidDemo.setRelativeId(UUID.randomUUID());
        invalidDemo.setExternalId("<script>alert('hi')</script>");
        invalidDemo.setStreetAddress("123 Main $$ St");
        List<Carington> invalidDemos = new ArrayList<>();
        invalidDemos.add(new Carington());
        invalidDemos.get(0).setRelativeId(UUID.randomUUID());
        invalidDemos.get(0).setExternalId("<script>$('html').html('');</script>");
        invalidDemos.get(0).setStreetAddress("123 Main <> St");
        invalidDemo.setItems(invalidDemos);

        inputValidator = new Sanitizer(
                MethodHandles.lookup(),
                MethodType.methodType(Sanitizer.ArrayGetter.class),
                MethodType.methodType(Sanitizer.ListGetter.class),
                MethodType.methodType(Sanitizer.StringGetter.class),
                MethodType.methodType(Sanitizer.ObjectGetter.class),
                MethodType.methodType(Sanitizer.ObjectsGetter.class),
                STRING_PATTERN
        );
    }

    @Test
    public void TestValidDemo() throws Throwable{
        List<Error> errors = inputValidator.validate(demo, s->s.getClass(),validationErrors, "");
        assertEquals(Collections.EMPTY_LIST,errors);
    }

    @Test
    public void TestInvalidDemo() throws Throwable{
        List<Error> errors = inputValidator.validate(invalidDemo, s->s.getClass(),validationErrors, "");
        assertEquals(4,errors.size());
    }
}