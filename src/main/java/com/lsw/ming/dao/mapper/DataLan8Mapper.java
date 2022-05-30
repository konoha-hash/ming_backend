package com.lsw.ming.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lsw.ming.dao.pojo.DataLan8;

import java.util.List;

public interface DataLan8Mapper extends BaseMapper<DataLan8> {

    List<DataLan8> getDataByTime(int year_s, int year_e, int day_s, int day_e);

}
