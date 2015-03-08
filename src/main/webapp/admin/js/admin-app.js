/**
 * Created by Andrey on 08.03.2015.
 */
angular.module('adminApp', ['ngRoute'])
    .controller('mainAdminCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.cities = [];
        $scope.tryToGetCities = function () {
            $http.get('/rest/admin/city/all')
                .success(function (data) {
                    $scope.cities = data;
                })
                .error(function () {
                    alert('Couldn\'t load cities!');
                });
        };
        $scope.tryToGetCities();
    }])
    .controller('museumCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.museumIdSearch = null;
        $scope.museumNameSearch = null;
        $scope.cityIdSearch = null;

        $scope.foundMuseums = [];
        $scope.notFound = false;

        $scope.findMuseums = function () {
            var requestParams = '';
            if ($scope.museumIdSearch) {
                requestParams = 'id=' + $scope.museumIdSearch;
            } else if ($scope.museumNameSearch) {
                requestParams = 'name=' + $scope.museumNameSearch;
                if ($scope.cityIdSearch) {
                    requestParams += '&cityId=' + $scope.cityIdSearch;
                }
            } else if ($scope.cityIdSearch) {
                requestParams = 'cityId=' + $scope.cityIdSearch;
            } else {
                return;
            }
            $http.get('/rest/admin/museum?' + requestParams)
                .success(function (data) {
                    $scope.foundMuseums = data;
                    $scope.notFound = false;
                })
                .error(function () {
                    $scope.notFound = true;
                });
            $scope.museumIdSearch = null;
            $scope.museumNameSearch = null;
            $scope.cityIdSearch = null;
        };
    }])
    .controller('museumAddingCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.museum = {};
        $scope._previousMuseum = {};
        $scope.saveMuseum = function () {
            $http.post('/rest/admin/museum/add');
        };
    }])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('', {
                template: 'admin registration'
            })
            .when('/', {
                template: 'admin registration'
            })
            .when('/museums', {
                templateUrl: '/admin/tmpl/admin_museums.html',
                controller: 'museumCtrl'
            })
            .when('/museums/add', {
                templateUrl: '/admin/tmpl/add_museum.html',
                controller: 'museumAddingCtrl'
            })
            .otherwise({
                redirectTo: ''
            });
    }]);