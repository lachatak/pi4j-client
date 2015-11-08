var webClient = angular.module('webClient', []);

webClient.directive("wcPin", function () {
    return {
        restrict: 'E',
        templateUrl: "template/pinButton.html",
        replace: true,
        scope: true
    }
});

webClient.controller('PinController', function ($scope) {
    function init() {
        $scope.pins = [];
        $scope.debugEnabled = false;
        $scope.messages = [];
        for (i = 0; i != 32; i++) {
            $scope.pins[i] = {id: i, mode: "disabled", value: null};
        }
        $scope.webSocket = new WebSocket("ws://localhost:8080/ws-echo");
        $scope.webSocket.onmessage = function(message) {
            $scope.$apply(function() {
                var data = JSON.parse(message.data);
                $scope.messages.push({direction: 'incoming', data: message.data});
                switch(data.type) {
                    case "init":
                        var pinsData = data.pins;
                        for(i = 0; i < pinsData.length; i++) {
                          $scope.pins[pinsData[i].id] = pinsData[i];
                        }
                        break;
                    case "stateChange":
                        var pinData = data.pin;
                        $scope.pins[pinData.id] = pinData;
                        break;
                }
            });
        };
    }

    init();

    $scope.pinChanged = function(pin) {
        var newValue = pin.value == "high" ? "low" : "high";
        var data = JSON.stringify({type: "pinChangeRequest",pin: {id: pin.id, mode: pin.mode, value: newValue}});
        $scope.webSocket.send(data);
        $scope.messages.push({direction: 'outgoing', data: data});
    };
});
