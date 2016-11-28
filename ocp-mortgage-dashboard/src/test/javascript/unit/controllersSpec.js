'use strict';

/* jasmine specs for controllers go here */
describe('Controller tests', function () {

    describe('HomeCtrl', function () {
        var scope, ctrl;

        beforeEach(module('app'));
        beforeEach(inject(function ($rootScope, $controller) {
            scope = $rootScope.$new();
            ctrl = $controller('HomeCtrl', {$scope: scope});
        }));


        it('should contain hello world', function () {
            expect(scope.title).toBe('Hello world!');
        });
    });
});
