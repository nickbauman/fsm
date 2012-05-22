package com.cortexity.fsm;

public class JSStateMachineTest extends JavaScriptTestCase {

   private static final String NO_VALID_TRANSITIONS_MSG = "No valid transitions found for 'init state'";

   private static final String NO_TRANSITIONS_CONFIGURED_MSG = "No transitions configured out of 'init state' for event 'GENERIC'";

   private static final String CANNOT_FIND_DESCRETE_TRANSITION_MSG = "Cannot find descrete transition. Multiple states are possible:";

   public JSStateMachineTest(String name) {
      super(name);
   }

   /**
    * The order the files are loaded matters...
    */
   @Override
   protected void doSetUp() throws Exception {
      executeScriptFile("jssrc/AssociativeArray.js");
      executeScriptFile("jssrc/State.js");
      executeScriptFile("jssrc/Set.js");
      executeScriptFile("jssrc/StateTransition.js");
      executeScriptFile("jssrc/StateModel.js");
      executeScriptFile("jssrc/StateMachine.js");
      executeScriptFile("unittest/com/cortexity/fsm/TestStateMachineable.js");
   }

   @Override
   protected void doTearDown() throws Exception {
   }

   public void testAddingStates() throws Exception {
      // States
      addLine("var initState = new State('init state')");
      addLine("var finalState = new State('final state')");
      addLine("var anotherState = new State('another state')");
      addLine("var yetAnotherState = new State('yet another state')");

      // Transitions
      addLine("var transition1 = new StateTransition(initState, finalState)");
      addLine("var transition2 = new StateTransition(initState, anotherState)");

      // Model
      addLine("var model = new StateModel(initState, finalState)");
      addLine("model.addState(anotherState)");
      addLine("model.addState(yetAnotherState)");

      // Assertions
      addLine("var states = model.getStates()\n");
      addLine("test.assertNotNull(states)");
      addLine("var len = states.length");
      addLine("test.assertEquals(2, len)");
      addLine("var statesSet = new Set(states)");
      addLine("test.assertTrue('missing yetAnotherState', statesSet.contains(yetAnotherState))");
      executeJSCode();
   }

   public void testFireEventWithNonDescreteTransition() throws Exception {
      // States
      addLine("var initState = new State('init state')");
      addLine("var finalState = new State('final state')");
      addLine("var anotherState = new State('another state')");
      addLine("var yetAnotherState = new State('yet another state')");

      // Transitions
      addLine("var transition1 = new StateTransition(initState, finalState)");
      addLine("var transition2 = new StateTransition(initState, anotherState)");
      addLine("test.assertNotNull('event 1 was null',transition1.getEvents().toArray()[0])");
      addLine("test.assertEquals('GENERIC', transition1.getEvents().toArray()[0])");
      addLine("test.assertNotNull('event 2 was null', transition2.getEvents().toArray()[0])");
      addLine("test.assertEquals('GENERIC', transition2.getEvents().toArray()[0])");
      addLine("test.assertEquals(2, initState.getAllTransitions().length)");

      // Model
      addLine("var model = new StateModel(initState, finalState)");
      addLine("model.addState(anotherState)");
      addLine("model.addState(yetAnotherState)");
      addLine("test.assertNotNull('model was null', model.getState())");
      addLine("test.assertEquals(initState, model.getState())");
      addLine("var machinable = new TestStateMachineable(model)");
      addLine("machinable.fire('GENERIC')");
      try {
         executeJSCode();
         fail("did not throw an exception");
      } catch (Exception e) {
         e.printStackTrace();
         String msg = e.getMessage();
         assertTrue("expected <" + CANNOT_FIND_DESCRETE_TRANSITION_MSG + "> but was <" + msg + ">",
               -1 != msg.indexOf(CANNOT_FIND_DESCRETE_TRANSITION_MSG));
      }
   }

