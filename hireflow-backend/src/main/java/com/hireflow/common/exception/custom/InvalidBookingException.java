package com.hireflow.common.exception.custom;

public class InvalidBookingException extends RuntimeException
{
    public InvalidBookingException(String message)
    {
        super(message);
    }
}
