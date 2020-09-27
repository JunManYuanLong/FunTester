# 使用httppost请求发送form表单的方法
本人在使用httpclient做接口自动化的过程中，遇到了post请求提交数据是form表单，因为我从数据库读取到的case是map形式的，所以经常尝试和验证，写了一个方法把map里面的数据转化为form表单，然后使用post请求发送数据。下面是转化和设置方法，分享出来，供大家参考。


```
	/**
	 * 设置post接口上传表单
	 * 
	 * @param httpPost
	 *            post请求
	 * @param apiCase
	 *            传入的参数map
	 */
	public void setFormHttpEntity(HttpPost httpPost, Map<String, String> apiCase) {
		Set<String> keys = apiCase.keySet();
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (String key : keys) {
			formparams.add(new BasicNameValuePair(key, apiCase.get(key)));
		}
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			output("form表单错误！");
			e.printStackTrace();
		}
		httpPost.setEntity(entity);
	}
```
> 这里可能会遇到中文所以统一用了urlencodeformentity这个类型的实体。

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>