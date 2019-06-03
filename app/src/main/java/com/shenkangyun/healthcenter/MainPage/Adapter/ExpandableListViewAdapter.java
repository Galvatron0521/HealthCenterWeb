package com.shenkangyun.healthcenter.MainPage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.shenkangyun.healthcenter.BeanFolder.EPDSEntity;
import com.shenkangyun.healthcenter.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/11/24.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context mcontext;
    private List<String> month;
    private List<List<EPDSEntity>> entities;

    public ExpandableListViewAdapter(Context context, List<String> Month, List<List<EPDSEntity>> epdsEntities) {

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
        ExpandableListViewAdapter.GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.partent_item, parent, false);
            groupViewHolder = new ExpandableListViewAdapter.GroupViewHolder();
            groupViewHolder.tvTitle = convertView.findViewById(R.id.label_group_normal);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (ExpandableListViewAdapter.GroupViewHolder) convertView.getTag();
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
        ExpandableListViewAdapter.ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item_time, parent, false);
            childViewHolder = new ExpandableListViewAdapter.ChildViewHolder();
            childViewHolder.tvTitle = convertView.findViewById(R.id.expand_child);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ExpandableListViewAdapter.ChildViewHolder) convertView.getTag();
        }

        Date date = new Date(entities.get(groupPosition).get(childPosition).getUpdateTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        String format = simpleDateFormat.format(date);
        childViewHolder.tvTitle.setText(format);
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
        TextView tvTitle;
    }
}
