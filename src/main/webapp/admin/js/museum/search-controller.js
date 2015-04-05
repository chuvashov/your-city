/**
 * Created by Andrey on 25.03.2015.
 */
angular.module('museumSearch', [])
    .controller('museumCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.museumIdSearch = null;
        $scope.museumNameSearch = null;
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

        $scope.foundMuseums = [];
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
            $scope.foundMuseums = data;
            if (data != null && data.length > 0) {
                $scope.notFound = false;
            } else {
                $scope.foundMuseums = [];
                $scope.notFound = true;
            }
        };
        var errorLoadingAction = function (data, status) {
            $scope.foundMuseums = [];
            if (status == 404) {
                $scope.notFound = true;
            } else {
                $scope.notFound = false;
                $scope.hasError = true;
            }
        };

        $scope.findByNameAndCity = function () {
            var cityId = getCityId($scope.citySearch) || '',
                name = $scope.museumNameSearch || '',
                request;
            $scope.hasError = false;
            if (cityId) {
                if (name) {
                    request = 'name/cityid?name=' + name + '&cityId=' + cityId;
                } else {
                    request = 'cityid?cityId=' + cityId;
                }
            } else if (name) {
                request = 'name?name=' + name;
            } else {
                request = 'all';
            }
            $http.get('/your-city/rest/admin/museum/find/' + request)
                .success(successLoadingAction)
                .error(errorLoadingAction);
        };

        $scope.findById = function () {
            $scope.hasError = false;
            $http.get('/your-city/rest/admin/museum/find/id?id=' + $scope.museumIdSearch)
                .success(successLoadingAction)
                .error(errorLoadingAction);
        };

        $scope.deleteMuseum = function (id) {
            $scope.hasError = false;
            $http.post('/your-city/rest/admin/museum/delete?id=' + id)
                .success(function () {
                    for (var j = 0; $scope.foundMuseums.length; j++) {
                        if ($scope.foundMuseums[j].id == id) {
                            $scope.foundMuseums.splice(j, 1);
                            break;
                        }
                    }
                })
                .error(function () {
                    $scope.hasError = true;
                });
        };
    }]);