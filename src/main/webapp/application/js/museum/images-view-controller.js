/**
 * Created by Andrey on 04.04.2015.
 */
angular.module('museumImagesView', [])
    .controller('museumImagesViewCtrl', ['$scope', '$http', '$routeParams', function ($scope, $http, $routeParams) {
        $scope.images = [];
        $scope.loading = null;
        $scope.noImagesMessage = false;
        $scope.museumName = "";

        $http.get('/your-city/museum/info/byId?id=' + $routeParams.museumId)
            .success(function (data) {
                $scope.museumName = data.name;
            });

        var tryToGetImages = function () {
            $scope.loading = true;
            $http.get('/your-city/museum/images?id=' + $routeParams.museumId)
                .success(function (data) {
                    $scope.images = [];
                    $.each(data, function (i, obj) {
                        $scope.images.push(obj);
                    });
                    $scope.loading = false;
                    if (data === []) {
                        $scope.noImagesMessage = true;
                    }
                })
                .error(function () {
                    $scope.noImagesMessage = true;
                    $scope.loading = false;
                });
        };
        tryToGetImages();
    }]);