package com.baizhi.em.update;

import com.baizhi.em.entity.HistoryData;
import com.baizhi.em.entity.LoginSuccessData;

public abstract class Update {

    /**
     * 更新操作：更新数据、功能执行完毕交给链条（放行）
     * @param loginSuccessData
     * @param historyData
     * @param chain
     */
    public abstract void update(LoginSuccessData loginSuccessData, HistoryData historyData,UpdateChain chain);
}
