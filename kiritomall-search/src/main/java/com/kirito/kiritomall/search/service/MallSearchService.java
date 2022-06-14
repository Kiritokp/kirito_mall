package com.kirito.kiritomall.search.service;

import com.kirito.kiritomall.search.vo.SearchParam;
import com.kirito.kiritomall.search.vo.SearchResult;

/**
 * @author kirito
 * @description: TODO
 * @date 2022-06-10 09:23
 */
public interface MallSearchService {
    /**
     * @param 检索的所有参数
     * @return 返回查询结果
     */
    SearchResult search(SearchParam param);
}
