angular.module('myee', [])
    .controller('tablePhoneMgrCtrl', tablePhoneMgrCtrl);

/**
 * merchantCtrl - controller
 */
tablePhoneMgrCtrl.$inject = ['$scope', 'Constants', 'cTables', 'cfromly', '$resource'];
function tablePhoneMgrCtrl($scope, Constants, cTables, cfromly, $resource) {
    var tableOpts = $resource('../admin/catering/table/options').query();
    var mgrData = {
        fields: [
            {
                key: 'table.id',
                type: 'c_select',
                className: 'c_formly_line c_select',
                templateOptions: {
                    required: true,
                    label: '餐桌',
                    valueProp: 'id',
                    options: tableOpts
                }
            },
            {
                key: 'phone',
                type: 'c_input',
                templateOptions: {
                    type: 'text',
                    label: '手机号',
                    required: true,
                    placeholder: '请输入11位手机号，多个以逗号隔开,不要空格',
                    pattern:'^(1[3578][0-9]|14[0-7])[0-9]{8}$|(^((1[3578][0-9]|14[0-7])[0-9]{8},)*(1[3578][0-9]|14[0-7])[0-9]{8}$)'
                }
            },
        ],
        api: {
            read: '../admin/catering/tablePhone/paging',
            update: '../admin/catering/tablePhone/save',
            delete: '../admin/catering/tablePhone/delete',
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);
}
