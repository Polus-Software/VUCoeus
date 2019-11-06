// Title: Div Slide
// Description: Use to slide div( while showing or hiding)
// Version: 0.1
// Date: 09-19-2006 (mm-dd-yyyy)
// Author: sharath k

// Copyright (c) Massachusetts Institute of Technology
// 77 Massachusetts Avenue, Cambridge, MA 02139-4307
// All rights reserved.

/** READ ME
* The Div tag must have 'overflow:hidden' as one of the style attribute values
* for sliding to work.
* ex: <div style="display:none;overflow:hidden"/>
*/


var divId;
var divHeight = -1;

var loopCount = 0, loopLimit;

var topLimit, bottomLimit, unit, interval;

var evalString;

function DivSlider() {
}

//function showHide(id) {
DivSlider.prototype.showHide = function (id) { 
this.divId = id;
with(this) {
    if(document.getElementById(divId).style.display == "") {
        //Visible, Hide it
        hideDiv(id);
    }else {
        //Not Visible, Show it
       showDiv(id);
    }
}
}

//function showDiv(id, evalStr) {
DivSlider.prototype.showDiv = function (id, evalStr) { 
this.divId = id;
this.evalString = evalStr;
with(this) {
    //divHeight = document.getElementById(divId).style.height;
    this.divHeight = getHeight(id);
    divHeight = getNumericalValue(this.divHeight);
    document.getElementById(id).style.height = 1;
    document.getElementById(id).style.display="";
    calculateParameters();
    loopCount = 0;
    show();
}
}


//function hideDiv(id, evalStr) {
DivSlider.prototype.hideDiv = function (id, evalStr) {
this.divId = id;
this.evalString = evalStr;
with(this) {
    divHeight = getHeight(id);
    document.getElementById(id).style.height = divHeight;
    calculateParameters();
    loopCount = 0;
    hide();
}
}

//function hide() {
DivSlider.prototype.hide = function () {
with(this) {
    height = document.getElementById(divId).style.height;
    height = getNumericalValue(height);
    loopCount = loopCount + 1;
    if(height > bottomLimit && loopCount < loopLimit){
        document.getElementById(divId).style.height = height - unit;
        //setTimeout ("hide()" , interval );
        var self = this; 
        setTimeout(function(){ hide(); }, interval);
    }else {
        document.getElementById(divId).style.display="none";
        document.getElementById(divId).style.height = divHeight;
        if(evalString != "") {
            eval(evalString);
        }
    }
}
}

//function show() {
DivSlider.prototype.show = function () {
with(this){
    height = document.getElementById(divId).style.height;
    height = getNumericalValue(height);
    loopCount = loopCount + 1;
    if(height < topLimit && loopCount < loopLimit){
        document.getElementById(divId).style.height = height + unit;
        //setTimeout ("show()" , interval );
        var self = this; 
        setTimeout(function(){ show(); }, interval);
    }else {
        document.getElementById(divId).style.display="";
        document.getElementById(divId).style.height = divHeight;
        if(evalString != "") {
            eval(evalString);
        }
    }
}
}

//function calculateParameters(){
DivSlider.prototype.calculateParameters = function () {
with(this) {
    unit = 20;
    interval = 75
    bottomLimit = unit;
    topLimit = divHeight - unit;
    loopLimit = 10;
}
}

//function getHeight(id) {
DivSlider.prototype.getHeight = function (id) {
with(this) {
    resetVisibility = false;
    if(document.getElementById(id).style.display != "") {
        document.getElementById(id).style.display = "";
        resetVisibility = true;
    }
    /*height= (document.getElementById(id).style.height || document.getElementById(id).height || 
            document.getElementById(id).scrollHeight || document.getElementById(id).clientHeight);
    */        
    height= (document.getElementById(id).height || 
            document.getElementById(id).scrollHeight || document.getElementById(id).clientHeight);        
    
    //reset Visibility
    if(resetVisibility) {
        document.getElementById(id).style.display = "none"; 
    }

    if(height.length > 2) {
        post = height.substring(height.length - 2, height.length);
        if(post == "px"){
            height = height.substring(0, height.length - 2);
        }
    }
    return height;
}
}

//function getWidth(id) {
DivSlider.prototype.getWidth = function (id) {
with(this) {
    resetVisibility = false;
    if(document.getElementById(id).style.display != "") {
        document.getElementById(id).style.display = "";
        resetVisibility = true;
    }
         
    width = (document.getElementById(id).width || 
            document.getElementById(id).scrollWidth || document.getElementById(id).clientWidth);        
    
    //reset Visibility
    if(resetVisibility) {
        document.getElementById(id).style.display = "none"; 
    }

    if(width.length > 2) {
        post = width.substring(width.length - 2, width.length);
        if(post == "px"){
            width = width.substring(0, width.length - 2);
        }
    }
    return width;
}
}

//function getNumericalValue(str) {
DivSlider.prototype.getNumericalValue = function (str) {
with(this) {
    if(str.length > 2) {
        post = str.substring(str.length - 2, str.length);
        if(post == "px"){
            str = str.substring(0, str.length - 2);
        }
    }
    return parseInt(str);
}
}