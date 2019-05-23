package com.abuqool.sqool.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.abuqool.sqool.auth.AuthService;
import com.abuqool.sqool.auth.MgmtGraphQLOps;
import com.abuqool.sqool.service.mgmt.MgmtProductService;
import com.abuqool.sqool.vo.ColorOption;
import com.abuqool.sqool.vo.Product;
import com.abuqool.sqool.vo.ProductStockUnit;

import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

@GraphQLApi
@Service
public class MgmtProductSchema {

    @Autowired
    private MgmtProductService productService;

    @Autowired
    private AuthService authService;

    @GraphQLQuery
    public List<Product> admProducts(String token, String schoolCode) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.QUERY_PRODUCTS)) {
            return null;
        }
        if(StringUtils.isEmpty(schoolCode)) {
            return productService.findProducts();
        }else {
            return productService.findListings(schoolCode, false);
        }
    }

    @GraphQLMutation
    public Integer admDisableProduct(String token, int id) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.MUTATION_DISABLE_PRODUCT)) {
            return null;
        }
        return productService.disableProduct(id);
    }

    @GraphQLQuery
    public Product admProductDetail(String token, int id, String schoolCode) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.QUERY_PRODUCT)) {
            return null;
        }
        return productService.findProduct(id, schoolCode);
    }

    @GraphQLQuery
    public List<ProductStockUnit> stockUnit(@GraphQLContext Product product){
        List<ProductStockUnit> sku = productService.findSku4Product(product);
        return sku;
    }


    @GraphQLMutation
    public Product admSaveProduct(String token, Integer id, String schoolCode, String code, String title, String picUrl, String gender,
            String category, Double price, Double mktPrice, List<String> detPicUrls, List<String> cfMapping,
            List<ProductStockUnit> sku, List<ColorOption> colors, List<Integer> sizes) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.MUTATION_SAVE_PRODUCT)) {
            return null;
        }
        return productService.saveProduct(
                schoolCode,
                id,
                code,
                title,
                picUrl,
                gender,
                category,
                price,
                mktPrice,
                detPicUrls,
                cfMapping,
                sku,
                colors,
                sizes);
    }
}
