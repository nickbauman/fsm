package com.cortexity.fsm;

public class JSTransitionTest extends JavaScriptTestCase {

   public JSTransitionTest(String name) {
      super(name);
   }

   public void testTransitionsInitialization() throws Exception {
      addLine("var from = new State('one state')");
      addLine("var to = new State('two state')");
      addLine("var transition = new StateTransition(from, to)");
      addLine("test.assertNotNull(transition.getTransitionName())");
      addLine("test.assertEquals(\"onestatetwostate\", transition.getTransitionName())");
      addLine("test.assertEquals(from, transition.getFrom())");
      addLine("test.assertEquals(to, transition.getTo())");
      executeJSCode();
   }

   public void testTransitionsThatListenForMultipleEvents() throws Exception {
      addLine("var from = new State('one state')");
      addLine("var to = new State('two state')");
      addLine("var three = new State('three state')");
      addLine("var transition = new StateTransition(from, to, 'foo')");
      addLine("transition.addEvent('bar')");
      addLine("var model = new StateModel(from, to)");
      addLine("model.addState(three)");
      addLine("var transition = new StateTransition(to, three, 'schnabs')");
      addLine("test.assertNotNull('model was null', model.getState())");
      addLine("test.assertEquals(from, model.getState())");
      addLine("var machinable = new TestStateMachineable(model)");
      addLine("machinable.fire('GENERIC')");
      try {
         executeJSCode();
         fail();
      } catch (Exception e) {
         assertEquals("No transitions configured out of 'one state' for event 'GENERIC' (jssrc/StateMachine.js#12)", e.getMessage());
      }
      addLine("machinable.fire('bar')");
      addLine("test.assertEquals(to, model.getState())");
      addLine("machinable.fire('schnabs')");
      addLine("test.assertEquals(three, model.getState())");
   }

   public void testAddGuard() throws Exception {
      addLine("var from = new State('from state')");
      addLine("var to = new State('to state')");
      addLine("var transition = new StateTransition(from, to)");
      Guard guard = new AlwaysBlocksGuard();
      putJavaObjectInJs("guard", guard);
      addLine("transition.setGuard(guard)");
      addLine("var goGuard = transition.getGuard()");
      addLine("test.assertNotNull(goGuard)");
      addLine("test.assertEquals(guard, goGuard)");
      executeJSCode();
   }

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
}
