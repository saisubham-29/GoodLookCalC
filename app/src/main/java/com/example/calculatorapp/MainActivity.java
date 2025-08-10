package com.example.calculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private TextView resultText;

    private String currentInput = "";
    private String operation = "";
    private double firstNumber = 0;
    private boolean operationSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.editTextMultiLine);
        resultText = findViewById(R.id.resultText);
        resultText.setVisibility(View.GONE); // Hide initially

        // Numbers
        setNumberClick(R.id.zero, "0");
        setNumberClick(R.id.one, "1");
        setNumberClick(R.id.two, "2");
        setNumberClick(R.id.three, "3");
        setNumberClick(R.id.four, "4");
        setNumberClick(R.id.five, "5");
        setNumberClick(R.id.six, "6");
        setNumberClick(R.id.seven, "7");
        setNumberClick(R.id.eight, "8");
        setNumberClick(R.id.nine, "9");

        // Dot
        findViewById(R.id.dot).setOnClickListener(v -> appendDot());

        // Operators
        setOperatorClick(R.id.plus, "+");
        setOperatorClick(R.id.minus, "-");
        setOperatorClick(R.id.multiply, "×");
        setOperatorClick(R.id.divide, "/");
        setOperatorClick(R.id.modulo, "%");

        // Actions
        findViewById(R.id.AC).setOnClickListener(v -> clearAll());
        findViewById(R.id.equal).setOnClickListener(v -> evaluateAndSetFinalResult());
        findViewById(R.id.plusminus).setOnClickListener(v -> toggleSign());
    }

    private void setNumberClick(int id, String value) {
        findViewById(id).setOnClickListener(v -> {
            currentInput += value;
            inputText.setText(currentInput);
            if (operationSet) tryAutoCalculate();
        });
    }

    private void appendDot() {
        String[] parts = currentInput.split("[+\\-×/% ]+");
        String lastPart = parts.length > 0 ? parts[parts.length - 1] : "";
        if (!lastPart.contains(".")) {
            currentInput += ".";
            inputText.setText(currentInput);
        }
    }

    private void setOperatorClick(int id, String op) {
        findViewById(id).setOnClickListener(v -> {
            if (!currentInput.isEmpty() && !operationSet) {
                try {
                    firstNumber = Double.parseDouble(currentInput);
                    operation = op;
                    currentInput += " " + op + " ";
                    inputText.setText(currentInput);
                    operationSet = true;
                    resultText.setVisibility(View.GONE);
                } catch (NumberFormatException ignored) {}
            }
        });
    }

    private void tryAutoCalculate() {
        if (operationSet && currentInput.contains(" " + operation + " ")) {
            String[] parts = currentInput.split(" \\" + operation + " ");
            if (parts.length == 2 && !parts[1].isEmpty()) {
                try {
                    double secondNumber = Double.parseDouble(parts[1]);
                    double result = calculate(firstNumber, secondNumber, operation);
                    resultText.setText(format(result));
                    resultText.setVisibility(View.VISIBLE);
                } catch (NumberFormatException ignored) {
                    resultText.setVisibility(View.GONE);
                }
            }
        }
    }

    private void evaluateAndSetFinalResult() {
        if (operationSet && currentInput.contains(" " + operation + " ")) {
            String[] parts = currentInput.split(" \\" + operation + " ");
            if (parts.length == 2) {
                try {
                    double secondNumber = Double.parseDouble(parts[1]);
                    double result = calculate(firstNumber, secondNumber, operation);
                    String formatted = format(result);
                    inputText.setText(formatted);
                    currentInput = formatted;
                    operation = "";
                    operationSet = false;
                    resultText.setVisibility(View.GONE);
                } catch (NumberFormatException e) {
                    inputText.setText("Error");
                    resultText.setVisibility(View.GONE);
                }
            }
        }
    }

    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "×": return a * b;
            case "/": return (b != 0) ? a / b : Double.NaN;
            case "%": return a % b;
        }
        return 0;
    }

    private void clearAll() {
        currentInput = "";
        operation = "";
        firstNumber = 0;
        operationSet = false;
        inputText.setText("");
        resultText.setText("");
        resultText.setVisibility(View.GONE);
    }

    private void toggleSign() {
        if (!currentInput.isEmpty() && !operationSet) {
            try {
                double num = Double.parseDouble(currentInput);
                num = -num;
                currentInput = String.valueOf(num);
                inputText.setText(currentInput);
            } catch (NumberFormatException ignored) {}
        }
    }

    private String format(double value) {
        if (Double.isNaN(value)) return "Error: ÷ by 0";
        if (value == (long) value) return String.format("%d", (long) value);
        else return String.format("%.2f", value);
    }
}
