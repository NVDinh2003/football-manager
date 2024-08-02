var stompClient = null;
var notificationCount = 0;

$(document).ready(function () {
    console.log("Index page is ready");
    connect();

    getUserInfo();

    $("#send-private").click(function () {
        sendPrivateMessage();
    });

    $("#notifications").click(function () {
        resetNotificationCount();
    });
});

function connect() {

    toastr.options = {
        "closeButton": true,
        "debug": false,
        "newestOnTop": true,
        "progressBar": true,
        "positionClass": "toast-top-right",
        "preventDuplicates": true,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };


    var socket = new SockJS('/stomp');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        console.log('Connected: ' + frame);
        updateNotificationDisplay();

        stompClient.subscribe('/user/topic/notifications', function (notification) {
            notificationCount += 1;
            updateNotificationDisplay();

            const noti = JSON.parse(notification.body);

            console.log("Received notification data:", noti);

            if (noti.title && noti.content) {
                const content = noti.content;
                const sender = noti.sender;
                const title = sender + ": " + noti.title;

                showNotification(title, content);
            } else {
                console.error("Unexpected message structure:", noti);
            }
        });

        stompClient.subscribe('/user/topic/private-messages', function (message) {
            const messageData = JSON.parse(message.body);
            if (messageData && messageData.content) {
                showMessage(messageData.content);
            } else {
                console.error("Unexpected message structure:", messageData);
            }
        });
    });
}

function showMessage(message) {
    $("#notifications").append("<tr><td>" + message + "</td></tr>");
    toastr.info(message, "New Message!");
}

function showNotification(title, content) {
    $("#notifications").append("<tr><td><strong>" + title + "</strong><br>" + content + "</td></tr>");

    toastr.info(content, title);
}

function sendPrivateMessage() {
    console.log("sending private message");
    stompClient.send("/ws/private-message", {}, $("#private-message").val());
}

function updateNotificationDisplay() {
    if (notificationCount === 0) {
        $('#inoti').hide();
    } else {
        $('#inoti').show();
        $('#inoti').text(notificationCount);
    }
}

function resetNotificationCount() {
    notificationCount = 0;
    updateNotificationDisplay();
}

function getUserInfo() {
    $.get("/api/users/me", function (response) {
        const user = response.data;

        console.log("Received user data:", user);

        if (user.username && user.name) {
            $("#username-display").text("Current user: " + user.username + " - " + user.name);
        } else {
            $("#username-display").text("User not logged in");
        }
    }).fail(function () {
        $("#username-display").text("Error fetching username");
    });
}
