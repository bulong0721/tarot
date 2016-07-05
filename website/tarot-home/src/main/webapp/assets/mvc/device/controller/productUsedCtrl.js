angular.module('inspinia', [])
    .controller('productUsedCtrl', productUsedCtrl);

/**
 * productUsedCtrl - controller
 */
productUsedCtrl.$inject = ['$scope', '$resource','Constants','cTables'];
function productUsedCtrl($scope, $resource, Constants,cTables) {
    var productOpts = Constants.productOpts;

    var mgrData = {
        fields: [
            {
                'id': 'store.name',
                'key': 'store.name',
                'type': 'input',
                'templateOptions': {'disabled': true, 'label': '门店名称', 'placeholder': '门店名称'}
            },
            {
                'key': 'code',
                'type': 'input',
                'templateOptions': {'label': '产品编号', required: true, 'placeholder': '产品编号'}
            },
            {
                'key': 'type',
                'type': 'select',
                'templateOptions': {
                    'label': '产品名称',
                    required: true,
                    'placeholder': '产品名称',
                    valueProp: 'type',
                    labelProp: 'friendlyType',
                    options: productOpts
                }
            },
            {
                'key': 'productNum',
                'type': 'input',
                'templateOptions': {'label': '产品版本', required: true, 'placeholder': '产品版本'}
            },
            {'key': 'description', 'type': 'input', 'templateOptions': {'label': '描述', 'placeholder': '描述'}}
        ],
        api: {
            read: '/product/used/paging',
            update: '/product/used/save',
            updateAttr: '/product/attribute/save',
            deleteAttr: '/product/attribute/delete',
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
            console.log(attr)
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