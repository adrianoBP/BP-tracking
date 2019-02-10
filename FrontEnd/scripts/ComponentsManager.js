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

function UpdateChart(sysVals, diaVals, dataLabels, normSys, normDia, sysMax, sysMin, diaMax, diaMin){
    if(myChart){ myChart.destroy(); }
    let tmp = [100, 100];
    myChart = new Chart(canvasGraph, {
        type: 'line',
        data: {
            labels: dataLabels,
            datasets: [
                {
                    label: "DIAMAX",
                    fill: false,
                    borderColor: "rgba(51,153,51,0)",
                    background: "rgba(51,153,51,0.3)",
                    data: diaMax,
                    pointRadius: 0,
                    pointHoverRadius: 0,
                    borderDash: [10,5]
                },
                {
                    label: "Diastolic",
                    fill: "-1",
                    backgroundColor: "rgba(119, 119, 119, 0.30)",
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
                },
                {
                    label: "DIAMIN",
                    fill: "-1",
                    borderColor: "rgba(51,153,51,0.0)",
                    background: "rgba(119, 119, 119, 0.30)",
                    data: diaMin,
                    pointRadius: 0,
                    pointHoverRadius: 0,
                    borderDash: [10,5]
                },
                {
                    label: "SYSMAX",
                    fill: false,
                    borderColor: "rgba(119, 119, 119, 0)",
                    background: "rgba(51,153,51,1)",
                    data: sysMax,
                    pointRadius: 0,
                    pointHoverRadius: 0,
                    borderDash: [10,5]
                },
                {
                    label: "Systolic",
                    fill: "-1", // Change to "-1" in order to fill between lines, change to "start" fill to the axis
                    backgroundColor: "rgba(119, 119, 119, 0.30))",
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
                    label: "SYSMIN",
                    fill: "-1",
                    borderColor: "rgba(51,153,51,0.0)",
                    background: "rgba(0, 119, 119, 1)",
                    data: sysMin,
                    pointRadius: 0,
                    pointHoverRadius: 0,
                    borderDash: [10,5]
                },
                {
                    label: "Systolic reference.",
                    fill: false,
                    borderColor: "rgba(51,153,51,0.7)",
                    data: normSys,
                    pointRadius: 0,
                    pointHoverRadius: 0,
                    borderDash: [10,5]
                },
                {
                    label: "Diastolic reference",
                    fill: false,
                    borderColor: "rgba(255,102,51,0.7)",
                    data: normDia,
                    pointRadius: 0,
                    pointHoverRadius: 0,
                    borderDash: [10,5]
                }

            ]
        },
        options: {
            legend: {
                display: false
            },
            maintainAspectRatio: false,
            spanGaps: false,
            scales: {
                yAxes: [{
                    ticks: {
                        min: 40,  // minimum value
                        max: 160 // maximum value
                    }
                }]
            },
            plugins: {
                filler: {
                    propagate: false
                }
            },
            layout:{
                padding:{
                    top: 32,
                    bottom: 16,
                    left: 16,
                    right: 16
                }
            }
        }
    })
}
