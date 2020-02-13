
///<reference path="/Site/scripts/toolkit.js" />

function setPanelVisibility(elemId, state)
{    
    var elem = $(elemId);
    if(elem != null)
    {
        if(state)
        {       
            if(!isVisibile(elemId))
            {
                new Effect.SlideDown(elem);
            }
        }
        else
        {
            if(isVisibile(elemId))
            {
                new Effect.SlideUp(elem);
            }
        }    
    }
    return false;
} 

function toggleEditorViewState(elemId)
{    
    if(!isVisibile(elemId))
    {
        setPanelVisibility(elemId, true);
    }
    else
    {
        setPanelVisibility(elemId, false);
    }
}

function toggleStatus(ControlId, ControlStatusId)
{
    if(ControlStatusId != '')
    {
        if($(ControlId).style.display != "none")
        {
            $(ControlStatusId).value = "closed";        
        }
        else
        {
            $(ControlStatusId).value = "open";
        }
    }
    toggleEditorViewState(ControlId);
}

function toggleEditorState(imgId, ControlId, ControlStatusId)
{
    if(ControlStatusId != '')
    {
        if($(ControlStatusId) != null)
        {
            if($(ControlId).style.display != "none")
            {
                $(ControlStatusId).value = "closed";        
            }
            else
            {
                $(ControlStatusId).value = "open";
            }    
        }
    }
    toggleEditorViewState(ControlId);
    toggleHeaderImage(imgId);
}

function toggleSearch(imgId, ControlId, ControlStatusId)
{
    if($(ControlStatusId) != null)
    {
        if($(ControlId) != null)
            {
            if($(ControlId).style.display != "none")
            {
                $(ControlStatusId).value = "closed";        
            }
            else
            {
                $(ControlStatusId).value = "open";
            }
            toggleEditorViewState(ControlId);
            toggleSearchImage(imgId);
        }
    }
}

// ToggleSearch image 
var toggleSearchState = false; 

function toggleSearchImage(toggleImageCtrl) 
{     
    var imgSearch = $(toggleImageCtrl);
    
    imgSearch.style.visible = true;
    if(toggleSearchState) 
    { 
        imgSearch.src = "/site/images/toggleSearch_up.jpg"
        toggleSearchState = false;
    } 
    else 
    { 
        imgSearch.src = "/site/images/toggleSearch_down.jpg"
        toggleSearchState = true;
    } 
} 

// Toggle image 
var toggleState = false; 

function toggleHeaderImage(toggleImageCtrl) 
{     
    var toggleImg = $(toggleImageCtrl);
    
    toggleImg.style.visible = true;
    if(toggleState) 
    { 
        toggleImg.src = "/site/images/up_arrow.jpg"
        toggleState = false;
    } 
    else 
    { 
        toggleImg.src = "/site/images/down_arrow.jpg"
        toggleState = true;
    } 
} 

function isVisibile(elemId)
{
    var elem = $(elemId);
    if(elem != null)
    {
        return $(elemId).style.display != "none"; 
    }   
    else
    {
        return false;
    }
}

var currentModal;
var parentModalData;


function showModal(empty, url, pageCallback)  {
	return currentModal = new Modal(url,pageCallback);
}
function showModalWithTimeOut(url, pageCallback) {
    return currentModal = new Modal(url,pageCallback);
}

function showModalWithData(div, url, data, pageCallback)  {
parentModalData = data;
return setTimeout("showModalWithDataAndTimeOut('"+url+"',"+pageCallback+")",500);
}
function showModalWithDataAndTimeOut(url, pageCallback)
{
    currentModal = new Modal(url,pageCallback);
    currentModal.contentWindow.parentData = parentModalData;
    return currentModal;
}

function resizableModal(div, url, pageCallback, modalHeight, modalWidth)  {
	 currentModal = new Modal(url,pageCallback);
	 currentModal.setSize(modalHeight,modalWidth);
	 return currentModal;
}

function closeModal(data,force)  {
	if (force) currentModal.close();	//	Force Close
	else currentModal.reply(data);		//	Trigger Reply
}
function getPageCallback() {
	
	if (currentModal.onreply != null)
	{
		return currentModal.onreply;
	}
	else
	{
		return '';
	}
}

//REGION WEB_METHOD
function wsGetValueByTag(xmlResult, tagName)    {
    return xmlResult.responseXML.getElementsByTagName(tagName)[0].firstChild.nodeValue;
}

// note: this isnt technically correct. 'Code' is checked in the full scope.
// Acceptable, since the SOAP error format can be controlled.
function wsHasErrors(xmlResult) {
    var errorCode = wsGetValueByTag(xmlResult, 'Code');
    if (errorCode != 0) return true;
    return false;
}

