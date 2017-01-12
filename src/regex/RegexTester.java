package regex;

import regex.exceptions.InvalidExpressionException;

public class RegexTester {
	
	public static void main(String [] args) {
		try {
			// An ACTION is an expression
			RegexCreator.addExpression("ACTION");
			// A PLACE is an expression
			RegexCreator.addExpression("PLACE");
			// A MODIFIER is an expression
			RegexCreator.addExpression("MODIFIER");
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
			
			// "quickly" is a MODIFIER
			RegexCreator.defineExpressionElement("MODIFIER", "quickly");
			// "quiet" is A MODIFIER
			RegexCreator.defineExpressionElement("MODIFIER", "quiet");
			
			// an ACTION can be proceeded by a PLACE
			RegexCreator.defineExpressionRule("ACTION", "PLACE");
			// an ACTION can be proceeded by a MODIFIER
			RegexCreator.defineExpressionRule("ACTION", "MODIFIER");
			// an ACTION can be proceeded by nothing
			RegexCreator.defineExpressionRule("ACTION", "NULL");
			
			//A MODIFIER can be proceeded by an ACTION
			RegexCreator.defineExpressionRule("MODIFIER", "ACTION");
			//A MODIFIER can be proceeded by a PLACE
			RegexCreator.defineExpressionRule("MODIFIER", "PLACE");
			//A MODIFIER can be proceeded by another MODIFIER
			RegexCreator.defineExpressionRule("MODIFIER", "MODIFIER");
			//A MODIFIER can be proceeded by nothing
			RegexCreator.defineExpressionRule("MODIFIER", "NULL");

			// a PLACE can be proceeded by a MODIFIER
			RegexCreator.defineExpressionRule("PLACE", "MODIFIER");
			// a PLACE can be proceeded by nothing
			RegexCreator.defineExpressionRule("PLACE", "NULL");

			// Display the syntax
			RegexCreator.display();

			// Parse a test input
			RegexCreator.parseInput("Quickly move to the quiet room");
		} catch (InvalidExpressionException e) {
			e.printStackTrace();
		}
	}

}
