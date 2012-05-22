package com.cortexity.fsm;

import com.cortexity.fsm.exception.StateMachineException;

public interface StateMachineable<T> {

    /**
     * A way for a machine to run state model instructions
     */
    StateModel getStateModel();

    /**
     * Allows delegation to a corresponding underlying state model.
     */
    void fire(StateMachineEvent event) throws StateMachineException;
}
