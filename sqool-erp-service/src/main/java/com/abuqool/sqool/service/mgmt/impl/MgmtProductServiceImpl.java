package com.abuqool.sqool.service.mgmt.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.abuqool.sqool.biz.BizException;
import com.abuqool.sqool.dao.common.ListingInfo;
import com.abuqool.sqool.dao.common.ProductInfo;
import com.abuqool.sqool.dao.common.ProductPicInfo;
import com.abuqool.sqool.dao.common.ProductStockUnitInfo;
import com.abuqool.sqool.repo.common.ListingRepo;
import com.abuqool.sqool.repo.common.ProductPicRepo;
import com.abuqool.sqool.repo.common.ProductRepo;
import com.abuqool.sqool.service.common.impl.ProductServiceImpl;
import com.abuqool.sqool.service.mgmt.MgmtProductService;
import com.abuqool.sqool.vo.ColorOption;
import com.abuqool.sqool.vo.Product;
import com.abuqool.sqool.vo.ProductStockUnit;

@Service
public class MgmtProductServiceImpl extends ProductServiceImpl implements MgmtProductService{

    protected static final Logger logger = LoggerFactory.getLogger(MgmtProductServiceImpl.class);
    
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ListingRepo listingRepo;

    @Autowired
    private ProductPicRepo productPicRepo;

    private interface ProductExcelConstants {
        String COL_PRODUCT_CODE = "product_code";
        String COL_PRODUCT_TITLE = "product_title";
        String COL_PRODUCT_CATEGORY = "product_category";
        String COL_PRODUCT_GENDER = "product_gender";
        String COL_MKT_PRICE = "product_mkt_price";

        String COL_SKU_CODE = "sku_code";
        String COL_SKU_COLOR = "sku_color";
        String COL_SKU_SIZE = "sku_size";
        String COL_SKU_QTY = "sku_qty";
        String COL_PRICE = "sku_price";
    }

    @Override
    public List<Product> findProducts() {
        List<ProductInfo> products = productRepo.findAll();
        return populateProductList(products);
    }

    private List<Product> populateProductList(List<ProductInfo> products) {
        if(products != null) {
            List<Product> list = new ArrayList<>(products.size());
            for(ProductInfo product : products) {
                if(product.getStatus() != LISTING_STATUS_DELETED) {
                    list.add(Product.populate(product));
                }
            }
            return list;
        }
        return null;
    }

