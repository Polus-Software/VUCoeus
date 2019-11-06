/**
 * ApplicantIntegrationSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0;

public class ApplicantIntegrationSoapBindingStub extends org.apache.axis.client.Stub implements gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationPortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[4];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetOpportunityList");
        oper.addParameter(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "GetOpportunityListRequest"), new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetOpportunityListRequest"), gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetOpportunityListResponse"));
        oper.setReturnClass(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "GetOpportunityListResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "fault"),
                      "org.xmlsoap.schemas.wsdl.soap.TFault",
                      new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tFault"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SubmitApplication");
        oper.addParameter(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "SubmitApplicationRequest"), new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">SubmitApplicationRequest"), gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._SubmitApplicationRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">SubmitApplicationResponse"));
        oper.setReturnClass(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._SubmitApplicationResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "SubmitApplicationResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "fault"),
                      "org.xmlsoap.schemas.wsdl.soap.TFault",
                      new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tFault"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetApplicationList");
        oper.addParameter(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "GetApplicationListRequest"), new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationListRequest"), gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationListResponse"));
        oper.setReturnClass(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "GetApplicationListResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "fault"),
                      "org.xmlsoap.schemas.wsdl.soap.TFault",
                      new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tFault"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetApplicationStatusDetail");
        oper.addParameter(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "GetApplicationStatusDetailRequest"), new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationStatusDetailRequest"), gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationStatusDetailResponse"));
        oper.setReturnClass(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "GetApplicationStatusDetailResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "fault"),
                      "org.xmlsoap.schemas.wsdl.soap.TFault",
                      new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tFault"), 
                      true
                     ));
        _operations[3] = oper;

    }

    public ApplicantIntegrationSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public ApplicantIntegrationSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public ApplicantIntegrationSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max50Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max50Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max120Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max120Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tStyleChoice");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.soap.TStyleChoice.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tBody");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.soap.TBody.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max60Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max60Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max3Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max3Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tTypes");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TTypes.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tExtensibleAttributesDocumented");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TExtensibleAttributesDocumented.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "OpportunityInformationType");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.OpportunityInformationType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tParam");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TParam.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max110Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max110Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tBindingOperationFault");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TBindingOperationFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tHeader");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.soap.THeader.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tBinding");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TBinding.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "useChoice");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.soap.UseChoice.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">SubmitApplicationResponse");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._SubmitApplicationResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationListResponse");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max35Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max35Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "YesNoType");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.YesNoType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tOperation");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.soap.TOperation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "IntegerMin1Max3Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.IntegerMin1Max3Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "ApplicationInformationType");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicationInformationType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tHeaderFault");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.soap.THeaderFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationStatusDetailResponse");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tOperation");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TOperation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "IntegerMin1Max2Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.IntegerMin1Max2Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tExtensibilityElement");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TExtensibilityElement.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max30Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max30Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tBindingOperationMessage");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TBindingOperationMessage.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", ">HashValue");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0._HashValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tService");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TService.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max15Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max15Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max144Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max144Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tAddress");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.soap.TAddress.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max200Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max200Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tPart");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TPart.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationListRequest>ApplicationFilter>Filter");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest_ApplicationFilter_Filter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationStatusDetailRequest");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tImport");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TImport.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tExtensibleDocumented");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TExtensibleDocumented.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max240Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max240Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tDocumentation");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TDocumentation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max250Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max250Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tBindingOperation");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TBindingOperation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max255Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max255Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max55Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max55Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max4096Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max4096Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tPort");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TPort.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tFaultRes");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.soap.TFaultRes.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max100Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max100Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">SubmitApplicationRequest");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._SubmitApplicationRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "DecimalMin1Max4Places2Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.DecimalMin1Max4Places2Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "DecimalMin1Max15Places2Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.DecimalMin1Max15Places2Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tBinding");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.soap.TBinding.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max2Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max2Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tPortType");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TPortType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationListRequest");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationListRequest>ApplicationFilter");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest_ApplicationFilter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tDefinitions");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TDefinitions.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max13Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max13Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetOpportunityListRequest");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "DecimalMin1Max14Places2Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.DecimalMin1Max14Places2Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tFault");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetOpportunityListResponse");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "encodingStyle");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.soap.EncodingStyle.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tFault");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.soap.TFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "GrantsGovApplicationStatusType");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.GrantsGovApplicationStatusType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max25Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max25Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max80Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max80Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tDocumented");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TDocumented.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max10Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max10Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/", "tMessage");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.TMessage.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max45Type");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.system.Global_V1_0.StringMin1Max45Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

    }

    private org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                        java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                        _call.registerTypeMapping(cls, qName, sf, df, false);
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", t);
        }
    }

    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListResponse getOpportunityList(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListRequest getOpportunityListRequest) throws java.rmi.RemoteException{
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://localhost:8080/app-s2s-server/services/ApplicantIntegrationSoapPort/GetOpportunityList");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetOpportunityList"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {getOpportunityListRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListResponse) org.apache.axis.utils.JavaUtils.convert(_resp, gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListResponse.class);
            }
        }
    }

    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._SubmitApplicationResponse submitApplication(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._SubmitApplicationRequest submitApplicationRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://localhost:8080/app-s2s-server/services/ApplicantIntegrationSoapPort/SubmitApplication");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "SubmitApplication"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {submitApplicationRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._SubmitApplicationResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._SubmitApplicationResponse) org.apache.axis.utils.JavaUtils.convert(_resp, gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._SubmitApplicationResponse.class);
            }
        }
    }

    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListResponse getApplicationList(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest getApplicationListRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://localhost:8080/app-s2s-server/services/ApplicantIntegrationSoapPort/GetApplicationList");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetApplicationList"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {getApplicationListRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListResponse) org.apache.axis.utils.JavaUtils.convert(_resp, gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListResponse.class);
            }
        }
    }

    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailResponse getApplicationStatusDetail(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailRequest getApplicationStatusDetailRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://localhost:8080/app-s2s-server/services/ApplicantIntegrationSoapPort/GetApplicationStatusDetail");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetApplicationStatusDetail"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {getApplicationStatusDetailRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailResponse) org.apache.axis.utils.JavaUtils.convert(_resp, gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailResponse.class);
            }
        }
    }

}
