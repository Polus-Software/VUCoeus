
/*
 * CoeusOptionPane.java
 *
 * Created on November 7 2002, 3:15 PM
 */

package edu.mit.coeus.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.JTextComponent;

import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;

class ButtonCancelAction implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (CoeusOptionPane.buttonOptions == CoeusOptionPane.OPTION_OK_CANCEL
				|| CoeusOptionPane.buttonOptions == CoeusOptionPane.OPTION_YES_NO_CANCEL) {
			CoeusOptionPane.btnCancelPressed();
		}
	}
}

// added to perform menmonics
class ButtonNoAction implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (CoeusOptionPane.buttonOptions == CoeusOptionPane.OPTION_YES_NO
				|| CoeusOptionPane.buttonOptions == CoeusOptionPane.OPTION_YES_NO_CANCEL)
			CoeusOptionPane.btnNoPressed();
	}
}

class ButtonYesAction implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (CoeusOptionPane.buttonOptions == CoeusOptionPane.OPTION_YES_NO
				|| CoeusOptionPane.buttonOptions == CoeusOptionPane.OPTION_YES_NO_CANCEL)
			CoeusOptionPane.btnYesPressed();
	}
}

class CoeusHTMLPane extends JEditorPane {
	public CoeusHTMLPane(String htmlStr) {
		super("text/html", htmlStr);
		setEditable(false);
		setSize(new Dimension(500, 200));
		setFont(CoeusFontFactory.getNormalFont());
		setBackground(UIManager.getColor("OptionPane.background"));
		addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				HyperlinkEvent.EventType type = e.getEventType();
				if (type.equals(HyperlinkEvent.EventType.ENTERED)) {
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else if (type.equals(HyperlinkEvent.EventType.EXITED)) {
					setCursor(Cursor.getDefaultCursor());
				} else {
					openPage(e.getURL());
				}
			}

			private void openPage(java.net.URL url) {
				try {
					BasicService bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
					bs.showDocument(url);
				} catch (UnavailableServiceException uEx) {
					uEx.printStackTrace();
					JOptionPane.showMessageDialog(CoeusGuiConstants.getMDIForm(), uEx.getMessage());
				}
				return;
			}
		});

	}
}

/**
 * A substitute for JOptionPane that gives better control to operator including
 * mnemonics and defaulting buttons. <br>
 * CoeusOptionPane allows for input, error, information, warning and question
 * dialogs as well as a multiple option dialog.
 */

public class CoeusOptionPane {

	private static JDialog dialog;
	private static JTextComponent txtInput;
	private static JButton btnCancel, btnOk, btnYes, btnNo;

	private static CoeusMessageResources coeusMessageResources;

	// Button options
	/**
	 * OK Option Value
	 */
	public static final int OPTION_OK = 0;
	/**
	 * OK_Cancel Option Value
	 */
	public static final int OPTION_OK_CANCEL = 1;
	/**
	 * YES_NO Option Value
	 */
	public static final int OPTION_YES_NO = 2;
	/**
	 * YES_NO_CANCEL Option Value
	 */
	public static final int OPTION_YES_NO_CANCEL = 3;
	public static int buttonOptions;
	// Default selections
	/**
	 * DEFAULT OK Option Value
	 */
	public static final int DEFAULT_OK = 0;
	/**
	 * DEFULT CANCEL Option Value
	 */
	public static final int DEFAULT_CANCEL = 1;
	/**
	 * DEFULT YES Option Value
	 */
	public static final int DEFAULT_YES = 2;
	/**
	 * DEFULT NO Option Value
	 */
	public static final int DEFAULT_NO = 3;

