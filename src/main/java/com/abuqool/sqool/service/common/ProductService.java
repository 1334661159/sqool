package com.abuqool.sqool.service.common;

import java.util.List;

import com.abuqool.sqool.vo.Product;
import com.abuqool.sqool.vo.ProductStockUnit;

public interface ProductService {

    int PRODUCT_STATUS_NORMAL = 0;
    int PRODUCT_STATUS_DELETED = -1;

    int LISTING_STATUS_DELETED = -1;
    int LISTING_STATUS_IN_STOCK = 0;
    int LISTING_STATUS_ACTIVE = 1;

    List<Product> findListings(String schoolCode, int page, String category, String gender);

    List<ProductStockUnit> findSku4Product(Product product);

    List<ProductStockUnit> findSku4School(String schoolCode, List<Integer> idList);

    List<Product> findListings(String schoolCode, boolean activatedOnly);



}
