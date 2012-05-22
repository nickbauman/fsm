package com.cortexity.fsm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class OrGuard implements Guard {
   private final Collection<Guard> guards = new HashSet<Guard>();

   public OrGuard(Guard first, Guard second) {
      guards.add(first);
      guards.add(second);
   }

   public void addOrCondition(Guard guard) {
      guards.add(guard);
   }

   public boolean canTransition() {
      boolean canTran = false;
      for (Iterator<Guard> iter = guards.iterator(); iter.hasNext();) {
         Guard guard = iter.next();
         if (guard.canTransition()) {
            canTran = true;
            break;
         }
      }
      return canTran;
   }

   public boolean canTransition(Object obj) {
      return this.canTransition();
   }
}
