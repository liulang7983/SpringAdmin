package com.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bean.LogInfo;
import com.bean.LogSearchVo;
import com.bean.MessageResult;
import com.service.LogManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryClient;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 **/
@Service
public class LogManageServiceImpl implements LogManageService {
    public final Logger logger = LoggerFactory.getLogger(LogManageServiceImpl.class);
    @Autowired
    private ConsulDiscoveryClient discoveryClient;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private RestTemplate restTemplate;



    @Override
    public MessageResult getLogInfoByServiceId(LogSearchVo logSearchVo) throws IOException {
        String serviceId = logSearchVo.getServiceId();
        List<ServiceInstance> instanceList = discoveryClient.getInstances(serviceId);
        List<LogInfo> logs = new ArrayList<>();
        int total = 0;
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("timeLimit", logSearchVo.getGetTime());

        return new MessageResult(true,"s");
    }

    @Override
    public Object getFile(LogInfo logInfo) throws IOException {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        String url = "http://" + logInfo.getServiceName() + "/hex/logapi/getLogFile";
        ResponseEntity<Resource> response = restTemplate.postForEntity(url, paramMap, Resource.class);
        InputStream in = response.getBody().getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int n = 0;
        while ((n = in.read(bytes)) != -1) {
            byteArrayOutputStream.write(bytes, 0, n);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public Object filterLog(String id, String filterContext) {
        StringBuilder stringBuilder = new StringBuilder();
        logger.info("获取端口号:{}和serviceId:{}",serverPort,id);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://127.0.0.1:" + serverPort + "/instances/" + id + "/actuator/logfile", String.class);
        String responseEntityBody = responseEntity.getBody();
        String[] ss = responseEntityBody.split("\n");
        for (String str : ss) {
            if (str.contains(filterContext)) {
                stringBuilder.append(str).append("\r\n");
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public MessageResult getInstances() {
        String url="http://127.0.0.1:" + serverPort + "/applications";
        logger.info("路径："+url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept",MediaType.APPLICATION_JSON_VALUE);
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONArray jsonArray = JSONObject.parseArray(responseEntityBody);
        List<LogInfo> list=new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            JSONArray jsonArray1 = JSONObject.parseArray(jsonObject.getString("instances"));
            for (int j = 0; j < jsonArray1.size(); j++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                LogInfo logInfo = new LogInfo();
                logInfo.setId(jsonObject1.getString("id"));
                logInfo.setServiceName(name);
                logInfo.setServerUrl(jsonObject1.getJSONObject("registration").getString("serviceUrl"));
                list.add(logInfo);
            }
        }
        return new MessageResult(true,list);
    }

}

