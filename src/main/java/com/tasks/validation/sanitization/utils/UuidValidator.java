package com.tasks.validation.sanitization.utils;



import org.apache.commons.lang3.StringUtils;

public class UuidValidator {
    public UuidValidator() {
    }

    public static boolean isValidUuid(String uuid) {
        return !StringUtils.isEmpty(uuid) && uuid.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    }
}
