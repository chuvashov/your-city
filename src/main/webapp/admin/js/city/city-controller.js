/**
 * Created by Andrey on 18.04.2015.
 */
angular.module('cityModule', [])
    .controller('cityCtrl', ['$scope', '$http', '$timeout', function ($scope, $http, $timeout) {

        $scope.deleteCity = function (id) {
            $scope.deletingError = false;
            $http.post('/your-city/rest/admin/city/delete?id=' + id)
                .success(function () {
                    var index;
                    $.each($scope.cities, function (j, city) {
                        if (city.id == id) {
                            index = j;
                            return false;
                        }
                    });
                    $scope.cities.splice(index, 1);
                })
                .error(function () {
                    $scope.deletingError = true;
                    $timeout(function () {
                        $scope.deletingError = false;
                    }, 4000);
                });
        };

        $scope.addCity = function () {
            $scope.addingError = false;
            $http.post('/your-city/rest/admin/city/add?name=' + $scope.cityName)
                .success(function () {
                    $scope.created = true;
                    $scope.tryToGetCities();
                    $timeout(function () {
                        $scope.created = false;
                    }, 2000);
                })
                .error(function () {
                    $scope.addingError = true;
                    $timeout(function () {
                        $scope.addingError = false;
                    }, 4000);
                });
        };

    }]);