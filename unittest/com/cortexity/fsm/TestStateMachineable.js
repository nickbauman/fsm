function TestStateMachineable(pStateModel) {
  this.stateModel = pStateModel;
  this.stateMachine = new StateMachine();
}
      
TestStateMachineable.prototype = {
  fire : function(stateMachineEvent) {
     this.stateMachine.fire(this, stateMachineEvent);
  },
  
  getStateModel : function() {
     return this.stateModel;
  }
};