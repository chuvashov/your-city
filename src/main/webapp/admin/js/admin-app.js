/**
 * Created by Andrey on 08.03.2015.
 */
angular.module('adminApp', ['ngRoute', 'ngCookies', 'museumSearch', 'museumAdding', 'museumEditing', 'museumImages',
        'registration', 'eventSearch', 'eventAdding', 'eventEditing', 'cityModule'])
    .controller('mainAdminCtrl', ['$scope', '$http', '$rootScope', '$cookies', function ($scope, $http, $rootScope) {

        $scope.logout = function () {
            $http.post('/your-city/logout')
                .success(function () {
                    document.location.href = 'login.html';
                });
        };

        $scope.eventType = '';

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
        $scope.defaultEventImage = '/your-city/application/images/default_event_image.png';
        $scope.waitProgress = '/your-city/application/images/wait_progress.gif';

        var _museum = {};
        $scope.saveMuseumInKeeper = function (museum) {
            angular.copy(museum, _museum);
        };
        $scope.getSavedMuseum = function () {
            return angular.copy(_museum);
        };

        var _event = {};
        $scope.saveEventInKeeper = function (event) {
            angular.copy(event, _event);
        };
        $scope.getSavedEvent = function () {
            return angular.copy(_event);
        };
    }])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('', {
                templateUrl: '/your-city/admin/tmpl/homepage.html'
            })
            .when('/', {
                templateUrl: '/your-city/admin/tmpl/homepage.html'
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
            .when('/events/:eventType/create', {
                templateUrl: '/your-city/admin/tmpl/event/add_event.html',
                controller: 'eventAddingCtrl'
            })
            .when('/events/:eventType/edit/:eventId', {
                templateUrl: '/your-city/admin/tmpl/event/edit_event.html',
                controller: 'eventEditingCtrl'
            })
            .when('/cities', {
                templateUrl: '/your-city/admin/tmpl/city/city.html',
                controller: 'cityCtrl'
            })
            .otherwise({
                redirectTo: ''
            });
    }]);