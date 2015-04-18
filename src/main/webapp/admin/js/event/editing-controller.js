/**
 * Created by Andrey on 26.03.2015.
 */
angular.module('eventEditing', [])
    .controller('eventEditingCtrl', ['$scope', '$http', '$location', '$timeout', '$routeParams',
        function ($scope, $http, $location, $timeout, $routeParams) {
            $scope.eventType = $routeParams.eventType;
            $scope.event = $scope.getSavedEvent();
            if ($routeParams.eventId == $scope.event.id) {
                $scope.eventOriginal = angular.copy($scope.event);
            } else {
                $scope.event = {};
                $scope.eventOriginal = {};
                $http.get('/your-city/rest/admin/event/find/' + $scope.eventType + '/id?id=' + $routeParams.eventId)
                    .success(function (data) {
                        $scope.event = angular.copy(data[0]);
                        $scope.eventOriginal = data[0];
                        updateCities();
                    })
                    .error(function () {
                        $scope.errorLoadingEvent = true;
                        $scope.errorEventId = $routeParams.eventId;
                        $timeout(function () {
                            $location.path('events/' + $scope.eventType);
                        }, 2000);
                    });
            }
            $scope.cityList = [];
            $scope.cityName = '';
            var updateCities = function () {
                $scope.cityList = [];
                $.each($scope.cities, function (j, city) {
                    $scope.cityList.push(city.name);
                });
                chooseCity();
            };
            var chooseCity = function () {
                if ($scope.event.cityId) {
                    $.each($scope.cities, function (j, city) {
                        if (city.id == $scope.event.cityId) {
                            $scope.cityName = city.name;
                            return false;
                        }
                    });
                }
            };

            if ($scope.cities.length > 0) {
                updateCities();
            }
            $scope.$on('citiesLoaded', function () {
                updateCities();
            });
            $scope.progressBar = false;
            $scope.imageChanged = false;
            var $name = $("input[name='eventName']"),
                $description = $("input[name='eventDescription']"),
                $about = $("textarea[name='eventAbout']"),
                $startTime = $("input[name='eventStartTime']"),
                $finishTime = $("input[name='eventFinishTime']"),
                $image = $("input[name='eventImage']"),
                $img = $("#image-view");

            $scope.clear = function () {
                $scope.event = {};
                $scope.event.image = $scope.defaultEventImage;
                $name.val('');
                $description.val('');
                $startTime.val('');
                $finishTime.val('');
                $about.val('');
                $scope.removeImage();
            };

            $scope.cancel = function () {
                $location.path('events/' + $scope.eventType);
            };

            $scope.save = function () {
                $scope.progressBar = true;
                $.each($scope.cities, function (j, city) {
                    if ($scope.cityName == city.name) {
                        $scope.event.cityId = city.id;
                        return false;
                    }
                });
                $http.post('/your-city/rest/admin/event/' + $scope.eventType + '/update?id=' + $routeParams.eventId,
                    angular.toJson($scope.event), {headers: {'Content-Type': 'application/json'}})
                    .success(function () {
                        $scope.progressBar = false;
                        $scope.created = true;
                        $scope.hasError = false;
                        $scope.eventOriginal = angular.copy($scope.event);
                        $timeout(function () {
                            $scope.created = false;
                        }, 3000);
                    })
                    .error(function () {
                        $scope.progressBar = false;
                        $scope.created = false;
                        $scope.hasError = true;
                        $timeout(function () {
                            $scope.hasError = false;
                        }, 5000);
                    });
            };

            $scope.removeImage = function () {
                $scope.imageChanged = false;
                $scope.event.image = $scope.defaultEventImage;
                $image.val('');
                $img.attr('src', $scope.defaultEventImage);
                $scope.file = null;
            };

            $scope.uploadImage = function (files) {
                $scope.file = files[0];
                $scope.$apply($scope.imageChanged = true);
                var reader = new FileReader();
                reader.onload = function (e) {
                    $img.attr('src', e.target.result);
                    $scope.event.image = e.target.result;
                };
                reader.readAsDataURL(files[0]);
            };

            $scope.setOriginalEvent = function () {
                $scope.event = angular.copy($scope.eventOriginal);
                chooseCity();
            };

            $scope.deleteEvent = function () {
                $scope.deleteError = false;
                $scope.progressBar = true;
                $http.post('/your-city/rest/admin/event/' + $scope.eventType + '/delete?id=' + $routeParams.eventId)
                    .success(function () {
                        $scope.deleted = true;
                        $timeout(function () {
                            $location.path('events/' + $scope.eventType);
                        }, 2000);
                        $scope.progressBar = false;
                    })
                    .error(function () {
                        $scope.deleteError = true;
                        $timeout(function () {
                            $scope.deleteError = false;
                        }, 3000);
                        $scope.progressBar = false;
                    });
            };
        }]);
