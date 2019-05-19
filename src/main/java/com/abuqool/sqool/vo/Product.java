package com.abuqool.sqool.vo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.abuqool.sqool.dao.common.ListingInfo;
import com.abuqool.sqool.dao.common.ProductInfo;
import com.abuqool.sqool.dao.common.ProductPicInfo;

public class Product extends AbstractCustomKeyProduct{

    private static final Comparator<ProductPicInfo> COMPARATOR = new Comparator<ProductPicInfo>() {

        @Override
        public int compare(ProductPicInfo o1, ProductPicInfo o2) {
            return o1.getSequence() - o2.getSequence();
        }

    };

    public static String colorsToString(List<ColorOption> colors) {
        StringBuilder sb = new StringBuilder();
        for (ColorOption option : colors) {
            sb.append(option.getName());
            sb.append(":");
            sb.append(option.getCode());
            sb.append(";");
        }
        return sb.toString();
    }

    private static List<ColorOption> stringToColors(String colors) {
        List<ColorOption> colorOptions = new ArrayList<>();
        if(!StringUtils.isEmpty(colors)) {
            String[] options = colors.split(";");
            for(String option : options) {
                if(StringUtils.isEmpty(options)) {
                    continue;
                }
                String[] color = option.split(":");
                if(color.length == 2) {
                    ColorOption co = new ColorOption();
                    co.setName(color[0]);
                    co.setCode(color[1]);
                    colorOptions.add(co);
                }
            }
        }
        return colorOptions;
    }

    public static String sizesToString(List<Integer> sizes) {
        StringBuilder sb = new StringBuilder();
        for (Integer size : sizes) {
            sb.append(size);
            sb.append(";");
        }
        return sb.toString();
    }

    private static List<Integer> stringToSizes(String sizes) {
        List<Integer> sizeOptions = new ArrayList<>();
        if(!StringUtils.isEmpty(sizes)) {
            String[] options = sizes.split(";");
            for(String option : options) {
                Integer size = Integer.valueOf(option);
                sizeOptions.add(size);
            }
        }
        return sizeOptions;
    }

    public static Product populate(ProductInfo p, ListingInfo l) {
        Product product = populate(p);
        product.setPrice(l.getListPrice());
        product.setSchoolCode(l.getSchoolCode());
        product.setListingStatus(l.getStatus());
        return product;
    }

    public static Product populate(ProductInfo product) {
        Product p = new Product();
        p.setId(product.getId());
        p.setCode(product.getCode());
        p.setTitle(product.getTitle());
        p.setCategory(product.getCategory());
        p.setGender(product.getGender());
        p.setPrice(product.getPrice());
        p.setMktPrice(product.getMktPrice());
        p.setPicUrl(product.getPicUrl());
        Set<ProductPicInfo> picSet = product.getPicSet();
        if(picSet != null && picSet.size() > 0) {
            List<String> picUrls = new ArrayList<>(picSet.size());
            List<ProductPicInfo> picList = new ArrayList<>(picSet);
            picList.sort(COMPARATOR);
            for(ProductPicInfo pic : picList) {
                picUrls.add(pic.getPicUrl());
            }
            p.setDetPicUrls(picUrls);
        }

        String colors = product.getColorOptions();
        List<ColorOption> colorOptions = stringToColors(colors);
        p.setColors(colorOptions);

        String sizes = product.getSizeOptions();
        List<Integer> sizeOptions = stringToSizes(sizes);
        p.setSizes(sizeOptions);

        p.fromDAO(product);
        return p;
    }

    private String schoolCode;
    private int id;
    private String code;
    private String title;
    private String picUrl;
    private String category;
    private String gender;
    private Double price;
    private Double mktPrice;
    private List<String> detPicUrls;
    private List<ColorOption> colors;
    private List<Integer> sizes;
    private int listingStatus;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getMktPrice() {
        return mktPrice;
    }

    public void setMktPrice(Double mktPrice) {
        this.mktPrice = mktPrice;
    }

    public List<String> getDetPicUrls() {
        return detPicUrls;
    }

    public void setDetPicUrls(List<String> detPicUrls) {
        this.detPicUrls = detPicUrls;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public List<ColorOption> getColors() {
        return colors;
    }

    public void setColors(List<ColorOption> colors) {
        this.colors = colors;
    }

    public List<Integer> getSizes() {
        return sizes;
    }

    public void setSizes(List<Integer> sizes) {
        this.sizes = sizes;
    }

    public int getListingStatus() {
        return listingStatus;
    }

    public void setListingStatus(int listingStatus) {
        this.listingStatus = listingStatus;
    }

}
