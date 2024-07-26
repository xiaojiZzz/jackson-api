package com.jackson.apiinterface.controller;

import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jackson.apiclientsdk.exception.ApiException;
import com.jackson.apiclientsdk.model.request.RandomWallpaperRequest;
import com.jackson.apiclientsdk.model.response.RandomWallpaperResponse;
import com.jackson.apiclientsdk.model.response.WeiboHotSearch;
import com.jackson.apiclientsdk.model.response.WeiboHotSearchResponse;
import com.jackson.apiinterface.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.jackson.apiinterface.utils.RequestUtils.buildUrl;
import static com.jackson.apiinterface.utils.RequestUtils.get;

/**
 * 名称 API
 */
@RestController
@RequestMapping("/")
@Slf4j
public class ServiceController {

    @GetMapping("/poisonousChickenSoup")
    public String getPoisonousChickenSoup() {
        return get("https://api.btstu.cn/yan/api.php?charset=utf-8&encode=json");
    }

    @GetMapping("/weiboHotSearch")
    public WeiboHotSearchResponse getWeiboHotSearch() {
        // 1. 访问微博热搜接口
        String responseJson = RequestUtils.get("https://weibo.com/ajax/side/hotSearch");

        // 解析 JSON
        JSONObject jsonObject = JSONObject.parseObject(responseJson);

        // 获取微博的realtime数组
        JSONArray realtimeArray = jsonObject.getJSONObject("data").getJSONArray("realtime");

        // 遍历realtime数组并只保留note、label_name和num字段
        List<WeiboHotSearch> weiboHotList = new ArrayList<>();
        for (int i = 0; i < realtimeArray.size(); i++) {
            JSONObject realtimeObject = realtimeArray.getJSONObject(i);
            JSONObject filteredObject = new JSONObject();
            String note = realtimeObject.getString("note");
            filteredObject.put("index", i + 1);
            filteredObject.put("title", note);
            filteredObject.put("hotType", realtimeObject.getString("label_name"));
            filteredObject.put("hotNum", realtimeObject.getInteger("num"));
            filteredObject.put("url", "https://s.weibo.com/weibo?q=%23" + URLUtil.encode(note) + "%23");
            WeiboHotSearch weiboHotSearch = filteredObject.toJavaObject(WeiboHotSearch.class);
            weiboHotList.add(weiboHotSearch);
        }
        WeiboHotSearchResponse weiboHotSearchResponse = new WeiboHotSearchResponse();
        weiboHotSearchResponse.setWeiboHotSearch(weiboHotList);

        // 3.返回
        return weiboHotSearchResponse;
    }

    @PostMapping("/randomWallpaper")
    public RandomWallpaperResponse getRandomWallpaper(@RequestBody RandomWallpaperRequest randomWallpaperRequest) throws ApiException {
        String baseUrl = "https://api.btstu.cn/sjbz/api.php";
        String url = buildUrl(baseUrl, randomWallpaperRequest);
        if (StringUtils.isAllBlank(randomWallpaperRequest.getLx(), randomWallpaperRequest.getMethod())) {
            url = url + "?format=json";
        } else {
            url = url + "&format=json";
        }
        return JSONUtil.toBean(get(url), RandomWallpaperResponse.class);
    }

}