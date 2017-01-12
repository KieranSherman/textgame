package regex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import regex.exceptions.InvalidExpressionException;

/**
 * Class models a BNF-style language-creator where the user can create
 * expressions, and define each one to have both rules and elements.
 * 
 * @author kieransherman
 *
 */
public class RegexCreator {
	
	private static LinkedHashMap<Object, List<Object>> expressionRules;
	private static LinkedHashMap<Object, List<Object>> expressionElements;
	
	static {
		expressionRules = new LinkedHashMap<Object, List<Object>>();
		expressionElements = new LinkedHashMap<Object, List<Object>>();
	}
	
	// Prevent object instantiation.
	private RegexCreator() {}
	
	/**
	 * Add an expression to the language.
	 * 
	 * @param expression the expression to add.
	 * @throws InvalidExpressionException thrown if the expression already exists.
	 */
	public static void addExpression(Object expression) throws InvalidExpressionException {
		if(expressionRules.containsKey(expression))
			throw new InvalidExpressionException("The expression: \""+expression+"\" already exists.");
		
		expressionRules.put(expression, new ArrayList<Object>());
		expressionElements.put(expression, new ArrayList<Object>());
	}
	
	/**
	 * Defines an existing expression to contain an element.
	 * 
	 * @param expression the expression to define.
	 * @param element the element to add.
	 * @throws InvalidExpressionException thrown if the expression does not exist or
	 * if the expression already contains the element.
	 */
	public static void defineExpressionElement(Object expression, Object element) throws InvalidExpressionException {
		if(!expressionElements.containsKey(expression))
			throw new InvalidExpressionException("The expression: \""+expression+"\" does not exist.");
		else
		if(expressionElements.get(expression).contains(element))
			throw new InvalidExpressionException("The expression: \""+expression+"\" already contains the element: ["+element+"].");
		
		expressionElements.get(expression).add(element);
	}
	
	/**
	 * Defines an existing expression to contain multiple elements.
	 * 
	 * @param expression the expression to define.
	 * @param elements the elements to add.
	 * @throws InvalidExpressionException thrown if the expression does not exist 
	 * or if any of the elements are already contained within the expression.
	 */
	public static void defineExpressionElements(Object expression, Collection<Object> elements) throws InvalidExpressionException {
		for(Object obj : elements)
			defineExpressionElement(expression, obj);
	}
	
	/**
	 * Defines an expression to contain a rule (which is another expression).
	 * 
	 * @param expression the expression to define.
	 * @param rule the rule to add.
	 * @throws InvalidExpressionException thrown if the expression does not exist,
	 * the rule does not exist, or if the expression already contains the rule.
	 */
	public static void defineExpressionRule(Object expression, Object rule) throws InvalidExpressionException {
		if(!expressionRules.containsKey(expression))
			throw new InvalidExpressionException("The expression: \""+expression+"\" does not exist.");
		else
		if(!expressionRules.containsKey(rule))
			throw new InvalidExpressionException("The expression: \""+rule+"\" does not exist.");
		else
		if(expressionRules.get(expression).contains(rule))
			throw new InvalidExpressionException("The expression: \""+expression+"\" already contains the rule: <"+rule+">.");
		
		expressionRules.get(expression).add(rule);
	}
	
	/**
	 * Defines an expression to contain multiple rules (which are other expressions).
	 * 
	 * @param expression the expression to define.
	 * @param rules the rules to add.
	 * @throws InvalidExpressionException thrown if the expression does not exist,
	 * any of the rules do not exist, or if any of the rules are already contained within the expression. 
	 */
	public static void defineExpressionRules(Object expression, Collection<Object> rules) throws InvalidExpressionException {
		for(Object obj : rules)
			defineExpressionRule(expression, obj);
	}
	
	/**
	 * Removes an expression, its rules, and its elements.
	 * 
	 * @param expression the expression to remove.
	 * @throws InvalidExpressionException thrown if the expression does not exist.
	 */
	public static void removeExpression(Object expression) throws InvalidExpressionException {
		if(!expressionRules.containsKey(expression))
			throw new InvalidExpressionException("The expression: \""+expression+"\" does not exist.");

		expressionRules.remove(expression);
		expressionElements.remove(expression);
	}
	
