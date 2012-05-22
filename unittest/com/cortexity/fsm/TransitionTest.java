package com.cortexity.fsm;

import junit.framework.*;

public class TransitionTest extends TestCase {

   public void testTransitionsInitialization() throws Exception {
      State from = new State("from state");
      State to = new State("to state");
      StateTransition transition = new StateTransition(from, to);
      assertNotNull(transition.getName());
      assertEquals("From 'from state' To 'to state'", transition.getName());
      assertEquals(from, transition.getFrom());
      assertEquals(to, transition.getTo());
   }

   public void testAddGuard() throws Exception {
      State from = new State("from state");
      State to = new State("to state");
      StateTransition transition = new StateTransition(from, to);
      Guard guard = new Guard() {
         public boolean canTransition() {
            return false;
         }

         public boolean canTransition(Object o) {
            return false;
         }
      };
      transition.setGuard(guard);
      Guard goGuard = transition.getGuard();
      assertNotNull(goGuard);
      assertEquals(guard, goGuard);
   }
}
