package com.example.electricitybillestimator;

import android.content.Context;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;

public class BillAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Object> items; // Can be String (header) or Bill

    public BillAdapter(Context context, ArrayList<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        Object item = items.get(position);
        if (item instanceof Bill) return ((Bill) item).getId();
        return -position-1; // unique for header
    }

    @Override
    public int getItemViewType(int position) {
        return (items.get(position) instanceof Bill) ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object item = items.get(position);

        if (getItemViewType(position) == 0) { // Header (year)
            TextView tv;
            if (convertView == null || !(convertView instanceof TextView)) {
                tv = new TextView(context);
                // increased top margin for greater gap before header
                tv.setPadding(32, 48, 0, 16); // left, top, right, bottom
                tv.setTextSize(18);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setBackgroundColor(0xFFE0E0E0); // light gray
            } else {
                tv = (TextView) convertView;
                // also update padding in case convertView is reused from a bill item
                tv.setPadding(32, 48, 0, 16);
            }
            tv.setText("Year: " + (String) item);
            return tv;
        } else { // Bill item
            Bill bill = (Bill) item;
            if (convertView == null || !(convertView instanceof LinearLayout)) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            }
            // Extra bottom margin for last bill item before next header
            int nextPos = position + 1;
            View rootLayout = convertView;
            if (rootLayout instanceof LinearLayout) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rootLayout.getLayoutParams();
                if (params == null) {
                    params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
                // If next item is a header or end of list, add bottom margin for gap
                if (nextPos >= getCount() || getItemViewType(nextPos) == 0) {
                    params.bottomMargin = 32; // dp, will be treated as px
                } else {
                    params.bottomMargin = 0;
                }
                rootLayout.setLayoutParams(params);
            }

            TextView txtMonth = convertView.findViewById(R.id.txtMonth);
            TextView txtFinalCost = convertView.findViewById(R.id.txtFinalCost);

            txtMonth.setText(bill.getMonth());
            txtFinalCost.setText(String.format("Final Cost: RM %.2f", bill.getFinalCost()));
            return convertView;
        }
    }
}