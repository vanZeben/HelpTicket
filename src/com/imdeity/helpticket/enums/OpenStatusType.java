package com.imdeity.helpticket.enums;

public enum OpenStatusType {
    OPEN, CLOSED;
    
    public static OpenStatusType getFromString(String name) {
        if (OPEN.name().equalsIgnoreCase(name)) { return OPEN; }
        if (CLOSED.name().equalsIgnoreCase(name)) { return CLOSED; }
        return null;
    }
}