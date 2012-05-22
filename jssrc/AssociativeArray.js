function AssociativeArray() {
	
}

/**
 * $Id: AssociativeArray.js 492 2007-10-17 16:27:40Z nick $
 * JavaScript can add properties[to_objects] so easily, but it doesn't work well
 * for iterating over them along with function objects so we have this class.
 */
AssociativeArray.prototype = {
	size : function() {
		var size = 0;
		for(property in this) {
			if(typeof this[property] == 'function') {
				continue;
			}
			size++;
		}
		return size;
	},
	
	push : function(value) {
		this[this.size()] = value;
	},
	
	values : function() {
		var vals = Array();
		for(property in this) {
			var pr = this[property];
			if(typeof pr == 'function') {
				continue;
			}
			vals.push(pr);
		}
		return vals;
	},
	
	keys : function() {
		var keyz = Array();
		for (property in this) {
			var pr = this[property];
			if(typeof pr == 'function') {
				continue;
			}
			keyz.push(property);
		}
		return keyz;
	},
	
	toString : function() {
		return '[AssociativeArray]:'+this.values().toSource();
	}
};