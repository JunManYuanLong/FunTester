package com.fun.utils.xml


import com.fun.base.exception.FailException
import com.fun.frame.SourceCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXException

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

class XMLUtil extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(XMLUtil.class)

    static List<com.sun.org.apache.xalan.internal.lib.NodeInfo> parseRoot(String path, String root) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance()
        try {
            DocumentBuilder db = dbf.newDocumentBuilder()
            Document document = db.parse(path.startsWith("http") ? new URI(path) : new File(path))
            NodeList nodeList = document.getElementsByTagName(root)
            return range(nodeList.getLength()).mapToObj {x -> parseNode(nodeList.item(x))}.collect() as List
        } catch (ParserConfigurationException e) {
            logger.error("解析配置错误!", e)
        } catch (IOException e) {
            logger.error("IO错误!", e)
        } catch (SAXException e) {
            logger.error("SAX错误!", e)
        }
        FailException.fail("解析文件:${path}中${root}节点出错!")
    }

    static NodeInfo parseNode(Node node) {
        if (node.getNodeType() != Node.ELEMENT_NODE) return null
        NodeInfo nodeInfo = new NodeInfo()
        NamedNodeMap attrs = node.getAttributes()
        List<Attr> nodeAttr = new ArrayList<>()
        range(attrs.getLength()).each {
            Node attr = attrs.item(it)
            nodeAttr << new Attr(attr.getNodeName(), attr.getNodeValue())
        }
        nodeInfo.attrs = nodeAttr
        NodeList childNodes = node.getChildNodes()
        List<com.sun.org.apache.xalan.internal.lib.NodeInfo> children = new ArrayList<>()
        range(childNodes.getLength()).each {children.add(parseNode(childNodes.item(it)))}
        nodeInfo.children = children.findAll {it != null}
        return nodeInfo
    }


}
