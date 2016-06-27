angular.module('inspinia', [])
    .controller('merchantCtrl', merchantCtrl);

/**
 * merchantCtrl - controller
 */
merchantCtrl.$inject = ['$scope', '$resource','$compile', 'Constants'];
function merchantCtrl($scope, $resource, $compile, Constants) {
    function actionsHtml(data, type, full, meta) {
        return '<a ng-click="goEditor(' + meta.row + ')"><i class="btn-icon fa fa-pencil bigger-130"></a>';
    }


    var mgrData = {
        columns: [
            {data: 'id', title: '商户ID', width: 85, orderable: true},
            {data: 'name', title: '商户名称', width: 85, orderable: true},
            {data: 'businessTypeKey', title: '商户类型', width: 60, orderable: true},
            {data: 'cuisineType', title: '商户菜系', width: 55, orderable: true},
            {data: 'imgFile', title: '商户图标', width: 70, orderable: false, align: 'center'},
            {data: 'description', title: '商户描述', width: 70, orderable: false},
            {title: '动作', width: 35, render: actionsHtml}
        ],
        fields: [
            {
                'key': 'name',
                'type': 'input',
                'templateOptions': {'type': 'text', 'label': '商户名称', required: true, 'placeholder': '商户名称'}
            },
            {
                'key': 'businessType', 'type': 'select',
                'templateOptions': {
                    required: true,
                    'label': '商户类型',
                    'options': Constants.merchantType
                }
            },
            {
                'key': 'cuisineType',
                'type': 'input',
                'templateOptions': {'type': 'text', 'label': '商户菜系', required: true, 'placeholder': '商户菜系'}
            },
            {
                'key': 'imgFile',
                'type': 'input',
                'templateOptions': {'type': 'file', 'label': '商户图标', 'placeholder': '商户图标'}
            },
            {
                'key': 'description',
                'type': 'textarea',
                'templateOptions': {'label': '商户描述', 'placeholder': '商户描述', "rows": 10}
            }
        ],
        api: {
            read: '/admin/merchant/paging',
            update: '/admin/merchant/save'
        }
    };
    Constants.initMgrCtrl(mgrData, $scope);
}