   public void testFireEventWithoutTransions() throws Exception {
      // States
      addLine("var initState = new State('init state')");
      addLine("var finalState = new State('final state')");
      addLine("var anotherState = new State('another state')");
      addLine("var yetAnotherState = new State('yet another state')");

      // Model
      addLine("var model = new StateModel(initState, finalState)");
      addLine("model.addState(anotherState)");
      addLine("model.addState(yetAnotherState)");
      addLine("test.assertNotNull('model was null', model.getState())");
      addLine("test.assertEquals(initState, model.getState())");
      addLine("var machinable = new TestStateMachineable(model)");
      addLine("machinable.fire('GENERIC')");
      try {
         executeJSCode();
         fail("failed to throw exception");
      } catch (Exception e) {
         e.printStackTrace();
         String msg = e.getMessage();
         assertTrue("expected <" + NO_TRANSITIONS_CONFIGURED_MSG + "> but was <" + msg + ">",
               -1 != msg.indexOf(NO_TRANSITIONS_CONFIGURED_MSG));
      }
   }

   public void testFireEventBlockedTransions() throws Exception {
      FakeAction dontRun = new FakeAction();
      putJavaObjectInJs("dontRun", dontRun);
      putJavaObjectInJs("alwaysBlocksGuard", new AlwaysBlocksGuard());
      assertFalse(dontRun.invoked);

      // States
      addLine("var initState = new State('init state')");
      addLine("var finalState = new State('final state')");
      addLine("var anotherState = new State('another state')");
      addLine("var yetAnotherState = new State('yet another state')");

      // Action
      addLine("finalState.addEntryAction(dontRun)");

      // Model
      addLine("var model = new StateModel(initState, finalState)");
      addLine("model.addState(anotherState)");
      addLine("model.addState(yetAnotherState)");

      // Transitions
      addLine("var transition = new StateTransition(initState, finalState)");

      // Guards
      addLine("transition.setGuard(alwaysBlocksGuard)");
      addLine("test.assertEquals(initState, model.getState())");
      addLine("test.assertNotNull(transition.getGuard())");

      // trip the Wiring
      addLine("var machinable = new TestStateMachineable(model)");
      addLine("machinable.fire('GENERIC')");

      try {
         executeJSCode();
         fail();
      } catch (Exception e) {
         e.printStackTrace();
         String msg = e.getMessage();
         assertTrue("expected <" + NO_VALID_TRANSITIONS_MSG + "> but was <" + msg + ">", -1 != msg
               .indexOf(NO_VALID_TRANSITIONS_MSG));
      }
      assertFalse(dontRun.invoked);
   }

   public void testfireEvent() throws Exception {
      // States
      addLine("var initState = new State('init state')");
      addLine("var finalState = new State('final state')");
      addLine("var anotherState = new State('another state')");
      addLine("var yetAnotherState = new State('yet another state')");

      // Transitions
      addLine("var transition1 = new StateTransition(initState, finalState)");
      addLine("var transition2 = new StateTransition(initState, anotherState)");

      // Model
      addLine("var model = new StateModel(initState, finalState)");
      addLine("model.addState(anotherState)");
      addLine("model.addState(yetAnotherState)");
      // TODO: this is strange: we lose the initial state and the initial and final states are not
      // part of the state array?
      addLine("test.assertEquals(2, model.getStates().length)");

      // Guards
      Guard guard = new AlwaysBlocksGuard();
      putJavaObjectInJs("guard", guard);
      putJavaObjectInJs("notGuard", new NotGuard(guard));
      addLine("transition1.setGuard(notGuard)");
      addLine("transition2.setGuard(guard)");

      // trip the Wiring
      addLine("var machinable = new TestStateMachineable(model)");
      addLine("var state = model.getState()");
      addLine("test.assertEquals('model initial state ' + state + ' incorrect', initState, state)");
      addLine("machinable.fire('GENERIC')");
      addLine("state = model.getState()");
      addLine("test.assertEquals('model final state ' +state + ' incorrect', finalState, state)");

      executeJSCode();
   }

