# python使用filter方法递归筛选法求N以内的质数（素数）--附一行打印心形标记的代码解析


本人在学习使用Python的lambda语法的过程中，用之前求解质数的思路重写了一遍，思路如下：就是新建一个长数组，然后从前往后递归相除去过滤后面的元素。中间对于Python语法的有了一点新的认识：看自己的代码很陌生，大概是因为写得少的原因。

下面是代码：


```

i = 0
a = range(2, 20)

def test(sss):
    global i
    if i >= len(sss): return sss
    re = list(filter(lambda x: True if (a[i] == x) else (x % a[i] != 0), sss))
    i += 1
    return test(re)


c = test(a)
print(c)
```
下面附上Python一行代码打印心形的代码解析，把原来一行代码分拆，把循环和判断单独拿出来，看起来比较清晰了，再次感叹Python语法的强大。

```

    print'\n'.join([''.join( [('Love'[(x - y) % 4] if ((x * 0.05) ** 2 + (y * 0.1) ** 2 - 1) ** 3 - (x * 0.05) ** 2 * (y * 0.1) ** 3 <= 0 else ' ') for x in range(-30, 30)]) for y in range(15, -15, -1)])

    for y in range(15, -15, -1):
        line = []
        for x in range(-30, 30):
            if ((x * 0.05) ** 2 + (y * 0.1) ** 2 - 1) ** 3 - (x * 0.05) ** 2 * (y * 0.1) ** 3 <= 0:
                line.append('Love'[(x - y) % 4])
            else:
                line.append(" ")
        l = "".join(line)
        print l
```
