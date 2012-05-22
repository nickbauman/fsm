package com.cortexity.fsm;

public class FakeAction {
   boolean invoked = false;

   public void execute(Object object) {
      invoked = true;
   }
}
