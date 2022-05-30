package com.lsw.ming.service;

import com.lsw.ming.vo.param.GSPublish;
import com.lsw.ming.vo.param.RSEIParam;
import com.lsw.ming.vo.param.TestParams;
import com.lsw.ming.vo.param.TimeParams;
import com.lsw.ming.vo.result.Result;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public interface RESIService {

    /**
     * 根据输入的时间查询数据
     * @param
     * @return
     */
    Result getData(TimeParams timeParams);

    /**
     * 输入数据路径和输出路径执行RSEI算法
     * @param rseiParam
     * @return
     */
    Result doAlgorithm(RSEIParam rseiParam) throws IOException, InterruptedException;

    /**
     * 根据输入的影像路径进行GS发布
     * @param gsPublish
     * @return
     */
    Result publish(GSPublish gsPublish) throws MalformedURLException, FileNotFoundException;
}
