package com.shenkangyun.healthcenter.MainPage.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.shenkangyun.healthcenter.BaseFolder.Base;
import com.shenkangyun.healthcenter.BeanFolder.EPDSBean;
import com.shenkangyun.healthcenter.BeanFolder.EPDSEntity;
import com.shenkangyun.healthcenter.BeanFolder.PatientMessageEntity;
import com.shenkangyun.healthcenter.MainPage.Adapter.PatientMessageAdapter;
import com.shenkangyun.healthcenter.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class PatientMessageActivity extends AppCompatActivity {

    @BindView(R.id.toolBar_title)
    TextView toolBarTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
//    @BindView(R.id.expend_list)
//    ExpandableListView expendList;

    @BindView(R.id.mWebView)
    WebView mWebView;

    private String months;

    private List<String> monthList = new ArrayList<>();
    private List<List<PatientMessageEntity>> epdsList = new ArrayList<>();
    private PatientMessageAdapter patientMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_message);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.home_red));
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            toolBarTitle.setText("问卷时间记录");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        initWebView();
    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        int userID = Base.getUserID();
        StringBuilder builder = new StringBuilder();
        builder.append(Base.URL).append("?act=").append("patientMessage").append("&data={\"appKey\":\"")
                .append("dc4d8d31d749ecb86157449d2fb804e0").append("\",").append("\"timeSpan\":\"").append("20101020").append("\",")
                .append("\"appType\":\"").append("1").append("\",")
                .append("\"mobileType\":\"").append("1").append("\",")
                .append("\"patientID\":\"").append(String.valueOf(userID)).append("\"").append("}");
        mWebView.loadUrl(builder.toString());
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_patient_message);
//        ButterKnife.bind(this);
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.home_red));
//        setSupportActionBar(toolBar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            toolBarTitle.setText("患者问卷概览");
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowTitleEnabled(false);
//        }
//        initNetRequest();
//    }
//
//    private void initNetRequest() {
//        OkHttpUtils.post()
//                .url(Base.URL)
//                .addParams("act", "patientMessageNew")
//                .addParams("data", new patientMessageNew("1", "1", String.valueOf(Base.getUserID()), Base.appKey, Base.timeSpan).toJson())
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        jsonToJavaObjectByNative(response);
//                    }
//                });
//    }
//
//    private void jsonToJavaObjectByNative(String response) {
//        Gson gson = new Gson();
//        EPDSBean epdsBean = gson.fromJson(response, EPDSBean.class);
//        List<EPDSBean.DataBean.MonthRecordMapBean.AllMonthBean> allMonths = epdsBean.getData().getMonthRecordMap().getAllMonth();
//        if (allMonths.size() != 0) {
//            for (int i = 0; i < allMonths.size(); i++) {
//                List<PatientMessageEntity> epdsEntities = new ArrayList<>();
//                months = allMonths.get(i).getMonths();
//                monthList.add(months);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONObject data = jsonObject.getJSONObject("data");
//                    JSONObject monthRecordMap = data.getJSONObject("monthRecordMap");
//                    JSONArray Month = monthRecordMap.getJSONArray(months);
//                    for (int j = 0; j < Month.length(); j++) {
//                        PatientMessageEntity patientMessageEntity = new PatientMessageEntity();
//                        JSONObject info = Month.getJSONObject(j);
//                        String results = info.getString("results");
//                        String moduleName = info.getString("moduleName");
//                        int fieldRecordID = info.getInt("id");
//                        Long updateTime = info.getLong("updateTime");
//                        patientMessageEntity.setResults(results);
//                        patientMessageEntity.setUpdateTime(updateTime);
//                        patientMessageEntity.setFieldID(fieldRecordID);
//                        patientMessageEntity.setModuleName(moduleName);
//                        epdsEntities.add(patientMessageEntity);
//                        epdsList.add(epdsEntities);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            initAdapter();
//        }
//    }
//
//    private void initAdapter() {
//        patientMessageAdapter = new PatientMessageAdapter(this, monthList, epdsList);
//        expendList.setAdapter(patientMessageAdapter);
//        expendList.setGroupIndicator(null);
//        //设置分组的监听
//        expendList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                for (int i = 0; i < monthList.size(); i++) {
//                    if (groupPosition != i) {
//                        expendList.collapseGroup(i);
//                    }
//                }
//                return false;
//            }
//        });
//        //设置子项布局监听
//        expendList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                return true;
//            }
//        });
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//        }
//        return true;
//    }
//
//    static class patientMessageNew {
//
//        private String appType;
//        private String mobileType;
//        private String patientID;
//        private String appKey;
//        private String timeSpan;
//
//        public patientMessageNew(String appType, String mobileType, String patientID, String appKey, String timeSpan) {
//            this.appType = appType;
//            this.mobileType = mobileType;
//            this.patientID = patientID;
//            this.appKey = appKey;
//            this.timeSpan = timeSpan;
//        }
//
//        public String toJson() {
//            return new Gson().toJson(this);
//        }
//    }
//}
