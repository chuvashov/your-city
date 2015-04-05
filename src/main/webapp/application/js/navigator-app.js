/**
 * Created by Andrey on 22.02.2015.
 */
angular.module('navigatorApp', ['ngRoute', 'ngCookies', 'mainMuseum', 'museumView', 'museumImagesView'])
    .controller('navigatorCtrl', ['$scope', '$http', '$location', '$cookies', '$rootScope',
            function ($scope, $http, $location, $cookies, $rootScope) {

        $scope.cities = [];
        $scope.curCity = $cookies.city ? $cookies.city : 'Choose city';
        $scope.saveCurCity = function (city) {
            $cookies.city = city;
        };

        $http.get('/your-city/cities/all')
            .success(function (data) {
                $scope.cities = [];
                $.each(data, function (i, obj) {
                    $scope.cities.push(obj['city']);
                });
                $scope.cities.sort();
                if ($scope.cities.length && $scope.curCity == 'Choose city') {
                    $scope.curCity = $scope.cities[0];
                }
            })
            .error(function () {
                $scope.cities = [];
            });

        $scope.setCurCity = function (city) {
            if (city == $scope.curCity) {
                return;
            }
            $scope.curCity = city;
            $scope.saveCurCity(city);
            var path = $location.path();
            if (path.substring(0, 9) == '/museums') {
                $location.path('museums');
            } else {
                $rootScope.$broadcast('cityWasChanged');
                //other
            }
        };

    }])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('', {
                template: 'home page'
            })
            .when('/', {
                template: 'home page'
            })
            .when('/museums', {
                templateUrl: '/your-city/application/tmpl/museum/museums_view.html',
                controller: 'mainMuseumCtrl'
            })
            .when('/museums/view/:museumId', {
                templateUrl: '/your-city/application/tmpl/museum/museum_view.html',
                controller: 'museumViewCtrl'
            })
            .when('/museums/images/:museumId', {
                templateUrl: '/your-city/application/tmpl/museum/museum_images_view.html',
                controller: 'museumImagesViewCtrl'
            })
            .otherwise({
                redirectTo: ''
            });
    }]);