// note: this isnt technically correct. 'Code' is checked in full scope.
// Acceptable, since the SOAP error format can be controlled.
function wsGetErrorCode(xmlResult) {
    return wsGetValueByTag(xmlResult, 'Code');
}

function wsGetNodeValue(node, elementName) {
    if(node.getElementsByTagName(elementName)[0] == null)   return "";
    if(node.getElementsByTagName(elementName)[0].firstChild == null)   return "";

    return node.getElementsByTagName(elementName)[0].firstChild.nodeValue;
}

function wsGetSoapRequest(methodName, params)   {
    var strParams = '<' + methodName + ' xmlns="http://www.puresafety.com/" >';
    if (params != null) {
        params.each(function(item)  {
            var xml = '<' + item.key + '>' + item.value + '</' + item.key + '>'
            strParams += xml;
        });
    }
    strParams += '</' + methodName + '>';

    //We will use SOAP 1.1
    var request = '<?xml version="1.0" encoding="utf-8"?>' +
        '<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">' +
        '<soap:Body>' +
        strParams +
        '</soap:Body>' +
        '</soap:Envelope>';
    return request;
}

function wsGetStdOptions(wsMethod, params, onSuccessCallBack, asynchronous)  {
    var soapReq = wsGetSoapRequest(wsMethod, $H(params));
    var opt = {
        asynchronous: asynchronous,
        method: 'post',
        postBody: soapReq,
        onSuccess: onSuccessCallBack,
        contentType: 'text/xml'
    }
    return opt;
}
//END REGION WEB_METHOD

/* Type Conversion */
function toBool(strValue)   {
    if (strValue.toLowerCase() == "false")    {
        return false;
    }
    else return true;
}
/* --- --- */


function ChangeState(controlId, imagePath, hdnFieldId)
{
    var hdnField = $(hdnFieldId);
    if(hdnField.value =='True')
     {
         hdnField.value='False';
         $(controlId).src = imagePath + 'False.gif';
     } 
     else if(hdnField.value =='False') 
     {
         hdnField.value='';
         $(controlId).src = imagePath + 'Null.gif';
     } 
     else if(hdnField.value =='') 
     {
         hdnField.value='True';
         $(controlId).src = imagePath + 'True.gif';
     }
}

function ChangeBiState(controlId, imagePath, hdnFieldId)
{
    var hdnField = $(hdnFieldId);
    if(hdnField.value =='True')
    {
     hdnField.value='False';
     $(controlId).src = imagePath + 'False.gif';
    } 
    else if(hdnField.value =='False') 
    {
     hdnField.value='True';
     $(controlId).src = imagePath + 'True.gif';
    } 
    else if(hdnField.value =='') 
    {
     hdnField.value='True';
     $(controlId).src = imagePath + 'True.gif';
    } 
}


/*  --------------------------------------------
        Elem Class - Represents an HTML element.
    -------------------------------------------- */

var Elem = Class.create({
    initialize: function(tag, className, id) {
        this.tag = tag;
        this.className = className;
        this.id = id;
        this.name = id;
        this.childNodes = new Array();
        this.meta = new Hash();
    },
    
    add: function(node) {
        this.childNodes.push(node);
    },
    
    addText: function(text) {
        this.add(new TextElem(text));
    },
    
    getHTML: function() {            
        var selfClosing = ['input', 'br', 'img'];        
        var html = '<' + this.tag;        
        var elemHash = $H(this);        
        
        elemHash.keys().each(function(key)    {        
            //remove functions...
            if (typeof(elemHash.get(key)) != 'function')    {            
                var attributeName = '';
                if (key != 'tag' && key != 'childNodes' && key != 'meta')    { //tag, childNodes, meta aren't attributes.
                    attributeName = key == 'className' ? 'class' : key;
                    if (elemHash.get(key) != null) {
                        var strAttribute = ' ' + attributeName + '="' + elemHash.get(key) + '"';     
                        html += strAttribute;
                    }
                }    
            }
        });
        
        if (selfClosing.include(this.tag))  {
            html += ' />';
        }
        else    {
            html += '>';        
            this.childNodes.each(function(child)    {
                html += child.getHTML();
            });                           
            html += '</' + this.tag + '>';
        }
        
        return html;        
    },
    
    innerHTML: function()   {
        var html = '';        
        this.childNodes.each(function(child)    {
            html += child.getHTML();
        });
        return html;
    }
});

/*  ---------------------------------------------
    TextElem Class - Represents an HTML text node.
    --------------------------------------------- */
