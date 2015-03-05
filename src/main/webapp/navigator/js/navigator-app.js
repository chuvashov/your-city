/**
 * Created by Andrey on 22.02.2015.
 */
angular.module('navigatorApp', ['ngRoute'])
    .controller('navigatorCtrl', ['$scope', '$http', function ($scope, $http) {

        $scope.cities = [];
        $scope.curCity = 'Choose city';
        $scope._defaultCity = 'Moscow';

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
                $scope.curCity = $scope._defaultCity;
                $scope.cities = [];
                $scope.cities.push($scope._defaultCity);
            });

        $scope.setCurCity = function (city) {
            $scope.curCity = city;
        };

    }])
    .controller('mainMuseumCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.museumList = [];
        $scope.loading = null;
        $scope.noMuseumsMessage = false;
        $scope.tryToGetMuseums = function () {
            $http.get('museum/all?city=' + $scope.curCity)
                .success(function (data) {
                    $scope.museumList = [];
                    $.each(data, function (i, obj) {
                        $scope.museumList.push(obj);
                        $scope.loading = false;
                    });
                })
                .error(function () {
                    $scope.loading = false;
                    $scope.noMuseumsMessage = true;
                });
        };
        $scope.loading = true;
        $scope.tryToGetMuseums();
    }])
    .controller('museumViewCtrl', ['$scope', '$http', '$routeParams', function ($scope, $http, $routeParams) {
        $scope.museum = {};
        $scope.loading = null;
        $scope.tryToGetMuseumInfo = function () {
            $http.get('/museum/view?id=' + $routeParams.museumId)
                .success(function (data) {
                    $scope.museum = data;
                    $scope.loading = false;
                })
                .error(function () {
                    alert('Couldn\'t load museum info');
                    $scope.loading = false;
                });
        };
        $scope.loading = true;
        $scope.tryToGetMuseumInfo();
    }])
    .controller('museumImagesViewCtrl', ['$scope', '$http', '$routeParams', function ($scope, $http, $routeParams) {
        $scope.images = [];
        $scope.loading = null;
        $scope.noImagesMessage = false;
        $scope.museumName = "";
        $http.get('/museum/info/byId?id=' + $routeParams.museumId)
            .success(function (data) {
                $scope.museumName = data.name;
            });
        $scope.tryToGetImages = function () {
            $http.get('/museum/images?id=' + $routeParams.museumId)
                .success(function (data) {
                    $scope.images = [];
                    $.each(data, function (i, obj) {
                        $scope.images.push(obj);
                    });
                    $scope.loading = false;
                })
                .error(function () {
                    $scope.noImagesMessage = true;
                    $scope.loading = false;
                });
        };
        $scope.loading = true;
        $scope.tryToGetImages();
    }])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/', {
                template: 'home page'
            })
            .when('/museums', {
                templateUrl: '/application/tmpl/museum/museums_view.html',
                controller: 'mainMuseumCtrl'
            })
            .when('/museums/view/:museumId', {
                templateUrl: '/application/tmpl/museum/museum_view.html',
                controller: 'museumViewCtrl'
            })
            .when('/museums/images/:museumId', {
                templateUrl: '/application/tmpl/museum/museum_images_view.html',
                controller: 'museumImagesViewCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    }]);