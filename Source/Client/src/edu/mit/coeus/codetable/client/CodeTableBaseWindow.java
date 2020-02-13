/*
 * CodeTableBaseWindow.java
 *
 * Created on December 30, 2002, 11:45 AM
 */




package edu.mit.coeus.codetable.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.beans.* ;
import java.util.* ;
import java.awt.event.WindowListener ;
import javax.swing.ScrollPaneLayout ;


import edu.mit.coeus.codetable.bean.RequestTxnBean ;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.toolbar.*;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusInternalFrame ;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.* ;


/**
 *
 * @author  prahalad
 */
public class CodeTableBaseWindow extends CoeusInternalFrame  implements ActionListener
{
    private CoeusToolBarButton btnSaveCodeTable, btnDeleteCodeTable, btnCloseCodeTable, btnAddCodeTable, btnShowTableInfo ;
    private JPanel pnlForm ;
    private CoeusAppletMDIForm mdiForm ;
    private CoeusMessageResources coeusMessageResources ;
    private CodeTableForm codeTableForm ; 
    private CoeusMenuItem codeTableAdd, codeTableSave, codeTableDelete ,codeTableDescription;
    private   boolean exceptionThrown = false ; 
    boolean hasRights = false;
    
    
    public CodeTableBaseWindow(CoeusAppletMDIForm mdiForm) 
    {
       //super("Code Table Maintenance", mdiForm);
        super(CoeusGuiConstants.TITLE_CODE_TABLE, mdiForm);
       try
        {
        this.mdiForm = mdiForm;
        this.setFrameToolBar(codeTableToolBar()); // add toolbar for this screen
        setFrameMenu(getCodeTableMenu());
//        this.setToolsMenu(getCodeTableMenu()); // add menu for this screen
        formatFields();
        initComponents();
        
        //mdiForm.putFrame("CodeTableMaintenance",this);
        mdiForm.putFrame(CoeusGuiConstants.TITLE_CODE_TABLE,this);
        mdiForm.getDeskTopPane().add(this);
       
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        Dimension dlgSize = getSize();
//        setLocation(screenSize.width/2 - (dlgSize.width/2),
//        screenSize.height/2 - (dlgSize.height/2));
        
        this.setSelected(true);
        this.setVisible(true);               
       
        
        setFrameIcon(mdiForm.getCoeusIcon());
               
        hackToGainFocus();    
         
       }
       catch(Exception ex)
       {
        ex.printStackTrace() ;
       }
     }
    
    private void hackToGainFocus() 
    {
        JFrame frame = new JFrame();
        frame.setLocation(-200,100);
        frame.setSize( 100, 100 );
        frame.show();
        frame.dispose();
        this.dispatchEvent(new InternalFrameEvent(this,InternalFrameEvent.INTERNAL_FRAME_ACTIVATED));
    }
    
