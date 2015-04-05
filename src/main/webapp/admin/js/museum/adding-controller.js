/**
 * Created by Andrey on 25.03.2015.
 */
angular.module('museumAdding', [])
    .controller('museumAddingCtrl', ['$scope', '$http', '$location', '$timeout',
        function ($scope, $http, $location, $timeout) {
            $scope.museum = {};
            $scope.museum.image = $scope.defaultMuseumAvatar;
            $scope.museum.city = '';
            $scope.cityList = [];
            var updateCities = function () {
                $scope.museum.city = $scope.cities[0].name;
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
                var requestParam = '';
                if ($scope.museum.name) {
                    requestParam = 'name=' + $scope.museum.name;
                } else {
                    return;
                }
                if ($scope.museum.city) {
                    $.each($scope.cities, function (j, city) {
                        if ($scope.museum.city == city.name) {
                            requestParam += '&cityId=' + city.id;
                        }
                    });
                } else {
                    return;
                }
                if ($scope.museum.address) {
                    requestParam += '&address=' + $scope.museum.address;
                }
                if ($scope.museum.email) {
                    requestParam += '&email=' + $scope.museum.email;
                }
                if ($scope.museum.description) {
                    requestParam += '&description=' + $scope.museum.description;
                }
                if ($scope.museum.about) {
                    requestParam += '&about=' + $scope.museum.about;
                }
                if ($scope.museum.phone) {
                    requestParam += '&phone=' + $scope.museum.phone;
                }
                var result = "";
                if ($scope.museum.image
                    && ($scope.museum.image !== $scope.defaultMuseumAvatar)) {
                    result = $scope.museum.image;
                }
                $http.post('/your-city/rest/admin/museum/add?' + requestParam, result, {
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

        }]);
