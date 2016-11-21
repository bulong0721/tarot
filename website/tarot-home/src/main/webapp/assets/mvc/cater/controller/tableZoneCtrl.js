/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('tableZoneMgrCtrl', tableZoneMgrCtrl);

/**
 * roleCtrl - controller
 */
tableZoneMgrCtrl.$inject = ['$scope', 'cTables','cfromly'];

function tableZoneMgrCtrl($scope, cTables,cfromly) {
    var mgrData = {
        fields: [
            {key: 'name', type: 'c_input',className:'c_formly_line', templateOptions: {'label': '名称', required: true, placeholder: '名称,50字以内',maxlength: 50}},
            {
                key: 'description',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'}
                },
                templateOptions: {label: '描述', placeholder: '描述,255字以内', rows: 10, style: 'max-width:500px',maxlength:255}
            },
        ],
        api: {
            read: './catering/zone/paging',
            update: './catering/zone/save',
            delete: './catering/zone/delete',
        }
    };

    cTables.initNgMgrCtrl(mgrData, $scope);
}