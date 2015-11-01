var webClient = angular.module('webClient', []);

webClient.directive("wcPin", function () {
    return {
        restrict: 'E',
        templateUrl: "template/pinButton.html",
        replace: true,
        scope: {
            pin: '='
        }
    }
});

webClient.controller('PinController', function ($scope) {
    function init() {
        $scope.pins = [];
        for (i = 0; i != 26; i++) {
            $scope.pins[i] = {id: i, value: "disabled"};
        }
        $scope.webSocket = new WebSocket("ws://localhost:8080/ws-echo");
        $scope.webSocket.onmessage = function(m) {
            $scope.$apply(function() {
                var pin = JSON.parse(m.data);
                $scope.pins[pin.pinId].value = pin.pinValue;
            });
        };
    }
    init();
});
