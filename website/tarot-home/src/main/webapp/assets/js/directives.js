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
        template: '<div class="showThisMerchant" ng-click="$root.rightSidebar = !$root.rightSidebar"><span>当前门店</span><span>{{storeInfo.name}}<i class="fa fa-chevron-down"></i></span></div>',
        link:function($scope){
            //获取门店列表，并切换到之前切换的门店
            Constants.getSwitchMerchantStore().then(function () {
                //Constants.getSwitchMerchant();//切换门店
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
function switchMerchant(Constants,$resource,$state,$rootScope) {
    return {
        template: [
            '<div class="ibox"><div class="ibox-title"><h5>切换门店</h5><div class="ibox-tools"> <a ng-click="$root.rightSidebar = !$root.rightSidebar"><i class="fa fa-times"></i></a></div></div><div class="ibox-content"><div class="form-group"><div class="input-group col-md-6"><input type="text" class="form-control" ng-model="nameFilter" placeholder="输入门店名或地址搜索"> </div></div>',
            '<div class="sidebar-message" ng-repeat="merchantStore in merchantStores | filter : nameFilter">',
            '<a ng-click="switchMerchantStore(merchantStore.id)" data-id="{{merchantStore.id}}"><div class="media-body">{{merchantStore.name}}<br>',
            '<small class="text-muted">{{merchantStore.address.province.name}}{{merchantStore.address.city.name}}</small></div></a></div></div>',
            '<pager page-count="pages.count" current-page="pages.page" on-page-change="pages.onPageChange()" page-first="false" page-last="false"></pager></div>',
        ].join(' '),
        link:function($scope){
            // 从后台获取所有门店列表
            Constants.getMerchantStores().then(function(){
                var pages = $scope.pages = {
                    page:1,
                    count:Math.ceil(Constants.merchantStores.length/10),
                    data:function(start,end){
                        return Constants.merchantStores.slice(start,end);
                    },
                    onPageChange:function(){
                        $scope.merchantStores = this.data(pages.page*10-10,pages.page*10);
                    }
                };
            });

            //点击切换门店
            $scope.switchMerchantStore = function (id) {
                //console.log("id:"+id)
                $resource('/tarot/admin/merchantStore/switch').save(id, function (resp) {
                    //关闭侧边栏
                    $rootScope.rightSidebar = !$rootScope.rightSidebar;
                    //刷新当前页面的显示
                    $rootScope.storeInfo = $scope.merchantStoreSelect = Constants.thisMerchantStore = resp.rows[0];
                    $state.go($state.current, {}, {reload: true});
                });
            };
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
        template:'<div class="modal-header"> <h4 class="modal-title">{{title}}</h4> </div> <div class="modal-footer"> <button class="btn btn-primary" type="button" ng-click="ok()">确定</button> <button class="btn btn-warning" type="button" ng-click="cancel()">取消</button> </div>',
        link:function(scope,ele,attr){
            scope.title = attr.title;
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