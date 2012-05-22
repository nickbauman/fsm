package com.cortexity.fsm;

import junit.framework.Test;
import junit.framework.TestSuite;

public final class AllTests {

    private AllTests() {
      // utility Singleton
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("fsm test");
        suite.addTestSuite(StateMachineTest.class);
        suite.addTestSuite(StateTest.class);
        suite.addTestSuite(TransitionTest.class);
        suite.addTestSuite(JSStateMachineTest.class);
        suite.addTestSuite(JSStateTest.class);
        suite.addTestSuite(JSTransitionTest.class);
        suite.addTestSuite(SetTest.class);
        return suite;
    }
}
