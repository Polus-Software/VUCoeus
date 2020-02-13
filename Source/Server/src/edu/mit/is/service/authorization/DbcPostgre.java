package edu.mit.is.service.authorization;
/*
   Copyright (c) 2002 Massachusetts Institute of Technology
 
   This work, including any software, documents, or other related items
   (the "Work"), is being provided by the copyright holder(s) subject to
   the terms of the MIT OKI(TM) API Definition License. By obtaining,
   using and/or copying this Work, you agree that you have read,
   understand, and will comply with the following terms and conditions of
   the MIT OKI(TM) API Definition License:
 
   You may use, copy, and distribute unmodified versions of this Work for
   any purpose, without fee or royalty, provided that you include the
   following on ALL copies of the Work that you make or distribute:
 
 *  The full text of the MIT OKI(TM) API Definition License in a
       location viewable to users of the redistributed Work.
 
 *  Any pre-existing intellectual property disclaimers, notices, or
       terms and conditions. If none exist, a short notice similar to the
       following should be used within the body of any redistributed
       Work: "Copyright (c) 2002 Massachusetts Institute of Technology. All
       Rights Reserved."
 
   You may modify or create Derivatives of this Work only for your
   internal purposes. You shall not distribute or transfer any such
   Derivative of this Work to any location or any other third party. For
   purposes of this license, "Derivative" shall mean any derivative of
   the Work as defined in the United States Copyright Act of 1976, such
   as a translation or modification.
 
   THIS WORK PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
   IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
   CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
   TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE WORK
   OR THE USE OR OTHER DEALINGS IN THE WORK.
 
   The name and trademarks of copyright holder(s) and/or MIT may NOT be
   used in advertising or publicity pertaining to the Work without
   specific, written prior permission. Title to copyright in the Work and
   any associated documentation will at all times remain with the
   copyright holders.
 
 */


public class DbcPostgre {
    
