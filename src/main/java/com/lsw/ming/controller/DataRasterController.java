package com.lsw.ming.controller;

import com.lsw.ming.service.DataRasterService;
import com.lsw.ming.vo.param.TestParams;
import com.lsw.ming.vo.param.TimeParams;
import com.lsw.ming.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class DataRasterController {
    @Autowired
    private DataRasterService dataRasterService;

    /**
     * 列出数据库中的栅格数据
     * @param testParams
     * @return
     */
    @RequestMapping("listrasterdata")
    public Result listRasterData(@RequestBody TestParams testParams){
        return dataRasterService.listRasterData(testParams);
    }

    @PostMapping("getbytime")
    public Result getRasterByTime(@RequestBody TimeParams timeParams){
        return  dataRasterService.getRasterByTime(timeParams);
    }


}
