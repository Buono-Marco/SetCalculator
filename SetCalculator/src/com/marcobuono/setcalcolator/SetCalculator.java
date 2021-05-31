package com.marcobuono.setcalcolator;
import java.util.TreeSet;

/**
 * This program is a "set calculator" for simple operations on sets of non-negative integers.
 * A set of positive integers will be represented as a list of integers, separated by commas
 * and, optionally, spaces and enclosed in square brackets. For example: [1,2,3] or
 * [17, 42, 9, 53,108]. The characters +, *, and - will be used for the union, intersection,
 * and difference operations. The user of the program will type in lines of input containing
 * two sets, separated by an operator. The program performs the operation and print
 * the resulting set.
 *  
 * @author marcobuono
 *
 */
public class SetCalculator {
	static boolean isSquareBracketClosed = false;
	
	public static void main(String[] args) {
		
		while (true) {
			System.out.println("***************************************************************");
			System.out.println("* Enter two sets of positive numbers, each enclosed in square *");
            System.out.println("* brackets and with values ​​separated by commas. Each value    *");
            System.out.println("* different from a digit will be ignored, and a negative      *");
            System.out.println("* number will be treated as a positive number.                *");
            System.out.println("* To end press return.                                        *");
            System.out.println("***************************************************************");
            System.out.print("\nEnter the two sets:  ");
            TextIO.skipBlanks();
            if ( TextIO.peek() == '\n' )
                break;
            try {
            	TreeSet<Integer> resultingSet = setCalculator(); // The resulting set
                TextIO.skipBlanks();
                if ( TextIO.peek() != '\n' ) // Raise an exception if there are data after the second set.
                	throw new ParseError("Extra data after end of two sets.");
                TextIO.getln();
                if(resultingSet.size() > 0) {
                	System.out.println("\nResult set is: " + resultingSet);
                } else {
                	System.out.println("\nResult is an empty set.");
                }
            }
            catch (ParseError e) {
                System.out.println("\n*** Error in input:    " + e.getMessage());
                System.out.println("*** Discarding input:  " + TextIO.getln() + "\n");
            }
        }

        System.out.println("\n\nDone.");
	}
	
	/**
     * Take the two sets, the operator, and performs the operation.
     * 
     * @return the resulting set
     * @throws ParseError if the input contains a syntax error
     */
	private static TreeSet<Integer> setCalculator() throws ParseError {
        TextIO.skipBlanks();
        if(TextIO.peek() == '[') {
        	TextIO.getAnyChar();  // Read the "["
            TreeSet<Integer> setA =  getSet(new TreeSet<Integer>()); // Populate the first set
            char op = getOperator(); // Read the operator.
            TreeSet<Integer> setB =  getSet(new TreeSet<Integer>()); // Populate the second set
            TextIO.skipBlanks();
            switch (op) {   //  Apply the operator and return the result. 
            case '+':
            	if(setA.addAll(setB)) {
            		return setA;
            	} else {
            		throw new ParseError("Encountered unexpected error");
            	}
            case '-':
            	if(setA.removeAll(setB)) {
            		return setA;
            	} else {
            		throw new ParseError("Encountered unexpected error");
            	}
            case '*':
            	if(setA.retainAll(setB)) {
            		return setA;
            	} else {
            		throw new ParseError("Encountered unexpected error");
            	}
            default:   return new TreeSet<Integer>();  // Can't occur since op is one of the above.
            										   // (But Java syntax requires a return value.)
            }
        }
        else { // Raise an exception if the first value is not a [
            throw new ParseError("Encountered unexpected initial character, \"" + 
                    TextIO.peek() + "\" in input. Was expected \"[\"");
        }
    } // end setCalculator()
	
	/**
	 * If the next character in input is a digit,
	 * read and add it to the set. When read the
	 * ']' return the set. 
	 * 
	 * @param set to be populate
	 * @return the populated set
	 * @throws If it wraps without the right bracket, throw a ParseError
	 */
	static TreeSet<Integer> getSet(TreeSet<Integer> set) throws ParseError {
        TextIO.skipBlanks();
        if(TextIO.peek() != ']') { // As long as it is different from ']'
        	if(Character.isDigit(TextIO.peek())) { // If it is a digit add to the set
        		set.add(TextIO.getInt());
        	} else if (TextIO.peek() == '\n') { // If it is a new line raise an exception
                throw new ParseError("Missing ']' at end of set.");
    		} else { // In the other cases go ahead
        		TextIO.getAnyChar();  // Read the ","
        	}
        	return getSet(set);
        } else { // If it is the closing bracket, finish reading the set
        	TextIO.getAnyChar();  // Read the "]"
            return set; // Return the set
        }
    } // end getSet()
	
	/**
     * If the next character in input is one of the legal operators,
     * read it and return it.  Otherwise, throw a ParseError.
     */
    static char getOperator() throws ParseError {
        TextIO.skipBlanks();
        char op = TextIO.peek(); 
        if (op == '+' || op == '-' || op == '*' ) {
            TextIO.getAnyChar();
            return op;
        }
        else if (op == '\n')
            throw new ParseError("Missing operator at end of line.");
        else
            throw new ParseError("Missing operator.  Found \"" +
                    op + "\" instead of +, -, *.");
    } // end getOperator()
	
	/**
     * An object of type ParseError represents a syntax error found in 
     * the user's input.
     */
    private static class ParseError extends Exception {
        ParseError(String message) {
            super(message);
        }
    } // end nested class ParseError

}
