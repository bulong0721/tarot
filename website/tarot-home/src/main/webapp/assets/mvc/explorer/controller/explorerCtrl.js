angular.module('myee', [])
    .controller('explorerCtrl', explorerCtrl);

/**
 * explorerCtrl - controller
 */
explorerCtrl.$inject = ['$scope', '$resource', '$filter'];
function explorerCtrl($scope, $resource, $filter) {
    $scope.treeData = [{path: '/', name: '/', type: 0}];
    $scope.expandField = {field: 'name'};
    $scope.treeColumns = [
        {
            displayName: '修改时间',
            columnWidth: '15%',
            cellTemplate: '<span>{{cellTemplateScope.format(row.branch)}}</span>',
            cellTemplateScope: {
                format: function (data) {
                    if (!data.modified) return "-";
                    return $filter('date')(new Date(data.modified), 'yyyy-MM-dd HH:mm:ss');
                }
            }
        },
        {
            displayName: '类型',
            columnWidth: '10%',
            cellTemplate: '<span>{{cellTemplateScope.text(row.branch)}}</span>',
            cellTemplateScope: {
                text: function (data) {
                    return data.type == 0 ? '目录' : '文件';
                },
                style: function (data) {
                    return data.type == 0 ? 'label label-primary' : 'label label-danger';
                }
            }
        },
        {
            displayName: '大小',
            columnWidth: '10%',
            cellTemplate: '<span>{{cellTemplateScope.format(row.branch)}}</span>',
            cellTemplateScope: {
                format: function (data) {
                    if (!data.size) return '-';
                    var value = data.size;
                    if (value > 1024 * 1024 * 1024) {
                        return Math.ceil(value / (1024 * 1024 * 1024)) + 'G';
                    }
                    if (value > 1024 * 1024) {
                        return Math.ceil(value / (1024 * 1024)) + 'M';
                    }
                    if (value > 1024) {
                        return Math.ceil(value / 1024) + 'K';
                    }
                    return value + 'B';
                }
            }
        },
        {
            displayName: '操作',
            columnWidth: '12%',
            cellTemplate: '<a><i class="btn-icon fa fa-plus" ng-click="cellTemplateScope.add(row.branch)"></i></a>' +
            '<span class="divider"></span>' +
            '<a><i class="btn-icon fa fa-pencil" ng-click="cellTemplateScope.edit(row.branch)"></i></a>' +
            '<span class="divider"></span>' +
            '<a><i class="btn-icon fa fa-trash-o" ng-click="cellTemplateScope.delete(row.branch)"></i></a>' +
            '<span class="divider"></span>' +
            '<a><i class="btn-icon fa fa-download" ng-click="cellTemplateScope.download(row.branch)"></i></a>',
            cellTemplateScope: {
                add: function (data) {
                    alert('add:' + data);
                },
                edit: function (data) {
                    alert('edit:' + data);
                },
                delete: function (data) {
                    alert('delete:' + data);
                },
                download: function (data) {
                    alert('download:' + data);
                }
            }
        }
    ];

    $scope.handleSelect = function (data) {
        if (data.type == 0) {
            $resource('../file/search').get({node: data.path}, {}, function success(resp) {
                angular.merge(data.children, resp.rows);
            });
        }
    };

    //点击推送按钮时调用
    $scope.goSend = function () {
        var arraySelected = [];
        var data = $scope.treeData;
        $scope.recursionTree(data, arraySelected);
        $resource('../file/search').save({}, JSON.stringify(arraySelected)).$promise.then(saveSuccess, saveFailed);
    };

    //递归出所有选中的文件
    $scope.recursionTree = function(data, arraySelected){
        angular.forEach(data, function(d){
            //console.log(d)
            if(d.checked == true && d.type == 1){
                arraySelected.push(d);
            }
            if(d.children.length > 0){
                $scope.recursionTree(d.children, arraySelected);
            }
        });
    }

    //成功后调用
    function saveSuccess(response) {

    }

    //失败调用
    function saveFailed(response) {
    }
}