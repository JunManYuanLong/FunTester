package com.fun.frame.execute;

import com.fun.config.Constant;
import com.fun.utils.StringUtil;
import com.fun.utils.Time;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.fun.frame.SourceCode.getManyString;
import static com.fun.frame.SourceCode.range;
import static java.util.stream.Collectors.toList;

/**
 * 用于性能测试数据统计工具类,主要是统计常用指标QPS,rt,时间,以及图形化输出测试结果
 *
 * <p>                  Demo
 *
 *     				FunTester300线程
 *
 * 	>>响应时间分布图,横轴排序分成桶的序号,纵轴每个桶的中位数<<
 * 		--<中位数数据最小值为:55 ms,最大值:1255 ms>--
 *                                                                   ██
 *                                                                   ██
 *                                                                   ██
 *                                                                   ██
 *                                                                   ██
 *                                                                ▃▃ ██
 *                                                       ▃▃ ▅▅ ▇▇ ██ ██
 *                                                    ▇▇ ██ ██ ██ ██ ██
 *                                                 ▃▃ ██ ██ ██ ██ ██ ██
 *                                              ▁▁ ██ ██ ██ ██ ██ ██ ██
 *                                           ▁▁ ██ ██ ██ ██ ██ ██ ██ ██
 *                                           ██ ██ ██ ██ ██ ██ ██ ██ ██
 *                                        ▇▇ ██ ██ ██ ██ ██ ██ ██ ██ ██
 *                                     ▇▇ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██
 *                                     ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██
 *                               ▃▃ ▇▇ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██
 *                            ▃▃ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██
 *                         ▂▂ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██
 *                         ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██
 *                      ▅▅ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██
 *                   ▃▃ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██
 *       ▁▁ ▂▂ ▃▃ ▄▄ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██
 * ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██ ██
 * </p>
 */
public class StatisticsUtil extends Constant {

    /**
     * 将性能测试数据图表展示,需要等宽字体显示
     *
     * <p>
     * 将数据排序,然后按照循序分桶,选择桶中中位数作代码,通过二维数组转化成柱状图
     * </p>
     * 生成统计结果数组大小{@link com.fun.config.Constant#BUCKET_SIZE},小于{@link com.fun.config.Constant#BUCKET_SIZE}平方的数据量不予以统计
     *
     * @param data 性能测试数据,也可以其他统计数据
     * @return
     */
    public static String statistics(List<Integer> data, String title, int threadNum) {
        int size = data.size();//获取总数据量大小
        Collections.sort(data);
        if (size < BUCKET_SIZE * BUCKET_SIZE) return EMPTY;//过滤少量数据
        int[] ints = range(1, BUCKET_SIZE + 1).map(x -> data.get(size * x / BUCKET_SIZE - size / BUCKET_SIZE / 2)).toArray();//获取取样点数据数组
        int largest = ints[BUCKET_SIZE - 1];//获取最大值
        String[][] map = Arrays.asList(ArrayUtils.toObject(ints)).stream().map(x -> getPercent(x, largest, BUCKET_SIZE)).collect(toList()).toArray(new String[BUCKET_SIZE][BUCKET_SIZE]);//转换成string二维数组
        String[][] result = new String[BUCKET_SIZE][BUCKET_SIZE];
        /*将二维数组反转成竖排*/
        for (int i = 0; i < BUCKET_SIZE; i++) {
            for (int j = 0; j < BUCKET_SIZE; j++) {
                result[i][j] = getManyString(map[j][BUCKET_SIZE - 1 - i], 2) + SPACE_1;
            }
        }
        StringBuffer table = new StringBuffer(LINE + StringUtil.center(((StringUtils.isEmpty(title)) ? DEFAULT_STRING : title.replaceAll("\\d{14}$", EMPTY) + threadNum), BUCKET_SIZE * 3) + LINE + LINE + StringUtil.center("Response Time: x-serial num,y-median", BUCKET_SIZE * 3) + LINE + StringUtil.center("min median:" + ints[0] + " ms,max:" + ints[BUCKET_SIZE - 1] + " ms", BUCKET_SIZE * 3) + LINE);
        range(BUCKET_SIZE).forEach(x -> table.append(Arrays.asList(result[x]).stream().collect(Collectors.joining()) + LINE));
        return table.toString();
    }

    /**
     * 将数据转化成string数组
     *
     * @param part   数据
     * @param total  基准数据,默认最大的中位数
     * @param length
     * @return
     */
    private static String[] getPercent(int part, int total, int length) {
        int i = part * 8 * length / total;
        int prefix = i / 8;
        int suffix = i % 8;
        String s = getManyString(getPercent(8), prefix) + (prefix == length ? EMPTY : getPercent(suffix) + getManyString(SPACE_1, length - prefix - 1));
        return s.split(EMPTY);
    }


    /**
     * 统一处理压测原始数据保存文件名
     *
     * @param thread
     * @param desc
     * @return
     */
    public static String getFileName(int thread, String desc) {
        return desc + COMMA + thread;
    }

    /**
     * 用于处理初始化concurrent的desc,主要处理后缀
     *
     * @param desc
     * @return
     */
    public static String getFileName(String desc) {
        return desc + Time.markDate();
    }

    /**
     * 用于
     *
     * @param desc
     * @return
     */
    public static String getTrueName(String desc) {
        return desc.replaceAll("\\d{6}$", EMPTY);
    }


}
