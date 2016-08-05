var module = require('main_module');

function Service($http, $q) {

  this.create = function(user) {
      return $http.post('/api/users', user);
  };

  this.get = function(id) {
    return $http.get('/api/users').then(
      function(response) {
        return response.data.filter(function(user) {
          return user.id === id;
        })[0];
      }
    );
  };

  this.all = function() {
    return $http.get('/api/users');
  };

  this.update = function(user) {
    var d = $q.defer();
    d.resolve(user);
    return d.promise;
  };

  // this.create = function(user) {
  //   var d = $q.defer();
  //   d.resolve(user);
  //   return d.promise;
  // };

  this.remove = function(id) {
    var d = $q.defer();
    d.resolve(id);
    return d.promise;
  };

  this.put = function(user) {
      return $http.put('/api/users/' + user.id, user);
  };

}

Service.$inject = ['$http', '$q'];
module.service('UserServiceImpl', Service);
