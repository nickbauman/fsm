package com.cortexity.fsm;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AndGuard implements Guard, Serializable {

    private static final long serialVersionUID = -6009086582758694605L;
    
    private final Set<Guard> guards = new HashSet<Guard>();

    public AndGuard(Guard one, Guard two) {
        guards.add(one);
        guards.add(two);
    }

    public void addAddCondition(Guard guard) {
        guards.add(guard);
    }

    public boolean canTransition() {
        boolean result = true;
        for (Iterator<Guard> iter = guards.iterator(); iter.hasNext();) {
            Guard guard = iter.next();
            if (!guard.canTransition()) {
                result = false;
                break;
            }
        }
        return result;
    }

   public boolean canTransition(Object obj) {
      return this.canTransition();
   }

}
