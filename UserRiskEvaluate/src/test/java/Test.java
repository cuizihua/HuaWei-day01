import com.baizhi.em.entity.EvalauteData;
import com.baizhi.em.entity.EvaluateReport;
import com.baizhi.em.entity.RiskFactor;
import com.baizhi.em.util.Util;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws JsonProcessingException {
        /*Map risk = new HashMap();
        //7个评估因子的初始化
        risk.put(RiskFactor.AREA,false);
        risk.put(RiskFactor.DEVICE,false);
        risk.put(RiskFactor.INPUTFEATURE,false);
        risk.put(RiskFactor.SIMILARITY,false);
        risk.put(RiskFactor.TIMESLOT,false);
        risk.put(RiskFactor.TOTAL,false);
        risk.put(RiskFactor.SPEED,false);

        String str = new ObjectMapper().writeValueAsString(risk);
        System.out.println(str);*/


        String log="INFO 2020-06-19 10:58:16 WebApplication EVALUATE [zhangsan] 119e76e86bea42e9a098c2461a6b9314 \"123456\" zz \"113.65,34.76\" [1500,2800,3100] \"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWesssbKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36\"";
         EvalauteData evalauteData = Util.parseLog2EvaluateData(log);
        EvaluateReport evaluateReport = new EvaluateReport(evalauteData.getAppName(), evalauteData.getUsername(), evalauteData.getUuid(), evalauteData.getTime(), evalauteData.getCity(), evalauteData.getGeoPoint());

        String str = new ObjectMapper().writeValueAsString(evaluateReport);
        System.out.println(str);


    }
}
