package com.lsw.ming.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lsw.ming.dao.pojo.DataRaster;
import com.lsw.ming.vo.DataRasterVO;
import com.lsw.ming.vo.param.TimeParams;
import com.lsw.ming.vo.result.Result;

import java.util.List;

public interface DataRasterMapper extends BaseMapper<DataRaster> {
    /**
     * 根据时间参数查询栅格数据
     * @param
     * @return
     */
    List<DataRaster> getRasterByTime(int startyear);
}
