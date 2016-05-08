angular.module('hello', [])
    .controller('home', function($http,$scope) {
        //var self = this;
        $http.get('/resource/').then(function(response) {
            console.debug(response.data);
            //console.debug(self);
            $scope.greeting = response.data;
        })
    });