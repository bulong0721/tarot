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

angular
    .module('inspinia')
    .controller('mainCtrl', mainCtrl);