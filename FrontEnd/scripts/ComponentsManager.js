var bGoogle = document.getElementById("dGoogle");
var bLogout = document.getElementById("bLogout");

function UIInit(){
    bLogout.className += " invisible";
}

function UILogin(){
    bGoogle.className += " invisible";
    bLogout.classList.remove("invisible");
}

function UILogout(){
    bGoogle.classList.remove("invisible");
    bLogout.className += " invisible";
}

function ShowSnack(message) {
    if(sessionStorage.getItem('lastMessage') != message){
        var x = document.getElementById("snackbar");
        x.innerHTML = message;
        x.className = "show";
            setTimeout(function() {
        x.className = x.className.replace("show", "");
        }, 3000);
        sessionStorage.setItem('lastMessage', message);
    }
}
