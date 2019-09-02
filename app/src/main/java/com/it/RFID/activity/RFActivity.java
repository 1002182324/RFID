package com.it.RFID.activity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.it.RFID.R;
import com.it.RFID.tools.DaoUtils;
import com.it.RFID.tools.SQLUtil;
import com.it.RFID.tools.StringUtils;
import com.it.RFID.tools.ToastUtil;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.HashMap;

import static com.it.RFID.MyApplication.getContext;
import static com.it.RFID.MyApplication.xdm;

public class RFActivity extends UHFMainActivity {

    Handler handler;
    private QMUITopBar topBar;
    private String tableName;
    private CheckBox cbFilter;
    private TextView tvBatch;
    private TextView tvShop;
    private TextView tvBox;
    private TextView tvCount;
    private ListView LvTags;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, String>> tagList;
    private HashMap<String, String> map;
    private Cursor cursor;
    private boolean loopFlag;
    private boolean keyFlag = false;
    private EditText edtFilter;
    private String Res = "";

    @Override
    protected void onStart() {
        super.onStart();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.obj + "";
                String[] strs = result.split("@");
                try {
                    addSKUToList(strs[0]);
                } catch (DbException e) {
                    e.printStackTrace();
                }

            }
        };
    }


    @Override
    public void onPause() {
        Log.i("RF", "onPause");
        super.onPause();

        // 停止识别
        stopInventory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rf);
        bindView();
        tagList = new ArrayList<HashMap<String, String>>();
        String batch = getIntent().getStringExtra("batch");
        String shop = getIntent().getStringExtra("shop");
        String box = getIntent().getStringExtra("box");
        tableName = batch + "_" + shop + "_" + box;
        tvBatch.setText(batch);
        tvShop.setText(shop);
        tvBox.setText(box);
        initTooBar();
        initList();
        initData();


    }

    private void bindView() {
        topBar = findViewById(R.id.toolbar);
        cbFilter = findViewById(R.id.cb_Filter);
        tvBatch = findViewById(R.id.tv_batch);
        tvShop = findViewById(R.id.tv_shop);
        tvBox = findViewById(R.id.tv_box);
        tvCount = findViewById(R.id.tv_count);
        LvTags = findViewById(R.id.LvTags);
        edtFilter = findViewById(R.id.edt_filter);
    }

    private void initData() {
        try {
            cursor = xdm.execQuery(SQLUtil.queryCount(tableName));
            cursor.moveToFirst();
            tvCount.setText(cursor.getString(cursor.getColumnIndex("count(*)")));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    private void initTooBar() {

        topBar.addLeftTextButton("返回", 0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void initList() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                tagList = DaoUtils.getSKU_list(tableName);
                if (tagList == null) {
                    tagList = new ArrayList<HashMap<String, String>>();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter = new SimpleAdapter(getContext(), tagList, R.layout.list_tag,
                        new String[]{"tagCount", "sku"},
                        new int[]{R.id.skucount, R.id.sku_id});

                LvTags.setAdapter(adapter);
            }
        }.execute();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139 || keyCode == 280) {

            if (event.getRepeatCount() == 0) {

                if (!keyFlag) {
                    readTag();

                } else {
                    stopInventory();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 添加SKU到列表中
     *
     * @param epc
     */
    private void addSKUToList(String epc) throws DbException {
        if (!TextUtils.isEmpty(epc) && !checkIsExist(epc)) {
            String sku = SQLUtil.getSKU(epc);
            Log.e("tag", "addSKUToList: " + sku);
            if (sku == null && setFilter(sku)) {
                return;
            }
            //int index = checkIsExist(epc);
            int index = checkIsskuExist(sku);
            Log.e("sku", "addSKUToList: " + sku);
            map = new HashMap<String, String>();

            //        map.put("tagUii", epc);
            map.put("tagCount", String.valueOf(1));
            map.put("sku", sku);

            // mContext.getAppContext().uhfQueue.offer(epc + "\t 1");

            if (index == -1) {
                xdm.execNonQuery(SQLUtil.insertEPCandSKU(tableName, epc, sku));
                tagList.add(map);
                LvTags.setAdapter(adapter);
                cursor = xdm.execQuery(SQLUtil.queryCount(tableName));
                cursor.moveToFirst();
                tvCount.setText("" + cursor.getString(cursor.getColumnIndex("count(*)")));
            } else {
                xdm.execQuery(SQLUtil.queryskuCount(tableName, sku));
                int tagcount = Integer.parseInt(
                        cursor.getString(cursor.getColumnIndex("count(*)")), 10);

                map.put("tagCount", String.valueOf(tagcount));

                tagList.set(index, map);

            }
            RFActivity.this.playSound(1);
            adapter.notifyDataSetChanged();

        }
    }


    /**
     * 判断EPC是否在列表中
     *
     * @param strEPC 索引
     * @return
     */
    public Boolean checkIsExist(String strEPC) throws DbException {
        Boolean existFlag = false;
        if (StringUtils.isEmpty(strEPC)) {
            return existFlag;
        }
        Cursor cursor = xdm.execQuery(SQLUtil.queryEPCCount(strEPC));
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getString(cursor.getColumnIndex("count(*)")) == "1") {
                existFlag = true;
            }
        }
        return existFlag;
    }

    /**
     * 判断sku是否在列表中
     *
     * @param strsku 索引
     * @return
     */
    public int checkIsskuExist(String strsku) {
        int existFlag = -1;
        if (StringUtils.isEmpty(strsku)) {
            return existFlag;
        }
        String tempStr = "";
        for (int i = 0; i < tagList.size(); i++) {
            HashMap<String, String> temp = new HashMap<String, String>();
            temp = tagList.get(i);
            tempStr = temp.get("tagUii");
            if (strsku.equals(tempStr)) {
                existFlag = i;
                break;
            }
        }
        return existFlag;
    }


    private void readTag() {

        //  mContext.mReader.setEPCTIDMode(true);
        if (this.mReader.startInventoryTag(1, 10)) {

            loopFlag = true;
            setViewEnabled(false);
            new TagThread().start();
        } else {
            this.mReader.stopInventory();
            ToastUtil.makeLToast("开启识别标签失败");
//					mContext.playSound(2);
        }


    }

    private void setViewEnabled(boolean b) {
        if (b) {
            topBar.setTitle("正在盘点");
        } else {
            topBar.setTitle("停止盘点");
        }

    }

    private boolean setFilter(String SKU) {
        return !cbFilter.isChecked() || SKU.matches(edtFilter.getText().toString().trim());
    }

    /**
     * 停止识别
     */
    private void stopInventory() {
        if (loopFlag) {
            loopFlag = false;
            setViewEnabled(true);
            if (this.mReader.stopInventory()) {
                topBar.setTitle("停止盘点");
            } else {
                ToastUtil.makeLToast("停止识别标签失败");
            }
        }
    }


    class TagThread extends Thread {
        @Override
        public void run() {
            String strTid;
            String strResult;
            String[] res = null;
            while (loopFlag) {
                res = RFActivity.this.mReader.readTagFromBuffer();
                if (res != null) {
                    strTid = res[0];
                    if (strTid.length() != 0 && !strTid.equals("0000000" +
                            "000000000") && !"000000000000000000000000".equals(strTid)) {
                        strResult = "TID:" + strTid + "\n";
                    } else {
                        strResult = "";
                    }
                    Log.i("data", "EPC:" + res[1] + "|" + strResult);
                    Message msg = handler.obtainMessage();
                    msg.obj = strResult + "EPC:" + RFActivity.this.mReader.convertUiiToEPC(res[1]) + "@" + res[2];

                    handler.sendMessage(msg);
                }
            }
        }
    }

}
