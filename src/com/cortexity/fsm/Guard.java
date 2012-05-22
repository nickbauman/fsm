package com.cortexity.fsm;

public interface Guard {
    boolean canTransition();
    boolean canTransition(Object obj);
}
