package com.usermanagerproj.exception;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(UUID id, Class<?> entityClass) {
        super("The entity " + entityClass.getSimpleName() + " with id " + id + " was not found");
    }
    public EntityNotFoundException(String userName, Class<?> entityClass) {
        super("The entity " + entityClass.getSimpleName() + " with name " + userName + " was not found");
    }
}