var TextElem = Class.create({
    initialize: function(text) {
        this.text = text != null ? text : '';
    },
    
    getHTML: function() {
        return this.text;
    },
    
    innerHTML: function()   {
        return this.text;
    }
    
});

/*--Selectable crud grid --*/
    var selectedValues = new Hash(); 
        
    function selectItem(e, hiddenField)
    {
        var selectedItemid = e.attributes["idTag"].value;
        var selectedItemvalue = e.attributes["valueTag"].value;
        var selectedHash = new Hash();
        var existingSelectedList ;
        
        if($(hiddenField).value  != '')
            existingSelectedList = eval('(' + $(hiddenField).value + ')');
            
        if(existingSelectedList != null && existingSelectedList != '')
            selectedHash = $H(existingSelectedList);
            
        if(e.checked)
        {
            if(selectedHash.get(selectedItemid) == null)
            {
                selectedHash.set(selectedItemid, selectedItemvalue);                
            }
        }
        else
        {
            if(selectedHash.get(selectedItemid) != null)
            {
                selectedHash.unset(selectedItemid);
            }
        }
        $(hiddenField).value = selectedHash.toJSON();
    }
   function selectAllItems(e, hiddenField, containerId)
    {
        var grid = $(containerId);
        var rows = grid.select('tr');
        
        var selectedItemid ;
        var selectedItemvalue ;
        
        var selectedHash = new Hash();
        var existingSelectedList ;
        
        if($(hiddenField).value  != '')
            existingSelectedList = eval('(' + $(hiddenField).value + ')');
            
        if(existingSelectedList != null && existingSelectedList != '')
            selectedHash = $H(existingSelectedList);
        
        
        rows = rows.reject(function(row) { return row.className == 'header'; });
        rows.each(function(row) 
            {
                var col = row.select('td')[0];//Selection check box is taken as the first column::TODO 
                if (col != null)    
                {   //the first row will only have THs, not TDs.
                    var checkbox = col.select('input[type="checkbox"]');
                    checkbox[0].checked = e.checked;
                    
                    selectedItemid = checkbox[0].attributes["idTag"].value;
                    selectedItemvalue = checkbox[0].attributes["valueTag"].value;
                    
                    if(e.checked)
                    {
                        if(selectedHash.get(selectedItemid) == null)
                        {
                            selectedHash.set(selectedItemid, selectedItemvalue);                
                        }
                    }
                    else
                    {
                        if(selectedHash.get(selectedItemid) != null)
                        {
                            selectedHash.unset(selectedItemid);
                        }
                    }
                }
            }
        );
        $(hiddenField).value = selectedHash.toJSON();
}
   /*---------------------------------------------
        Section Validation Starts
    --------------------------------------------- */
    
