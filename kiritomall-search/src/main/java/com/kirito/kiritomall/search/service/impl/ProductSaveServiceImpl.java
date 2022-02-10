package com.kirito.kiritomall.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kirito.common.to.es.SkuEsModel;
import com.kirito.kiritomall.search.config.ElasticsearchConfig;
import com.kirito.kiritomall.search.constant.EsConstant;
import com.kirito.kiritomall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        // 1.给es建立索引（product）,建立映射关系

        // 2.给es中保存数据（使用bulk批量操作）
        // BulkRequest bulkRequest, RequestOptions options
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            // 构造保存请求数据
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            indexRequest.source(JSONObject.toJSONString(skuEsModel), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, ElasticsearchConfig.COMMON_OPTIONS);

        // TODO 1、如果批量错误
        boolean b = bulk.hasFailures();// 是否异常
        List<String> collect = Arrays.stream(bulk.getItems()).filter(BulkItemResponse::isFailed)
                .map(BulkItemResponse::getId).collect(Collectors.toList());
        log.info("商品上架完成：{}", collect);
        return b;
    }
}
