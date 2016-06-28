/**
 * INSPINIA - Responsive Admin Theme
 *
 */
/**
 * 公共Controller放这里
 */
/**
 * MainCtrl - controller
 */
function mainCtrl() {
    this.userName = 'Martin.Xu';
    this.helloText = 'Welcome in SeedProject';
    this.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';
}

//function switchMerchantCtrl($scope, $resource, $compile, Constants, $state) {
//    Constants.getMerchants().then(function () {
//        //获取商户列表，并切换到之前切换的商户
//        Constants.getSwitchMerchant().then(
//            function () {
//                $scope.merchants = Constants.merchants;
//                if (Constants.thisMerchant)$scope.merchantSelect = Constants.thisMerchant;
//            }
//        );
//
//        //获取门店列表，并切换到之前切换的门店
//        Constants.getSwitchMerchantStore().then(
//            function () {
//                $scope.merchantStores = Constants.merchantStores;
//                if (Constants.thisMerchantStore)$scope.merchantStoreSelect = Constants.thisMerchantStore;
//            }
//        );
//
//        $scope.switchMerchant = function () {
//            $resource('/admin/merchant/switch').save($scope.merchantSelect.id, function (resp) {
//                Constants.flushThisMerchant(resp.rows[0].id, Constants.merchants);
//                $state.go($state.current, {}, {reload: true});
//
//            });
//        };
//
//        $scope.switchMerchantStore = function () {
//            $resource('/admin/merchantStore/switch').save($scope.merchantStoreSelect.value, function (resp) {
//                Constants.flushThisMerchantStore(resp.rows[0].id, Constants.merchantStores);
//                $state.go($state.current, {}, {reload: true});
//            });
//        };
//    });
//
//}

angular
    .module('inspinia')
    //.controller('switchMerchantCtrl', switchMerchantCtrl)
    .controller('mainCtrl', mainCtrl);