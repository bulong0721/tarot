/**
 * INSPINIA - Responsive Admin Theme
 *
 * Inspinia theme use AngularUI Router to manage routing and views
 * Each view are defined as state.
 * Initial there are written state for all view in theme.
 *
 */
function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider) {
    $urlRouterProvider.otherwise("/merchant/shop_list");

    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: false
    });

    $stateProvider
        .state('merchant', {
            abstract: true,
            url: "/merchant",
            templateUrl: "assets/views/content.html",
        })
        .state('merchant.shop_list', {
            url: "/shop_list",
            templateUrl: "assets/views/merchant/shop_list.html",
            data: {pageTitle: '店铺列表'}
        })
        .state('merchant.merchant', {
            url: "/merchant",
            templateUrl: "assets/views/merchant/merchant.html",
            data: {pageTitle: '编辑商户'}
        })
        .state('equipment', {
            abstract: true,
            url: "/equipment",
            templateUrl: "assets/views/content.html",
        })
        .state('equipment.type', {
            url: "/type",
            templateUrl: "assets/views/equipment/type.html",
            data: {pageTitle: '设备类型'}
        })
        .state('equipment.list', {
            url: "/list",
            templateUrl: "assets/views/equipment/list.html",
            data: {pageTitle: '设备列表'}
        })
        .state('explorer', {
            abstract: true,
            url: "/explorer",
            templateUrl: "assets/views/content.html",
        })
        .state('explorer.explorer', {
            url: "/explorer",
            templateUrl: "assets/views/explorer/explorer.html",
            data: {pageTitle: '资源管理'}
        })
        .state('explorer.push', {
            url: "/push",
            templateUrl: "assets/views/explorer/explorer.html",
            data: {pageTitle: '推送日志'}
        })
        .state('user', {
            abstract: true,
            url: "/user",
            templateUrl: "assets/views/content.html"
        })
        .state('user.user_list', {
            url: "/user_list",
            templateUrl: "assets/views/user/user_list.html",
            data: {pageTitle: '用户列表'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            serie: true,
                            files: ['assets/js/plugins/dataTables/datatables.min.js', 'assets/css/plugins/dataTables/datatables.min.css']
                        },
                        {
                            serie: true,
                            name: 'datatables',
                            files: ['assets/js/plugins/dataTables/angular-datatables.min.js', 'assets/css/plugins/dataTables/angular-datatables.min.css']
                        },
                        {
                            serie: true,
                            name: 'datatables.buttons',
                            files: ['assets/js/plugins/dataTables/angular-datatables.buttons.min.js']
                        },
                        {
                            serie: true,
                            files: ['assets/js/plugins/formly/api-check.min.js']
                        },
                        {
                            serie: true,
                            name: 'formly',
                            files: ['assets/js/plugins/formly/formly.min.js']
                        },
                        {
                            serie: true,
                            name: 'formlyBootstrap',
                            files: ['assets/js/plugins/formly/angular-formly-templates-bootstrap.min.js']
                        }
                    ]);
                }
            }
        })
        .state('user.role_list', {
            url: "/role_list",
            templateUrl: "assets/views/user/role_list.html",
            data: {pageTitle: '角色管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            serie: true,
                            name: 'ui.grid',
                            files: ['assets/js/plugins/ui-grid/ui-grid.min.js', 'assets/js/plugins/ui-grid/ui-grid.min.css']
                        },
                        {
                            name: 'ui.grid.selection',
                            files: ['assets/js/plugins/ui-grid/ui-grid.min.js']
                        },
                        {
                            name: 'ui.grid.pagination',
                            files: ['assets/js/plugins/ui-grid/ui-grid.min.js']
                        }
                    ]);
                }
            }
        })
}
angular
    .module('inspinia')
    .config(config)
    .run(function ($rootScope, $state) {
        $rootScope.$state = $state;
    });
