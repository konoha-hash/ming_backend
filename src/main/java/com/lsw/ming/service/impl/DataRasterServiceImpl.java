package com.lsw.ming.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsw.ming.dao.mapper.DataRasterMapper;
import com.lsw.ming.dao.pojo.DataRaster;
import com.lsw.ming.service.DataRasterService;
import com.lsw.ming.vo.DataRasterVO;
import com.lsw.ming.vo.param.TestParams;
import com.lsw.ming.vo.param.TimeParams;
import com.lsw.ming.vo.result.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataRasterServiceImpl implements DataRasterService {
    @Autowired
    private DataRasterMapper dataRasterMapper;


    @Override
    public Result listRasterData(TestParams testParams) {

        Page<DataRaster> page = new Page<>(testParams.getPage(),testParams.getPageSize());
        LambdaQueryWrapper<DataRaster> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(DataRaster::getId);

        Page<DataRaster> dataRasterPage = dataRasterMapper.selectPage(page,queryWrapper);

        List<DataRaster> records = dataRasterPage.getRecords();

        List<DataRasterVO> rastersVOList = copyList(records);

        return Result.success(rastersVOList);
    }

    @Override
    public Result getRasterByTime(TimeParams timeParams) {

        List<DataRaster> rasterByTime = dataRasterMapper.getRasterByTime(timeParams.getStartyear());

        System.out.println(rasterByTime);

        List<DataRasterVO> rastersVOList = copyList(rasterByTime);

        System.out.println(rastersVOList);

//        return Result.success(rasterByTime);
        return null;
    }


    private List<DataRasterVO> copyList(List<DataRaster> records) {
        List<DataRasterVO> dataRasterVOList = new ArrayList<>();
        for (DataRaster record : records){
            dataRasterVOList.add(copy(record));
        }

        return dataRasterVOList;
    }

    private DataRasterVO copy(DataRaster dataRaster) {
        DataRasterVO dataRasterVO = new DataRasterVO();

        BeanUtils.copyProperties(dataRaster,dataRasterVO);

        return dataRasterVO;

    }

}
