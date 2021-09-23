package com.baizhi.em.util;

import com.baizhi.em.entity.EvalauteData;
import com.baizhi.em.entity.GeoPoint;
import com.baizhi.em.entity.LoginSuccessData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

/**
 * 工具类，对日志进行正则解析处理生成需要的对象的工具类
 */
public class Util {


    /**
     * 解析日志，生成评估数据对象
     * @param log 采集过来的日志信息
     * @return 评估数据对象
     */
    public static EvalauteData parseLog2EvaluateData(String log){

        //正则表达式
        final String regex = "INFO\\s+(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})\\s+([a-z0-9\\u4e00-\\u9fa5]*)\\s+(EVALUATE)\\s+\\[([a-z0-9\\u4e00-\\u9fa5]*)\\]\\s+([a-z0-9]{32})\\s+\\\"([a-z0-9\\.\\-\\,]{6,12})\\\"\\s+([a-z\\u4e00-\\u9fa5]*)\\s+\\\"([0-9\\.\\,]*)\\\"\\s+\\[([0-9\\,\\.]*)\\]\\s\\\"(.*)\\\"";

        //正则表达式封装形成的一个匹配模式
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(log);

        //matcher.find()：如果log能够匹配上正则表达式，就返回true

        if (matcher.find()) {
            //System.out.println("Full match: " + matcher.group(0));//所有匹配了正则表达式的内容；log

            //1.对日志进行处理
            String str = matcher.group(1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long time =0;
            try {
                time = simpleDateFormat.parse(str).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //2.对地理位置进行处理
            String strGeoPoint = matcher.group(8);
            String[] split = strGeoPoint.split(",");
            GeoPoint geoPoint=new GeoPoint(Double.parseDouble(split[0]),Double.parseDouble(split[1]));

            //3.对输入特征进行处理
            String strInputFeatures = matcher.group(9);
            String[] split1 = strInputFeatures.split(",");
            Stream<String> stream = Arrays.stream(split1);// 把数组转换成流对象
            DoubleStream doubleStream = stream.mapToDouble(e -> Double.parseDouble(e));//把stream中的每一个元素转换成double类型
            double[] inputFeatures = doubleStream.toArray();//把流转换成数组


            EvalauteData evalauteData=new EvalauteData(time,matcher.group(2),matcher.group(4),matcher.group(5),matcher.group(6),matcher.group(7),geoPoint,inputFeatures,matcher.group(10));
            return evalauteData;
        }
        return null;
    }

    /**
     * 把日志解析为登录成功对象
     * @param log
     * @return
     */
    public static LoginSuccessData parseLog2LoginSuccessData(String log){
        //正则表达式
        final String regex = "INFO\\s+(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})\\s+([a-z0-9\\u4e00-\\u9fa5]*)\\s+(SUCCESS)\\s+\\[([a-z0-9\\u4e00-\\u9fa5]*)\\]\\s+([a-z0-9]{32})\\s+\\\"([a-z0-9\\.\\-\\,]{6,12})\\\"\\s+([a-z\\u4e00-\\u9fa5]*)\\s+\\\"([0-9\\.\\,]*)\\\"\\s+\\[([0-9\\,\\.]*)\\]\\s\\\"(.*)\\\"";

        //正则表达式封装形成的一个匹配模式
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(log);

        //matcher.find()：如果log能够匹配上正则表达式，就返回true

        if (matcher.find()) {
            //System.out.println("Full match: " + matcher.group(0));//所有匹配了正则表达式的内容；log

            //1.对日志进行处理
            String str = matcher.group(1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long time =0;
            try {
                time = simpleDateFormat.parse(str).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //2.对地理位置进行处理
            String strGeoPoint = matcher.group(8);
            String[] split = strGeoPoint.split(",");
            GeoPoint geoPoint=new GeoPoint(Double.parseDouble(split[0]),Double.parseDouble(split[1]));

            //3.对输入特征进行处理
            String strInputFeatures = matcher.group(9);
            String[] split1 = strInputFeatures.split(",");
            Stream<String> stream = Arrays.stream(split1);// 把数组转换成流对象
            DoubleStream doubleStream = stream.mapToDouble(e -> Double.parseDouble(e));//把stream中的每一个元素转换成double类型
            double[] inputFeatures = doubleStream.toArray();//把流转换成数组


            LoginSuccessData evalauteData=new LoginSuccessData(time,matcher.group(2),matcher.group(4),matcher.group(5),matcher.group(6),matcher.group(7),geoPoint,inputFeatures,matcher.group(10));
            return evalauteData;
        }
        return null;
    }

    //还需要什么方法就添加什么方法

    /**
     * 判断日志是否是评估数据或者登录成功数据
     * @param log 日志信息
     * @return 如果日志是评估数据或者登录成功数据，就返回true；否则就返回false
     */
    public static boolean isEvaluateOrSuccess(String log){

//        System.out.println(log+"util");
        //正则表达式
        final String success_regex = "INFO\\s+(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})\\s+([a-z0-9\\u4e00-\\u9fa5]*)\\s+(SUCCESS)\\s+\\[([a-z0-9\\u4e00-\\u9fa5]*)\\]\\s+([a-z0-9]{32})\\s+\\\"([a-z0-9\\.\\-\\,]{6,12})\\\"\\s+([a-z\\u4e00-\\u9fa5]*)\\s+\\\"([0-9\\.\\,]*)\\\"\\s+\\[([0-9\\,\\.]*)\\]\\s\\\"(.*)\\\"";

        //正则表达式封装形成的一个匹配模式
        final Pattern success_pattern = Pattern.compile(success_regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        final Matcher success_matcher = success_pattern.matcher(log);
        //return success_matcher.find();

        //正则表达式
        final String regex = "INFO\\s+(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})\\s+([a-z0-9\\u4e00-\\u9fa5]*)\\s+(EVALUATE)\\s+\\[([a-z0-9\\u4e00-\\u9fa5]*)\\]\\s+([a-z0-9]{32})\\s+\\\"([a-z0-9\\.\\-\\,]{6,12})\\\"\\s+([a-z\\u4e00-\\u9fa5]*)\\s+\\\"([0-9\\.\\,]*)\\\"\\s+\\[([0-9\\,\\.]*)\\]\\s\\\"(.*)\\\"";

        //正则表达式封装形成的一个匹配模式
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(log);

       //return  matcher.find();
        return success_matcher.find()||matcher.find();

    }


    /**
     * 判断日志是否是评估日志
     * @param log
     * @return
     */
    public static boolean isEvaluate(String log){
        //正则表达式
        final String regex = "INFO\\s+(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})\\s+([a-z0-9\\u4e00-\\u9fa5]*)\\s+(EVALUATE)\\s+\\[([a-z0-9\\u4e00-\\u9fa5]*)\\]\\s+([a-z0-9]{32})\\s+\\\"([a-z0-9\\.\\-\\,]{6,12})\\\"\\s+([a-z\\u4e00-\\u9fa5]*)\\s+\\\"([0-9\\.\\,]*)\\\"\\s+\\[([0-9\\,\\.]*)\\]\\s\\\"(.*)\\\"";

        //正则表达式封装形成的一个匹配模式
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(log);
        return  matcher.find();
    }

    /**
     * 判断日志是否是登录成功的日志
     * @param log
     * @return
     */
    public static boolean isSuccess(String log){
        //正则表达式
        final String success_regex = "INFO\\s+(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})\\s+([a-z0-9\\u4e00-\\u9fa5]*)\\s+(SUCCESS)\\s+\\[([a-z0-9\\u4e00-\\u9fa5]*)\\]\\s+([a-z0-9]{32})\\s+\\\"([a-z0-9\\.\\-\\,]{6,12})\\\"\\s+([a-z\\u4e00-\\u9fa5]*)\\s+\\\"([0-9\\.\\,]*)\\\"\\s+\\[([0-9\\,\\.]*)\\]\\s\\\"(.*)\\\"";

        //正则表达式封装形成的一个匹配模式
        final Pattern success_pattern = Pattern.compile(success_regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        final Matcher success_matcher = success_pattern.matcher(log);
        return success_matcher.find();
    }

    /**
     * 解析日志，获取到应用名和用户名
     * @param log 日志
     * @return 应用名:用户名
     */
    public static String getAppNameAndUsername(String log){
        //正则表达式
        final String success_regex = "INFO\\s+(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})\\s+([a-z0-9\\u4e00-\\u9fa5]*)\\s+(SUCCESS)\\s+\\[([a-z0-9\\u4e00-\\u9fa5]*)\\]\\s+([a-z0-9]{32})\\s+\\\"([a-z0-9\\.\\-\\,]{6,12})\\\"\\s+([a-z\\u4e00-\\u9fa5]*)\\s+\\\"([0-9\\.\\,]*)\\\"\\s+\\[([0-9\\,\\.]*)\\]\\s\\\"(.*)\\\"";

        //正则表达式封装形成的一个匹配模式
        final Pattern success_pattern = Pattern.compile(success_regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        final Matcher success_matcher = success_pattern.matcher(log);
        if(success_matcher.find()){
            return success_matcher.group(2)+":"+success_matcher.group(4);
        }

        //正则表达式
        final String regex = "INFO\\s+(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})\\s+([a-z0-9\\u4e00-\\u9fa5]*)\\s+(EVALUATE)\\s+\\[([a-z0-9\\u4e00-\\u9fa5]*)\\]\\s+([a-z0-9]{32})\\s+\\\"([a-z0-9\\.\\-\\,]{6,12})\\\"\\s+([a-z\\u4e00-\\u9fa5]*)\\s+\\\"([0-9\\.\\,]*)\\\"\\s+\\[([0-9\\,\\.]*)\\]\\s\\\"(.*)\\\"";

        //正则表达式封装形成的一个匹配模式
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(log);
        if(matcher.find()){
            return matcher.group(2)+":"+matcher.group(4);
        }

        return null;
    }

}