	// Styles
	private static final int STYLE_WARNING = 0;
	private static final int STYLE_ERROR = 1;
	private static final int STYLE_INFO = 2;
	private static final int STYLE_QUESTION = 3;
	private static ButtonNoAction bNoAction = new ButtonNoAction();
	private static ButtonCancelAction bCancelAction = new ButtonCancelAction();
	private static ButtonYesAction bYesAction = new ButtonYesAction();
	// Return selections
	/**
	 * SELECTION YES Option Value
	 */
	public static final int SELECTION_YES = JOptionPane.YES_OPTION;
	/**
	 * SELECTION NO Option Value
	 */
	public static final int SELECTION_NO = JOptionPane.NO_OPTION;
	/**
	 * SELECTION CANCEL Option Value
	 */
	public static final int SELECTION_CANCEL = JOptionPane.CANCEL_OPTION;

	// selection is the last pressed buttons value
	private static int selection = 0;
	// Internal storage var. for setting focus on preferred buttons.
	private static int defaultSelection = -1;

	/**
	 * boolean which represents whether ESC key event is propagating to its
	 * parent
	 */
	private static boolean propagating;

	/**
	 * Method to add a 'Cancel' button
	 */

	private static void addCancelButton(JPanel parent) {
		btnCancel = new JButton("Cancel");
		btnCancel.setMnemonic('C');
		btnCancel.setFont(CoeusFontFactory.getLabelFont());
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnCancelPressed();
			}
		});
		parent.add(btnCancel);
	}

	/**
	 * Method to add a 'No' button
	 */

	private static void addNoButton(JPanel parent) {
		btnNo = new JButton("No");
		btnNo.setMnemonic('N');
		btnNo.setFont(CoeusFontFactory.getLabelFont());
		btnNo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnNoPressed();
			}
		});
		btnNo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_N) {
					btnNoPressed();
				}
			}
		});

		parent.add(btnNo);
	}

	/**
	 * Method to add a 'OK' button
	 */

	private static void addOkButton(JPanel parent) {
		btnOk = new JButton("OK");
		btnOk.setMnemonic('O');
		btnOk.setFont(CoeusFontFactory.getLabelFont());
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnOkPressed();
			}
		});
		btnOk.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_O) {
					btnOkPressed();
				}
			}
		});
		parent.add(btnOk);
	}

	/**
	 * Method to add a 'Yes' button.
	 */

	private static void addYesButton(JPanel parent) {
		btnYes = new JButton("Yes");
		btnYes.setMnemonic('Y');
		btnYes.setFont(CoeusFontFactory.getLabelFont());
		btnYes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnYesPressed();
			}
		});
		btnYes.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_Y) {
					btnYesPressed();
				}
			}
		});
		parent.add(btnYes);
	}

	/**
	 * Action to perform when Cancel button is pressed
	 */

	public static void btnCancelPressed() {
		// If doing an input dialog and cancel was pressed, set value to null
		if (txtInput != null)
			txtInput.setText(null);

		selection = JOptionPane.CANCEL_OPTION;
		propagating = false;
		dialog.dispose();
	}

	/**
	 * Action to perform when No button is pressed
	 */

	public static void btnNoPressed() {
		selection = JOptionPane.NO_OPTION;
		propagating = false;
		dialog.dispose();
	}

	/**
	 * Action taken when OK button is pressed
	 */

	private static void btnOkPressed() {
		selection = JOptionPane.OK_OPTION;
		propagating = false;
		dialog.dispose();
	}

	/**
	 * Action to perform when Yes button is pressed
	 */

	public static void btnYesPressed() {
		selection = JOptionPane.YES_OPTION;
		propagating = false;
		dialog.dispose();
	}

	/**
	 * Helper method for creating the appropriate buttons in the appropriate
	 * order within the dialog.
	 */

	private static JPanel createButtonPanel(int buttonOption) {
		JPanel panel = new JPanel();
		Component[] components = new Component[3];
		ScreenFocusTraversalPolicy traversal;
		switch (buttonOption) {
		case OPTION_OK_CANCEL:
			addOkButton(panel);
			addCancelButton(panel);
			components = new Component[2];
			components[0] = btnOk;
			components[1] = btnCancel;
			traversal = new ScreenFocusTraversalPolicy(components);
			panel.setFocusTraversalPolicy(traversal);
			panel.setFocusCycleRoot(true);
			buttonOptions = OPTION_OK_CANCEL;
			break;
		case OPTION_OK:
			addOkButton(panel);
			buttonOptions = OPTION_OK;
			components = new Component[1];
			components[0] = btnOk;
			traversal = new ScreenFocusTraversalPolicy(components);
			panel.setFocusTraversalPolicy(traversal);
			panel.setFocusCycleRoot(true);
			break;
		case OPTION_YES_NO:
			addYesButton(panel);
			addNoButton(panel);
			buttonOptions = OPTION_YES_NO;
			components = new Component[2];
			components[0] = btnYes;
			components[1] = btnNo;
			traversal = new ScreenFocusTraversalPolicy(components);
			panel.setFocusTraversalPolicy(traversal);
			panel.setFocusCycleRoot(true);
			break;
		case OPTION_YES_NO_CANCEL:
			addYesButton(panel);
			addNoButton(panel);
			addCancelButton(panel);
			buttonOptions = OPTION_YES_NO_CANCEL;
			components = new Component[3];
			components[0] = btnYes;
			components[1] = btnNo;
			components[2] = btnCancel;
			traversal = new ScreenFocusTraversalPolicy(components);
			panel.setFocusTraversalPolicy(traversal);
			panel.setFocusCycleRoot(true);
			break;
		}
		return panel;
	}

	/**
	 * Set focus on the requested button. This is called when the
	 * <code>WindowListener</code> for the <code>JDialog</code> executes the
	 * WindowOpened event. This is done because <code>JButton</code>s cannot
	 * request focus until the frame is visible.
	 */

	private static void focusButton() {
		switch (getFocusSelection()) {
		case DEFAULT_CANCEL:
			if (btnCancel != null)
				btnCancel.requestFocus();
			break;
		case DEFAULT_YES:
			if (btnYes != null)
				btnYes.requestFocus();
			break;
		case DEFAULT_NO:
			if (btnNo != null)
				btnNo.requestFocus();
			break;
		case DEFAULT_OK:
			if (btnOk != null)
				btnOk.requestFocus();
			break;
		}
	}

	/**
	 * Returns the preffered default focus selection when the window is opened
	 */

	private static int getFocusSelection() {
		return defaultSelection;
	}

	/**
	 * This method is used to check whether ESC key event is fired to close the
	 * CoeusOptionPane dialog. If ESC key event is fired to close the
	 * CoeusOptionPane dialog it will return true, else false.
	 *
	 * @return propagating boolean true if ESC key event is fired to close
	 *         CoeusOptionPane, else false.
	 */
	public static boolean isPropagating() {
		return propagating;
	}

	/**
	 * Holds the preferred focus selection while JDialog is built
	 */

	private static void setFocusSelection(int defaultSel) {
		defaultSelection = defaultSel;
	}

	/**
	 * This method is used to set whether the ESC key event to be propagated to
	 * its parent.
	 *
	 * @param prop
	 *            boolean value which specifies that the ESC key event is to be
	 *            propagated to its parent or not.
	 */
	public static void setPropagating(boolean prop) {
		propagating = prop;

	}

	/**
	 * Displays a dialog depending on CoeusClientException's id, messageType,
	 * and messageKey.
	 *
	 * @param coeusClientException
	 *            CoeusClientException.
	 */
	public synchronized static void showDialog(CoeusClientException coeusClientException) {
		int messageType;
		String id, messageKey, message;

		id = coeusClientException.getId();
		messageType = coeusClientException.getMessageType();
		messageKey = coeusClientException.getMessage();

		if (coeusMessageResources == null) {
			coeusMessageResources = CoeusMessageResources.getInstance();
		}
		message = coeusMessageResources.parseMessageKey(messageKey);

		// Change the message type to be displayed by CoeusOptionPane.
		switch (messageType) {
		case CoeusClientException.ERROR_MESSAGE:
			messageType = CoeusOptionPane.STYLE_ERROR;
			break;
		case CoeusClientException.INFORMATION_MESSAGE:
			messageType = CoeusOptionPane.STYLE_INFO;
			break;
		case CoeusClientException.WARNING_MESSAGE:
			messageType = CoeusOptionPane.STYLE_WARNING;
			break;
		}
		showDialog(CoeusGuiConstants.getMDIForm(), CoeusGuiConstants.MESSAGE_WINDOW_TITLE, message, messageType,
				CoeusOptionPane.OPTION_OK, CoeusOptionPane.DEFAULT_OK);
	}

	/**
	 * Create the info, question, error or warning dialog. The dialog style,
	 * button options and preferred focus button are all provided in the
	 * parameters
	 *
	 * @param parent
	 *            The parent component for this dialog.
	 * @param title
	 *            The title to display for this dialog
	 * @param message
	 *            The message to display in this dialog
	 * @param style
	 *            The dialog style (see constants STYLE_*) to show. Determines
	 *            the icon to display
	 * @param order
	 *            The order and selection of buttons (see constants OPTION_*) to
	 *            use for this
	 * @param defaultSelection
	 *            The button that should be defaulted when dialog is set visible
	 *            (see constants DEFAULT_*). If null or invalid the first button
	 *            will request focus.
	 */

	private static void showDialog(Component parent, String title, String message, int style, int option,
			int defaultSelection) {
		if (System.getProperty("java.version").startsWith("1.4")) {
			dialog = new JDialog(JOptionPane.getFrameForComponent(parent), title, true) {
				@Override
				protected JRootPane createRootPane() {
					ActionListener actionListener = new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent actionEvent) {
							dialog.dispose();
						}
					};
					JRootPane rootPane = new JRootPane();
					KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
					rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
					return rootPane;
				}
			};
			dialog.setResizable(false);
			// added to perform yes,no,cancel operations when menmonics pressed
			KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_N, 0);
			dialog.getRootPane().registerKeyboardAction(bNoAction, stroke,
					javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

			stroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, 0);
			dialog.getRootPane().registerKeyboardAction(bCancelAction, stroke,
					javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

			stroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0);
			dialog.getRootPane().registerKeyboardAction(bYesAction, stroke,
					javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

		} else {
			propagating = true;
			dialog = new JDialog(JOptionPane.getFrameForComponent(parent), title, true);
		}
		dialog.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dialog.dispose();
				} else if (ke.getKeyCode() == KeyEvent.VK_C)
					btnCancelPressed();
				else if (ke.getKeyCode() == KeyEvent.VK_N)
					btnNoPressed();
				else if (ke.getKeyCode() == KeyEvent.VK_Y)
					btnYesPressed();
				else if (ke.getKeyCode() == KeyEvent.VK_O)
					btnOkPressed();
			}
		});
		// Save the default focus selection for later
		setFocusSelection(defaultSelection);
		// When the window opens, set the default button
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dialog.dispose();
			}

			@Override
			public void windowOpened(WindowEvent e) {
				focusButton();
			}
		});

		JLabel jl = null;
		// Based on the style, create the correct icon
		switch (style) {
		case STYLE_ERROR:
			jl = new JLabel(UIManager.getIcon("OptionPane.errorIcon"));
			break;
		case STYLE_INFO:
			jl = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
			break;
		case STYLE_WARNING:
			jl = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
			break;
		case STYLE_QUESTION:
			jl = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
			break;
		default:
			jl = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
		}
		// Create a panel to hold the icon and the message
		JPanel jp = new JPanel(new BorderLayout(10, 10));
		jp.add(jl, BorderLayout.WEST);

		if (message == null) {
			message = "Unknown Error, Contact Coeus Support Team";
		}

		if (message.toLowerCase().startsWith("<html>")) {
			// jp.add(new JLabel(message));
			CoeusHTMLPane html = new CoeusHTMLPane(message);
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.getViewport().add(html, BorderLayout.CENTER);
			Dimension d = new Dimension(500, 250);
			scrollPane.setSize(d);
			scrollPane.setPreferredSize(d);
			scrollPane.setMaximumSize(d);
			scrollPane.setMinimumSize(d);
			jp.add(scrollPane);
		} else {
			JTextArea jta = new JTextArea(message);
			jta.setBackground(UIManager.getColor("OptionPane.background"));
			jta.setDisabledTextColor(jta.getForeground());
			jta.setFont(CoeusFontFactory.getNormalFont());
			if (message.length() > 110 && message.indexOf('\n') == -1) {
				jta.setColumns(75);
				jta.setLineWrap(true);
				jta.setWrapStyleWord(true);
			}
			jta.setEnabled(false);
			jp.setBorder(new EmptyBorder(20, 10, 0, 0));
			jp.add(jta, BorderLayout.CENTER);
		}
		dialog.getContentPane().setLayout(new BorderLayout(10, 10));
		dialog.getContentPane().add(jp, BorderLayout.CENTER);
		// Using the helper method, put the correct buttons onto the dialog
		dialog.getContentPane().add(createButtonPanel(option), BorderLayout.SOUTH);

		dialog.pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = dialog.getSize();
		dialog.setLocation(screenSize.width / 2 - (dlgSize.width / 2), screenSize.height / 2 - (dlgSize.height / 2));
		dialog.setVisible(true);
	}

	/**
	 * Show an error style dialog
	 *
	 * @param parent
	 *            The parent component for this dialog
	 * @param message
	 *            The message to display within the dialog
	 */

	public static void showErrorDialog(Component parent, String message) {
		showDialog(parent, CoeusGuiConstants.MESSAGE_WINDOW_TITLE, message, STYLE_ERROR, OPTION_OK, DEFAULT_OK);
	}

	/**
	 * Show an error style dialog
	 *
	 * @param message
	 *            The message to display within the dialog
	 */

	public static void showErrorDialog(String message) {
		showDialog(CoeusGuiConstants.getMDIForm(), CoeusGuiConstants.MESSAGE_WINDOW_TITLE, message, STYLE_ERROR,
				OPTION_OK, DEFAULT_OK);
	}

	/**
	 * Show an information style dialog
	 * 
	 * @param message
	 *            The message to display within the dialog
	 */
	public static void showInfoDialog(String message) {
		showDialog(CoeusGuiConstants.getMDIForm(), CoeusGuiConstants.MESSAGE_WINDOW_TITLE, message, STYLE_INFO,
				OPTION_OK, DEFAULT_OK);
	}

	/**
	 * Show an input dialog and gather input from a user defined component. The
	 * user defined component must be an extension of JTextComponent. This
	 * allows one to use a JTextArea for input or to create a document for a
	 * JTextField to control the formatting of the input information.
	 *
	 * @param parent
	 *            The component this modal dialog box is to be associated with
	 * @param title
	 *            The title for the dialog
	 * @param prompt
	 *            The prompt to display for input
	 * @param input
	 *            The <code>JTextComponent</code> to use for input. Can be any
	 *            components that extends JTextComponent (JTextField,
	 *            JTextArea).
	 * @param defValue
	 *            the default value, if any, for the text field (null is a valid
	 *            value)
	 *
	 * @return String containing the user input. Value will be null if canceled
	 *         or no entry supplied
	 */

	public static String showInputDialog(Component parent, String title, String prompt, JTextComponent input,
			String defValue) {
		dialog = new JDialog(JOptionPane.getFrameForComponent(parent), title, true);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		JPanel jp = new JPanel(new BorderLayout());
		if (prompt.toLowerCase().startsWith("<html>")) {
			// jp.add(new JLabel(prompt));
			jp.add(new CoeusHTMLPane(prompt));
		} else {
			JTextArea jta = new JTextArea(prompt);
			jta.setBackground(UIManager.getColor("OptionPane.background"));
			jta.setEnabled(false);
			jta.setDisabledTextColor(jta.getForeground());
			jta.setFont(UIManager.getFont("OptionPane.font"));
			jp.add(jta, BorderLayout.NORTH);
		}
		if (input != null)
			txtInput = input;
		else
			txtInput = new JTextField(30);

		txtInput.setText(defValue);
		txtInput.setSelectionStart(0);
		txtInput.setSelectionEnd(txtInput.getText().length());
		jp.add(txtInput, BorderLayout.CENTER);
		dialog.getContentPane().add(jp, BorderLayout.CENTER);
		dialog.getContentPane().add(createButtonPanel(OPTION_OK_CANCEL), BorderLayout.SOUTH);
		dialog.pack();
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
		if (txtInput.getText().equals(""))
			return null;
		return txtInput.getText();

	}

	/**
	 * Show an input dialog and return the user input
	 *
	 * @param parent
	 *            The component this modal dialog box is to be associated with
	 * @param title
	 *            The title for the dialog
	 * @param prompt
	 *            The prompt to display for input
	 * @param defValue
	 *            the default value, if any, for the text field (null is a valid
	 *            value)
	 *
	 * @return String containing the user input. Value will be null if canceled
	 */

	public static String showInputDialog(Component parent, String title, String prompt, String defValue) {
		return showInputDialog(parent, title, prompt, null, defValue);
	}

	/**
	 * Show an option dialog. The Object[] can be any array of objects. The
	 * toString() method for the object will be used to display an appropriate
	 * button for that option. Commonly a String[] is passed for this parameter,
	 * but any object can be used. For example:<br>
	 * 
	 * <pre>
	 * String[] sa = new String[] { "Yes", "No", "Maybe", "I Don't Know" };
	 * int selection = POptionPane.showOptionDialog(parent, "Selection Dialog", "Choose an Option", sa);
	 * </pre>
	 * 
	 * <br>
	 * The <code>int</code> returned from this will be the element within the
	 * object[] that was selected. If the dialog is closed with no option
	 * selection the return value will be -1.
	 *
	 * @param message
	 *            The message to present to the user for input
	 * @param options
	 *            An object array whose toString() method will be used to
	 *            present choices to the user
	 *
	 * @return int The element within Object[] options that was selected. -1 if
	 *         no selection made.
	 */

	public static int showOptionDialog(String message, Object[] options) {

		return JOptionPane.showOptionDialog(CoeusGuiConstants.getMDIForm(), message,
				CoeusGuiConstants.MESSAGE_WINDOW_TITLE, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
				UIManager.getIcon("OptionPane.questionIcon"), options, options[0]);
	}

	/**
	 * Show a question style dialog
	 *
	 * @param message
	 *            The message to display within the dialog
	 * @param buttonOptions
	 *            int value represent the button option value.
	 * @param defaultSelection
	 *            The order the buttons within the dialog should appear
	 *
	 * @return int The selection (see constants SELECTION_*
	 */
	public static int showQuestionDialog(String message, int buttonOptions, int defaultSelection) {
		if (buttonOptions == OPTION_OK_CANCEL || buttonOptions == OPTION_YES_NO_CANCEL)
			selection = JOptionPane.CANCEL_OPTION;
		else if (buttonOptions == OPTION_YES_NO)
			selection = JOptionPane.NO_OPTION;
		showDialog(CoeusGuiConstants.getMDIForm(), CoeusGuiConstants.MESSAGE_WINDOW_TITLE, message, STYLE_QUESTION,
				buttonOptions, defaultSelection);
		return selection;
	}

	/**
	 * Show a warning style dialog
	 * 
	 * @param message
	 *            The message to display within the dialog
	 */

	public static void showWarningDialog(Component parent, String message) {
		showDialog(parent, CoeusGuiConstants.MESSAGE_WINDOW_TITLE, message, STYLE_WARNING, OPTION_OK, DEFAULT_OK);
	}

	/**
	 * Show a warning style dialog
	 * 
	 * @param message
	 *            The message to display within the dialog
	 */

	public static void showWarningDialog(String message) {
		showDialog(CoeusGuiConstants.getMDIForm(), CoeusGuiConstants.MESSAGE_WINDOW_TITLE, message, STYLE_WARNING,
				OPTION_OK, DEFAULT_OK);
	}

}
