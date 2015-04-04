/**
 * Created by Andrey on 04.04.2015.
 */
angular.module('museumView', [])
    .controller('museumViewCtrl', ['$scope', '$http', '$routeParams', function ($scope, $http, $routeParams) {
        $scope.museum = {};
        $scope.loading = null;
        $scope.tryToGetMuseumInfo = function () {
            $scope.loading = true;
            $http.get('/your-city/museum/view?id=' + $routeParams.museumId)
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
    }]);