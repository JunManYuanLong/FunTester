# httpclient 爬虫实例——爬取三级中学名

本人在使用 httpclient 的过程中，突然想起来可以爬取一些数据，比如全国的中学名。当然不是空穴来风，之前也做过这方面的爬虫，不过基于selenium 做的 UI 脚本，效率非常慢，而且很不稳定，所以这次采取了接口的形式，果然效率提升了几个档次。一共6万+数据，用了16分钟左右，期间包括数据库的存储。现在分享代码供大家参考。关键信息隐去，大家看一下思路就好了。


```
package practise;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.client.methods.HttpGet;
import net.sf.json.JSONObject;
import source.ApiLibrary;
import source.Concurrent;
 
public class Crawler extends ApiLibrary {
	public static String host = "";
	public static Map<String, Integer> countrys = new HashMap<>();
	public static Map<String, Integer> citys = new HashMap<>();
	public static Map<String, Integer> address = new HashMap<>();
	public static Map<String, Integer> school = new HashMap<>();
	public static List<String> total = new ArrayList<>();
 
	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		crawler.getCountry1();// 省份
		Set<String> countryId = countrys.keySet();
		for (String name : countryId) {
			int id = countrys.get(name);
			crawler.getCountry2(id);// 市
			Set<String> cityId = citys.keySet();
			for (String city : cityId) {
				int cid = citys.get(city);
				crawler.getCountry3(cid);// 县
				Set<String> adresss = address.keySet();
				for (String adres : adresss) {
					int aid = address.get(adres);
					crawler.getCountry4(aid);// 名
					Set<String> schol = school.keySet();
					for (String sch : schol) {
						String line = name + PART + city + PART + adres + PART + sch;
						total.add(line);
					}
				}
			}
		}
		Concurrent.saveRequestTimes(total);
		testOver();
	}
 
	/**
	 * 查询省份
	 */
	public void getCountry1() {
		String url = host + "/user/editinfo/getSchollCountryList";
		HttpGet httpGet = getHttpGet(url);
		// httpGet.addHeader("Cookie", cookies);
		// httpGet.addHeader("User-Agent", userangent);
		JSONObject response = getHttpResponseEntityByJson(httpGet);
		String[] country = response.getString("content").split("</a>");
		int size = country.length;
		for (int i = 0; i < size; i++) {
			String msg = country[i];
			int code = getCode(msg);
			String name = getName(msg);
			countrys.put(name, code);
		}
	}
 
	/**
	 * 查询市
	 * 
	 * @param id
	 */
	public void getCountry2(int id) {
		String url = host + "/user/editinfo/getSchollCityList?region_id=" + id;
		HttpGet httpGet = getHttpGet(url);
		JSONObject response = getHttpResponseEntityByJson(httpGet);
		String[] ssString = response.getString("content").split("</a>");
		int size = ssString.length;
		citys.clear();
		for (int i = 0; i < size; i++) {
			String msg = ssString[i];
			int code = getCode(msg);
			String name = getName(msg);
			citys.put(name, code);
		}
 
	}
 
	/**
	 * 查询县
	 * 
	 * @param id
	 */
	public void getCountry3(int id) {
		String url = host + "/user/editinfo/getSchollAddressList?region_id=" + id;
		HttpGet httpGet = getHttpGet(url);
		JSONObject response = getHttpResponseEntityByJson(httpGet);
		String[] ssString = response.getString("content").split("</a>");
		int size = ssString.length;
		address.clear();
		for (int i = 0; i < size; i++) {
			String msg = ssString[i];
			int code = getCode(msg);
			String name = getName(msg);
			address.put(name, code);
		}
	}
 
	/**
	 * 查询学校
	 * 
	 * @param id
	 */
	public void getCountry4(int id) {
		String url = host + "/user/editinfo/getSchoolNameList?region_id=" + id;
		HttpGet httpGet = getHttpGet(url);
		JSONObject response = getHttpResponseEntityByJson(httpGet);
		String[] ssString = response.getString("content").split("</a>");
		int size = ssString.length;
		school.clear();
		for (int i = 0; i < size; i++) {
			String msg = ssString[i];
			int code = getCode(msg);
			String name = getName(msg);
			school.put(name, code);
		}
	}
 
	/**
	 * 获取 code
	 * 
	 * @param text
	 * @return
	 */
	public int getCode(String text) {
		int code = 0;
		Pattern pattern = Pattern.compile("\"\\d+\"");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			code = changeStringToInt(matcher.group(0).replace("\"", ""));
		}
		return code;
	}
 
	/**
	 * 获取名称
	 * 
	 * @param text
	 * @return
	 */
	public String getName(String text) {
		String name = text.substring(text.lastIndexOf(">") + 1, text.length());
		return name;
	}
 
}
```
下面是爬取到数据截图

![](/blog/pic/20180206181404174.png)

### 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
- [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
- [python plotly处理接口性能测试数据方法封装](https://mp.weixin.qq.com/s/NxVdvYlD7PheNCv8AMYqhg)
- [单点登录性能测试方案](https://mp.weixin.qq.com/s/sv8FnvIq44dFEq63LpOD2A)



### 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
- [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
- [测试之《代码不朽》脑图](https://mp.weixin.qq.com/s/2aGLK3knUiiSoex-kmi0GA)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [优秀软件开发人员的态度](https://mp.weixin.qq.com/s/0uEEeFaR27aTlyp-sm61bA)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [自动化测试解决了什么问题](https://mp.weixin.qq.com/s/96k2I_OBHayliYGs2xo6OA)
- [17种软件测试人员常用的高效技能-上](https://mp.weixin.qq.com/s/vrM_LxQMgTSdJxaPnD_CqQ)
- [17种软件测试人员常用的高效技能-下](https://mp.weixin.qq.com/s/uyWdVm74TYKb62eIRKL7nQ)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [编写测试用例的17个技巧](https://mp.weixin.qq.com/s/2OPKYEQkl3o1M9fenF-uMA)


### 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)

