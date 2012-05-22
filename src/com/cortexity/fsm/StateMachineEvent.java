package com.cortexity.fsm;

public class StateMachineEvent {

    public static final StateMachineEvent GENERIC = new StateMachineEvent("GENERIC");

    private final String name;

    public StateMachineEvent(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj || !this.getClass().equals(obj.getClass())) {
            return false;
        }
        StateMachineEvent other = (StateMachineEvent) obj;
        return other.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

}
