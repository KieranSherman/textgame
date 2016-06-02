package regex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import regex.exceptions.InvalidExpressionException;

public class RegexCreator {
	
	private static HashMap<Object, List<Object>> expressionRules;
	private static HashMap<Object, List<Object>> expressionElements;
	
	static {
		expressionRules = new LinkedHashMap<Object, List<Object>>();
		expressionElements = new LinkedHashMap<Object, List<Object>>();
	}
	
	private RegexCreator() {}
	
	public static void addExpression(Object expression) throws InvalidExpressionException {
		if(expressionRules.containsKey(expression))
			throw new InvalidExpressionException("The expression: \""+expression+"\" already exists.");
		
		expressionRules.put(expression, new ArrayList<Object>());
		expressionElements.put(expression, new ArrayList<Object>());
	}
	
	public static void defineExpressionElement(Object expression, Object element) throws InvalidExpressionException {
		if(!expressionElements.containsKey(expression))
			throw new InvalidExpressionException("The expression: \""+expression+"\" does not exist.");
		
		expressionElements.get(expression).add(element);
	}
	
	public static void defineExpressionRule(Object expression, Object rule) throws InvalidExpressionException {
		if(!expressionRules.containsKey(expression))
			throw new InvalidExpressionException("The expression: \""+expression+"\" does not exist.");
		else
		if(!expressionRules.containsKey(rule))
			throw new InvalidExpressionException("The expression: \""+rule+"\" does not exist.");
		
		expressionRules.get(expression).add(rule);
	}
	
	public static void removeExpression(Object expression) throws InvalidExpressionException {
		if(!expressionRules.containsKey(expression))
			throw new InvalidExpressionException("The expression: \""+expression+"\" does not exist.");

		expressionRules.remove(expression);
		expressionElements.remove(expression);
	}
	
	public static void removeExpressionRuleFromExpression(Object expression, Object expressionRule) throws InvalidExpressionException {
		List<Object> rules = expressionRules.get(expression);
			
		if(rules == null)
			throw new InvalidExpressionException("The expression: \""+expression+"\" does not exist.");
		else
		if(!rules.contains(expressionRule))
			throw new InvalidExpressionException("The expression: \""+expression+"\" does not contain the rule: \""+expressionRule+"\".");
			
		rules.remove(expressionRule);
	}
	
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
	
	public static List<Object> getExpressionRules(Object expression) {
		return expressionRules.get(expression);
	}
	
	public static List<Object> getExpressionElements(Object expression) {
		return expressionElements.get(expression);
	}
	
	public static void display() {
		for(Object key : expressionRules.keySet())
			printExpression(key);
			
		for(Map.Entry<Object, List<Object>> map : expressionElements.entrySet())
			System.out.format("%-15s %s \n", map.getKey(), map.getValue());
		
		System.out.println();
	}
	
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
