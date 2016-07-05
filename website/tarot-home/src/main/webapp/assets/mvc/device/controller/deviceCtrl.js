angular.module('inspinia', [])
    .controller('deviceCtrl', deviceCtrl);

/**
 * deviceCtrl - controller
 */
deviceCtrl.$inject = ['$scope','$resource', 'Constants','cTables','cfromly'];
function deviceCtrl($scope,$resource, Constants,cTables,cfromly) {

    var mgrData = {
        fields: [
            {'key': 'name', 'type': 'c_input', 'templateOptions': {'label': '名称', required: true, 'placeholder': '名称'}},
            {'key': 'versionNum', 'type': 'c_input', 'templateOptions': {'label': '版本号', 'placeholder': '版本号'}},
            {'key': 'description', 'type': 'c_input','templateOptions': {'label': '描述', 'placeholder': '描述'}}
        ],
        api: {
            read: '/device/paging',
            update: '/device/update',
            updateAttr: '/device/attribute/save',
            deleteAttr: '/device/attribute/delete',
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
            attr.editing = false;
        });
    };

    $scope.deleteAttr = function (product, attr) {
        var xhr = $resource(mgrData.api.deleteAttr);
        xhr.save({id: product.id}, attr).$promise.then(function (result) {
            var index = product.attributes.indexOf(attr);
            product.attributes.splice(index, 1);
        });
    };

    $scope.goEditorCustom = function (rowIndex) {
        $scope.goEditor(rowIndex);
        if (Constants.thisMerchantStore) {
            $scope.formData.model.store={name: Constants.thisMerchantStore.name};
        }
    };

}