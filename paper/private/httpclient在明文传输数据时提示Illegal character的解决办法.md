# httpclient在明文传输数据时提示Illegal character的解决办法
本人在用httpclient做接口测试的时候，遇到有些字段需要明文传输的一些特殊数据，在写用例的时候，考虑了一下空格和特殊字符，结果提示：
Illegal character in query at index 150: ，在使用fiddler查看接口信息的时候发现，原来信息已经做了加密，写了一个方法，解决了空格和特殊字符提示错误的问题。分享一下，供大家参考。
之前的传参代码：jsonObject.put("password_orig", word);
修改之后的传参代码：jsonObject.put("password_orig", urlEncoderText(word));
其中urlEncoderText()方法代码如下：
多写了一个解码的。


```
	//url进行转码
	public String urlEncoderText(String text) throws UnsupportedEncodingException {
		return java.net.URLEncoder.encode(text, "utf-8");
	}
	//url进行解码
	public String urlDecoderText(String text) throws UnsupportedEncodingException {
		return java.net.URLDecoder.decode(text, "utf-8");
		}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>