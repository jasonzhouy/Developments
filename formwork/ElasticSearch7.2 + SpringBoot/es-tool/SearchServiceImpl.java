package com.it.hikvision.service.mob.impl;


import com.alibaba.fastjson.JSON;

import com.it.hikvision.mobtool.tool.ElasticSearchTools;
import com.it.hikvision.mobtool.exception.ElasticSeachException;
import com.it.hikvision.entity.mob.KnowledgeWithNoDetailsEntity;
import com.it.hikvision.service.mob.SearchService;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.common.text.Text;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import org.elasticsearch.search.builder.SearchSourceBuilder;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static com.it.hikvision.mobtool.KnowledgeConstantParamOfES.*;

@Service
public class SearchServiceImpl implements SearchService {


    @Resource
    private RestHighLevelClient restHighLevelClient;


    /**
     * 根据输入的关键字，在多字段（【2021年4月29日 版本v1】字段是title、keywords、details、）中匹配，返回关联度最高的文档
     * 限制条件：
     * 1、文档要符合可见等相关条件
     * 2、关键字要匹配
     */
    @Override
    public List<KnowledgeWithNoDetailsEntity> getTitlesByKeyWords(String keywords, String collection, String collectionValid, String subcategoryValid,
                                                                  String categoryValid) {


        // 返回结果
        List<KnowledgeWithNoDetailsEntity> result = null;

        // K-V: field-权重
        Map<String, Float> cartMap = new HashMap<>(3);
        // K-V：field-是否高亮
        Map<String, Boolean> highLightMap = new HashMap<>(1);

        // 设置当前业务中各字段的权重以及高亮字段，查询字段在 setSearchParamWeight()方法中
        setSearchTitlesByKeyWordsParamWeight(cartMap, highLightMap);


        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 获取查询条件开始---------------------------------------------
        BoolQueryBuilder temp = QueryBuilders.boolQuery();

        // 获得子级查询条件（业务的需求条件）
        // 根据回传的条件限制（是否是全部分类下的数据、是否公开、是否可见）获得查询条件
        List<QueryBuilder> conditions = getAssignConditionQueryBuilder(collection, collectionValid, subcategoryValid, categoryValid);
        // 获得各字段混合的查询语句
        MultiMatchQueryBuilder multiMatchQueryBuilder = ElasticSearchTools.getMultiMatchQueryBuilder(keywords, cartMap);
        //  设置当前查询中，其他字段的查询权重（因为是多字段查询，所以必然会有一个字段影响力过大，所以为了综合较小影响力的字段，就要设置tieBreaker）
        multiMatchQueryBuilder.tieBreaker(Float.valueOf(0.4f));
        // 获得高亮字段
        HighlightBuilder highlightBuilder = ElasticSearchTools.getHighLightSourceBuilder(highLightMap);
        // 获取查询条件结束---------------------------------------------
        // 拼装查询语句
        temp.must().addAll(conditions);
        temp.must(multiMatchQueryBuilder);
        searchSourceBuilder.query(temp).highlighter(highlightBuilder);
        System.out.println(searchSourceBuilder);

        // 设置查询索引
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);

        // 追踪totalSize，当totalSize>10000时，totalSize也可以正常显示
        // 通过设置size(ELASTICSEARCH_DEFAULT_MAX_SIZE)使得突破每次只返回10条数据的限制
        searchRequest.source(searchSourceBuilder.trackTotalHits(true).size(ELASTICSEARCH_DEFAULT_MAX_SIZE));

