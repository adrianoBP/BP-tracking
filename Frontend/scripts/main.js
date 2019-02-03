
// TODO: token / Revoke token

// DOM values
var mainContainer = document.getElementById("container");
var canvasGraph = document.createElement("canvas");
canvasGraph.className += "mainCanvas";

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

// looping functions
window.setInterval(function(){
    GetPressureData();
}, 3000);

Init();

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
    localStorage.clear();
    GetPressureData();
    mainContainer.appendChild(canvasGraph);
}

function GetPressureData(){

    $.ajax({
      type: "GET",
      url: "https://projectsherlock.ddns.net/projects/BloodPressureMonitoring/api/GetMeasurements.php",
      data: {},
      dataType: "json",
      success: function(response) {
        if (!response["Error"]) {

            if(response['Data'].length != localStorage.getItem('lastValsCount')){

                let sysData = [];
                let diaData = [];
                let dataLabels = [];

                response['Data'].forEach(function(pressureEntry){
                    sysData.unshift(pressureEntry.systole);
                    diaData.unshift(pressureEntry.diastole);
                    dataLabels.unshift(NormalizeTime(new Date(pressureEntry.createTime)));
                });

                UpdateChart(sysData, diaData, dataLabels);
                localStorage.setItem('lastValsCount', response['Data'].length);
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
    if(localStorage.getItem('lastMessage') != message){
        var x = document.getElementById("snackbar");
        x.innerHTML = message;
        x.className = "show";
            setTimeout(function() {
        x.className = x.className.replace("show", "");
        }, 3000);
        localStorage.setItem('lastMessage', message);
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
