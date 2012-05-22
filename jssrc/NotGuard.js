/**
 *  $Id: NotGuard.js 2416 2007-11-08 17:35:41Z nick $
 */
function NotGuard(aGuard) {
	this.mGuard = aGuard;
}

NotGuard.prototype = {
	
	canTransition: function(machine) {
		return ! (this.mGuard.canTransition());
	},
	
	setGuard : function(pGuard) {
		this.mGuard = pGuard;
	},
	
	getGuard : function() {
		return this.mGuard;
	},
	
	/**
	 * This outputs in GraphViz 'dot' notation suitable for OmniGraffle and GraphViz
	 */
	toString : function() {
    	return 'NOT '+ this.mGuard;
    }
};