        try {

            // 开始查询并获得结果（同步阻塞）
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

            // 此处从long-> int ，当数据量超过MAXVALUE会丢失精度，注意
            int size = (int) searchResponse.getHits().totalHits;
            if (size < ELASTICSEARCH_DEFAULT_MAX_SIZE) {
                // 解析返回数据拿到返回值
                result = getKnowledgeWithNoDetailsEntity(searchResponse);
            } else {
                // 超过10000条的数据要分页
                // TODO
                throw new ElasticSeachException("数据超过一万条，要分页！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ElasticSeachException(e);
        }


        return result;
    }


    /**
     * 获得getTitlesByKeyWords的文章可见性限制条件
     */

    private List<QueryBuilder> getAssignConditionQueryBuilder(String collection, String collectionValid, String subcategoryValid,
                                                              String categoryValid) {

        List<QueryBuilder> queryBuilderList = new ArrayList<>();

        // 固定写死，只查已经Published的document
        queryBuilderList.add(QueryBuilders.matchQuery(STATUS_COLUMN, "Published"));

        if (StringUtils.isNotBlank(collection)) {
            queryBuilderList.add(QueryBuilders.matchQuery(COLLECTION_COLUMN, collection));
        }

        if (StringUtils.isNotBlank(collectionValid)) {
            queryBuilderList.add(QueryBuilders.matchQuery(COLLECTIONVALID_COLUMN, collectionValid));
        }

        if (StringUtils.isNotBlank(subcategoryValid)) {
            queryBuilderList.add(QueryBuilders.matchQuery(SUBCATEGORYVALID_COLUMN, subcategoryValid));
        }

        if (StringUtils.isNotBlank(categoryValid)) {
            queryBuilderList.add(QueryBuilders.matchQuery(CATEGORYVALID_COLUMN, categoryValid));
        }
        return queryBuilderList;

    }

    /**
     * 设置getTitlesByKeyWords查询逻辑的权重以及高亮字段
     * 目前（2021年4月28日 移动端v1版本）的逻辑是：
     * 1。 查title、keywords、details字段（权重比为3：2：1）
     * 2.  只高亮title
     */
    private void setSearchTitlesByKeyWordsParamWeight(Map<String, Float> cartMap, Map<String, Boolean> highLightMap) {

        // 权重
        float titleCart = 4.0f;
        float keywordsCart = 2.0f;
        float detailsCart = 1.0f;

        // 目前（2021年4月28日 移动端v1版本）查询条件固定写死，后续根据扩展需要可以放开
        // 目前版本的默认逻辑：title的权重为4，keywords的权重为2，details的权重为1
        cartMap.put(TITLE_COLUMN, titleCart);
        cartMap.put(KEYWORDS_COLUMN, keywordsCart);
        cartMap.put(DETAILS_COLUMN, detailsCart);

        // 只高亮标题字段
        highLightMap.put(TITLE_COLUMN, true);

    }

    /**
     * 目前版本（2021年4月28日 移动端v1）固定写死，针对KnowledgeWithNoDetailsEntity拼装字段
     */
    private List<KnowledgeWithNoDetailsEntity> getKnowledgeWithNoDetailsEntity(SearchResponse searchResponse) {

        List<KnowledgeWithNoDetailsEntity> result = new ArrayList<>();

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        if (searchHits.length > 0) {

            for (SearchHit searchHit : searchHits) {
                KnowledgeWithNoDetailsEntity perEntity = null;

                // 把sourceMap拼装回去
                if (searchHit.getSourceAsString() != null) {
                    String source = searchHit.getSourceAsString();
                    perEntity = JSON.parseObject(source, KnowledgeWithNoDetailsEntity.class);
                }

                Map<String, HighlightField> map = searchHit.getHighlightFields();


                // 把高亮字段拼装回去
                if (map != null) {
                    Map<String, List<String>> temp = new HashMap<>();
                    Iterator<Map.Entry<String, HighlightField>> iterator = map.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, HighlightField> entry = iterator.next();
                        if (temp.get(entry.getKey()) == null) {
                            temp.put(entry.getKey(), new ArrayList<>());
                        }
                        for (Text text : entry.getValue().getFragments()) {
                            temp.get(entry.getKey()).add(text.toString());
                        }
                    }
                    perEntity.setHighLight(temp);
                }

                result.add(perEntity);
            }

        }

        return result;


    }


}
