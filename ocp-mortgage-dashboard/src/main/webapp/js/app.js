'use strict';


var app = angular.module('app', [
    'ngRoute',
    'ngResource',
    'ngSanitize'
]);

app.constant('Constants', {
   PROCESS_STATE: {
       0: 'PENDING',
       1: 'ACTIVE',
       2: 'COMPLETED',
       3: 'ABORTED',
       4: 'SUSPENDED'
   }
});

app.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/home.html',
                controller: 'HomeCtrl'
            })
            .when('/dataCorrection', {
                templateUrl: 'views/dataCorrection.html',
                controller: 'DataCorrectionCtrl'
            })
            .when('/manager', {
                templateUrl: 'views/manager.html',
                controller: 'ManagerCtrl'
            })
            .when('/applicant', {
                templateUrl: 'views/applicant.html',
                controller: 'ApplicantCtrl'
            })
            .when('/appraiser', {
                templateUrl: 'views/appraiser.html',
                controller: 'AppraiserCtrl'
            })
            .when('/troubleshoot', {
                templateUrl: 'views/troubleshoot.html',
                controller: 'TroubleshootCtrl'
            })
            .when('/downPayment', {
                templateUrl: 'views/downPayment.html',
                controller: 'DownPaymentCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    }]);