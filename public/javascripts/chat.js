var app = angular.module('chatApp', ['ngMaterial']);

app.controller('chatController', function ($scope) {
    $scope.messages = [
        {
            'sender': 'USER',
            'text': 'Hello'
    },
        {
            'sender': 'BOT',
            'text': 'Hello, what can I do for you?'
    },
        {
            'sender': 'USER',
            'text': 'Help me find a nice place to eat'
    },
        {
            'sender': 'BOT',
            'text': 'Tell me where'
    }

	];
});