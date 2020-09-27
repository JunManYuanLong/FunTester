# JMeter参数签名——Groovy工具类形式

发现JMeter系列写了不少文章，干脆整个全套加强版的，把剩下的Demo也发一下，旧文如下：

- [用Groovy处理JMeter断言和日志](https://mp.weixin.qq.com/s/Q4yPA4p8dZYAARZ60ZDh9w)
- [用Groovy处理JMeter变量](https://mp.weixin.qq.com/s/BxtweLrBUptM8r3LxmeM_Q)
- [用Groovy在JMeter中执行命令行](https://mp.weixin.qq.com/s/VTip7tiLpwBOr1gUoZ0n8A)
- [用Groovy处理JMeter中的请求参数](https://mp.weixin.qq.com/s/9pCUOXWpMwXR5ynvCMYJ7A)
- [用Groovy在JMeter中使用正则提取赋值](https://mp.weixin.qq.com/s/9riPpnQZCfKGscuzOOpYmQ)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [Groovy在JMeter中处理cookie](https://mp.weixin.qq.com/s/DCnDjWaj2aiKv5HVw3-n6A)
- [Groovy在JMeter中处理header](https://mp.weixin.qq.com/s/juY-1jEWODJ5HHiEsxhIEw)

如何在JMeter中对参数进行签名？

上期[JMeter参数签名——Groovy脚本形式](https://mp.weixin.qq.com/s/wQN9-xAUQofSqiAVFXdqug)用的是`Groovy`脚本形式，本期继续`Groovy`在`JMeter`中参数签名的使用——通过`Groovy`工具类完成参数签名。

这个情况接口测试中是经常遇到的，接口的某个参数是由其他参数（包括校验`token`）决定的，在我的经验中，常见于`PHP`后端服务中。下面分享一下如何用Groovy工具类处理这种情况。代码都是开发提供的，直接复制过来就行，这也是我选择`Groovy`的原因之一：与Java近乎完美的兼容性。代码中Groovy脚本内容与上期一致，关键信息略去。


* 首先新建一个简单的线程组和一个简单的请求：

![](http://pic.automancloud.com/1583240306684.jpg)

* 添加JSR223 预处理程序

![](http://pic.automancloud.com/QQ20200303-210125.png)

脚本内容：


```Groovy
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

vars.put("MY1","flow")
props.put("MY","ewewewerr")
log.info(props.get("MY"))
sampler.addArgument("name","funteddster")
sampler.addArgument("pwd","funtddester")

def args = sampler.getArguments()
def ss = [:]
log.info(sampler.getArguments().toString())
args.getArgumentCount().times{
	def a = args.getArgument(it)
	ss.put(a.ARG_NAME,a.VALUE)
}

def my_var = RSAUtilLJT.sign(ss,RSAUtilLJT.getPrivateKey(RSAUtilLJT.RSA_PRIVATE_KEY)) as String;

log.warn "输出参数-------- ${vars} console"
log.info("222222 " + my_var);

sampler.addArgument("sign", my_var)



public class RSAUtilLJT {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * RSA私钥
     */
    public static final String RSA_PRIVATE_KEY= "保密内容";
    /**
     * RSA公钥
     */
    public static final String RSA_PUBLIC_KEY= "保密内容";

    /**
     * 不参与签名参数
     */
    private static final String excludeKey = "sign";


    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密
     *
     * @param data 待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return new String(Base64.encodeBase64String(encryptedData));
    }

    /**
     * RSA解密
     *
     * @param data 待解密数据
     * @param privateKey 私钥
     * @return
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, "UTF-8");
    }

    /**
     * 签名
     *
     * @param data 待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     *
     * @param srcData 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    /**
     * 对map进行签名
     * @param mapData
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String sign(Map<String, String> mapData, PrivateKey privateKey) throws Exception {
        return sign(getStr(mapData), privateKey);
    }

    /**
     * 签名验证
     * @param mapData 参数map
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static boolean verify(Map<String, String> mapData, PublicKey publicKey) throws Exception {
        String sign = mapData.remove(excludeKey);
        if (sign == null || sign.length() < 1) {
            throw new RuntimeException("参数缺少签名");
        }
        return verify(getStr(mapData), publicKey, sign);
    }

    /**
     * 接口请求参数转字符串
     * @param parms
     * @return
     */
    public static String getStr(Map<String, String> parms) {
        parms.remove(excludeKey);
        TreeMap<String, String> sortParms = new TreeMap<>();
        sortParms.putAll(parms);
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : parms.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(String.valueOf(entry.getValue()));
            builder.append("&");
        }
        if (builder.length() > 1) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
```

* 控制台输出：

```Java
2020-04-17 17:13:30,989 INFO o.a.j.e.StandardJMeterEngine: Running the test!
2020-04-17 17:13:30,991 INFO o.a.j.s.SampleEvent: List of sample_variables: []
2020-04-17 17:13:31,008 INFO o.a.j.g.u.JMeterMenuBar: setRunning(true, *local*)
2020-04-17 17:13:31,208 INFO o.a.j.e.StandardJMeterEngine: Starting ThreadGroup: 1 : 线程组
2020-04-17 17:13:31,208 INFO o.a.j.e.StandardJMeterEngine: Starting 1 threads for group 线程组.
2020-04-17 17:13:31,208 INFO o.a.j.e.StandardJMeterEngine: Thread will continue on error
2020-04-17 17:13:31,209 INFO o.a.j.t.ThreadGroup: Starting thread group... number=1 threads=1 ramp-up=0 perThread=0.0 delayedStart=false
2020-04-17 17:13:31,210 INFO o.a.j.t.ThreadGroup: Started thread group number 1
2020-04-17 17:13:31,213 INFO o.a.j.e.StandardJMeterEngine: All thread groups have been started
2020-04-17 17:13:31,216 INFO o.a.j.t.JMeterThread: Thread started: 线程组 1-1
2020-04-17 17:13:31,337 INFO o.a.j.m.J.JSR223 参数签名Groovy类: ewewewerr
2020-04-17 17:13:31,341 INFO o.a.j.m.J.JSR223 参数签名Groovy类: t=flow()&s=ewewewerr()&name=funteddster()&pwd=funtddester()
2020-04-17 17:13:31,360 WARN o.a.j.m.J.JSR223 参数签名Groovy类: 输出参数-------- org.apache.jmeter.threads.JMeterVariables@7bdab282 console
2020-04-17 17:13:31,361 INFO o.a.j.m.J.JSR223 参数签名Groovy类: 222222 DV1UC0RF7y7FWArtYJP8LaUYwWZm7Mc5P8vmx5e4cGqQstaW3LlfR+o5mSiBTTxLY3NSvsr5EHLkLzPcfJ3YCmjJnneZj+lCb7fR7XA5snwGHJNbeDejn6x3oNVEZF8i4MR/vPO9I1lawA6pEuO5t7kW21IizQdEyxAc2pxLcj8=
2020-04-17 17:13:31,495 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-1
2020-04-17 17:13:31,495 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-1
2020-04-17 17:13:31,495 INFO o.a.j.e.StandardJMeterEngine: Notifying test listeners of end of test
2020-04-17 17:13:31,495 INFO o.a.j.g.u.JMeterMenuBar: setRunning(false, *local*)


```

* 查看结果树

![](http://pic.automancloud.com/QQ20200416-230312.png)

* 可以清楚看到，签名字段`sign`已经写到参数里面了。

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [多项目登录互踢测试用例](https://mp.weixin.qq.com/s/Nn_CUy_j7j6bUwHSkO0pCQ)
- [从Java到Groovy的八级进化论](https://mp.weixin.qq.com/s/QTrRHsD3w-zLGbn79y8yUg)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [简化测试用例](https://mp.weixin.qq.com/s/BhwfDqhN9yoa3Iul_Eu5TA)
- [软件测试中的虚拟化](https://mp.weixin.qq.com/s/zHyJiNFgHIo2ZaPFXsxQMg)
- [生产环境中进行自动化测试](https://mp.weixin.qq.com/s/JKEGRLOlgpINUxs-6mohzA)
- [所谓UI测试](https://mp.weixin.qq.com/s/wDvUy_BhQZCSCqrlC2j1qA)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)