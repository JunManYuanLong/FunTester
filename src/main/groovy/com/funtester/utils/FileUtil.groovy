package com.funtester.utils

import com.funtester.config.Constant
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * 文件读写类,与{@link RWUtil}有功能上的重合,原因在与Java和Groovy的不兼容问题.
 */
class FileUtil extends Constant {

    private static Logger logger = LogManager.getLogger(FileUtil.class)

    /**
     * 拷贝文件
     * @param source
     * @param target
     * @return
     */
    static def copy(String source, String target) {
        def s = new File(source)
        def t = new File(target)
        if (s.exists() && s.isFile()) t.newOutputStream() << s.newInputStream()
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
     * @param name
     * @return
     */
    static def down(String url, String name) {
        new File(name) << new URL(url).openStream()
    }

    /**
     * 下载文件,目前只要针对图片
     * @param url
     * @return
     */
    static def down(String url) {
        def tuple = handlePicName(url)
        down(tuple.first, tuple.second);
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

    /**
     * 处理下载网络图片的时候明文件的问题
     * @param name
     * @return
     */
    static Tuple2 handlePicName(String url) {
        url -= ".webp"
        String name = url.substring(url.lastIndexOf("/") + 1);
        if (name.contains(UNKNOW)) name = name.substring(0, name.indexOf(UNKNOW))
        return new Tuple2<String, String>(url, name)
    }

}
