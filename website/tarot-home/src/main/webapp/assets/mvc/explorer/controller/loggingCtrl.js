/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('loggingCtrl', loggingCtrl);

/**
 * loggingCtrl - controller
 */
loggingCtrl.$inject = ['$scope', '$resource', 'cTables', 'cfromly'];

function loggingCtrl($scope, $resource, cTables, cfromly) {
    var typeOpts = $resource('../admin/catering/type/options').query();
    var zoneOpts = $resource('../admin/catering/zone/options').query();

    var mgrData = {
        fields: [
            {
                key: 'name',
                type: 'c_input',
                className: 'c_formly_line',
                templateOptions: {label: '名称', required: true, placeholder: '名称'}
            },
            {
                key: 'description',
                type: 'c_input',
                className: 'c_formly_line',
                templateOptions: {label: '描述', required: true, placeholder: '描述'}
            },
            {
                key: 'tableType.id',
                type: 'c_select',
                className: 'c_formly_line c_select',
                templateOptions: {
                    label: '桌型',
                    valueProp: 'id',
                    options: typeOpts,
                    required: true,
                    placeholder: '桌型'
                }
            },
            {
                key: 'tableZone.id',
                type: 'c_select',
                className: 'c_formly_line c_select',
                templateOptions: {label: '区域', valueProp: 'id', options: zoneOpts, required: true, placeholder: '区域'}
            },
        ],
        api: {
            read: '../admin/catering/table/paging',
            update: '../admin/catering/table/save',
            delete: '../admin/catering/table/delete'
        }
    };

    cTables.initNgMgrCtrl(mgrData, $scope);
}