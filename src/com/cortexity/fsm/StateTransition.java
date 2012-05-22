package com.cortexity.fsm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StateTransition {
    private String name;

    private State origin;

    private State destination;

    private Guard guard;

    private final Set<StateMachineAction> actions = new HashSet<StateMachineAction>();

    private Set<StateMachineEvent> events = new HashSet<StateMachineEvent>();
    
    public StateTransition(State fromState, State toState) {
        this(fromState, toState, StateMachineEvent.GENERIC);
    }

    public StateTransition(State fromState, State toState, StateMachineEvent router) {
        name = "From '" + fromState.getName() + "' To '" + toState.getName() + "'";
        origin = fromState;
        destination = toState;
        events.add(router);
        fromState.addTranstion(this);
    }

    public String getName() {
        return name;
    }

    public State getFrom() {
        return origin;
    }

    public State getTo() {
        return destination;
    }

    public void setGuard(Guard guard) {
        this.guard = guard;
    }

    Guard getGuard() {
        return guard;
    }

    Collection<StateMachineAction> getActions() {
        return actions;
    }

    public void addAction(StateMachineAction transitionAction) {
        this.actions.add(transitionAction);
    }
    
    Set<StateMachineEvent> getEvents() {
        return this.events;
    }
    
    @Override
    public String toString() {
        return name;
    }
    

}
