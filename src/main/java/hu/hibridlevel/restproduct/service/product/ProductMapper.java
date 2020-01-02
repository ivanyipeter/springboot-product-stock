package hu.hibridlevel.restproduct.service.product;

import hu.hibridlevel.restproduct.dto.ProductDto;
import hu.hibridlevel.restproduct.model.Product;
import hu.hibridlevel.restproduct.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductMapper {

    private StockRepository stockRepository;

    @Autowired
    public ProductMapper(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Product mapEntityFromDto(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCount(productDto.getCount());
        return product;
    }

    public ProductDto mapDtoFromEntity(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCount(product.getCount());
        productDto.setStockId(product.getStock().getId());
        productDto.setStockName(product.getStock().getName());
        return productDto;
    }

    public List<ProductDto> mapDtoListFromEntityList(List<Product> products) {

        /*
            Megoldás stream API-val ha esetleg érdekel:
            return products.stream()
                .map(this::mapProductDtoFromEntity)
                .collect(Collectors.toList());
         */

        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(mapDtoFromEntity(product));
        }
        return productDtos;
    }

    public List<Product> mapEntityListFromDtoList(List<ProductDto> productDtoList){
        List<Product> productList = new ArrayList<>();
        for (ProductDto p: productDtoList) {
            productList.add(mapEntityFromDto(p));
        }
        return productList;
    }
}
