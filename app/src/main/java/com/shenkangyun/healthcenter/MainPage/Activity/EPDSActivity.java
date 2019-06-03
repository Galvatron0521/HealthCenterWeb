package com.shenkangyun.healthcenter.MainPage.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.shenkangyun.healthcenter.BaseFolder.Base;
import com.shenkangyun.healthcenter.BeanFolder.EPDSBean;
import com.shenkangyun.healthcenter.BeanFolder.EPDSEntity;
import com.shenkangyun.healthcenter.MainPage.Adapter.MyExpandableListViewAdapter;
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
import butterknife.OnClick;
import okhttp3.Call;

public class EPDSActivity extends AppCompatActivity {

    @BindView(R.id.toolBar_title)
    TextView toolBarTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.expend_list)
    ExpandableListView expendList;
    @BindView(R.id.tv_add)
    TextView tvAdd;

    private String moduleCode;
    private String moduleName;
    private String moduleUrl;
    private String months;

    private List<String> monthList = new ArrayList<>();
    private List<List<EPDSEntity>> epdsList = new ArrayList<>();
    private MyExpandableListViewAdapter myExpandableListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epds);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.home_red));
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            toolBarTitle.setText("爱丁堡产后抑郁量表");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        initView();
        initNetRequest();
    }

    private void initView() {
        Intent intent = getIntent();
        moduleCode = intent.getStringExtra("ModuleCode");
        moduleName = intent.getStringExtra("ModuleName");
        moduleUrl = intent.getStringExtra("ModuleUrl");
    }

    private void initNetRequest() {
        OkHttpUtils.post()
                .url(Base.URL)
                .addParams("act", "patientsFieldNew")
                .addParams("moduleCode", moduleCode)
                .addParams("moduleName", moduleName)
                .addParams("moduleUrl", moduleUrl)
                .addParams("patientID", String.valueOf(Base.getUserID()))
                .addParams("appType", "1")
                .addParams("diseasesid", "")
                .addParams("data", new patientsFieldNew("1", Base.appKey, Base.timeSpan).toJson())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        jsonToJavaObjectByNative(response);
                    }
                });
    }

    private void jsonToJavaObjectByNative(String response) {
        Gson gson = new Gson();
        EPDSBean epdsBean = gson.fromJson(response, EPDSBean.class);
        List<EPDSBean.DataBean.MonthRecordMapBean.AllMonthBean> allMonths = epdsBean.getData().getMonthRecordMap().getAllMonth();
        if (allMonths.size() != 0) {
            for (int i = 0; i < allMonths.size(); i++) {
                List<EPDSEntity> epdsEntities = new ArrayList<>();
                months = allMonths.get(i).getMonths();
                monthList.add(months);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject monthRecordMap = data.getJSONObject("monthRecordMap");
                    JSONArray Month = monthRecordMap.getJSONArray(months);
                    for (int j = 0; j < Month.length(); j++) {
                        EPDSEntity epdsEntity = new EPDSEntity();
                        JSONObject info = Month.getJSONObject(j);
                        String results = info.getString("results");
                        String fieldRecordID = info.getString("id");
                        long updateTime = info.getLong("updateTime");
                        epdsEntity.setResults(results);
                        epdsEntity.setUpdateTime(updateTime);
                        epdsEntity.setFieldRecordID(fieldRecordID);
                        epdsEntities.add(epdsEntity);
                        epdsList.add(epdsEntities);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initAdapter();
        }
    }

    @OnClick(R.id.tv_add)
    public void onViewClicked() {
        Intent intent = new Intent(this, AddEPDSActivity.class);
        intent.putExtra("ModuleCode", moduleCode);
        intent.putExtra("ModuleName", moduleName);
        intent.putExtra("ModuleUrl", moduleUrl);
        startActivityForResult(intent, 0);
    }

    private void initAdapter() {
        myExpandableListViewAdapter = new MyExpandableListViewAdapter(this, monthList, epdsList);
        expendList.setAdapter(myExpandableListViewAdapter);
        expendList.setGroupIndicator(null);
        //设置分组的监听
        expendList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                for (int i = 0; i < monthList.size(); i++) {
                    if (groupPosition != i) {
                        expendList.collapseGroup(i);
                    }
                }
                return false;
            }
        });
        //设置子项布局监听
        expendList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(EPDSActivity.this, EditEPDSActivity.class);
                intent.putExtra("moduleCode", moduleCode);
                intent.putExtra("moduleName", moduleName);
                intent.putExtra("fieldRecordID", epdsList.get(groupPosition).get(childPosition).getFieldRecordID());
                startActivityForResult(intent, 1);
                return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            monthList.clear();
            epdsList.clear();
            initNetRequest();
        }
        if (requestCode == 1 && resultCode == 2) {
            monthList.clear();
            epdsList.clear();
            initNetRequest();
        }
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

    static class patientsFieldNew {
        private String appKey;
        private String mobileType;
        private String timeSpan;

        public patientsFieldNew(String mobileType, String appKey, String timeSpan) {
            this.appKey = appKey;
            this.timeSpan = timeSpan;
            this.mobileType = mobileType;
        }

        public String toJson() {
            return new Gson().toJson(this);
        }
    }
}
