package com.cortexity.fsm;

import java.util.Collection;

import junit.framework.TestCase;

public class StateTest extends TestCase {
    public void testAddingTransitions() throws Exception {
        State state = new State("a state");
        assertEquals("a state", state.getName());
        StateTransition transition = new StateTransition(state, state);
        state.addTranstion(transition);
        Collection<StateTransition> transitions = state.getTransitions(StateMachineEvent.GENERIC);
        assertNotNull(transitions);
        assertEquals(1, transitions.size());
        assertEquals(transition, transitions.iterator().next());
    }
}
