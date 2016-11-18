angular.module('myee', [])
    .controller('deviceTypeCtrl', deviceTypeCtrl);

/**
 * deviceCtrl - controller
 */
deviceTypeCtrl.$inject = ['$scope', '$resource', 'Constants', 'cTables', 'cfromly', 'cAlerts'];
function deviceTypeCtrl($scope, $resource, Constants, cTables, cfromly, cAlerts) {

    var mgrData = {
        fields: [
            {
                key: 'name',
                type: 'c_input',
                templateOptions: {label: '名称', required: true, placeholder: '名称,60字以内', maxlength: 60}
            },
            {
                key: 'versionNum',
                type: 'c_input',
                templateOptions: {label: '版本号', placeholder: '版本号,60字以内', maxlength: 60}
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
            read: './device/paging',
            update: './device/update',
            delete: './device/delete',
            updateAttr: './device/attribute/save',
            deleteAttr: './device/attribute/delete',
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);
    cTables.initAttrNgMgr(mgrData, $scope);

    $scope.tips = "*设备类型所有门店通用，不受切换门店影响";

}