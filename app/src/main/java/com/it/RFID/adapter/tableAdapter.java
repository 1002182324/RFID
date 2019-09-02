package com.it.RFID.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.it.RFID.R;
import com.it.RFID.bean.TableBean;

import java.util.List;

public class tableAdapter extends BaseAdapter {

    private List<TableBean> lstTable;
    private Context mcontext;
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();//用来存放CheckBox的选中状态，true为选中,false为没有选中

    public tableAdapter(Context context, List<TableBean> lstTable) {

        this.lstTable = lstTable;
        this.mcontext = context;
    }

    @Override
    public int getCount() {
        return lstTable.size();
    }

    @Override
    public TableBean getItem(int position) {
        if (lstTable.size() > 0) {
            return lstTable.get(position);
        }
        throw new IllegalStateException("No table at position " + position);
    }

    public SparseBooleanArray getStateCheckedMap() {
        return stateCheckedMap;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder view = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(mcontext).inflate(R.layout.list_table, parent, false);
            view = new ViewHolder();
            view.batchid = convertView.findViewById(R.id.batch);
            view.shopid = convertView.findViewById(R.id.shop);
            view.boxid = convertView.findViewById(R.id.box);
            view.cb = convertView.findViewById(R.id.cb);
            convertView.setTag(view);

        } else {
            view = (ViewHolder) convertView.getTag();
        }

        view.batchid.setText(lstTable.get(position).getBatchid());
        view.shopid.setText(lstTable.get(position).getShopid());
        view.boxid.setText(lstTable.get(position).getBoxid());
        view.cb.setChecked(stateCheckedMap.get(position, false));
        view.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stateCheckedMap.put(position, isChecked);
                Log.e("db", "onCheckedChanged: " + isChecked);
            }
        });


        return convertView;
    }


    static class ViewHolder {

        TextView batchid;
        TextView shopid;
        TextView boxid;
        CheckBox cb;
    }

}