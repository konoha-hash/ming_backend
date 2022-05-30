package com.lsw.ming.service.impl;

import com.lsw.ming.dao.mapper.DataLan8Mapper;
import com.lsw.ming.dao.mapper.ResRSEIMapper;
import com.lsw.ming.dao.pojo.DataLan8;
import com.lsw.ming.service.RESIService;
import com.lsw.ming.utils.geoserver.GeoServer;
import com.lsw.ming.vo.param.GSPublish;
import com.lsw.ming.vo.param.RSEIParam;
import com.lsw.ming.vo.param.TimeParams;
import com.lsw.ming.vo.result.Result;
import org.python.antlr.ast.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RESIServiceImpl implements RESIService {

    @Autowired
    private DataLan8Mapper dataLan8Mapper;

    @Autowired
    private ResRSEIMapper resRSEIMapper;

    @Autowired
    private GeoServer geoServer;

    @Override
    public Result getData(TimeParams timeParams) {
        int year_s = timeParams.getStartyear();
        int year_e = timeParams.getEndyear();
        int day_s = timeParams.getStartday();
        int day_e = timeParams.getEndday();

        List<DataLan8> data = dataLan8Mapper.getDataByTime(year_s, year_e, day_s, day_e);



        return Result.success(data);
    }

    @Override
    public Result doAlgorithm(RSEIParam rseiParam) throws IOException, InterruptedException {
        String input = rseiParam.getDatapath();
//        String output = rseiParam.getOutputpath();

        String cmd = getCmd(input)[0];

        Process proc = Runtime.getRuntime().exec(cmd);
        proc.waitFor();

        Date date = new Date();//获取当前的日期
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String strDate = df.format(date);//获取String类型的时间

        String output = getCmd(input)[1] + "\\rsei.tif";

        //将处理得到的结果导入res_rsei数据库
        //写一个方法生成createRSEI的输入参数
        String[] param = getRseiParam(output);

        resRSEIMapper.createResRSEI(strDate,output,strDate,"null");

        //已经获得了rsei结果的路径，需要将其给server发布
        boolean isPublished = geoServer.releaseTiff("rsei" + strDate, output);


        return Result.success("rsei" + strDate);
    }

    @Override
    public Result publish(GSPublish gsPublish) throws MalformedURLException, FileNotFoundException {
        String fileUrl = gsPublish.getImgpath();
        System.out.println(geoServer.getUrl());

        Boolean ispublished =  geoServer.releaseTiff("test0528",fileUrl);

        return Result.success(ispublished);
    }

    private String[] getRseiParam(String output) {



        return null;
    }

    private String[] getCmd(String input) {
        String cmd="";

        //打开conda的dos窗口
        String cmd1 = "D:\\ProgramData\\anaconda\\Scripts\\activate.bat D:\\ProgramData\\anaconda &&";
        //激活geo_env1环境
        String cmd2 = "conda activate D:\\anaconda\\envs\\geo_env1 &&";
        //执行算法文件
        String cmd3 = "python E:\\study\\springboot\\back-end\\src\\main\\resources\\public\\algorithm\\RSEI\\RSEIexe.py";
        //输入执行参数
        String DATA_BASE_PATH = "E:\\study\\springboot\\back-end\\src\\main\\resources\\public\\Data\\";
        String inputpath = input; //算法参数1
        String outputpath = DATA_BASE_PATH + "RSEI\\res"; //算法参数2

        cmd = cmd1+" "+ cmd2 +" "+ cmd3 +" "+ inputpath +" "+ outputpath;

        String[] res= {cmd,outputpath};

        return res;
    }
}
