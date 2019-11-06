package edu.mit.is.service.authorization;

public class QualifiersIterator
implements org.okip.service.authorization.api.QualifiersIterator {

//  protected Rows                rows         =   null; //Sabari
  protected Factory factory =
    null;

  /**
   * Method hasNext
   *
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
   /**
    * Sabari
    * Need Coeus impl
    */
  public boolean hasNext()
  throws org.okip.service.authorization.api.AuthorizationException {
    return false;
  }

  /**
   * Method next
   *
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
   /**
    * Sabari
    * Need Coeus impl
    */

    /**
    * Sabari
    * Need Coeus impl
    */
  public org.okip.service.authorization.api.Qualifier next()
  throws org.okip.service.authorization.api.AuthorizationException {
  /*
    if (null != this.rows) {
      return getQualifier(this.rows.getColumnNames(), this.rows.nextRow());
    }

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.QUALIFIERENUMERATIONFAILED);
      */
      return null;
  }

  /**
   * Method getQualifierChildren
   *
   *
   * @param factory
   * @param qualifier
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
   /**
    * Sabari
    * Need Coeus impl
    */
  public static org.okip.service.authorization.api.QualifiersIterator getQualifierChildren(
          Factory factory,
          org.okip.service.authorization.api.Qualifier qualifier)
  throws org.okip.service.authorization.api.AuthorizationException {

  /*
    if ((null != factory) && (null != qualifier)) {
      Connection connection = factory.getConnection();
      Table table = connection.executeQuery(
        new String(
          "select" + " q.qualifier_id, q.qualifier_code, q.qualifier_name,"
          + " q.qualifier_type, q.has_child"
          + " from qualifier q0, qualifier_child qc, qualifier q" + " where "
          + " q0.qualifier_type = '"
          + ((QualifierType) qualifier.getQualifierType()).getType()
          + "'" + " and q0.qualifier_code = '"
          + ((Qualifier) qualifier).getKey()
          + "'" + " and qc.parent_id = q0.qualifier_id"
          + " and q.qualifier_id = qc.child_id"
          + " order by q.qualifier_code"));

      if (null != table) {
        QualifiersIterator qualifiers =
          new QualifiersIterator();

        qualifiers.factory = factory;
        qualifiers.rows = table.getRows();

        return qualifiers;
      }
    }

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.INVALIDQUALIFIER);
      */
      return null;
  }

  /**
    * Sabari
    * Need Coeus impl
    */
  protected static org.okip.service.authorization.api.QualifiersIterator getQualifiers(
          Factory factory,
          String sqlQuery)
  throws org.okip.service.authorization.api.AuthorizationException {

  /*
    try {
      if (null != sqlQuery) {
        Connection connection = factory.getConnection();
        Table table = connection.executeQuery(sqlQuery);
        QualifiersIterator qualifiers = new QualifiersIterator();

        qualifiers.factory = factory;
        qualifiers.rows = table.getRows();

        return qualifiers;
      }
    }
    catch (Exception ex) {
      throw new org.okip.service.authorization.api.AuthorizationException(
        ex.getMessage());
    }

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.QUALIFIERENUMERATIONFAILED);

*/
return null;
  }

  /**
    * Sabari
    * Not needed
    */

  private org.okip.service.authorization.api.Qualifier getQualifier(
          String[] columnNames, Object[] row)
  throws org.okip.service.authorization.api.AuthorizationException {
  /*
    if ((null != columnNames) && (null != row)) {
      java.util.Properties properties = new java.util.Properties();

      for (int i = 0; columnNames.length != i; i++) {
        properties.put(columnNames[i], row[i]);
      }

      return new Qualifier(
        this.factory, properties);
    }

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.QUALIFIERENUMERATIONFAILED);
      */
      return null;
  }
}
