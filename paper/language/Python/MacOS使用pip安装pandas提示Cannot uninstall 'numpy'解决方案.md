# MacOS使用pip安装pandas提示Cannot uninstall 'numpy'解决方案
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

本人在重新搭建自己的电脑的plotly环境的时候遇到一个坑，就是使用pip安装pandas的时候一直提示：

```
pip uninstall numpy
Cannot uninstall 'numpy'. It is a distutils installed project and thus we cannot accurately determine which files belong to it which would lead to only a partial uninstall.
```
由于英文水平有限，期初以为是权限不够，加上sudo依然报错。在借助工具翻译了之后，发现是无法卸载numpy，原因如下：


```
这是一个distutils安装的项目，因此我们无法准确确定哪些文件属于它，这将导致仅部分卸载。
```
原来numpy是标准库的东西，让我想起window系统里面有些软件安装时候会放在system文件夹下，导致提示无法准确识别需要卸载删除的文件导致卸载失败的情况。虽然不清楚为啥装pandas需要先卸载numpy，但是使用pip先安装了一波。提示如下：


```
Requirement already satisfied: numpy in /System/Library/Frameworks/Python.framework/Versions/2.7/Extras/lib/python (1.8.0rc1)
```
还是不行，干脆直接删除了。提醒一下先备份，然后在安装重新安装numpy，这样保险一些。

```
/System/Library/Frameworks/Python.framework/Versions/2.7/Extras/lib/python$ ls | grep numpy
numpy-1.8.0rc1-py2.7.egg-info
/System/Library/Frameworks/Python.framework/Versions/2.7/Extras/lib/python$ sudo mv numpy-1.8.0rc1-py2.7.egg-info /Users/fv/
/System/Library/Frameworks/Python.framework/Versions/2.7/Extras/lib/python$
```
下面放一下我安装命令，用的源是清华的，豆瓣的老提示连接失败。

```
sudo pip install --index-url https://pypi.tuna.tsinghua.edu.cn/simple numpy
sudo pip install --index-url https://pypi.tuna.tsinghua.edu.cn/simple pandas
```
>划重点，还有一些坑：
看国外网友是3.+的版本bug比较多，我果然滚回来2.4
新电脑一定要注意相关依赖库的版本，有时候plotly提示找不到模块，只是因为版本太低了。

<br></br>
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>