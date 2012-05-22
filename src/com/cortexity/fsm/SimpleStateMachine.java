package com.cortexity.fsm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.cortexity.fsm.exception.MultipleValidTransitionsExistException;
import com.cortexity.fsm.exception.StateMachineException;
import com.cortexity.fsm.exception.StateModelEndedException;
import com.cortexity.fsm.exception.StateTransitionException;

public class SimpleStateMachine<T> implements StateMachine<T> {

   public void fire(StateMachineable<T> object, StateMachineEvent event)
         throws StateMachineException {
      StateModel model = object.getStateModel();
      if (model.getCurrentState().equals(model.getFinalState())) {
         throw new StateModelEndedException("Cannot move past final state in model");
      }
      Collection<StateTransition> trans = model.getCurrentState().getTransitions(event);
      if (trans.size() == 0) {
         throw new StateTransitionException("No transitions configured out of '" + model.getCurrentState() + "' for event '" + event + "'");
      }
      Set<State> possibleStates = new HashSet<State>();
      StateTransition correctTransition = null;
      for (Iterator<StateTransition> iter = trans.iterator(); iter.hasNext();) {
         StateTransition tran = iter.next();
         if (null != tran.getGuard()) {
               if (tran.getGuard().canTransition()) {
                  correctTransition = tran;
                  possibleStates.add(tran.getTo());
               }
         } else {
            correctTransition = tran;
            possibleStates.add(tran.getTo());
         }
      }
      if (possibleStates.size() == 1) {
         // be careful with combining entry and exit actions: you can change things
         // that can't be unchanged because an exit action can run and then and
         // entry action can fail, leaving your data inconsistent.
         executeActions(model.getCurrentState().getExitActions(), object);
         executeActions(correctTransition.getActions(), object);
         model.setState(possibleStates.iterator().next());
         executeActions(model.getCurrentState().getEntryActions(), object);
         if (model.getCurrentState().isTemporary()) {
            fire(object, StateMachineEvent.GENERIC);
         }
      } else if (possibleStates.size() == 0) {
         throw new StateTransitionException("No valid transitions found for '"
               + model.getCurrentState() + '\'');
      } else if (possibleStates.size() > 1) {
         throw new MultipleValidTransitionsExistException(possibleStates);
      }
   }

   private void executeActions(Collection<StateMachineAction> actions, StateMachineable<T> fsmAble)
         throws StateMachineException {
      for (Iterator<StateMachineAction> iter = actions.iterator(); iter.hasNext();) {
         iter.next().execute(fsmAble);
      }
   }

   public void fire(StateMachineable<T> object) throws StateMachineException {
      fire(object, StateMachineEvent.GENERIC);
   }
}
