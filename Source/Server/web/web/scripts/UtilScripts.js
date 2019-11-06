/******************************************************************************
   The Below function is used in COIDisclosure.jsp
   to avoid the user from entering invalid characters
   and convert them to other valid characters.
 */

/*function formatCode(numm) {
	var countyychar = 0;
	var newyy = "";
	var yy = numm.value.toUpperCase();
	for (var i = 0; i < yy.length; i++){
		var tempyy = yy.substring(i,i + 1);
		if ((tempyy >= "0" && tempyy <= "9" ) ||
			(tempyy >= "A" && tempyy <= "Z")  ||
			(tempyy == "\&") ||
			(tempyy == "/")  ||
			(tempyy == "_")	 ||
			(tempyy == "-") ){

				if (yy.substring(i,i + 1) == "/" ||
					yy.substring(i,i + 1) == "&" ||
					yy.substring(i,i + 1) == "-") {
						newyy += "-";
				} else{
					newyy += yy.substring(i,i + 1);
				}
				countyychar++;
		} // end if characters within specs
	}  // end for
	eval("document.frmCOIDisclosure." + numm.name + ".value = newyy");
}*///end of function
/*****************************************************************************/
<!-- Added for CASE #1374 -->
function opensmallwin(filename,winname){
    var newwin=window.open(filename,winname,"height=375,width=550,top=10,left=5,scrollbars=1,resizable=YES");
    if (!newwin.opener) { // for Nav 2.0x
        newwin.opener = this.window // this creates and sets a new property
    }
}

<!-- Added for CASE #1374 -->
function openmediumwin(filename,winname){
    var newwin=window.open(filename,winname,"height=440,width=550,top=10,left=5,scrollbars=1,resizable=YES");
    if (!newwin.opener) { // for Nav 2.0x
        newwin.opener = this.window // this creates and sets a new property
    }
}
<!-- End added for CASE #1374 -->

window.name="currentWindow";
//6/3/2003: Make this window slightly longer, to accomodate help text at top of search screens.
function openwintext(filename,winname){
    var newwin=window.open(filename,winname,"height=530,width=778,top=10,left=5,scrollbars=1,resizable=YES");
    if (!newwin.opener) { // for Nav 2.0x
        newwin.opener = this.window // this creates and sets a new property
    }
}

function openwinDesc(filename,winname){
    //var newwin=window.open(filename,winname,"height=175,width=300,top=200,left=300,scrollbars=1,resizable");
    var newwin=window.open(filename,winname,"height=450,width=500,top=200,left=300,scrollbars=1,resizable=YES");
    if (!newwin.opener) { // for Nav 2.0x
        newwin.opener = this.window // this creates and sets a new property
    }
}


//function for opening a new window
function openwin(filename,winname){
    var newwin=window.open(filename,winname,"height=475,width=778,top=10,left=5,scrollbars=1,resizable=YES");
    if (!newwin.opener) { // for Nav 2.0x
        newwin.opener = this.window // this creates and sets a new property
    }    
}
function changeFocus(obj){
    obj.focus();
}

// Begin CASE #275
//This window is slightly longer than other above popup window, to accomodate a few lines of help.
function openSearchWin(filename,winname){
    var newwin=window.open(filename,winname,"height=530,width=778,top=10,left=5,scrollbars=1,resizable=YES");
}
function changeFocus(obj){
    obj.focus();
}
// End CASE #275
//to display question details
function showQuestion(x){
//updated 9/5/02 to make the same dimensions as other popup windows and to make resizable.  -LMR.
   // var wid = screen.width-10;
//	var hit = screen.height-70;
   // window.open(""+x,"child1","HEIGHT="+hit+",WIDTH="+wid+",top=0,left=0,status=YES,scrollbars=YES");
    window.open(""+x,"child1","HEIGHT=475,WIDTH=778,top=10,left=5,status=YES,scrollbars=YES,resizable=YES");
}

/* CASE #819 Comment Begin */
/*  To check all the check boxes for the user convenience apply the changes to
the selected disclosures status*/
/*function checkAll(totPendDisclosures){
	if(document.frmAnnualDisclosures.chkAllDisclosures.checked){
        var totPendDiscls = parseInt(totPendDisclosures);
        if (totPendDiscls >1 ) {
            for(cntChkDisclosures=0;cntChkDisclosures<totPendDiscls;cntChkDisclosures++){
                eval("document.frmAnnualDisclosures.chkConflictStatus["+cntChkDisclosures+"].checked = true");
            }
        }else{
            document.frmAnnualDisclosures.chkConflictStatus.checked = true;
        }
    }else {

		if (totPendDiscls > 1 ) {
            for(cntChkDisclosures=0;cntChkDisclosures<totPendDiscls;cntChkDisclosures++){
                eval("document.frmAnnualDisclosures.chkConflictStatus["+cntChkDisclosures+"].checked = false");
            }
        }else{
            document.frmAnnualDisclosures.chkConflictStatus.checked = false;
        }
	}
}*/
/* CASE #819 Comment End */
/* CASE #819 Begin */
//Select/deselct all conflict status checkboxes.
function checkAll(){

	var isChecked = document.frmAnnualDisclosures.chkAllDisclosures.checked;
	if(document.frmAnnualDisclosures.chkConflictStatus.length > 1){
	     for (var i=0; i<document.frmAnnualDisclosures.chkConflictStatus.length; i++) {
		document.frmAnnualDisclosures.chkConflictStatus[i].checked = isChecked;
	     }
	}
	else{
		document.frmAnnualDisclosures.chkConflictStatus.checked = isChecked;
	}
}
/* CASE #819 End */

