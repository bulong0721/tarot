angular.module('myee', [])
    .controller('campaignCtrl', campaignCtrl);

/**
 * campaignCtrl - controller
 */
campaignCtrl.$inject = ['$scope', 'Constants','cTables','cfromly','toaster'];
function campaignCtrl($scope, Constants,cTables,cfromly,toaster) {
    var mgrData = {
        fields: [
            {
                key: 'name',
                type: 'c_input',
                templateOptions: {type: 'text', label: '商户名称', required: true, placeholder: '商户名称'}
            },
            {
                key: 'businessType',
                type: 'c_select',
                className:'c_select',
                templateOptions: {
                    required: true,
                    label: '商户类型',
                    options: Constants.merchantType
                }
            },
            {
                key: 'cuisineType',
                type: 'c_input',
                templateOptions: {type: 'text', label: '商户菜系', required: true, placeholder: '商户菜系'}
            },
            {
                key: 'imgFile',
                type: 'c_input',
                templateOptions: {type: 'file', label: '商户图标', placeholder: '商户图标'}
            },
            {
                key: 'description',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'}
                },
                templateOptions: {label: '商户描述', placeholder: '商户描述', rows: 10,style: 'max-width:500px'}
            }
        ],
        api: {
            read: '../api/info/findHistoryInfoByStoreToday',
            //update: '../admin/merchant/save'
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.checkCode = function() {
        toaster.warning({ body:"hehehehe"})
        var data= $scope.where;
        if(data.checkCode==null){
            toaster.warning({ body:"请输入验证码！"});
            return;
        }
        $.post("../api/info/checkCode",{"checkCode":data.checkCode},function(data){
            if(data.status==0){
                toaster.success({ body:"验证成功"});
                $scope.loadByInit = true;
                $scope.tableOpts.reload();
            }else{
                toaster.error({ body:data.statusMessage});
            }
        })

    }
}