	/**
	 * Removes an expression rule from an expression.
	 * 
	 * @param expression the expression to remove from.
	 * @param expressionRule the rule to remove.
	 * @throws InvalidExpressionException thrown if the expression does not exist,
	 * or if the expression does not contain the rule.
	 */
	public static void removeExpressionRuleFromExpression(Object expression, Object expressionRule) throws InvalidExpressionException {
		if(!expressionRules.containsKey(expression))
			throw new InvalidExpressionException("The expression: \""+expression+"\" does not exist.");
		
		List<Object> rules = expressionRules.get(expression);
			
		if(rules == null)
			throw new InvalidExpressionException("The expression: \""+expression+"\" does not exist.");
		else
		if(!rules.contains(expressionRule))
			throw new InvalidExpressionException("The expression: \""+expression+"\" does not contain the rule: \""+expressionRule+"\".");
			
		rules.remove(expressionRule);
	}
	
	/**
	 * Tests and parses input into expressions.
	 * 
	 * @param input the input to parse.
	 * @throws InvalidExpressionException thrown if the input does not follow
	 * the syntactical ruling defined by the expressions.
	 */
	public static void parseInput(String input) throws InvalidExpressionException {
		String [] data = input.replaceAll("[,.?!+-]", "").split("\\s+");
		
		LinkedHashMap<Object, Object> map = new LinkedHashMap<Object, Object>();
		
		for(String str : data)
			for(Object key : expressionRules.keySet())
				for(Object element : getExpressionElements(key))
					if(str.equalsIgnoreCase(element.toString()))
						map.put(str, key);
		
		isValid(input, map);
		System.out.println(input+": "+map);
	}
	
	/**
	 * Checks to make sure a given map of expressions is valid.
	 */
	private static void isValid(String input, LinkedHashMap<Object, Object> map) throws InvalidExpressionException {
		Collection<Object> keySet = map.values();
		Iterator<Object> iterator = keySet.iterator();
		List<Object> rules = null;
		Object lastExpression = null;
		
		while(iterator.hasNext()) {
			Object expression = iterator.next();
			
			if(rules != null && !rules.contains(expression))
				throw new InvalidExpressionException("The phrase: \""+input+"\" is not valid.\n"
						+ "The expression: <"+expression+"> cannot follow the expression: <"+lastExpression+">.");
			
			rules = getExpressionRules(expression);
			lastExpression = expression;
		}
	}
	
	/**
	 * Returns an expression's rules, null if the expression does not exist.
	 * 
	 * @param expression the expression to get the rules from.
	 * @return the expression's rules.
	 */
	public static List<Object> getExpressionRules(Object expression) {
		return expressionRules.get(expression);
	}
	
	/**
	 * Returns an expression's elements, null if the expression does not exist.
	 * 
	 * @param expression the expression to get the elements from.
	 * @return the expression's elements.
	 */
	public static List<Object> getExpressionElements(Object expression) {
		return expressionElements.get(expression);
	}
	
	/**
	 * Displays the expressions' rules and elements.
	 */
	public static void display() {
		for(Object key : expressionRules.keySet())
			printExpression(key);
			
		for(Map.Entry<Object, List<Object>> map : expressionElements.entrySet())
			System.out.format("%-15s %s \n", map.getKey(), map.getValue());
		
		System.out.println();
	}
	
	/**
	 * Neatly formats and prints an expression's rules.
	 */
	private static void printExpression(Object expression) {
		String str = "* <"+expression+"> --> ";
		List<Object> rules = getExpressionRules(expression);
		
		for(int i = 0; i < rules.size()-1; i++)
			str += "<"+rules.get(i)+"> | ";
		
		if(rules.size() > 0)
			str += "<"+rules.get(rules.size()-1)+">";
		else
			str = "* <"+expression+">";
		
		System.out.println(str+"\n");
	}
	
}
