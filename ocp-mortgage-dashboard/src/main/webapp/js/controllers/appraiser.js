'use strict';

angular.module('app')
    .controller("AppraiserCtrl", ['$scope', '$http', 'Constants', '$location', '$timeout',
        function ($scope, $http, Constants, $location, $timeout) {

            $scope.consts = Constants;
            $scope.taskInProgress = false;

            fetchTasks();

            function fetchTasks() {
                $http.get("service/appraiser/tasks").success(function (data) {
                    $scope.tasks = data;
                });
            }

            $scope.claimTask = function (taskId) {
                $http.post("service/appraiser/claimTask", taskId).then(
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
                $http.post("service/appraiser/releaseTask", taskId).then(
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
                $http.post("service/appraiser/startTask", taskId).then(
                    function (data) {
                        $scope.successMessage = "Task started successfully";
                        $scope.successVisible = true;
                        $scope.taskInProgress = true;
                        $scope.taskInProgressId = taskId;

                        var $app = data.data;
                        $scope.applicantName = $app.applicant.name;
                        $scope.applicantSsn = $app.applicant.ssn;
                        $scope.applicantIncome = $app.applicant.income;
                        $scope.propertyAddress = $app.property.address;
                        $scope.propertyPrice = $app.property.price;
                        $scope.downPayment = $app.downPayment;
                        $scope.amortization = $app.amortization;
                        $scope.apr = $app.apr;
                        $scope.appraisalValue = null;

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
                $http.post("service/appraiser/stopTask", taskId).then(
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
                    appraisal : {
                        property : {
                            address: $scope.propertyAddress,
                            price: $scope.propertyPrice
                        },
                        value: $scope.appraisalValue
                    },
                    mortgageAmount: ($scope.propertyPrice - $scope.downPayment),
                    apr: $scope.apr
                };

                $http.post("service/appraiser/completeTask/" + $scope.taskInProgressId, $application).then(
                    function(data) {
                        $scope.successMessage = "Appraisal submitted successfully";
                        $scope.successVisible = true;
                        $timeout(function() {
                            $scope.successVisible = false;
                            $scope.successMessage = "";
                            $location.path("/home");
                        }, 2000);
                    },
                    function (data) {
                        $scope.errorMessage = "Error occurred while attempting to submit Appraisal";
                        $scope.errorVisible = true;
                        console.log("ERROR: " + JSON.stringify({data: data}));
                        $timeout(function() {
                            $scope.errorVisible = false;
                            $scope.successMessage = "";
                        }, 2000);
                    });
            };
        }]);