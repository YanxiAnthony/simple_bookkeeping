package com.example.simple_bookkeeping.line_chart;

import android.graphics.Color;
import android.view.View;

import com.example.simple_bookkeeping.db.BarChartItemBean;
import com.example.simple_bookkeeping.db.DBManager;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class IncomeChartFragment extends BasecomeChartFragment {
    int kind = 1;

    @Override
    public void onResume() {
        super.onResume();
        loadData(year, month, kind);
    }

    @Override
    protected void setAxisData(int year, int month) {
        List<ILineDataSet> sets = new ArrayList<>();
//        获取这个月每天的支出总金额
        List<BarChartItemBean> list = DBManager.getSumMoneyOneDayInMonth(year, month, kind);
        if (list.size() == 0) {
            lineChart.setVisibility(View.GONE);
            chartTv.setVisibility(View.VISIBLE);
        } else {
            lineChart.setVisibility(View.VISIBLE);
            chartTv.setVisibility(View.GONE);
//          设置有多少根柱子
            List<Entry> entries1 = new ArrayList<>();
            for (int i = 0; i < 31; i++) {
                Entry entry = new Entry(i, 0.0f);
                entries1.add(entry);
            }
            for (int i = 0; i < list.size(); i++) {
                BarChartItemBean itemBean = list.get(i);
                int day = itemBean.getDay();//获取日期
                //根据天数，获取X轴的位置
                int xIndex = day - 1;
                Entry entry1 = entries1.get(xIndex);
                entry1.setY(itemBean.getSummoney());
            }
            LineDataSet dataSet = new LineDataSet(entries1, "");
            dataSet.setValueTextSize(8f);
            dataSet.setColor(Color.parseColor("#006400"));
            dataSet.setCircleColor(Color.parseColor("#FDC06A"));
            dataSet.setDrawValues(false); // 不显示值
            sets.add(dataSet);

            LineData lineData = new LineData(sets);
            lineChart.setData(lineData);
            lineChart.setScaleEnabled(false); // 设置不能缩放
            lineChart.setTouchEnabled(false); // 设置不能触摸
        }
    }

    @Override
    protected void setYAxis(int year, int month) {
        //获取本月收入最高的一天为多少，将他设定为y轴的最大值
        float maxMoney = DBManager.getMaxMoneyOneDayInMonth(year, month, kind);
        float max = (float) Math.ceil(maxMoney);   // 将最大金额向上取整
        //设置y轴
        // 右y轴
        YAxis yAxis_right = lineChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);  // 设置y轴的最大值
        yAxis_right.setAxisMinimum(0f);  // 设置y轴的最小值
        yAxis_right.setTextSize(12f);

        // 左y轴
        YAxis yAxis_left = lineChart.getAxisLeft();
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setAxisMaximum(max);
        yAxis_left.setTextSize(12f);

        //设置不显示图例
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        //不显示右边坐标轴
        lineChart.getAxisRight().setEnabled(false);
    }

    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year, month, kind);
    }
}
