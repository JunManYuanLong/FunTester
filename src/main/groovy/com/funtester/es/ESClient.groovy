package com.funtester.es

import com.funtester.frame.SourceCode
import groovy.util.logging.Log4j2
import org.apache.http.HttpHost
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.search.SearchHits
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.fetch.subphase.FetchSourceContext

import java.util.concurrent.TimeUnit
/**
 * ES客户端API练习类
 */
@Log4j2
class ESClient extends SourceCode {

    String host

    int port

    String scheme

    RestHighLevelClient client

    ESClient(String host, int port = 9200, String scheme = "http") {
        this.host = host
        this.port = port
        this.scheme = scheme
        // 设置验证信息，填写账号及密码
        //        CredentialsProvider credentialsProvider = new BasicCredentialsProvider()
        //        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("user", "passwd"))
        def builder = RestClient.builder(new HttpHost(host, port, scheme))
        // 设置认证信息
        //        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
        //
        //            @Override
        //            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
        //                return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
        //            }
        //        })
        builder.setMaxRetryTimeoutMillis(1000)
        client = new RestHighLevelClient(builder)
    }

    def index(String index, type, Map data) {
        IndexRequest indexRequest = new IndexRequest(index, type).source(data)
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT)
        indexResponse.getId()
    }

    def get(String index, type, id) {
        // 查询文档
        GetRequest getRequest = new GetRequest(index, type, id)
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT)
        if (getResponse.isExists()) {
            getResponse.getSourceAsString()
        }
    }

    def exists(String index, type, id) {
        GetRequest getRequest = new GetRequest(index, type, id)
        getRequest.fetchSourceContext(new FetchSourceContext(false))
        getRequest.storedFields("_none_")
        client.exists(getRequest, RequestOptions.DEFAULT)
    }

    def delete(String index, type, id) {
        GetRequest getRequest = new GetRequest(index, type, id)
        client.delete(getRequest, RequestOptions.DEFAULT)
    }

    def search(String index, QueryBuilder query, int size = 10) {
        SearchRequest searchRequest = new SearchRequest(index)
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
        sourceBuilder.query(query)
        sourceBuilder.from(0)
        sourceBuilder.size(size)
        sourceBuilder.timeout(new TimeValue(1, TimeUnit.SECONDS))
        searchRequest.source(sourceBuilder)
        client.search(searchRequest, RequestOptions.DEFAULT)
        //        search.getHits().getHits().each {
        //            output("命中" + TAB + it)
        //        }

    }

    def searchScroll(String index, QueryBuilder query, int size = 10) {
        SearchRequest searchRequest = new SearchRequest(index)
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
        searchSourceBuilder.query(query)
        searchSourceBuilder.size(size)
        searchRequest.source(searchSourceBuilder)
        searchRequest.scroll(TimeValue.timeValueMinutes(1L))
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)
        String scrollId = searchResponse.getScrollId()
        SearchHits hits = searchResponse.getHits()
        def searchHits = hits.getHits()
//        searchHits.each {
//            output("命中" + LINE + TAB + it)
//        }
//        while (searchHits != null && searchHits.length > 0) {
//            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId)
//            scrollRequest.scroll(TimeValue.timeValueMinutes(1L))
//            searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT)
//            scrollId = searchResponse.getScrollId()
//            searchHits = searchResponse.getHits().getHits()
//        }
        //        output(searchRequest)

    }

    def close() {
        client.close()
    }
}
