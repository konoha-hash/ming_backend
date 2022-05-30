package com.lsw.ming.controller;

import com.lsw.ming.service.RESIService;
import com.lsw.ming.vo.param.GSPublish;
import com.lsw.ming.vo.param.RSEIParam;
import com.lsw.ming.vo.param.TestParams;
import com.lsw.ming.vo.param.TimeParams;
import com.lsw.ming.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("RSEI")
public class RSEIController {

    @Autowired
    private RESIService resiService;

    @PostMapping("getData")
    public Result getData(@RequestBody TimeParams timeParams){
        return resiService.getData(timeParams);
    }

    @PostMapping("doAlgorithm")
    public Result doAlgorithm(@RequestBody RSEIParam rseiParam) throws IOException, InterruptedException {
        return  resiService.doAlgorithm(rseiParam);
    }

    @PostMapping("gspublish")
    public Result gspublish(@RequestBody GSPublish gsPublish) throws MalformedURLException, FileNotFoundException {
        return resiService.publish(gsPublish);
    }

}
