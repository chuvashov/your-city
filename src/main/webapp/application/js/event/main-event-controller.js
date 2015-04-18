/**
 * Created by Andrey on 18.04.2015.
 */
angular.module('mainEvent', [])
    .controller('mainEventCtrl', ['$scope', '$http', '$routeParams', '$timeout',
            function ($scope, $http, $routeParams, $timeout) {
        $scope.eventType = $routeParams.eventType;
        $scope.eventList = [];
        $scope.loading = null;
        $scope.noEventsMessage = false;

        var tryToGetEvents = function () {
            $scope.loading = true;
            $scope.noEventsMessage = false;
            if ($scope.defaultCityLabel == $scope.curCity) {
                $timeout(function () {
                    tryToGetEvents();
                }, 100);
            }
            $http.get('/your-city/event/' + $scope.eventType + '/all?city=' + $scope.curCity)
                .success(function (data) {
                    $scope.eventList = [];
                    $.each(data, function (i, obj) {
                        $scope.eventList.push(obj);
                    });
                    $scope.loading = false;
                })
                .error(function () {
                    $scope.loading = false;
                    $scope.noEventsMessage = true;
                    $scope.eventList = [];
                });
        };
        tryToGetEvents();

        $scope.$on('cityWasChanged', function () {
            tryToGetEvents();
        });
    }]);