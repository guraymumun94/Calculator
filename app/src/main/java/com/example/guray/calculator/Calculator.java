package com.example.guray.calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * http://www.technical-recipes.com/2011/a-mathematical-expression-parser-in-java-and-cpp/
 * Generated by guray on 3/9/16.
 */
public class Calculator {

    private String expression;
    private static final int LEFT_ASSOC = 0;
    private static final int RIGHT_ASSOC = 1;
    private static final Map<String, int[]> OPERATORS = new HashMap<>();
    static {
        OPERATORS.put("+", new int[]{0, LEFT_ASSOC});
        OPERATORS.put("-", new int[]{0, LEFT_ASSOC});
        OPERATORS.put("*", new int[]{5, LEFT_ASSOC});
        OPERATORS.put("/", new int[]{5, LEFT_ASSOC});
    }

    public Calculator(String expression) {
        this.expression = expression;
    }

    public double calculate() {
        return calculateRPN(getRPN(getElements(this.expression)));
    }

    public double calculate(String expression) {
        return calculateRPN(getRPN(getElements(expression)));
    }

    private static String modificatedExpression(String expression) {
        expression = expression.replace("(", "( ");
        expression = expression.replace(")", " )");

        return expression;
    }

    private static ArrayList<String> getElements(String expression) {
        return new ArrayList<String>(Arrays.asList(modificatedExpression(expression).split("[ ]")));
    }

    //Calculating expressions in Reverse Polish Notation
    private static double calculateRPN(ArrayList<String> RPN) {
        Stack<String> stack = new Stack<String>();

        for (String element : RPN) {
            if(!isOperator(element)) {
                stack.push(element);
            }

            else {
                double number2 = Double.parseDouble(stack.pop());
                double number1 = Double.parseDouble(stack.pop());

                double result = element.equals("+") == true ? (number1 + number2) :
                        element.equals("-") == true ? (number1 - number2) :
                                element.equals("*") == true ? (number1 * number2) : (number1 / number2);

                stack.push(String.valueOf(result));
            }
        }
        return Double.parseDouble(stack.pop());
    }

    //Shunting yard algorithm
    private static ArrayList<String> getRPN(ArrayList<String> elements) {
        ArrayList<String> result = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();

        for (String element : elements) {
            if(isOperator(element)) {

                while(!stack.isEmpty() && isOperator(stack.peek())) {
                    if(isAssociative(element, LEFT_ASSOC) && comparePrecedence(element, stack.peek()) <= 0
                            || isAssociative(element, RIGHT_ASSOC) && comparePrecedence(element, stack.peek()) < 0) {
                        result.add(stack.pop());
                        continue;
                    }
                    break;
                }
                stack.push(element);
            }

            else if(element.equals("("))
                stack.push(element);

            else if(element.equals(")")) {

                while(!stack.isEmpty() && !stack.peek().equals("(")) {
                    result.add(stack.pop());
                }
                if(!stack.isEmpty())
                    stack.pop();
            }

            else
                result.add(element);
        }

        while(!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    private static boolean isOperator(String element) {
        if(element.equals("+") || element.equals("-") || element.equals("*") || element.equals("/"))
            return true;

        return false;
    }

    private static boolean isAssociative(String operator, int associativity) {
        if(OPERATORS.get(operator)[1] == associativity)
            return true;

        return false;
    }

    private static int comparePrecedence(String operator1, String operator2) {
        return OPERATORS.get(operator1)[0] - OPERATORS.get(operator2)[0];
    }
}