     JPanel pnlDescription = null ;
     public void initComponents() 
     {
       pnlForm = new JPanel() ;
         
       BorderLayout borderLayout = new BorderLayout( 0, 0) ;
       pnlForm.setLayout(borderLayout) ;
       codeTableForm = new CodeTableForm() ;
       codeTableForm.userHasRights = hasRights;
       JComponent codeTableComp = codeTableForm.getCodeTableComponent(mdiForm) ;       
       pnlForm.add(codeTableComp , BorderLayout.CENTER ) ;
       pnlForm.setBackground(Color.white) ;
       pnlForm.setPreferredSize(new Dimension(codeTableComp.getWidth(),codeTableComp.getHeight())) ;
       pnlForm.setMinimumSize(new Dimension(codeTableComp.getWidth(),codeTableComp.getHeight())) ;
       
       
       JScrollPane scrlpnlForMainpnl = new javax.swing.JScrollPane(pnlForm); 
       
       scrlpnlForMainpnl.setBackground(Color.white) ; 
       getContentPane().add(scrlpnlForMainpnl) ;
           
       
       coeusMessageResources = CoeusMessageResources.getInstance();
      
        // this will take care of the window closing...
        //prps start
            
        this.addVetoableChangeListener(new VetoableChangeListener()
            {
                public void vetoableChange(PropertyChangeEvent pce)
                throws PropertyVetoException 
                {
                   int i = 0 ;
                    if (exceptionThrown)
                      { // if VeotableChange is invoked by the exception thrown
                        // when user selects cancel button, then do nothing
                          exceptionThrown = false ;
                          return ;
                       }    
                   
                   if (pce.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY))
                   {
                       if (codeTableForm.btnCloseActionPerformed())
                        {
                            CoeusInternalFrame frame 
                                = mdiForm.getFrame(CoeusGuiConstants.TITLE_CODE_TABLE);

                            if (frame != null)
                            {
                                //frame.doDefaultCloseAction();
                                mdiForm.removeFrame(CoeusGuiConstants.TITLE_CODE_TABLE);
                            }
                        }
                        else
                        { 
                            exceptionThrown = true ;
                            throw new PropertyVetoException("Cancelled",pce);   
                        }
                    }// end if pce
                   
                }}) ;
           // prps end
                
           
        
                
                
    }
    
         
    // create the toolbar and buttons on it, this toolbar will be displayed when code table screen is show to the user
    private JToolBar codeTableToolBar() 
    {
        JToolBar toolbar = new JToolBar();
        
        btnAddCodeTable = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),null,
                "Add New Row");
 
        btnSaveCodeTable = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),null,
                "Save Changes");

        btnDeleteCodeTable = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),null,
                "Delete Selected Row");

        btnCloseCodeTable = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),null,
                "Close Code Table");
        
        btnShowTableInfo = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),null,
                "Show Table Description");
        
        btnAddCodeTable.addActionListener(this) ;
        btnSaveCodeTable.addActionListener(this) ;
        btnCloseCodeTable.addActionListener(this) ;
        btnDeleteCodeTable.addActionListener(this) ;
        btnShowTableInfo.addActionListener(this) ;
        
        toolbar.add(btnAddCodeTable);
        toolbar.addSeparator();
        toolbar.add(btnDeleteCodeTable);
        toolbar.addSeparator();        
        toolbar.add(btnSaveCodeTable);
        toolbar.addSeparator();
        toolbar.add(btnShowTableInfo);
        toolbar.addSeparator();
        toolbar.add(btnCloseCodeTable);
       
        
        toolbar.setFloatable(false);
        return toolbar;
    }

    // create the menu to be displyed when code table maintenance screen in displayed    
    private CoeusMenu getCodeTableMenu() 
    {
        CoeusMenu mnuCodeTable = null;
        Vector vecMenuItems = new Vector();

        codeTableAdd = new CoeusMenuItem("Add Row", null, true, true);
        codeTableAdd.setMnemonic('A');
        codeTableAdd.addActionListener(this);

        codeTableSave = new CoeusMenuItem("Save Changes", null, true, true);
        codeTableSave.setMnemonic('S');
        codeTableSave.addActionListener(this);

        codeTableDelete = new CoeusMenuItem("Delete Row", null, true, true);
        codeTableDelete.setMnemonic('D');
        codeTableDelete.addActionListener(this);
        
        codeTableDescription = new CoeusMenuItem("Show Table Info", null, true, true);
        codeTableDescription.setMnemonic('T');
        codeTableDescription.addActionListener(this);


        vecMenuItems.add(codeTableAdd);
        vecMenuItems.add(codeTableDelete);
        vecMenuItems.add(codeTableSave);
        vecMenuItems.add(codeTableDescription);
        
        mnuCodeTable = new CoeusMenu("Edit", null, vecMenuItems, true, true);
        mnuCodeTable.setMnemonic('E');
       
        return mnuCodeTable;

    }
    
    /**
     * This method is used to enable/disable form's menu item or toool bar button..
     */
    private void formatFields(){
        if(!hasMaintainCodeTableRights())
        {
            codeTableAdd.setEnabled(false);
            codeTableSave.setEnabled(false);
            codeTableDelete.setEnabled(false);  
            
            btnAddCodeTable.setEnabled(false);
            btnSaveCodeTable.setEnabled(false);
            btnDeleteCodeTable.setEnabled(false);
        }
    }
    
 /**
 * This method will check user's right *
 * @return boolean. true indicates has rights and false indicates no rights.
 */
    private boolean hasMaintainCodeTableRights()
    {
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/CodeTableServlet";   
        String userid = mdiForm.getUserName();
        RequestTxnBean requestTxnBean = new RequestTxnBean();
        requestTxnBean.setTableName(userid); // just using exist method to pass a string
        requestTxnBean.setAction("GET_USER_MAINTAIN_CODE_TABLES_RIGHTS");
        RequesterBean request = new RequesterBean();
        request.setDataObject(requestTxnBean);        
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();

        if (response != null)
        {
            if (response.isSuccessfulResponse())
            {
                Boolean objRet = (Boolean)response.getDataObject() ;
                if (objRet.booleanValue() != false)
                {
                    hasRights = true;

                }
            }
        }
      
        return hasRights;
    }
    
     public void actionPerformed(ActionEvent actionEvent)
     {
        Object actionSource = actionEvent.getSource();
         
        if (actionSource.equals(btnCloseCodeTable))
        { // if return true then close this screen
            //if (codeTableForm.btnCloseActionPerformed(actionEvent))
            if (codeTableForm.btnCloseActionPerformed())
            {
              exceptionThrown = true ;                
              CoeusInternalFrame frame 
                    = mdiForm.getFrame(CoeusGuiConstants.TITLE_CODE_TABLE);
              if (frame != null){
                  frame.doDefaultCloseAction();
                  mdiForm.removeFrame(CoeusGuiConstants.TITLE_CODE_TABLE);
              }
            }
        }    
            
         //prps check
        // btn add need some fixing, btn save and btn insert functionality to be added
        if (actionSource.equals(btnAddCodeTable) ||actionSource.equals(codeTableAdd))
        { //add new row
            codeTableForm.addBtnClicked(actionEvent) ;
        }
        if (actionSource.equals(btnSaveCodeTable) || actionSource.equals(codeTableSave))
        { //save table data
            codeTableForm.btnSaveActionPerformed(actionEvent) ;
        }
        if (actionSource.equals(btnDeleteCodeTable) || actionSource.equals(codeTableDelete))
        { //detelete row
            codeTableForm.btnDeleteMouseClicked(actionEvent) ;
        }
        if (actionSource.equals(btnShowTableInfo) || actionSource.equals(codeTableDescription))
        { //show description
             Vector vecDetails = codeTableForm.showTableDescription(actionEvent) ;
             //nothing was selected
             if (vecDetails.isEmpty()) return;
             
             SplashWindow sw = new SplashWindow(mdiForm, vecDetails.get(0).toString(), vecDetails.get(1).toString());
            
        }
        

     }
   
     /* Do not delete   */
     public void saveActiveSheet(){
         codeTableForm.btnSaveActionPerformed();
     }
   
     public void saveAsActiveSheet() {
     }     
     
     
