/**
 * Created by Andrey on 22.02.2015.
 */
angular.module('navigatorApp', ['ngRoute'])
    .controller('navigatorCtrl', ['$scope', '$http', function ($scope, $http) {

        $scope.cities = [];
        $scope.curCity = 'Choose city';

        $http.get('/cities/all').
            success(function (data) {
                $scope.cities = [];
                $.each(data, function (i, obj) {
                    $scope.cities.push(obj['city']);
                });
                $scope.cities.sort();
                if ($scope.cities.length) {
                    $scope.curCity = $scope.cities[0];
                }
            }).
            error(function () {
                alert('не смог скачать города');
            });

        $scope.setCurCity = function (city) {
            $scope.curCity = city;
        };

        $scope._views = ['home', 'museums'];
        $scope.curView = 'home';
        $scope.setHomeView = function () {
            $scope.curView = 'home';
        };
        $scope.isHomeView = function () {
            return $scope.curView == 'home';
        };
        $scope.setMuseumView = function () {
            debugger;
            $scope.curView = 'museums';
        };
        $scope.isMuseumsView = function () {
            return $scope.curView == 'museums';
        };

    }]);
/*.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
 //$locationProvider.html5Mode(true);
 $routeProvider
 .when('/', {
 template: 'ttttttttttttttt'
 })
 .when('/museums', {
 templateUrl: '/application/tmpl/museums_view.html',
 controller: 'museumCtrl'
 })
 .otherwise({
 redirectTo: '/'
 });
 }]);         */