    public static void main(String[] args) {
        
        boolean heCanDoIt;
        edu.mit.is.service.authorization.Person person;
        edu.mit.is.service.authorization.Function function;
        edu.mit.is.service.authorization.Qualifier qualifier;
        edu.mit.is.service.authorization.FunctionType functionType;
        edu.mit.is.service.authorization.PersonsIterator personsIterator;
        edu.mit.is.service.authorization.FunctionsIterator functionsIterator;
        edu.mit.is.service.authorization.Authorization authorization;
        edu.mit.is.service.authorization.AuthorizationsIterator authorizationsIterator;
        int itemCount;
        
        try {
            //get an instance of Dbc Factory
            //Takes two package names. 1. API package 2. Implementation Package
            Factory myFactory =
            (Factory) org.okip.service.shared.api.FactoryManager.getFactory(
            "org.okip.service.authorization.api",
            "edu.mit.is.service.authorization",
            null
            );
            person = (Person) myFactory.newPerson("snair");
            
            
            //Does the user have a right in a given unit?
            System.out.println("Does the user have a right in a given unit?");
            function = (Function) myFactory.newFunction("VIEW_AWARD");
            qualifier = (Qualifier) myFactory.newQualifier("000001");
            
            heCanDoIt = myFactory.isAuthorized(person, function, qualifier);
            if ( heCanDoIt == true ) {
                System.out.println(person.getKey()  + " has the right  " + function.getKey()
                + " in unit  " + qualifier.getKey() );
                System.out.println() ;
            }
            else {
                System.out.println(person.getKey()  + " DO NOT have the right  " + function.getKey()
                + " in unit  " + qualifier.getKey() );
                System.out.println() ;
            }
            
            
            //Does the user have a right in a ANY unit?
            System.out.println("Does the user have a right in a ANY unit?");
            function = (Function) myFactory.newFunction("CREATE_PROPOSAL");
            //commented by Geo
//            heCanDoIt = myFactory.isAuthorized(person, function, null);
            if ( heCanDoIt == true ) {
                System.out.println(person.getKey()  + " has the right  " + function.getKey()
                + " in some unit  " );
                System.out.println() ;
            }
            else {
                System.out.println(person.getKey()  + " DO NOT have the right  " + function.getKey()
                + " in any unit  ");
                System.out.println() ;
            }
            
            //Does the user have a type of right in ANY unit?
            System.out.println("Does the user have a type of right in ANY unit?");
            functionType = (FunctionType) myFactory.newFunctionType("OSP", "");
            
            heCanDoIt = myFactory.isAuthorized(person, functionType, null);
            if ( heCanDoIt == true ) {
                System.out.println(person.getKey()  + " has atleast one right of type   " + functionType.getKeyword()
                + " in sone unit  " );
                System.out.println() ;
            }
            else {
                System.out.println(person.getKey()  + " DO NOT have any right of type " + functionType.getKeyword()
                + " in any unit  ");
                System.out.println() ;
            }
            
            //Does the user have a type of right in a given unit?
            System.out.println("Does the user have a type of right in a given unit?");
            functionType = (FunctionType) myFactory.newFunctionType("OSP", "");
            qualifier = (Qualifier) myFactory.newQualifier("000001");
            
            heCanDoIt = myFactory.isAuthorized(person, functionType, qualifier);
            if ( heCanDoIt == true ) {
                System.out.println(person.getKey()  + " has atleast one right of type   " + functionType.getKeyword()
                + " in the department  " + qualifier.getKey() );
                System.out.println() ;
            }
            else {
                System.out.println(person.getKey()  + " DO NOT have any right of type " + functionType.getKeyword()
                + " in the department  " + qualifier.getKey());
                System.out.println() ;
            }
            
            //Does the user have a role in a given unit?.  Looking for role 1 in unit 000001
            System.out.println("Does the user have a role in a given unit?.  Looking for role 1 in unit 000001");
            function = (Function) myFactory.newFunction("ROLE", "1");
            qualifier = (Qualifier) myFactory.newQualifier("000001");
            
            try {
                heCanDoIt = myFactory.isAuthorized(person, function, qualifier);
                if ( heCanDoIt == true ) {
                    System.out.println(person.getKey()  + " has the role   " + function.getKey()
                    + " in the department  " + qualifier.getKey() );
                    System.out.println() ;
                }
                else {
                    System.out.println(person.getKey()  + " DO NOT have the role " + function.getKey()
                    + " in the department  " + qualifier.getKey());
                    System.out.println() ;
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            //Does the user have a role in ANY unit?.  Looking for role 2 in ANY unit
            System.out.println("Does the user have a role in ANY unit?.  Looking for role 2 in ANY unit");
            function = (Function) myFactory.newFunction("ROLE", "2");
            
//            heCanDoIt = myFactory.isAuthorized(person, function, null);
            if ( heCanDoIt == true ) {
                System.out.println(person.getKey()  + " has the role   " + function.getKey()
                + " in atleast one department  ");
                System.out.println() ;
            }
            else {
                System.out.println(person.getKey()  + " DO NOT have the role " + function.getKey()
                + " in ANY department  " + qualifier.getKey());
                System.out.println() ;
            }
            
            //Does the user have a right in a particular Proposal.
            System.out.println("Does the user have a right in a particular Proposal. Looking for VIEW_NARRATIVE in proposal 00000673");
            function = (Function) myFactory.newFunction("VIEW_NARRATIVE");
            qualifier = (Qualifier) myFactory.newQualifier("PROPOSAL", "00000673");
            
            heCanDoIt = myFactory.isAuthorized(person, function, qualifier);
            if ( heCanDoIt == true ) {
                System.out.println(person.getKey()  + " has the right  " + function.getKey()
                + " in proposal  " + qualifier.getKey());
                System.out.println() ;
            }
            else {
                System.out.println(person.getKey()  + " DO NOT have the right " + function.getKey()
                + " in proposal  " + qualifier.getKey());
                System.out.println() ;
            }
            
            //Does the user have a particular role in a proposal?
            System.out.println("Does the user have a particular role in a proposal?. Looking for role 100 (aggregatro) in proposal 00000673");
            function = (Function) myFactory.newFunction("ROLE", "100");
            qualifier = (Qualifier) myFactory.newQualifier("PROPOSAL", "00000673");
            
            heCanDoIt = myFactory.isAuthorized(person, function, qualifier);
            if ( heCanDoIt == true ) {
                System.out.println(person.getKey()  + " has the role  " + function.getKey()
                + " in proposal  " + qualifier.getKey());
                System.out.println() ;
            }
            else {
                System.out.println(person.getKey()  + " DO NOT have the role  " + function.getKey()
                + " in proposal  " + qualifier.getKey());
                System.out.println() ;
            }
            
            //Get a list of all Users who has a particular role in a department
            function = (Function) myFactory.newFunction("ROLE", "12");
            qualifier = (Qualifier) myFactory.newQualifier("000001");
            
            personsIterator = (PersonsIterator) myFactory.getWhoCanDo(function, qualifier, true);
            if (personsIterator != null) {
                itemCount = personsIterator.getPersonCount();
                
                System.out.println("Person Count : " + itemCount);
                System.out.println("Users who has role " +  function.getKey() 
                        +  " in department "  + qualifier.getKey());
                System.out.println("******************************************");
                
                while (personsIterator.hasNext()) {
                     person = (Person) personsIterator.next();
                    
                    System.out.println( person.getKey() + "  " +
                            person.getName() + " Descend:" + person.getStatusCode());
                }
            } else {
                System.out.println("PersonsIterator is NULL");
            }
            
            //Get a list of all Users who has a particular role in a Proposal
            function = (Function) myFactory.newFunction("ROLE", "100");
            qualifier = (Qualifier) myFactory.newQualifier("PROPOSAL", "00000673");
            
            personsIterator = (PersonsIterator) myFactory.getWhoCanDo(function, qualifier, true);
            if (personsIterator != null) {
                itemCount = personsIterator.getPersonCount();
                
                System.out.println("Person Count : " + itemCount);
                System.out.println("Users who has role " +  function.getKey() 
                        +  " in proposal "  + qualifier.getKey());
                System.out.println("******************************************");
                
                while (personsIterator.hasNext()) {
                     person = (Person) personsIterator.next();
                    
                    System.out.println( person.getKey() + "  " +
                            person.getName() + " " + 
                            person.getDeptCode() + ":" + person.getDeptName() + 
                            " Status:" + person.getStatusCode());
                }
            } else {
                System.out.println("PersonsIterator is NULL");
            }
            
            
            //Get a list of all Rights in Coeus - all Functions
            functionType = (FunctionType) myFactory.newFunctionType("ALL", "");
            
            System.out.println();
            System.out.println();
            System.out.println("******************************************");
            System.out.println("Get a list of all Rights in Coeus - all Functions");
            System.out.println("All functions of type " +  functionType.getKeyword());
            System.out.println("******************************************");
            
            functionsIterator = (FunctionsIterator) myFactory.getFunctionsOfType(functionType);
            if (functionsIterator != null) {
                itemCount = functionsIterator.getFunctionCount();
                
                System.out.println("Function Count : " + itemCount);
                
                while (functionsIterator.hasNext()) {
                     function = (Function) functionsIterator.next();
                    
                    System.out.println( function.getKey() + "  " +
                            function.getDescription() + "  " +
                            function.getFunctionType().getKeyword() + " " + 
                            function.getFunctionType().getDescription());
                }
            } else {
                System.out.println("functionsIterator is NULL");
            }
            
            //Get a list of all Rights of a particular type
            functionType = (FunctionType) myFactory.newFunctionType("P", "");
            
            System.out.println();
            System.out.println("******************************************");
            System.out.println("Get a list of all Rights of a particular type");
            System.out.println("All functions of type " +  functionType.getKeyword());
            System.out.println("******************************************");
            
            functionsIterator = (FunctionsIterator) myFactory.getFunctionsOfType(functionType);
            if (functionsIterator != null) {
                itemCount = functionsIterator.getFunctionCount();
                
                System.out.println();
                System.out.println("Function Count : " + itemCount);
                
                while (functionsIterator.hasNext()) {
                     function = (Function) functionsIterator.next();
                    
                    System.out.println( function.getKey() + "  " +
                            function.getDescription() + "  " +
                            function.getFunctionType().getKeyword() + " " + 
                            function.getFunctionType().getDescription());
                }
            } else {
                System.out.println("functionsIterator is NULL");
            }
            
            
            //Get a list of all Rights in a role
            //In this case pass ROLE in function type key and set role Id in description
            functionType = (FunctionType) myFactory.newFunctionType("ROLE", "1");
            
            System.out.println();
            System.out.println("******************************************");
            System.out.println("Get a list of all Rights in a role");
            System.out.println("All functions in role " +  functionType.getDescription());
            System.out.println("******************************************");
            
            functionsIterator = (FunctionsIterator) myFactory.getFunctionsOfType(functionType);
            if (functionsIterator != null) {
                itemCount = functionsIterator.getFunctionCount();
                
                System.out.println();
                System.out.println("Function Count : " + itemCount);
                
                while (functionsIterator.hasNext()) {
                     function = (Function) functionsIterator.next();
                    
                    System.out.println( function.getKey() + "  " +
                            function.getDescription() + "  " +
                            function.getFunctionType().getKeyword() + " " + 
                            function.getFunctionType().getDescription());
                }
            } else {
                System.out.println("functionsIterator is NULL");
            }
            
            
            
            //Get a list of all Roles for a user
            person = (Person) myFactory.newPerson("snair");
            
            System.out.println();
            System.out.println("******************************************");
            System.out.println("Get a list of all Roles for a user");
            System.out.println("All roles for user  " +  person.getKey());
            System.out.println("******************************************");
            
            authorizationsIterator = (AuthorizationsIterator) myFactory.getAuthorizations(person, true, true);
            if (authorizationsIterator != null) {
                itemCount = authorizationsIterator.getAuthorizationCount();
                
                System.out.println("Authorization Count : " + itemCount);
                
                while (authorizationsIterator.hasNext()) {
                     authorization = (Authorization) authorizationsIterator.next();
                    
                    System.out.println( authorization.getFunction().getKey() + "   " + 
                                        authorization.getFunction().getDescription() + " at unit " +
                                        authorization.getQualifier().getKey());
                }
            } else {
                System.out.println("authorizationsIterator is NULL");
            }
            
            
            //Get a list of all authorizations in a unit.
            qualifier = (Qualifier) myFactory.newQualifier("151000");
            
            System.out.println();
            System.out.println("******************************************");
            
            System.out.println("All authorizations in unit  " +  qualifier.getKey());
            System.out.println("******************************************");
            
            authorizationsIterator = (AuthorizationsIterator) myFactory.getAuthorizations(
            (Person) null, (Function) null, qualifier, true, true);
            if (authorizationsIterator != null) {
                itemCount = authorizationsIterator.getAuthorizationCount();
                
                System.out.println("Authorization Count : " + itemCount);
                
                while (authorizationsIterator.hasNext()) {
                     authorization = (Authorization) authorizationsIterator.next();
                    person = (Person) authorization.getAgent();
                    
                    System.out.println( authorization.getAgent().getName() + " has the role  " + 
                                        authorization.getFunction().getKey() + "   " + 
                                        authorization.getFunction().getDescription() + " at unit " +
                                        authorization.getQualifier().getKey() + " * Role Type:  " +
                                        authorization.getFunction().getFunctionType().getKeyword() + " *** " + 
                                        authorization.getFunction().getFunctionType().getDescription() + 
                                        " user status: " + person.getStatusCode());
                }
            } else {
                System.out.println("authorizationsIterator is NULL");
            }
            
             //Get a list of all authorizations in a Proposal.
            qualifier = (Qualifier) myFactory.newQualifier("PROPOSAL", "00000673");
            
            System.out.println();
            System.out.println();
            System.out.println("******************************************");
            System.out.println("Get a list of all authorizations in a Proposal.");
            System.out.println("All authorizations in proposal  " +  qualifier.getKey());
            System.out.println("******************************************");
                
                
            authorizationsIterator = (AuthorizationsIterator) myFactory.getAuthorizations(
                                    (Person) null, (Function) null, qualifier, true, true);
            if (authorizationsIterator != null) {
                itemCount = authorizationsIterator.getAuthorizationCount();
                
                System.out.println("Authorization Count : " + itemCount);
                
                while (authorizationsIterator.hasNext()) {
                     authorization = (Authorization) authorizationsIterator.next();
                    person = (Person) authorization.getAgent();
                    
                    System.out.println( authorization.getAgent().getName() + " has the role  " + 
                                        authorization.getFunction().getKey() + "   " + 
                                        authorization.getFunction().getDescription() + 
                                        " user status: " + person.getStatusCode());
                }
            } else {
                System.out.println("authorizationsIterator is NULL");
            }
            
        } catch (Exception ex) {
            System.out.println( ex.getMessage() );
        }
        
        
    }
}

