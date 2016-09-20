/**
 * INSPINIA - Responsive Admin Theme
 *
 */

/**
 * pageTitle - Directive for set Page title - mata title
 */
function pageTitle($rootScope, $timeout) {
    return {
        link: function(scope, element) {
            var listener = function(event, toState, toParams, fromState, fromParams) {
                // Default title - load on Dashboard 1
                var title = '木爷终端系统';
                // Create your own title pattern
                if (toState.data && toState.data.pageTitle) title = toState.data.pageTitle + ' | 木爷终端系统';
                $timeout(function() {
                    element.text(title);
                });
            };
            $rootScope.$on('$stateChangeStart', listener);
        }
    }
}

/**
 * sideNavigation - Directive for run metsiMenu on sidebar navigation
 */
function sideNavigation($timeout) {
    return {
        restrict: 'A',
        link: function(scope, element) {
            // Call the metsiMenu plugin and plug it to sidebar navigation
            $timeout(function(){
                element.metisMenu();
            });
        }
    };
}


/**
 * minimalizaSidebar - Directive for minimalize sidebar
*/
function minimalizaSidebar($timeout) {
    return {
        restrict: 'A',
        template: '<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="" ng-click="minimalize()"><i class="fa fa-bars"></i></a>',
        controller: function ($scope, $element) {
            $scope.minimalize = function () {
                $("body").toggleClass("mini-navbar");
                if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
                    // Hide menu in order to smoothly turn on when maximize menu
                    $('#side-menu').hide();
                    // For smoothly turn on menu
                    setTimeout(
                        function () {
                            $('#side-menu').fadeIn(400);
                        }, 200);
                } else if ($('body').hasClass('fixed-sidebar')){
                    $('#side-menu').hide();
                    setTimeout(
                        function () {
                            $('#side-menu').fadeIn(400);
                        }, 100);
                } else {
                    // Remove all inline style from jquery fadeIn function to reset menu state
                    $('#side-menu').removeAttr('style');
                }
            }
        }
    };
}

/**
 * showThisMerchant
 */
function showThisMerchant(Constants,$rootScope) {
    return {
        template: '<div class="showThisMerchant" ng-click="showRightSideBar()"><span>当前门店</span><span>{{storeInfo.name}}<i class="fa fa-chevron-down"></i></span></div>',
        link:function($scope){
            $scope.showRightSideBar = function(){
                $rootScope.storeInfo.firstSwitch = true;
                $rootScope.rightSidebar = !$rootScope.rightSidebar;
            }

            //获取门店列表，并切换到之前切换的门店
            Constants.getSwitchMerchantStore().then(function () {
                Constants.thisMerchant = Constants.thisMerchantStore.merchant;
                if (Constants.thisMerchantStore){
                    $rootScope.storeInfo = Constants.thisMerchantStore
                };
            });
        }
    };
}

/**
 * switchMerchant
 */
function switchMerchant(Constants,$resource,$state,$rootScope,NgTableParams,toaster,$q) {
    return {
        link:function($scope){

            $scope.mgrData = {
                api: {
                    read: 'merchantStore/list',
                }
            };

            //初始化搜索配置
            $scope.where = {};

            //点击切换
            $scope.switch = function (rowIndex) {
                if (rowIndex > -1) {
                    var data = $scope.tableOpts.data[rowIndex];
                    $resource('merchantStore/switch').save(data.id, function (resp) {
                        if (0 != resp.status) {
                            toaster.error({ body:"出错啦！"+resp.statusMessage});
                            return;
                        }

                        //关闭侧边栏
                        $rootScope.rightSidebar = !$rootScope.rightSidebar;
                        //刷新当前页面的显示
                        $rootScope.storeInfo = $scope.merchantStoreSelect = Constants.thisMerchantStore = resp.rows[0];
                        Constants.thisMerchant = Constants.thisMerchantStore.merchant;
                        toaster.success({ body:"门店切换成功"});
                        $state.go($state.current, {}, {reload: true});
                    });
                }
            };

            //tables获取数据
            $scope.tableOpts = new NgTableParams({}, {
                counts: [],
                getData: function (params) {
                    if (!$scope.loadByInit) {
                        return [];
                    }
                    var xhr = $resource($scope.mgrData.api.read);
                    var args = angular.extend(params.url(), $scope.where);

                    return xhr.get(args).$promise.then(function (data) {
                        if (0 != data.status) {
                            toaster.error({ body:"出错啦！"+resp.statusMessage});
                            return;
                        }
                        //console.log($rootScope.storeInfo.firstSwitch)
                        if($rootScope.storeInfo.firstSwitch){//第一次不弹提示,把标记位改变
                            $rootScope.storeInfo.firstSwitch = false;
                        }
                        else{
                            toaster.success({ body:"查询成功"});
                        }

                        params.total(data.recordsTotal);
                        return data.rows;
                    });
                }
            });

            //搜索tables的数据
            $scope.search = function () {
                $scope.loadByInit = true;
                $scope.tableOpts.page(1);
                $scope.tableOpts.reload();
            };

            //监听第一次点击切换门店，执行初始化搜索一次，执行完销毁该函数
            $scope.watchFirstSwitch = $scope.$watch('storeInfo.firstSwitch',function(newValue,oldValue){
                if(newValue){
                    $scope.search();
                    $scope.watchFirstSwitch = null;
                }
            });

        }
    };
}

/**
 * pager
 */
