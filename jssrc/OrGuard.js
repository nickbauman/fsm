/**
 *  $Id: OrGuard.js 2416 2007-11-08 17:35:41Z nick $
 */
function OrGuard(firstGuard, secondGuard) {
	this.mGuards.push(firstGuard);
	this.mGuards.push(secondGuard);
}

OrGuard.prototype = {

	setGuards : function(pGuards) {
		this.mGuards = pGuards;
	},
	
	getGuards : function() {
		return this.mGuards;
	},

	addOrCondition : function(aGuard) {
		this.mGuards.push(aGuard);
	},

	canTransition : function() {
		var canTran = false;
		for (var i = 0; i < mGuards.length; i++) {
			if (mGuards[i].canTransition()) {
				canTran = true;
				break;
			}
		}
		return canTran;
	},
	
	toString : function() {
		var gs = 'OrGuard ';
		for(var i = 0; i < this.mGuards[i]; i++) {
			gs += this.mGuards[i].toString();
			if((i + 1) < this.mGuards.length) {
				gs += ' - ';
			}
		}
		return gs;
	}
};
