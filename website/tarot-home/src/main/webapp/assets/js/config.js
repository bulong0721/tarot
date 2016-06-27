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
        },
        {
            serie: true,
            name: 'ngTable',
            files: ['assets/js/plugins/ng-table/ng-table.js', 'assets/js/plugins/ng-table/ng-table.css']
        },
    ]);
}

//动态加载controller
function ctrlManagerLoader(oclazyload,ctrl) {
    return oclazyload.load([
        {
            name: 'inspinia',//属于哪个模块
            files: ['assets/apps/'+ctrl+'.js']
        }
    ])
}

function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider) {
    $urlRouterProvider.otherwise("/merchant/shop");

    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: false
    });

    $stateProvider
        .state('merchant', {
            abstract: true,
            url: "/merchant",
            templateUrl: "assets/views/content.html",
            resolve: {
                loadPlugin: managerLoader
            }
        })
        .state('merchant.shop', {
            url: "/shop",
            templateUrl: "assets/views/manager.html",
            controller: 'merchantShopCtrl',
            data: {
                pageTitle: '门店管理',
                subTitle: '门店管理',
                datatable: 'assets/apps/merchant/view/shop_datatable.html',
                editor: 'assets/views/formly/basic_editor.html'
            },
            resolve: {
                loadPlugin: function($ocLazyLoad){
                    return ctrlManagerLoader($ocLazyLoad,'merchant/controller/shopCtrl');
                }
            }
        })
        .state('merchant.merchant', {
            url: "/merchant",
            templateUrl: "assets/views/manager.html",
            controller: 'merchantCtrl',
            data: {
                pageTitle: '门店管理',
                subTitle: '商户管理',
                datatable:'assets/apps/merchant/view/merchant_datatable.html',
                editor: 'assets/views/formly/basic_editor.html'
            },
            resolve: {
                loadPlugin: function($ocLazyLoad){
                    return ctrlManagerLoader($ocLazyLoad,'merchant/controller/merchantCtrl')
                }
            }
        })
        .state('device', {
            abstract: true,
            url: "/device",
            templateUrl: "assets/views/content.html",
            resolve: {
                loadPlugin: managerLoader
            }
        })
        .state('device.type', {
            url: "/type",
            templateUrl: "assets/views/manager.html",
            controller: 'deviceCtrl',
            data: {
                pageTitle: '设备管理',
                subTitle:'设备类型',
                datatable:'assets/apps/device/view/device_datatable.html',
                editor: 'assets/apps/device/view/product_used_editor.html'
            },
            resolve: {
                loadPlugin: function($ocLazyLoad){
                    return ctrlManagerLoader($ocLazyLoad,'device/controller/deviceCtrl');
                }
            }
        })
        .state('device.list', {
            url: "/list",
            templateUrl: "assets/views/manager.html",
            controller: "deviceUsedCtrl",
            data: {
                pageTitle: '设备管理',
                subTitle: '设备列表',
                datatable:'assets/apps/device/view/device_used_datatable.html',
                editor: 'assets/apps/device/view/device_used_editor.html'
            },
            resolve: {
                loadPlugin: function($ocLazyLoad){
                    return ctrlManagerLoader($ocLazyLoad,'device/controller/deviceUsedCtrl');
                }
            }
        })
        .state('device.product', {
            url: "/product",
            templateUrl: "assets/views/manager.html",
            controller: 'productUsedCtrl',
            data: {
                pageTitle: '产品管理',
                subTitle: '产品管理',
                datatable: 'assets/apps/device/view/product_used_datatable.html',
                editor: 'assets/apps/device/view/product_used_editor.html'
            },
            resolve: {
                loadPlugin: function($ocLazyLoad){
                    return ctrlManagerLoader($ocLazyLoad,'device/controller/productUsedCtrl');
                }
            }
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
        .state('cater', {
            abstract: true,
            url: "/cater",
            templateUrl: "assets/views/content.html",
            data: {pageTitle: '餐厅设置'},
            resolve: {
                loadPlugin: managerLoader
            }
        })
        .state('cater.type', {
            url: "/type",
            templateUrl: "assets/views/manager.html",
            controller: 'tableTypeMgrCtrl',
            data: {
                subTitle: '餐桌类型',
                datatable: 'assets/views/catering/type_datatable.html'
            }
        })
        .state('cater.zone', {
            url: "/zone",
            templateUrl: "assets/views/manager.html",
            controller: 'tableZoneMgrCtrl',
            data: {
                subTitle: '餐桌区域',
                datatable: 'assets/views/catering/zone_datatable.html'
            }
        })
        .state('cater.table', {
            url: "/table",
            templateUrl: "assets/views/manager.html",
            controller: 'tableMgrCtrl',
            data: {
                subTitle: '餐桌管理',
                datatable: 'assets/views/catering/table_datatable.html'
            }
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
            controller: 'userMgrCtrl',
            data: {
                pageTitle: '用户管理',
                subTitle: '用户管理',
                datatable: 'assets/views/user/user_datatable.html',
                editor: 'assets/views/formly/user_editor.html'
            }
        })
        .state('user.role', {
            url: "/role",
            templateUrl: "assets/views/manager.html",
            controller: 'roleMgrCtrl',
            data: {
                pageTitle: '角色管理',
                subTitle: '角色管理',
                datatable: 'assets/views/user/role_datatable.html'
            }
        });
}
angular
    .module('inspinia')
    .config(config)
    .run(function ($rootScope, $state) {
        $rootScope.$state = $state;
    });
