package com.it.RFID;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.it.RFID.DB.SKUEPC;
import com.it.RFID.activity.RFActivity;
import com.it.RFID.adapter.tableAdapter;
import com.it.RFID.bean.TableBean;
import com.it.RFID.tools.FileUtils;
import com.it.RFID.tools.PermissionUtil;
import com.it.RFID.tools.SQLUtil;
import com.it.RFID.tools.StringUtils;
import com.it.RFID.tools.ToastUtil;
import com.it.RFID.tools.UpdataUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIAnimationListView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import org.xutils.DbManager;
import org.xutils.common.util.FileUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * The type Main activity.
 */
public class MainActivity extends BaseActivity {

    /**
     * The Lv table.
     */
    // @ViewInject(R.id.list_table)
    QMUIAnimationListView lv_table;
    /**
     * The Dao config.
     */
    DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("test.db")
            .setAllowTransaction(true)
            // 不设置dbDir时, 默认存储在app的私有目录.
            .setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
            .setDbVersion(3)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    // TODO: ...
                    // db.addColumn(...);
                    // db.dropTable(...);
                    // ...
                    // or
                    // db.dropDb();
                }
            });
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();//用来存放CheckBox的选中状态，true为选中,false为没有选中
    private tableAdapter ta;
    private TableBean taItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);
        initTooBar();
        initview();
        initListener();
        PermissionUtil.getPermission(this);
        initData();

        UpdataUtil.checkUpdate(this);


    }

    private void initListener() {


        lv_table.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TableBean selectedItem = (TableBean) parent.getItemAtPosition(position);

                String batch = selectedItem.getBatchid().trim();
                String shop = selectedItem.getShopid().trim();
                String box = selectedItem.getBoxid().trim();
                toRFActivity(batch, shop, box);
            }
        });


        lv_table.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {
                final TableBean selectedItem = (TableBean) parent.getItemAtPosition(position);
                // reName(selectedItem);
                String[] listItems = new String[]{
                        "删除",
                        "修改",
                        "导出",
                };
                List<String> data = new ArrayList<>();
                Collections.addAll(data, listItems);
                final QMUIListPopup qmuiListPopup = new QMUIListPopup(MainActivity.this,
                        QMUIPopup.DIRECTION_NONE,
                        new ArrayAdapter<>(MainActivity.this,
                                R.layout.simple_list_item, data));
                qmuiListPopup.create(QMUIDisplayHelper.dp2px(MainActivity.this, 250), QMUIDisplayHelper.dp2px(MainActivity.this, 200), new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                new QMUIDialog.MessageDialogBuilder(MainActivity.this)
                                        .setTitle("删除盘点单")
                                        .setMessage("确定删除此盘点单吗")
                                        .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                                            @Override
                                            public void onClick(QMUIDialog dialog, int index) {
                                                DbManager dm = x.getDb(daoConfig);
                                                try {
                                                    dm.execNonQuery(SQLUtil.delTable(selectedItem.toString()));
                                                } catch (DbException e) {
                                                    Log.e("db", "onDelPanTab: " + e.getMessage());
                                                }
                                                initview();
                                                dialog.dismiss();
                                            }
                                        })
                                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                                            @Override
                                            public void onClick(QMUIDialog dialog, int index) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .create().show();
                                break;
                            case 1:
                                reName(selectedItem);
                                break;
                            case 2:

                                if (FileUtil.deleteFileOrDir(new File("/sdcard/pgit/TestData")) &&
                                        FileUtil.deleteFileOrDir(new File("/sdcard/pgit/TestName"))) {
                                    if (FileUtils.writeText("/sdcard/pgit/TestName/NameList.txt", selectedItem.getTableName())
                                            && FileUtils.wEPC2TXT(selectedItem.toString(), selectedItem.getTableName())) {
                                        ToastUtil.makeLToast("导出成功");

                                    } else {
                                        ToastUtil.makeLToast("导出失败");

                                    }

                                } else {
                                    ToastUtil.makeLToast("删除失败");
                                }


                                break;
                        }
                        qmuiListPopup.dismiss();
                    }
                });

                qmuiListPopup.show(view);
                return true;
            }
        });
    }


    private void reName(TableBean selectedItem) {
        final String batchid = selectedItem.getBatchid();
        final String shopid = selectedItem.getShopid();
        final String boxid = selectedItem.getBoxid();
        QMUIDialog dialog = new QMUIDialog.CustomDialogBuilder(MainActivity.this)
                .setLayout(R.layout.layout_newtab)
                .setTitle("修改")
                .addAction(0, "修改", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String batch = ((EditText) dialog.findViewById(R.id.edt_batch)).getText().toString().trim();
                        String shop = ((EditText) dialog.findViewById(R.id.edt_shop)).getText().toString().trim();
                        String box = ((EditText) dialog.findViewById(R.id.edt_box)).getText().toString().trim();
                        if (StringUtils.isNotEmpty(batch) && StringUtils.isNotEmpty(shop) && StringUtils.isNotEmpty(box)) {
                            DbManager dm = x.getDb(daoConfig);
                            try {
                                dm.execNonQuery(SQLUtil.renameTable(batchid + "_" + shopid + "_" + boxid, batch + "_" + shop + "_" + box));
                            } catch (DbException e) {
                                e.printStackTrace();
                                Log.e("database", "修改单号:onClick: " + e.getMessage());
                            }
                            initview();
                            dialog.dismiss();
                        } else {
                            ToastUtil.makeLToast("不能为空");
                        }
                    }
                })
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create();
        ((EditText) dialog.findViewById(R.id.edt_batch)).setText(batchid);
        ((EditText) dialog.findViewById(R.id.edt_shop)).setText(shopid);
        ((EditText) dialog.findViewById(R.id.edt_box)).setText(boxid);
        dialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        initview();
    }

    private void initTooBar() {
//
//        QMUITopBar topBar = findViewById(R.id.toolbar);
//        topBar.setTitle("盘点");
//        topBar.addLeftTextButton("返回", 0).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

    }


    @Event(value = {R.id.btn_delpandian}, type = View.OnClickListener.class)
    private void onDelPanTab(View v) {

        stateCheckedMap = ta.getStateCheckedMap();
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("删除盘点单")
                .setMessage("确定删除此盘点单吗")
                .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        DbManager dm = x.getDb(daoConfig);
                        for (int i = stateCheckedMap.size() - 1; i >= 0; i--) {
                            Log.e("db", "onDelPanTab: " + stateCheckedMap.valueAt(i));
                            if (stateCheckedMap.valueAt(i)) {
                                taItem = ta.getItem(stateCheckedMap.keyAt(i));
                                try {
                                    dm.execNonQuery(SQLUtil.delTable(taItem.toString()));
                                } catch (DbException e) {
                                    Log.e("db", "onDelPanTab: " + e.getMessage());
                                }
                            }
                        }
                        initview();
                        dialog.dismiss();
                    }
                })
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }


    @Event(value = {R.id.get_pandian}, type = View.OnClickListener.class)
    private void onTab2File(View v) {

        ToastUtil.makeLToast("fdsgsd");
        stateCheckedMap = ta.getStateCheckedMap();

        if (FileUtil.deleteFileOrDir(new File("/sdcard/pgit/TestData")) &&
                FileUtil.deleteFileOrDir(new File("/sdcard/pgit/TestName"))) {
            for (int i = stateCheckedMap.size(); i >= 0; i--) {
                if (stateCheckedMap.get(i)) {
                    taItem = ta.getItem(i);
                    if (FileUtils.writeText("/sdcard/pgit/TestName/NameList.txt", taItem.getTableName())
                            && FileUtils.wEPC2TXT(taItem.toString(), taItem.getTableName())) {
                        ToastUtil.makeLToast(taItem.toString() + "导出成功");
                    }
                }
            }
        } else {
            ToastUtil.makeLToast("删除失败");
        }
    }


    @Event(value = {R.id.btn_newpandian}, type = View.OnClickListener.class)
    private void onNewPanTab(View v) {
        new QMUIDialog.CustomDialogBuilder(this).setLayout(R.layout.layout_newtab)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String batch = ((EditText) dialog.findViewById(R.id.edt_batch)).getText().toString().trim();
                        String shop = ((EditText) dialog.findViewById(R.id.edt_shop)).getText().toString().trim();
                        String box = ((EditText) dialog.findViewById(R.id.edt_box)).getText().toString().trim();
                        if (StringUtils.isNotEmpty(batch) && StringUtils.isNotEmpty(shop) && StringUtils.isNotEmpty(box)) {
                            DbManager dm = x.getDb(daoConfig);
                            try {
                                dm.execNonQuery(SQLUtil.creatTable(batch + "_" + shop + "_" + box));
                            } catch (DbException e) {
                                e.printStackTrace();
                                Log.e("database", "onNewPanTab:onClick: " + e.getMessage());
                            }
                            initview();
                            toRFActivity(batch, shop, box);
                            dialog.dismiss();
                        } else {
                            ToastUtil.makeLToast("不能为空");
                        }
                    }
                })
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void toRFActivity(String batch, String shop, String box) {
        Intent intent = new Intent(MainActivity.this, RFActivity.class);
        intent.putExtra("batch", batch);
        intent.putExtra("shop", shop);
        intent.putExtra("box", box);
        startActivity(intent);
    }


    private void initview() {


        lv_table = findViewById(R.id.list_table);
        DbManager dm = x.getDb(daoConfig);

        try {
            List<TableBean> list = new ArrayList<>();
            TableBean tb;
            Cursor cursor = dm.execQuery(SQLUtil.checkTable());
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String string = cursor.getString(0);
                    string = string.replaceFirst("table_", "");
                    String[] s = string.split("_");
                    tb = new TableBean();
                    tb.setBatchid(s[0]);
                    tb.setShopid(s[1]);
                    tb.setBoxid(s[2]);
                    list.add(tb);
                } while (cursor.moveToNext());
            }
            ta = new tableAdapter(MainActivity.this, list);
            lv_table.setAdapter(ta);

        } catch (DbException e) {
            e.printStackTrace();
            Log.e("db", "exception: " + e);
        }


    }


    //初始化并导入对照表到数据库
    private void initData() {
        File sd = Environment.getExternalStorageDirectory();
        String pdit = sd.getPath() + "/pgit";
        File file = new File(pdit);
        if (!file.exists()) {
            file.mkdir();
        }
        new MyTask().execute();

    }

    //加载数据库异步线程
    private class MyTask extends AsyncTask<String, Integer, String> {
        private QMUITipDialog tipDialog = new QMUITipDialog.Builder(MainActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载数据文件")
                .create();


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                new QMUIDialog.MessageDialogBuilder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage(s)
                        .create()
                        .show();
            }
            tipDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {

            tipDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                File file = new File(Environment.getExternalStorageDirectory() + "/pgit/epc.txt");
                if (!file.exists()) {
                    return "没有找到对照表，请连接PC上传";
                }
                String s;
                SKUEPC db;
                DbManager dm = x.getDb(daoConfig);

                List<SKUEPC> list = new ArrayList<>();
                BufferedReader br = new BufferedReader(new FileReader(file));
                while ((s = br.readLine()) != null) {
                    db = new SKUEPC();
                    db.setEPC(s.split("\\|")[0]);
                    db.setSKU(s.split("\\|")[1]);
                    list.add(db);

                }
                //  dm.delete(SKUEPC.class);
                dm.replace(list);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("database----", e.getMessage());
            }

            return null;
        }
    }
}
