package calculator;

import calculator.ui.CalculatorUI;
import calculator.controller.CalculatorController;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Δημιουργούμε το γραφικό περιβάλλον
            CalculatorUI ui = new CalculatorUI();

            // 2. Δημιουργούμε τον Controller και του περνάμε το UI για να ακούει τα κουμπιά
            CalculatorController controller = new CalculatorController(ui);

            // 3. Εμφανίζουμε την εφαρμογή
            ui.showUI();
            ui.setDisplayText("0");
        });
    }
}