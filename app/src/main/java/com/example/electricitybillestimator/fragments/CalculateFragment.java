package com.example.electricitybillestimator.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.electricitybillestimator.Bill;
import com.example.electricitybillestimator.BillAdapter;
import com.example.electricitybillestimator.DBHelper;
import com.example.electricitybillestimator.R;

import java.util.ArrayList;

public class CalculateFragment extends Fragment {

    Spinner spinnerMonth;
    Spinner spinnerYear;
    EditText editUnit;
    RadioGroup radioRebate;
    Button btnCalculate;

    DBHelper dbHelper;
    ArrayList<Object> displayList; // can contain String (year header) or Bill
    BillAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        spinnerMonth = view.findViewById(R.id.spinnerMonth);
        spinnerYear = view.findViewById(R.id.spinnerYear);
        editUnit = view.findViewById(R.id.editUnit);
        radioRebate = view.findViewById(R.id.radioRebate);
        btnCalculate = view.findViewById(R.id.btnCalculate);

        dbHelper = new DBHelper(getContext());
        ArrayList<Bill> billList = dbHelper.getAllBills();
        displayList = buildSectionedList(billList);

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.years, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);

        // Set default selection to 0%
        radioRebate.check(R.id.radio0);

        btnCalculate.setOnClickListener(v -> {
            String month = spinnerMonth.getSelectedItem().toString();
            String year = spinnerYear.getSelectedItem().toString();
            String unitStr = editUnit.getText().toString().trim();

            if (unitStr.isEmpty()) {
                Toast.makeText(getContext(), "Please enter unit used", Toast.LENGTH_SHORT).show();
                return;
            }

            int unit = Integer.parseInt(unitStr);
            double totalCharges = calculateCharges(unit);

            // Get rebate percentage from selected radio button
            int checkedRadioButtonId = radioRebate.getCheckedRadioButtonId();
            RadioButton checkedRadioButton = radioRebate.findViewById(checkedRadioButtonId);
            int rebatePercentage = 0;
            if (checkedRadioButton != null) {
                String text = checkedRadioButton.getText().toString().replace("%", "").trim();
                rebatePercentage = Integer.parseInt(text);
            }

            double rebate = totalCharges * (rebatePercentage / 100.0);
            double finalCost = totalCharges - rebate;

            new AlertDialog.Builder(getContext())
                    .setTitle("Save Calculation")
                    .setMessage("Do you want to save this bill for " + month + "?")
                    .setPositiveButton("Save", (dialog, which) -> {
                        if (dbHelper.isMonthYearExist(month, year)) {
                            Toast.makeText(getContext(), "A bill for "+month+" "+year+" already exists.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Bill bill = new Bill(0, month, year, unit, totalCharges, rebate, finalCost);
                        dbHelper.insertBill(bill);

                        ArrayList<Bill> updatedBillList = dbHelper.getAllBills();
                        displayList.clear();
                        displayList.addAll(buildSectionedList(updatedBillList));

                        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        View currentFocus = requireActivity().getCurrentFocus();
                        if (imm != null && currentFocus != null) {
                            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                        }

                        editUnit.setText("");
                        Toast.makeText(getContext(), "Calculation saved successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return view;
    }

    private double calculateCharges(int unit) {
        double total = 0;
        if (unit <= 200) total = unit * 0.218;
        else if (unit <= 300) total = (200 * 0.218) + ((unit - 200) * 0.334);
        else if (unit <= 600) total = (200 * 0.218) + (100 * 0.334) + ((unit - 300) * 0.516);
        else if (unit <= 900) total = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((unit - 600) * 0.546);
        else total = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + (300 * 0.546) + ((unit - 900) * 0.571);
        return total;
    }

    private ArrayList<Object> buildSectionedList(ArrayList<Bill> bills) {
        ArrayList<Object> result = new ArrayList<>();
        String lastYear = null;
        for (Bill bill : bills) {
            String year = bill.getYear();
            if (!year.equals(lastYear)) {
                result.add(year);
                lastYear = year;
            }
            result.add(bill);
        }
        return result;
    }
}