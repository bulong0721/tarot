angular.module('myee', [])
    .controller('voiceLogCtrl', voiceLogCtrl);

/**
 * voiceLogCtrl - controller
 * @type {string[]}
 */
voiceLogCtrl.$inject = ['$scope', '$filter', 'cTables', 'Constants', 'cAlerts', 'toaster'];
function voiceLogCtrl($scope, $filter, cTables, Constants, cAlerts, toaster) {
    var mgrData = {
        fields: [],
        api: {
            read: '../voiceLog/paging',
            download: '../voiceLog/download'
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.download = function () {
        var beginTime = timeConverter(new Date($scope.where.beginDate));
        var endTime = timeConverter(new Date($scope.where.endDate));
        window.location.href = mgrData.api.download + "?" + ($scope.where.beginDate == undefined ? "beginDate=" : "beginDate=" + beginTime)
            + ($scope.where.endDate == undefined ? "&endDate=" : "&endDate=" + endTime)
            + ($scope.where.voiceLogType == undefined ? "&voiceLogType=" : "&voiceLogType=" + $scope.where.voiceLogType)
            + ($scope.where.keyword == undefined ? "&keyword=" : "&keyword=" + $scope.where.keyword);
    }

    function timeConverter(time) {
        var month = time.getMonth()+1;
        var timeAvailable = time.getFullYear() + "-" + month + "-" + time.getDate() + " 00:00:00";
        return timeAvailable;
    }

    $scope.today = function () {
        $scope.dt = new Date();
    };
    //$scope.today();

    $scope.clear = function () {
        $scope.dt = null;
    };

    $scope.inlineOptions = {
        customClass: getDayClass,
        minDate: new Date(),
        showWeeks: true
    };

    $scope.dateOptions = {
        dateDisabled: disabled,
        formatYear: 'yy',
        maxDate: new Date(2020, 5, 22),
        minDate: new Date(),
        startingDay: 1
    };

    // Disable weekend selection
    function disabled(data) {
        var date = data.date,
            mode = data.mode;
        return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
    }

    $scope.toggleMin = function () {
        $scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
        $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
    };

    $scope.toggleMin();

    $scope.open1 = function () {
        $scope.popup1.opened = true;
    };

    $scope.open2 = function () {
        $scope.popup2.opened = true;
    };

    $scope.setDate = function (year, month, day) {
        $scope.where.startDate = new Date(year, month, day);
        $scope.where.endDate = new Date(year, month, day);
    };

    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];
    $scope.altInputFormats = ['M!/d!/yyyy'];

    $scope.popup1 = {
        opened: false
    };

    $scope.popup2 = {
        opened: false
    };

    var tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    var afterTomorrow = new Date();
    afterTomorrow.setDate(tomorrow.getDate() + 1);
    $scope.events = [
        {
            date: tomorrow,
            status: 'full'
        },
        {
            date: afterTomorrow,
            status: 'partially'
        }
    ];

    function getDayClass(data) {
        var date = data.date,
            mode = data.mode;
        if (mode === 'day') {
            var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

            for (var i = 0; i < $scope.events.length; i++) {
                var currentDay = new Date($scope.events[i].date).setHours(0, 0, 0, 0);

                if (dayToCheck === currentDay) {
                    return $scope.events[i].status;
                }
            }
        }

        return '';
    }
}