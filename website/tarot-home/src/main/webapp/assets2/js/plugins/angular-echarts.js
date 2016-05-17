/**
 * Created by Martin on 2015/7/23.
 */
angular.module('angular-echarts', []).directive('echarts', function ($resource) {
    return {
        restrict: 'EA',
        template: '<div></div>',
        scope: {
            optionUrl: '=',
            chartInstance: '=',
            callback: '='
        },
        link: function (scope, element, attributes) {
            var container, echarts, myChart, theme, isLaunched, init, requireCallback, refresh, onOptionsChanged;
            requireCallback = function (ec, th) {
                echarts = ec;
                theme = th;
                refresh();
                window.onresize = myChart.resize;
            };

            refresh = function () {
                if (myChart && myChart.dispose) {
                    myChart.dispose();
                }
                container = element[0];
                myChart = echarts.init(container, theme);
                scope.chartInstance = myChart;
                container.onresize = myChart.resize;
                window.onresize = myChart.resize;
                var optionUrl = scope.optionUrl;
                $resource(optionUrl).get({}, (function (data) {
                    myChart.setOption(data, true);
                }));
            };

            init = function () {
                if (isLaunched) {
                    return;
                }
                isLaunched = true;
                require.config({
                    paths: {
                        echarts: './assets/js/echarts'
                    }
                });
                require(
                    [
                        'echarts',
                        'echarts/theme/macarons',
                        'echarts/chart/line',
                        'echarts/chart/bar',
                        'echarts/chart/scatter',
                        'echarts/chart/k',
                        'echarts/chart/pie',
                        'echarts/chart/radar',
                        'echarts/chart/force',
                        'echarts/chart/chord',
                        'echarts/chart/gauge',
                        'echarts/chart/funnel',
                        'echarts/chart/eventRiver',
                        'echarts/chart/venn',
                        'echarts/chart/treemap',
                        'echarts/chart/tree',
                        'echarts/chart/wordCloud'
                    ],
                    requireCallback
                );
            };

            if (!scope.chartInstance) {
                scope.chartInstance = {};
            }

            onOptionsChanged = function () {
                init();
            };
            return scope.$watch('optionUrl', onOptionsChanged, true);
        }
    };
});
