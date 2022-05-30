package com.lsw.ming.service;

import com.lsw.ming.vo.param.TestParams;
import com.lsw.ming.vo.param.TimeParams;
import com.lsw.ming.vo.result.Result;

public interface DataRasterService {

    /**
     * 查询所有栅格数据
     * @param testParams
     * @return
     */
    Result listRasterData(TestParams testParams);

    Result getRasterByTime(TimeParams timeParams);
}
