(function () {
    var takePicture = document.querySelector("#take-picture");
    takePicture.onchange = function (event) {
        var files = event.target.files, file;
        if (files && files.length > 0) {
            file = files[0];
            console.log("file>>> " + file)
        }
    }
})();