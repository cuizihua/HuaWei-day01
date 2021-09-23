import com.baizhi.em.entity.*;
import com.baizhi.em.evaluate.*;
import com.baizhi.em.update.*;
import com.baizhi.em.util.Util;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {
    private HistoryData historyData=new HistoryData();

    @Before
//    @org.junit.Test
    public void update(){
//        System.out.println("update");
        //在这个方法里面，把登录成功的数据，写入到历史输入里面

        String logs[]={
                "INFO 2020-06-19 10:23:26 WebApplication SUCCESS [zhangsan] 119e76e86bea42e9a098c2461a6cc314 \"123456\" Zhengzhou \"113.65,34.76\" [1500,2800,3300] \"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36\"",
                "INFO 2020-06-19 10:26:26 WebApplication SUCCESS [zhangsan] 119e76e86bea42e9a098c2461a6dc314 \"123456\" Zhengzhou \"113.65,34.76\" [1600,2700,3000] \"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36\"",
                "INFO 2020-06-19 10:43:26 WebApplication SUCCESS [zhangsan] 119e76e86bea42e9a098c2461a6ac314 \"123456\" Zhengzhou \"113.65,34.76\" [1700,2600,3100] \"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36\""
        };
        for (String log : logs) {
            LoginSuccessData loginSuccessData = Util.parseLog2LoginSuccessData(log);

            //走更新链把登录成功的数据存入到历史数据中
            Update update1=new DeviceUpdate(10);
            Update update2=new CityUpdate();
            Update update3=new InputFeaturesUpdate(10);
            Update update4=new PasswordUpdate();
            Update update5=new TimeSlotUpdate();

            List<Update> list=new ArrayList<>();
            list.add(update1);
            list.add(update2);
            list.add(update3);
            list.add(update4);
            list.add(update5);

            UpdateChain chain=new UpdateChain(list);
            chain.doUpdate(loginSuccessData,historyData);

            historyData.setLastLoginTime(loginSuccessData.getTime());
            historyData.setLastLoginGeoPoint(loginSuccessData.getGeoPoint());
        }
//        historyData.setCurrentDayLoginCount(1);

        System.out.println(historyData);
    }

    @org.junit.Test
    public void evaluate(){
//        System.out.println("pingg");
        //根据历史数据、评估数据、评估报告（空：只有基础信息），走评估链生成完成的评估报告

        String log="INFO 2020-06-29 10:43:27 WebApplication EVALUATE [zhangsan] 119e76e86bea42e9a098c2461a3b9314 \"abcdef\" sh \"121.23,31.07\" [15,28,31] \"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36\"";


        EvalauteData evalauteData = Util.parseLog2EvaluateData(log);
        EvaluateReport evaluateReport=new EvaluateReport(evalauteData.getAppName(),evalauteData.getUsername(),evalauteData.getUuid(),evalauteData.getTime(),evalauteData.getCity(),evalauteData.getGeoPoint());

        Evaluate evaluate1=new CityEvaluate();
        Evaluate evaluate2=new InputFeatureEvaluate();
        Evaluate evaluate3=new SimilirtyEvaluate(0.95);
        Evaluate evaluate4=new SpeedEvaluate(750);
        Evaluate evaluate5=new TimeslotEvaluate(2);
        Evaluate evaluate6=new TotalCountEvaluate(10);
//        Evaluate evaluate7=new CityEvaluate();

        List<Evaluate> list = new ArrayList<>();
        list.add(evaluate1);
        list.add(evaluate2);
        list.add(evaluate3);
        list.add(evaluate4);
        list.add(evaluate5);
//        list.add(evaluate6);

        EvaluateChain chain = new EvaluateChain(list);
        chain.doEvaluate(historyData,evalauteData,evaluateReport);

        System.out.println(evaluateReport);


    }

    @org.junit.Test
    public void test(){
        List list=new ArrayList();
        list.add(1);
        list.add(5);
        list.add(3);
        list.add(8);
        System.out.println(list);//1，5，3，8

        Collections.shuffle(list);//对list进行shuffle：把list中的元素打乱

        System.out.println(list);//8, 3, 5, 1/3, 5, 1, 8/5, 1, 8, 3

    }

}
