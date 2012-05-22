package com.cortexity.fsm.exception;

/**
 * Any state machine exceptions are the result of a programming error, therefore
 * they are unchecked (runtime) exceptions
 */
public class StateMachineException extends RuntimeException {

    private static final long serialVersionUID = 1284372166294790498L;

    public StateMachineException(String string) {
        super(string);
    }


}