   public void testRunStateEntryActions() throws Exception {
      // Actions
      FakeAction wrapper = new FakeAction();
      putJavaObjectInJs("action", wrapper);
      assertFalse(wrapper.invoked);

      // States
      addLine("var initState = new State('init state')");
      addLine("var finalState = new State('final state')");
      addLine("finalState.addEntryAction(action)");
      addLine("test.assertEquals(1, finalState.getEntryActions().length)");
      addLine("var anotherState = new State('another state')");
      addLine("var yetAnotherState = new State('yet another state')");

      // Transitions
      addLine("var transition1 = new StateTransition(initState, finalState)");
      addLine("var transition2 = new StateTransition(initState, anotherState)");

      // Guards
      Guard guard = new AlwaysBlocksGuard();
      putJavaObjectInJs("guard", guard);
      putJavaObjectInJs("notGuard", new NotGuard(guard));
      addLine("transition1.setGuard(notGuard)");
      addLine("transition2.setGuard(guard)");

      // Model & Machine
      addLine("var model = new StateModel(initState, finalState)");
      addLine("model.addState(anotherState)");
      addLine("model.addState(yetAnotherState)");
      addLine("var machinable = new TestStateMachineable(model)");

      // trip the Wiring
      addLine("var state = model.getState()");
      addLine("test.assertEquals(initState, state)");
      addLine("machinable.fire('GENERIC')");
      addLine("state = model.getState()");
      addLine("test.assertEquals(finalState, state)");
      executeJSCode();
      assertTrue(wrapper.invoked);
   }

   public void testEventPastFinalStateThrowsException() throws Exception {
      // States
      addLine("var initState = new State('init state')");
      addLine("var finalState = new State('final state')");

      // Transitions
      addLine("var transition1 = new StateTransition(initState, finalState)");

      // Model
      addLine("var model = new StateModel(initState, finalState)");
      addLine("var machinable = new TestStateMachineable(model)");
      addLine("machinable.fire('GENERIC')");
      addLine("var state = model.getState()");
      addLine("test.assertEquals('wrong state: '+state, finalState, state)");
      addLine("machinable.fire('GENERIC')");
      try {
         executeJSCode();
         fail();
      } catch (Exception e) {
         assertTrue(-1 != e.getMessage().indexOf("Cannot move past final state in model"));
      }
   }

   public void testRunStateExitActions() throws Exception {
      // Actions
      FakeAction entryAction = new FakeAction();
      putJavaObjectInJs("entryAction", entryAction);
      assertFalse(entryAction.invoked);

      FakeAction exitAction = new FakeAction();
      putJavaObjectInJs("exitAction", exitAction);
      assertFalse(exitAction.invoked);

      // States
      addLine("var initState = new State('init state')");
      addLine("initState.addExitAction(exitAction)");
      addLine("test.assertEquals(1,initState.getExitActions().length)");
      addLine("var finalState = new State('final state')");
      addLine("finalState.addEntryAction(entryAction)");
      addLine("test.assertEquals(1, finalState.getEntryActions().length)");

      addLine("var anotherState = new State('another state')");
      addLine("var yetAnotherState = new State('yet another state')");

      // Transitions
      addLine("var transition1 = new StateTransition(initState, finalState)");
      addLine("var transition2 = new StateTransition(initState, anotherState)");

      // Guards
      Guard guard = new AlwaysBlocksGuard();
      putJavaObjectInJs("guard", guard);
      putJavaObjectInJs("notGuard", new NotGuard(guard));
      addLine("transition1.setGuard(notGuard)");
      addLine("transition2.setGuard(guard)");

      // Model
      addLine("var model = new StateModel(initState, finalState)");
      addLine("model.addState(anotherState)");
      addLine("model.addState(yetAnotherState)");
      addLine("test.assertEquals(initState, model.getState())");

      addLine("var machinable = new TestStateMachineable(model)");
      addLine("machinable.fire('GENERIC')");
      addLine("var state = model.getState()");
      addLine("test.assertEquals('wrong state: '+state, finalState, state)");

      executeJSCode();

      assertTrue(exitAction.invoked);
      assertTrue(entryAction.invoked);
   }

