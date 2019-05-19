package com.abuqool.sqool.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abuqool.sqool.auth.AuthService;
import com.abuqool.sqool.auth.MgmtGraphQLOps;
import com.abuqool.sqool.service.mgmt.MgmtProductService;
import com.abuqool.sqool.vo.Product;

import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

@GraphQLApi
@Service
public class MgmtListingSchema {

    @Autowired
    private MgmtProductService productService;

    @Autowired
    private AuthService authService;


    @GraphQLMutation
    public Product admSetListing(String token, String schoolCode, int productId, boolean activated, double price) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.MUTATION_SET_LISTING)) {
            return null;
        }
        Product p = productService.setListing(schoolCode, productId, activated, price);
        return p;
    }


    @GraphQLMutation
    public Product admEnableListing(String token, String schoolCode, int productId, double price) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.MUTATION_ENABLE_LISTING)) {
            return null;
        }
        Product p = productService.enableListing(schoolCode, productId, price);
        return p;
    }

    @GraphQLMutation
    public Integer admDisableListing(String token, String schoolCode, List<Integer> productIds) {
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.MUTATION_DISABLE_LISTING)) {
            return null;
        }
        int count = productService.disableListings(schoolCode, productIds);
        return count;
    }

    @GraphQLQuery
    public List<Product> admListings(String token, String schoolCode){
        if(!authService.isAdmReqAllowed(token, MgmtGraphQLOps.QUERY_LISTING)) {
            return null;
        }
        List<Product> list = productService.findListings(schoolCode, false);
        return list;
    }
}
