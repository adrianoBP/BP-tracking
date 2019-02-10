
// TODO-FE: Better layout
// TODO-FE: load Google Image
// TODO-FE/BE: handle new user
// TODO-BE: token TTL

// DOM values
var mainContainer = document.getElementById("container");
var canvasGraph = document.createElement("canvas");
canvasGraph.className += "mainCanvas";

var googleId = "";
var googleEmail = "";
var googleName = "";
var googleImageURL = "";

var myChart = new Chart(canvasGraph.getContext('2d'), {
    type: 'line',
    data: {
    },
    options: {
        maintainAspectRatio: false,
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        }
    }
});

Init();

// looping function
window.setInterval(function(){
    if(ReadCookie("authorizationToken")){
        GetPressureData();
    }
}, 3000);

// Google Sign in
function onSignIn(googleUser) {

    var profile = googleUser.getBasicProfile();
    // console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
    // console.log('Name: ' + profile.getName());
    // console.log('Image URL: ' + profile.getImageUrl());
    // console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.

    let latestToken = ReadCookie("authorizationToken");
    if(!latestToken){
        GoogleAuthentication(profile.getId());
    }else{
        UILogin();
        GetPressureData();
    }
}

function SignOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
        $.ajax({
            type: "POST",
            url: "https://projectsherlock.ddns.net/projects/BloodPressureMonitoring/BackEnd/api/Logout.php",
            data: JSON.stringify({"token":ReadCookie("authorizationToken")}),
            dataType: "json",
            success: function(response) {
                RemoveCookie("authorizationToken");
                UILogout();
                location.reload();
            },
            error: function() {
                ShowSnack("Error");
                location.reload();
            }
        });
    });
}

function StartGAuth() {
    gapi.load('auth2', function(){
    auth2 = gapi.auth2.init({
            client_id: '146276899170-ts6gbrgmlsobn2pugjhont9a4vbo13m0.apps.googleusercontent.com',
            cookiepolicy: 'single_host_origin',
        });
        AttachSignin(document.getElementById('dGoogle'));
    });
};

function AttachSignin(element) {
    let latestToken = ReadCookie("authorizationToken");
    if(!latestToken){
        auth2.attachClickHandler(element, {},
            function(googleUser) {
                var profile = googleUser.getBasicProfile();
                    GoogleAuthentication(profile.getId());
            }, function(error) {
                alert(JSON.stringify(error, undefined, 2));
            }
        );
    }else{
        UILogin();
        GetPressureData();
        GoogleInit();
    }
}

// External calls
function GoogleAuthentication(googleId){
    $.ajax({
        type: "POST",
        url: "https://projectsherlock.ddns.net/projects/BloodPressureMonitoring/BackEnd/api/GoogleAuthorization.php",
        data: JSON.stringify({"googleId":googleId}),
        dataType: "json",
        success: function(response) {
            if (!response["Error"]) {
                WriteCookie("authorizationToken", response["token"]);
                ShowSnack("Authenticated via Google!");
                GoogleInit();
                UILogin();
                GetPressureData();
            } else {
                ShowSnack(response["Error"]);
            }
      },
        error: function() {
            ShowSnack("Error");
        }
    });
}

function GetPressureData(){
    $.ajax({
      type: "POST",
      url: "https://projectsherlock.ddns.net/projects/BloodPressureMonitoring/BackEnd/api/GetMeasurements.php",
      data: JSON.stringify({"token":ReadCookie("authorizationToken")}),
      dataType: "json",
      success: function(response) {
        if (!response["Error"]) {

            if(response['Data'].length != sessionStorage.getItem('lastValsCount')){

                let sysData = [];
                let diaData = [];
                let dataLabels = [];
                let normSys = [];
                let normDia = [];
                let sysMax = [];
                let sysMin = [];
                let diaMax = [];
                let diaMin = [];

                response['Data'].forEach(function(pressureEntry){
                    sysData.unshift(pressureEntry.systole);
                    diaData.unshift(pressureEntry.diastole);
                    normSys.unshift(120);
                    normDia.unshift(80);
                    sysMax.unshift(140);
                    sysMin.unshift(100);
                    diaMax.unshift(90);
                    diaMin.unshift(60);
                    dataLabels.unshift(NormalizeTime(new Date(pressureEntry.createTime)));
                });

                UpdateChart(sysData, diaData, dataLabels, normSys, normDia, sysMax, sysMin, diaMax, diaMin);
                sessionStorage.setItem('lastValsCount', response['Data'].length);
            }

        } else {
          ShowSnack(response["Error"]);
        }
      },
      error: function() {
        ShowSnack("Error");
      }
    });
}

// Internal functions
function Init(){
    sessionStorage.clear();
    UIInit();
    StartGAuth();
}

function GoogleInit(){
    gapi.auth2.getAuthInstance().isSignedIn.listen(signinChanged);
    mainContainer.appendChild(canvasGraph);
}

var signinChanged = function (val) {
    if(!val){
        ShowSnack("LOGGED OUT.");
    }
};
