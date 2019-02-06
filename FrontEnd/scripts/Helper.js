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
