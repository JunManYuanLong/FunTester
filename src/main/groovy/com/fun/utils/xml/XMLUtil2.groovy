package com.fun.utils.xml

import com.fun.base.exception.FailException
import com.fun.frame.SourceCode
import org.dom4j.*
import org.dom4j.io.SAXReader
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class XMLUtil2 extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(XMLUtil2.class)

    static List<NodeInfo> parse(String path) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(path.startsWith("http") ? new URL(path) : new File(path));
            Element bookStore = document.getRootElement();
            def iterator = bookStore.elementIterator()
            List<NodeInfo> info = new ArrayList<>()
            while (iterator.hasNext()) {
                info << parseNode(iterator.next() as Element)
            }
            return info;
        } catch (DocumentException e) {
            logger.error("解析文件${path}失败!", e)
        }
        FailException.fail("解析文件${path}失败!")
    }

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
