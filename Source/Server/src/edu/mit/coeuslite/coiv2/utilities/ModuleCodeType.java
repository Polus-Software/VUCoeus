package edu.mit.coeuslite.coiv2.utilities;

import java.io.Serializable;

public final class ModuleCodeType implements Serializable {
	private String value;
	ModuleCodeType(String value)
	{
		this.value=value;
	}
	public static final ModuleCodeType award=new ModuleCodeType("Award");
	public static final ModuleCodeType proposal=new ModuleCodeType("Proposal");
        public static final ModuleCodeType protocol=new ModuleCodeType("IRB Protocol");
	public static final ModuleCodeType iacucprotocol=new ModuleCodeType("IACUC Protocol");
        public static final ModuleCodeType travel=new ModuleCodeType("Travel");
        public static final ModuleCodeType annual=new ModuleCodeType("Annual");
        public static final ModuleCodeType update=new ModuleCodeType("Revision");
        public static final ModuleCodeType other=new ModuleCodeType("Other");


        public static final ModuleCodeType disclaward=new ModuleCodeType("Award");
	public static final ModuleCodeType disclproposal=new ModuleCodeType("Development Proposal");
        public static final ModuleCodeType disclinstprop=new ModuleCodeType("Institute Proposal");
        public static final ModuleCodeType disclprotocol=new ModuleCodeType("IRB Protocol");
	public static final ModuleCodeType discliacucprotocol=new ModuleCodeType("IACUC Protocol");
        public static final ModuleCodeType disclannual=new ModuleCodeType("Annual Coi Disclosure");


        
	public String getValue() {
	    return value;
	}
}
