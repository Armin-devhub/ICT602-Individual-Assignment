package com.example.electricitybillestimator;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    TextView txtDetailMonth, txtDetailYear, txtDetailUnit, txtDetailTotal, txtDetailRebate, txtDetailFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //getSupportActionBar().setTitle("ElectriCount");

        txtDetailMonth = findViewById(R.id.txtDetailMonth);
        txtDetailYear = findViewById(R.id.txtDetailYear);
        txtDetailUnit = findViewById(R.id.txtDetailUnit);
        txtDetailTotal = findViewById(R.id.txtDetailTotal);
        txtDetailRebate = findViewById(R.id.txtDetailRebate);
        txtDetailFinal = findViewById(R.id.txtDetailFinal);

        Bill bill = (Bill) getIntent().getSerializableExtra("bill");

        if (bill != null) {
            txtDetailMonth.setText("" + bill.getMonth());
            txtDetailYear.setText("" + bill.getYear());
            txtDetailUnit.setText("" + bill.getUnitUsed());
            txtDetailTotal.setText("RM " + String.format("%.2f", bill.getTotalCharges()));
            txtDetailRebate.setText("RM " + String.format("%.2f", bill.getRebate()));
            txtDetailFinal.setText("RM " + String.format("%.2f", bill.getFinalCost()));
        }
    }
}
