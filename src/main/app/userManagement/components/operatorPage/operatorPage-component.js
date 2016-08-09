var module = require('main_module');

function Controller(Session, OperatorService, $state) {
  var vm = this;
  vm.operatorState = {};
  vm.error = undefined;

  vm.getVerificationInfo = getVerificationInfo;

  vm.$onInit = function() {
    OperatorService.getVerificationInfo(Session.getSession().userId).then(
    function (response) {
        vm.operatorState = response.data;
    },
    function (err) {
        vm.error = err.data.error_description;
    });
  };


  function getVerificationInfo() {
      OperatorService.getVerificationInfo(Session.getSession().userId).then(
          function (response) {
              vm.error = undefined;
              $state.go('root.operatorPage');
          },
          function (err) {
              vm.error = err.data.error_description;
          });
  }

}
Controller.$injcet = ['Session', 'OperatorService', '$state'];
module.component('operatorPage', {
    controller: Controller,
    templateUrl: require('./operatorPage.html')
});
