/**
 * Created by Andrey on 10.04.2015.
 */
angular.module('loginApp', [])
    .controller('authCtrl', ['$scope', '$http',
        function ($scope, $http) {
            $scope.login;
            $scope.password;
            $scope.error = false;

            $scope.tryToLogin = function() {
                $http.post('/your-city/authenticate', angular.toJson({userId: $scope.login, password: $scope.password}),
                    {headers: {'Content-Type': 'application/json'}})
                    .success(function () {
                        document.location.href = 'admin.html';
                    })
                    .error(function () {
                        $scope.error = true;
                    })
            };
        }]);
