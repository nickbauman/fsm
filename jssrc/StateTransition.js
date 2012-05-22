/**
 *  $Id: StateTransition.js 2420 2007-11-10 05:15:52Z nick $
 */
function StateTransition(fromState, toState, pEvents) {
	this.origin = fromState;
	this.destination = toState;
	this.transitionName = this.getTransitionName();
	this.mActions = Array();
	this.mEvents = new Set();
	this.mGuard = new Object();
	this.mGuard.canTransition = function() {
		return true;
	};
	this.mGuard.toString = function() {
		return 'DEFAULT';
	};
	if(pEvents == null) {
		this.mEvents.add('GENERIC');
	} else if(typeof pEvents == 'string') {
		this.mEvents.add(pEvents);
	} else if(typeof pEvents == 'array' || pEvents.constructor.name == 'Set') {
		this.mEvents = new Set(pEvents);
	} else {
		throw 'Cannot handle event type of '+pEvents;
	}
	fromState.addTransition(this);
}

StateTransition.prototype = {
	
	addEvent : function(eventString) {
		this.mEvents.add(eventString);
	},
	
	setGuard : function(guard) {
		if(null == guard || undefined == guard['canTransition']) {
			throw 'Guard '+guard+' must have a canTransition function that returns a boolean';
		}
		this.mGuard = guard;
	},
	
	addAction : function(transitionAction) {
		this.mActions.push(transitionAction);
	},

	getFrom : function() {
		return this.origin;
	},

	getTo : function() {
		return this.destination;
	},

	getGuard : function() {
		return this.mGuard;
	},

	getActions : function() {
		return this.mActions;
	},
	
	getEvents : function() {
		return this.mEvents;
	},
	
	/**
	 * This outputs in GraphViz 'dot' notation suitable for OmniGraffle and GraphViz
	 */
	toString : function() {
		var originStateName = this.origin.stateName.replace(/\s/g, '');
		var destinationStateName = this.destination.stateName.replace(/\s/g, '');
		var digraph = "";
		var simpleTransition = originStateName + '->' + destinationStateName;
		var complexTransitionEdgeName = originStateName + "To" + destinationStateName +"Edge";
		var complexTransitionTo = originStateName + '->' + complexTransitionEdgeName + "[arrowhead=odot];\n"; 
		var complexTransitionFrom = complexTransitionEdgeName + '->' + destinationStateName + "[arrowhead=normal];\n"; 
		var trans = this.origin.getAllTransitions();
		var events = Array();
		for(var i = 0; i < trans.length; i++) {
			if(trans[i] == this) {
				var tEvents = trans[i].getEvents().toArray();
				for(var q = 0; q < tEvents.length; q++) {
					events.push("on " + tEvents[q]);
				}
				break;
			}
		}
		var transitionRecord = 'complexTransitionEdgeName[width=0,height=0,shape=record,color=black,label=""]';
		if( 'DEFAULT' != this.getGuard().toString() || this.getFrom().getExitActions().length || 
			this.getTo().getEntryActions().length || this.getActions().length || events.length) {
			var tranNodeName = this.getTransitionName() + 'Transition';
			transitionRecord = complexTransitionEdgeName + '[width=0,height=0,shape=record,color=black,label="{';
			if (events.length) {
				transitionRecord += "- Events -|";
				for (var i = 0; i < events.length; i++) {
					transitionRecord += events[i] + "\\l|";
				}
			}
			if('DEFAULT' != this.getGuard().toString()) {
				transitionRecord += '- Guards -|' + this.getGuard().toString() +'\\l';
			}
			if(this.getFrom().getExitActions().length) {
				transitionRecord += '|- Exit Actions -|';
				for(var i = 0; i < this.getFrom().getExitActions().length; i++) {
					transitionRecord += this.getFrom().getExitActions()[i].toString() + '\\l|';
				}
			}
			if(this.getActions().length) {
				transitionRecord += '|- Transition Actions -|';
				for(var i = 0; i < this.getActions().length; i++) {
					transitionRecord += this.getActions()[i].toString() + '\\l|';
				}
			}
			if(this.getTo().getEntryActions().length) {
				transitionRecord += '|- Entry Actions -|';
				for(var i = 0; i < this.getTo().getEntryActions().length; i++) {
					transitionRecord += this.getTo().getEntryActions()[i].toString() + '\\l|';
				}
			}
			digraph += complexTransitionTo + complexTransitionFrom;
			digraph +=  transitionRecord;
			digraph += '}",fontsize=9];\n';
		} else {
			digraph += simpleTransition; // + "[shape=circle]";
		}
		return digraph;
	},
	
	getTransitionName : function() {
		return this.origin.stateName.replace(/\s/g, '') + this.destination.stateName.replace(/\s/g, '');
	}
	
};
