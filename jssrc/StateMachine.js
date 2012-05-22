function StateMachine() {
}

StateMachine.prototype = {
	fire : function(stateMachinable, stateMachineEvent) {
		var model = stateMachinable.getStateModel();
		if (model.getCurrentState() === (model.getFinalState())) {
			throw "Cannot move past final state in model";
		}
	var trans = model.getCurrentState().getTransitions(stateMachineEvent);
	if (null == trans || trans.length == 0) {
		throw "No transitions configured out of '" + model.getCurrentState().stateName + "' for event '" + stateMachineEvent + "'";
	} 
	this.executeActions(model.getCurrentState().getExitActions(), stateMachinable);
	var possibleStates = Array();
	var correctTransition = null;
	for (var i = 0; i < trans.length; i++) {
		var guard = trans[i].getGuard();
		if (null != guard) {
			var canTransition = guard.canTransition(stateMachinable);
			if (canTransition) {
				correctTransition = trans[i];
				possibleStates.push(trans[i].getTo());
			}
		} else {
			correctTransition = trans[i];
			possibleStates.push(trans[i].getTo());
		}
	}
	if (possibleStates.length == 1) {
		// be careful with combining entry and exit actions: you can change things 
		// that can't be unchanged because an exit action can run and then and 
		// entry action can fail, leaving your data inconsistent.
		this.executeActions(correctTransition.getActions(), stateMachinable);
		model.setState(possibleStates[0]);
		if( typeof dump == "function" ) {
			dump('[StateMachine]: transitioned to '+possibleStates[0].stateName+'\n');
		}
		this.executeActions(model.getCurrentState().getEntryActions(), stateMachinable);
		if (model.getCurrentState().isTemporary()) {
			this.fire(stateMachinable, "GENERIC");
		}
	} else if (possibleStates.length == 0) {
		throw "No valid transitions found for '" + model.getCurrentState().stateName + "'";
	} else if (possibleStates.length > 1) {
		var statesFound = '';
		for(var i = 0; i < possibleStates.length; i ++) {
			statesFound += possibleStates[i].stateName + ', ';
		}
		throw "Cannot find descrete transition. Multiple states are possible: " + statesFound;
	}
	},
	
	executeActions : function(actions, fsmAble) {
		for (var i = 0; i < actions.length; i++) {
			actions[i].execute(fsmAble);
		}
	},
	
	fireGeneric : function(stateMachinable) {
		this.fire(stateMachinable,"GENERIC");
	},
	
	toString : function() {
		return '[StateMachine]:';
	}
};
