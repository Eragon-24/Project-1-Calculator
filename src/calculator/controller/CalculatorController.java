package calculator.controller;

import calculator.ui.CalculatorUI;
import calculator.operations.BasicOperations;
import calculator.history.CalculationHistory;
import calculator.history.CalculationRecord;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CalculatorController {
    private CalculatorUI ui;
    private BasicOperations basicOps;
    private CalculationHistory history;

    public CalculatorController(CalculatorUI ui) {
        this.ui = ui;
        this.basicOps = new BasicOperations();
        this.history = new CalculationHistory();
        this.ui.addButtonListener(new ButtonClickListener());
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            String currentText = ui.getDisplayText();

            try {
                // 1. Αν πατήθηκε αριθμός ή τελεία
                if (command.matches("[0-9]") || command.equals(".")) {
                    // Αν η οθόνη γράφει 0 ή έχει βγάλει σφάλμα, σβήστο και γράψε το νέο νούμερο
                    if (currentText.equals("0") || currentText.equals("Σφάλμα")) {
                        ui.setDisplayText(command);
                    } else {
                        // Αλλιώς, πρόσθεσέ το δίπλα σε αυτό που υπάρχει ήδη
                        ui.setDisplayText(currentText + command);
                    }
                }
                // 2. Αν πατήθηκε το C (Καθαρισμός)
                else if (command.equals("C")) {
                    ui.setDisplayText("0");
                }
                // 3. Αν πατήθηκε πράξη (+, -, *, /)
                else if (command.matches("[+\\-*/]")) {
                    // Βάζει την πράξη μόνο αν δεν υπάρχει ήδη κάποια άλλη πράξη στην οθόνη
                    if (!currentText.contains(" + ") && !currentText.contains(" - ") &&
                            !currentText.contains(" * ") && !currentText.contains(" / ")) {

                        // Βάζει κενά δεξιά-αριστερά από την πράξη για να φαίνεται ωραία (π.χ. "5 - ")
                        ui.setDisplayText(currentText + " " + command + " ");
                    }
                }
                // 4. Αν πατήθηκε το ίσον (=)
                else if (command.equals("=")) {
                    // Χωρίζει το κείμενο της οθόνης όπου υπάρχουν κενά (πχ το "5.0 - 3.0" γίνεται 3 κομμάτια)
                    String[] parts = currentText.split(" ");

                    if (parts.length == 3) {
                        double num1 = Double.parseDouble(parts[0]);
                        String operator = parts[1];
                        double num2 = Double.parseDouble(parts[2]);
                        double result = 0;

                        if (operator.equals("+")) result = basicOps.add(num1, num2);
                        else if (operator.equals("-")) result = basicOps.subtract(num1, num2);
                        else if (operator.equals("*")) result = basicOps.multiply(num1, num2);
                        else if (operator.equals("/")) result = basicOps.divide(num1, num2);

                        // Εμφανίζει ΜΟΝΟ το αποτέλεσμα μετά το =
                        ui.setDisplayText(String.valueOf(result));

                        // Αποθήκευση στο ιστορικό της ομάδας
                        history.add(currentText, result);
                    }
                }
                // 5. Αν πατήθηκε το History
                else if (command.equals("History")) {
                    List<String> historyStrings = new ArrayList<>();

                    // Παίρνει όλα τα records από την κλάση CalculationHistory της ομάδας σου
                    for (CalculationRecord record : history.getAll()) {
                        historyStrings.add(record.toString());
                    }

                    // Καλεί το παραθυράκι popup που φτιάξαμε νωρίτερα στο CalculatorUI
                    ui.showHistoryDialog(historyStrings);
                }

            } catch (Exception ex) {
                ui.setDisplayText("Σφάλμα");
                // Αυτό σε βοηθάει να δεις στην κονσόλα σου αν κάτι πήγε στραβά
                System.out.println("Βρέθηκε σφάλμα στο κουμπί: " + command);
                ex.printStackTrace();
            }
        }
    }
}