package com.cortexity.fsm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StateModel {

    private final Set<State> states = new HashSet<State>();

    private final State finalState;

    private State currentState;

    public StateModel(State initState, State endState) {
        currentState = initState;
        finalState = endState;
    }

    public void addState(State state) {
        this.states.add(state);
    }

    Collection<State> getStates() {
        return states;
    }

    public State getState() {
        return currentState;
    }

    State getFinalState() {
        return this.finalState;
    }

    State getCurrentState() {
        return this.currentState;
    }

    void setState(State state) {
        this.currentState = state;
    }

}
