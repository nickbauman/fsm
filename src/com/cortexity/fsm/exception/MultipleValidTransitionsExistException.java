package com.cortexity.fsm.exception;

import java.util.Collection;
import java.util.Iterator;

import com.cortexity.fsm.State;

public class MultipleValidTransitionsExistException extends StateMachineException {

    public MultipleValidTransitionsExistException(Collection<State> possibleStates) {
        super(convertStatesToMessage(possibleStates));
    }

    private static final long serialVersionUID = -4504609586995092802L;

    private static String convertStatesToMessage(Collection<State> possibleStates) {
        StringBuffer buffer = new StringBuffer(128);
        buffer.append("Cannot find descrete transition. Multiple states are possible: ");
        for (Iterator<State> iter = possibleStates.iterator(); iter.hasNext();) {
            State state = iter.next();
            buffer.append('<').append(state).append('>');
        }
        return buffer.toString();
    }
}
