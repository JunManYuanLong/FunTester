# “双花”BUG的测试分享



“双花”一词我是从区块链领域的听到的，查了一下资料，基本所有的引用都是基于区块链，但是今天所讲的“双花”不是区块链领域，而是普通的接口测试中遇到的BUG，由于概念一致，所以采用“双花”一词。
双花，顾名思义，花了两次，一分钱或者交换流通的物品。下面分享一下自己在工作中遇到的一个双花的BUG的测试方案和原因解释。

场景：有一个兑换活动，大概金币兑换礼物，金币是整个平台流通的货币，礼物价格不等。用户登录活动页后，选择不同的礼物输入数量，点击兑换。
接口：活动接口两个：一、获取活动详情以及礼物详情；二、兑换一定数量礼物。兑换记录和消费记录以及个人物品都是老接口，不再赘述。
测试工具：Java（不唯一），把接口提供的功能封装为方法，然后通过多线程调用封装号的方法，完成多线程请求兑换接口。

解决方案：
在常规测试场景以外，利用多线程并发去测试双花BUG。主要利用了写好的性能测试框架去并发去发送某一个httprequestbase对象，通过构造对应的测试数据，检查测试完成后的测试数据，对比发现是否存在双花的BUG。
用户A，设置用户余额100,000，兑换价值100的礼物，并发1,010次。最终结果，用户余额为零，兑换的1,000个改礼物，各种记录正常。最后10次响应结果为用户余额不足。

在兑换接口中，业务逻辑如下：获取用户余额，判断是否足以支付礼品总价，（大于等于时），发起扣币以及记录相关封装模块功能。

BUG描述：在完成测试时，用户获取到的礼物数量大于1000，余额为零。最后10次请求，有一些是响应成功的。

BUG复盘，在获取完用户余额和判断完总价之后，发起扣费等业务时，并没有重新校验用户余额（或者说改过程是非原子操作不安全），这样导致了最后扣费的时候，使用的用户余额是旧的数值，其他线程也尚未完成扣费，造成了用户的一份金币，被当做两份金币消费了，也就是双花。

下面是测试代码，主要用到了自己写的测试框架，把HttpRequestBase对象组装好之后丢到trhead对象里面，设置请求次数和线程数。

```
package com.fission.najm.activity.before.workPractise;

import com.fission.najm.base.NajmBase;
import com.fun.frame.excute.Concurrent;
import com.fun.frame.thead.RequestThread;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

public class Exchange extends NajmBase {
	public String loginKey;
	public String exchangeCode = "";
	public int balance;
	public int coin;
	public HttpRequestBase Rechargerequest;
	public HttpRequestBase exchangeRequest;


	public static void main(String[] args) {
		NajmBase base = new NajmBase();
		Exchange exchange = new Exchange(base);
		exchange.recharge();
		RequestThread requestThread = new RequestThread(exchangeRequest, 101);
		new Concurrent(requestThread,10).start();
		allOver();
	}

	/**
	 * 充值
	 *
	 * @return
	 */
	public JSONObject recharge() {
		JSONObject response = null;
		String url = "http://www.7najm.com/cash/exchangecrecharge";
		JSONObject params = new JSONObject();
		params.put("loginKey", loginKey);
		params.put("exchangeCode", exchangeCode);
		params.put("requestType", "We");
		Rechargerequest = getHttpPost(url, params);
		//response = getHttpResponseEntityByJson(Rechargerequest);
		//output(response);
		return response;
	}

	/**
	 * 获取充值记录
	 *
	 * @return
	 */
	public JSONObject getRechargeRecord() {
		JSONObject response = null;
		String url = "http://www.7najm.com/cash/getecrrecord";
		JSONObject args = new JSONObject();
		args.put("loginKey", loginKey);
		args.put("page", 1);
		args.put("pageSize", 10);
		args.put("requestType", "Web");
		HttpGet httpGet = getHttpGet(url, args);
		response = getHttpResponseEntityByJson(httpGet);
		output(response);
		return response;
	}

	/**
	 * 获取渠道商余额
	 *
	 * @return
	 */
	public JSONObject getBalance() {
		JSONObject response = null;
		String url = "http://www.7najm.com/cash/exchangebalance";
		JSONObject args = new JSONObject();
		args.put("loginKey", loginKey);
		args.put("requestType", "Web");
		HttpGet httpGet = getHttpGet(url, args);
		response = getHttpResponseEntityByJson(httpGet);
		if (response.containsKey("dataInfo"))
			balance = response.getInt("dataInfo");
		output(response);
		return response;
	}

	/**
	 * 获取充值码
	 *
	 * @return
	 */
	public JSONObject getRechargeCode() {
		JSONObject response = null;
		String url = "http://www.7najm.com/cash/getexchangecode";
		JSONObject params = new JSONObject();
		params.put("loginKey", loginKey);
		params.put("requestType", "0");
		params.put("balance", coin);
		exchangeRequest = getHttpPost(url, params);
		response = getHttpResponseEntityByJson(exchangeRequest);
		if (response.containsKey("dataInfo")) {
			exchangeCode = response.getJSONObject("dataInfo").getString("exchangeCode");
		}
		output(response);
		return response;
	}

	/**
	 * 获取充值码列表
	 *
	 * @return
	 */
	public JSONObject getCodeRecord() {
		JSONObject response = null;
		String url = "http://www.7najm.com/cash/getecrecord";
		JSONObject args = new JSONObject();
		args.put("loginKey", loginKey);
		args.put("page", 1);
		args.put("pageSize", 20);
		args.put("requestType", "Web");
		args.put("codeType", 1);
		HttpGet httpGet = getHttpGet(url, args);
		response = getHttpResponseEntityByJson(httpGet);
		output(response);
		return response;
	}
}

```

### 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [测试之JVM命令脑图](https://mp.weixin.qq.com/s/qprqyv0j3SCvGw1HMjbaMQ)
9. [将json数据格式化输出到控制台](https://mp.weixin.qq.com/s/2IPwvh-33Ov2jBh0_L8shA)
10. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
11. [“双花”BUG的测试分享](https://mp.weixin.qq.com/s/0dsBsssNfg-seJ_tu9zFaQ)

