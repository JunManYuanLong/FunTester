# 使用WireMock进行更好的集成测试



无论您是遵循传统的测试金字塔还是采用诸如“测试蜂窝”这样的较新方法，都应该在开发过程中的某个时候开始编写集成测试用例。
您可以编写不同类型的集成测试。从持久性测试开始，您可以检查组件之间的交互，也可以模拟调用外部服务。本文将讨论后一种情况。
在谈论WireMock之前，让我们从一个典型的例子开始。

## ChuckNorrisService

我们有一个简单的API，用于手动测试。在“业务”类意外是，它可以调用外部API。它使用Spring 框架提供功能的。没什么特别的。我多次看到的是模拟RestTemplate并返回一些预先确定的答案的测试。该实现可能如下所示：

```
@Service
public class ChuckNorrisService{
...
  public ChuckNorrisFact retrieveFact() {
    ResponseEntity<ChuckNorrisFactResponse> response = restTemplate.getForEntity(url, ChuckNorrisFactResponse.class);
    return Optional.ofNullable(response.getBody()).map(ChuckNorrisFactResponse::getFact).orElse(BACKUP_FACT);
  }
 ...
 }
```

在检查成功案例的常规单元测试旁边，将至少有一项覆盖HTTP错误码的测试用例，即4xx或5xx状态代码：

```
@Test
  public void shouldReturnBackupFactInCaseOfError() {
    String url = "http://localhost:8080";
    RestTemplate mockTemplate = mock(RestTemplate.class);
    ResponseEntity<ChuckNorrisFactResponse> responseEntity = new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    when(mockTemplate.getForEntity(url, ChuckNorrisFactResponse.class)).thenReturn(responseEntity);
    ChuckNorrisService service = new ChuckNorrisService(mockTemplate, url);
    ChuckNorrisFact retrieved = service.retrieveFact();
    assertThat(retrieved).isEqualTo(ChuckNorrisService.BACKUP_FACT);
  }
```
看起来还不错吧？响应实体返回503错误代码，我们的服务不会崩溃。所有测试都是绿色通过的，我们可以部署我们的应用程序。
不幸的是，Spring的RestTemplate不能这样使用。方法签名getForEntity给了我们很小的提示。它指出throws RestClientException。这就是mock的RestTemplate与实际实现不同的地方。我们将永远不会收到ResponseEntity带有4xx或5xx状态代码的。RestTemplate将抛出的子类RestClientException。通过查看类的层次结构，我们可以对可能抛出的结果有一个很好的印象：

