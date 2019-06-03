package com.shenkangyun.healthcenter.MainPage.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.shenkangyun.healthcenter.MainPage.Adapter.ExpandableListViewAdapter;
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

public class MedicalRecordsActivity extends AppCompatActivity {

    @BindView(R.id.toolBar_title)
    TextView toolBarTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.mWebView)
    WebView mWebView;
//    @BindView(R.id.expend_list)
//    ExpandableListView expendList;
//
//    private String months;
//    private List<String> monthList = new ArrayList<>();
//    private List<List<EPDSEntity>> epdsList = new ArrayList<>();
//    private ExpandableListViewAdapter myExpandableListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_records);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.home_red));
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            toolBarTitle.setText("就诊记录");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
//        initNetRequest();
        initWebView();
    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        int userID = Base.getUserID();
        StringBuilder builder = new StringBuilder();
        builder.append(Base.URL).append("?act=").append("patientsField3").append("&data={\"appKey\":\"")
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

//    private void initNetRequest() {
//        OkHttpUtils.post()
//                .url(Base.URL)
//                .addParams("act", "patientsRecordsData")
//                .addParams("moduleCode", "SP020109")
//                .addParams("moduleName", "就诊记录")
//                .addParams("moduleUrl", "/form/patientsfield2.html")
//                .addParams("patientID", String.valueOf(Base.getUserID()))
//                .addParams("appType", "1")
//                .addParams("diseasesid", "")
//                .addParams("data", new patientsRecordsData("1", Base.appKey, Base.timeSpan).toJson())
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
//                List<EPDSEntity> epdsEntities = new ArrayList<>();
//                months = allMonths.get(i).getMonths();
//                monthList.add(months);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONObject data = jsonObject.getJSONObject("data");
//                    JSONObject monthRecordMap = data.getJSONObject("monthRecordMap");
//                    JSONArray Month = monthRecordMap.getJSONArray(months);
//                    for (int j = 0; j < Month.length(); j++) {
//                        EPDSEntity epdsEntity = new EPDSEntity();
//                        JSONObject info = Month.getJSONObject(j);
//                        String results = info.getString("results");
//                        String fieldRecordID = info.getString("id");
//                        Long updateTime = info.getLong("updateTime");
//                        epdsEntity.setResults(results);
//                        epdsEntity.setUpdateTime(updateTime);
//                        epdsEntity.setFieldRecordID(fieldRecordID);
//                        epdsEntities.add(epdsEntity);
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
//        myExpandableListViewAdapter = new ExpandableListViewAdapter(this, monthList, epdsList);
//        expendList.setAdapter(myExpandableListViewAdapter);
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
////                Intent intent = new Intent(MedicalRecordsActivity.this, EditBSESActivity.class);
////                intent.putExtra("moduleCode", moduleCode);
////                intent.putExtra("moduleName", moduleName);
////                intent.putExtra("fieldRecordID", epdsList.get(groupPosition).get(childPosition).getFieldRecordID());
////                startActivityForResult(intent, 1);
//                return true;
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0 && resultCode == 1) {
//            monthList.clear();
//            epdsList.clear();
//            initNetRequest();
//        }
//        if (requestCode == 1 && resultCode == 2) {
//            monthList.clear();
//            epdsList.clear();
//            initNetRequest();
//        }
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
//    static class patientsRecordsData {
//        private String appKey;
//        private String mobileType;
//        private String timeSpan;
//
//        public patientsRecordsData(String mobileType, String appKey, String timeSpan) {
//            this.appKey = appKey;
//            this.timeSpan = timeSpan;
//            this.mobileType = mobileType;
//        }
//
//        public String toJson() {
//            return new Gson().toJson(this);
//        }
//    }
}
