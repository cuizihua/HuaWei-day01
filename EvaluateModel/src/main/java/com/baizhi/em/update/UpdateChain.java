package com.baizhi.em.update;

import com.baizhi.em.entity.HistoryData;
import com.baizhi.em.entity.LoginSuccessData;

import java.util.List;

/**
 * 更新链
 */
public class UpdateChain {

    private List<Update> list;

    public UpdateChain(List<Update> list) {
        this.list = list;
    }
    private int position;

    /**
     * 由更新链负责驱动下一个更新的执行
     * @param loginSuccessData
     * @param historyData
     */
    public void doUpdate(LoginSuccessData loginSuccessData,HistoryData historyData){

        if(position<list.size()){
            Update update = list.get(position);
            position++;
            update.update(loginSuccessData, historyData,this);
        }
    }
}
