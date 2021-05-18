package com.it.hikvision.mobtool.tool;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 主要是用于Moblie的ES操作，封装了部分API，未跟PC端混用
 */

public class ElasticSearchTools {


    public static SearchSourceBuilder getMultiMatchQueryAndHighLightSourceBuilder(String keywords, Map<String, Float> fieldsAndCart,
                                                                                  Map<String, Boolean> fieldsAndHighLight) {
        return getMultiMatchQueryAndHighLightSourceBuilder(keywords, fieldsAndCart, fieldsAndHighLight, null);
    }

    public static SearchSourceBuilder getMultiMatchQueryAndHighLightSourceBuilder(String keywords, Map<String, Float> fieldsAndCart) {
        return getMultiMatchQueryAndHighLightSourceBuilder(keywords, fieldsAndCart, new HashMap<>(16), null);
    }


    /**
     * @variable： keywords——关键字，
     * fieldsAndCart——fields对应的查询权重
     * fieldsAndHighLight——fields是否需要高亮
     * tieBreaker——返回结果时，低影响力的权重因子。（<=1）
     * @return: searchSourceBuilder
     * @attention: 1. 对keywords、fieldsAndCart、fieldsAndHighLight、tieBreaker均做了判空处理
     * 2. 如果keywords、fieldsAndCart、fieldsAndHighLight为null，直接返回
     * 3. 如果fieldsAndCart、fieldsAndHighLight中的Key为空，不添加进查询语句。如果value为空，如果fieldsAndCart默认该
     * key的权重为1，fieldsAndHighLight不高亮该key
     */
    public static SearchSourceBuilder getMultiMatchQueryAndHighLightSourceBuilder(String keywords, Map<String, Float> fieldsAndCart,
                                                                                  Map<String, Boolean> fieldsAndHighLight, Float tieBreaker) {
        if (StringUtils.isBlank(keywords) || fieldsAndCart == null) {
            return new SearchSourceBuilder();
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MultiMatchQueryBuilder queryBuilder = getMultiMatchQueryBuilder(keywords, fieldsAndCart);

        searchSourceBuilder.query(queryBuilder);
        // 构建语句，准备返回
        // 只有tieBreaker <= 1.0的时候，才设置tiebreaker
        if (tieBreaker != null && tieBreaker <= 1.0) {
            searchSourceBuilder.query(queryBuilder.tieBreaker(tieBreaker));
        } else {

            searchSourceBuilder.query(queryBuilder);
        }
        HighlightBuilder highlightBuilder = getHighLightSourceBuilder(fieldsAndHighLight);

        searchSourceBuilder.highlighter(highlightBuilder);

        System.out.println(searchSourceBuilder.toString());

        // 返回结果
        return searchSourceBuilder;
    }


    /**
     * 获得multiple_match语句
     */

    public static MultiMatchQueryBuilder getMultiMatchQueryBuilder(String keywords, Map<String, Float> fieldsAndCart) {

        List<String> searchFields = new ArrayList<>(fieldsAndCart.size());

        Iterator cartIterator = fieldsAndCart.entrySet().iterator();
        while (cartIterator.hasNext()) {
            Map.Entry<String, Float> temp = (Map.Entry<String, Float>) cartIterator.next();
            // 如果遇到空值,忽略
            if (temp.getKey() == null) {
                continue;
            }
            // 默认权重为1
            final int cart = 1;
            if (temp.getValue() != null) {
                if (temp.getValue() <= 0) {
                    // 加权，拼装field
                    temp.setValue(1.0F);
                }
            } else {
                // 如果权重为空，则默认不加权
                temp.setValue(1.0F);
            }
            searchFields.add(temp.getKey());
        }

        MultiMatchQueryBuilder queryBuilder = new MultiMatchQueryBuilder(keywords, searchFields.toArray(new String[0]));
        queryBuilder.fields(fieldsAndCart);


        // 返回结果
        return queryBuilder;

    }

    /**
     * 获得高亮语句
     */

    public static HighlightBuilder getHighLightSourceBuilder(Map<String, Boolean> fieldsAndHighLight) {
        if (fieldsAndHighLight == null) {
            return new HighlightBuilder();
        }

        HighlightBuilder highlightBuilder = new HighlightBuilder();

        Iterator highLightIterator = fieldsAndHighLight.entrySet().iterator();
        while (highLightIterator.hasNext()) {
            Map.Entry<String, Boolean> temp = (Map.Entry<String, Boolean>) highLightIterator.next();
            // 如果遇到空值，忽略
            if (temp.getKey() == null) {
                continue;
            }
            if (temp.getValue() != null) {
                //需要高亮
                if (temp.getValue()) {
                    // 增加高亮字段进highLightBuilder
                    HighlightBuilder.Field field = new HighlightBuilder.Field(temp.getKey());
                    highlightBuilder.field(field);
                }
            }
        }


        // 返回结果
        return highlightBuilder;
    }

    /**
     * @return
     * @description: 批量获取返回结果的所有sourceMap，也就是拿到数据本体
     */
    public static <T> List<T> getSourceOfResultsList(SearchResponse searchResponse, @NotNull Class<T> clazz) {
        if (searchResponse == null) {
            return new ArrayList<>();
        }
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        String finalString = "";

        // 此处可能有丢失数据的风险
        List<T> result = new ArrayList<T>((int) searchResponse.getHits().totalHits);

        for (SearchHit searchHit : searchHits) {
            if (searchHit.getSourceAsString() != null) {
                String source = searchHit.getSourceAsString();
                T value = JSON.parseObject(source, clazz);
                result.add(value);
            }

        }

        return result;
    }

    public static int getTotalSize(SearchResponse searchResponse) {
        return (int) searchResponse.getHits().totalHits;

    }




}
