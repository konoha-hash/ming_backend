package com.lsw.ming.service;

public interface GeoServerService {

    /**
     * 1.将影像发布到对应的工作空间
     * 参数：影像、工作空间
     */
    void publishWMS(String imgpath);
}
