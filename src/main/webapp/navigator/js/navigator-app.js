/**
 * Created by Andrey on 22.02.2015.
 */
angular.module('navigatorApp', ['ngRoute'])
    .controller('navigatorCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {

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
                $scope.curCity = $scope._defaultCity;
                $scope.cities = [];
                $scope.cities.push($scope._defaultCity);
            });

        $scope.setCurCity = function (city) {
            if (city == $scope.curCity) {
                return;
            }
            $scope.curCity = city;
            var path = $location.path();
            if (path.substring(0, 9) == '/museums/') {
                $location.path('/museums');
            } else {
                //other
            }
        };

    }])
    .controller('mainMuseumCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.museumList = [];
        $scope.loading = null;
        $scope.noMuseumsMessage = false;
        $scope.tryToGetMuseums = function () {
            $scope.loading = true;
            $http.get('museum/all?city=' + $scope.curCity)
                .success(function (data) {
                    $scope.museumList = [];
                    $.each(data, function (i, obj) {
                        $scope.museumList.push(obj);
                    });
                    $scope.loading = false;
                })
                .error(function () {
                    $scope.loading = false;
                    $scope.noMuseumsMessage = true;
                });
        };
        $scope.tryToGetMuseums();
    }])
    .controller('museumViewCtrl', ['$scope', '$http', '$routeParams', function ($scope, $http, $routeParams) {
        $scope.museum = {};
        $scope.loading = null;
        $scope.tryToGetMuseumInfo = function () {
            $scope.loading = true;
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
            $scope.loading = true;
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