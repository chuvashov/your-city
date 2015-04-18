/**
 * Created by Andrey on 22.02.2015.
 */
angular.module('navigatorApp', ['ngRoute', 'ngCookies', 'mainMuseum', 'museumView', 'museumImagesView',
    'mainEvent', 'eventView'])
    .controller('navigatorCtrl', ['$scope', '$http', '$location', '$cookies', '$rootScope',
            function ($scope, $http, $location, $cookies, $rootScope) {
        $scope.eventType = '';
        $scope.cities = [];
        $scope.defaultCityLabel = 'Choose city';
        $scope.curCity = $cookies.city ? $cookies.city : $scope.defaultCityLabel;
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
                if ($scope.cities.length && $scope.curCity == $scope.defaultCityLabel) {
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
            $rootScope.$broadcast('cityWasChanged');
        };

    }])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('', {
                templateUrl: '/your-city/application/tmpl/homepage.html'
            })
            .when('/', {
                templateUrl: '/your-city/application/tmpl/homepage.html'
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
            .when('/events/:eventType', {
                templateUrl: '/your-city/application/tmpl/event/events_view.html',
                controller: 'mainEventCtrl'
            })
            .when('/events/:eventType/view/:eventId', {
                templateUrl: '/your-city/application/tmpl/event/event_view.html',
                controller: 'eventViewCtrl'
            })
            .otherwise({
                redirectTo: ''
            });
    }]);