   public void testRunTransitionActions() throws Exception {
      // Actions
      FakeAction entryAction = new FakeAction();
      putJavaObjectInJs("entryAction", entryAction);
      assertFalse(entryAction.invoked);

      FakeAction exitAction = new FakeAction();
      putJavaObjectInJs("exitAction", exitAction);
      assertFalse(exitAction.invoked);

      FakeAction transitionAction = new FakeAction();
      putJavaObjectInJs("transitionAction", transitionAction);
      assertFalse(transitionAction.invoked);

      // States
      addLine("var initState = new State('init state')");
      addLine("initState.addExitAction(exitAction)");
      addLine("var finalState = new State('final state')");
      addLine("finalState.addEntryAction(entryAction)");

      addLine("var anotherState = new State('another state')");
      addLine("var yetAnotherState = new State('yet another state')");

      // Transitions
      addLine("var transition1 = new StateTransition(initState, finalState)");
      addLine("transition1.addAction(transitionAction)");
      addLine("test.assertEquals(1, transition1.getActions().length)");
      addLine("var transition2 = new StateTransition(initState, anotherState)");

      // Guards
      Guard guard = new AlwaysBlocksGuard();
      putJavaObjectInJs("guard", guard);
      putJavaObjectInJs("notGuard", new NotGuard(guard));
      addLine("transition1.setGuard(notGuard)");
      addLine("transition2.setGuard(guard)");

      // Model
      addLine("var model = new StateModel(initState, finalState)");
      addLine("model.addState(anotherState)");
      addLine("model.addState(yetAnotherState)");
      addLine("test.assertEquals(initState, model.getState())");

      addLine("var machinable = new TestStateMachineable(model)");
      addLine("machinable.fire('GENERIC')");
      addLine("var state = model.getState()");
      addLine("test.assertEquals('wrong state: '+state, finalState, state)");

      executeJSCode();

      assertTrue(entryAction.invoked);
      assertTrue(exitAction.invoked);
      assertTrue(transitionAction.invoked);
   }

   public void testEventTravelsAcrossCorrectTransition() throws Exception {
      // States
      addLine("var stateOne = new State('state one')");
      addLine("var stateTwo = new State('state two')");
      addLine("var finalState = new State('final')");

      // Transitions
      addLine("var transitionOne = new StateTransition(stateOne, stateTwo)");
      addLine("var transitionTwo = new StateTransition(stateOne, finalState, 'taxation')");

      addLine("var model = new StateModel(stateOne, finalState)");
      addLine("model.addState(stateTwo)");
      addLine("var machinable = new TestStateMachineable(model)");
      addLine("machinable.fire('taxation')");
      addLine("test.assertEquals(finalState.toString(), model.getCurrentState().toString())");
      executeJSCode();
   }

   public void testEventTravelsAcrossCorrectTransitionAndHandlesTransientState() throws Exception {
      // States
      addLine("var stateOne = new State('state one')");
      addLine("var stateTwo = new State('state two', true)");
      addLine("var boloneyState = new State('state baloney')");
      addLine("var finalState = new State('state final')");

      // Transitions
      addLine("var transitionTwo = new StateTransition(stateOne, boloneyState)");
      addLine("var transitionOne = new StateTransition(stateOne, stateTwo, 'taxation')");
      addLine("var transitionThree = new StateTransition(stateTwo, finalState)");

      // Model
      addLine("var model = new StateModel(stateOne, finalState)");
      addLine("model.addState(stateTwo)");
      addLine("model.addState(boloneyState)");
      addLine("new TestStateMachineable(model).fire('taxation')");

      addLine("test.assertEquals(finalState.toString(), model.getCurrentState().toString())");
      executeJSCode();
   }
}
