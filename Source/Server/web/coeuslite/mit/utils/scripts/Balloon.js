/**
* returns the position(Left, Top) of the Element as an array.
*/
function getPosition(id) {
    var curleft = curtop = 0;
    obj = document.getElementById(id);
    if (obj.offsetParent) {
            curleft = obj.offsetLeft
            curtop = obj.offsetTop
            while (obj = obj.offsetParent) {
                    curleft += obj.offsetLeft
                    curtop += obj.offsetTop
            }
    }
    return [curleft,curtop];
}

 var lastDivId = -1;

/**
* 
*/
function showBalloonPopup(divId) {
    var elem = document.getElementById(divId); 
    if(divId == lastDivId) {
        //Same Div just Toggle
        var style = document.getElementById(divId).style.display;
        if(style == "") {
            style = "none";
        }else{
            style = "";
        }

        ds = new DivSlider();
        if(style == "") {
            document.getElementById(divId).style.display = "";
            ds.showDiv(divId);
        }else {
            var evalStr = "document.getElementById('"+divId+"').style.display = 'none'";
            ds.hideDiv(divId, evalStr);
        }
    }else {
        document.getElementById(divId).style.display = "";
        ds = new DivSlider();
        ds.showDiv(divId);

        //reset last selected row
        if(lastDivId != -1) {
            elem = document.getElementById(lastDivId);
            ds2 = new DivSlider();
            var evalStr = "document.getElementById('"+lastDivId+"').style.display = 'none'"
            ds2.hideDiv(lastDivId, evalStr);
        }
    }
    lastDivId = divId;
}

function displayBalloon(id, divId) {
    var position = getPosition(id);
    elem = document.getElementById(divId);
    elem.style.top = position[1] + 20;
    
    
    var winWidth = window.screen.width;
    ds = new DivSlider();
    var divWidth = ds.getWidth(divId);
    //divWidth = divWidth + 25;
    // Added For Case 3051-Floating scroll bar in Help in Protocol Screens in Lite
    divWidth = divWidth + 35;

    spaceLeft = winWidth - position[0];
    if(divWidth > spaceLeft) {
        left = position[0] - (divWidth - spaceLeft);
        if(left < 0) {
            left = 0;
        }
        elem.style.left = left;
    }else {
        elem.style.left = position[0];
    }
    showBalloonPopup(divId);
}

function hideBalloon(id) {
    ds = new DivSlider();
    ds.hideDiv(id);
}

function scrollToBookmark(frameId, bookmarkId){ 
    var gip=document.getElementById(frameId) 
    var x=gip.contentWindow.document.getElementById(bookmarkId).offsetLeft 
    var y=gip.contentWindow.document.getElementById(bookmarkId).offsetTop 
    gip.contentWindow.scrollTo(x,y) 
} 

function showBalloon(linkId, frameId, width, height, src, bookmarkId) {
    var elem = document.getElementById(frameId);

    if(src)  {
        elem.setAttribute("src", src);
    }
    
    if(width) {
        elem.setAttribute("width", width);
    }
    
    if(height) {
         elem.setAttribute("height", height);
    }

    displayBalloon(linkId, frameId);
    
    if(bookmarkId) {
        scrollToBookmark(frameId, bookmarkId);
    }
    
}
