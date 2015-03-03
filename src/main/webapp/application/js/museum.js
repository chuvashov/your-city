/**
 * Created by Andrey on 22.02.2015.
 */
angular.module('museumApp', [])
    .controller('mainMuseumCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.museumList = [];
        $scope.tryGetMuseums = function () {
            $http.get('museum/all?city=' + $scope.curCity)
                .success(function (data) {
                    $scope.museumList = [];
                    $.each(data, function (i, obj) {
                        $scope.museumList.push(obj);
                    });
                })
                .error(function () {
                    alert('Couldn\'t load museums');
                });
        };
        $scope.tryGetMuseums();

        $scope.lookAtMuseum = function(museum) {

        };

        $scope.lookAtMuseumImages = function(museum) {

        };
    }]);