angular.module('myee', [])
    .controller('pushCtrl', pushCtrl);

/**
 * pushCtrl - controller
 */
pushCtrl.$inject = ['$scope', '$resource', '$filter'];
function pushCtrl($scope, $resource, $filter) {
    $scope.treeData = [{path: '/', type: 0}];
    // $resource('../file/search').get({node: "root"},{},function success(resp){
    //     console.log(resp.rows)
    //     $scope.treeData = resp.rows;
    //});

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
                    return data.type==0 ? '目录' : '文件';
                },
                style: function(data) {
                    return data.type==0 ? 'label label-primary' : 'label label-danger';
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
            columnWidth: '15%',
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
}