class SplashWindow extends JWindow
{
    public SplashWindow(Frame f, String strTblName, String strDescription)
    {
       super(f);
        
       Dimension screenSize =
       Toolkit.getDefaultToolkit().getScreenSize(); 
        
       JLabel lblTblName = new  JLabel(strTblName ) ;
       lblTblName.setFont(new Font(null,Font.BOLD, 18)) ;
       JTextArea txtTblDesc = new JTextArea(strDescription) ;
       txtTblDesc.addMouseListener(new MouseAdapter()
        {
        public void mousePressed(MouseEvent e)
        {
        setVisible(false);
        dispose();
        }
        });
        
        txtTblDesc.addKeyListener( new KeyAdapter() 
        {
            public void keyPressed(KeyEvent e) 
            {
                setVisible(false);
                dispose();
            }
          });
          
       txtTblDesc.setEditable(false);
       txtTblDesc.setFont(new Font(null,Font.PLAIN, 15)) ;
       JLabel btnClose = new JLabel("Click anywhere to close") ;
       btnClose.addMouseListener(new MouseAdapter()
       {
          public void mousePressed(MouseEvent e)
          {
            setVisible(false);
            dispose();
           }
       }) ;
       btnClose.setSize(20, 20) ;
       btnClose.setFont(new Font(null,Font.BOLD, 12)) ;
       btnClose.setForeground(Color.blue) ;
       btnClose.setBackground(Color.white) ;
       btnClose.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT) ;
       
       pnlDescription = new JPanel() ;
       BorderLayout borderLayout2 = new BorderLayout( 0, 0) ;
       pnlDescription.setLayout(borderLayout2) ;
       pnlDescription.add(lblTblName, BorderLayout.NORTH) ;
       pnlDescription.add(txtTblDesc, BorderLayout.CENTER) ;
       pnlDescription.add(btnClose, BorderLayout.SOUTH) ;
              
       // BorderFactory.createLineBorder(Color.gray, 4)
       pnlDescription.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.yellow)) ;
       pnlDescription.setBackground(Color.white) ;
       pnlDescription.setMinimumSize(new Dimension(screenSize.width/2, 100)) ;
       pnlDescription.setPreferredSize(new Dimension(screenSize.width/2, 100)) ;
               
       getContentPane().add(pnlDescription, BorderLayout.CENTER);
        pack();
        
        Dimension labelSize = pnlForm.getPreferredSize();
        
        setLocation(100, 100);

        addMouseListener(new MouseAdapter()
        {
        public void mousePressed(MouseEvent e)
        {
        setVisible(false);
        dispose();
        }
        });
        
        addKeyListener( new KeyAdapter() 
        {
            public void keyPressed(KeyEvent e) 
            {
                setVisible(false);
                dispose();
            }
          });

                setVisible(true);
                requestFocus();


        
    }
  }//end class

     
     
}

