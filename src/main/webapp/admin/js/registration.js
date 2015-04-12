/**
 * Created by Andrey on 12.04.2015.
 */
angular.module('registration', [])
    .controller('registrationCtrl', ['$scope', '$http', '$location', '$timeout', '$routeParams',
        function ($scope, $http, $location, $timeout, $routeParams) {
            $scope.user = {};

            var createUser = function () {
                var user = {
                    name: $scope.user.name,
                    login: $scope.user.login,
                    password: $scope.user.password,
                    email: $scope.user.email
                };
                return user;
            };
            $scope.save = function () {
                $scope.badLogin = false;
                if (!$scope.user.password || !$scope.user.passwordDuplicate || $scope.user.password != $scope.user.passwordDuplicate) {
                    $scope.errorPassword = true;
                    return;
                }
                $scope.errorPassword = false;
                $http.post('/your-city/rest/admin/register', angular.toJson(createUser()),
                    {headers: {'Content-Type': 'application/json'}})
                    .success(function () {
                        $scope.created = true;
                        $timeout(function () {
                            $scope.created = false;
                        }, 2000);
                    })
                    .error(function (data) {
                        debugger;
                        if (data == 'User with the login exists.') {
                            $scope.badLogin = true;
                        } else {
                            $scope.error = true;
                            $timeout(function () {
                                $scope.error = false;
                            }, 4000);
                        }
                    });
            };
        }]);