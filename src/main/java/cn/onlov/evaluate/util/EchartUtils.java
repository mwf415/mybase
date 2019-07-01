package cn.onlov.evaluate.util;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.feature.DataView;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.feature.Mark;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Funnel;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.itemstyle.Normal;

import java.util.HashMap;
import java.util.Map;

public class EchartUtils {

    public GsonOption getBar(boolean isHorizontal,String [] xString,int [] datas) {
//        String[] citis = { "广州", "深圳", "珠海", "汕头", "韶关", "佛山" };
//        int[] datas = { 6030, 7800, 5200, 3444, 2666, 5708 };
        String[] colors = { "rgb(2,111,230)", "rgb(186,73,46)", "rgb(78,154,97)", "rgb(2,111,230)", "rgb(186,73,46)", "rgb(78,154,97)" };
        String title = "考试分数统计";

        GsonOption option = new GsonOption();

        option.title(title); // 标题
        // 工具栏
        option.toolbox().show(true).feature(Tool.mark, // 辅助线
                Tool.dataView, // 数据视图
                new MagicType(Magic.line, Magic.bar),// 线图、柱状图切换
                Tool.restore,// 还原
                Tool.saveAsImage);// 保存为图片

        option.tooltip().show(true).formatter("{a} <br/>{b} : {c}");//显示工具提示,设置提示格式

        option.legend(title);// 图例

        Bar bar = new Bar(title);// 图类别(柱状图)
        CategoryAxis category = new CategoryAxis();// 轴分类
        category.data(xString);// 轴数据类别
        // 循环数据
        for (int i = 0; i < xString.length; i++) {
            int data = datas[i];
            String color = colors[i];
            // 类目对应的柱状图
            Map<String, Object> map = new HashMap<String, Object>(2);
            map.put("value", data);
            map.put("itemStyle", new ItemStyle().normal(new Normal().color(color)));
            bar.data(map);
        }

        if (isHorizontal) {// 横轴为类别、纵轴为值
            option.xAxis(category);// x轴
            option.yAxis(new ValueAxis());// y轴
        } else {// 横轴为值、纵轴为类别
            option.xAxis(new ValueAxis());// x轴
            option.yAxis(category);// y轴
        }

        option.series(bar);
        return option;
    }


    /**
     *  饼图
     */
    public static GsonOption getPie(String[] types,int[] datas ,String title ) {
//        String[] types = { "邮件营销", "联盟广告", "视频广告" };
//        int[] datas = { 120, 132, 101 };
//        String title = "数据";
        GsonOption option = new GsonOption();

        option.title().text(title).subtext("虚构").x("center");// 大标题、小标题、标题位置

        // 提示工具 鼠标在每一个数据项上，触发显示提示数据
        option.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");

        // 工具栏
        option.toolbox().show(true).feature(new Mark().show(true),// 辅助线
                new DataView().show(true).readOnly(false),// 数据视图
                new MagicType().show(true).type(new Magic[] { Magic.pie, Magic.funnel }), //饼图、漏斗图切换
                new Option().series(new Funnel().x("25%").width("50%").funnelAlign(X.left).max(1548)),// 漏斗图设置
                Tool.restore,// 还原
                Tool.saveAsImage);// 保存为图片

        option.legend().orient(Orient.horizontal).x("left").data(types);// 图例及位置

        option.calculable(true);// 拖动进行计算

        Pie pie = new Pie();

        // 标题、半径、位置
        pie.name(title).radius("55%").center("50%", "60%");

        // 循环数据
        for (int i = 0; i < types.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>(2);
            map.put("value", datas[i]);
            map.put("name", types[i]);
            pie.data(map);
        }

        option.series(pie);
        return option;
    }
}
