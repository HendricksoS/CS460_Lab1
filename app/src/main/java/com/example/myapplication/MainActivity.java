package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

/**
 *CS 460
 *Spencer Hendrickson 49720450
 *I pledge that this submission is solely my work, and that I have neither given, nor
 *received help from anyone.
 */
public class MainActivity extends AppCompatActivity {

    // UI Components
    private TextView mainDisplay;
    private TextView secondaryDisplay;

    // Calculator Variables
    private String expression = "";
    private final String DEVELOPER_NAME = "Spencer";
    private final String CWU_ID = "49720450";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeUI();

        // Set initial display values
        mainDisplay.setText(DEVELOPER_NAME);
        secondaryDisplay.setText("0");
    }

    private void initializeUI() {
        // Get references to display TextViews
        mainDisplay = findViewById(R.id.main_display);
        secondaryDisplay = findViewById(R.id.secondary_display);

        // Numeric buttons (0-9)
        setupNumericButton(R.id.button_0, "0");
        setupNumericButton(R.id.button_1, "1");
        setupNumericButton(R.id.button_2, "2");
        setupNumericButton(R.id.button_3, "3");
        setupNumericButton(R.id.button_4, "4");
        setupNumericButton(R.id.button_5, "5");
        setupNumericButton(R.id.button_6, "6");
        setupNumericButton(R.id.button_7, "7");
        setupNumericButton(R.id.button_8, "8");
        setupNumericButton(R.id.button_9, "9");

        // Operator buttons
        setupOperatorButton(R.id.button_add, "+");
        setupOperatorButton(R.id.button_subtract, "-");
        setupOperatorButton(R.id.button_multiply, "*");
        setupOperatorButton(R.id.button_divide, "/");

        // Parentheses buttons
        setupOperatorButton(R.id.button_open_paren, "(");
        setupOperatorButton(R.id.button_close_paren, ")");

        // Decimal button
        setupOperatorButton(R.id.button_decimal, ".");

        // Special function buttons
        setupClearButton();
        setupAllClearButton();
        setupEqualsButton();
    }

    /**
     * Sets up a numeric button with a click listener.
     * When clicked, appends the corresponding digit to the expression.
     *
     * @param buttonId the resource ID of the button
     * @param value the numeric value (0-9) to append to the expression
     */
    private void setupNumericButton(int buttonId, String value) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> appendToExpression(value));
    }

    /**
     * Sets up an operator button with a click listener.
     * When clicked, appends the operator or special character to the expression.
     *
     * @param buttonId the resource ID of the button
     * @param value the operator or special character (+, -, *, /, (, ), .)
     */
    private void setupOperatorButton(int buttonId, String value) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> appendToExpression(value));
    }

    /**
     * Sets up the Clear (C) button.
     * Removes the last character from the expression and updates the display.
     */
    private void setupClearButton() {
        Button clearButton = findViewById(R.id.button_C);
        clearButton.setOnClickListener(v -> {
            if (!expression.isEmpty()) {
                expression = expression.substring(0, expression.length() - 1);
            }
            updateDisplay();
        });
    }

    /**
     * Sets up the All Clear (AC) button.
     * Clears the entire expression and resets the display to initial values.
     */
    private void setupAllClearButton() {
        Button allClearButton = findViewById(R.id.button_AC);
        allClearButton.setOnClickListener(v -> {
            expression = "";
            mainDisplay.setText(DEVELOPER_NAME);
            secondaryDisplay.setText("0");
        });
    }

    /**
     * Sets up the Equals (=) button.
     * Evaluates the current expression and displays the result.
     * If the expression is invalid, displays an error message.
     */
    private void setupEqualsButton() {
        Button equalsButton = findViewById(R.id.button_equals);
        equalsButton.setOnClickListener(v -> {
            if (!expression.isEmpty()) {
                try {
                    // Evaluate the expression
                    double result = evaluateExpression(expression);

                    // Format result: remove unnecessary decimal places
                    String resultString = formatResult(result);

                    // Update displays
                    mainDisplay.setText(resultString);
                    secondaryDisplay.setText(expression);

                    // Store result and reset expression for next calculation
                    expression = resultString;
                } catch (Exception e) {
                    // Display error message if expression is invalid
                    mainDisplay.setText("Error");
                    secondaryDisplay.setText("Invalid Expression");
                }
            }
        });
    }

    /**
     * Appends a character (digit, operator, or decimal) to the expression string
     * and updates the display.
     *
     * @param value the character to append to the expression
     */
    private void appendToExpression(String value) {
        expression += value;
        updateDisplay();
    }

    /**
     * Updates both display TextViews to reflect the current expression state.
     * The main display shows the current expression, and the secondary display
     * shows the current expression or "0".
     */
    private void updateDisplay() {
        // Update main display with current expression
        if (expression.isEmpty()) {
            mainDisplay.setText(DEVELOPER_NAME);
        } else {
            mainDisplay.setText(expression);
        }

        // Update secondary display
        secondaryDisplay.setText(expression.isEmpty() ? "0" : expression);
    }

    /**
     * Evaluates a mathematical expression string and returns the result.
     * Supports basic arithmetic operations (+, -, *, /), parentheses,
     * and decimal numbers.
     *
     * @param expr the expression string to evaluate
     * @return the numerical result of the expression
     * @throws Exception if the expression is invalid or cannot be evaluated
     */
    private double evaluateExpression(String expr) throws Exception {
        return new ExpressionEvaluator(expr).evaluate();
    }

    /**
     * Formats the result of a calculation by removing trailing zeros
     * and unnecessary decimal points.
     *
     * @param result the numerical result to format
     * @return a formatted string representation of the result
     */
    private String formatResult(double result) {
        // Check if result is an integer
        if (result == (long) result) {
            return String.format(Locale.US, "%d", (long) result);
        } else {
            // Round to avoid floating point precision issues
            return String.format(Locale.US, "%.10f", result)
                    .replaceAll("0+$", "")
                    .replaceAll("\\.$", "");
        }
    }

    /**
     * ExpressionEvaluator is a helper class that parses and evaluates mathematical expressions.
     * It handles operator precedence correctly (multiplication and division before addition and subtraction)
     * and supports parentheses.
     */
    private static class ExpressionEvaluator {
        private String expression;
        private int pos = 0;

        /**
         * Constructs an ExpressionEvaluator with the given expression.
         *
         * @param expression the mathematical expression to evaluate
         */
        ExpressionEvaluator(String expression) {
            this.expression = expression.replaceAll("\\s+", "");
        }

        /**
         * Evaluates the expression and returns the numerical result.
         *
         * @return the result of evaluating the expression
         * @throws Exception if the expression is invalid
         */
        public double evaluate() throws Exception {
            double result = parseExpression();
            if (pos < expression.length()) {
                throw new Exception("Unexpected character at position " + pos);
            }
            return result;
        }

        /**
         * Parses and evaluates addition and subtraction (lowest precedence).
         *
         * @return the result of the expression at this level
         * @throws Exception if parsing fails
         */
        private double parseExpression() throws Exception {
            double result = parseTerm();

            while (pos < expression.length() && (peek() == '+' || peek() == '-')) {
                char operator = expression.charAt(pos++);
                double next = parseTerm();
                if (operator == '+') {
                    result += next;
                } else {
                    result -= next;
                }
            }

            return result;
        }

        /**
         * Parses and evaluates multiplication and division (higher precedence).
         *
         * @return the result at this precedence level
         * @throws Exception if parsing fails
         */
        private double parseTerm() throws Exception {
            double result = parseFactor();

            while (pos < expression.length() && (peek() == '*' || peek() == '/')) {
                char operator = expression.charAt(pos++);
                double next = parseFactor();
                if (operator == '*') {
                    result *= next;
                } else {
                    if (next == 0) {
                        throw new Exception("Division by zero");
                    }
                    result /= next;
                }
            }

            return result;
        }

        /**
         * Parses factors: numbers and parenthesized expressions (highest precedence).
         *
         * @return the numeric value of the factor
         * @throws Exception if parsing fails
         */
        private double parseFactor() throws Exception {
            // Handle parentheses
            if (pos < expression.length() && peek() == '(') {
                pos++; // Skip '('
                double result = parseExpression();
                if (pos >= expression.length() || expression.charAt(pos) != ')') {
                    throw new Exception("Missing closing parenthesis");
                }
                pos++; // Skip ')'
                return result;
            }

            // Handle unary minus
            if (pos < expression.length() && peek() == '-') {
                pos++;
                return -parseFactor();
            }

            // Handle unary plus
            if (pos < expression.length() && peek() == '+') {
                pos++;
                return parseFactor();
            }

            // Parse number (integer or decimal)
            return parseNumber();
        }

        /**
         * Parses a numeric value (integer or decimal number).
         *
         * @return the parsed numeric value
         * @throws Exception if no valid number is found
         */
        private double parseNumber() throws Exception {
            if (pos >= expression.length()) {
                throw new Exception("Unexpected end of expression");
            }

            StringBuilder number = new StringBuilder();

            // Parse digits before decimal point
            while (pos < expression.length() && Character.isDigit(expression.charAt(pos))) {
                number.append(expression.charAt(pos++));
            }

            // Parse decimal point and digits after
            if (pos < expression.length() && expression.charAt(pos) == '.') {
                number.append(expression.charAt(pos++));
                while (pos < expression.length() && Character.isDigit(expression.charAt(pos))) {
                    number.append(expression.charAt(pos++));
                }
            }

            if (number.length() == 0 || (number.length() == 1 && number.charAt(0) == '.')) {
                throw new Exception("Invalid number format");
            }

            try {
                return Double.parseDouble(number.toString());
            } catch (NumberFormatException e) {
                throw new Exception("Invalid number: " + number);
            }
        }

        /**
         * Peeks at the current character without consuming it.
         *
         * @return the current character at the parser position
         */
        private char peek() {
            return expression.charAt(pos);
        }
    }
}
