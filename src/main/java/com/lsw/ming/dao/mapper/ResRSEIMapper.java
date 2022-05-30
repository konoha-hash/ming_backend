package com.lsw.ming.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lsw.ming.dao.pojo.ResRSEI;

public interface ResRSEIMapper extends BaseMapper<ResRSEI>{

    //向数据库中添加以生成的RSEI结果
    void createResRSEI(String name,String path,String createTime,String metaData);

}
