/**
 * Created by Andrey on 17.04.2015.
 */
angular.module('eventSearch', [])
    .controller('eventCtrl', ['$scope', '$http', '$routeParams', function ($scope, $http, $routeParams) {
        $scope.eventType = $routeParams.eventType;
        $scope.eventIdSearch = null;
        $scope.eventNameSearch = null;
        $scope.cityList = [];
        var allCities = '-- ALL CITIES --';
        $scope.citySearch = allCities;
        var updateCities = function () {
            $scope.citySearch = allCities;
            $scope.cityList = [];
            $scope.cityList.push(allCities);
            $.each($scope.cities, function (j, city) {
                $scope.cityList.push(city.name);
            });
        };
        if ($scope.cities.length > 0) {
            updateCities();
        }
        $scope.$on('citiesLoaded', function () {
            updateCities();
        });

        $scope.foundEvents = [];
        $scope.notFound = false;
        $scope.hasError = false;

        var getCityId = function (cityName) {
            var id = null;
            $.each($scope.cities, function (j, city) {
                if (city.name == cityName) {
                    id = city.id;
                }
            });
            return id;
        };
        var successLoadingAction = function (data) {
            $scope.foundEvents = data;
            if (data != null && data.length > 0) {
                $scope.notFound = false;
            } else {
                $scope.foundEvents = [];
                $scope.notFound = true;
            }
        };
        var errorLoadingAction = function (data, status) {
            $scope.foundEvents = [];
            if (status == 404) {
                $scope.notFound = true;
            } else {
                $scope.notFound = false;
                $scope.hasError = true;
            }
        };

        $scope.findByNameAndCity = function () {
            var cityId = getCityId($scope.citySearch) || '',
                name = $scope.eventNameSearch || '',
                request;
            $scope.hasError = false;
            if (cityId) {
                if (name) {
                    request = '/name/cityid?name=' + name + '&cityId=' + cityId;
                } else {
                    request = '/cityid?cityId=' + cityId;
                }
            } else if (name) {
                request = '/name?name=' + name;
            } else {
                request = '/all';
            }
            $http.get('/your-city/rest/admin/event/find/' + $scope.eventType + request)
                .success(successLoadingAction)
                .error(errorLoadingAction);
        };

        $scope.findById = function () {
            $scope.hasError = false;
            $http.get('/your-city/rest/admin/event/find/' + $scope.eventType + '/id?id=' + $scope.eventIdSearch)
                .success(successLoadingAction)
                .error(errorLoadingAction);
        };

        $scope.deleteEvent = function (id) {
            $scope.hasError = false;
            $http.post('/your-city/rest/admin/event/' + $scope.eventType + '/delete?id=' + id)
                .success(function () {
                    for (var j = 0; $scope.foundEvents.length; j++) {
                        if ($scope.foundEvents[j].id == id) {
                            $scope.foundEvents.splice(j, 1);
                            break;
                        }
                    }
                })
                .error(function () {
                    $scope.hasError = true;
                });
        };
    }]);