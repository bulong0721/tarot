angular.module('myee', [])
    .controller('clientPrizeCheckCtrl', clientPrizeCheckCtrl);

/**
 * campaignCtrl - controller
 */
clientPrizeCheckCtrl.$inject = ['$scope', 'Constants','cTables','cfromly','toaster','$timeout'];
function clientPrizeCheckCtrl($scope, Constants,cTables,cfromly,toaster,$timeout) {
    var mgrData = {
        api: {
            read: './clientPrizeInfo/pagingListOfChecked'
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.checkCode = function(e) {
        //回车(enter)判断开始
        if(e){if((window.event?e.keyCode:e.which)!=13){return false;}}
        //回车(enter)判断结束

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
                $timeout(function () {toaster.error({ body:data.statusMessage})}, 0);
            }
        })

    }
}
