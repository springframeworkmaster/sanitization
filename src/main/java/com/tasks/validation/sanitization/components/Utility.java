package com.tasks.validation.sanitization.components;

public class Utility
{
    /**
     * Casts the receiver to type T (unsafe). T can be automatically inferred.
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast (Object object)
    {
        return (T) object;
    }
}