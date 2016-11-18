/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('userMgrCtrl', userMgrCtrl);

/**
 * roleCtrl - controller
 */
userMgrCtrl.$inject = ['$scope', 'cTables', 'cfromly','$rootScope'];

function userMgrCtrl($scope, cTables, cfromly, $rootScope) {
    var mgrData = {
        fields: [
            {key: 'name', type: 'c_input', templateOptions: {label: '昵称', required: true, placeholder: '昵称,20字以内', maxlength: 20}},
            {
                key: 'login',
                type: 'c_input',
                templateOptions: {label: '登录名', required: true, placeholder: '登录名,40字以内', maxlength: 40}
            },
            {
                key: 'phoneNumber',
                type: 'c_input',
                templateOptions: {
                    label: '电话号码',
                    placeholder: '电话号码,11位手机',
                    maxlength: 11,
                    pattern: '^(1[3578][0-9]|14[0-7])[0-9]{8}$|(^((1[3578][0-9]|14[0-7])[0-9]{8},)*(1[3578][0-9]|14[0-7])[0-9]{8}$)'
                }
            },
            {
                key: 'email',
                type: 'c_input',
                templateOptions: {type: 'email', label: '电子邮件', required: true, placeholder: '电子邮件,60字以内', maxlength: 60}
            },
            {
                key: 'activeStatusFlag',
                type: 'c_input',
                className: 'formly-min-checkbox',
                templateOptions: {label: '激活账号', placeholder: '激活账号',type: 'checkbox'},
                defaultValue: false
            }
        ],
        api: {
            read: '../admin/users/paging',
            update: '../admin/users/save',
            delete: '../admin/users/delete'
        }
    };

    cTables.initNgMgrCtrl(mgrData, $scope);
    $scope.tips = "*新建的管理员账号将绑定当前所切换的门店";
    $scope.userName = $rootScope.baseUrl.userName;
}