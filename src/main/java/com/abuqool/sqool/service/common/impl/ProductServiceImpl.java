package com.abuqool.sqool.service.common.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.abuqool.sqool.dao.common.ListingInfo;
import com.abuqool.sqool.dao.common.ProductInfo;
import com.abuqool.sqool.dao.common.ProductStockUnitInfo;
import com.abuqool.sqool.repo.common.ListingRepo;
import com.abuqool.sqool.repo.common.ProductRepo;
import com.abuqool.sqool.repo.common.ProductStockUnitRepo;
import com.abuqool.sqool.service.common.ProductService;
import com.abuqool.sqool.vo.Product;
import com.abuqool.sqool.vo.ProductStockUnit;

public class ProductServiceImpl implements ProductService{

    protected static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    protected ProductRepo productRepo;

    @Autowired
    protected ListingRepo listingRepo;

    @Autowired
    protected ProductStockUnitRepo productStockUnitRepo;

    @Override
    public List<ProductStockUnit> findSku4Product(Product product) {
        ProductInfo productInfo = productRepo.findById(product.getId()).get();
        if(productInfo != null) {
            Set<ProductStockUnitInfo> sku = productInfo.getSkuSet();
            return populateSkuList(product.getSchoolCode(), sku, false);
        }
        return null;
    }

    private List<ProductStockUnit> populateSkuList(String schoolCode, Collection<ProductStockUnitInfo> sku, boolean checkQty) {
        if(sku != null) {
            List<ProductStockUnit> list = new ArrayList<>(sku.size());
            if(sku.size() > 0) {
                Map<Integer, ListingInfo> cache = new HashMap<>(sku.size());
                for(ProductStockUnitInfo s : sku) {
                    if(s.getStatus() != PRODUCT_STATUS_DELETED) {//only return sku with stocks
                        boolean valid = false;
                        if(StringUtils.isEmpty(schoolCode)) {
                            valid = true;
                        }else {
                            ListingInfo l = cache.get(s.getProduct().getId());
                            if(l == null) {
                                l = listingRepo.findBySchoolCodeAndProductId(schoolCode, s.getProduct().getId());
                                cache.put(s.getProduct().getId(), l);
                            }
                            if(l != null && l.getStatus() == LISTING_STATUS_ACTIVE && s.getCode().contains(l.getSkuPattern())) {
                                valid = !(checkQty && s.getQuantity() == 0);
                            }
                        }
                        if(valid) {
                            list.add(ProductStockUnit.populate(s));
                        }
                    }
                }
            }
            return list;
        }
        return null;
    }


    @Override
    public List<ProductStockUnit> findSku4School(String schoolCode, List<Integer> idList) {
        List<ProductStockUnitInfo> list = productStockUnitRepo.findAllById(idList);
        if(list != null) {
            List<ProductStockUnit> sku = populateSkuList(schoolCode, list, true);
            return sku;
        }
        return null;
    }

    @Override
    public List<Product> findListings(String schoolCode, int pageNum, String category, String gender) {
        List<Product> listings = findListings(schoolCode, true);

        boolean byCategory = !StringUtils.isEmpty(category);
        boolean byGender = !StringUtils.isEmpty(gender);

        List<Product> products = new ArrayList<>();
        if(byCategory){
            for (Product product: listings) {
                if(category.equals(product.getCategory())) {
                    products.add(product);
                }
            }
        }else if(byGender){
            for (Product product: listings) {
                if(gender.equals(product.getGender())) {
                    products.add(product);
                }
            }
        }else {//no filter, return all with pagination
            int pageSize = 20;
            int start = pageNum * pageSize;
            int end = (pageNum+1) * pageSize;
            for (int i = 0; i < listings.size(); i++) {
                if(start <= i && i < end) {
                    Product product = listings.get(i);
                    products.add(product);
                }
            }
        }

        return products;
    }
    @Override
    public List<Product> findListings(String schoolCode, boolean activatedOnly) {
        List<ListingInfo> list = listingRepo.findBySchoolCode(schoolCode);
        if(list != null) {
            List<Product> pList = new ArrayList<>(list.size());
            for (ListingInfo l : list) {
                if(activatedOnly) {
                    if(l.getStatus() != LISTING_STATUS_ACTIVE) {
                        continue;
                    }
                }else {
                    if(l.getStatus() == LISTING_STATUS_DELETED) {
                        continue;
                    }
                }
                ProductInfo p = productRepo.findById(l.getProductId()).get();
                pList.add(Product.populate(p, l));
            }
            return pList;
        }
        return null;
    }
}
