package com.tean.supergame.model.enums;

public enum AttendanceStatusEnum {
    ACTIVE(0), LOCK(1);

    public final int value;

    AttendanceStatusEnum(int i) {
        value = i;
    }
}