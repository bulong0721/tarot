package com.myee.tarot.device.service.impl.elasticSearch;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.web.EntityQueryDto;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.*;
import io.searchbox.params.Parameters;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Selim on 2016/8/10.
 */
@Component
public class ESUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESUtils.class);
    //后期通过spring配置获取
   // private static JestClient jestClient = InitES.jestClient();

    @Value("${es_http_url}")
    private String connectionUrl;

    private JestClient jestClient ;

    @Autowired
    public void setJestClient() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder(connectionUrl).multiThreaded(false).build());
        jestClient = factory.getObject();
    }
    public JestClient getJestClient() {
        return jestClient;
    }
    /**
     * 添加单个索引
     *
     * @param index
     * @param type
     * @param object
     */
    public void addDomain(String index, String type, Object object) {
        LOGGER.info("开始添加索引");
        try {
            Index addIndex = new Index.Builder(object).index(index).type(type).build();
            jestClient.execute(addIndex);
            LOGGER.info("添加索引成功");
        } catch (IOException e) {
            LOGGER.error("添加索引失败");
            e.printStackTrace();
        }
    }

    /**
     * 创建es news索引 bulk批量添加 List
     */
    public <T> void bulkAddList(String index, String type, List<T> addList) {
        LOGGER.info("开始批量添加索引");
        long start = System.currentTimeMillis();
        try {
            List<Index> indexes = Lists.newArrayList();
            for (T t: addList) {
                indexes.add(new Index.Builder(t).build());
            }
            // Bulk 两个参数1:索引名称2:类型名称(用文章(article)做类型名称)
            Bulk bulk = new Bulk.Builder().defaultIndex(index).defaultType(type).addAction(indexes).build();
            jestClient.execute(bulk);
        } catch (Exception e) {
            LOGGER.error("批量添加出现异常");
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        LOGGER.info("批量创建索引时间:数据量是  " + addList.size() + "记录,共用时间 -->> " + (end - start) + " 毫秒");
    }

    /**
     * 通过id 获取实体类
     *
     * @param index
     * @param type
     * @param id
     * @return
     */
    public <T> T getDomain(String index, String type, String id, Class<T> clazz) {
        try {
            Get get = new Get.Builder(index, id).type(type).build();
            JestResult jestResult = jestClient.execute(get);
            return jestResult.getSourceAsObject(clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 分页查询 使用 from to  可用 存在性能问题？深度查询效率会变慢
     *
     * @param index
     * @param type
     * @param queryTitle
     * @param param
     * @param pageNum
     * @param pageSize
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> PageResult<T> searchPageSource(String index, String type, String queryTitle, String param, int pageNum, int pageSize, Class<T> clazz) {
        try {
            LOGGER.info("搜索开始");
            PageResult<T> pageResult =  new PageResult<>();
            int fromNum = pageSize * (pageNum - 1);
            long start = System.currentTimeMillis();
            //采用自行拼接json
            String query = "";
            if(StringUtils.isNotBlank(queryTitle)){
                query = "{\n" +
                        "    \"query\": {\n" +
                        "    \"filtered\": {\n" +
                        "    \"filter\": {\n" +
                        "          \"match\" : {\n" +
                        "              \""+queryTitle+"\" : {\n" +
                        "                    \"query\" : \""+ param +"\"\n" +
                        "              }\n" +
                        "          }\n" +
                        "      }\n" +
                        "      }\n" +
                        "    }\n" +
                        "}";
            }
            Search searchAllCount = new Search.Builder(query).addIndex(index).addType(type).build();
            SearchResult countResult = jestClient.execute(searchAllCount);
            pageResult.setRecordsTotal(countResult.getTotal()== null ? 0:countResult.getTotal());
            Search search = new Search.Builder(query).addIndex(index).addType(type).setParameter(Parameters.FROM, fromNum).setParameter(Parameters.SIZE, pageSize).build();
            JestResult result = jestClient.execute(search);
            pageResult.setList(result.getSourceAsObjectList(clazz));
            long end = System.currentTimeMillis();
            LOGGER.info("搜索共用时间 -->> " + (end - start) + " 毫秒");
            return pageResult;
        } catch (Exception e) {
            LOGGER.error("搜索出现异常");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 多个查询条件 bool Must联合 分页
     *
     * @param index
     * @param type
     * @param queries  多个参数条件 放在Map集合中
     * @param pageNum
     * @param pageSize
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> PageResult<T> searchPageQueries(String index, String type, Map<String, EntityQueryDto> queries, int pageNum, int pageSize, Class<T> clazz) {
        try {
            LOGGER.info("搜索开始");
            PageResult<T> pageResult =  new PageResult<>();
            int fromNum = pageSize * (pageNum - 1);
            long start = System.currentTimeMillis();
            StringBuffer query = new StringBuffer();
            if(queries != null && queries.size() != 0){
                query.append("{\n" +
                        "    \"query\": {\n" +
                        "    \"filtered\": {\n" +
                        "    \"filter\": {\n" +
                        "      \"bool\": {\n" +
                        "        \"should\": [\n");
                for (String key : queries.keySet()) {
                    if(queries.get(key).getQueryPattern() == 0){
                        query.append( "          { \"match\" : {\n" +
                                "              \""+key+"\" : {\n" +
                                "                    \"query\" : \""+queries.get(key).getFieldValue().toString()+"\"\n" +
                                "              }\n" +
                                "          }}," );
                    }
                }
                query.deleteCharAt(query.length()-1);
                query.append("        ],\n" +
                        "        \"must\": [\n");
                for (String key : queries.keySet()) {
                    if(queries.get(key).getQueryPattern() == 1){
                        //特定为时间选择做的query
                        if(key.equals("startDate")){
                            query.append( "          { \"range\" : {\n" +
                                    "              \"date\" : {\n" +
                                    "                    \"gte\" : \""+ queries.get(key).getFieldValue() +"\"\n" +
                                    "              }\n" +
                                    "          }}," );
                        }else if(key.equals("endDate")){
                            query.append( "          { \"range\" : {\n" +
                                    "              \"date\" : {\n" +
                                    "                    \"lte\" : \""+ queries.get(key).getFieldValue() +"\"\n" +
                                    "              }\n" +
                                    "          }}," );

                        }else{
                            query.append( "          { \"match\" : {\n" +
                                    "              \"" + key + "\" : {\n" +
                                    "                    \"query\" : \""+ queries.get(key).getFieldValue().toString() +"\"\n" +
                                    "              }\n" +
                                    "          }}," );
                        }
                    }
                }
                query.deleteCharAt(query.length()-1);
                query.append("        ]\n" +
                        "      }\n" +
                        "      }\n" +
                        "      }\n" +
                        "    }\n" +
                        "}");
            }
            LOGGER.info("查询语句为"+query.toString());
            Search searchAllCount = new Search.Builder(query.toString()).addIndex(index).addType(type).build();
            SearchResult countResult = jestClient.execute(searchAllCount);
            pageResult.setRecordsTotal(countResult.getTotal()== null ? 0:countResult.getTotal());
            Search search = new Search.Builder(query.toString()).addIndex(index).addType(type).setParameter(Parameters.FROM, fromNum).setParameter(Parameters.SIZE, pageSize).build();
            JestResult result = jestClient.execute(search);
            pageResult.setList(result.getSourceAsObjectList(clazz));
            long end = System.currentTimeMillis();
            LOGGER.info("搜索共用时间 -->> " + (end - start) + " 毫秒");
            return pageResult;
        } catch (Exception e) {
            LOGGER.error("搜索出现异常");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 深度分页  暂时不用存在问题
     * @param index
     * @param type
     * @param scrollId
     * @param queries
     * @param pageSize
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Map<String,Object> searchScrollPageQueries(String index, String type, String scrollId, Map<String, String> queries,int pageSize, Class<T> clazz) {
        try {
            LOGGER.info("搜索开始");
            long start = System.currentTimeMillis();
            Map<String,Object> result = Maps.newHashMap();
            JestResult jestResult = null;
            if(StringUtils.isNotBlank(scrollId)){
                jestResult = readMoreScroll(scrollId,pageSize);
                result.put("scrollId",scrollId);
            }else {
                SearchResult searchResult = startScroll(index, type, pageSize);
                String newScrollId = searchResult.getJsonObject().get("_scroll_id").getAsString();
                //List<> hits = searchResult.getHits(clazz);
                result.put("scrollId",newScrollId);
                System.out.println(newScrollId);
            }
            result.put("list", jestResult.getSourceAsObjectList(clazz));
            long end = System.currentTimeMillis();
            LOGGER.info("搜索共用时间 -->> " + (end - start) + " 毫秒");
            return result;
        } catch (Exception e) {
            LOGGER.error("搜索出现异常");
            e.printStackTrace();
        }
        return null;
    }

    public SearchResult startScroll(String index, String type, int pageSize) {
        try {
            //SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            Search search = new Search.Builder("").addIndex(index).addType(type).setParameter(Parameters.SIZE, pageSize).setParameter(Parameters.SCROLL, "3m").build();
            SearchResult result = jestClient.execute(search);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JestResult readMoreScroll(String scrollId , int size){
        try {
            SearchScroll scroll = new SearchScroll.Builder(scrollId,"3m").setParameter(Parameters.SIZE,size).build();
            JestResult jestResult = jestClient.execute(scroll);
            return jestResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
