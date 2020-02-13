package edu.mit.is.service.authorization;

public class Person
extends org.okip.service.authorization.api.Agent
{
	private static String NAME = "NAME" ;
	private static String KERBEROS_NAME = "KERBEROS_NAME" ;
	private static String EMAIL_ADDR = "EMAIL_ADDR" ;
	private static String DEPT_CODE = "DEPT_CODE" ;
	private static String DEPT_NAME = "DEPT_NAME" ;
	private static String PRIMARY_PERSON_TYPE = "PRIMARY_PERSON_TYPE" ;
	private static String ACTIVE = "ACTIVE" ;
	private static String STATUS_CODE = "STATUS_CODE" ;
	private static String STATUS_DATE = "STATUS_DATE" ;

	private java.util.Properties properties = null;
	public java.util.Properties getProperties() { return this.properties; }
	public void setProperties(java.util.Properties properties)
		{ this.properties = properties; }

	private Factory factory = null ;
	public Factory getFactory() { return this.factory ; }
	public void setFactory( Factory factory )
		{ this.factory = factory ; }

        public Object getProperty( String propertyName )
		{
			if( null != getProperties() )
			{
				Object obj = getProperties().get( propertyName ) ;

				if( null == obj )
				{
					try
					{
						if( isFoundInAuthorization() )
						{
							obj = getProperties().get( propertyName ) ;
						}
					}
					catch( Exception ex )
					{
					}
				}
				return obj ;
			}
			return null ;
		}

    public void setProperty( String propertyName, Object property )
		{
			if( null != getProperties() )
			{
				if( ( null != propertyName ) && ( null != property ) ) {
					getProperties().put( propertyName, property ) ;
				}
			}
		}

	public java.io.Serializable getKey()
	{
		return getKerberosName() ;
	}

	public void setKey( java.io.Serializable key ) {
		setKerberosName((String) key);
	}

	public String getName() {
            return (String) getProperty( NAME );
        }

	public void setName(String displayName) {
            setProperty( NAME, displayName );
        }

	public String getKerberosName()
	{
		return ( String ) getProperty( KERBEROS_NAME ) ;
	}
	protected void setKerberosName( String kerberosName )
	{
		//setProperty( KERBEROS_NAME , kerberosName.toUpperCase() ) ;
            setProperty( KERBEROS_NAME , kerberosName ) ;
	}

	protected String getEmailAddr()
	{
		return ( String ) getProperty( EMAIL_ADDR ) ;
	}
	private void setEmailAddr( String emailAddr )
	{
		setProperty( EMAIL_ADDR , emailAddr ) ;
	}

	public String getDeptCode()
	{
		return ( String ) getProperty( DEPT_CODE ) ;
	}
	public void setDeptCode( String deptCode )
	{
		setProperty( DEPT_CODE , deptCode ) ;
	}

	public String getDeptName()
	{
		return ( String ) getProperty( DEPT_NAME ) ;
	}
	public void setDeptName( String deptName )
	{
		setProperty( DEPT_NAME , deptName ) ;
	}

	protected String getPrimaryPersonType()
	{
		return ( String ) getProperty( PRIMARY_PERSON_TYPE ) ;
	}
	private void setPrimaryPersonType( String primaryPersonType )
	{
		setProperty( PRIMARY_PERSON_TYPE , primaryPersonType ) ;
	}

	// A(ctive) I(nactive)
	protected String getActive()
	{
		return ( String ) getProperty( ACTIVE ) ;
	}
	private void setActive( String active )
	{
		setProperty( ACTIVE , active ) ;
	}

	public String getStatusCode()
	{
		return ( String ) getProperty( STATUS_CODE ) ;
	}
	public void setStatusCode( String statusCode )
	{
		setProperty( STATUS_CODE , statusCode ) ;
	}

	protected java.sql.Timestamp getStatusDate()
	{
		return ( java.sql.Timestamp ) getProperty( STATUS_DATE ) ;
	}
	private void setStatusDate( java.sql.Timestamp statusDate )
	{
		setProperty( STATUS_DATE , statusDate ) ;
	}

	private boolean foundInAuthorization = false;
	public boolean isFoundInAuthorization()
		throws org.okip.service.authorization.api.AuthorizationException
	{
		if(!this.foundInAuthorization)
		{
			findInAuthorization() ;
		}
		return this.foundInAuthorization ;
	}

        /**
         *
         * Sabari
         * Need Coeus implementation
         */
	private void findInAuthorization()
		throws org.okip.service.authorization.api.AuthorizationException
	{
        /*
		if( ( null != getProperties() )
		&&  ( null != getProperties().get( KERBEROS_NAME )  )
		&&  ( null != getFactory() )
		&&  ( null != getFactory().getConnection() )
		)
		{
			String sql
				= new String
					( "select kerberos_name, "
					+ " mit_id, last_name,"
					+ " first_name, email_addr,"
					+ " dept_code, primary_person_type,"
					+ " active, status_code, status_date"
					+ " from person"
					+ " where kerberos_name = '" + getProperties().get( KERBEROS_NAME ) + "'"
					) ;

			Connection connection = getFactory().getConnection() ;
			Table table
				= connection.executeQuery
					( sql
					) ;

			if( null != table )
			{
	            java.util.Properties properties = getPropertiesFromTable( getFactory(), table );
				if ( null != properties )
				{
				  setProperties( properties ) ;
				  this.foundInAuthorization = true;
				  return ;
				}
			}
		}
		this.foundInAuthorization = false;

                */
	}

	public void refresh()
		throws org.okip.service.authorization.api.AuthorizationException
	{
		findInAuthorization() ;
	}

	public Person
		( Factory factory
		, java.util.Properties properties
		)
	{
		setProperties( properties ) ;
		setFactory( factory ) ;
	}

	public Person
		( Factory factory
		, String kerberosName
		, String emailAddr
		, String deptCode
		, String deptName
		, String primaryPersonType
		, String statusCode
		)
	{

                properties = new java.util.Properties();
		setFactory( factory );
		setKerberosName( kerberosName ) ;
		setEmailAddr( emailAddr ) ;
		setDeptCode( deptCode ) ;
		setDeptName( deptName ) ;
		setPrimaryPersonType( primaryPersonType ) ;
		setStatusCode( statusCode ) ;
	}

	public static org.okip.service.authorization.api.Agent create( Factory factory,
                   String kerberosName)
	{
		return new Person
			( factory
			, kerberosName
			, null
			, null
			, null
			, null
			, null
			) ;
	}
/*
	private  java.util.Properties getPropertiesFromTable
			( Factory factory
			, Table table
			)
			throws org.okip.service.authorization.api.AuthorizationException
		{
			java.util.Properties properties = new java.util.Properties() ;
			if( null != table )
			{
				Rows rows = table.getRows() ;
				if( rows.hasMoreRows() )
				{
					Object[] row = rows.nextRow() ;
					String [] columnNames = rows.getColumnNames();

					for( int i = 0;  columnNames.length != i ; i++ )
					{
						if
							(  ( null != columnNames[i] )
							&& ( null != row[i] )
							&& ! ( columnNames[i].equals("") )
							&& ! ( columnNames[i].equalsIgnoreCase("NULL") )
							)
						{
							if( columnNames[i].trim().equals( STATUS_DATE ) )
							{
							    properties.put
							    	( columnNames[i]
							    	, getFactory().createSqlTimestamp
							    	  ( ( String ) row[ i ] )
									);
							}
							else
							{
								properties.put( columnNames[i], row[i] ) ;
							}
						}
					}
				}
			}
			return properties ;
	}
*/
}
