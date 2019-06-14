package com.fun.utils

import com.fun.frame.SourceCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FileUtil extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class)

/**
 * 拷贝文件
 * @param source
 * @param target
 * @return
 */
    static def copy(String source, String target) {
        def s = new File(source)
        def t = new File(target)
        if (s.exists() && s.isFile()) return t.newOutputStream() << s.newInputStream()
        output("copy file error！")
    }

/**
 * 重命名一个文件
 * @param oldPath
 * @param newPath
 * @return
 */
    static def rename(String oldPath, String newPath) {
        if (new File(oldPath).renameTo(newPath)) logger.error("rename file error!，old:{},new:{}", oldPath, newPath)
    }

/**
 * 从url下载文件
 * @param url
 * @param target
 * @return
 */
    static def down(String url, String name) {
        new File(name) << new URL(url).openStream()
    }

/**
 * 获取文件夹下所有文件的绝对路径的方法，递归，排除了Linux系统的隐藏文件
 *
 * @param path
 * @return
 */
    static List<String> getAllFile(String path) {
        List<String> list = new ArrayList<>()
        File file = new File(path)
        if (!file.exists() || file.isFile()) return list
        File[] files = file.listFiles()
        int length = files.length
        if (length == 0) return list
        for (int i in 0..length - 1) {
            File file1 = files[i]
            if (file1.isDirectory()) {
                List<String> allFile = getAllFile(file1.getAbsolutePath())
                list.addAll(allFile)
                continue
            }
            String path1 = file1.getAbsolutePath()
            if (path1.contains("/.")) continue
            list.add(path1)
        }
        return list
    }
}
