angular.module('myee', [])
    .controller('rollMainCtrl', rollMainCtrl);

/**
 * deviceCtrl - controller
 */
rollMainCtrl.$inject = ['$scope','$resource', 'Constants','cTables','cfromly','cAlerts'];
function rollMainCtrl($scope,$resource, Constants,cTables,cfromly,cAlerts) {

    var mgrData = {
        fields: [
            {key: 'title', type: 'c_input', templateOptions: {label: '活动标题', required: true, placeholder: '活动标题,100字以内',max:100}},
            {key: 'description', type: 'c_input',templateOptions: {label: '活动描述', placeholder: '活动描述,255字以内',max:255}},
        ],
        api: {
            read: './superman/rollMain/paging',
            update: './superman/rollMain/update',
            delete: './superman/rollMain/delete',
            updateAttr: './superman/rollDetail/save',
            deleteAttr: './superman/rollDetail/delete',
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.insertAttr = function (product) {
        if (!product.attributes) {
            product.attributes = [];
        }
        product.attributes.push({name: '', value: '', editing: true});
    };

    $scope.updateAttr = function (product, attr) {
        var xhr = $resource(mgrData.api.updateAttr);
        xhr.save({id: product.id}, attr).$promise.then(function (result) {
            if (0 != result.status) {
                $scope.toasterManage($scope.toastError,result);
                return;
            }
            $scope.toasterManage($scope.toastOperationSucc);
            attr.editing = false;
        });
    };

    $scope.deleteAttr = function (product, attr) {
        cAlerts.confirm('确定删除?',function(){
            //点击确定回调
            var xhr = $resource(mgrData.api.deleteAttr);
            xhr.save({id: product.id}, attr).$promise.then(function (result) {
                if (0 != result.status) {
                    $scope.toasterManage($scope.toastError,result);
                    return;
                }
                $scope.toasterManage($scope.toastDeleteSucc);
                var index = product.attributes.indexOf(attr);
                product.attributes.splice(index, 1);
            });
        },function(){
            //点击取消回调
        });

    };

}