/*
    The implimentation of javascript framework.  <<Soon it will move the UI.js>>    
*/

 PS.NameSpace.define("PS.UI.Page",Class.create({
    initialize:function()
    {
        this.validations = new Array();
    },
    getValidations : function()
    {
        return this.validations;
    },
    addValidation : function(validationObject)
    {
        this.validations.push(validationObject);
    },
    getValue : function(parentId,controlName)
    {
      var returnValue     = "";
      var controlId       = "";
      var uniquieControlId = ""; 
      var parentControl  = $(parentId);
      
      if( !PS.Common.Functions.isNullOrUndefined(parentControl))
      {
         var inputControls = parentControl.getElementsByTagName("INPUT");
         var listControls  = parentControl.getElementsByTagName("SELECT");
         var inputControlArea = parentControl.getElementsByTagName("TEXTAREA");
         
          
         if(!PS.Common.Functions.isNullOrUndefined(inputControlArea))
         {
            for(var index=0;index <inputControlArea.length;index++)
            {
                controlId = "txt" + controlName;
                uniquieControlId= !PS.Common.Functions.isNullOrEmpty(inputControlArea[index].id) ? inputControlArea[index].id : "";
                var regx=RegExp(controlId+"$");                    
                    if(regx.test(uniquieControlId))                
                {
                    returnValue = inputControlArea[index].value;
                    break;
                }
            }
         }
         
         
         if(!PS.Common.Functions.isNullOrUndefined(inputControls))
         {
            for(var index=0;index <inputControls.length;index++)
            {
                controlId = "txt" + controlName;
                uniquieControlId= !PS.Common.Functions.isNullOrEmpty(inputControls[index].id) ? inputControls[index].id : "";
                var regx=RegExp(controlId+"$");                    
                    //if(uniquieControlId.toLowerCase().indexOf(controlId.toLowerCase()) > 0 )
                    if(regx.test(uniquieControlId))                
                {
                    returnValue = inputControls[index].value;
                    break;
                }
            }
         }
         
         if(!PS.Common.Functions.isNullOrUndefined(listControls))
         {
            for(var index=0;index <listControls.length;index++)
            {
                controlId = "ddl" + controlName;
                uniquieControlId= !PS.Common.Functions.isNullOrEmpty(listControls[index].id) ? listControls[index].id : "";
                
                var regx=RegExp(controlId+"$");
                  //if(uniquieControlId.toLowerCase().indexOf(controlId.toLowerCase()) > 0 )
                  if(regx.test(uniquieControlId))
                {
                    returnValue = listControls[index].value;
                    break;
                }
            }
         }
      }
      return PS.Common.Functions.trim(returnValue);
    },
   getWarnings : function(sectionId) 
   {
        var warnings = "";
        if ( this.validations  != null )
        {
            for(var index=0;index < this.validations.length;index++)
            {
                if( this.isWarning(this.validations[index]) && 
                    !this.validations[index].isValid() && 
                    this.validations[index].sectionId == sectionId)
                    warnings += this.validations[index].errorMessage + "<BR/>";
            }
        }
        return warnings;
   },
   getErrors : function(sectionId)
   {
        var errors = "";
        if ( this.validations  != null )
        {
            errors  = this.getRequiredErrors(sectionId)
            errors += this.getNonRequiredErrors(sectionId)

            if( errors.length <= 0)
                errors = this.getOtherErrors(sectionId)
        }
        return errors;
   },
   isWarning :  function(type)
   {
        if( type instanceof PS.UI.Page.ValidationBase.StatusRequiredField)
            return true;
        else
            return false;   
   },
   getRequiredErrors : function(sectionId)
   {
        var errors = "";
        for(var index=0;index < this.validations.length;index++)
        {
             if(this.validations[index] instanceof  PS.UI.Page.ValidationBase.RequiredField && 
                this.validations[index].sectionId == sectionId)
             {
                  if(!this.validations[index].isValid())
                     errors += this.validations[index].errorMessage +"<BR/>";
             }
        }
        return errors;
   },
   getOtherErrors : function(sectionId)
   {
        var errors = "";
        for(var index=0;index < this.validations.length;index++)
        {
             if( !(this.validations[index] instanceof  PS.UI.Page.ValidationBase.RequiredField) && 
                   !this.isWarning(this.validations[index]) && 
                   this.validations[index].sectionId == sectionId)
             {
                  if(!this.validations[index].isValid())
                     errors += this.validations[index].errorMessage +"<BR/>";
              }
        }
        return errors;
   },
   getNonRequiredErrors : function(sectionId)
   {
        var errors = "";
        var name  = "";
        
        for(var index=0;index < this.validations.length;index++)
        {
            name  = this.validations[index].id;
            if(!this.hasRequiredField(name,sectionId))
            {
              if(!this.validations[index].isValid() && 
                 !this.isWarning(this.validations[index]) && 
                 this.validations[index].sectionId == sectionId)
                errors += this.validations[index].errorMessage + "<BR/>";
            }
        }
        return errors;
   },
   hasRequiredField : function(name,sectionId)
   {
        for(var index=0;index < this.validations.length;index++)
        {
            if(this.validations[index].id ==  name && 
               this.validations[index] instanceof  PS.UI.Page.ValidationBase.RequiredField && 
               this.validations[index].sectionId == sectionId)
                return  true;
        }   
        return false;
   },
   getErrorsByNames : function(sectionId,names)
   {
        var errors = "";
        if ( this.validations  != null )
        {
            errors  = this.getRequiredErrorsByNames(sectionId,names)
            errors += this.getNonRequiredErrorsByNames(sectionId,names)

            if( errors.length <= 0)
                errors = this.getOtherErrorsByNames(sectionId,names)
        }
        return errors;
   },
   getRequiredErrorsByNames : function(sectionId,names)
   {
        var errors = "";
        for(var fldIndex=0;fldIndex < names.length ; fldIndex++)
        {
            for(var index=0;index < this.validations.length;index++)
            {
                 if(this.validations[index] instanceof  PS.UI.Page.ValidationBase.RequiredField && 
                    this.validations[index].sectionId == sectionId && 
                    this.validations[index].id  ==  names[fldIndex])
                 {
                      if(!this.validations[index].isValid())
                         errors += this.validations[index].errorMessage +"<BR/>";
                 }
            }
        }
        return errors;
   },
   getOtherErrorsByNames : function(sectionId,names)
   {
        var errors = "";
        for(var fldIndex=0;fldIndex < names.length ; fldIndex++)
        {
            for(var index=0;index < this.validations.length;index++)
            {
                 if( !(this.validations[index] instanceof  PS.UI.Page.ValidationBase.RequiredField) && 
                       !this.isWarning(this.validations[index]) && 
                       this.validations[index].sectionId == sectionId && 
                       this.validations[index].id  ==  names[fldIndex])
                 {
                      if(!this.validations[index].isValid())
                         errors += this.validations[index].errorMessage +"<BR/>";
                  }
            }
        }
        return errors;
   },
   getNonRequiredErrorsByNames : function(sectionId,names)
   {
        var errors = "";
        var name  = "";
        
        for(var fldIndex=0;fldIndex < names.length ; fldIndex++)
        {
            for(var index=0;index < this.validations.length;index++)
            {
                if(this.validations[index].id  ==  names[fldIndex])
                {
                    name  = this.validations[index].id;
                    if(!this.hasRequiredField(name,sectionId))
                    {
                      if(!this.validations[index].isValid() && 
                         !this.isWarning(this.validations[index]) && 
                         this.validations[index].sectionId == sectionId)
                        errors += this.validations[index].errorMessage + "<BR/>";
                    }
                }
            }
        }
        return errors;
   },
   isSectionRegistered : function(sectionId)
   {
        for(var index=0;index < this.validations.length;index++)
        {
            if(this.validations[index].sectionId == sectionId)
                return true;
        }
        return false;
   },
   getErrorMessage  : function(sectionId)
   {
       return this.getErrors(sectionId);    
   },
   getErrorByFieldNames : function(sectionId,names)
   {
       return this.getErrorsByNames(sectionId,names);    
   }
  }));
   
   
  PS.NameSpace.define("PS.UI.Page.ValidationBase",Class.create(
  {
    initialize: function(sectionId,id,errorMessage)
     {
        this.sectionId    = sectionId;
        this.id           = id;
        this.errorMessage = errorMessage;
        this.value        = "";
     }, 
     isValid :function()
     {
        return false;  
     },
     getValue : function()
     {
        var userInputValue  = PS.Common.Functions.trim(this.value);
        if(PS.Common.Functions.isNullOrEmpty(userInputValue))
           userInputValue = activePage.getValue(this.sectionId,this.id);   
        return PS.Common.Functions.trim(userInputValue);
     }
  }));

 PS.NameSpace.define("PS.UI.Page.ValidationBase.RequiredField",Class.create(PS.UI.Page.ValidationBase,
 {
    initialize: function($super,sectionId,id,canProcess,errorMessage)
    {
         $super(sectionId,id,errorMessage);                   
         this.canProcess = canProcess; 
    },
    isValid : function()
    {
        var returnValue = !PS.Common.Functions.isNullOrEmpty(this.getValue());
        this.value  = "";
        return returnValue;
    }
 }));
   
  PS.NameSpace.define("PS.UI.Page.ValidationBase.RegularExpression",Class.create(PS.UI.Page.ValidationBase,
  {
    initialize: function($super,sectionId,id,expression,errorMessage)
    {
         $super(sectionId,id,errorMessage);                   
         this.expression = expression;
    },
    isValid : function()
    {
       var returnValue  = true;
       if( !PS.Common.Functions.isNullOrEmpty(this.getValue()))
       {
           var regExp = new RegExp(this.expression);
           returnValue = regExp.test(this.getValue());
       }
       this.value  = "";
       return returnValue;
    }
  }));

  PS.NameSpace.define("PS.UI.Page.ValidationBase.StatusRequiredField",Class.create(PS.UI.Page.ValidationBase,
  {
    initialize: function($super,sectionId,id,errorMessage)
    {
         $super(sectionId,id,errorMessage);                  
    },
    isValid : function()
    {
        var returnValue  = !PS.Common.Functions.isNullOrEmpty(this.getValue());
        this.value = "";
        return returnValue;
    }
  }));
  
  PS.NameSpace.define("PS.UI.Page.ValidationBase.Method",Class.create(PS.UI.Page.ValidationBase,
  {
    initialize: function($super,sectionId,id,errorMessage,isClientSideEnabled)
    {
         $super(sectionId,id,errorMessage);
         this.isClientSideEnabled = isClientSideEnabled;                 
    },
    isValid : function()
    {
        //(id ==> PureSafety.Hydra.Hierarchy.Entity.IsValidateHours)
        var returnValue  = true;
        if( this.isClientSideEnabled )
        {
             var methodName  = this.id + "()";
             try
             {
                returnValue  = eval(methodName);
             }catch(e)
             {
                returnValue = false;
             }
         }
        return returnValue;
    }
  }));                  
  
  //TODO : Delete this method once validation implimenation completed.
  PS.NameSpace.define("PureSafety.Hydra.Hierarchy.Entity.WorkHours",{
         initialize: function()
         {
         },
         IsValidWorkHours : function()
         {
            return true;
         }
  });
  
  var activePage = null;
   
  function registerValidationSection(sectionId)
  {
      if( typeof(activePage) == "undefined"  || activePage == null )
            activePage = new PS.UI.Page();

     var methodName = "registerValidatons_" + sectionId + "()";
     if(!isValidationSectionRegistered(sectionId))
          eval(methodName);
  }                 
  
  function isValidationSectionRegistered(sectionId)
  {
     return activePage.isSectionRegistered(sectionId);
  } 
    
  //To validatie multiple section(s). 
  function validateSections(sectionIds)
  {
       var errorMessages  =   ""; 
       if(!PS.Common.Functions.isNullOrEmpty(sectionIds))
       {
           for(var index=0;index < sectionIds.length;index++)
           {
               registerEntityValidation(sectionIds[index]);
               errorMessages += activePage.getErrorMessage(sectionIds[index]);
           }
       }
       
       if( !PS.Common.Functions.isNullOrEmpty(errorMessages) )
       {
            //TODO: Need to change based, scenarios used...
            if(!PS.Common.Functions.isNullOrEmpty(sectionIds))
                showErrorMessage(sectionIds[0],errorMessages);
            return false;
       }
       return true;
   } 
   
   function getControlNames(sectionId,validations)
   {
      var name              = "";
      var controlId         = "";  
      var uniquieControlId  = "";
      var controlNames      = new Array();
      var sectionControl    = $(sectionId);

      if( !PS.Common.Functions.isNullOrUndefined(sectionControl))
      {
         var inputControls = sectionControl.getElementsByTagName("INPUT");
         var listControls  = sectionControl.getElementsByTagName("SELECT");
         
         var inputControlArea = sectionControl.getElementsByTagName("TEXTAREA");
         
          
         if(!PS.Common.Functions.isNullOrUndefined(inputControlArea))
          {
             for(var index=0;index <inputControlArea.length;index++)
             {
                for(var vIndex=0;vIndex < validations.length;vIndex++)
                {
                    controlId = "txt" + validations[vIndex].id;
                    uniquieControlId = !PS.Common.Functions.isNullOrEmpty(inputControlArea[index].id) ? inputControlArea[index].id : "";
                    var regx=RegExp(controlId+"$");                    
                    if(regx.test(uniquieControlId))
                    {
                        validations[vIndex].value = inputControlArea[index].value;//$(inputControls[index].id);
                        controlNames.push(validations[vIndex].id);
                        break;
                    }
                }
             }
	      }
         
         
         
          if(!PS.Common.Functions.isNullOrUndefined(inputControls))
          {
             for(var index=0;index <inputControls.length;index++)
             {
                for(var vIndex=0;vIndex < validations.length;vIndex++)
                {
                    controlId = "txt" + validations[vIndex].id;
                    uniquieControlId = !PS.Common.Functions.isNullOrEmpty(inputControls[index].id) ? inputControls[index].id : "";
                    var regx=RegExp(controlId+"$");                    
                    //if(uniquieControlId.toLowerCase().indexOf(controlId.toLowerCase()) > 0 )
                    if(regx.test(uniquieControlId))
                    {
                        validations[vIndex].value = inputControls[index].value;//$(inputControls[index].id);
                        controlNames.push(validations[vIndex].id);
                        break;
                    }
                }
             }
	      }
	      
	      if(!PS.Common.Functions.isNullOrUndefined(listControls))
          {
             for(var index=0;index <listControls.length;index++)
             {
                for(var vIndex=0;vIndex < validations.length;vIndex++)
                {
                  controlId = "ddl" + validations[vIndex].id;
                  uniquieControlId = !PS.Common.Functions.isNullOrEmpty(listControls[index].id) ? listControls[index].id : "";
                  var regx=RegExp(controlId+"$");                    
                  //if(uniquieControlId.toLowerCase().indexOf(controlId.toLowerCase()) > 0 )
                  if(regx.test(uniquieControlId))
                  {
                    validations[vIndex].value = listControls[index].value;//$(listControls[index].id);
                    controlNames.push(validations[vIndex].id);
                    break;
                  }
                }
             }
	      }
	  }
     return controlNames;
   }
   
   //For Partial validations.
   function validateSectionFields(sectionId,activeSectionId)
   {
       registerValidationSection(sectionId); 
       var names  = getControlNames(activeSectionId,activePage.getValidations());
       if(names.length >  0)
       {
           var errorMessages  = activePage.getErrorByFieldNames(sectionId,names);   
           if( !PS.Common.Functions.isNullOrEmpty(errorMessages) )
           {
                showErrorMessage(sectionId,errorMessages);
                return false;
           }
       }
       return true;
   }
    
   function validateSection(sectionId)
   {
        registerValidationSection(sectionId);
        var errorMessages = activePage.getErrorMessage(sectionId);
        if( !PS.Common.Functions.isNullOrEmpty(errorMessages))
        {
            showErrorMessage(sectionId,errorMessages);
            return false;
        }
        return true;
   }
   
   function showErrorMessage(sectionId,errorMesage)
   {
        var replacepattern  = /\<BR\/>/gi
        var sectionControl  = $(sectionId);
        var errorControl    = getErrorControl(sectionControl);
        if(errorControl)
        {
            errorControl.style.display = "";
            errorControl.innerHTML  = errorMesage;
        }
        else
        {
            errorMesage = errorMesage.replace(replacepattern,"\n");
            alert(errorMesage);
        }
   }
   
   function getErrorControl(sectionControl)
   {
        var returnControl = null;
        var sectionChildren = sectionControl.getElementsByTagName("DIV");
        for(var index=0;index < sectionChildren.length;index++)
        {
           if( sectionChildren[index].id.indexOf("_divErrorMessage") != -1 )
           {
              returnControl = sectionChildren[index];
              break;
           }
        }
        return returnControl;
   }
   
   function registerRequiredField(sectionId,id,canProceed,errorMessage)
   {
        var validateObject = new PS.UI.Page.ValidationBase.RequiredField(sectionId,id,canProceed,errorMessage);
        activePage.addValidation(validateObject );
   }

   function registerRegularExpression(sectionId,id,expresion,errorMessage)
   {
        var validateObject = new PS.UI.Page.ValidationBase.RegularExpression(sectionId,id,expresion,errorMessage);
        activePage.addValidation(validateObject );
   }
   
   function registerStatusRequiredField(sectionId,id,errorMessage)
   {
        var validateObject = new PS.UI.Page.ValidationBase.StatusRequiredField(sectionId,id,errorMessage);
        activePage.addValidation(validateObject );
   }

   function registerMethod(sectionId,id,isClientSideEnabled,errorMessage)
   {
        var validateObject = new PS.UI.Page.ValidationBase.Method(sectionId,id,errorMessage,isClientSideEnabled);
        activePage.addValidation(validateObject );
   }

   /*---------------------------------------------
        Section Validation Ends
    --------------------------------------------- */
    
    function selectAllItems(e, hiddenField, containerId)
    {
        var grid = $(containerId);
        var rows = grid.select('tr');
        
        var selectedItemid ;
        var selectedItemvalue ;
        
        var selectedHash = new Hash();
        var existingSelectedList ;
        
        if($(hiddenField).value  != '')
            existingSelectedList = eval('(' + $(hiddenField).value + ')');
            
        if(existingSelectedList != null && existingSelectedList != '')
            selectedHash = $H(existingSelectedList);
        
        
        rows = rows.reject(function(row) { return row.className == 'header'; });
        rows.each(function(row) 
            {
                var col = row.select('td')[0];//Selection check box is taken as the first column::TODO 
                if (col != null)    
                {   //the first row will only have THs, not TDs.
                    var checkbox = col.select('input[type="checkbox"]');
                    checkbox[0].checked = e.checked;
                    
                    selectedItemid = checkbox[0].attributes["idTag"].value;
                    selectedItemvalue = checkbox[0].attributes["valueTag"].value;
                    
                    if(e.checked)
                    {
                        if(selectedHash.get(selectedItemid) == null)
                        {
                            selectedHash.set(selectedItemid, selectedItemvalue);                
                        }
                    }
                    else
                    {
                        if(selectedHash.get(selectedItemid) != null)
                        {
                            selectedHash.unset(selectedItemid);
                        }
                    }
                }
            }
        );
        $(hiddenField).value = selectedHash.toJSON();
    }

