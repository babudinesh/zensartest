package com.zensar.zensartest;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class GetProductsController {

	@Autowired
	RestTemplate restTemplate;
	/**
	 * This method will return the products based on the sorting order i.e highest price difference first.
	 * ProductDTO is the data transfer object which will be sent through as a list of ProductDTO.
	 * @return
	 */
	@RequestMapping(value="/getProducts")
	public List<ProductDTO> getProducts(){
		List<ProductDTO> productList = getProductsFromRestApi(null);
		productList = productList.stream().sorted(Comparator.comparing(ProductDTO::getPriceDiff).reversed()).collect(Collectors.toList());
		return productList;
		
	}
	/**
	 * This method will return the products based on the sorting order i.e highest price difference first.
	 * This method accepts an request parameter priceLabel based on which the result of the priceLabel will be changed
	 * ProductDTO is the data transfer object which will be sent through as a list of ProductDTO.
	 * @return
	 */
	@RequestMapping(value="/getProducts",params="priceLabel")
	public List<ProductDTO> getProducts(@RequestParam String priceLabel){
		List<ProductDTO> productList = getProductsFromRestApi(priceLabel);
		productList = productList.stream().sorted(Comparator.comparing(ProductDTO::getPriceDiff).reversed()).collect(Collectors.toList());
		return productList;
		
	}
	/**
	 * This method accepts priceLabel as parameter and gives the desired result .
	 * Here in this method the restTemplate line of code is commented as sometimes unable to get the api response
	 * For this to work i have copied the same json result into Product.json file
	 * @param priceLabel
	 * @return
	 */
	public List<ProductDTO> getProductsFromRestApi(String priceLabel) {
		String url ="https://jl-nonprod-syst.apigee.net/v1/categories/600001506/products?key=2ALHCAAs6ikGRBoy6eTHA58RaG097Fma";
	     
		//String result = restTemplate.getForObject(url, String.class);
		//ProductRestDTO product = new ObjectMapper().readValue(result, ProductRestDTO.class);
		/**
		 * Here in this method the restTemplate line of code is commented as sometimes unable to get the api response
		 * For this to work i have copied the same json result into Product.json file
		 */
		InputStream stream = ProductRestDTO.class.getResourceAsStream("/Product.json");
		
		List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();
		ProductDTO product = new ProductDTO();
		ColorSwatchDTO colorSwatchDTO = new ColorSwatchDTO();
		try {
			
			ProductRestDTO productRestDto = new ObjectMapper().readValue(stream,ProductRestDTO.class);
			List<Product> productList = productRestDto.getProducts();
			for (Product productObj : productList) {
				product.setProductId(productObj.getProductId());
				product.setTitle(productObj.getTitle());
				product.setWas((productObj.getPrice().getWas()=="") ? new BigDecimal(0):BigDecimal.valueOf(Double.parseDouble(productObj.getPrice().getWas())));
				product.setNow((productObj.getPrice().getNow()=="") ? new BigDecimal(0):BigDecimal.valueOf(Double.parseDouble(productObj.getPrice().getNow())));
				product.setPriceDiff(product.getWas().subtract(product.getNow()));
				if(priceLabel !=null) {
					if(priceLabel.equalsIgnoreCase("ShowWasNow")) {
						product.setPriceLabel("Was "+Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getWas() +", Now "+Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getNow() );
					}else if(priceLabel.equalsIgnoreCase("ShowWasThenNow")) {
						if(productObj.getPrice().getThen2()=="") {
							if(productObj.getPrice().getThen1()=="") {
								product.setPriceLabel("Was "+Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getWas() +", Now "+Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getNow() );
							}else {
								product.setPriceLabel("Was "+Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getWas() +", Then "+Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getThen1() +", Now "+Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getNow());
							}
						}else {
							product.setPriceLabel("Was "+Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getWas() +", Then "+Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getThen2() +", Now "+Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getNow());
						}
						
					}else if(priceLabel.equalsIgnoreCase("ShowPercDscount")) {
						if(product.getWas().equals(new BigDecimal(0))) {
							product.setPriceLabel(Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getNow());
						}else {
							product.setPriceLabel(product.getNow().divide(product.getWas(),RoundingMode.HALF_EVEN).multiply(new BigDecimal(100)) + "%off - "+Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getNow());
						}
						
					}
					
				}else {
					product.setPriceLabel("Was "+Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getWas() +", Now "+Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getNow());
				}
				List<ColorSwatches> colorSwatchesList = productObj.getColorSwatches();
				List<ColorSwatchDTO> colorSwatchDTOList = new ArrayList<ColorSwatchDTO>();
				for (ColorSwatches colorSwatchObj : colorSwatchesList) {
					colorSwatchDTO.setColor(colorSwatchObj.getColor());
					colorSwatchDTO.setRgbcolor(getColorMap().get(colorSwatchObj.getBasicColor()));
					colorSwatchDTO.setSkuid(colorSwatchObj.getSkuId());
					colorSwatchDTOList.add(colorSwatchDTO);
					colorSwatchDTO = null;
					colorSwatchDTO = new ColorSwatchDTO();
					
				}
				product.setNowPrice(Currency.getInstance(productObj.getPrice().getCurrency()).getSymbol() +""+productObj.getPrice().getNow());
				
				product.setColorswatches(colorSwatchDTOList);
				colorSwatchDTOList = null;
				colorSwatchDTOList = new ArrayList<ColorSwatchDTO>();
				
				if(product.getNow().compareTo(product.getWas()) ==-1) {
					productDTOList.add(product);
				}
				product = null;
				product = new ProductDTO();
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productDTOList;
		
	}
	
	public Map<String,String> getColorMap(){
		Map colorMap = new HashMap();
		colorMap.put("Red", "#FF0000");
		colorMap.put("Pink", "#fb08c8");
		colorMap.put("White", "#ffffff");
		colorMap.put("Blue", "#033bfa");
		colorMap.put("Purple", "#ba03fa");
		colorMap.put("Yellow", "#FFFF00");
		colorMap.put("Multi", "#ba03fa");
		colorMap.put("Black", "#0000");
		colorMap.put("Orange", "#fa3f03");
		colorMap.put("Grey", "#a29d9c");
		colorMap.put("Green", "#04f704");
		return colorMap;
	}
}
