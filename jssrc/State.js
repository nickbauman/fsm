/**
 *  $Id: State.js 2420 2007-11-10 05:15:52Z nick $
 */
function State(string, temporary) {
	this.stateName = string;
	this.temporary = temporary;
	this.entryActions = Array();
	this.exitActions = Array();
	this.transitions = new Set();
	if(temporary == null) {
		this.temporary = false;
	}
}

State.prototype = {
	
	addTransition : function(pTransition) {
		if(pTransition.getFrom() !== this) {
			throw 'Programming error: Transitions supposed origin: '+ pTransition.getFrom() + ' is not the same as ' + this;
		}
		this.transitions.add(pTransition);
	},

	isTemporary : function() {
		return this.temporary;
	},

	addEntryAction : function(action) {
		this.entryActions.push(action);
	},
	
	addExitAction : function(action) {
		this.exitActions.push(action);
	},

	/**
	 * This outputs in GraphViz 'dot' notation suitable for OmniGraffle and GraphViz
	 */
	toString : function() {
		var diagraph = this.stateName.replace(/\s/g, '') + '[shape=';
		if(this.temporary) {
			diagraph += 'circle,';
		} else {
			diagraph += 'doublecircle,';
		}
		diagraph += 'color=black,'
		var label = this.stateName.replace(/\s/g, '\\n')
		diagraph += 'label="'+label+'"];\n';
		var tr = this.transitions.toArray();
		for(var i = 0; i < tr.length; i++) {
			diagraph += tr[i];
		}
		diagraph += '\n';
		return diagraph;
	},

	getTransitions : function(event) {
		var eventTransitions = Array();
		var ta = this.transitions.toArray();
		for(var i = 0; i < ta.length; i++) {
			if(ta[i].getEvents().contains(event)) {
				eventTransitions.push(ta[i]);
			}
		}
		return eventTransitions;
	},

	getAllTransitions : function() {
		return this.transitions.toArray();
	},

	getEntryActions : function() {
		return this.entryActions;	
	},

	getExitActions : function() {
		return this.exitActions;
	},

	getStateName : function() {
		return this.stateName;	
	}
};