var oldSelectedClassName;
// Script for selecting a row for case management.
function onCaseWindowClick(tr,selectedId,status)
{
    var tbody=tr.parentNode;
    SetID(selectedId);
    if (tbody)
    {
        var trList=tbody.getElementsByTagName("tr");
        for (var i=0;i<trList.length;i++)
        {
            trList[i].className="";

        }
    }
    if (status !=null)
        tr.className= status.toLowerCase()+ "Case";
    else
        tr.className="selectedRow";
}


function onIncidentWindowClick(tr,selectedId,parentGUID,status)
{

    var tbody=tr.parentNode;  
    SetID(selectedId,parentGUID,status,tr);
    if (tbody)
    {
        var trList=tbody.getElementsByTagName("tr");
        for (var i=0;i<trList.length;i++)
        {
            trList[i].className="";

        }
    }  
    if (status !=null)
        //tr.className= status.toLowerCase()+ "Case";
        tr.className=status.substring(0,1).toLowerCase()+status.substring(1,status.length).replace(" ",'');
    else
        tr.className="selectedRow";
}

var oldSelectedClassName;
// Script for selecting a row for case management.
function onClick(tr,selectedId)
{
    var tbody=tr.parentNode;
    SetID(selectedId);
    if (tbody)
    {
        var trList=tbody.getElementsByTagName("tr");
        for (var i=0;i<trList.length;i++)
        {
            if (trList[i].className=="rowSelect")
                trList[i].className=oldSelectedClassName;
        }
    }
    oldSelectedClassName=tr.className;
    tr.className="rowSelect";
}


