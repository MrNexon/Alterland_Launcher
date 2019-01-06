package ru.alterland.java.api.exceptions;

public class AuthException extends ApiExceptions{
    private Type type;

    public enum Type {
        UserNotFound, AccessError
    }

    public AuthException(String message, Type type){
        super(message);
        this.type = type;
    }

    public Type getAuthType(){
        return this.type;
    }

}
