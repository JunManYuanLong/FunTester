# Mockito框架Mock Void方法



在编写代码时，总是有方法返回`void`，并且在某个测试用例需要模拟`void`方法。那么我们如何去做呢？让我们一起在下面的内容中使用`Mockito`完成这个需求。

* `Mockito`是用于编写单元测试的最著名的模拟框架之一。

# 为什么模拟void方法

假设我们有一个方法**A**，在此方法中，使用了另一个`void`方法**B**。现在，当要为该方法编写测试用例时，我们如何测试**B**方法被调用？另外，是否将正确的参数传递给**B**方法？在这种情况下，`Mockito`可以帮助我们解决这个问题。

让我们举个例子，我们有一个`UserService`类。在此类中，我们有一个`updateName()`方法。


```Java
public UserService{

   public void updateName(Long id, String name){
      userRepository.updateName(id, name);
   }
}
```

现在，我们要为`UserService`类编写单元测试并模拟`userRepository`。但是，在此测试用例中，我们唯一需要验证的是使用正确的参数集调用了`userRepository`中的`updateName()`方法。为此，我们需要模拟`updateName()`方法，捕获参数并验证参数。

这里要注意的最重要的是，我们不能仅仅使用`Mockito`的==when-then==机制来模拟`void`方法。因为，`Mockito`的`when()`方法适用于返回值，而方法返回值是`void`时则不适用。

# 如何在Mockito中模拟void方法

在`Mockito`中，我们可以使用不同的方法来调用实例方法或模拟`void`方法。根据要求使用其中一个选项：

* `doNothing()`：完全忽略对`void`方法的调用，这是默认
* `doAnswer()`：在调用`void`方法时执行一些运行时或复杂的操作
* `doThrow()`：调用模拟的 `void`方法时引发异常
* `doCallRealMethod()`：不要模拟并调用真实方法

## 使用doNothing()

如果我们只想完全忽略`void`方法调用，则可以使用`doNothing()`。

在测试用例中，对于模拟对象的每种方法，`doNothing`是默认行为。因此，如果不想验证参数，则使用`doNothing`是完全可以的。

将`doNothing()`用于`void`方法的`Demo`：


```Java
@Test
public void test001() {
   doNothing().when(mockedUserRepository).updateName(anyLong(),anyString());
 
   userService.updateName(1L,"FunTester");
     
   verify(mockedUserRepository, times(1)).updateName(1L,"FunTester");
}
```

不对空方法使用`doNothing()`：


```Java
@Test
public void test002() {
 
   userService.updateName(1L,"FunTester");
     
   verify(mockedUserRepository, times(1)).updateName(1L,"FunTester");
}

```

使用`doNothing()`进行参数捕获的示例


```Java
@Test
public void testUpdateNameUsingArgumentCaptor() {
 
   ArgumentCaptor<Long> idCapture = ArgumentCaptor.forClass(Long.class);
   ArgumentCaptor<String> nameCapture = ArgumentCaptor.forClass(String.class);
   doNothing().when(mockedUserRepository).updateName(idCapture.capture(),nameCapture.capture());
  
   userService.updateName(1L,"FunTester");
     
   assertEquals(1L, idCapture.getValue());
   
   assertEquals("FunTester", nameCapture.getValue());
}
```


## 将doAnswer()用于void方法

如果我们不想调用真实方法，则需要执行一些运行时操作，请使用`doAnswer()`。

下面是使用`doAnswer()`打印并验证参数的Demo:


```Java
@Test
public void testUpdateNameUsingDoAnswer() {
   doAnswer(invocation -> {
      long id = invocation.getArgument(0);
      String name = invocation.getArgument(1);
      System.out.println("called for id: "+id+" and name: "+name);
 
      assertEquals(1L, id);
      assertEquals("FunTester", name);
 
      return null;
}).when(mockedUserRepository).updateName(anyLong(),anyString());
 
   userService.updateName(1L,"FunTester");
   verify(mockedUserRepository, times(1)).updateName(1L,"FunTester");
}
```


## 使用doThrow()引发异常


如果要在调用方法时引发异常，则可以使用嘲笑的`doThrow()`方法。

让我们举一个例子：当使用`null`作为`id`调用`updateName()`方法时，我们将引发`InvalidParamException`。


```Java
@Test(expected = InvalidParamException.class)
public void testUpdateNameThrowExceptionWhenIdNull() {
   doThrow(new InvalidParamException())
      .when(mockedUserRepository).updateName(null,anyString();
   userService.updateName(null,"FunTester");
}
```


## 使用doCallRealMethod()进行真实方法调用

有时有必要从模拟对象中调用真实方法，在这种情况下，我们需要使用`doCallRealMethod()`，因为`doNothig()`是默认行为。

在以下示例中，即使是模拟对象，也会调用`userRepository`中的真实方法。



```Java
@Test
public void testUpdateNameCallRealRepositoryMethod() {
 
   doCallRealMethod().when(mockedUserRepository).updateName(anyLong(), anyString());
  
   userService.updateName(1L,"真实调用方法");
  
   verify(mockedUserRepository, times(1)).add(1L,"真实调用方法");
}
```

--- 
* 公众号**FunTester**首发，更多原创文章：[FunTester430+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [如何mock固定QPS的接口](https://mp.weixin.qq.com/s/yogj9Fni0KJkyQuKuDYlbA)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [电子书网站爬虫实践](https://mp.weixin.qq.com/s/KGW0dIS5NTLgxyhSjxDiOw)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)