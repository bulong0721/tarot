/**
 * Created by Martin on 2016/6/27.
 */
angular.module('inspinia', [])
    .controller('roleMgrCtrl', roleMgrCtrl);

/**
 * roleCtrl - controller
 */
roleMgrCtrl.$inject = ['$scope', 'cTables'];

function roleMgrCtrl($scope, cTables) {
    var mgrOpts = {
        fields: [
            {
                'key': 'roleName',
                'type': 'input',
                'templateOptions': {'label': '角色名', required: true, 'placeholder': '角色名'}
            },
            {'key': 'description', 'type': 'input', 'templateOptions': {'label': '描述', 'placeholder': '描述'}}
        ],
        api: {
            read: '/admin/roles/paging',
            update: '/admin/roles/save'
        }
    };

    cTables.initNgMgrCtrl(mgrOpts, $scope);
}