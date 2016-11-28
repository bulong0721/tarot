/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('superMenuMgrCtrl', superMenuMgrCtrl);

/**
 * 小超人菜品列表 - controller
 */
superMenuMgrCtrl.$inject = ['$scope', 'cTables', 'cfromly','cResource','$filter'];

function superMenuMgrCtrl($scope, cTables, cfromly,cResource,$filter) {
    var mgrData = {
        fields: [
            {
                key: 'name',
                type: 'c_input',
                className: 'c_formly_line',
                templateOptions: {type: 'text', label: '菜品名称', required: true, placeholder: '菜品名称,最长60字',maxlength: 60}
            },
            {
                id: 'imgFile',
                key: 'photo',
                type: 'upload',
                name: 'img',//这个name是用来判断上传文件的类型，不判断为空('') || null
                templateOptions: {type: 'file', label: '菜品图片', required: false, placeholder: '菜品图片'}
            },
            {
                key: 'images',
                type: 'c_images',
                templateOptions: {label: '菜品图片预览', Multi: false}
            },
            {
                key: 'scanCode',
                type: 'c_input',
                className: 'c_formly_line',
                templateOptions: {
                    type: 'text',
                    label: '扫描码',
                    required: true,
                    placeholder: '扫描码,最长10位',
                    maxlength: 10,
                    pattern: '^[0-9]*$'
                }
            },
            {
                key: 'menuId',
                type: 'c_input',
                className: 'c_formly_line',
                templateOptions: {
                    type: 'text',
                    label: '品项编码',
                    required: true,
                    placeholder: '品项编码,最长10位',
                    maxlength: 10,
                    pattern: '^[0-9]*$'
                }
            },
            {
                key: 'subMenuId',
                type: 'c_input',
                className: 'c_formly_line',
                templateOptions: {
                    type: 'text',
                    label: '大类编码',
                    required: true,
                    placeholder: '大类编码,最长10位',
                    maxlength: 10,
                    pattern: '^[0-9]*$'
                }
            },
            {
                key: 'price',
                type: 'c_input',
                className: 'c_formly_line',
                templateOptions: {
                    type: 'text',
                    label: '价格',
                    required: true,
                    placeholder: '价格,最长10位,只能为整数',
                    maxlength: 10,
                    pattern: '^[1-9]{1}[0-9]*$'
                }
            },
            {
                key: 'active',
                type: 'c_input',
                className: 'formly-min-checkbox',
                templateOptions: {
                    label: '是否启用',
                    placeholder: '是否启用',
                    type: 'checkbox'
                }
            }
        ],
        api: {
            read: './superman/superMenu/paging',
            update: './superman/superMenu/save',
            delete: './superman/superMenu/delete',
            upload: './files/create',
            uploadMenu: './superman/superMenu/upload'
        }
    };

    cTables.initNgMgrCtrl(mgrData, $scope);

    var iEditor = 1;

    //点击编辑
    $scope.goEditor = function (rowIndex) {
        angular.element('#imgFile')[0].value = '';//清空input[type=file]value[ 垃圾方式 建议不要使用]
        if (rowIndex > -1) {
            var data = $scope.tableOpts.data[rowIndex];
            $scope.formData.model = angular.copy(data);
            //console.log(data)
            $scope.formData.model.images = $filter('myeeUrlImg')(data.photo);
            $scope.rowIndex = rowIndex;
        } else {
            $scope.formData.model = {};
            $scope.rowIndex = -1;
        }
        $scope.activeTab = iEditor;
    };

    //console.log( $filter('date')(new Date().getTime(), "yyyyMMdd") );
    //上传控件监听器
    $scope.$on('fileToUpload', function (event, arg) {
        //console.log(arg)
        //上传文件到后台
        cResource.upload(mgrData.api.upload,{path: "image/superMenu/"+$filter('date')(new Date().getTime(), "yyyyMMdd"), type: "file"}, arg).then(function(){
            var pic = res.dataMap.tree.downloadPath;
            $scope.formData.model.photo = pic;
            $scope.formData.model.images = $filter('myeeUrlImg')( pic);
        });
    });

    //批量上传菜单
    $scope.menuUP = function(file){
        var fd = new FormData();
        fd.append('file', file.files[0]);
        cResource.save(mgrData.api.uploadMenu,{},fd).then(function(res){
            $scope.search();
        });
    };
}