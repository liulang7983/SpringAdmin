package com.controller;


import com.alibaba.fastjson.JSONObject;
import com.bean.LogInfo;
import com.bean.LogSearchVo;
import com.bean.MessageResult;
import com.service.LogManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author ming.li
 * @date 2023/10/17 10:58
 */
@RestController
public class LogManageController {

    @Autowired
    private LogManageService logManageService;

    /**
     *
     * @param searchVo
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/hex/log/getLogList")
    public MessageResult getLogList(LogSearchVo searchVo) throws IOException {
        return logManageService.getLogInfoByServiceId(searchVo);
    }

    /**
     * 下载
     * @param logInfo
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    @RequestMapping(value = "/hex/log/download")
    public Object getFile(LogInfo logInfo) throws IOException {
        return logManageService.getFile(logInfo);
    }

    /**
     *  过滤
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/hex/log/filterLog")
    public Object filterLog(@RequestBody String json){
        JSONObject jsonObject = JSONObject.parseObject(json);
        String id = jsonObject.getString("id");
        String filterContext = jsonObject.getString("filterContext");
        return logManageService.filterLog(id,filterContext);
    }
    /**
     *  过滤
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/hex/log/getInstances")
    public MessageResult getInstances(){
        return logManageService.getInstances();
    }
}
