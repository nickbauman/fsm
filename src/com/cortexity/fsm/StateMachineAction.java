package com.cortexity.fsm;

import com.cortexity.fsm.exception.StateMachineException;

public interface StateMachineAction {
    void execute(StateMachineable<?> fsmAble) throws StateMachineException;
}
