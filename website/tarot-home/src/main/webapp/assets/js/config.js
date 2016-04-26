/**
 * Created by Martin on 2016/4/12.
 */
function config($stateProvider, $urlRouterProvider, $httpProvider) {
    $urlRouterProvider.otherwise("/dashboard");

    function configState(routes) {
        angular.forEach(routes, function (route) {
            $stateProvider.state(route.state, route);
            if (route.children) {
                configState(route.children);
            }
        });
    };

    $.getJSON("../assets/route.json", function (data) {
        console.log(data);
        configState(data);
    });
}
angular
    .module('clover')
    .config(config)
    .run(function ($rootScope, $state) {
        $rootScope.$state = $state;
    });