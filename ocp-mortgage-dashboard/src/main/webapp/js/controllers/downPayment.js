'use strict';

angular.module('app')
    .controller("DownPaymentCtrl", ['$scope', '$http', 'Constants', '$location', '$timeout',
        function ($scope, $http, Constants, $location, $timeout) {

            $scope.consts = Constants;
            $scope.taskInProgress = false;

            fetchTasks();

            function fetchTasks() {
                $http.get("service/broker/downPaymentTasks").success(function (data) {
                    $scope.tasks = data;
                });
            }

            $scope.claimTask = function (taskId) {
                $http.post("service/broker/claimTask", taskId).then(
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
                $http.post("service/broker/releaseTask", taskId).then(
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
                $http.post("service/broker/startTask", taskId).then(
                    function (data) {
                        $scope.successMessage = "Task started successfully";
                        $scope.successVisible = true;
                        $scope.taskInProgress = true;
                        $scope.taskInProgressId = taskId;

                        var $app = data.data.taskInputApplication;
                        $scope.applicantName = $app.applicant.name;
                        $scope.applicantSsn = $app.applicant.ssn;
                        $scope.applicantIncome = $app.applicant.income;
                        $scope.propertyAddress = $app.property.address;
                        $scope.propertyPrice = $app.property.price;
                        $scope.downPayment = $app.downPayment;
                        $scope.amortization = $app.amortization;
                        $scope.apr = $app.apr;

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
                $http.post("service/broker/stopTask", taskId).then(
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

            $scope.submitApp = function() {

                var $application = {
                    applicant : {
                        name: $scope.applicantName,
                        ssn: $scope.applicantSsn,
                        income: $scope.applicantIncome,
                        creditScore: null
                    },
                    property : {
                        address: $scope.propertyAddress,
                        price: $scope.propertyPrice
                    },
                    downPayment: $scope.downPayment,
                    amortization: $scope.amortization,
                    appraisal : null,
                    mortgageAmount: ($scope.propertyPrice - $scope.downPayment),
                    apr: $scope.apr
                };

                $http.post("service/broker/completeTask/" + $scope.taskInProgressId, $application).then(
                    function(data) {
                        $scope.successMessage = "Adjustment submitted successfully";
                        $scope.successVisible = true;
                        $timeout(function() {
                            $scope.successVisible = false;
                            $scope.successMessage = "";
                            $location.path("/home");
                        }, 2000);
                    },
                    function (data) {
                        $scope.errorMessage = "Error occurred while attempting to submit Adjustment";
                        $scope.errorVisible = true;
                        console.log("ERROR: " + JSON.stringify({data: data}));
                        $timeout(function() {
                            $scope.errorVisible = false;
                            $scope.errorMessage = "";
                        }, 2000);
                    });
            };
        }]);