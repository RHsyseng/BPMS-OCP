'use strict';

angular.module('app')
    .controller("HomeCtrl", ['$scope', '$http', 'Constants', function ($scope, $http, Constants) {

        $scope.consts = Constants;

        $http.get("service/home/processes").success(function (data) {
            $scope.processes = data;
        });

        $http.get("service/home/running").success(function (data) {
            $scope.running = data;
        });

        $http.get("service/home/workitems").success(function (data) {
            $scope.workItems = data;
        });
    }]);