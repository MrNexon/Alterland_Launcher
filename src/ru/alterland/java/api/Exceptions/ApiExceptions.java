package ru.alterland.java.api.Exceptions;

public class ApiExceptions extends Exception {
    private Type type;
    public enum Type {
        UnknownError, InternalError, InvalidAccessToken, InvalidClass, InvalidMethod, IncorrectParameters, InvalidFormat, NotFound;
    }

    public ApiExceptions(String message){
        super(message);
    }

    public ApiExceptions(String message, Type type){
        super(message);
        this.type = type;
    }

}
