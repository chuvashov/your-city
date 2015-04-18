/**
 * Created by Andrey on 04.04.2015.
 */
angular.module('eventView', [])
    .controller('eventViewCtrl', ['$scope', '$http', '$routeParams', '$location',
            function ($scope, $http, $routeParams, $location) {
        $scope.eventType = $routeParams.eventType;
        $scope.event = {};
        $scope.loading = null;
        $scope.tryToGetEventInfo = function () {
            $scope.loading = true;
            $http.get('/your-city/event/' + $scope.eventType + '/view?id=' + $routeParams.eventId)
                .success(function (data) {
                    $scope.event = data[0];
                    $scope.loading = false;
                })
                .error(function () {
                    alert('Couldn\'t load event info');
                    $scope.loading = false;
                });
        };
        $scope.tryToGetEventInfo();

        $scope.$on('cityWasChanged', function () {
            $location.path('events/' + $scope.eventType);
        });
    }]);