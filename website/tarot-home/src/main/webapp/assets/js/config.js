/**
 * INSPINIA - Responsive Admin Theme
 *
 * Inspinia theme use AngularUI Router to manage routing and views
 * Each view are defined as state.
 * Initial there are written state for all view in theme.
 *
 */
function treeLoader($ocLazyLoad) {
    return $ocLazyLoad.load([
        {
            files: ['assets/js/plugins/jsTree/style.min.css', 'assets/js/plugins/jsTree/jstree.min.js']
        }
    ]);
}

function managerLoader($ocLazyLoad) {
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
            files: ['assets/js/plugins/formly/angular-formly-templates-bootstrap.js']
        }
    ]);
}

function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider) {
    $urlRouterProvider.otherwise("/merchant/shop_list");

    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: false
    });

    // set templates here
    //formlyConfigProvider.setWrapper({
    //    name: 'horizontalBootstrapLabel',
    //    template: [
    //        '<label for="{{::id}}" class="col-sm-2 control-label">',
    //        '{{to.label}} {{to.required ? "*" : ""}}',
    //        '</label>',
    //        '<div class="col-sm-8">',
    //        '<formly-transclude></formly-transclude>',
    //        '</div>'
    //    ].join(' ')
    //});
    //
    //formlyConfigProvider.setType({
    //    name: 'horizontalInput',
    //    extends: 'input',
    //    wrapper: ['horizontalBootstrapLabel', 'bootstrapHasError']
    //});

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
            controller:"editMerchantCtrl",
            data: {pageTitle: '编辑商户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
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
                            files: ['assets/js/plugins/formly/angular-formly-templates-bootstrap.js']
                        }//,
                        //{
                        //    serie: true,
                        //    files: ['assets/js/plugins/dropzone/dropzone.js']
                        //}
                    ]);
                }
            }
        })
        .state('device', {
            abstract: true,
            url: "/device",
            templateUrl: "assets/views/content.html",
        })
        .state('device.type', {
            url: "/type",
            templateUrl: "assets/views/device/type.html",
            data: {pageTitle: '设备类型'}
        })
        .state('device.list', {
            url: "/list",
            templateUrl: "assets/views/device/list.html",
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
            data: {pageTitle: '资源管理'},
            resolve: {
                loadPlugin: treeLoader
            }
        })
        .state('explorer.push', {
            url: "/push",
            templateUrl: "assets/views/explorer/explorer.html",
            data: {pageTitle: '推送日志'}
        })
        .state('user', {
            abstract: true,
            url: "/user",
            templateUrl: "assets/views/content.html",
            resolve: {
                loadPlugin: managerLoader
            }
        })
        .state('user.user', {
            url: "/user",
            templateUrl: "assets/views/manager.html",
            controller: 'datatablesCtrl',
            data: {pageTitle: '用户管理'}
        })
        .state('user.user.datatable', {
            url: '/datatable',
            templateUrl: 'assets/views/user/user_datatable.html',
            data: {subTitle: '用户列表'}
        })
        .state('user.user.editor', {
            url: '/editor',
            templateUrl: 'assets/views/formly/user_editor.html',
            data: {subTitle: '编辑用户'}
        })
        .state('user.role', {
            url: "/role",
            templateUrl: "assets/views/manager.html",
            controller: 'datatablesCtrl',
            data: {pageTitle: '角色管理'}
        })
        .state('user.role.datatable', {
            url: "/datatable",
            templateUrl: "assets/views/user/role_datatable.html",
            data: {subTitle: '角色列表'}
        }).state('user.role.editor', {
            url: '/editor',
            templateUrl: 'assets/views/formly/basic_editor.html',
            data: {subTitle: '编辑角色'}
        });
}
angular
    .module('inspinia')
    .config(config)
    .run(function ($rootScope, $state) {
        $rootScope.$state = $state;
    });
