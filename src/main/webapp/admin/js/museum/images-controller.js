/**
 * Created by Andrey on 28.03.2015.
 */
angular.module('museumImages', [])
    .controller('museumImagesCtrl', ['$scope', '$http', '$routeParams', '$timeout',
        function ($scope, $http, $routeParams, $timeout) {
            $scope.images = [];
            var imageOriginalKeeper = [],
                idGenerator = -1;
            $scope.tryToGetImages = function () {
                $http.get('/rest/admin/museum/images?museumId=' + $routeParams.museumId)
                    .success(function (data) {
                        $scope.notFound = false;
                        $scope.hasError = false;
                        $scope.images = data;
                        imageOriginalKeeper = angular.copy(data);
                    })
                    .error(function (data, status) {
                        if (status == 404) {
                            $scope.notFound = true;
                        } else {
                            $scope.hasError = true;
                        }
                    });
            };
            $scope.tryToGetImages();

            var getImageIndexById = function (id) {
                var result = null;
                $.each($scope.images, function (j, img) {
                    if (img.id == id) {
                        result = j;
                        return false;
                    }
                });
                return result;
            };

            $scope.cancel = function (img) {
                var index = getImageIndexById(img.id);
                if (img.id < 0) {
                    $scope.images.splice(index, 1);
                    imageOriginalKeeper.splice(index, 1);
                } else {
                    $scope.images[index] = angular.copy(imageOriginalKeeper[index]);
                    img.editMode = false;
                }
            };

            $scope.editMode = function (img) {
                img.editMode = true;
            };

            $scope.uploadImage = function (files, img) {
                $scope.file = files[0];
                var reader = new FileReader();
                reader.onload = function (e) {
                    $('#image-view-' + img.id).attr('src', e.target.result);
                    img.src = e.target.result;
                };
                reader.readAsDataURL(files[0]);
            };

            $scope.delete = function (img) {
                var $deleteButton = $('#delete-button-' + img.id),
                    $editButton = $('#edit-button-' + img.id);
                $deleteButton.attr('enable', false);
                $editButton.attr('enable', false);
                $http.post('/rest/admin/museum/image/delete?id=' + img.id)
                    .success(function () {
                        var index = getImageIndexById(img.id);
                        $scope.images.splice(index, 1);
                        imageOriginalKeeper.splice(index, 1);
                        $editButton.attr('enable', true);
                        $deleteButton.attr('enable', true);
                        $scope.deleted = true;
                        $timeout(function () {
                            $scope.deleted = false;
                        }, 2000);
                    })
                    .error(function () {
                        $editButton.attr('enable', true);
                        $deleteButton.attr('enable', true);
                        $scope.deleteError = true;
                        $timeout(function () {
                            $scope.deleteError = false;
                        }, 3000);
                    });
            };

            $scope.save = function (img) {
                var museumImage = {},
                    $saveButton = $('#save-button-' + img.id),
                    $cancelButton = $('#cancel-button-' + img.id);
                $saveButton.attr('enable', false);
                $cancelButton.attr('enable', false);
                museumImage.description = img.description;
                museumImage.src = img.src;
                museumImage.id = img.id;
                museumImage.museumId = $routeParams.museumId;
                $http.post('/rest/admin/museum/image/update?id=' + museumImage.id, angular.toJson(museumImage), {
                    headers: {'Content-Type': 'application/json'}
                })
                    .success(function (data) {
                        var index = getImageIndexById(img.id);
                        $scope.images[index] = angular.copy(data[0]);
                        imageOriginalKeeper[index] = angular.copy(data[0]);
                        $saveButton.attr('enable', true);
                        $cancelButton.attr('enable', true);
                        $scope.saved = true;
                        $timeout(function () {
                            $scope.saved = false;
                        }, 2000);
                    })
                    .error(function () {
                        $saveButton.attr('enable', true);
                        $cancelButton.attr('enable', true);
                        $scope.saveError = true;
                        $timeout(function () {
                            $scope.saveError = false;
                        }, 3000);
                    });
            };

            var generateId = function () {
                return idGenerator--;
            };
            $scope.addNewImage = function () {
                var img = {};
                img.src = $scope.defaultMuseumAvatar;
                img.description = null;
                img.id = generateId();
                img.editMode = true;
                imageOriginalKeeper.push(angular.copy(img));
                $scope.images.push(angular.copy(img));
                $timeout(function () {
                    document.getElementById('image-view-' + img.id).scrollIntoView();
                }, 500);
            };
        }]);