    @Override
    @Transactional
    public Product saveProduct(
            String schoolCode,
            Integer id,
            String code,
            String title,
            String picUrl,
            String gender,
            String category,
            Double price,
            Double mktPrice,
            List<String> picUrls,
            List<String> cfMapping,
            List<ProductStockUnit> sku,
            List<ColorOption> colors,
            List<Integer> sizes) {
        ProductInfo product;
        Date now = new Date();
        if(id != null && id > 0) {
            //update
            product = productRepo.findById(id).get();
            if(product == null) {
                throw new BizException(BizException.Code.ADMIN_INVALID_PARAM);
            }
        }else {
            //create
            product = new ProductInfo();
            product.setCreateTime(now);
        }
        /*
         * create ProductInfo and save
         * create SKU, connect and save
         */
        product.setCode(code);
        product.setGender(gender);
        product.setCategory(category);
        product.setPicUrl(picUrl);
        product.setTitle(title);
        product.setPrice(price);
        product.setMktPrice(mktPrice);
        product.setUpdateTime(now);
        if(cfMapping != null) {
            for(int i=0,l=cfMapping.size();i<l;i++) {
                String def = cfMapping.get(i);
                int fldIdx = i+1;
                try {
                    Method m = ProductInfo.class.getMethod("setCustomFieldKey"+fldIdx, String.class);
                    m.invoke(product, def);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        if(colors != null && colors.size() > 0) {
            product.setColorOptions(Product.colorsToString(colors));
        }else {
//            throw new BizException(BizException.Code.ADMIN_INCORRECT_PARAM);
        }

        if(sizes != null && sizes.size() > 0) {
            product.setSizeOptions(Product.sizesToString(sizes));
        }else {
//            throw new BizException(BizException.Code.ADMIN_INCORRECT_PARAM);
        }

        if(!StringUtils.isEmpty(schoolCode)) {
            if(sku != null && sku.size() > 0) {
                List<ProductStockUnitInfo> list = new ArrayList<>(sku.size());
                Set<Integer> validIds = new HashSet<>();
                for(ProductStockUnit info : sku) {
                    validIds.add(info.getId());
                }
                Set<ProductStockUnitInfo> toDelete = new HashSet<>();
                Map<Integer, ProductStockUnitInfo> map = new HashMap<>();
                for(ProductStockUnitInfo stored : product.getSkuSet()) {
                    if(stored.getCode().contains(schoolCode) && !validIds.contains(stored.getId())) {
                        stored.setStatus(PRODUCT_STATUS_DELETED);
                        toDelete.add(stored);
                    }else {
                        map.put(stored.getId(), stored);
                    }
                }
                for(ProductStockUnit unit : sku) {
                    ProductStockUnitInfo u;
                    if(unit.getId() > 0) {
                        u = map.get(unit.getId());
                        if(u == null) {
                            //skip invalid data, this could only happen by attack or concurrent change
                            continue;
                        }
                    }else {
                        u = new ProductStockUnitInfo();
                        u.setCreateTime(now);
                        list.add(u);
                    }
                    u.setCode(unit.getCode());
                    u.setQuantity(unit.getQuantity());
                    u.setPicUrl(unit.getPicUrl());
                    unit.fillDAO(u);
                    u.setUpdateTime(now);
                    u.setProduct(product);
                }
                productStockUnitRepo.saveAll(list);
                list.removeAll(toDelete);
            }else {
                //delete all existing sku
                Set<ProductStockUnitInfo> skuSet = product.getSkuSet();
                for(ProductStockUnitInfo unit : skuSet) {
                    //soft delete
                    unit.setStatus(PRODUCT_STATUS_DELETED);
                }
                productStockUnitRepo.saveAll(skuSet);
                product.setSkuSet(null);
            }
        }
        productRepo.save(product);

        if(picUrls != null) {
            if(product.getPicSet() != null) {
                productPicRepo.deleteAll(product.getPicSet());
            }
            if(picUrls.size() > 0) {
                List<ProductPicInfo> list = new ArrayList<>(picUrls.size());
                for(int i=0,l=picUrls.size();i<l;i++) {
                    String url = picUrls.get(i);
                    ProductPicInfo p = new ProductPicInfo();
                    p.setCreateTime(now);
                    p.setUpdateTime(now);
                    p.setPicUrl(url);
                    p.setSequence(i);
                    p.setProduct(product);
                    list.add(p);
                }
                productPicRepo.saveAll(list);
                product.setPicSet(new HashSet<>(list));
            }
        }
        return Product.populate(product);
    }

    @Override
    public Product findProduct(int id, String schoolCode) {
        ProductInfo product = productRepo.findById(id).get();
        if(product != null && product.getStatus() != PRODUCT_STATUS_DELETED) {
            if(StringUtils.isEmpty(schoolCode)) {
                return Product.populate(product);
            }else {
                ListingInfo listing = listingRepo.findBySchoolCodeAndProductId(schoolCode, product.getId());
                return Product.populate(product, listing);
            }
        }
        return null;
    }

    @Override
    public Integer disableProduct(int id) {
        ProductInfo product = productRepo.findById(id).get();
        if(product == null) {
            return null;
        }
        product.setStatus(PRODUCT_STATUS_DELETED);
        productRepo.save(product);
        return id;
    }

    @Override
    public void batchSaveProducts(List<Map<String, String>> data) {
        if(data == null || data.size() == 0) {
            return;
        }
        Set<String> codeSet = new HashSet<>();
        for(Map<String, String> row : data) {
            String code = row.get(ProductExcelConstants.COL_PRODUCT_CODE);
            codeSet.add(code);
        }
        List<ProductInfo> existingProducts = productRepo.findByCodeIn(codeSet);
        Map<String, ProductInfo> code2Product = new HashMap<>(existingProducts.size());
        if(existingProducts != null) {
            for(ProductInfo info : existingProducts) {
                code2Product.put(info.getCode(), info);
            }
        }
        for(Map<String, String> row : data) {
            String code = row.get(ProductExcelConstants.COL_PRODUCT_CODE);
            String title = row.get(ProductExcelConstants.COL_PRODUCT_TITLE);
            String category = row.get(ProductExcelConstants.COL_PRODUCT_CATEGORY);
            String gender = row.get(ProductExcelConstants.COL_PRODUCT_GENDER);

            String skuCode = row.get(ProductExcelConstants.COL_SKU_CODE);
            String skuColor = row.get(ProductExcelConstants.COL_SKU_COLOR);
            String skuSize = row.get(ProductExcelConstants.COL_SKU_SIZE);
            skuSize = skuSize.replace("cm", "");
            int skuQty = (int)Double.parseDouble(row.get(ProductExcelConstants.COL_SKU_QTY));
            double price = Double.parseDouble(row.get(ProductExcelConstants.COL_PRICE));
            double mktPrice = Double.parseDouble(row.get(ProductExcelConstants.COL_MKT_PRICE));


            //For product, add on new code, or update. Keep not appeared
            //For SKU, add on new, update same, Keep not appeared
            ProductInfo info = code2Product.get(code);
            if(info == null) {
                //create
                info = new ProductInfo();
                info.setCustomFieldKey1("颜色");
                info.setCustomFieldKey2("尺码");
                info.setCode(code);
                code2Product.put(code, info);
            }
            info.setTitle(title);
            info.setPrice(price);//for now, price is per product
            info.setMktPrice(mktPrice);//for now, price is per product
            info.setCategory(category);
            info.setGender(gender);
            info.setStatus(PRODUCT_STATUS_NORMAL);
            productRepo.save(info);

            //check SKU
            Set<ProductStockUnitInfo> skuSet = info.getSkuSet();
            //TODO refine as map to avoid looping on the same product code for multiple SKU row input
            ProductStockUnitInfo skuInfo = null;
            for(ProductStockUnitInfo sku : skuSet) {
                if(sku.getCode().equals(skuCode)) {
                    skuInfo = sku;
                    break;
                }
            }
            if(skuInfo == null) {
                skuInfo = new ProductStockUnitInfo();
                skuInfo.setCode(skuCode);
                skuInfo.setProduct(info);
            }
            skuInfo.setCustomFieldValue1(skuColor);
            skuInfo.setCustomFieldValue2(skuSize);
            skuInfo.setQuantity(skuQty);
            skuInfo.setPrice(price);
            skuInfo.setMktPrice(mktPrice);
            productStockUnitRepo.save(skuInfo);

        }

    }

    @Override
    public void tryMatchPic(String fileName, String picUrl) {
        logger.info("Start matching file name: "+fileName);

        Date now = new Date();
        ProductInfo product = productRepo.findByCode(fileName);
        if(product != null) {
            product.setPicUrl(picUrl);
            product.setUpdateTime(now);
            productRepo.save(product);
            logger.info("Product "+fileName+" is updated with "+picUrl);
        }else {
            List<ProductStockUnitInfo> skuList = productStockUnitRepo.findAllByCodeLike(fileName);
            if(skuList != null && !skuList.isEmpty()) {
                for(ProductStockUnitInfo sku : skuList) {
                    if(sku.getCode().length() - fileName.length() == 3) {
                        sku.setPicUrl(picUrl);
                        sku.setUpdateTime(now);
                        productStockUnitRepo.save(sku);
                        logger.info("SKU "+fileName+" is updated with "+picUrl);
                    }
                }
            }
        }

        logger.info("Done matching file name: "+fileName);
    }

    @Override
    public Product enableListing(String schoolCode, int productId, double price) {
        return setListing(schoolCode, productId, false, price);
    }

    @Override
    public Product setListing(String schoolCode, int productId, boolean activated, double price) {
        ProductInfo p = productRepo.findById(productId).get();
        if(p == null) {
            throw new BizException(BizException.Code.ADMIN_INVALID_PARAM);
        }
        Date now = new Date();
        ListingInfo l = listingRepo.findBySchoolCodeAndProductId(schoolCode, productId);
        if(l == null) {
            l = new ListingInfo();
            l.setCreateTime(now);
            l.setSchoolCode(schoolCode);
            l.setProductId(productId);
        }
        l.setStatus(activated?LISTING_STATUS_ACTIVE:LISTING_STATUS_IN_STOCK);
        l.setListPrice(price);
        l.setSkuPattern(schoolCode);
        l.setUpdateTime(now);
        listingRepo.save(l);
        return Product.populate(p, l);
    }

    @Override
    @Transactional
    public int disableListings(String schoolCode, List<Integer> productIds) {
        if(productIds == null || productIds.size() == 0) {
            throw new BizException(BizException.Code.ADMIN_INCORRECT_PARAM);
        }
        for(int id : productIds) {
            ProductInfo p = productRepo.findById(id).get();
            if(p == null) {
                throw new BizException(BizException.Code.ADMIN_INVALID_PARAM);
            }
            ListingInfo l = listingRepo.findBySchoolCodeAndProductId(schoolCode, id);
            if(l == null) {
                throw new BizException(BizException.Code.ADMIN_INVALID_PARAM);
            }
            l.setStatus(LISTING_STATUS_DELETED);
            Date now = new Date();
            l.setUpdateTime(now);
            listingRepo.save(l);
        }
        return productIds.size();
    }

}
