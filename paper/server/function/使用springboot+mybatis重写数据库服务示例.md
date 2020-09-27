# 使用springboot+mybatis重写数据库服务示例


在之前自己写过的接口测试框架中，使用了MySQL记录了各种请求响应以及用例等等的信息，为了提高存储速度，我单独写了一个数据库的存储的服务，部署在内网的服务器上。当有需要记录的信息时，直接把信息发送到这个服务的固定接口中，实现了数据库的异步存储。在学习了springboot和mybatis框架之后，觉得使用mybatis再写一遍这个功能。由于保留了之前的服务的代码，下面只分享一下新的功能的实现代码。

下面是springboot启动类的代码：

```
package com.fun;
 
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
 
@Configuration
@MapperScan("com.fun.dao")//扫描包下面接口
@SpringBootApplication(exclude ={MongoAutoConfiguration.class})
public class ApiTestMysqlserviceApplication {
 
	public static void main(String[] args) {
		SpringApplication.run(ApiTestMysqlserviceApplication.class, args);
		System.out.println("-----------------------------------start-----------------------------------");
	}
}

```
下面是userDao和userService的相关代码：


```
package com.fun.dao;
 
 
import com.fun.model.RequestBean;
 
public interface UserDao {
 
    int insertRequest(RequestBean requestBean);
}

```


```
package com.fun.user.impl;
 
import com.fun.dao.UserDao;
import com.fun.model.RequestBean;
import com.fun.model.UserDomain;
import com.fun.user.UserService;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service(value = "userService")
public class UserServiceImpl implements UserService {
 
    @Override
    public int insertRequest(RequestBean requestBean) {
        return userDao.insertRequest(requestBean);
    }
 
}

```
```
package com.fun.user;
 
import com.fun.model.RequestBean;
import com.fun.model.UserDomain;
 
public interface UserService {
 
    int insertRequest(RequestBean requestBean);
}

```

```
    @PostMapping("/test")
    @ResponseBody
    public ResultUtil test( RequestBean requestBean) {
        logger.info(requestBean.toString());
        int i = userService.insertRequest(requestBean);
        return ResultUtil.build(i);
    }

```
下面是mapper.xml的配置：


```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.fun.dao.UserDao">
    <sql id="REQUEST_TABLE">
        request
    </sql>
 
    <insert id="insertRequest" parameterType="com.fun.model.RequestBean">
        INSERT INTO
        <include refid="REQUEST_TABLE"/>
        <trim prefix="(" suffix=")" suffixOverrides=",">
            domain,api,type,expend_time,data_size,status,code,method,local_ip,local_name,create_time,
        </trim>
        <trim prefix="VALUES(" suffix=")" suffixOverrides=",">
            #{domain},#{api},#{type},#{expend_time},#{data_size},#{status},#{code},#{method},#{local_ip},#{local_name},#{create_time}
        </trim>
    </insert>
</mapper>

```
下面是项目的property配置：


```
spring.datasource.url=jdbc:mysql://****:3306/fan?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true
spring.datasource.username=root
spring.datasource.password=+
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
 
 
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.type-aliases-package=com.fun.model
 
pagehelper.helperDialect=mysql
pagehelper.reasonable=true
pagehelper.supportMethodsArguments=true
pagehelper.params=count=countSql
pagehelper.returnPageInfo=check

```
下面是数据库存储的信息：
![](http://pic.automancloud.com/2019011214594081.png)

---
* **郑重声明**：文章禁止第三方（腾讯云除外）转载、发表，事情原委[测试窝，首页抄我七篇原创还拉黑，你们的良心不会痛吗？](https://mp.weixin.qq.com/s/ke5avkknkDMCLMAOGT7wiQ)

