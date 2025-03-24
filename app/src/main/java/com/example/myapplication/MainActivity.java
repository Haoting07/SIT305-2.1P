package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerSource, spinnerDestination;
    private EditText editTextValue;
    private Button buttonConvert, buttonLength, buttonWeight, buttonTemperature;
    private TextView textViewResult;

    private final String[] lengthUnits = {"Inch", "Foot", "Yard", "Mile", "Centimeter", "Kilometer"};
    private final String[] weightUnits = {"Pound", "Ounce", "Ton", "Gram", "Kilogram"};
    private final String[] tempUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    private final Map<String, Double> lengthMap = new HashMap<>();
    private final Map<String, Double> weightMap = new HashMap<>();
    private String currentCategory = "Length";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerSource = findViewById(R.id.spinnerSourceUnit);
        spinnerDestination = findViewById(R.id.spinnerDestinationUnit);
        editTextValue = findViewById(R.id.editTextValue);
        buttonConvert = findViewById(R.id.buttonConvert);
        textViewResult = findViewById(R.id.textViewResult);

        buttonLength = findViewById(R.id.buttonLength);
        buttonWeight = findViewById(R.id.buttonWeight);
        buttonTemperature = findViewById(R.id.buttonTemperature);

        initializeConversionMaps();
        updateUnitSpinner(lengthUnits);

        buttonLength.setOnClickListener(view -> {
            currentCategory = "Length";
            updateUnitSpinner(lengthUnits);
        });

        buttonWeight.setOnClickListener(view -> {
            currentCategory = "Weight";
            updateUnitSpinner(weightUnits);
        });

        buttonTemperature.setOnClickListener(view -> {
            currentCategory = "Temperature";
            updateUnitSpinner(tempUnits);
        });

        buttonConvert.setOnClickListener(view -> performConversion());
    }

    private void initializeConversionMaps() {
        lengthMap.put("Inch", 2.54);
        lengthMap.put("Foot", 30.48);
        lengthMap.put("Yard", 91.44);
        lengthMap.put("Mile", 160934.0);

        weightMap.put("Pound", 0.453592);
        weightMap.put("Ounce", 0.0283495);
        weightMap.put("Ton", 907.185);
    }

    private void updateUnitSpinner(String[] units) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        spinnerSource.setAdapter(adapter);
        spinnerDestination.setAdapter(adapter);
    }

    private void performConversion() {
        String sourceUnit = spinnerSource.getSelectedItem().toString();
        String destinationUnit = spinnerDestination.getSelectedItem().toString();
        String inputValue = editTextValue.getText().toString();

        if (inputValue.isEmpty()) {
            textViewResult.setText("Please enter a value.");
            return;
        }

        double value = Double.parseDouble(inputValue);
        double result = convertUnits(sourceUnit, destinationUnit, value);

        textViewResult.setText("Result: " + result + " " + destinationUnit);
    }

    private double convertUnits(String from, String to, double value) {
        if (currentCategory.equals("Length") && lengthMap.containsKey(from) && lengthMap.containsKey(to)) {
            return (value * lengthMap.get(from)) / lengthMap.get(to);
        }
        if (currentCategory.equals("Weight") && weightMap.containsKey(from) && weightMap.containsKey(to)) {
            return (value * weightMap.get(from)) / weightMap.get(to);
        }
        if (currentCategory.equals("Temperature")) {
            if (from.equals("Celsius") && to.equals("Fahrenheit")) {
                return (value * 1.8) + 32;
            }
            if (from.equals("Fahrenheit") && to.equals("Celsius")) {
                return (value - 32) / 1.8;
            }
            if (from.equals("Celsius") && to.equals("Kelvin")) {
                return value + 273.15;
            }
            if (from.equals("Kelvin") && to.equals("Celsius")) {
                return value - 273.15;
            }
        }
        return value;
    }
}
