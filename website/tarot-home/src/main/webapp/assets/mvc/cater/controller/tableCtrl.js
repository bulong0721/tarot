/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('tableMgrCtrl', tableMgrCtrl);

/**
 * roleCtrl - controller
 */
tableMgrCtrl.$inject = ['$scope', '$resource', 'cTables','cfromly'];

function tableMgrCtrl($scope, $resource,cTables,cfromly) {
    var typeOpts = $resource('/admin/catering/type/options').query();
    var zoneOpts = $resource('/admin/catering/zone/options').query();

    var mgrData = {
        fields: [
            {key: 'name', type: 'c_input',className:'c_formly_line', templateOptions: {label: '名称', required: true, placeholder: '名称'}},
            {
                key: 'description',
                type: 'c_input',
                className:'c_formly_line',
                templateOptions: {label: '描述', required: true, placeholder: '描述'}
            },
            {
                key: 'tableType.id',
                type: 'c_select',
                className:'c_formly_line c_select',
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
                className:'c_formly_line c_select',
                templateOptions: {label: '区域', valueProp: 'id', options: zoneOpts, placeholder: '区域'}
            },
        ],
        api: {
            read: '/admin/catering/table/paging',
            update: '/admin/catering/table/save'
        }
    };

    cTables.initNgMgrCtrl(mgrData, $scope);
}