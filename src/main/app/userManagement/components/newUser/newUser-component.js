var module = require('main_module');

function Controller($state, UserServiceImpl) {
  var vm = this;

  vm.user = {};

  vm.create = create;
  vm.logout = logout;
  vm.errors = [];
  vm.myCustomForm = {};
  vm.customValidate = customValidate;
  vm.validate = validate;

  function create() {
    console.log("creating new user");
    UserServiceImpl.create(vm.user).then(
        function () {
            console.log(vm.user.userName);
            $state.go('root.Login', { username: vm.user.userName});
        },
        function (err) {
            if (err.status === 400) {
                vm.errors = err.data;
            } else {
                console.log('Error', err);
            }
        }
    );
  }


  function logout() {
      console.log("loggin out");
      $state.go('root.Login', { username: vm.user.userName});

  }

  function customValidate() {

      return true;
  }

  function validate(){
    return vm.myCustomForm.$valid && customValidate();

  }

}

Controller.$inject = ['$state', 'UserServiceImpl'];
module.component('newUser', {
    controller: Controller,
    templateUrl: require('./newUser.html')
});
