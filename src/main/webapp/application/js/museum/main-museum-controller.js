/**
 * Created by Andrey on 04.04.2015.
 */
angular.module('mainMuseum', [])
    .controller('mainMuseumCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.museumList = [];
        $scope.loading = null;
        $scope.noMuseumsMessage = false;

        var tryToGetMuseums = function () {
            $scope.loading = true;
            $http.get('/your-city/museum/all?city=' + $scope.curCity)
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
        tryToGetMuseums();

        $scope.$on('cityWasChanged', function () {
            tryToGetMuseums();
        });
    }]);