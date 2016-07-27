angular.module('myee', [])
    .controller('pushCtrl', pushCtrl);

/**
 * pushCtrl - controller
 */
pushCtrl.$inject = ['$scope', '$resource'];
function pushCtrl($scope, $resource) {
    $scope.treeData = [
        {
            path: "USA", size: 9826675, time: 318212000, type: "UTC -5 to -10",
            children: [
                {
                    path: "California", size: 423970, time: 38340000, type: "Pacific Time",
                    children: [
                        {path: "San Francisco", size: 231, time: 837442, type: "PST"},
                        {path: "Los Angeles", size: 503, time: 3904657, type: "PST"}
                    ]
                },
                {
                    path: "Illinois", size: 57914, time: 12882135, type: "Central Time Zone",
                    children: [
                        {path: "Chicago", size: 234, time: 2695598, type: "CST"}
                    ]
                }
            ]
        },
        {path: "Texas", size: 268581, time: 26448193, type: "Mountain"}
    ];

    $scope.treeColumns = [
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
                add: function(data) {
                    alert('add:' + data);
                },
                edit: function(data) {
                    alert('edit:' + data);
                },
                delete: function(data) {
                    alert('delete:' + data);
                },
                download: function(data) {
                    alert('download:' + data);
                }
            }
        },
        {field: 'time', displayName: '修改时间', columnWidth: '20%'},
        {field: 'type', displayName: '类型', columnWidth: '10%'},
        {field: 'size', displayName: '大小', columnWidth: '10%'}
    ];

    $scope.handleSelect = function(data) {
        alert('数据懒加载');
        //$resource('...').query({}).then(function (result) {
        //    data.push({})
        //});
    };
}