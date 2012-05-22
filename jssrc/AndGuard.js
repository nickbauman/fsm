/**
 *  $Id: AndGuard.js 2416 2007-11-08 17:35:41Z nick $
 */
function AndGuard(guardOne, guardTwo) {
	this.guards = Array();
	this.guards.push(guardOne);
	this.guards.push(guardTwo);
}

AndGuard.prototype = {
	
	canTransition: function() {
		var result = true;
		for (var i = 0; i < mGuards.length; i++) { 
			var guard = mGuards[i];
			if (!guard.canTransition()) {
				result = false;
				break;
			}
		}
		return result;
	},
	
	setGuards : function(pGuards) {
		this.guards = pGuards;
	},
	
	getGuards : function() {
		return this.guards;
	},
	
	addGuard : function(pGuard) {
		this.guards.push(pGuard);
	},
	
	/**
	 * This outputs in GraphViz 'dot' notation suitable for OmniGraffle and GraphViz
	 */
	toString : function() {
		var gs = 'AndGuard ';
		for(var i = 0; i < this.mGuards[i]; i++) {
			gs += this.mGuards[i].toString();
			if((i + 1) < this.mGuards.length) {
				gs += ' - ';
			}
		}
		return gs;
	}
};
