angular.module('myee', [])
    .controller('pushCtrl', pushCtrl);

/**
 * pushCtrl - controller
 */
pushCtrl.$inject = ['$scope', '$resource'];
function pushCtrl($scope, $resource) {
    $scope.treeData = [{path: '/', type: 0}];
    // $resource('../file/search').get({node: "root"},{},function success(resp){
    //     console.log(resp.rows)
    //     $scope.treeData = resp.rows;
    //});

    $scope.treeColumns = [
        {field: 'modified', displayName: '修改时间', columnWidth: '20%'},
        {displayName: '类型', columnWidth: '10%'},
        {field: 'size', displayName: '大小', columnWidth: '10%'},
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