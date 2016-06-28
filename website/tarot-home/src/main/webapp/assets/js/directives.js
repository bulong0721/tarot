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
                var title = 'INSPINIA | Responsive Admin Theme';
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
 * iboxTools - Directive for iBox tools elements in right corner of ibox
 */
function iboxTools($timeout) {
    return {
        restrict: 'A',
        scope: true,
        templateUrl: 'assets/views/ibox_tools.html',
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
            },
                // Function for close ibox
                $scope.closebox = function () {
                    var ibox = $element.closest('div.ibox');
                    ibox.remove();
                }
        }
    };
}

/**
 * iboxTools with full screen - Directive for iBox tools elements in right corner of ibox with full screen option
 */
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
function showThisMerchant(Constants,$resource,$state) {

    return {
        template: [
            '<div class="showThisMerchant" ng-click="$root.rightSidebar = !$root.rightSidebar"><span>当前门店</span><span>{{merchantStoreSelect.name}}<i class="fa fa-chevron-down"></i></span></div>'
        ].join(' '),
        link:function($scope){
            $scope.merchantStoreSelect = {name:"门店100"};
            //获取门店列表，并切换到之前切换的门店
            //Constants.getSwitchMerchantStore().then(
            //    function () {
            //        if (Constants.thisMerchantStore){$scope.merchantStoreSelect = Constants.thisMerchantStore};
            //    }
            //);

        }
    };
}

/**
 * switchMerchant
 */
