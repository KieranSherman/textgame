package regex;

import regex.exceptions.InvalidExpressionException;

public class RegexTester {
	
	public static void main(String [] args) {
		try {
			// An ACTION is an expression
			RegexCreator.addExpression("ACTION");
			// A PLACE is an expression
			RegexCreator.addExpression("PLACE");
			// NULL represents nothing
			RegexCreator.addExpression("NULL");
			
			// "move" is an ACTION
			RegexCreator.defineExpressionElement("ACTION", "move");
			// "run" is an ACTION
			RegexCreator.defineExpressionElement("ACTION", "run");
			// "hide" is an ACTION
			RegexCreator.defineExpressionElement("ACTION", "hide");
			
			// "room" is a PLACE
			RegexCreator.defineExpressionElement("PLACE", "room");
			// "house" is a PLACE
			RegexCreator.defineExpressionElement("PLACE", "house");
			// "cave" is a PLACE
			RegexCreator.defineExpressionElement("PLACE", "cave");
			
			// an ACTION can be proceeded by a PLACE
			RegexCreator.defineExpressionRule("ACTION", "PLACE");
			// an ACTION can be proceeded by nothing
			RegexCreator.defineExpressionRule("ACTION", "NULL");
			// a PLACE can be proceeded by nothing
			RegexCreator.defineExpressionRule("PLACE", "NULL");

			RegexCreator.display();

			RegexCreator.parseInput("Move to the room please");
		} catch (InvalidExpressionException e) {
			e.printStackTrace();
		}
		
		
	}

}
