/**
 * Created by Martin on 2016/6/27.
 */
angular.module('inspinia', [])
    .controller('tableZoneMgrCtrl', tableZoneMgrCtrl);

/**
 * roleCtrl - controller
 */
tableZoneMgrCtrl.$inject = ['$scope', 'Constants'];

function tableZoneMgrCtrl($scope, Constants) {
    var mgrData = {
        fields: [
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '名称', required: true, 'placeholder': '名称'}},
            {
                'key': 'description',
                'type': 'input',
                'templateOptions': {'label': '描述', required: true, 'placeholder': '描述'}
            },
        ],
        api: {
            read: '/admin/catering/zone/paging',
            update: '/admin/catering/zone/save',
            delete: '/admin/catering/zone/delete',
        }
    };

    Constants.initNgMgrCtrl(mgrData, $scope);
}