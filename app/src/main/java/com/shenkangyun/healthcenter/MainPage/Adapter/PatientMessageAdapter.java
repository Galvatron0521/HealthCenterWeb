package com.shenkangyun.healthcenter.MainPage.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.shenkangyun.healthcenter.BeanFolder.PatientMessageEntity;
import com.shenkangyun.healthcenter.R;

import java.util.List;

/**
 * Created by Administrator on 2018/11/24.
 */

public class PatientMessageAdapter extends BaseExpandableListAdapter {
    private Context mcontext;
    private List<String> month;
    private List<List<PatientMessageEntity>> entities;

    public PatientMessageAdapter(Context context, List<String> Month, List<List<PatientMessageEntity>> epdsEntities) {
        mcontext = context;
        month = Month;
        entities = epdsEntities;
    }

    @Override
    // 获取分组的个数
    public int getGroupCount() {
        return month.size();
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return entities.get(groupPosition).size();
    }

    //获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return month.get(groupPosition);
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return entities.get(groupPosition).get(childPosition);
    }

    //获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded    该组是展开状态还是伸缩状态
     * @param convertView   重用已有的视图对象
     * @param parent        返回的视图对象始终依附于的视图组
     */
    // 获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        PatientMessageAdapter.GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.partent_item, parent, false);
            groupViewHolder = new PatientMessageAdapter.GroupViewHolder();
            groupViewHolder.tvTitle = convertView.findViewById(R.id.label_group_normal);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (PatientMessageAdapter.GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(month.get(groupPosition));
        return convertView;
    }

    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象
     * @param parent        返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#
     * <p>
     * getChildView(int, int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        PatientMessageAdapter.ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item_message, parent, false);
            childViewHolder = new PatientMessageAdapter.ChildViewHolder();
            childViewHolder.result = convertView.findViewById(R.id.result);
            childViewHolder.moduleName = convertView.findViewById(R.id.moduleName);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (PatientMessageAdapter.ChildViewHolder) convertView.getTag();
        }
        String results = entities.get(groupPosition).get(childPosition).getResults();
        if (TextUtils.isEmpty(results) || "null".equals(results)) {
            childViewHolder.result.setText("空");
        } else {
            childViewHolder.result.setText(results);
        }
        childViewHolder.moduleName.setText(entities.get(groupPosition).get(childPosition).getModuleName());
        return convertView;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView tvTitle;
    }

    static class ChildViewHolder {
        TextView result;
        TextView moduleName;
    }
}