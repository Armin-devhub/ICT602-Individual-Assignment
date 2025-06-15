package com.example.electricitybillestimator.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.electricitybillestimator.Bill;
import com.example.electricitybillestimator.BillAdapter;
import com.example.electricitybillestimator.DBHelper;
import com.example.electricitybillestimator.DetailActivity;
import com.example.electricitybillestimator.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    ListView listView;
    Button btnDeleteAll;
    DBHelper dbHelper;
    ArrayList<Object> displayList;
    BillAdapter adapter;
    ArrayList<Bill> deletedBillsBackup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        listView = view.findViewById(R.id.listView);
        btnDeleteAll = view.findViewById(R.id.btnDeleteAll);

        dbHelper = new DBHelper(getContext());
        ArrayList<Bill> billList = dbHelper.getAllBills();
        displayList = buildSectionedList(billList);

        adapter = new BillAdapter(getContext(), displayList);
        listView.setAdapter(adapter);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        listView.setLayoutAnimation(new LayoutAnimationController(animation));

        btnDeleteAll.setOnClickListener(v -> {
            ArrayList<Bill> allBills = dbHelper.getAllBills();
            if (allBills.isEmpty()) {
                Toast.makeText(getContext(), "No bills to delete", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(getContext())
                    .setTitle("Delete All")
                    .setMessage("Are you sure you want to delete all bills?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deletedBillsBackup = new ArrayList<>(allBills);
                        dbHelper.deleteAllBills();

                        displayList.clear();
                        adapter.notifyDataSetChanged();

                        Snackbar.make(listView, "All bills deleted", Snackbar.LENGTH_LONG)
                                .setAction("Undo", v1 -> {
                                    for (Bill bill : deletedBillsBackup) {
                                        dbHelper.insertBill(bill);
                                    }
                                    ArrayList<Bill> undoBillList = dbHelper.getAllBills();
                                    displayList.clear();
                                    displayList.addAll(buildSectionedList(undoBillList));
                                    adapter.notifyDataSetChanged();
                                })
                                .show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Object obj = displayList.get(position);
            if (obj instanceof Bill) {
                Bill selectedBill = (Bill) obj;
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("bill", selectedBill);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener((parent, view12, position, id) -> {
            Object obj = displayList.get(position);
            if (!(obj instanceof Bill)) return false;

            Bill billToDelete = (Bill) obj;

            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Bill")
                    .setMessage("Are you sure you want to delete this bill?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        dbHelper.deleteBillById(billToDelete.getId());

                        ArrayList<Bill> updatedBillList = dbHelper.getAllBills();
                        displayList.clear();
                        displayList.addAll(buildSectionedList(updatedBillList));
                        adapter.notifyDataSetChanged();

                        Snackbar.make(view12, "Bill deleted", Snackbar.LENGTH_LONG)
                                .setAction("Undo", v1 -> {
                                    dbHelper.insertBill(billToDelete);
                                    ArrayList<Bill> undoBillList = dbHelper.getAllBills();
                                    displayList.clear();
                                    displayList.addAll(buildSectionedList(undoBillList));
                                    adapter.notifyDataSetChanged();
                                })
                                .show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });

        return view;
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