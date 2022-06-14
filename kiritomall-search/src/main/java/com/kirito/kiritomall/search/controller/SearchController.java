package com.kirito.kiritomall.search.controller;

import com.kirito.kiritomall.search.service.MallSearchService;
import com.kirito.kiritomall.search.vo.SearchParam;
import com.kirito.kiritomall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author kirito
 * @description: TODO
 * @date 2022-06-10 08:43
 */
@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    /**
     * 自动将页面提交过来的所有请求参数封装成指定的对象
     * @param param
     * @return
     */
    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model){
        SearchResult result=mallSearchService.search(param);
        model.addAttribute("result",result);

        return "list";
    }
}
