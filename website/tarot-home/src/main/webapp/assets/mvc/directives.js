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
function switchMerchant(Constants,$resource,$state,$rootScope,NgTableParams,toaster,$q,$filter) {
    return {
        link:function($scope){

            $scope.mgrData = {
                api: {
                    read: 'merchantStore/listSwitch',
                }
            };

            $scope.merchantStoreSelect = {name:baseUrl.thisStoreName} ;

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
            $scope.filterBindOptions = function () {
                var deferred = $q.defer();
                //tables获取数据,获取可绑定的所有门店
                $scope.tableOpts = new NgTableParams({}, {
                    counts: [],
                    dataset: $filter('filter')($scope.initalBindProductList, $scope.where.queryName || "")//根据搜索字段过滤数组中数据
                });
                $scope.loadByInit = true;
                $scope.tableOpts.page(1);
                $scope.tableOpts.reload();
                deferred.resolve($scope.tableOpts);
                return deferred.promise;
            }

            function initalBindProduct() {
                //if ($scope.initalBindProductList) {//如果已经从后台读取过数据了，则不再访问后台获取列表
                //    var deferred = $q.defer();
                //    deferred.resolve($scope.initalBindProductList);
                //    return deferred.promise;
                //} else {//第一次需要从后台读取列表
                return $resource($scope.mgrData.api.read).get().$promise.then(function(data){
                    //初始化showCase.selected数组，给全选框用，让它知道应该全选哪些
                    $scope.initalBindProductList = data.rows;
                    return data.rows;
                });
                //}
            }

            $scope.goSwitch = function() {
                initalBindProduct().then(function () {
                    $scope.filterBindOptions().then(function () {
                        $scope.loadByInit = true;
                        $scope.tableOpts.page(1);
                        $scope.tableOpts.reload();
                    });
                });
            }

            //$scope.tableOpts = new NgTableParams({}, {
            //    counts: [],
            //    getData: function (params) {
            //        if (!$scope.loadByInit) {
            //            return [];
            //        }
            //        var xhr = $resource($scope.mgrData.api.read);
            //        var args = angular.extend(params.url(), $scope.where);
            //
            //        return xhr.get(args).$promise.then(function (data) {
            //            if (0 != data.status) {
            //                toaster.error({ body:"出错啦！"+resp.statusMessage});
            //                return;
            //            }
            //            //console.log($rootScope.storeInfo.firstSwitch)
            //            if($rootScope.storeInfo.firstSwitch){//第一次不弹提示,把标记位改变
            //                $rootScope.storeInfo.firstSwitch = false;
            //            }
            //            else{
            //                toaster.success({ body:"查询成功"});
            //            }
            //
            //            params.total(data.recordsTotal);
            //            return data.rows;
            //        });
            //    }
            //});

            //搜索tables的数据
            //$scope.search = function (e) {
            //    //回车(enter)判断
            //    if(e){if((window.event?e.keyCode:e.which)!=13){return false;}}
            //    //$scope.loadByInit = true;
            //    //$scope.tableOpts.page(1);
            //    //$scope.tableOpts.reload();
            //    $scope.goSwitch();
            //};

            //监听第一次点击切换门店，执行初始化搜索一次，执行完销毁该函数
            //$scope.watchFirstSwitch = $scope.$watch('storeInfo.firstSwitch',function(newValue,oldValue){
            //    if(newValue){
            //        $scope.search();
            //        $scope.watchFirstSwitch = null;
            //    }
            //});

            //监听打开切换门店页面，每次打开就搜索一次
            $scope.$watch('rightSidebar',function(newValue,oldValue){
                if(newValue){
                    $scope.goSwitch();
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
    function formatDate (strTime) {
        return new Date(strTime);
    }
    var d = formatDate((new Date()),"yyyy-MM-dd");
    return {
        template:'<div class="input-group"><input uib-datepicker-popup placeholder="{{placeholder}}" type="text"  ng-model="model" ng-model-options="{ timezone: \'+0800\' }" class="form-control" ng-click="datepicker.open($event)" is-open="datepicker.opened" datepicker-options="datepicker.dateOptions" /> <span class="input-group-btn"><button type="button" class="btn btn-default" ng-click="datepicker.open($event)"><i class="fa fa-calendar"></i></button></span> </div>',
        replace: true,
        scope:{
            placeholder:'@placeholder',
            model:'='
        },
        link:function(scope,ele,attr){
            scope.model = new Date(d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate());
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

function iboxToolsFullScreen($timeout) {
    return {
        restrict: 'A',
        scope: true,
        templateUrl: 'views/common/ibox_tools_full_screen.html',
        controller: function ($scope, $element) {
            // Function for collapse ibox
            $scope.showhide = function () {
                var ibox = $element.closest('div.ibox');
                var icon = $element.find('i:first');
                var content = ibox.find('div.ibox-content');
                content.slideToggle(200);
                // Toggle icon from up to down
                icon.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
                ibox.toggleClass('').toggleClass('border-bottom');
                $timeout(function () {
                    ibox.resize();
                    ibox.find('[id^=map-]').resize();
                }, 50);
            };
            // Function for close ibox
            $scope.closebox = function () {
                var ibox = $element.closest('div.ibox');
                ibox.remove();
            };
            // Function for full screen
            $scope.fullscreen = function () {
                var ibox = $element.closest('div.ibox');
                var button = $element.find('i.fa-expand');
                $('body').toggleClass('fullscreen-ibox-mode');
                button.toggleClass('fa-expand').toggleClass('fa-compress');
                ibox.toggleClass('fullscreen');
                setTimeout(function() {
                    $(window).trigger('resize');
                }, 100);
            }
        }
    };
}

/*
 * 远程监控
 * */
function metrics($ocLazyLoad,metrics,$timeout){
    return {
        restrict: 'E',
        template: '<div class="chart"></div>',
        replace: true,
        scope: {
            "options": "=",
            "type": "=",
            "period":"="
        },
        link: function (scope, element,attrs) {
            $ocLazyLoad.load('http://echarts.baidu.com/dist/echarts.common.min.js').then(function(){
                //设置饼图高度
                element[0].style.height = attrs.height + "px";
                element[0].style.width = attrs.width + "px";
                //监听
                scope.$watch('options', function(n,o) {
                    if(n){
                        setChart(n,scope.type,scope.period);
                    }
                });
            });
            function setChart(n,t,p){
                // 基于准备好的dom，初始化echarts图表
                var myChart = echarts.init(element[0]),options;
                //判断画图类型
                switch(n.drawType) {
                    case '0':
                        options = metrics.str(n,t,p);
                        break;
                    case '1':
                        options = metrics.pie(n,t,p);
                        break;
                    case '2':
                        options = metrics.bar(n,t,p);
                        break;
                    case '3':
                        options = metrics.area(n,t,p);
                        break;
                }
                if(options){
                    myChart.setOption(options);
                }
            }
        }
    };
}

//复制到剪切板
function copyToClip(toaster){
    return {
        restrict: 'E',
        template: '<div class="copyToClip cropLongString" ng-click="copy()" tooltip-placement="top" uib-tooltip="点击复制"><input value="{{content}}" /><span>{{content}}</span></div>',
        replace: true,
        scope: {
            "content": "="
        },
        link: function (scope,element,attrs) {

            scope.copy = function(){
                element[0].children[0].select();
                document.execCommand("Copy");
                toaster.success({ body:"复制成功"});
            }
        }
    };
}

//cameraVideo video.js
function cameraVideo($ocLazyLoad){
    return {
        //template:'<video id="{{id}}" class="video-js vjs-default-skin vjs-big-play-centered"><source src="{{rtmpSrc}}" type="rtmp/flv"/></video>',
        scope: {
            "id": "@id"
        },
        link:function(scope){
            $ocLazyLoad.load(['http://vjs.zencdn.net/5.0.2/video-js.min.css','http://vjs.zencdn.net/5.0.2/video.min.js']).then(function(){
                //videojs.options.flash.swf = "video-js.swf";
                videojs(scope.id).ready(function(){
                    this.play();
                    this.dimensions(320,240)
                });
            });
        }
    }
}

/*
    *高级搜索
    *<advanced-search title="" mgrdata="{{mgrData.fields}}" datepicker="true" shigh="true"></advanced-search>
    *mgrdata 搜索列表
    *datepicker是否有时间参数
    *shigh设置默认高级是否显示
 */
function advancedSearch(){
    return {
        template: [
            '<ul class="form-inline" ng-keyup="search($event)">',
            '<li ng-if="!sHigh && title"><div class="form-group"><input placeholder="{{title}}" ng-model="where.queryName" type="text" class="form-control" style="width:200px;"/></div></li>',
            '<li ng-if="datepicker"><div class="form-group m-l"><c-Datepicker placeholder="开始日期" model="where.queryObj.beginDate" style="width:150px;"></c-Datepicker><i class="fa fa-exchange"></i><c-Datepicker placeholder="结束日期" model="where.queryObj.endDate" style="width:150px;"></c-Datepicker></div></li>',
            '<li class="searchHigh" ng-if="sHigh"><div class="form-group searchHigh" ng-repeat="f in mgrData" ng-if="f.templateOptions.isSearch">',
            '<input ng-if="f.type == \'c_input\' || f.type == \'c_textarea\'" ng-model="where.queryObj[f.key]" type="text" class="form-control" placeholder="{{f.templateOptions.label}}"/>',
            '<select ng-if="f.type == \'c_select\'" ng-model="where.queryObj[f.key]" ng-init="where.queryObj[f.key]=\'\'" class="form-control m-l-sm">',
            '<option value="" selected="selected">选择{{f.templateOptions.label}}</option>',
            '<option ng-repeat="o in f.templateOptions.options" value="{{o.id || o.value}}">{{o.name}}</option>',
            '</select></div></li>',
            '<li><button class="btn btn-sm btn-danger m-l" type="button" ng-click="search()"><i class="fa fa-search"></i><span class="bold">搜索</span></button><span ng-if="!advancedSearch" class="m-l-xs" ng-click="sHighBut()">{{sHigh?"简化搜索":"高级搜索"}}</span></li>',
            '</ul>'
        ].join(' '),
        replace: true,
        link:function(scope,ele,attr){
            scope.advancedSearch = false;
            //判断sHigh使用service or directives
            if(attr.shigh){
                scope.sHigh = attr.shigh;
                scope.advancedSearch = true;
            }
            scope.title = attr.title;//简单搜索的input title
            scope.datepicker = attr.datepicker || false;//是否显示时间控件
            if(attr.mgrdata){
                scope.mgrData = JSON.parse(attr.mgrdata);//搜索列表
            }else{
                scope.advancedSearch = true;
            }
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
    .directive('metrics', metrics)
    .directive('copyToClip', copyToClip)
    .directive('iboxToolsFullScreen', iboxToolsFullScreen)
    .directive('advancedSearch', advancedSearch)
    .directive('cameraVideo', cameraVideo);