package com.cortexity.fsm;

public class NotGuard implements Guard {
   private final Guard guard;

   public NotGuard(Guard guard) {
      this.guard = guard;
   }

   public boolean canTransition() {
      return this.canTransition((Object)null);
   }

   public boolean canTransition(Object obj) {
      return !guard.canTransition();
   }

}
