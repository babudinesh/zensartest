package com.zensar.zensartest;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorSwatches {

	private String color;
	
	private String basicColor;
	
	private String skuId;
	private String colorSwatchUrl;
	private String imageUrl;
	@JsonProperty("isAvailable")
	private boolean available;
	
	
	
}
