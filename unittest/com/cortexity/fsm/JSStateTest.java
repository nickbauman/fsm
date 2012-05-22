package com.cortexity.fsm;

public class JSStateTest extends JavaScriptTestCase {
   public JSStateTest(String name) {
      super(name);
   }

   public void testAddingTransitions() throws Exception {
      addLine("var state = new State('a state')");
      addLine("test.assertEquals('a state', state.getStateName())");
      addLine("var transition = new StateTransition(state, state)");
      addLine("var transitions = state.getTransitions('GENERIC')");
      addLine("test.assertNotNull(transitions)");
      addLine("test.assertEquals(1, transitions.length)");
      addLine("test.assertEquals(transition, transitions[0])");
      executeJSCode();
   }

   @Override
   protected void doSetUp() throws Exception {
      executeScriptFile("jssrc/AssociativeArray.js");
      executeScriptFile("jssrc/State.js");
      executeScriptFile("jssrc/StateTransition.js");
      executeScriptFile("jssrc/StateModel.js");
      executeScriptFile("jssrc/Set.js");
      executeScriptFile("jssrc/StateMachine.js");
      executeScriptFile("unittest/com/cortexity/fsm/TestStateMachineable.js");
   }

   @Override
   protected void doTearDown() throws Exception {
   }
}
