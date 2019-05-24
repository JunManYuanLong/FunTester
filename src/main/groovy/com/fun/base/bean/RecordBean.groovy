package com.fun.base.bean

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 测试记录的bean
 */
public class RecordBean extends BaseBean {

    private static Logger logger = LoggerFactory.getLogger(RecordBean.class)

    String domain;

    String type;

    String api;

    long expend_time;

    int data_size;

    int status;

    int code;

    String method;

    String local_ip;

    String local_name;

    String create_time;

    @Override
    def print() {
        logger.info("接口：{}，响应时间{}", api, expend_time)
    }
}