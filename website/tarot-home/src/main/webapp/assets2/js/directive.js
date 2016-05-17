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

function aceFileInput() {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var file = element;
            file.ace_file_input({
                style: 'well',
                btn_choose: '拖拽或选择图片文件到这里，图片大小不要超过2M',
                btn_change: null,
                no_icon: 'ace-icon fa fa-picture-o',
                droppable: true,
                thumbnail: 'small',
                preview_error: function (filename, error_code) {
                    //name of the file that failed
                    //error_code values
                    //1 = 'FILE_LOAD_FAILED',
                    //2 = 'IMAGE_LOAD_FAILED',
                    //3 = 'THUMBNAIL_FAILED'
                    //alert(error_code);
                }

            }).on('change', function () {
                //console.log($(this).data('ace_input_files'));
                //console.log($(this).data('ace_input_method'));
            });
        }
    };
}

angular
    .module('clover')
    .directive('sideMenu', sideMenu)
    .directive('ngAceFile', aceFileInput);