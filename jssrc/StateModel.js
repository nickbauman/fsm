/**
 *  $Id: StateModel.js 2416 2007-11-08 17:35:41Z nick $
 */
function StateModel(initState, endState) {
	this.initState = initState;
	this.currentState = initState;
	this.finalState = endState;
	this.states = Array();
}

StateModel.prototype = {

	addState : function(state) {
		for(var i = 0; i <this.states.length; i++) {
			if(state.stateName == this.states[i].stateName) {
				throw 'Programming error: state "'+state.stateName+'" already exists in this model';
			}
		}
		this.states.push(state);
	},

	getStates : function() {
		return this.states;
	},

	getState : function() {
		return this.currentState;
	},

	getFinalState : function() {
		return this.finalState;
	},

	getCurrentState : function() {
		return this.currentState;
	},

	setState : function(state) {
		this.currentState = state;
	},
	
	/**
	 * This outputs in GraphViz 'dot' notation suitable for OmniGraffle and GraphViz
	 */
	toString : function() {
		var diagraph = 'digraph state_model{\narrowhead=normal;\nnode[shape=polygon,sides=8,color=red,regular=true];\n' + this.initState ;
		for(var i = 0; i < this.states.length; i++) {
			diagraph += this.states[i].toString();
		}
		diagraph += this.finalState;
		diagraph += '}\n';
		return diagraph;
	}
};

