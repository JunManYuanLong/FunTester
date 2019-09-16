package com.fun.base.constaint;

import com.fun.db.mysql.MySqlTest;
import com.fun.frame.SourceCode;
import net.sf.json.JSONObject;

/**
 * 用例虚拟类
 */
public abstract class CaseBase extends SourceCode {

    /**
     * 保存测试用例的执行结果
     *
     * @param label  测试用例的标签
     * @param result 测试用例结果
     */
    public void saveResult(String label, JSONObject result) {
        MySqlTest.saveTestResult(label, result);
    }

    /**
     * 前置处理
     *
     * @return
     */
    public abstract boolean before();

    /**
     * 后置处理
     *
     * @return
     */
    public abstract boolean after();

    /**
     * 初始化
     *
     * @return
     */
    public abstract boolean init();


}
