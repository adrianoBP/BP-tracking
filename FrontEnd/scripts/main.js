
// TODO: token / Revoke token
// TODO: handle new user

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

// looping function
window.setInterval(function(){
    if(ReadCookie("authorizationToken")){
        console.log("From interval.");
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

    }

    Init();
}

function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
        $.ajax({
            type: "POST",
            url: "https://projectsherlock.ddns.net/projects/BloodPressureMonitoring/BackEnd/api/Logout.php",
            data: JSON.stringify({"token":ReadCookie("authorizationToken")}),
            dataType: "json",
            success: function(response) {
                RemoveCookie("authorizationToken");
                location.reload();
            },
            error: function() {
                ShowSnack("Error");
            }
        });
    });
}

var signinChanged = function (val) {
    if(!val){
        ShowSnack("LOGGED OUT.");
    }
};

// General Functions
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

function UpdateChart(sysVals, diaVals, dataLabels){
    if(myChart){ myChart.destroy(); }
    myChart = new Chart(canvasGraph, {
        type: 'line',
        data: {
            labels: dataLabels,
            datasets: [
                {
                    label: "Systolic",
                    fill: false,
                    backgroundColor: "#b71c1c",
                    borderColor: "#b71c1c",
                    borderCapStyle: 'butt',
                    borderDash: [],
                    boderDashOffset: 0.0,
                    borderJoinStyle: 'miter',
                    pointBorderColor: "#b71c1c",
                    pointBackgroundColor: "#fff",
                    pointBorderWidth: 2,
                    pointHoverRadius: 5,
                    pointHoverBackgroundColor: "#b71c1c",
                    pointHoverBorderColor: "#fff",
                    pointHoverBorderWidth: 2,
                    pointRaduis: 2,
                    pointHitRadius: 10,
                    data: sysVals
                },
                {
                    label: "Diastolic",
                    fill: false,
                    backgroundColor: "#283593",
                    bezierCurve : true,
                    borderColor: "#283593",
                    borderCapStyle: 'butt',
                    borderDash: [],
                    boderDashOffset: 0.0,
                    borderJoinStyle: 'miter',
                    pointBorderColor: "#283593",
                    pointBackgroundColor: "#fff",
                    pointBorderWidth: 2,
                    pointHoverRadius: 5,
                    pointHoverBackgroundColor: "#283593",
                    pointHoverBorderColor: "#fff",
                    pointHoverBorderWidth: 2,
                    pointRaduis: 2,
                    pointHitRadius: 10,
                    data: diaVals
                }
            ]
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
    })
}

function Init(){
    sessionStorage.clear();
    gapi.auth2.getAuthInstance().isSignedIn.listen(signinChanged);
    mainContainer.appendChild(canvasGraph);
}

function GetPressureData(){
// "5B74E1CF-6B1D-4C31-8A4C-ED0DC1E5E017"
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

                response['Data'].forEach(function(pressureEntry){
                    sysData.unshift(pressureEntry.systole);
                    diaData.unshift(pressureEntry.diastole);
                    dataLabels.unshift(NormalizeTime(new Date(pressureEntry.createTime)));
                });

                UpdateChart(sysData, diaData, dataLabels);
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

function NormalizeTime(date){
    let day = date.getDate();
    let month = date.getMonth();
    let year = date.getFullYear();
    let hour = date.getHours();
    let minute = date.getMinutes();
    let second = date.getSeconds();

    if(day<10){day = "0"+day; }
    if(month<10){month = "0"+month; }
    if(hour<10){hour = "0"+hour; }
    if(minute<10){minute = "0"+minute; }
    if(second<10){second = "0"+second; }

    return day + "/" +  month + "/" +  year + " " +  hour + ":" +  minute + ":" +  second;
}
