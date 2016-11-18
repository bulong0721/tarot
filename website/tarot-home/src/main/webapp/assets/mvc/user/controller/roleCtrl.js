/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('roleMgrCtrl', roleMgrCtrl);

/**
 * roleCtrl - controller
 */
roleMgrCtrl.$inject = ['$scope', 'cTables','cfromly'];

function roleMgrCtrl($scope, cTables,cfromly) {
    var mgrOpts = {
        fields: [
            {
                key: 'roleName',
                type: 'c_input',
                templateOptions: {label: '角色名', required: true, placeholder: '角色名,20字以内', maxlength: 20}
            },
            {
                key: 'description',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'}
                },
                templateOptions: {label: '描述', placeholder: '描述,255字以内', rows: 10, style: 'max-width:500px',maxlength:255}
            }
        ],
        api: {
            read: '../admin/roles/paging',
            update: '../admin/roles/save',
            delete: '../admin/roles/delete'
        }
    };

    cTables.initNgMgrCtrl(mgrOpts, $scope);
    $scope.tips = "*管理所有角色，不受切换门店影响";
}