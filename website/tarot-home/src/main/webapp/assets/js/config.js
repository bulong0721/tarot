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
function ctrlManagerLoader(oclazyload, dir, ctrl) {
    return oclazyload.load([
        {
            name: 'myee',//属于哪个模块
            files: ['assets/mvc/' + dir + '/controller/' + ctrl]
        }
    ])
}

function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider,$httpProvider) {
    $urlRouterProvider.otherwise("/merchant/shop");

    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: false
    });

    $httpProvider.defaults.headers.post['Content-Type'] = undefined;

    $stateProvider
        .state('merchant', {
            abstract: true,
            url: "/merchant",
            template: "<div ui-view></div>",
            resolve: {
                loadPlugin: managerLoader
            }
        })
        .state('merchant.shop', {
            url: "/shop",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'merchantShopCtrl',
            data: {
                pageTitle: '门店管理',
                subTitle: '门店列表',
                datatable: 'assets/mvc/merchant/view/shop_datatable.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'merchant', 'shopCtrl.js');
                }
            }
        })
        .state('merchant.merchant', {
            url: "/merchant",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'merchantCtrl',
            data: {
                pageTitle: '门店管理',
                subTitle: '商户管理',
                datatable: 'assets/mvc/merchant/view/merchant_datatable.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'merchant', 'merchantCtrl.js')
                }
            }
        })
        .state('device', {
            abstract: true,
            url: "/device",
            template: "<div ui-view></div>",
            resolve: {
                loadPlugin: managerLoader
            }
        })
        .state('device.type', {
            url: "/type",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'deviceCtrl',
            data: {
                pageTitle: '设备管理',
                subTitle: '设备类型',
                datatable: 'assets/mvc/device/view/device_datatable.html',
                editor: 'assets/mvc/device/view/device_editor.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'device', 'deviceCtrl.js');
                }
            }
        })
        .state('device.list', {
            url: "/list",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: "deviceUsedCtrl",
            data: {
                pageTitle: '设备管理',
                subTitle: '设备列表',
                datatable: 'assets/mvc/device/view/device_used_datatable.html',
                editor: 'assets/mvc/device/view/device_used_editor.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'device', 'deviceUsedCtrl.js');
                }
            }
        })
        .state('device.product', {
            url: "/product",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'productUsedCtrl',
            data: {
                pageTitle: '设备组管理',
                subTitle: '设备组管理',
                datatable: 'assets/mvc/device/view/product_used_datatable.html',
                editor: 'assets/mvc/device/view/product_used_editor.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'device', 'productUsedCtrl.js');
                }
            }
        })
        .state('explorer', {
            abstract: true,
            url: "/explorer",
            template: "<div ui-view></div>",
            resolve: {
                loadPlugin: treeLoader,
                loadToaster:  function($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            insertBefore: '#loadBefore',
                            name: 'toaster',
                            files: ['assets/js/plugins/toastr/toastr.min.js', 'assets/css/plugins/toastr/toastr.min.css']
                        }
                    ]);
                }
            }
        })
        .state('explorer.explorer', {
            url: "/explorer",
            templateUrl: "assets/mvc/explorer/view/explorer.html",
            data: {pageTitle: '资源管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'explorer', 'explorerCtrl.js')
                }
            }
        })
        .state('explorer.push', {
            url: "/push",
            templateUrl: "assets/mvc/explorer/view/explorer.html",
            data: {pageTitle: '推送日志'}
        })
        .state('cater', {
            abstract: true,
            url: "/cater",
            template: "<div ui-view></div>",
            data: {pageTitle: '餐厅设置'},
            resolve: {
                loadPlugin: managerLoader
            }
        })
        .state('cater.type', {
            url: "/type",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'tableTypeMgrCtrl',
            data: {
                subTitle: '餐桌类型',
                datatable: 'assets/mvc/cater/view/type_datatable.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'cater', 'tableTypeCtrl.js')
                }
            }
        })
        .state('cater.zone', {
            url: "/zone",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'tableZoneMgrCtrl',
            data: {
                subTitle: '餐桌区域',
                datatable: 'assets/mvc/cater/view/zone_datatable.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'cater', 'tableZoneCtrl.js')
                }
            }
        })
        .state('cater.table', {
            url: "/table",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'tableMgrCtrl',
            data: {
                subTitle: '餐桌管理',
                datatable: 'assets/mvc/cater/view/table_datatable.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'cater', 'tableCtrl.js')
                }
            }
        })
        .state('user', {
            abstract: true,
            url: "/user",
            template: "<div ui-view></div>",
            resolve: {
                loadPlugin: managerLoader
            }
        })
        .state('user.user', {
            url: "/user",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'userMgrCtrl',
            data: {
                pageTitle: '用户管理',
                subTitle: '用户管理',
                datatable: 'assets/mvc/user/view/user_datatable.html',
                editor: 'assets/mvc/user/view/user_editor.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'user', 'userCtrl.js')
                }
            }
        })
        .state('user.role', {
            url: "/role",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'roleMgrCtrl',
            data: {
                pageTitle: '角色管理',
                subTitle: '角色管理',
                datatable: 'assets/mvc/user/view/role_datatable.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'user', 'roleCtrl.js')
                }
            }
        });
}
angular
    .module('myee')
    .config(config)
    .run(function ($rootScope, $state) {
        $rootScope.storeInfo = {};
        $rootScope.$state = $state;
    });
