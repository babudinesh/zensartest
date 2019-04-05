package com.zensar.zensartest;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class Product {

    private String productId;
    private String type;
    private String title;
    private String code;
    private double averageRating;
    private long reviews;
    private Price price;
    private String image;
    private Object[] additionalServices;
    private String displaySpecialOffer;
    private PromoMessages promoMessages;
    private String nonPromoMessage;
    private String defaultSkuId;
    private List<ColorSwatches> colorSwatches;
    private long colorSwatchSelected;
    private String colorWheelMessage;
    private boolean outOfStock;
    private boolean emailMeWhenAvailable;
    private String availabilityMessage;
    private boolean compare;
    private String fabric;
    private boolean swatchAvailable;
    private boolean categoryQuickViewEnabled;
    private String swatchCategoryType;
    private String brand;
    private long ageRestriction;
    @JsonProperty("isInStoreOnly")
    private boolean isInStoreOnly;
    @JsonProperty("isMadeToMeasure")
    private boolean isMadeToMeasure;
    @JsonProperty("isBundle")
    private boolean bundle;
    @JsonProperty("isProductSet")
    private boolean productSet;
    private Object[] promotionalFeatures;
    private Object[] features;
    private DynamicAttributes dynamicAttributes;
    private String directorate;
    private long releaseDateTimestamp;
	
}
