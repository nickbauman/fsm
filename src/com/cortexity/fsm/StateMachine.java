package com.cortexity.fsm;

import com.cortexity.fsm.exception.*;

public interface StateMachine<T> {

   void fire(StateMachineable<T> object, StateMachineEvent event) throws StateMachineException;

   void fire(StateMachineable<T> object) throws StateMachineException;

}