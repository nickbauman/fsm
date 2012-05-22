function Set(array) {
	this.__a = Array();
	if(array) {
		if(array.constructor.name == 'Array') {
			for(var i = 0; i < array.length; i++) {
				this.add(array[i]);
			}
		} else if (array.constructor.name == 'Set') {
			this.addAll(array);
		} else {
			throw 'Cannot coerce '+ array.constructor.name + ' into Set'; 
		}
	}
}

/**
 * $Id: Set.js 2422 2007-11-14 02:45:49Z nick $
 * This is a JavaScript set object, which iplements what the java.util.Set does (except Iterator);
 */
Set.prototype = {

	size : function() {
		return this.__a.length;
	},

	isEmpty : function () {
		return this.size() == 0;
	},

	contains : function(object) {
		var result = false;
		for(var i = 0; i < this.size(); i++) {
			if(this.__a[i] == object) {
				result = true;
				break;
			}
		}
		return result;
	},

	toArray : function() {
		return this.__a;
	},

	add : function(object) {
		if(this.contains(object)) {
			return false;
		}
		this.__a.push(object);
		return true;
	},

	remove : function(object) {
		var ar = Array();
		for(var i = 0; i < this.size(); i++) {
			if(this.__a[i] != object) {
				ar.push(this.__a[i]);
			}
		}
		this.__a = ar;
	},

	containsAll : function(set) {
		for(var i = 0; i < this.__a.length; i++) {
			if(! set.contains(this.__a[i])) {
				return false;
			}
		}
		return true;
	},

	addAll : function(set) {
		var array = set.toArray();
		for(var i = 0; i < array.length; i++) {
			this.add(array[i]);
		}
	},

	retainAll : function(set) {
		var other = set.toArray();
		for(var i = 0; i < other.length; i++) {
			if(! this.contains(other[i])) {
				this.remove(other[i]);
			}
		}
	},

	removeAll : function(set) {
		var other = set.toArray();
		for(var i = 0; i < other.length; i++) {
			if(this.contains(other[i])) {
				this.remove(other[i]);
			}
		}
	},

	clear : function() {
		this.__a = Array();
	}
}