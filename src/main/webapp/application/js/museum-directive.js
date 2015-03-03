/**
 * Created by Andrey on 03.03.2015.
 */
angular.module('museumApp', [])
    .directive('museumDirective', function() {
        return {
            restrict: 'A',
            templateUrl: '/application/tmpl/museums_view.html',
            replace: true
        }
    });