'use strict';

angular.module('app')
    .controller("ApplicantCtrl", ['$scope', '$http', '$location', '$timeout',
        function ($scope, $http, $location, $timeout) {

        $scope.successVisible = false;

        $scope.applicantName = "Bob Smith";
        $scope.applicantSsn = "313781234";
        $scope.applicantIncome = 150000;
        $scope.propertyAddress = "100 Central Ave";
        $scope.propertyPrice = 350000;
        $scope.downPayment = 50000;
        $scope.amortization = 10;
        $scope.apr = 4.5;

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
                mortgageAmount: null,
                apr: null
            };

            $http.post("service/applicant/startApp", $application).then(
                function(data) {
                    $scope.successVisible = true;
                    $timeout(function() {
                        $scope.successVisible = false;
                        $location.path("/home");
                    }, 2000);
                },
                function (data) {
                    $scope.errorVisible = true;
                    console.log("ERROR: " + JSON.stringify({data: data}));
                    $timeout(function() {
                        $scope.errorVisible = false;
                    }, 2000);
            });
        };

    }]);