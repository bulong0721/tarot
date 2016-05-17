/**
 * INSPINIA - Responsive Admin Theme
 *
 * Inspinia theme use AngularUI Router to manage routing and views
 * Each view are defined as state.
 * Initial there are written state for all view in theme.
 *
 */
function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider) {
    $urlRouterProvider.otherwise("/index/main");

    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: false
    });

    $stateProvider

        .state('index', {
            abstract: true,
            url: "/index",
            templateUrl: "assets/views/common/content.html",
        })
        .state('index.main', {
            url: "/main",
            templateUrl: "assets/views/main.html",
            data: { pageTitle: 'Main view' }
        })
        .state('index.minor', {
            url: "/minor",
            templateUrl: "assets/views/minor.html",
            data: { pageTitle: 'Minor view' }
        })
}
angular
    .module('inspinia')
    .config(config)
    .run(function($rootScope, $state) {
        $rootScope.$state = $state;
    });
