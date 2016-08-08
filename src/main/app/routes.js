var angular = require('angular');
var module = angular.module('AngularSpringRestDemo');

module.config(function($stateProvider, $urlRouterProvider) {

  // For any unmatched url, redirect to /
  $urlRouterProvider.otherwise("/login");
  //
  // Now set up the states
  $stateProvider
    .state('root', {
      template: "<main></main>",
    })
    .state('root.home', {
      url: '/',
      template: "<h4>This is home</h4>",
    })
    .state('root.Login', {
      url: "/login",
      template: "<login></login>",
      data: {
        isPublic: true
      },
      params: {
        username: undefined
      }
    })
    .state('root.itemList', {
      url: "/items",
      template: "<item-list></item-list>"
    })
    .state('root.itemNew', {
      url: "/items/new",
      template: "<item-new></item-new>"
    })
    .state('root.ItemDetails', {
      url: "/items/:id",
      template: "<item-details></item-details>"
    })
    .state('root.newUser', {
      url: "/newUser",
      template: "<new-user></new-user>",
      data: {
        isPublic: true
      }
    })
    .state('root.customerPage', {
      url: "/customerPage",
      template: "<customer-page></customer-page>",
      data : {
        role : "ROLE_CUSTOMER"
      }
    })
    .state('root.accountInformation', {
      url: "/accountInformation",
      template: "<account-information></account-information>",
    })
    .state('root.operatorPage', {
      url: "/operatorPage",
      template: "<operator-page></operator-page>",
      data : {
        role : "ROLE_OPERATOR"
      }
    })
    .state('root.adminPage', {
      url: "/adminPage",
      template: "<admin-page></admin-page>",
      data : {
        role: "ROLE_ADMIN"
      }
    })
    .state('root.userList', {
      url: "/userList",
      template: "<user-list></user-list>",
      data: {
        role: "ROLE_ADMIN"
      }
    });
});

module.run(['$transitions', 'Session', '$state', function($transitions, Session, $state) {

  Session.initHttp();

  $transitions.onStart(
    {
      to: function (state) { return !state.data || !state.data.isPublic; }
    },
    function () {
      console.log("back to login");
      if (!Session.isSessionActive()) {

        return $state.target("root.Login");
      }
    });

  $transitions.onStart(
  {
    to: function (state) {
       return state.name == "root.Login" && Session.isSessionActive();
     }
  },
  function () {
    console.log("TODO: fix this errror");
      return $state.go("root.home");

  });

  $transitions.onStart(
   {
     to: function (state) {
       return state.data && state.data.role && state.data.role.indexOf("ROLE_ADMIN") >= 0;
     }
   },
   function () {
      if (Session.getSession().authorities[0] != "ROLE_ADMIN") {
        return $state.target('root.home');
      }
   });

   $transitions.onStart(
    {
      to: function (state) {
        return state.data && state.data.role && state.data.role.indexOf("ROLE_CUSTOMER") >= 0;
      }
    },
    function () {
       if (Session.getSession().authorities[0] != "ROLE_CUSTOMER") {
         return $state.target('root.home');
       }
    });

    $transitions.onStart(
     {
       to: function (state) {
         return state.data && state.data.role && state.data.role.indexOf("ROLE_OPERATOR") >= 0;
       }
     },
     function () {
        if (Session.getSession().authorities[0] != "ROLE_OPERATOR") {
          return $state.target('root.home');
        }
     });


}]);