/* CASE #812 Comment Begin */
// this is to apply the selcted status information to all selected disclosures conflict status
/*function changeStatus(totPendDisclosures){

    var totPendDiscls = parseInt(totPendDisclosures);
	var conflictStatus = document.frmAnnualDisclosures.conflictStatus.selectedIndex;
    if (totPendDiscls >1 ) {
        for(cntChkDisclosures=0;cntChkDisclosures<totPendDiscls;cntChkDisclosures++){
            if( eval("document.frmAnnualDisclosures.chkConflictStatus["+cntChkDisclosures+"].checked") ){
                eval("document.frmAnnualDisclosures.disclConflictStatus["+cntChkDisclosures+"].selectedIndex = " + conflictStatus);
            }
        }
    }else if (totPendDiscls == 1 ) {
        if( document.frmAnnualDisclosures.chkConflictStatus.checked ){
            document.frmAnnualDisclosures.disclConflictStatus.selectedIndex = conflictStatus;
        }
    }

}*/
/* CASE #812 Comment End */


/* CASE #1046 Comment Begin */
/* CASE #812 Begin */
// Apply the selcted status information to all selected disclosures conflict status
/*function changeStatus(){
	var conflictStatus = document.forms[0].conflictStatus.value;
	if(document.frmAnnualDisclosures.chkConflictStatus.length > 1){
		for(var cnt=0; cnt<document.frmAnnualDisclosures.chkConflictStatus.length; cnt++){
			if(document.frmAnnualDisclosures.chkConflictStatus[cnt].checked){
				document.frmAnnualDisclosures.disclConflictStatus[cnt].value = conflictStatus;

			}
		}
	}
	else{
		document.frmAnnualDisclosures.disclConflictStatus.value = conflictStatus;	
	}
}*/
/* CASE #812 End */
/* CASE #1046 Comment End */
/* CASE #1046 Begin */
//Function updated to work with updated disclConflStatus select elements on JSP.
/*function changeStatus(){
	var conflictStatus = document.forms[0].conflictStatus.value;
	//alert("document.frmAnnualDisclosures.chkConflictStatus.length: "+document.frmAnnualDisclosures.chkConflictStatus.length);
	//alert("document.frmAnnualDisclosures.length: "+document.frmAnnualDisclosures.length);
	//alert("document.frmAnnualDisclosures.elements.length: "+document.frmAnnualDisclosures.elements.length);
	//Start iteration through the form elements with 5, to skip conflictStatus select and option elements.
	for(var elementIndex=5; elementIndex<document.frmAnnualDisclosures.length; elementIndex++){
		//alert(document.frmAnnualDisclosures.elements[elementIndex].type);
		if(document.frmAnnualDisclosures.elements[elementIndex].type=="select-one"){
			if(document.frmAnnualDisclosures.elements[elementIndex-1].checked){
				document.frmAnnualDisclosures.elements[elementIndex].value = conflictStatus;
			}
		}
	}	
}*/
/* CASE #1046 End */

/* CASE #1374 Begin */
<!-- For Annual Disclosures page -->
function changeStatus(){
	var conflictStatus = document.frmAnnualDisclosures.conflictStatus.value;
	//alert("conflictStatus: "+conflictStatus);
	var firstConflictStatus = 0;
	for(var elementIndex=0; elementIndex<6; elementIndex++){
			//alert("element type: "+document.frmAnnualDisclosures.elements[elementIndex].type);
			//alert("element value: "+document.frmAnnualDisclosures.elements[elementIndex].value);
			//alert("element name: "+document.frmAnnualDisclosures.elements[elementIndex].name);
			var elementName = document.frmAnnualDisclosures.elements[elementIndex].name;
			if(elementName == "disclConflictStatus0"){
				break;
			}
			firstConflictStatus++;
	}
	for(var elementIndex=firstConflictStatus; elementIndex<document.frmAnnualDisclosures.length; elementIndex++){		
			if(document.frmAnnualDisclosures.elements[elementIndex].type=="select-one" ){
				if(document.frmAnnualDisclosures.elements[elementIndex-1].checked){
					document.frmAnnualDisclosures.elements[elementIndex].value = conflictStatus;
				}
			}
	}
}

function changeAllStatus(){
	var conflictStatus = document.forms[0].conflictStatus.value;
	for(var elementIndex=0; elementIndex<document.forms[0].elements.length; elementIndex++){
			var elementName = document.forms[0].elements[elementIndex].name;
			if( (elementName.indexOf("sltConflictStat") != -1)
				|| (elementName.indexOf("disclConflictStatus") != -1) ){
				document.forms[0].elements[elementIndex].value=conflictStatus;
			}
	}	
}
/* CASE #1374 End */

/*  This is called when user wants to update the conflict status of all pending disclosures and to route the
    user to next entity for further process.
*/

/*function annDisclosuresUpdate(requestType){
    document.frmAnnualDisclosures.action = "annualDisclosuresUpdate.do?reqType=" + requestType;
    //document.frmAnnualDisclosures.action = "annualDisclosuresUpdate.do";
    document.frmAnnualDisclosures.submit();
}*/

/*  This is called when user wants to update the conflict status of all pending disclosures and to control the
    user to welcome.jsp
*/

/*function annDisclsFinalUpdate(){
    document.frmAnnualDisclosures.action ="annualDisclsFinalUpdate.do";
    document.frmAnnualDisclosures.submit();
}*/
