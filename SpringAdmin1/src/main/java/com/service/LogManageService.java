package com.service;


import com.bean.LogInfo;
import com.bean.LogSearchVo;
import com.bean.MessageResult;

import java.io.IOException;

/**
 *
 **/

public interface LogManageService {
    MessageResult getLogInfoByServiceId(LogSearchVo logSearchVo) throws IOException;

    Object getFile(LogInfo logInfo) throws IOException;

    Object filterLog(String id, String filterContext);
    MessageResult getInstances();
}
