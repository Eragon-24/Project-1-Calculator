package calculator.controller;

import calculator.ui.CalculatorUI;
import calculator.operations.BasicOperations;
import calculator.operations.AdvancedOperations;
import calculator.history.CalculationHistory;
import calculator.history.CalculationRecord;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CalculatorController {

    private final CalculatorUI ui;
    private final BasicOperations basicOps;
    private final AdvancedOperations advancedOps;
    private final CalculationHistory history;

    private Double firstNumber = null;
    private String pendingOperator = null;
    private boolean startNewNumber = true;

    public CalculatorController(CalculatorUI ui) {
        this.ui = ui;
        this.basicOps = new BasicOperations();
        this.advancedOps = new AdvancedOperations();
        this.history = new CalculationHistory();

        this.ui.addButtonListener(new ButtonClickListener());
    }

    private class ButtonClickListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            try {
                if (command.matches("[0-9]")) {
                    enterDigit(command);
                } else if (command.equals(".")) {
                    enterDecimalPoint();
                } else if (command.equals("C")) {
                    clear();
                } else if (command.matches("[+\\-*/]")) {
                    selectOperator(command);
                } else if (command.equals("=")) {
                    calculate();
                } else if (command.equals("√")) {
                    calculateUnary("√");
                } else if (command.equals("log")) {
                    calculateUnary("log");
                } else if (command.equals("ln")) {
                    calculateUnary("ln");
                } else if (command.equals("e^x")) {
                    calculateUnary("e^x");
                } else if (command.equals("x^y")) {
                    selectOperator("^");
                } else if (command.equals("%")) {
                    calculateUnary("%");
                } else if (command.equals("History")) {
                    showHistory();
                }
            } catch (Exception ex) {
                ui.setDisplayText("Σφάλμα");
                firstNumber = null;
                pendingOperator = null;
                startNewNumber = true;

                System.out.println("Σφάλμα στο κουμπί: " + command);
                ex.printStackTrace();
            }
        }
    }

    private void enterDigit(String digit) {
        String current = ui.getDisplayText();

        if (startNewNumber || current.equals("0")
                || current.equals("Σφάλμα")) {
            ui.setDisplayText(digit);
            startNewNumber = false;
        } else {
            ui.setDisplayText(current + digit);
        }
    }

    private void enterDecimalPoint() {
        String current = ui.getDisplayText();

        if (startNewNumber || current.equals("Σφάλμα")) {
            ui.setDisplayText("0.");
            startNewNumber = false;
        } else if (!current.contains(".")) {
            ui.setDisplayText(current + ".");
        }
    }

    private void clear() {
        ui.setDisplayText("0");
        firstNumber = null;
        pendingOperator = null;
        startNewNumber = true;
    }

    private void selectOperator(String operator) {
        String current = ui.getDisplayText();

        if (operator.equals("-") && startNewNumber) {
            return;
        }

        if (operator.equals("-") && firstNumber == null
                && !current.equals("0") && !current.equals("Σφάλμα")) {
            double number = Double.parseDouble(current);
            ui.setDisplayText(format(-number));
            startNewNumber = true;
            return;
        }

        firstNumber = Double.parseDouble(current);
        pendingOperator = operator;
        startNewNumber = true;
    }

    private void calculate() {
        if (firstNumber == null || pendingOperator == null) {
            return;
        }

        double secondNumber =
                Double.parseDouble(ui.getDisplayText());

        double result;

        switch (pendingOperator) {
            case "+":
                result = basicOps.add(firstNumber, secondNumber);
                break;
            case "-":
                result = basicOps.subtract(firstNumber, secondNumber);
                break;
            case "*":
                result = basicOps.multiply(firstNumber, secondNumber);
                break;
            case "/":
                result = basicOps.divide(firstNumber, secondNumber);
                break;
            case "^":
                result = advancedOps.power(firstNumber, secondNumber);
                break;
            default:
                throw new IllegalArgumentException(
                        "Άγνωστη πράξη: " + pendingOperator);
        }

        String expression = format(firstNumber) + " "
                + pendingOperator + " " + format(secondNumber);

        ui.setDisplayText(format(result));
        history.add(expression, result);

        firstNumber = null;
        pendingOperator = null;
        startNewNumber = true;
    }

    private void calculateUnary(String operation) {
        double number = Double.parseDouble(ui.getDisplayText());
        double result;

        switch (operation) {
            case "√":
                result = advancedOps.sqrt(number);
                break;
            case "log":
                result = advancedOps.log(number);
                break;
            case "ln":
                result = advancedOps.ln(number);
                break;
            case "e^x":
                result = advancedOps.expo(number);
                break;
            case "%":
                result = advancedOps.percent(number);
                break;
            default:
                throw new IllegalArgumentException(
                        "Άγνωστη πράξη: " + operation);
        }

        history.add(operation + "(" + format(number) + ")", result);
        ui.setDisplayText(format(result));
        startNewNumber = true;
    }

    private void showHistory() {
        List<String> historyStrings = new ArrayList<>();

        for (CalculationRecord record : history.getAll()) {
            historyStrings.add(record.toString());
        }

        ui.showHistoryDialog(historyStrings);
    }

    private String format(double number) {
        if (!Double.isFinite(number)) {
            return Double.toString(number);
        }

        DecimalFormat df = new DecimalFormat(
                "0.############",
                DecimalFormatSymbols.getInstance(Locale.US)
        );

        return df.format(number);
    }
}
