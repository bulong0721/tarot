/**
 * Created by Martin on 2016/6/27.
 */
angular.module('inspinia', [])
    .controller('tableZoneMgrCtrl', tableZoneMgrCtrl);

/**
 * roleCtrl - controller
 */
tableZoneMgrCtrl.$inject = ['$scope', 'cTables','cfromly'];

function tableZoneMgrCtrl($scope, cTables,cfromly) {
    var mgrData = {
        fields: [
            {'key': 'name', 'type': 'c_input','className':'c_formly_line', 'templateOptions': {'label': '名称', required: true, 'placeholder': '名称'}},
            {
                'key': 'description',
                'type': 'c_input',
                'className':'c_formly_line',
                'templateOptions': {'label': '描述', required: true, 'placeholder': '描述'}
            },
        ],
        api: {
            read: '/admin/catering/zone/paging',
            update: '/admin/catering/zone/save',
            delete: '/admin/catering/zone/delete',
        }
    };

    cTables.initNgMgrCtrl(mgrData, $scope);
}