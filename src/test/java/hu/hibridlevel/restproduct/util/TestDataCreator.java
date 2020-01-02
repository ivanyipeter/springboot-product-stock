package hu.hibridlevel.restproduct.util;

import hu.hibridlevel.restproduct.dto.ProductDto;
import hu.hibridlevel.restproduct.dto.StockDto;
import hu.hibridlevel.restproduct.model.Product;
import hu.hibridlevel.restproduct.model.Stock;

import java.util.ArrayList;
import java.util.List;

public class TestDataCreator {

    public Product createProduct(Integer id, String name, String description, int count) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setCount(count);
        product.setStock(createStock(1, "testStock", new ArrayList<>()));
        return product;
    }

    public ProductDto createProductDto(Integer id, String name, String description, int count) {
        ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setName(name);
        productDto.setDescription(description);
        productDto.setCount(count);
        productDto.setStockId(1);
        return productDto;
    }

    public List<ProductDto> createProductDtoList() {
        List<ProductDto> productDtos = new ArrayList<>();
        ProductDto productDto1 = createProductDto(1, "test", "test", 1);
        ProductDto productDto2 = createProductDto(2, "test2", "test2", 2);
        productDtos.add(productDto1);
        productDtos.add(productDto2);
        return productDtos;
    }

    public List<ProductDto> createProductDtoListWithoutId() {
        List<ProductDto> productDtos = new ArrayList<>();
        ProductDto productDto1 = createProductDto(null, "test", "test", 1);
        ProductDto productDto2 = createProductDto(null, "test2", "test2", 2);
        productDtos.add(productDto1);
        productDtos.add(productDto2);
        return productDtos;
    }

    public List<Product> createProductList() {
        Product product1 = createProduct(null, "test", "test", 1);
        Product product2 = createProduct(null, "test2", "test2", 2);
        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);

        return productList;
    }

    public List<Product> createProductListWithId() {
        Product product1 = createProduct(1, "test", "test", 1);
        Product product2 = createProduct(1, "test2", "test2", 2);
        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);
        return productList;
    }

    public StockDto createStockDto() {
        StockDto stockDto = new StockDto(1, "testStock", new ArrayList<>());
        List<ProductDto> list = createProductDtoListWithoutId();
        list.forEach(p -> p.setStockId(stockDto.getId()));
        stockDto.setProducts(list);
        return stockDto;
    }

    public Stock createStock() {
        Stock stock = new Stock();
        stock.setId(1);
        stock.setName("testStock");
        stock.setProducts(createProductList());
        return stock;
    }

    public Stock createStock(Integer id, String name,List<Product> productList) {
        Stock stock = new Stock();
        stock.setId(id);
        stock.setName(name);
        stock.setProducts(productList);
        return stock;
    }

    public Stock createStock(String name, List<Product> productList){
        return new Stock(name,productList);
    }

    public StockDto createStockDto(String name, List<ProductDto> productDtoList){
        return new StockDto(name,productDtoList);
    }

    public StockDto createStockDto(Integer id, String name, List<ProductDto> productDtoList){
        return new StockDto(id, name,productDtoList);
    }


}



