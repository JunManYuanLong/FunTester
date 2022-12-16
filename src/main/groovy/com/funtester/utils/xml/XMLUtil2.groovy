package com.funtester.utils.xml

import com.funtester.base.exception.FailException
import groovy.util.logging.Log4j2
import org.dom4j.Attribute
import org.dom4j.Document
import org.dom4j.DocumentException
import org.dom4j.Element
import org.dom4j.Node
import org.dom4j.io.SAXReader

/**
 * 基于dom4j解析xml工具类
 */
@Log4j2
class XMLUtil2 {

    /**
     * 解析xml文件
     * @param path 绝对路径或者URL
     * @return
     */
    static List<NodeInfo> parse(String path) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(path.startsWith("http") ? new URL(path) : new File(path));
            Element rootElement = document.getRootElement();
            def iterator = rootElement.elementIterator()
            List<NodeInfo> info = new ArrayList<>()
            while (iterator.hasNext()) {
                info << parseNode(iterator.next() as Element)
            }
            return info;
        } catch (DocumentException e) {
            log.error("解析文件${path}失败!", e)
        }
        FailException.fail("解析文件${path}失败!")
    }

    /**
     * 解析节点信息
     * @param e
     * @return
     */
    static NodeInfo parseNode(Element e) {
        if (e.getNodeType() != Node.ELEMENT_NODE) return null;
        def info = new NodeInfo()
        List<Attribute> attributes = e.attributes();
        List<Attr> attrs = new ArrayList<>()
        attributes.each {
            attrs << new Attr(it.name, it.value)
        }
        info.setAttrs(attrs)
        List<NodeInfo> children = new ArrayList<>()
        def iterator = e.elementIterator()
        if (iterator.hasNext()) {
            children << parseNode(iterator.next() as Element)
        }
        info.setChildren(children)
        return info;
    }
}
