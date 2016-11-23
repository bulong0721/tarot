/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('customerMgrCtrl', customerMgrCtrl);

/**
 * roleCtrl - controller
 */
customerMgrCtrl.$inject = ['$scope', 'cTables', 'cfromly'];

function customerMgrCtrl($scope, cTables, cfromly) {
    var mgrData = $scope.mgrData = {
        fields: [
            {key: 'firstName', type: 'c_input', templateOptions: {label: '姓氏', required: false, placeholder: '20字以内', maxlength: 20}},
            {key: 'lastName', type: 'c_input', templateOptions: {label: '名字', required: true, placeholder: '20字以内', maxlength: 20}},
            {
                key: 'username',
                type: 'c_input',
                templateOptions: {label: '登录名', required: true, placeholder: '登录名,40字以内', maxlength: 40,isSearch:true}
            },
            //{key: 'phoneNumber', type: 'c_input', templateOptions: {label: '电话号码', placeholder: '电话号码'}},
            {
                key: 'emailAddress',
                type: 'c_input',
                templateOptions: {type: 'email', label: '电子邮件', required: false, placeholder: '电子邮件,60字以内', maxlength: 60,isSearch:true}
            },
            {
                key: 'receiveEmail',
                type: 'c_input',
                className: 'formly-min-checkbox',
                templateOptions: {label: '接收邮件', placeholder: '接收邮件',type: 'checkbox'}
            },
            {
                key: 'deactivated',
                type: 'c_input',
                className: 'formly-min-checkbox',
                templateOptions: {label: '冻结账号', placeholder: '冻结账号',type: 'checkbox'}
            }
        ],
        api: {
            read: '../admin/customers/paging',
            update: '../admin/customers/save'
        }
    };

    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.tips = "*请切换不同门店，来管理不同门店的普通用户账号";
}