toggleSwitch.addEventListener('change', switchTheme, false);
function loadOne() {
    console.log("Getting random fortune");
    $.ajax({
        url: "/fortune",
        success: function (data) {
            var container = $('#fortune');
            console.log("Adding Fortune: " + data);
            if(data) {
                container.append("<p>" + data.text + "</p>");
            } else {
                container.append("<p>Your future is murky...</p>");
            }
        },
        error: function () {
            var container = $('#fortune');
            container.append("<p>Your future is murky...</p>");
        }
    });
}

function loadAll() {
    console.log("Getting all fortunes");

    $.ajax({
        url: "/fortunes"
    }).then(function(data) {
        var container = $('#fortune');
        console.log("Adding Fortunes: " + data);
        container.empty();
        if(data) {
            $.each(data, function(index, value) {
                console.log("Adding fortune " + index);
                container.append("<p>" + value.text + "</p><br>");
            });
        } else {
            container.append("<p>-- Empty --</p>");
        }
    });
}

function handle(event) {
    if (event.keyCode == 13) {
        event.preventDefault();
        var dataObject = new Object();
        dataObject.text = input_fortune.value;
        console.log("Data: " + dataObject);
        $.ajax({
            url: "/fortune",
            method: "PUT",
            data: JSON.stringify(dataObject),
            dataType: 'json',
            contentType:"application/json; charset=utf-8",
            success: function () {
                location.reload();
            }
        });
    }
};
