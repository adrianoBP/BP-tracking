function ReadCookie(cookieName){
    let cookieData = document.cookie;
    let allCookies = cookieData.split(";");

    let cookieValue = null;

    allCookies.forEach(function(cookie){
        let currentCookieData = cookie.trim().split("=");
        if(currentCookieData[0] == cookieName){
            cookieValue = currentCookieData[1];
        }
    });
    return cookieValue;
}

function WriteCookie(cookieName, cookieValue){
    RemoveCookie(cookieName);
    document.cookie = cookieName + "=" + cookieValue + "; expires=Fri, 31 Dec 9999 23:59:59 GMT";
}

function RemoveCookie(cookieName){
    document.cookie = cookieName + "=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
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
