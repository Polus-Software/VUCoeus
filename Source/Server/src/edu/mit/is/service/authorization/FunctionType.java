package edu.mit.is.service.authorization;

public class FunctionType
extends org.okip.service.authorization.api.FunctionType {

  private final static String FUNCTION_DESCRIPTION = "FUNCTION_CATEGORY_DESC";

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

  protected void setFactory(Factory factory) {
    this.factory = factory;
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
      catch (org.okip.service.authorization.api.AuthorizationException ex) {}
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
   /**
    * Sabari
    * Don;t need this
    */
  public void refresh()
  throws org.okip.service.authorization.api.AuthorizationException {
  /*
    if ((null != getKeyword()) && (null != getFactory())
            && (null != getFactory().getConnection())) {
      Connection connection = getFactory().getConnection();
      Table      table      =
        connection.executeQuery(new String("select"
                                           + " function_category_desc"
                                           + " from category"
                                           + " where function_category = '"
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
   * Constructor FunctionType
   *
   *
   * @param factory
   * @param category
   * @param description
   *
   */
  public FunctionType(
          Factory factory,
          String category, String description) {
	super(category, description);
    setFactory(factory);
  }
  
  /**
   * Constructor FunctionType
   *
   *
   * @param factory
   * @param category
   *
   *This constructor added by sabari
   */
  public FunctionType(
          Factory factory,
          String category) {
	super(category, null);
    setFactory(factory);
  }

}
