package com.tean.supergame.model.enums;

public enum StatusCodeEnum {
    //USER
    USER1200("USER1200"), // Sign up successfully
    USER1201("USER1201"), // Sign in successfully
    USER0200("USER0200"), // Sign up failed
    USER0201("USER0201"), // User not exist
    USER0202("USER0202"), // login password is incorrect
    USER0203("USER0203"),// The user has checked in before
    USER0204("USER0204"),// It's not time to check in yet
    USER1202("USER1202"),// The user has successfully checked in
    USER0205("USER0205"), // user already exists
    USER1203("USER1203"),// get profile successfully
    USER1204("USER1204"),// can take attendance
    USER1205("USER1205"),// get point transaction successfully
    //EXCEPTION
    EXCEPTION500("EXCEPTION500"); //Server error

    public final String value;

    StatusCodeEnum(String i) {
        value = i;
    }
}
