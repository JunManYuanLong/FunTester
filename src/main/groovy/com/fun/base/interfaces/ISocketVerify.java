package com.fun.base.interfaces;

import com.fun.base.bean.VerifyBean;

public interface ISocketVerify extends Runnable {

    /**
     * 初始化消息,某些场景下需要将消息转成固定对象,进行验证,如json
     *
     * @param msg
     */
    public void initMsg(String msg);

    /**
     * 执行一次现有消息的全部验证,是否有匹配
     *
     * @return
     */
    public boolean verify();

    /**
     * 往验证队列中添加verify对象
     *
     * @param bean
     */
    public void addVerify(VerifyBean bean);

    /**
     * 清除verify,验证通过的verify可以从队列中清除
     *
     * @param bean
     */
    public void remoreVerify(VerifyBean bean);


}