function switchMerchant(Constants,$resource,$state) {
    return {
        template: [
            '<div class="ibox"><div class="ibox-title"><h5>切换门店</h5><div class="ibox-tools"> <a ng-click="$root.rightSidebar = !$root.rightSidebar"><i class="fa fa-times"></i></a></div></div><div class="ibox-content"><div class="form-group"><div class="input-group col-md-6"><input type="text" class="form-control" placeholder="输入门店名或地址搜索"> <span class="input-group-btn"> <button type="button" class="btn btn-primary">走你!</button> </span></div></div>',
            '<div class="sidebar-message" ng-repeat="merchantStore in merchantStores">',
            '<a href="#" data-id="{{merchantStore.id}}"><div class="media-body">{{merchantStore.name}}<br>',
            '<small class="text-muted">{{merchantStore.address.province.name}}{{merchantStore.address.city.name}}</small></div></a></div></div>',
            '<pager page-count="pages.count" current-page="pages.page" on-page-change="pages.onPageChange()" page-first="false" page-last="false"></pager></div>',
        ].join(' '),
        link:function($scope){
            //模拟数据
            var data_ = [
                {"id":21,"address":{"id":2,"new":false},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"门店7","experience":false,"code":"23423432"},
                {"id":22,"phone":"1","address":{"address":"1","city":{"code":"100000","id":66,"level":2,"name":"北京市","new":false},"id":16,"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"1","experience":false,"code":"1"},
                {"id":23,"phone":"1","address":{"address":"1","city":{"code":"066000","id":70,"level":2,"name":"秦皇岛市","new":false},"id":17,"new":false,"province":{"code":"130000","id":3,"level":1,"name":"河北省","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"2","experience":false,"code":"1"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"3","experience":false,"code":"23423432"},
                {"id":25,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"4","experience":false,"code":"23423432"},
                {"id":26,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"5","experience":false,"code":"23423432"},
                {"id":27,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"6","experience":false,"code":"23423432"},
                {"id":28,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"7","experience":false,"code":"23423432"},
                {"id":29,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"8","experience":false,"code":"23423432"},
                {"id":30,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"9","experience":false,"code":"23423432"},
                {"id":31,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"10","experience":false,"code":"23423432"},
                {"id":32,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"11","experience":false,"code":"23423432"},
                {"id":33,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"12","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"13","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"14","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"15","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"16","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"17","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"18","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"19","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"20","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"21","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"22","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"23","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"24","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"25","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"26","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"27","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"28","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"29","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"30","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"31","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"32","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"33","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"34","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"35","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"36","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"37","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"38","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"39","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"40","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"41","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"42","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"43","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"44","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"45","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"46","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"47","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"48","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"49","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"50","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"51","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"52","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"53","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"54","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"55","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"56","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"57","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"58","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"59","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"60","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"61","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"62","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"63","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"64","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"65","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"66","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"67","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"68","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"69","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"70","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"71","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"72","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"73","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"74","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"75","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"76","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"77","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"78","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"79","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"80","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"81","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"82","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"83","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"84","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"85","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"86","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"87","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"88","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"89","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"90","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"91","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"92","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"93","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"94","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"95","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"96","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"97","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"98","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"99","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"100","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"101","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"102","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"103","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"104","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"105","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"106","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"107","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"108","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"109","experience":false,"code":"23423432"},
                {"id":24,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"110","experience":false,"code":"23423432"},
                {"id":158,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"111","experience":false,"code":"23423432"},
                {"id":159,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"112","experience":false,"code":"23423432"},
                {"id":160,"address":{"circle":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"city":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"id":250,"mall":{"code":"110000","id":1,"level":1,"name":"北京市","new":false},"new":false,"province":{"code":"110000","id":1,"level":1,"name":"北京市","new":false}},"merchant":{"businessType":"FOOD","cuisineType":"tttt2","description":"1232344124","id":100,"name":"商户100","new":false},"name":"113","experience":false,"code":"23423432"},
            ];
            //
            var pages = $scope.pages = {
                page:1,
                count:Math.ceil(data_.length/10),
                data:function(start,end){
                    return data_.slice(start,end);
                },
                onPageChange:function(){
                    $scope.merchantStores = this.data(pages.page*10-9,pages.page*10+1);
                }
            };

            //Constants.getMerchants().then(function () {
            //
            //    ////点击切换商户
            //    //$scope.switchMerchant = function () {
            //    //    $resource('/admin/merchant/switch').save($scope.merchantSelect.id, function (resp) {
            //    //        Constants.flushThisMerchant(resp.rows[0].id, Constants.merchants);
            //    //        $state.go($state.current, {}, {reload: true});
            //    //
            //    //    });
            //    //};
            //
            //
            //});

            //点击切换门店
            $scope.switchMerchantStore = function () {
                $resource('/admin/merchantStore/switch').save($scope.merchantStoreSelect.value, function (resp) {
                    Constants.flushThisMerchantStore(resp.rows[0].id, Constants.merchantStores);
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
    var pagexConfig = {
        visiblePageCount: 5,
        firstText: 'First',
        lastText: 'Last',
        prevText: '<上一页',
        nextText: '下一页>'
    }
    return {
        link: function(scope, element, attrs) {
            var visiblePageCount = angular.isDefined(attrs.visiblePageCount) ? attrs.visiblePageCount : pagexConfig.visiblePageCount;
            scope.firstText = angular.isDefined(attrs.firstText) ? attrs.firstText : pagexConfig.firstText;
            scope.lastText = angular.isDefined(attrs.lastText) ? attrs.lastText : pagexConfig.lastText;
            scope.prevText = angular.isDefined(attrs.prevText) ? attrs.prevText : pagexConfig.prevText;
            scope.nextText = angular.isDefined(attrs.nextText) ? attrs.nextText : pagexConfig.nextText;
            scope.currentPage = 1;
            scope.pageChange = function(page) {
                if (page >= 1 && page <= scope.pageCount) {
                    scope.currentPage = page;
                } else {
                    scope.currentPage = 1;
                }
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
        scope: {
            pageFirst: '=',
            pageLast: '=',
            pageCount: '=',
            currentPage: '=',
            onPageChange: '&'
        },
        template: '<ul class="pagination"><li ng-if="pageFirst" ng-click="pageChange(1)">{{firstText}}</li>' +
        '<li ng-click="pageChange(currentPage-1>0?currentPage-1:1)"><span>{{prevText}}</span></li>' +
        '<li ng-repeat="pagenum in pagenums" ng-click="pageChange(pagenum)" ng-class="{active:currentPage===pagenum}"><span>{{pagenum}}</span></li>' +
        '<li ng-click="pageChange(currentPage+1<=pageCount?currentPage+1:pageCount)"><span>{{nextText}}</span></li>' +
        '<li ng-if="pageLast" ng-click="pageChange(pageCount)"><span>{{lastText}}</span></li></ul>'
    }
}

/**
 *
 * Pass all functions into module
 */
angular
    .module('inspinia')
    .directive('pageTitle', pageTitle)
    .directive('sideNavigation', sideNavigation)
    .directive('iboxTools', iboxTools)
    .directive('minimalizaSidebar', minimalizaSidebar)
    .directive('iboxToolsFullScreen', iboxToolsFullScreen)
    .directive('switchMerchant', switchMerchant)
    .directive('showThisMerchant', showThisMerchant)
    .directive("pager", pager);