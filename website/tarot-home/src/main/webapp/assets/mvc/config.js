/**
 * INSPINIA - Responsive Admin Theme
 *
 * Inspinia theme use AngularUI Router to manage routing and views
 * Each view are defined as state.
 * Initial there are written state for all view in theme.
 *
 */
//动态加载controller
function ctrlManagerLoader(oclazyload, dir, ctrl) {
    return oclazyload.load([
        {
            serie: true,
            files: ['assets/plugins/formly/api-check.min.js']
        },
        {
            serie: true,
            name: 'formly',
            files: ['assets/plugins/formly/formly.min.js']
        },
        {
            serie: true,
            name: 'formlyBootstrap',
            files: ['assets/plugins/formly/angular-formly-templates-bootstrap.js']
        },
        {
            name: 'ngMessages',
            files: ['assets/plugins/angular/angular-messages.min.js']
        },
        {
            serie: true,
            name: 'ngTable',
            files: ['assets/plugins/ng-table/ng-table.js', 'assets/plugins/ng-table/ng-table.css']
        },
        {
            serie: true,
            name: 'treeGrid',
            files: ['assets/plugins/ui-tree/angular-tree-grid.css', 'assets/plugins/ui-tree/angular-tree-grid.js']
        },
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
            template: "<div ui-view></div>"
        })
        .state('merchant.shop', {
            url: "/shop",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'merchantShopCtrl',
            data: {
                pageTitle: '门店管理',
                subTitle: '门店列表',
                datatable: 'assets/mvc/merchant/view/shop_datatable.html',
                editor: 'assets/mvc/merchant/view/shop_editor.html'
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
        .state('campaign', {
            abstract: true,
            url: "/campaign",
            template: "<div ui-view></div>"
        })
        .state('campaign.priceCheck', {
            url: "/priceCheck",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'campaignCtrl',
            data: {
                pageTitle: '活动管理',
                subTitle: '抽奖管理',
                datatable: 'assets/mvc/campaign/view/priceCheck_datatable.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'campaign', 'campaignCtrl.js')
                }
            }
        })
        .state('device', {
            abstract: true,
            url: "/device",
            template: "<div ui-view></div>"
        })
        .state('device.type', {
            url: "/type",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'deviceTypeCtrl',
            data: {
                pageTitle: '设备管理',
                subTitle: '设备类型',
                datatable: 'assets/mvc/device/view/device_datatable.html',
                editor: 'assets/mvc/device/view/device_editor.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'device', 'deviceTypeCtrl.js');
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
            data: {pageTitle: '资源推送'}
        })
        .state('explorer.explorer', {
            url: "/explorer",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'explorerCtrl',
            data: {
                subTitle: '资源管理',
                datatable: 'assets/mvc/explorer/view/explorer_datatable.html',
                editor: 'assets/mvc/explorer/view/explorer_push.html',
                other1: 'assets/mvc/explorer/view/explorer_editor.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'explorer', 'explorerCtrl.js');
                }
            }
        })
        .state('explorer.logging', {
            url: "/logging",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'loggingCtrl',
            data: {
                subTitle: '推送日志',
                datatable: 'assets/mvc/explorer/view/logging_datatable.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'explorer', 'loggingCtrl.js')
                }
            }
        })
        .state('cater', {
            abstract: true,
            url: "/cater",
            template: "<div ui-view></div>",
            data: {pageTitle: '餐厅设置'}
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
                datatable: 'assets/mvc/cater/view/table_datatable.html',
                editor: 'assets/mvc/cater/view/table_editor.html'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'cater', 'tableCtrl.js')
                }
            }
        })
        .state('dataCenter', {
            abstract: true,
            url: "/dataCenter",
            template: "<div ui-view></div>"
        })
        .state('dataCenter.selfCheckLog', {
            url: "/selfCheckLog",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'logListCtrl',
            data: {
                pageTitle: '数据中心',
                subTitle: '自检日志',
                datatable: 'assets/mvc/datacenter/view/selfCheckLog_datatable.html',
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'datacenter', 'logCtrl.js');
                }
            }
        })
        .state('dataCenter.waittoken', {
            url: "/waittoken",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'waitTokenCtrl',
            data: {
                pageTitle: '数据中心',
                subTitle: '排队数据',
                datatable: 'assets/mvc/datacenter/view/waitToken_datatable.html',
                //editor: ''
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'datacenter', 'waitTokenCtrl.js')
                }
            }
        })
        .state('dataCenter.voiceLog', {
            url: "/voiceLog",
            templateUrl: "assets/mvc/desktop/view/manager.html",
            controller: 'voiceLogCtrl',
            data: {
                pageTitle: '数据中心',
                subTitle: '语音日志',
                datatable: 'assets/mvc/datacenter/view/voicelog_datatable.html',
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return ctrlManagerLoader($ocLazyLoad, 'datacenter', 'voiceLogCtrl.js');
                }
            }
        })
        .state('user', {
            abstract: true,
            url: "/user",
            template: "<div ui-view></div>"
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
