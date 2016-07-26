angular.module('myee', [])
    .controller('pushCtrl', pushCtrl);

/**
 * deviceCtrl - controller
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
        {field: 'name', cellTemplate: ''},
        {field: 'time', displayName: '修改时间'},
        {field: 'type', displayName: '类型'},
        {field: 'size', displayName: '大小'}
    ];

    $scope.handleSelect = function(data) {
        data.children.push({path: "111", size: 111, time: 111, type: "111"});
      console.log(data);
    };
}