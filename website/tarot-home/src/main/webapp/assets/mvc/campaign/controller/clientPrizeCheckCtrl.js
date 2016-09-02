angular.module('myee', [])
    .controller('clientPrizeCheckCtrl', clientPrizeCheckCtrl);

/**
 * campaignCtrl - controller
 */
clientPrizeCheckCtrl.$inject = ['$scope', 'Constants','cTables','cfromly','toaster'];
function clientPrizeCheckCtrl($scope, Constants,cTables,cfromly,toaster) {
    var mgrData = {
        api: {
            read: './clientPrizeInfo/pagingListOfChecked'
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.checkCode = function() {
        var data= $scope.where;
        if(data.checkCode==null){
            toaster.warning({ body:"请输入验证码！"});
            return;
        }
        $.post("./clientPrizeInfo/checkClientPriceInfo",{"checkCode":data.checkCode},function(data){
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
