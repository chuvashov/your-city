/**
 * Created by Andrey on 08.03.2015.
 */
angular.module('adminApp', ['ngRoute', 'ngCookies', 'museumSearch', 'museumAdding', 'museumEditing', 'museumImages',
        'registration', 'eventSearch'])
    .controller('mainAdminCtrl', ['$scope', '$http', '$rootScope', '$cookies', function ($scope, $http, $rootScope, $cookies) {

        $scope.logout = function () {
            //$cookies.JSESSIONID = null;
            $http.post('/rest/admin/logout1', angular.toJson({}), {headers: {'Content-Type': 'application/json'}})
                .success(function () {

                });
            $http.post('/signout', angular.toJson({}), {headers: {'Content-Type': 'application/json'}})
                .success(function () {

                });
        };

        $scope.cities = [];
        $scope.tryToGetCities = function () {
            $http.get('/your-city/rest/admin/city/all')
                .success(function (data) {
                    $scope.cities = data;
                    $rootScope.$broadcast('citiesLoaded');
                    $scope.authenticated = true;
                })
                .error(function () {
                    document.location.href = 'login.html';
                });
        };
        $scope.tryToGetCities();

        $scope.eventTypes = [];
        $scope.tryToGetEventTypes = function () {
            $http.get('/your-city/rest/admin/event/types')
                .success(function (data) {
                    $scope.eventTypes = [];
                    $.each(data, function (j, event) {
                        $scope.eventTypes.push(event['eventType']);
                    });
                })
                .error(function () {
                    document.location.href = 'login.html';
                });
        };
        $scope.tryToGetEventTypes();

        $scope.defaultMuseumAvatar = '/your-city/application/images/default_museum_avatar.png';
        $scope.waitProgress = '/your-city/application/images/wait_progress.gif';

        var _museum = {};
        $scope.saveMuseumInKeeper = function (museum) {
            angular.copy(museum, _museum);
        };
        $scope.getSavedMuseum = function () {
            return angular.copy(_museum);
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
            .when('/registration', {
                templateUrl: '/your-city/admin/tmpl/registration.html',
                controller: 'registrationCtrl'
            })
            .when('/museums', {
                templateUrl: '/your-city/admin/tmpl/museum/admin_museums.html',
                controller: 'museumCtrl'
            })
            .when('/museums/add', {
                templateUrl: '/your-city/admin/tmpl/museum/add_museum.html',
                controller: 'museumAddingCtrl'
            })
            .when('/museums/:museumId/edit', {
                templateUrl: '/your-city/admin/tmpl/museum/edit_museum.html',
                controller: 'museumEditingCtrl'
            })
            .when('/museums/:museumId/images', {
                templateUrl: '/your-city/admin/tmpl/museum/images_museum.html',
                controller: 'museumImagesCtrl'
            })
            .when('/events/:eventType', {
                templateUrl: '/your-city/admin/tmpl/event/admin_events.html',
                controller: 'eventCtrl'
            })
            .otherwise({
                redirectTo: ''
            });
    }]);