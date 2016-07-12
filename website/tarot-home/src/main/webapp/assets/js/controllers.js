/**
 * 公共Controller放这里
 */
/**
 * MainCtrl - controller
 */
function mainCtrl($scope,cAlerts,$window) {
    this.userName = 'Martin.Xu';
    this.helloText = 'Welcome in SeedProject';
    this.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

    //通栏方法[登出,]
    var main = $scope.main = {
        logout:function(){//登出
            cAlerts.confirm('确定登出?',function(){
                $window.location.href = '/tarot/admin/j_spring_security_logout';
            },function(){

            });
        }
    }
}

angular
    .module('myee')
    .controller('mainCtrl', mainCtrl);