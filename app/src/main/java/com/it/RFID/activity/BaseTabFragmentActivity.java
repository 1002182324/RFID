package com.it.RFID.activity;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.it.RFID.BaseActivity;
import com.it.RFID.Frament.KeyDwonFragment;
import com.it.RFID.adapter.ViewPagerAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.rscja.deviceapi.RFIDWithUHF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-03-10.
 */
public class BaseTabFragmentActivity extends BaseActivity {

    private final int offscreenPage = 2; //����ViewPager���ڵļ���ҳ��
    // public Reader mReader;
    public RFIDWithUHF mReader;
    protected ActionBar mActionBar;
    protected ViewPagerAdapter mViewPagerAdapter;
    protected List<KeyDwonFragment> lstFrg = new ArrayList<KeyDwonFragment>();
    protected List<String> lstTitles = new ArrayList<String>();
//    private int index = 0;
//    private ActionBar.Tab tab_kill, tab_lock, tab_set;
//    private DisplayMetrics metrics;
//    private AlertDialog dialog;
//    private long[] timeArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initUHF() {

        try {
            mReader = RFIDWithUHF.getInstance();
        } catch (Exception ex) {

            toastMessage(ex.getMessage());

            return;
        }

        if (mReader != null) {
            new InitTask().execute();
        }
    }

    protected void initViewPageData() {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == 139 || keyCode == 280) {

            if (event.getRepeatCount() == 0) {


            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {

        if (mReader != null) {
            mReader.free();
        }
        super.onDestroy();
    }

    public class InitTask extends AsyncTask<String, Integer, Boolean> {
        QMUITipDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            return mReader.init();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            mypDialog.dismiss();

            if (!result) {
                Toast.makeText(BaseTabFragmentActivity.this, "初始化失败",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mypDialog = new QMUITipDialog.Builder(BaseTabFragmentActivity.this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord("初始化rf...")
                    .create();

            mypDialog.setTitle("初始化rf...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }


}
