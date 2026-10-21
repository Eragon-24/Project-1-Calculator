package calculator.operations;

public class BasicOperations {

    // Πρόσθεση
    public double add(double a, double b) {
        return a + b;
    }

    // Αφαίρεση
    public double subtract(double a, double b) {
        return a - b;
    }

    // Πολλαπλασιασμός
    public double multiply(double a, double b) {
        return a * b;
    }

    // Διαίρεση
    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Δεν επιτρέπεται η διαίρεση με το μηδέν.");
        }
        return a / b;
    }
}
