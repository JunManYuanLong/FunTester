package com.funtester.utils.xml
/**
 * 基于dom解析xml文件工具类
 */
class XMLUtil {

//    private static Logger logger = LogManager.getLogger(XMLUtil.class)
//
//    /**
//     *  解析某个节点(根节点)信息
//     * @param path 绝对路径或者URL
//     * @param root
//     * @return
//     */
//    static List<NodeInfo> parseRoot(String path, String root) {
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance()
//        try {
//            DocumentBuilder db = dbf.newDocumentBuilder()
//            Document document = db.parse(path.startsWith("http") ? new URI(path) : new File(path))
//            NodeList nodeList = document.getElementsByTagName(root)
//            return range(nodeList.getLength()).mapToObj {x -> parseNode(nodeList.item(x))}.collect() as List
//        } catch (ParserConfigurationException e) {
//            logger.error("解析配置错误!", e)
//        } catch (IOException e) {
//            logger.error("IO错误!", e)
//        } catch (SAXException e) {
//            logger.error("SAX错误!", e)
//        }
//        FailException.fail("解析文件:${path}中${root}节点出错!")
//    }
//
//    /**
//     * 解析某个节点信息
//     * @param node
//     * @return
//     */
//    static NodeInfo parseNode(Node node) {
//        if (node.getNodeType() != Node.ELEMENT_NODE) return null
//        NodeInfo nodeInfo = new NodeInfo()
//        NamedNodeMap attrs = node.getAttributes()
//        List<Attr> nodeAttr = new ArrayList<>()
//        range(attrs.getLength()).each {
//            Node attr = attrs.item(it)
//            nodeAttr << new Attr(attr.getNodeName(), attr.getNodeValue())
//        }
//        nodeInfo.attrs = nodeAttr
//        NodeList childNodes = node.getChildNodes()
//        List<NodeInfo> children = new ArrayList<>()
//        range(childNodes.getLength()).each {children.add(parseNode(childNodes.item(it)))}
//        nodeInfo.children = children.findAll {it != null}
//        return nodeInfo
//    }


}
