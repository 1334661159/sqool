package com.abuqool.sqool.service.mgmt;

import java.util.List;
import java.util.Map;

import com.abuqool.sqool.service.common.ProductService;
import com.abuqool.sqool.vo.ColorOption;
import com.abuqool.sqool.vo.Product;
import com.abuqool.sqool.vo.ProductStockUnit;

public interface MgmtProductService extends ProductService{

    List<Product> findProducts();

    Product saveProduct(String schoolCode, Integer id, String code, String title, String picUrl, 
            String gender, String category, Double price, Double mktPrice, List<String> picUrls,
            List<String> cfMapping, List<ProductStockUnit> sku, List<ColorOption> colors, List<Integer> sizes);

    Product findProduct(int id, String schoolCode);

    Integer disableProduct(int id);

    void batchSaveProducts(List<Map<String, String>> data);

    void tryMatchPic(String fileName, String picUrl);

    Product enableListing(String schoolCode, int productId, double price);

    int disableListings(String schoolCode, List<Integer> productIds);

    Product setListing(String schoolCode, int productId, boolean activated, double price);
}
