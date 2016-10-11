/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('tableTypeMgrCtrl', tableTypeMgrCtrl);

/**
 * roleCtrl - controller
 */
tableTypeMgrCtrl.$inject = ['$scope', 'cTables','cfromly'];

function tableTypeMgrCtrl($scope, cTables,cfromly) {
    var mgrData = {
        fields: [
            {key: 'name', type: 'c_input',className:'c_formly_line', templateOptions: {label: '名称', required: true, placeholder: '名称,50字以内',maxlength: 50}},
            {
                key: 'description',
                type: 'c_input',
                className:'c_formly_line',
                templateOptions: {label: '描述', required: true, placeholder: '描述,255字以内',maxlength: 255}
            },
            {key: 'capacity', type: 'c_input',className:'c_formly_line', templateOptions: {label: '容纳人数', placeholder: '容纳人数,11位以内数字',maxlength: 11,pattern: '^[0-9]*$'}},
            {
                key: 'minimum',
                type: 'c_input',
                className:'c_formly_line',
                templateOptions: {label: '最小就坐', required: true, placeholder: '最小就坐,11位以内数字',maxlength: 11,pattern: '^[0-9]*$'}
            }
        ],
        api: {
            read: './catering/type/paging',
            update: './catering/type/save',
            delete: './catering/type/delete'
        }
    };

    cTables.initNgMgrCtrl(mgrData, $scope)
}