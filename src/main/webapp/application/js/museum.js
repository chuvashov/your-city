/**
 * Created by Andrey on 22.02.2015.
 */
angular.module('museumApp', [])
    .controller('museumCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.museumCount = 0;
        $http.get('/museum/count?city=' + $scope.curCity).
            success(function (data) {
                $scope.museumCount = data;
            }).
            error(function () {
                alert('не смог узнать количество музеев');
            });

        $scope.museumList = [];
        $scope.indexFrom = 1;
        $scope.indexTo = 10;
        $scope.tryGetMuseums = function () {
            $http.get('museum/indexes?city=' + $scope.curCity + '&from=' + $scope.indexFrom + '&to=' + $scope.indexTo).
                success(function (data) {
                    $scope.museumList = [];
                    $.each(data, function (i, obj) {
                        $scope.museumList.push(obj);
                    });
                }).
                error(function () {
                    alert('не смог скачать музеи');
                });
        }
        $scope.tryGetMuseums();
    }]);