package com.cortexity.fsm.exception;


public class StateTransitionException extends StateMachineException {
  
    public StateTransitionException(String string) {
        super(string);
    }

    private static final long serialVersionUID = -7144558743483032747L;
}
