package com.cortexity.fsm;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

import com.cortexity.fsm.exception.MultipleValidTransitionsExistException;
import com.cortexity.fsm.exception.StateMachineException;
import com.cortexity.fsm.exception.StateModelEndedException;
import com.cortexity.fsm.exception.StateTransitionException;

public class StateMachineTest extends TestCase implements StateMachineable<StateMachineTest> {

   private StateModel model;

   private State initState;

   private State finalState;

   @Override
   protected void tearDown() throws Exception {
      model = null;
      initState = null;
      finalState = null;
      super.tearDown();
   }

   @Override
   protected void setUp() throws Exception {
      super.setUp();
   }

   public void testAddingStates() throws Exception {
      // States
      initState = new State("init state");
      finalState = new State("final state");
      State anotherState = new State("another state");
      State yetAnotherState = new State("yet another state");

      // Transitions
      new StateTransition(initState, finalState);
      new StateTransition(initState, anotherState);

      // Model
      model = new StateModel(initState, finalState);
      model.addState(anotherState);
      model.addState(yetAnotherState);

      Collection<State> states = model.getStates();
      assertNotNull(states);
      assertEquals(2, states.size());
      assertTrue(states.contains(yetAnotherState));
      states = model.getStates();
      assertEquals(2, states.size());
      boolean foundFirstState = false;
      boolean foundAnotherState = false;
      for (Iterator<State> iterator = states.iterator(); iterator.hasNext();) {
         State state = iterator.next();
         if (state.equals(yetAnotherState)) {
            foundFirstState = true;
         } else if (state.equals(anotherState)) {
            foundAnotherState = true;
         }
      }
      assertTrue(foundAnotherState);
      assertTrue(foundFirstState);
      assertFalse(anotherState.equals(yetAnotherState));
   }

   public void testFireEventWithNonDescreteTransition() throws Exception {
      // States
      initState = new State("init state");
      finalState = new State("final state");
      State anotherState = new State("another state");
      State yetAnotherState = new State("yet another state");

      // Transitions
      new StateTransition(initState, finalState);
      new StateTransition(initState, anotherState);

      // Model
      model = new StateModel(initState, finalState);
      model.addState(anotherState);
      model.addState(yetAnotherState);

      assertEquals(initState, model.getState());
      try {
         fire(StateMachineEvent.GENERIC);
         fail();
      } catch (Exception e) {
         assertEquals(MultipleValidTransitionsExistException.class, e.getClass());
         assertTrue(-1 != e.getMessage().indexOf(
               "Cannot find descrete transition. Multiple states are possible:"));
      }
   }

   public void testFireEventWithoutTransions() throws Exception {
      // States
      initState = new State("init state");
      finalState = new State("final state");
      State anotherState = new State("another state");
      State yetAnotherState = new State("yet another state");

      // Model
      model = new StateModel(initState, finalState);
      model.addState(anotherState);
      model.addState(yetAnotherState);

      assertEquals(initState, model.getState());
      try {
         fire(StateMachineEvent.GENERIC);
         fail();
      } catch (Exception e) {
         assertEquals(StateTransitionException.class, e.getClass());
         assertEquals("No transitions configured out of 'init state' for event 'GENERIC'", e.getMessage());
      }
   }

   public void testFireEventBlockedTransions() throws Exception {
      StateMachineAction dontRun = createMock(StateMachineAction.class);
      replay(dontRun);

      // States
      initState = new State("init state");
      finalState = new State("final state");
      State anotherState = new State("another state");
      State yetAnotherState = new State("yet another state");

      // Action
      initState.addExitAction(dontRun);

      // Model
      model = new StateModel(initState, finalState);
      model.addState(anotherState);
      model.addState(yetAnotherState);

      // Transitions
      StateTransition transition = new StateTransition(initState, finalState);

      // Guards
      transition.setGuard(new AlwaysBlocksGuard());

      assertEquals(initState, model.getState());
      try {
         fire(StateMachineEvent.GENERIC);
         fail();
      } catch (Exception e) {
         assertEquals(StateTransitionException.class, e.getClass());
         assertEquals("No valid transitions found for 'init state'", e.getMessage());
      }
      verify(dontRun);
   }

   public void testFireEvent() throws Exception {
      // States
      initState = new State("init state");
      finalState = new State("final state");
      State anotherState = new State("another state");
      State yetAnotherState = new State("yet another state");

      // Transitions
      StateTransition transition1 = new StateTransition(initState, finalState);
      StateTransition transition2 = new StateTransition(initState, anotherState);

      // Guards
      Guard guard = new AlwaysBlocksGuard();
      NotGuard notGuard = new NotGuard(guard);
      transition1.setGuard(notGuard);
      transition2.setGuard(guard);

      // Model
      model = new StateModel(initState, finalState);
      model.addState(anotherState);
      model.addState(yetAnotherState);

      assertEquals(initState, model.getState());
      fire(StateMachineEvent.GENERIC);
      assertEquals(finalState, model.getState());
   }

