/**
 * Created by Andrey on 25.03.2015.
 */
angular.module('eventAdding', [])
    .controller('eventAddingCtrl', ['$scope', '$http', '$location', '$timeout', '$routeParams',
        function ($scope, $http, $location, $timeout, $routeParams) {
            var eventType = $routeParams.eventType;
            $scope.event = {};
            $scope.event.image = $scope.defaultEventImage;
            $scope.event.city = '';
            $scope.cityList = [];
            var updateCities = function () {
                $scope.event.city = $scope.cities[0].name;
                $scope.cityList = [];
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
                $about.val('');
                $startTime.val('');
                $finishTime.val('');
                $scope.removeImage();
            };

            $scope.cancel = function () {
                $location.path('events/' + eventType);
            };

            $scope.save = function () {
                $scope.progressBar = true;
                var event = {};
                if ($scope.event.name) {
                    event.name = $scope.event.name;
                } else {
                    return;
                }
                if ($scope.event.city) {
                    $.each($scope.cities, function (j, city) {
                        if ($scope.event.city == city.name) {
                            event.cityId = city.id;
                        }
                    });
                } else {
                    return;
                }
                if ($scope.event.about) {
                    event.about = $scope.event.about;
                } else {
                    return;
                }
                if ($scope.event.description) {
                    event.description =  $scope.event.description;
                }
                if ($scope.event.startTime) {
                    event.startTime =  $scope.event.startTime;
                }
                if ($scope.event.finishTime) {
                    event.finishTime =  $scope.event.finishTime;
                }
                if ($scope.event.image && ($scope.event.image !== $scope.defaultEventImage)) {
                    event.image = $scope.event.image;
                }
                $http.post('/your-city/rest/admin/event/create/' + eventType, angular.toJson(event), {
                    headers: {'Content-Type': 'application/json'}
                })
                    .success(function () {
                        $scope.progressBar = false;
                        $scope.created = true;
                        $scope.hasError = false;
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

        }]);
