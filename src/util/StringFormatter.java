package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class allows a String to be built for formatting.
 * 
 * @author kieransherman
 *
 */
public class StringFormatter {
	
	private String format;
	private ArrayList<String> arguments;
	private int argumentsRequired;
	
	/**
	 * Creates a new StringFormatter.
	 */
	public StringFormatter() {
		format = "%s";
		arguments = new ArrayList<String>();
		argumentsRequired = 1;
	}
	
	/**
	 * Creates a new StringFormmater with a given format.
	 * 
	 * @param format the String format.
	 */
	public StringFormatter(String format) {
		this.format = format;
		arguments = new ArrayList<String>();
		argumentsRequired = getArgumentCount();
	}
	
	/**
	 * Sets the format to format the arguments with.
	 * 
	 * @param format the String format.
	 */
	public void setFormat(String format) {
		this.format = format;
		this.argumentsRequired = getArgumentCount();
	}
	
	/**
	 * Returns the number of String arguments that are in the format.
	 */
	private int getArgumentCount() {
		int count = 0;
		for(int i = 0; i < format.length()-1; i++) {
			if(format.charAt(i) == '%') {
				int nextIndex = format.substring(i+1).indexOf('%');
				
				if(nextIndex != -1 && format.substring(i, i+nextIndex).contains("s"))
					count++;
				else if (nextIndex == -1 && format.substring(i).contains("s"))
					count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Adds an argument to the end of the list.
	 * 
	 * @param argument the argument to add.
	 */
	public void addArgument(String argument) {
		arguments.add(argument);
	}
	
	/**
	 * Adds an argument with padding before and after to the end of the list.
	 * 
	 * @param argument the argument to add.
	 * @param padBefore the pad before argument.
	 * @param padAfter the pad after argument.
	 */
	public void addArgument(String argument, int padBefore, int padAfter) {
		for(int i = 0; i < padBefore; i++)
			arguments.add("");
		
		arguments.add(argument);
		
		for(int i = 0; i < padAfter; i++)
			arguments.add("");
	}
	
	/**
	 * Adds a List of arguments to the end of the list.
	 * 
	 * @param arguments a List of arguments.
	 */
	public void addArgumentList(List<String> arguments) {
		this.arguments.addAll(arguments);
	}
	
	/**
	 * Returns the formatted String.
	 * 
	 * @return the formatted String.
	 */
	public String getFormattedString() {
		if(arguments.size() % argumentsRequired != 0) {
			System.out.println(Arrays.toString(arguments.toArray()));
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < arguments.size()/argumentsRequired; i++)
			sb.append(String.format(format, arguments.subList(i*argumentsRequired, i*argumentsRequired+argumentsRequired).toArray()));
		
		return sb.toString();
	}
	
	/**
	 * Returns an {@link ArrayList} of type {@code String} containing lines of an original String 
	 * split up by word wrap by a maximum line character count.
	 * 
	 * @param str the str to word wrap.
	 * @param lineCharMax the maximum number of characters per line.
	 * @return the ArrayList containing the new lines.
	 */
	public static ArrayList<String> getWordWrap(String str, int lineCharMax) {
		ArrayList<String> strSplit = new ArrayList<String>();
		
		String[] newLineSplit = str.split("\n");
		
		for(int i = 0; i < newLineSplit.length; i++)
			strSplit.addAll(wrapText(newLineSplit[i], lineCharMax));
		
		return strSplit;
	}
	
	/**
	 * Returns an {@link ArrayList} of type {@code String} containing lines of an original String 
	 * split up by word wrap by a maximum line character count.
	 * 
	 */
	private static ArrayList<String> wrapText(String str, int lineCharMax) {
		String newLineSymbol = "--";
		int modifier = 0;
		int count = 0;
		ArrayList<String> strSplit = new ArrayList<String>();
		
		while(true) {
			String split, current;
			boolean splitWord = false;
			int indexLastWord = 0;
			int start = count*lineCharMax-modifier;
			
			if(start >= str.length())
				break;
			
			if(count >= 1) {
				current = newLineSymbol + str.substring(start);
				modifier += newLineSymbol.length();
			} else {
				current = str.substring(start);
			}
			
			if(current.contains("\t")) {
				int tabCount = current.split("\t").length - 1;
				tabCount = tabCount == -1 ? 0 : tabCount;
				
				current = current.replaceAll("\t", "        ");
				modifier += (8*tabCount)-1;
			}
			
			if(current.length() > lineCharMax && !current.equals(newLineSymbol)) {
				if(current.charAt(lineCharMax) == ' ')
					splitWord = false;
				else
					splitWord = true;
			}
			
			if(splitWord) {
				indexLastWord = current.substring(0, lineCharMax).length()-current.substring(0, lineCharMax).lastIndexOf(' ');
				
				if(indexLastWord >= lineCharMax-(count >= 1 ? newLineSymbol.length() : 1))
					indexLastWord = 0;
				
				modifier += indexLastWord;
			}
			
			split = current.substring(0, (current.length() > lineCharMax ? lineCharMax-indexLastWord : current.length()));
			
			if(!split.trim().equals(newLineSymbol))
				strSplit.add(split);
			
			count++;
		}
		
		return strSplit;
	}

}
