angular.module('inspinia', [])
    .controller('merchantShopCtrl', merchantShopCtrl);

/**
 * merchantShopCtrl - controller
 */
merchantShopCtrl.$inject = ['$scope', '$resource','$compile', 'Constants'];
function merchantShopCtrl($scope, $resource, $compile, Constants) {
    //function actionsHtml(data, type, full, meta) {
    //    return '<a ng-click="goEditor(' + meta.row + ')"><i class="btn-icon fa fa-pencil bigger-130"></a>';
    //}
    //
    ////var key = ['id','name','address.province','address.city','address.county','address.circle','address.mall','address.address', 'phone','code','active'];
    ////var title = ['门店ID','门店名称', '省份','城市','区县','商圈','商场','地址','联系电话','门店码', '操作','动作'];
    //var mgrData = {
    //    columns: [
    //        {data: 'id', title: '门店ID', width: 85, orderable: true},
    //        {data: 'name', title: '门店名称', width: 85, orderable: false},
    //        {data: 'address.province.name', title: '省份', width: 60, orderable: true},
    //        {data: 'address.city.name', title: '城市', width: 55, orderable: true},
    //        {data: 'address.county.name', title: '区县', width: 70, orderable: false, align: 'center'},
    //        {data: 'address.circle.name', title: '商圈', width: 70, orderable: false},
    //        {data: 'address.mall.name', title: '商场', width: 65, orderable: false},
    //        {data: 'address.address', title: '地址', width: 100, orderable: false},
    //        {data: 'phone', title: '联系电话', width: 40, orderable: false},
    //        {data: 'code', title: '门店码', width: 40, orderable: false},
    //        {data: 'active', title: '操作', width: 40, orderable: false},
    //        {title: '动作', width: 35, render: actionsHtml}
    //    ],
    //    fields: [
    //        {
    //            'id': 'merchant.name',
    //            'key': 'merchant.name',
    //            'type': 'input',
    //            'templateOptions': {'disabled': true, 'label': '商户名称', 'placeholder': '商户名称'}
    //        },
    //        {
    //            'key': 'name',
    //            'type': 'input',
    //            'templateOptions': {'label': '门店名称', required: true, 'placeholder': '门店名称'}
    //        },
    //        {
    //            'key': 'address.province.id',
    //            'type': 'select',
    //            'templateOptions': {'label': '省份', 'options': Constants.provinces},
    //            expressionProperties: {
    //                'value'/*这个名字配置没用，市和区变化仍然会触发*/: function ($viewValue, $modelValue, scope) {
    //                    console.log("###省");
    //                    Constants.getCitysByProvince($viewValue, $scope);
    //                }
    //            }
    //        },
    //        {
    //            'key': 'address.city.id',
    //            'type': 'select',
    //            'templateOptions': {'label': '城市', 'options': Constants.citys},
    //            expressionProperties: {
    //                'value'/*这个名字配置没用，市和区变化仍然会触发*/: function ($viewValue, $modelValue, scope) {
    //                    console.log("###城市");
    //                    Constants.getDistrictsByCity($viewValue);
    //                }
    //            }
    //        },
    //        {
    //            'key': 'address.county.id',
    //            'type': 'select',
    //            'templateOptions': {'label': '区县', 'options': Constants.districts}
    //        },
    //        {
    //            'key': 'address.circle.id',
    //            'type': 'select',
    //            'templateOptions': {'label': '商圈', 'options': Constants.circles}
    //        },
    //        {
    //            'key': 'address.mall.id',
    //            'type': 'select',
    //            'templateOptions': {'label': '商场', 'options': Constants.malls}
    //        },
    //        {'key': 'address.address', 'type': 'input', 'templateOptions': {'label': '地址', 'placeholder': '地址'}},
    //        {'key': 'phone', 'type': 'input', 'templateOptions': {'label': '联系电话', 'placeholder': '联系电话'}},
    //        {'key': 'code', 'type': 'input', 'templateOptions': {'label': '门店码', required: true, 'placeholder': '门店码'}},
    //    ],
    //    api: {
    //        read: '/admin/merchantStore/pagingByMerchant',
    //        update: '/admin/merchantStore/save'
    //    }
    //
    //};
    //
    //Constants.initMgrCtrl(mgrData, $scope);


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
                        console.log("###省");
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
                        console.log("###城市");
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