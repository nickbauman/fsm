package com.cortexity.fsm;

final class AlwaysBlocksGuard implements Guard {
   AlwaysBlocksGuard() {
      // this is meant to be used only as a test component
   }

   public boolean canTransition() {
      return false;
   }

   public boolean canTransition(Object o) {
      return false;
   }
}