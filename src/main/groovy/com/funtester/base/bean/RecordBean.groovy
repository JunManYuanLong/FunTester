package com.funtester.base.bean

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * 测试记录的bean
 */
class RecordBean extends AbstractBean implements Serializable{

    private static final long serialVersionUID = -159594234325649847L;

    static Logger logger = LogManager.getLogger(RecordBean.class)

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

    static RecordBean get() {
        new RecordBean()
    }

    RecordBean setDomain(String domain) {
        this.domain = domain
        this
    }

    RecordBean setType(String type) {
        this.type = type
        this
    }

    RecordBean setApi(String api) {
        this.api = api
        this
    }

    RecordBean setExpend_time(long expend_time) {
        this.expend_time = expend_time
        this
    }

    RecordBean setData_size(int data_size) {
        this.data_size = data_size
        this
    }

    RecordBean setStatus(int status) {
        this.status = status
        this
    }

    RecordBean setCode(int code) {
        this.code = code
        this
    }

    RecordBean setMethod(String method) {
        this.method = method
        this
    }

    RecordBean setLocal_ip(String local_ip) {
        this.local_ip = local_ip
        this
    }

    RecordBean setLocal_name(String local_name) {
        this.local_name = local_name
        this
    }

    RecordBean setCreate_time(String create_time) {
        this.create_time = create_time
        this
    }

    @Override
    def print() {
        logger.info "接口：{}，响应时间{}", api, expend_time
    }
}