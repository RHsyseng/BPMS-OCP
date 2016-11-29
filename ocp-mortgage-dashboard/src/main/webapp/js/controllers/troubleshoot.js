'use strict';

angular.module('app')
    .controller("TroubleshootCtrl", ['$scope', '$http', 'Constants', '$timeout',
        function ($scope, $http, Constants, $timeout) {

            $scope.consts = Constants;
            $scope.taskInProgress = false;

            fetchItems();

            function fetchItems() {
                $http.get("service/troubleshoot/list").success(function (data) {
                    $scope.items = data;
                });
            }
        }]);