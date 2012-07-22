package com.imdeity.helpticket.enums;

public enum PriorityType {
    LOW, MEDIUM, HIGH;
    
    public static PriorityType getFromString(String name) {
        if (LOW.name().equalsIgnoreCase(name)) { return LOW; }
        if (MEDIUM.name().equalsIgnoreCase(name)) return MEDIUM;
        if (HIGH.name().equalsIgnoreCase(name)) { return HIGH; }
        return null;
    }
}