package com.hireflow.common.exception.custom;

public class UnauthorizedActionException extends RuntimeException
{
    public UnauthorizedActionException(String message)
    {
        super(message);
    }
}
