package com.funtester.base.interfaces;

public interface IMessage {
    /**
     * 发送系统异常
     */
    public void sendSystemMessage();

    /**
     * 发送功能异常
     */
    public void sendFunctionMessage();

    /**
     * 发送业务异常
     */
    public void sendBusinessMessage();

    /**
     * 发送程序异常
     */
    public void sendCodeMessage();

    /**
     * 提醒推送
     */
    public void sendRemindMessage();
}
