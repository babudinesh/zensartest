package com.zensar.zensartest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductRestDTO {

	private List<Product> products;

    
}
