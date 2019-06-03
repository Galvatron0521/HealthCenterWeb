package com.shenkangyun.healthcenter.BeanFolder;

/**
 * Created by Administrator on 2018/10/18.
 */

public class EPDSEntity {
    public String results;
    public long updateTime;
    public String fieldRecordID;

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getFieldRecordID() {
        return fieldRecordID;
    }

    public void setFieldRecordID(String fieldRecordID) {
        this.fieldRecordID = fieldRecordID;
    }
}
