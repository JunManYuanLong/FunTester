# 如何验证json数据中任意字段的值
在做接口自动化测试过程中，需要验证很多字段的字段值，因为接口返回的json数据类型多种多样，所以直接getvalue的方法有很多的局限性，上篇文章提到了将json解析为一行一行的数据的方法，可以使用到验证json字段值，具体的方法如下。


```
	/**
	 * 从json数据中获取固定字段的值，如果字段有重复，则去第一个字段值返回
	 * 
	 * @param lines
	 *            传输参数，解析过的响应json数据
	 * @param key
	 *            字段名
	 * @return 返回字段值
	 */
	public String getValueFromJson(List<String> lines, String key) {
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.contains(key + ":")) {
				return deleteCharFromString(key + ":", line);
			}
		}
		return author;
	}
```
下面是将json解析为list的方法：

```
	// 解析json
	public List<String> parseJsonLines(JSONObject response) {
		List<String> jsonLines = new ArrayList<String>();
		String jsonStr = response.toString();// 先将json对象转化为string对象
		jsonStr = jsonStr.replaceAll(",", LINE);
		jsonStr = jsonStr.replaceAll("\\\\/", "/");
		jsonStr = jsonStr.replaceAll("\\{", "");
		jsonStr = jsonStr.replaceAll("\"", "");
		jsonStr = jsonStr.replaceAll("\\}", "");
		jsonStr = jsonStr.replaceAll("\\]", "");
		jsonStr = jsonStr.replaceAll("\\[", "");
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonStr.getBytes());
		InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				jsonLines.add(line);
				output(line);
			}
			bufferedReader.close();
			inputStreamReader.close();
			byteArrayInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonLines;
	}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>