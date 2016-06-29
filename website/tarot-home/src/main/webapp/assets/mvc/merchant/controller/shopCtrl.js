angular.module('inspinia', [])
    .controller('merchantShopCtrl', merchantShopCtrl);

/**
 * merchantShopCtrl - controller
 */
merchantShopCtrl.$inject = ['$scope', '$resource','$compile', 'Constants'];
function merchantShopCtrl($scope, $resource, $compile, Constants) {

    var mgrData = {
        fields: [
            {
                'id': 'merchant.name',
                'key': 'merchant.name',
                'type': 'input',
                'templateOptions': {'disabled': true, 'label': '商户名称', 'placeholder': '商户名称'}
            },
            {
                'key': 'name',
                'type': 'input',
                'templateOptions': {'label': '门店名称', required: true, 'placeholder': '门店名称'}
            },
            {
                'key': 'address.province.id',
                'type': 'select',
                'templateOptions': {'label': '省份', 'options': Constants.provinces},
                expressionProperties: {
                    'value'/*这个名字配置没用，市和区变化仍然会触发*/: function ($viewValue, $modelValue, scope) {
                        Constants.getCitysByProvince($viewValue, $scope);
                    }
                }
            },
            {
                'key': 'address.city.id',
                'type': 'select',
                'templateOptions': {'label': '城市', 'options': Constants.citys},
                expressionProperties: {
                    'value'/*这个名字配置没用，市和区变化仍然会触发*/: function ($viewValue, $modelValue, scope) {
                        Constants.getDistrictsByCity($viewValue);
                    }
                }
            },
            {
                'key': 'address.county.id',
                'type': 'select',
                'templateOptions': {'label': '区县', 'options': Constants.districts}
            },
            {
                'key': 'address.circle.id',
                'type': 'select',
                'templateOptions': {'label': '商圈', 'options': Constants.circles}
            },
            {
                'key': 'address.mall.id',
                'type': 'select',
                'templateOptions': {'label': '商场', 'options': Constants.malls}
            },
            {'key': 'address.address', 'type': 'input', 'templateOptions': {'label': '地址', 'placeholder': '地址'}},
            {'key': 'phone', 'type': 'input', 'templateOptions': {'label': '联系电话', 'placeholder': '联系电话'}},
            {'key': 'code', 'type': 'input', 'templateOptions': {'label': '门店码', required: true, 'placeholder': '门店码'}},
        ],
        api: {
            read: '/admin/merchantStore/pagingByMerchant',
            update: '/admin/merchantStore/save'
        }
    };
    Constants.initNgMgrCtrl(mgrData, $scope);

    $scope.goEditorCustom = function (rowIndex) {
        $scope.goEditor(rowIndex);
        if (Constants.thisMerchant) {
            $scope.formData.model = {merchant: {name: Constants.thisMerchant.name}};
        }
    };
}