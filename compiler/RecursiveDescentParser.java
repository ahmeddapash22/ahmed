package javaapplication134;

import java.util.Scanner;

public class RecursiveDescentParser {
    private String inputString;
    private int index;
    private Character currentToken;

    public RecursiveDescentParser(String inputString) {
        this.inputString = inputString;
        this.index = 0;
        this.currentToken = null;
    }

    private void nextToken() {
        while (index < inputString.length() && Character.isWhitespace(inputString.charAt(index))) {
            index++;
        }

        if (index < inputString.length()) {
            currentToken = inputString.charAt(index);
            index++;
        } else {
            currentToken = null;
        }
    }

    private void match(char expectedToken) throws SyntaxError {
        if (currentToken != null && currentToken == expectedToken) {
            nextToken();
        } else {
            throw new SyntaxError(String.format("Expected %c, but got %c", expectedToken, currentToken));
        }
    }

    private void parseExpression() throws SyntaxError {
        parseTerm();
        while (currentToken != null && (currentToken == '+' || currentToken == '-')) {
            char op = currentToken;
            nextToken();
            parseTerm();
            System.out.println("  " + op);
        }
    }

    private void parseTerm() throws SyntaxError {
        parseFactor();
        while (currentToken != null && (currentToken == '*' || currentToken == '/')) {
            char op = currentToken;
            nextToken();
            parseFactor();
            System.out.println("  " + op);
        }
    }

    private void parseFactor() throws SyntaxError {
        if (Character.isDigit(currentToken)) {
            System.out.println("  " + currentToken);
            nextToken();
        } else if (currentToken != null && currentToken == '(') {
            nextToken();
            parseExpression();
            match(')');
        } else {
            throw new SyntaxError("Unexpected token: " + currentToken);
        }
    }

    public void parse() throws SyntaxError {
        nextToken();
        parseExpression();
        if (currentToken != null && !Character.isWhitespace(currentToken)) {
            throw new SyntaxError("Unexpected tokens after parsing");
        } else {
            System.out.println("Syntax is correct");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a Mathematical EX: ");
        String inputExpr = scanner.nextLine();

        RecursiveDescentParser parser = new RecursiveDescentParser(inputExpr);
        try {
            parser.parse();
        } catch (SyntaxError e) {
            System.out.println("SyntaxError: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}

class SyntaxError extends Exception {
    public SyntaxError(String message) {
        super(message);
    }
}
