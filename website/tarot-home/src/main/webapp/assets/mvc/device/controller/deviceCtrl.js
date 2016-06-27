angular.module('inspinia', [])
    .controller('deviceCtrl', deviceCtrl);

/**
 * deviceCtrl - controller
 */
deviceCtrl.$inject = ['$scope', 'Constants'];
function deviceCtrl($scope, Constants) {

    function actionsHtml(data, type, full, meta) {
        return '<a ng-click="goEditor(' + meta.row + ')"><i class="btn-icon fa fa-pencil bigger-130"></a>';
    }

    function detailsHtml(data, type, full, meta) {
        return '<a ng-click="goDetails(' + meta.row + ')"><i class="btn-icon fa fa-list-alt bigger-130"></a>';
    }

    var mgrData = {
        columns: [
            {title: '', width: 2, render: detailsHtml, className: 'details-control', orderable: false},
            {data: 'name', title: '名称', width: 40, orderable: false},
            {data: 'versionNum', title: '版本号', width: 40, orderable: false},
            {data: 'description', title: '描绘', width: 60, orderable: false},
            {title: '动作', width: 35, render: actionsHtml, className: 'center'}

        ],
        fields: [
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '名称', required: true, 'placeholder': '名称'}},
            {'key': 'versionNum', 'type': 'input', 'templateOptions': {'label': '版本号', 'placeholder': '版本号'}},
            {'key': 'description', 'type': 'input', 'templateOptions': {'label': '描述', 'placeholder': '描述'}}
        ],
        detailFields: [
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '参数名', required: true, 'placeholder': '参数名'}},
            {
                'key': 'parentId',
                'type': 'input',
                'templateOptions': {'disabled': true, 'label': '父节点ID', required: true, 'placeholder': '父节点ID'}
            },
            {'key': 'value', 'type': 'input', 'templateOptions': {'label': '参数值', required: true, 'placeholder': '参数值'}}
        ],
        api: {
            read: '/device/paging',
            update: '/device/update',
            updateDetail: '/device/attribute/save',
            attributeList: '/device/attribute/listByProductId',
            attributeDelete: '/device/attribute/delete'
        }

    };

    Constants.initMgrCtrl(mgrData, $scope);

}