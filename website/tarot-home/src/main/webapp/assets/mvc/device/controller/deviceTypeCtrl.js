angular.module('myee', [])
    .controller('deviceTypeCtrl', deviceTypeCtrl);

/**
 * deviceCtrl - controller
 */
deviceTypeCtrl.$inject = ['$scope','$resource', 'Constants','cTables','cfromly','cAlerts'];
function deviceTypeCtrl($scope,$resource, Constants,cTables,cfromly,cAlerts) {

    var mgrData = {
        fields: [
            {key: 'name', type: 'c_input', templateOptions: {label: '名称', required: true, placeholder: '名称'}},
            {key: 'versionNum', type: 'c_input', templateOptions: {label: '版本号', placeholder: '版本号'}},
            {key: 'description', type: 'c_input',templateOptions: {label: '描述', placeholder: '描述'}}
        ],
        api: {
            read: './device/paging',
            update: './device/update',
            delete: './device/delete',
            updateAttr: './device/attribute/save',
            deleteAttr: './device/attribute/delete',
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