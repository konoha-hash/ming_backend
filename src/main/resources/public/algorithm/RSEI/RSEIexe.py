from CalIntegratedEcoIndex import CalIntegratedEcoIndex
import time
import sys

def RSEIexe(input,output):
    start = time.time()
    cal = CalIntegratedEcoIndex(input,output)
    wet, ndvi, lst, ndbsi, rsei = cal.get_rsei()
    aver_wet, aver_ndvi, aver_lst, aver_ndbsi, aver_rsei = cal.get_average_index(wet, ndvi, lst, ndbsi, rsei)
    # cal.save_result(wet, ndvi, lst, ndbsi, rsei)
    cal.output_tif(wet, ndvi, lst, ndbsi, rsei)
    end = time.time()

    print("归一化后各指数的平均值\n湿度", aver_wet, "绿度", aver_ndvi, "热度", aver_lst, "干度", aver_ndbsi,
          "遥感生态指数", aver_rsei, "Total cost time %.2f s" % (end - start))


# input = "E:\\python\\demo\\calRSEI\\data_dongsheng\\422\\image.tif"
# output = "E:\\python\\demo\\calRSEI\\data_dongsheng\\res_gee"

input = sys.argv[1]
output = sys.argv[2]

RSEIexe(input,output)

