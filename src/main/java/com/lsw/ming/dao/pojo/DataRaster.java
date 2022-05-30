package com.lsw.ming.dao.pojo;

import lombok.Data;

@Data
public class DataRaster {

    private Long id;

    private String img_name;

    private String number_of_band;

    private String band_list;

    private String spacecraft;

    private String map_projection;

    private String projection_units;

    private String coordinate;

    private int year;

    private int day;

    private String path;

}
