/**
 * Created by Martin on 2016/4/12.
 */
function sideMenu() {
    return {
        restrict: 'E',
        templateUrl: '../assets/views/component/side_menu.html',
        scope: {
            data: '='
        },
        controller: ['$scope', function ($scope) {
            $scope.expand = function (item, $event) {
                item.$$isExpand = !item.$$isExpand;
                $event.stopPropagation();
            };

            $scope.itemIcon = function (item) {
                return item.icon ? item.icon : "";
            };

            $scope.isLeaf = function (item) {
                return !item.children || !item.children.length;
            };

            $scope.warpCallback = function (callback, item, $event) {
                ($scope[callback] || angular.noop)({
                    $item: item,
                    $event: $event
                });
            };
        }]
    };
}

angular
    .module('clover')
    .directive('sideMenu', sideMenu);