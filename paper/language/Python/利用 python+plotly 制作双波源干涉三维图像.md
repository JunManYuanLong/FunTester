# 利用 python+plotly 制作双波源干涉三维图像



本人在学习完制作双波源干涉现象的的二维Contour Plots图像之后，发现 plotly 还有3D 图像制作，也就是3D Surface Plots，这个更能展示双波源干涉现象的结果，果然学之。中间有些地方要说明一下，3D Surface Plots图表默认的底部是正方形，所以我采用了100*100的干涉图，然后加上一层透明的图标，让图像压扁，不然图标的上下限就是波动位置，看起来非常不雅观。

下面分享代码和结果：

下面是 python部分的代码：
```
#!/usr/bin/python
# coding=utf-8
 
import plotly.plotly
 
z = []
with open("/Users/Vicky/Documents/workspace/fission/long/intervene.log") as apidata:
    for i in apidata:
        data = i.split("\n")[0].split(",")
        z.append(data)
matrix = [[20 for zij in zi] for zi in z]#为了让立体图压扁
plotly.offline.plot([
     dict(
            z=z,
            type="surface"
    ),
    dict(
            z=matrix,
            showscale=False,
            opacity=0.01,#透明度
            type="surface"
    )
], filename="2222.html")
```
下面是 java 部分的代码，是为了生成数据：


```
package practise;
 
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import source.SourceCode;
 
public class Intervene extends SourceCode {
	public List<List<Double>> data = new ArrayList<>();
 
	public static void main(String[] args) {
		Intervene intervene = new Intervene();
		intervene.testDemo001();
	}
 
	public void testDemo001() {
		Point point1 = new Point(25, 25);
		Point point2 = new Point(75, 75);
		int lamda = 6;
		for (int i = 0; i < 100; i++) {// y 轴
			List<Double> distance = new ArrayList<>();
			for (int j = 0; j < 100; j++) {// x 轴
				Point point = new Point(j, i);
				double x = point.distance(point1) % lamda / lamda;
				double y = point.distance(point2) % lamda / lamda;
				double xx = Math.sin(x * 2 * Math.PI);
				double yy = Math.sin(y * 2 * Math.PI);
				distance.add(xx + yy);
			}
			data.add(distance);
		}
		StringBuffer content = new StringBuffer();
		int size = data.size();
		for (int i = 0; i < size; i++) {
			String text = data.get(i).toString();
			text = text.substring(1, text.length() - 1);
			if (i == 0)
				content.append(text);
			content.append(LINE + text);
		}
		logLong("intervene.log", content.toString());
	}
}
```
下面是3D Surface Plots图的截图：
![](http://pic.automancloud.com/20180306092335285.png)
![](http://pic.automancloud.com/2018030609234447.png)

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
- [单点登录性能测试方案](https://mp.weixin.qq.com/s/sv8FnvIq44dFEq63LpOD2A)

### 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
- [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)

### 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)


# [点击查看公众号地图](https://mp.weixin.qq.com/s/l_zkWzQL65OIQOjKIvdG-Q)