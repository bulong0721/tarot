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
            '<a ng-click="$root.rightSidebar = !$root.rightSidebar"><i class="fa fa-tasks"></i>当前门店<span>{{merchantStoreSelect.name}}</span></a>'
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
            '<div class="input-group"><input type="text" class="form-control" placeholder="输入门店名或地址搜索"> <span class="input-group-btn"> <button type="button" class="btn btn-primary">走你!</button> </span></div>',
            '<div class="sidebar-message" ng-repeat="merchantStore in merchantStores">',
            '<a href="#"><div class="media-body">{{merchantStore.name}}<br>',
            '<small class="text-muted">{{merchantStore.address.province.name}}{{merchantStore.address.city.name}}</small>',
            '</div></a></div>'
        ].join(' '),
        link:function($scope){

            $scope.merchantStores=[
                {
                    "id": 7,
                    "address": {
                        "id": 2,
                        "new": false
                    },
                    "merchant": {
                        "businessType": "FOOD",
                        "cuisineType": "tttt2",
                        "description": "1232344124",
                        "id": 100,
                        "name": "商户100",
                        "new": false
                    },
                    "name": "门店7",
                    "experience": false,
                    "code": "23423432"
                },
                {
                    "id": 22,
                    "phone": "1",
                    "address": {
                        "address": "1",
                        "city": {
                            "code": "100000",
                            "id": 66,
                            "level": 2,
                            "name": "北京市",
                            "new": false
                        },
                        "id": 16,
                        "new": false,
                        "province": {
                            "code": "110000",
                            "id": 1,
                            "level": 1,
                            "name": "北京市",
                            "new": false
                        }
                    },
                    "merchant": {
                        "businessType": "FOOD",
                        "cuisineType": "tttt2",
                        "description": "1232344124",
                        "id": 100,
                        "name": "商户100",
                        "new": false
                    },
                    "name": "1",
                    "experience": false,
                    "code": "1"
                },
                {
                    "id": 23,
                    "phone": "1",
                    "address": {
                        "address": "1",
                        "city": {
                            "code": "066000",
                            "id": 70,
                            "level": 2,
                            "name": "秦皇岛市",
                            "new": false
                        },
                        "id": 17,
                        "new": false,
                        "province": {
                            "code": "130000",
                            "id": 3,
                            "level": 1,
                            "name": "河北省",
                            "new": false
                        }
                    },
                    "merchant": {
                        "businessType": "FOOD",
                        "cuisineType": "tttt2",
                        "description": "1232344124",
                        "id": 100,
                        "name": "商户100",
                        "new": false
                    },
                    "name": "2",
                    "experience": false,
                    "code": "1"
                },
                {
                    "id": 100,
                    "address": {
                        "circle": {
                            "code": "110000",
                            "id": 1,
                            "level": 1,
                            "name": "北京市",
                            "new": false
                        },
                        "city": {
                            "code": "110000",
                            "id": 1,
                            "level": 1,
                            "name": "北京市",
                            "new": false
                        },
                        "id": 250,
                        "mall": {
                            "code": "110000",
                            "id": 1,
                            "level": 1,
                            "name": "北京市",
                            "new": false
                        },
                        "new": false,
                        "province": {
                            "code": "110000",
                            "id": 1,
                            "level": 1,
                            "name": "北京市",
                            "new": false
                        }
                    },
                    "merchant": {
                        "businessType": "FOOD",
                        "cuisineType": "tttt2",
                        "description": "1232344124",
                        "id": 100,
                        "name": "商户100",
                        "new": false
                    },
                    "name": "门店100",
                    "experience": false,
                    "code": "23423432"
                }
            ];

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
