# 使用 httpclient 上传文件方法的封装
本人使用 httpclient 进行接口测试的过程中，遇到了上传文件的接口，之前的文章已经完成了二进制流上传图片的代码，但是还没有封装成固定的使用方法，今天分享一下封装后的方法，供大家参考。


```
/**
	 * 设置二进制流实体，params 里面参数值为 file
	 * 
	 * @param httpPost
	 *            httpPsot 请求
	 * @param params
	 *            请求参数
	 * @param file
	 *            文件
	 */
	public void setMultipartEntityEntity(HttpPost httpPost, JSONObject params, File file) {
		String fileName = getFileName(file);
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Iterator<String> keys = params.keys();// 遍历 params 参数和值
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();// 新建builder对象
		while (keys.hasNext()) {
			String key = keys.next();
			String value = params.getString(key);
			if (value.equals("file")) {
				builder.addBinaryBody(key, inputStream, ContentType.create("multipart/form-data"), fileName);// 设置流参数
			} else {
				StringBody body = new StringBody(value, ContentType.create("text/plain", Consts.UTF_8));// 设置普通参数
				builder.addPart(key, body);
			}
		}
		HttpEntity entity = builder.build();// 生成entity
		httpPost.setEntity(entity);// 设置 entity
	}
```
> 此方法仅针对 Linux 系统，因为 Windows 系统在文件路径中用的“\”，在代码里是“\\”所以 Windows 系统的朋友得注意力。

### 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [python plotly处理接口性能测试数据方法封装](https://mp.weixin.qq.com/s/NxVdvYlD7PheNCv8AMYqhg)

### 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
- [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [17种软件测试人员常用的高效技能-上](https://mp.weixin.qq.com/s/vrM_LxQMgTSdJxaPnD_CqQ)
- [17种软件测试人员常用的高效技能-下](https://mp.weixin.qq.com/s/uyWdVm74TYKb62eIRKL7nQ)

### 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