function onMouseMoveSelection(tr,selectedId,status)
{
    var tbody=tr.parentNode;
    SetID(selectedId);
    if (tbody)
    {
        var trList=tbody.getElementsByTagName("tr");
        for (var i=0;i<trList.length;i++)
        {
            trList[i].className="";

        }
    }
    if (status !=null)
        tr.className= status.toLowerCase()+ "Case";
    else
        tr.className="selectedRow";
}
function onMouseIn()
{   
//     var tdList= document.getElementById(td).style.backgroundColor = "#d3d2d2";         
     this.style.backgroundColor = "#d3d2d2";
}
function onMouseOut()
{   
//    var tdList= document.getElementById(td).style.backgroundColor = "#FFFFFF";   
    this.style.backgroundColor = "#FFFFFF";
}

/* OBSOLETE
function addRequiredFieldIndicators()   {
    var reqFields = $$('*[IsRequired="true"]');
    reqFields.each(function(control)   {
        control.addClassName('required');
        control.insert({after:'<span class="required indicator">*</span>'});
    });
}
*/

function addRequiredFieldIndicators()   {
    var reqFields = $$('*[IsRequired="true"]');
    reqFields.each(function(control)   {
        control.addClassName('required');
        var id = control.attributes["id"];
        if (id) {
			var i,iL,a = $$("label[for="+id.value+"]");
			for (i=0,iL=a.length;i<iL;i++) a[i].addClassName('required');
        }
    });
}
function LoadRequiredFieldIndicators() {
    if(typeof(IE) == "undefined"){
        addRequiredFieldIndicators();
    }
}
Event.observe(window, 'load', LoadRequiredFieldIndicators);


