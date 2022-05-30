"""
利用湿度、绿度、热度、干度计算遥感生态指数
"""
import os

import numpy as np
from scipy import misc
# from osgeo import gdal, gdalconst
from osgeo import gdal
from sklearn.decomposition import PCA


class CalIntegratedEcoIndex:

    def __init__(self, tifFolder, res_save_dir):
        # self.img_width, self.img_height, self.img = self.read_img(img_path)
        self.img = [0]
        self.deno_bias = 0.00001  # 分母偏置，防止除0
        self.res_save_dir = res_save_dir

        # tifNameList = os.listdir(tifFolder)
        # bandnum = ['QA','1','2','3','4','5','6','7','8','9','10','11',]
        # for j in bandnum:
        #     for i in range(len(tifNameList)):
        #         #  判断当前文件是否为tif文件
        #         if (os.path.splitext(tifNameList[i])[1] == ".TIF"):
        #             tifPath = tifFolder + "/" + tifNameList[i]
        #             if os.path.basename(tifPath).split('_')[1]==j:
        #                 print(tifPath)
        #                 img_width,img_height,img,img_geotrans,img_proj = self.read_img(tifPath)
        #                 self.img_width, self.img_height,self.img_geotrans, self.img_proj = img_width,img_height,img_geotrans,img_proj
        #
        #                 img[img==65535] = np.nan
        #                 self.img.append(img)

        self.readTif_gee(tifFolder)


    def readTif_gee(self,fileName):
        dataset = gdal.Open(fileName)
        if dataset == None:
            print(fileName + "文件无法打开")
        im_width = dataset.RasterXSize  # 栅格矩阵的列数
        im_height = dataset.RasterYSize  # 栅格矩阵的行数
        im_geotrans = dataset.GetGeoTransform()  # 获取仿射矩阵信息
        im_proj = dataset.GetProjection()  # 获取投影信息
        im_bands = dataset.RasterCount  # 波段数
        self.img_width, self.img_height,self.img_geotrans, self.img_proj = im_width,im_height,im_geotrans,im_proj

        im_data = dataset.ReadAsArray(0, 0, im_width, im_height)  # 获取数据

        i = 0
        while i < im_bands:
            band = dataset.GetRasterBand(i+1)
            band_array = band.ReadAsArray(0, 0, im_width, im_height)
            # im_Band = im_data[i, 0:im_height, 0:im_width]  # 获取单波段
            self.img.append(band_array)
            i=i+1


    def read_img(self, img_path):
        """读取遥感数据信息"""
        dataset = gdal.Open(img_path, gdalconst.GA_ReadOnly)

        img_width = dataset.RasterXSize
        img_height = dataset.RasterYSize
        img_data = np.array(dataset.ReadAsArray(0, 0, img_width, img_height), dtype=float)  # 将数据写成数组，对应栅格矩阵
        img_geotrans = dataset.GetGeoTransform()
        img_proj = dataset.GetProjection()

        del dataset

        return img_width, img_height, img_data, img_geotrans, img_proj

    def get_wet_degree(self):
        """获取湿度指标"""
        print('获取湿度指标')
        # return 0.2626 * self.img[0] + 0.2141 * self.img[1] + 0.0926 * self.img[2] + \
        #       0.0656 * self.img[3] - 0.7629 * self.img[4] - 0.5388 * self.img[6]

        return 0.1511 * self.img[2] + 0.1973 * self.img[3] + 0.3283 * self.img[4] + \
               0.3407 * self.img[5] - 0.7117 * self.img[6] - 0.4559 * self.img[7]

    def get_green_degree(self):
        """获取绿度指标"""
        print('获取绿度指标')

        ndvi = (self.img[5] - self.img[4]) / (self.img[5] + self.img[4] + self.deno_bias)
        # NDVIsoil = -0.008582
        # NDVIveg = 0.701344
        #
        # pv = (ndvi < NDVIsoil) * 0 + (ndvi > NDVIveg) * 1 + ((ndvi >= NDVIsoil) & (ndvi <= NDVIveg)) * ((ndvi - NDVIsoil)) / (NDVIveg - NDVIsoil)

        return (ndvi)

    def get_temperature(self):
        """获取热度指标"""
        print('获取热度指标')
        # gain = 3.20107  # landsat5 第6波段的增益值
        # bias = 0.25994  # 第6波段的偏置值
        #
        # K1 = 606.09
        # K2 = 1282.71
        # _lambda = 11.45
        # _rho = 1.438e10-2
        # _epsilon = 0.96  # 比辐射率
        #
        # DN = self.img[4] * 299/1000 + self.img[3] * 587/1000 + self.img[2] * 114/1000  # 获取象元灰度值
        #
        # L6 = gain * DN + bias
        # T = K2 / np.log(K1 / L6 + 1)
        # LST = T / (1 + (_lambda * T / _rho) * np.log(_epsilon))


        #########
        # ndvi = (self.img[5] - self.img[4]) / (self.img[5] + self.img[4] + self.deno_bias)
        #
        # NDVIsoil = -0.008582
        # NDVIveg = 0.701344
        #
        # pv = (ndvi < NDVIsoil) * 0 + (ndvi > NDVIveg) * 1 + ((ndvi >= NDVIsoil) & (ndvi <= NDVIveg)) * ((ndvi - NDVIsoil)) / (NDVIveg - NDVIsoil)
        #
        # print(pv)
        #
        # c = 0.004*pv+0.986
        #
        # LST = (self.img[10]/(1 + (0.00109* (self.img[10] / 1.438))*np.log(c)))-273.15
        #

        ndvi = (self.img[5] - self.img[4]) / (self.img[5] + self.img[4] + self.deno_bias)

        NDVIsoil = -0.008582
        NDVIveg = 0.701344

        pv = (ndvi < NDVIsoil) * 0 + (ndvi > NDVIveg) * 1 + ((ndvi >= NDVIsoil) & (ndvi <= NDVIveg)) * ((ndvi - NDVIsoil)) / (NDVIveg - NDVIsoil)

        c = 0.004 * pv + 0.986

        TOA = 0.0003342*self.img[10]+0.1

        L = TOA
        K1 = 774.8853
        K2 = 1321.0789
        BT = (K2/np.log((K1 /L)+1))-273.15

        LST = (BT / (1 + (0.00115 * BT / 1.4388) * np.log(c)))

        return LST

    def get_dryness_degree(self):
        """获取干度指标"""
        print('获取干度指标')
        band6_plus_band4 = self.img[6] + self.img[4]
        band2_plus_band5 = self.img[2] + self.img[5]

        SI = (band6_plus_band4 - band2_plus_band5) / (band6_plus_band4 + band2_plus_band5 + self.deno_bias)

        left_expr = 2 * self.img[6] / (self.img[6] + self.img[5] + self.deno_bias)
        right_expr = self.img[5] / (self.img[4] + self.img[5] + self.deno_bias) + \
                     self.img[3] / (self.img[6] + self.img[3] + self.deno_bias)
        IBI = (left_expr - right_expr) / (left_expr + right_expr + self.deno_bias)

        NDBSI = (IBI + SI) / 2

        return NDBSI

    def arr2img(self, save_path, arr):
        misc.imsave(save_path, arr)

    def normlize(self, img_arr):
        arr = np.array(img_arr)
        return (arr - arr.min()) / (arr.max() - arr.min())

    def get_rsei(self):
        """获取遥感生态指数"""
        wet = self.get_wet_degree()
        ndvi = self.get_green_degree()
        lst = self.get_temperature()
        ndbsi = self.get_dryness_degree()

        # wet = self.normlize(wet)
        # ndvi = self.normlize(ndvi)
        # lst = self.normlize(lst)
        # ndbsi = self.normlize(ndbsi)

        data = np.array([self.normlize(wet), self.normlize(ndvi), self.normlize(lst), self.normlize(ndbsi)])
        data = data.reshape(data.shape[0], -1).T

        pca = PCA(n_components=1)
        rsei = self.normlize(1 - np.reshape(pca.fit_transform(data), newshape=(self.img_height, self.img_width)))

        # return wet, ndvi, lst, ndbsi, 1 - rsei
        return wet, ndvi, lst, ndbsi, rsei


    def save_result(self, wet, ndvi, lst, ndbsi, rsei):
        """将各指数结果保存为图片"""
        res_dict = {"wet": wet, "green": ndvi, "temperature": lst, "dry": ndbsi, "rsei": rsei}

        for k, v in res_dict.items():
            self.arr2img(os.path.join(self.res_save_dir, k + ".jpg"), v)

    def get_average_index(self, wet, ndvi, lst, ndbsi, rsei):
        """获取归一化后各指标的平均值"""
        return np.mean(self.normlize(wet)), np.mean(self.normlize(ndvi)), \
               np.mean(self.normlize(lst)), np.mean(self.normlize(ndbsi)), np.mean(self.normlize(rsei))

    def output_tif(self,wet, ndvi, lst, ndbsi, rsei):
        """将各个指标的tif文件输出"""
        res_dict = {"wet": wet, "green": ndvi, "temperature": lst, "dry": ndbsi, "rsei": rsei}

        for k, v in res_dict.items():
            self.writeTiff(v,self.img_geotrans,self.img_proj,os.path.join(self.res_save_dir,k + ".tif"))

    def writeTiff(self,im_data, im_geotrans, im_proj, path):
        if 'int8' in im_data.dtype.name:
            datatype = gdal.GDT_Byte
        elif 'int16' in im_data.dtype.name:
            datatype = gdal.GDT_UInt16
        else:
            datatype = gdal.GDT_Float32
        if len(im_data.shape) == 3:
            im_bands, im_height, im_width = im_data.shape
        elif len(im_data.shape) == 2:
            im_data = np.array([im_data])
            im_bands, im_height, im_width = im_data.shape
        # 创建文件
        driver = gdal.GetDriverByName("GTiff")
        dataset = driver.Create(path, int(im_width), int(im_height), int(im_bands), datatype)
        if (dataset != None):
            dataset.SetGeoTransform(im_geotrans)  # 写入仿射变换参数
            dataset.SetProjection(im_proj)  # 写入投影
        for i in range(im_bands):
            dataset.GetRasterBand(i + 1).WriteArray(im_data[i])
        del dataset

