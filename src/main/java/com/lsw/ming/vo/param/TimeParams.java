package com.lsw.ming.vo.param;

import lombok.Data;

@Data
public class TimeParams {

//    private String startime;
//
//    private String endtime;

    /**
     * 时间请求参数
     */
    private int startyear;  //开始年

    private int startday;   //开始日

    private int endyear;

    private int endday;

}
