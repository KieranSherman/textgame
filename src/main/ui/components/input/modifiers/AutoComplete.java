package main.ui.components.input.modifiers;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import util.Resources;

/**
 * Class extends {@link DocumentListener} to modify a document listener.
 * 
 * @author kieransherman
 *
 */
public class AutoComplete implements DocumentListener {
	
	private JTextField textField;
	private List<String> keywords;
	private Mode mode = Mode.INSERT;
	
	private static enum Mode {
		INSERT,
		COMPLETION
	};
	
	/**
	 * Creates a new {@link DocumentListener} and overrides its {@code insertUpdate(DocumentEvent e)} 
	 * method to autcomplete words.
	 * 
	 * @param textField the text field to autocomplete.
	 * @param keywords the keywords to autocomplete.
	 */
	public AutoComplete(JTextField textField) {
		this.textField = textField;
		this.keywords = Resources.USER_COMMANDLIST_ENTRIES;
		Collections.sort(keywords);
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {}
	
	@Override
	public void removeUpdate(DocumentEvent e) {}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		if(e.getLength() != 1)
			return;
		
		int pos = e.getOffset();
		String content = null;
		try {
			content = textField.getText(0, pos+1);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
		
		int w;
		for(w = pos; w >= 0; w--)
			if(Character.isWhitespace(content.charAt(w)))
				break;
		
		if(pos-w < 1)
			return;
		
		String prefix = content.substring(w+1).toLowerCase();
		int n = Collections.binarySearch(keywords, prefix);
		if(n < 0 && -n <= keywords.size()) {
			String match = keywords.get(-n-1);
			if(match.startsWith(prefix)) {
				String completion = match.substring(pos-w);
				SwingUtilities.invokeLater(new CompletionTask(completion, pos+1));
			}
		} else {
			mode = Mode.INSERT;
		}
	}

	public void setKeywordList(List<String> keywords) {
		this.keywords = keywords;
		Collections.sort(keywords);
	}
	
	public boolean isActive() {
		return mode == Mode.COMPLETION;
	}
	
	public class CommitAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(mode == Mode.COMPLETION) {
				int pos = textField.getSelectionEnd();
				StringBuffer sb = new StringBuffer(textField.getText());
				sb.insert(pos, " ");
				textField.setText(sb.toString());
				textField.setCaretPosition(pos+1);
				mode = Mode.INSERT;
			} else {
				textField.replaceSelection("\t");
			}
		}
	}
	
	private class CompletionTask implements Runnable {
		private String completion;
		private int position;
		
		private CompletionTask(String completion, int position) {
			this.completion = completion;
			this.position = position;
		}
		
		@Override
		public void run() {
			StringBuffer sb = new StringBuffer(textField.getText());
			sb.insert(position, completion);
			textField.setText(sb.toString());
			textField.setCaretPosition(position+completion.length());
			textField.moveCaretPosition(position);
			mode = Mode.COMPLETION;
		}
	}

}