   public void testRunStateEntryActions() throws Exception {
      // Actions
      createMock(StateMachineAction.class);
      StateMachineAction action = createMock(StateMachineAction.class);
      action.execute(this);
      replay(action);

      // States
      initState = new State("init state");
      finalState = new State("final state");
      finalState.addEntryAction(action);
      State anotherState = new State("another state");
      State yetAnotherState = new State("yet another state");

      // Transitions
      StateTransition transition1 = new StateTransition(initState, finalState);
      StateTransition transition2 = new StateTransition(initState, anotherState);

      // Guards
      Guard guard = new AlwaysBlocksGuard();
      NotGuard notGuard = new NotGuard(guard);
      transition1.setGuard(notGuard);
      transition2.setGuard(guard);

      // Model
      model = new StateModel(initState, finalState);
      model.addState(anotherState);
      model.addState(yetAnotherState);

      assertEquals(initState, model.getState());
      fire(StateMachineEvent.GENERIC);
      assertEquals(finalState, model.getState());
      verify(action);
   }

   public void testEventPastFinalStateThrowsException() throws Exception {
      // States
      initState = new State("init state");
      finalState = new State("final state");

      // Transitions
      new StateTransition(initState, finalState);

      // Model
      model = new StateModel(initState, finalState);

      assertEquals(initState, model.getState());
      fire(StateMachineEvent.GENERIC);
      assertEquals(finalState, model.getState());
      try {
         fire(StateMachineEvent.GENERIC);
         fail();
      } catch (StateModelEndedException e) {
         assertEquals("Cannot move past final state in model", e.getMessage());
      }
   }

   public void testRunStateExitActions() throws Exception {
      // Actions
      StateMachineAction exitAction = createMock(StateMachineAction.class);
      exitAction.execute(this);
      replay(exitAction);

      StateMachineAction entryAction = createMock(StateMachineAction.class);
      entryAction.execute(this);
      replay(entryAction);

      // States
      initState = new State("init state");
      initState.addExitAction(exitAction);
      finalState = new State("final state");
      finalState.addEntryAction(entryAction);

      State anotherState = new State("another state");
      State yetAnotherState = new State("yet another state");

      // Transitions
      StateTransition transition1 = new StateTransition(initState, finalState);
      StateTransition transition2 = new StateTransition(initState, anotherState);

      // Guards
      Guard guard = new AlwaysBlocksGuard();
      NotGuard notGuard = new NotGuard(guard);
      transition1.setGuard(notGuard);
      transition2.setGuard(guard);

      // Model
      model = new StateModel(initState, finalState);
      model.addState(anotherState);
      model.addState(yetAnotherState);

      assertEquals(initState, model.getState());
      fire(StateMachineEvent.GENERIC);
      assertEquals(finalState, model.getState());
      verify(entryAction);
      verify(exitAction);
   }

   public void testRunTransitionActions() throws Exception {
      // Actions
      StateMachineAction exitAction = createMock(StateMachineAction.class);
      exitAction.execute(this);
      replay(exitAction);

      StateMachineAction entryAction = createMock(StateMachineAction.class);
      entryAction.execute(this);
      replay(entryAction);

      StateMachineAction transitionAction = createMock(StateMachineAction.class);
      transitionAction.execute(this);
      replay(transitionAction);

      // States
      initState = new State("init state");
      initState.addExitAction(exitAction);
      finalState = new State("final state");
      finalState.addEntryAction(entryAction);

      State anotherState = new State("another state");
      State yetAnotherState = new State("yet another state");

      // Transitions
      StateTransition transition1 = new StateTransition(initState, finalState);
      transition1.addAction(transitionAction);
      StateTransition transition2 = new StateTransition(initState, anotherState);

      // Guards
      Guard guard = new AlwaysBlocksGuard();
      NotGuard notGuard = new NotGuard(guard);
      transition1.setGuard(notGuard);
      transition2.setGuard(guard);

      // Model
      model = new StateModel(initState, finalState);
      model.addState(anotherState);
      model.addState(yetAnotherState);

      assertEquals(initState, model.getState());
      fire(StateMachineEvent.GENERIC);
      assertEquals(finalState, model.getState());
      verify(exitAction);
      verify(transitionAction);
      verify(entryAction);
   }

   public void testEventTravelsAcrossCorrectTransition() throws Exception {
      // States
      State stateOne = new State("state one");
      State stateTwo = new State("state two");
      finalState = new State("final");

      // Events
      StateMachineEvent event = new StateMachineEvent("taxation");

      // Transitions
      new StateTransition(stateOne, stateTwo);
      new StateTransition(stateOne, finalState, event);

      model = new StateModel(stateOne, finalState);
      model.addState(stateTwo);

      new SimpleStateMachine<StateMachineTest>().fire(this, new StateMachineEvent("taxation"));

      assertEquals(finalState, model.getCurrentState());
   }

   public void testEventTravelsAcrossCorrectTransitionAndHandlesTransientState() throws Exception {
      // States
      State stateOne = new State("state one");
      State stateTwo = new State("state two", true);
      State boloneyState = new State("boloney state");
      finalState = new State("final");

      // Events
      StateMachineEvent taxation = new StateMachineEvent("taxation");

      // Transitions
      new StateTransition(stateOne, boloneyState);
      new StateTransition(stateOne, stateTwo, taxation);
      new StateTransition(stateTwo, finalState);

      model = new StateModel(stateOne, finalState);
      model.addState(stateTwo);
      model.addState(boloneyState);

      new SimpleStateMachine<StateMachineTest>().fire(this, new StateMachineEvent("taxation"));

      assertEquals(finalState, model.getCurrentState());
   }

   public StateModel getStateModel() {
      return model;
   }

   public void fire(StateMachineEvent event) throws StateMachineException {
      new SimpleStateMachine<StateMachineTest>().fire(this, event);
   }

}
