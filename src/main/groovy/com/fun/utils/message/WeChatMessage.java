package com.fun.utils.message;

import com.fun.frame.SourceCode;
import com.fun.base.interfaces.IMessage;

/**
 * 用户微信通知相关功能，不允许频繁多线程调用,暂时没有分级推送机制
 */
public class WeChatMessage extends SourceCode implements IMessage {
    private String content;


    public WeChatMessage(String content) {
        this.content = content;
    }

    /**
     * 发送消息
     */
    public void sendMessage(String name) {
    }

    @Override
    public void sendSystemMessage() {

    }

    @Override
    public void sendFunctionMessage() {

    }

    @Override
    public void sendBusinessMessage() {

    }

    @Override
    public void sendCodeMessage() {

    }

    @Override
    public void sendRemindMessage() {
        sendMessage("X");
    }
}