/************************************************************/       
            
    function mask(e,str,textbox,loc,delim)
    {
        var caretPos = doGetCaretPosition(textbox);
        var key = e.keyCode;
        var regEx = new RegExp (delim, 'gi') ;
        str = str.replace(regEx, '')

        if(key!=37 && key!=39 && key!=46 && key!=8 && key!=36 && key!=16 && key!=17 && key!=35)
        {
            var locs = loc.split(',');
            for (var i = 0; i <= locs.length; i++)
            {
                       for (var k = 0; k <= str.length; k++)
                        {
                         if (k == locs[i])
                         {
                          if (str.substring(k, 1) != delim)
                          {
                            str = str.substring(0,k) + delim + str.substring(k,str.length)
                            if(caretPos==k)
                                caretPos = caretPos+1;
                          }
                         }
                        }
             }
            textbox.value = str
        }
        process(textbox,caretPos);
    }

    function doGetCaretPosition (ctrl) {

                var CaretPos = 0;
                // IE Support
                if (document.selection) {

                            ctrl.focus ();
                            var Sel = document.selection.createRange ();

                            Sel.moveStart ('character', -ctrl.value.length);

                            CaretPos = Sel.text.length;
                }
                // Firefox support
                else if (ctrl.selectionStart || ctrl.selectionStart == '0')
                            CaretPos = ctrl.selectionStart;

                return (CaretPos);

    }


    function setCaretPosition(ctrl, pos)
    {

                if(ctrl.setSelectionRange)
                {
                            ctrl.focus();
                            ctrl.setSelectionRange(pos,pos);
                }
                else if (ctrl.createTextRange) {
                            var range = ctrl.createTextRange();
                            range.collapse(true);
                            range.moveEnd('character', pos);
                            range.moveStart('character', pos);
                            range.select();
                }
    }

    function process(ctrl,pos)
    {
                setCaretPosition(ctrl,pos);
    }
/************************************************************/ 
