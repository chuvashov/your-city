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
                $.each(data, function(i, obj) {
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

        $scope.setCurCity = function(city) {
            $scope.curCity = city;
        }
    }])
    .controller('museumCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.museumCount = 0;
        $http.get('/museum/count?city=' + $scope.curCity).
            success(function (data) {
                $scope.museumCount = data;
            }).
            error(function () {
                alert('не смог узнать количество музеев');
            });

        $scope.museumList = [];
        $scope.indexFrom = 0;
        $scope.indexTo = 9;
        $scope.tryGetMuseums = function () {
            $http.get('museum/indexes?city=' + $scope.curCity + '&from=' + $scope.indexFrom + '&to=' + $scope.indexTo).
                success(function (data) {
                    $scope.museumList = [];
                    $.each(data, function (i, obj) {
                        $scope.museumList.push(obj);
                    });
                }).
                error(function () {
                    alert('не смог скачать музеи');
                });
        }
        $scope.tryGetMuseums();
    }])
    .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        //$locationProvider.html5Mode(true);
        $routeProvider
            .when('/', {
                template: 'dcdcdcdcdcdcdddccdcdddcd'
            })
            .when('/museums', {
                templateUrl: '/application/tmpl/museums_view.html',
                controller: 'museumCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    }]);