![](http://pic.automancloud.com/classHierarchy.png)

因此，让我们看看如何使这项测试更好。

## WireMock进行拯救

WireMock通过启动模拟服务器并返回将其配置为返回的答案来模拟Web服务。得益于出色的DSL，它很容易集成到您的测试中，并且模拟请求也很简单。

对于JUnit 4，有一个WireMockRule有助于启动停止服务器的工具。对于JUnit 5，大概需要自己做一个这样的工具。当您检查示例项目时，您可以找到ChuckNorrisServiceIntegrationTest。这是基于JUnit 4的SpringBoot测试。让我们看一下。

最重要的部分是ClassRule：

```
@ClassRule
  public static WireMockRule wireMockRule = new WireMockRule();
```
如前所述，这将启动和停止WireMock服务器。您也可以像往常一样使用该规则Rule来启动和停止每个测试的服务器。对于我们的测试，这不是必需的。

接下来，您将看到几种configureWireMockFor...方法。这些包含WireMock何时返回答案的说明。将WireMock配置分为几种方法并从测试中调用它们是我使用WireMock的方法。当然，您可以在一个@Before方法中设置所有可能的请求。对于正确使用的Demo，我们这样做：


```
public void configureWireMockForOkResponse(ChuckNorrisFact fact) throws JsonProcessingException {
    ChuckNorrisFactResponse chuckNorrisFactResponse = new ChuckNorrisFactResponse("success", fact);
    stubFor(get(urlEqualTo("/jokes/random"))
        .willReturn(okJson(OBJECT_MAPPER.writeValueAsString(chuckNorrisFactResponse))));
  }
```
所有方法都是从静态导入的`com.github.tomakehurst.wiremock.client.WireMock`。如您所见，我们将HTTP GET存入路径`/jokes/random`并返回JSON对象。该`okJson()`方法只是带有JSON内容的200响应的简写。对于错误情况，代码甚至更简单：


```
private void configureWireMockForErrorResponse() {
    stubFor(get(urlEqualTo("/jokes/random"))
        .willReturn(serverError()));
  }
```
如您所见，DSL使阅读说明变得容易。将WireMock放置在适当的位置，我们可以看到我们先前的实现不起作用，因为RestTemplate引发了异常。因此，我们必须调整代码：


```
public ChuckNorrisFact retrieveFact() {
    try {
      ResponseEntity<ChuckNorrisFactResponse> response = restTemplate.getForEntity(url, ChuckNorrisFactResponse.class);
      return Optional.ofNullable(response.getBody()).map(ChuckNorrisFactResponse::getFact).orElse(BACKUP_FACT);
    } catch (HttpStatusCodeException e){
      return BACKUP_FACT;
    }
  }

```

这已经涵盖了WireMock的基本用例。配置请求的答案，执行测试，检查结果，so easy，就这么简单。尽管如此，在云环境中运行测试时通常会遇到一个问题。让我们看看我们能做什么。

## 动态端口上的WireMock
您可能已经注意到，项目中的集成测试包含一个
`ApplicationContextInitializer`类，并且其`@TestPropertySource`注释会覆盖实际API的URL。那是因为我想在随机端口上启动WireMock。当然，您可以为WireMock配置一个固定端口，并在测试中将此端口用作常量来处理。但是，如果您的测试在某些云提供商的基础架构上运行，则无法确定该端口是否可用。因此，我认为随机端口更好。

不过，在Spring应用程序中使用属性时，我们必须以某种方式将随机端口传递给我们的服务。或者，如您在示例中看到的那样，覆盖URL。这就是为什么我们使用`ApplicationContextInitializer`。我们将动态分配的端口添加到应用程序上下文中，然后可以使用属性来引用它`${wiremock.port}`。这里唯一的缺点是我们现在必须使用`ClassRule`。否则，我们无法在初始化Spring应用程序之前访问端口。

解决了此问题后，让我们看一下涉及HTTP调用的一个常见问题。

## 超时时间

WireMock提供了更多的响应可能性，而不仅仅是对GET请求的简单答复。经常被遗忘的另一个测试案例是测试超时。开发人员往往会忘记在`RestTemplate`设置超时`URLConnections`。如果没有超时，则两者都将等待无限量的时间来进行响应。在最好的情况下，在最坏的情况下，所有线程都将等待永远不会到达的响应。

因此，我们应该添加一个模拟超时的测试。当然，我们也可以使用Mockito模拟来创建延迟，但是在这种情况下，我们将再次猜测RestTemplate的行为。使用WireMock模拟延迟非常简单：


```
private void configureWireMockForSlowResponse() throws JsonProcessingException {
    ChuckNorrisFactResponse chuckNorrisFactResponse = new ChuckNorrisFactResponse("success", new ChuckNorrisFact(1L, ""));
    stubFor(get(urlEqualTo("/jokes/random"))
        .willReturn(
            okJson(OBJECT_MAPPER.writeValueAsString(chuckNorrisFactResponse))
                .withFixedDelay((int) Duration.ofSeconds(10L).toMillis())));
  }
```

`withFixedDelay()`期望一个表示毫秒的int值。我更喜欢使用`Duration`或至少一个表示该参数表示毫秒的常量，而不必每次写代码都需要看一下代码注释。

设置超时`RestTemplate`并添加响应的测试后，我们可以看到`RestTemplate`抛出`ResourceAccessException`。因此，我们可以调整catch块以捕获此异常和，`HttpStatusCodeException`或者仅捕获两者的超类：

```
public ChuckNorrisFact retrieveFact() {
    try {
      ResponseEntity<ChuckNorrisFactResponse> response = restTemplate.getForEntity(url, ChuckNorrisFactResponse.class);
      return Optional.ofNullable(response.getBody()).map(ChuckNorrisFactResponse::getFact).orElse(BACKUP_FACT);
    } catch (RestClientException e){
      return BACKUP_FACT;
    }
  }
```

现在，我们已经很好地介绍了执行HTTP请求时最常见的情况，并且可以确定我们正在测试接近真实条件的条件。

## 为什么不？
HTTP集成测试的另一个选择是`Hoverfly`。它的工作原理类似于`WireMock`，但我更喜欢后者。原因是在运行包含浏览器的端到端测试时，WireMock也非常有用。`Hoverfly`（至少是Java库）受JVM代理的限制。这可能使它比`WireMock`更快，但是当例如某些`JavaScript`代码开始起作用时，它根本不起作用。当您的浏览器代码也直接调用其他一些服务时，WireMock启动Web服务器这一功能非常有用。然后，您也可以使用WireMock来mock它们，并编写例如Selenium测试。

# 结论

本文可以向您展示两件事：
* 集成测试的重要性
* WireMock是个非常不错的测试框架

当然，这两个主题都可以写出非常多的文章。尽管如此，还是分享了如何使用WireMock及其功能。在以后的学习路上多去阅读他们的文档，然后尝试更多其他功能，例如利用WireMock来进行身份验证。

---
* **郑重声明**：文章禁止第三方（腾讯云除外）转载、发表，事情原委[测试窝，首页抄我七篇原创还拉黑，你们的良心不会痛吗？](https://mp.weixin.qq.com/s/ke5avkknkDMCLMAOGT7wiQ)

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [JUnit中用于Selenium测试的中实践](https://mp.weixin.qq.com/s/KG4sltQMCfH2MGXkRdtnwA)

## 非技术文章精选

- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [自动化和手动测试，保持平衡！](https://mp.weixin.qq.com/s/mMr_4C98W_FOkks2i2TiCg)
- [自动化测试生命周期](https://mp.weixin.qq.com/s/SH-vb2RagYQ3sfCY8QM5ew)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)