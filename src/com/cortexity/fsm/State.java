package com.cortexity.fsm;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class State {

   private String name;

   private final Map<StateMachineEvent, Set<StateTransition>> transitions = new HashMap<StateMachineEvent, Set<StateTransition>>();

   private final Collection<StateMachineAction> entryActions = new HashSet<StateMachineAction>();

   private final Collection<StateMachineAction> exitActions = new HashSet<StateMachineAction>();

   private boolean temporary;

   public State(String string) {
      this(string, false);
   }

   public State(String string, boolean temporary) {
      name = string;
      this.temporary = temporary;
   }

   public String getName() {
      return name;
   }

   protected void addTranstion(StateTransition transition) {
      Set<StateMachineEvent> events = transition.getEvents();
      for (StateMachineEvent event : events) {
         Set<StateTransition> set = transitions.get(event);
         if (null == set) {
            set = new HashSet<StateTransition>();
         }
         set.add(transition);
         transitions.put(event, set);
      }
   }

   Collection<StateTransition> getTransitions(StateMachineEvent event) {
      Set<StateTransition> trans = transitions.get(event);
      if (null == trans) {
         trans = new HashSet<StateTransition>();
         transitions.put(event, trans);
      }
      return trans;
   }

   Collection<StateMachineAction> getEntryActions() {
      return entryActions;
   }

   @Override
   public String toString() {
      return name;
   }

   public void addEntryAction(StateMachineAction action) {
      this.entryActions.add(action);
   }

   public void addExitAction(StateMachineAction action) {
      this.exitActions.add(action);
   }

   Collection<StateMachineAction> getExitActions() {
      return this.exitActions;
   }

   public boolean isTemporary() {
      return temporary;
   }
}
