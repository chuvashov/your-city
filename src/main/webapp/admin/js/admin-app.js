/**
 * Created by Andrey on 08.03.2015.
 */
angular.module('adminApp', ['ngRoute', 'museumSearch', 'museumAdding', 'museumEditing', 'museumImages'])
    .controller('mainAdminCtrl', ['$scope', '$http', '$rootScope', function ($scope, $http, $rootScope) {
        $scope.cities = [];
        $scope.tryToGetCities = function () {
            $http.get('/your-city/rest/admin/city/all')
                .success(function (data) {
                    $scope.cities = data;
                    $rootScope.$broadcast('citiesLoaded');
                })
                .error(function () {
                    alert('Couldn\'t load cities!');
                });
        };
        $scope.tryToGetCities();

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
            .otherwise({
                redirectTo: ''
            });
    }]);