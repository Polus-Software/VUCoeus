package edu.mit.is.service.authorization;

public class QualifierType
extends org.okip.service.authorization.api.QualifierType {
  private static String                                            QUALIFIER_TYPE_DESC =
    "QUALIFIER_TYPE_DESC";

  private Factory factory = null;

  /**
   * Method getFactory
   *
   *
   * @return
   *
   */
  public Factory getFactory() {
    return this.factory;
  }

  protected void setFactory(
          Factory factory) {
    this.factory = factory;
  }

  /**
   * Method getType
   *
   *
   * @return
   *
   */
  public String getType() {
    return getKeyword();
  }

  /**
   * Method getDescription
   *
   *
   * @return
   *
   */
  public String getDescription() {
    if (null == super.getDescription()) {
      try {
        refresh();
      }
      catch (Exception ex) {}
    }

    return super.getDescription();
  }

  private boolean foundInAuthorization = false;

  /**
   * Method isFoundInAuthorization
   *
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public boolean isFoundInAuthorization()
  throws org.okip.service.authorization.api.AuthorizationException {
    if (!this.foundInAuthorization) {
      refresh();
    }

    return this.foundInAuthorization;
  }

  /**
   * Method refresh
   *
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void refresh()
  throws org.okip.service.authorization.api.AuthorizationException {
  /**
   * Sabari
   * Need Coeus Implementation
   */
   /*
    if ((null != getKeyword()) && (null != getFactory())) {
      Connection connection =
        getFactory().getConnection();
      Table      table      =
        connection.executeQuery(new String("select" + " qualifier_type_desc"
                                           + " from qualifier_type"
                                           + " where qualifier_type = '"
                                           + getKeyword() + "'"));

      if (null != table) {
        Rows rows = table.getRows();

        if (rows.hasMoreRows()) {
          Object[] row = rows.nextRow();

          this.foundInAuthorization = true;
        }
      }
    }
    */
  }

  /**
   * Constructor QualifierType
   *
   *
   * @param factory
   * @param type
   * @param description
   *
   */
  public QualifierType(
          Factory factory,
          String type, String description) {
    super(type, description);
    setFactory(factory);
  }

}