function pager() {
    var pagexConfig = {visiblePageCount: 5, firstText: 'First', lastText: 'Last', prevText: '<上一页', nextText: '下一页>'};
    return {
        link: function(scope, element, attrs) {
            var visiblePageCount = angular.isDefined(attrs.visiblePageCount) ? attrs.visiblePageCount : pagexConfig.visiblePageCount;
            scope.firstText = angular.isDefined(attrs.firstText) ? attrs.firstText : pagexConfig.firstText;
            scope.lastText = angular.isDefined(attrs.lastText) ? attrs.lastText : pagexConfig.lastText;
            scope.prevText = angular.isDefined(attrs.prevText) ? attrs.prevText : pagexConfig.prevText;
            scope.nextText = angular.isDefined(attrs.nextText) ? attrs.nextText : pagexConfig.nextText;
            scope.currentPage = 1;
            scope.pageChange = function(page) {
                scope.currentPage = (page >= 1 && page <= scope.pageCount)?page:1;
            }
            function build() {
                var low, high, v;
                scope.pagenums = [];
                if (scope.pageCount == 0) {
                    return;
                }
                if (scope.currentPage > scope.pageCount) {
                    scope.currentPage = 1;
                }
                if (scope.pageCount <= visiblePageCount) {
                    low = 1;
                    high = scope.pageCount;
                } else {
                    v = Math.ceil(visiblePageCount / 2);
                    low = Math.max(scope.currentPage - v, 1);
                    high = Math.min(low + visiblePageCount - 1, scope.pageCount);
                    if (scope.pageCount - high < v) {
                        low = high - visiblePageCount + 1;
                    }
                }
                for (; low <= high; low++) {
                    scope.pagenums.push(low);
                }
                scope.onPageChange();
            }
            scope.$watch('currentPage+pageCount', function() {
                build();
            });
        },
        replace: true,
        restrict: "E",
        scope: {pageFirst: '=', pageLast: '=', pageCount: '=', currentPage: '=', onPageChange: '&'},
        template: '<ul class="pagination"><li ng-if="pageFirst" ng-click="pageChange(1)">{{firstText}}</li><li ng-click="pageChange(currentPage-1>0?currentPage-1:1)"><span>{{prevText}}</span></li><li ng-repeat="pagenum in pagenums" ng-click="pageChange(pagenum)" ng-class="{active:currentPage===pagenum}"><span>{{pagenum}}</span></li><li ng-click="pageChange(currentPage+1<=pageCount?currentPage+1:pageCount)"><span>{{nextText}}</span></li><li ng-if="pageLast" ng-click="pageChange(pageCount)"><span>{{lastText}}</span></li></ul>'
    }
}
/**
 * breadcrumb
 */

function breadcrumb(){
    return {
        template:'<div class="row wrapper border-bottom gray-bg page-heading"><div class="col-lg-10"><h1>{{pageTitle}}<small> <i class="fa fa-angle-double-right"></i> {{subTitle}}</small></h1></div>',
        link:function(scope,ele,attr){
            scope.$on('$stateChangeSuccess',function(event,toState){
                scope.pageTitle = toState.data.pageTitle;
                scope.subTitle = toState.data.subTitle;
            });
        }
    }
}

/*
* alerts
* */
function alerts(){
    return {
        template:'<div class="modal-header"> <h4 class="modal-title">{{title}}</h4> </div> <div class="modal-footer"> <button class="btn-xs btn-primary" type="button" ng-click="ok()">确定</button> <button class="btn-xs btn-warning" type="button" ng-click="cancel()">取消</button> </div>',
        link:function(scope,ele,attr){
            scope.title = attr.title;
        }
    }
}

/*cDatepicker*/
function cDatepicker(){
    return {
        template:'<div class="input-group"><input uib-datepicker-popup placeholder="{{placeholder}}" type="text"  ng-model="model" class="form-control" ng-click="datepicker.open($event)" is-open="datepicker.opened" datepicker-options="datepicker.dateOptions" /> <span class="input-group-btn"><button type="button" class="btn btn-default" ng-click="datepicker.open($event)"><i class="fa fa-calendar"></i></button></span> </div>',
        replace: true,
        scope:{
            placeholder:'@placeholder',
            model:'='
        },
        link:function(scope,ele,attr){
            //datepicker配置
            scope.datepicker = {
                opened:false,
                open:function($event){
                    scope.datepicker.opened = !scope.datepicker.opened;
                },
                dateOptions : {
                    maxDate: new Date(2020, 5, 22),
                    //minDate: new Date()
                }
            };
        }
    }
}

/*
* 视频播放
* */
function cVideo(){
    return {
        link: function (scope, ele,attr) {
            scope.$watch(attr.ngShow, function(val) {
                if(val){
                    ele.html('<video src="'+val+'" controls></video>');
                }
            });
        }
    }
}

/**
 *
 * Pass all functions into module
 */
angular
    .module('myee')
    .directive('pageTitle', pageTitle)
    .directive('sideNavigation', sideNavigation)
    .directive('minimalizaSidebar', minimalizaSidebar)
    .directive('switchMerchant', switchMerchant)
    .directive('showThisMerchant', showThisMerchant)
    .directive("pager", pager)
    .directive("breadcrumb", breadcrumb)
    .directive("alerts", alerts)
    .directive("cDatepicker", cDatepicker)
    .directive("cVideo", cVideo)