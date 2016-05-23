package main.ui.components.input;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Class allows a character count limitation to be put on a JTextField.
 * 
 * @author kieransherman
 *
 */
public class JTextFieldLimit extends PlainDocument {
	private static final long serialVersionUID = 1L;
	
	private int limit;

	/**
	 * Creates a new PlainDocument with a character limit.
	 * 
	 * @param limit the character limit.
	 */
	public JTextFieldLimit(int limit) {
		super();
		this.limit = limit;
	}

	@Override
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if(str == null)
			return;
		
		str = str.toUpperCase();

		if((getLength() + str.length()) <= limit)
			super.insertString(offset, str, attr);
	}
	
}