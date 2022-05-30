package com.lsw.ming.utils.geoserver;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder;
import it.geosolutions.geoserver.rest.encoder.coverage.GSCoverageEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSGeoTIFFDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder.ProjectionPolicy;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Component
@Data
public class GeoServer {
    private  Logger logger = LoggerFactory.getLogger(GeoServer.class);

    /**
     * geoserver配置
     */
    @Value("${geoserver.url}")
    private  String url;

    @Value("${geoserver.username}")
    private  String geoUsername;

    @Value("${geoserver.password}")
    private  String geoPassword;
    //待发布的图层工作空间
    @Value("${geoserver.workspace}")
    private  String ws;

    private  String stylePath;

//    @Value("${geoserver.url}")
//    public void setUrl(String url){
//        GeoServer.url = url;
//    }
//
//    @Value("${geoserver.username}")
//    public void setGeoUsername(String geoUsername){
//        GeoServer.geoUsername = geoUsername;
//    }
//
//    @Value("${geoserver.password}")
//    public void setGeoPassword(String geoPassword){
//        GeoServer.geoPassword = geoPassword;
//    }
//
//    @Value("${geoserver.workspace}")
//    public void setWs(String workspace) {
//        GeoServer.ws = workspace;
//    }

    /**
     * 判断工作区（workspace）是否存在，不存在则创建
     */
    public  void judgeWorkSpace(String workspace) throws MalformedURLException {
        URL u = new URL(url);
        GeoServerRESTManager manager = new GeoServerRESTManager(u, geoUsername, geoPassword);
        GeoServerRESTPublisher publisher = manager.getPublisher();
        List<String> workspaces = manager.getReader().getWorkspaceNames();
        if (!workspaces.contains(workspace)) {
            boolean createWorkspace = publisher.createWorkspace(workspace);
            logger.info("create workspace : " + createWorkspace);
        } else {
            logger.info("workspace已经存在了,workspace :" + workspace);
        }
    }

    /**
     * 判断存储是否存在
     *
     * @param store 存储名
     * @return boolean
     */
    public  boolean judgeDatabase(String store) {
        try {
            URL u = new URL(url);
            GeoServerRESTManager manager = new GeoServerRESTManager(u, geoUsername, geoPassword);
            RESTDataStore restStore = manager.getReader().getDatastore(ws, store);
            if (restStore == null) {
                logger.info("数据存储不存在，可以创建！");
                return true;
            } else {
                logger.info("数据存储已经存在了,store:" + store);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 直接发布tiff影像到geoServer
     * 将遥感影像经过http传递过去对应的文件及
     *
     * @param storeName 数据存储名/图层名
     * @param fileUrl   本地文件地址
     */
    public  boolean releaseTiff(String storeName, String fileUrl) throws MalformedURLException, FileNotFoundException {
        System.out.println("url打印" +url);
        URL u = new URL(url);
        //创建一个geoServer rest连接对象
        GeoServerRESTManager manager = new GeoServerRESTManager(u, geoUsername, geoPassword);
        //判断数据存储桶是否存在
        RESTDataStore restStore = manager.getReader().getDatastore(ws, storeName);
        //如果不存在就创建一个数据存储，并发布
        if (restStore == null) {
            GSGeoTIFFDatastoreEncoder gsGeoTIFFDatastoreEncoder = new GSGeoTIFFDatastoreEncoder(storeName);
            gsGeoTIFFDatastoreEncoder.setWorkspaceName(ws);
            //不确定是否有用
//            gsGeoTIFFDatastoreEncoder.setUrl(new URL("file:" + fileUrl));
//            gsGeoTIFFDatastoreEncoder.setUrl(new URL("file:" + "test.tif"));
            boolean createStore = manager.getStoreManager().create(ws, gsGeoTIFFDatastoreEncoder);
            logger.info("create store (TIFF文件创建状态) : " + createStore);
            boolean publish = false;

            publish = manager.getPublisher().publishGeoTIFF(ws, storeName, storeName, new File(fileUrl),"EPSG:4326",ProjectionPolicy.REPROJECT_TO_DECLARED,"RSEIstyle");


            logger.info("publish (TIFF文件发布状态) : " + publish);
            if (publish) {
                return true;
            }
        } else {
            logger.info("数据存储已经存在了,store:" + storeName);
        }
        return false;
    }

    /**
     * 上传图层样式文件到geoServer，sld文件
     * 需要放入指定文件夹下
     *
     * @param styleType 样式文件名(不包含sld后缀)
     * @return boolean
     */
    /**
    public static boolean publishStyle(String styleType) {
        try {
            URL u = new URL(url);
            GeoServerRESTManager manager = new GeoServerRESTManager(u, geoUsername, geoPassword);
            GeoServerRESTReader reader = manager.getReader();
            GeoServerRESTPublisher publisher = manager.getPublisher();
            //读取style文件
            String styleFile = stylePath + File.separator + styleType + ".sld";
            File file = new File(styleFile);
            //是否已经发布了改style
            if (!reader.existsStyle(shpWorkspace, styleType)) {
                publisher.publishStyleInWorkspace(shpWorkspace, file, styleType);
            }
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }
     **/
}
