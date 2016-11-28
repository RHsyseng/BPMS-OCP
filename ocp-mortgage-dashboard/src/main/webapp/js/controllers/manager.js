'use strict';

angular.module('app')
    .controller("ManagerCtrl", ['$scope', '$http', function ($scope, $http) {

        $scope.role = "manager";
    }]);