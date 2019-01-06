package ru.alterland.java.api.exceptions;

public class ApiExceptions extends Exception {
    public Type type;
    public enum Type {
        UnknownError, InternalError, InvalidAccessToken, InvalidClass, InvalidMethod, IncorrectParameters, InvalidFormat, NotFound, ConnectionError
    }

    public ApiExceptions(String message){
        super(message);
    }

    public Type getType() { return type; }

    public ApiExceptions(String message, Type type){
        super(message);
        this.type = type;
    }

}
