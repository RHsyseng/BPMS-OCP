'use strict';

angular.module('app')
    .controller("FinancialReviewCtrl", ['$scope', '$http', 'Constants', '$location', '$timeout',
        function ($scope, $http, Constants, $location, $timeout) {

            $scope.consts = Constants;
            $scope.taskInProgress = false;

            fetchTasks();

            function fetchTasks() {
                $http.get("service/finance/tasks").success(function (data) {
                    $scope.tasks = data;
                });
            }

            $scope.claimTask = function (taskId) {
                $http.post("service/finance/claimTask", taskId).then(
                    function (data) {
                        $scope.successMessage = "Task claimed Successfully";
                        $scope.successVisible = true;
                        fetchTasks();
                        $timeout(function () {
                            $scope.successVisible = false;
                            $scope.successMessage = "";
                        }, 2000);
                    },
                    function (data) {
                        $scope.errorMessage = "Error occurred while attempting to claim task";
                        $scope.errorVisible = true;
                        fetchTasks();
                        console.log("ERROR: " + JSON.stringify({data: data}));
                        $timeout(function () {
                            $scope.errorVisible = false;
                            $scope.errorMessage = "";
                        }, 2000);
                    });
            };

            $scope.releaseTask = function (taskId) {
                $http.post("service/finance/releaseTask", taskId).then(
                    function (data) {
                        $scope.successMessage = "Task released Successfully";
                        $scope.successVisible = true;
                        fetchTasks();
                        $timeout(function () {
                            $scope.successVisible = false;
                            $scope.successMessage = "";
                        }, 2000);
                    },
                    function (data) {
                        $scope.errorMessage = "Error occurred while attempting to release task";
                        $scope.errorVisible = true;
                        fetchTasks();
                        console.log("ERROR: " + JSON.stringify({data: data}));
                        $timeout(function () {
                            $scope.errorVisible = false;
                            $scope.errorMessage = "";
                        }, 2000);
                    });
            };

            $scope.startTask = function (taskId) {
                $http.post("service/finance/startTask", taskId).then(
                    function (data) {
                        $scope.successMessage = "Task started successfully";
                        $scope.successVisible = true;
                        $scope.taskInProgress = true;
                        $scope.taskInProgressId = taskId;

                        var $app = data.data.taskInputApplication;
                        $scope.propertyPrice = $app.property.price;
                        $scope.appraisedValue = $app.appraisal ? $app.appraisal.value : "";
                        $scope.downPayment = $app.downPayment;
                        $scope.amortization = $app.amortization;
                        $scope.apr = $app.apr;
                        $scope.creditScore = $app.applicant.creditScore;
                        $scope.applicantIncome = $app.applicant.income;

                        fetchTasks();
                        $timeout(function () {
                            $scope.successVisible = false;
                            $scope.successMessage = "";
                        }, 2000);
                    },
                    function (data) {
                        $scope.errorMessage = "Error occurred while attempting to start task";
                        $scope.errorVisible = true;
                        fetchTasks();
                        console.log("ERROR: " + JSON.stringify({data: data}));
                        $timeout(function () {
                            $scope.errorVisible = false;
                            $scope.errorMessage = "";
                        }, 2000);
                    });
            };

            $scope.stopTask = function (taskId) {
                $http.post("service/finance/stopTask", taskId).then(
                    function (data) {
                        $scope.successMessage = "Task stopped successfully";
                        $scope.successVisible = true;
                        $scope.taskInProgress = false;
                        $scope.taskInProgressId = null;
                        fetchTasks();
                        $timeout(function () {
                            $scope.successVisible = false;
                            $scope.successMessage = "";
                        }, 2000);
                    },
                    function (data) {
                        $scope.errorMessage = "Error occurred while attempting to stop task";
                        $scope.errorVisible = true;
                        fetchTasks();
                        console.log("ERROR: " + JSON.stringify({data: data}));
                        $timeout(function () {
                            $scope.errorVisible = false;
                            $scope.errorMessage = "";
                        }, 2000);
                    });
            };

            $scope.completeReview = function(approved) {

                $http.post("service/finance/completeTask/" + $scope.taskInProgressId + "/" + approved).then(
                    function(data) {
                        $scope.successMessage = "Review submitted successfully";
                        $scope.successVisible = true;
                        $timeout(function() {
                            $scope.successVisible = false;
                            $scope.successMessage = "";
                            $location.path("/home");
                        }, 2000);
                    },
                    function (data) {
                        $scope.errorMessage = "Error occurred while attempting to submit Review";
                        $scope.errorVisible = true;
                        console.log("ERROR: " + JSON.stringify({data: data}));
                        $timeout(function() {
                            $scope.errorVisible = false;
                            $scope.errorMessage = "";
                        }, 2000);
                    });
            };
        }]);