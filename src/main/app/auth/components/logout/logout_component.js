var module = require('main_module');


function Controller($state, Session, AuthService, $http) {

    var vm = this;

    vm.logout = logout;
    vm.accountInfo = accountInfo;
    vm.isLogoutVisible = isLogoutVisible;
    vm.isNavigationVisible = isNavigationVisible;

    function isLogoutVisible() {
        return Session.isSessionActive();
    }

    function isNavigationVisible() {
      if($state.current.name != "root.Login"){
          return true;
      } else {
          return false;
      }
    }

    function logout() {
        AuthService.logout().then(
            function() {
                $http.defaults.headers.common.Authorization=undefined;
                Session.invalidate();
                $state.go('root.Login');
            });
    }

    function accountInfo() {
        $state.go('root.accountInformation');
    }
}


Controller.$inject = ['$state', 'Session', 'AuthService', '$http'];

module.component('logout', {
    controller: Controller,
    templateUrl: require('./logout.html')
});
