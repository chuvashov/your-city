/**
 * Created by Andrey on 26.03.2015.
 */
angular.module('museumEditing', [])
    .controller('museumEditingCtrl', ['$scope', '$http', '$location', '$timeout', '$routeParams',
        function ($scope, $http, $location, $timeout, $routeParams) {
            $scope.museum = $scope.getSavedMuseum();
            if ($routeParams.museumId == $scope.museum.id) {
                $scope.museumOriginal = angular.copy($scope.museum);
            } else {
                $scope.museum = {};
                $scope.museumOriginal = {};
                $http.get('/your-city/rest/admin/museum/id?id=' + $routeParams.museumId)
                    .success(function (data) {
                        $scope.museum = angular.copy(data[0]);
                        $scope.museumOriginal = data[0];
                        updateCities();
                    })
                    .error(function () {
                        $scope.errorLoadingMuseum = true;
                        $scope.errorMuseumId = $routeParams.museumId;
                        $timeout(function () {
                            $location.path('museums');
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
                if ($scope.museum.cityId) {
                    $.each($scope.cities, function (j, city) {
                        if (city.id == $scope.museum.cityId) {
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
            var $name = $("input[name='museumName']"),
                $address = $("input[name='museumAddress']"),
                $email = $("input[name='museumEmail']"),
                $description = $("input[name='museumDescription']"),
                $phone = $("input[name='museumPhone']"),
                $about = $("textarea[name='museumAbout']"),
                $image = $("input[name='museumImage']"),
                $img = $("#image-view");

            $scope.clear = function () {
                $scope.museum = {};
                $scope.museum.image = $scope.defaultMuseumAvatar;
                $name.val('');
                $address.val('');
                $email.val('');
                $description.val('');
                $phone.val('');
                $about.val('');
                $scope.removeImage();
            };

            $scope.cancel = function () {
                $location.path('museums');
            };

            $scope.save = function () {
                $scope.progressBar = true;
                $.each($scope.cities, function (j, city) {
                    if ($scope.cityName == city.name) {
                        $scope.museum.cityId = city.id;
                        return false;
                    }
                });
                $http.post('/your-city/rest/admin/museum/update?id=' + $routeParams.museumId, angular.toJson($scope.museum), {
                    headers: {'Content-Type': 'application/json'}
                })
                    .success(function () {
                        $scope.progressBar = false;
                        $scope.created = true;
                        $scope.hasError = false;
                        $scope.museumOriginal = angular.copy($scope.museum);
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
                $scope.museum.image = $scope.defaultMuseumAvatar;
                $image.val('');
                $img.attr('src', $scope.defaultMuseumAvatar);
                $scope.file = null;
            };

            $scope.uploadImage = function (files) {
                $scope.file = files[0];
                $scope.$apply($scope.imageChanged = true);
                var reader = new FileReader();
                reader.onload = function (e) {
                    $img.attr('src', e.target.result);
                    $scope.museum.image = e.target.result;
                };
                reader.readAsDataURL(files[0]);
            };

            $scope.setOriginalMuseum = function () {
                $scope.museum = angular.copy($scope.museumOriginal);
                chooseCity();
            };

            $scope.deleteMuseum = function () {
                $scope.deleteError = false;
                $scope.progressBar = true;
                $http.post('/your-city/rest/admin/museum/delete?id=' + $routeParams.museumId)
                    .success(function () {
                        $scope.deleted = true;
                        $timeout(function () {
                            $location.path('museums');
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
