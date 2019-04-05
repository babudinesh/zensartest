package com.zensar.zensartest;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ProductDTO {
private String productId;
	
	private String title;
	
	
	private String priceLabel;
	
	private List<ColorSwatchDTO> colorswatches;
	
	private String nowPrice;
	
	@JsonIgnore
	private BigDecimal was;
	@JsonIgnore
	private BigDecimal now;
	@JsonIgnore
	private BigDecimal priceDiff;
	
}
