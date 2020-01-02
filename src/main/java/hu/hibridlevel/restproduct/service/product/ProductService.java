package hu.hibridlevel.restproduct.service.product;


import hu.hibridlevel.restproduct.dto.ProductDto;
import hu.hibridlevel.restproduct.model.Product;
import hu.hibridlevel.restproduct.repository.ProductRepository;
import hu.hibridlevel.restproduct.service.exception.ProductModificationException;
import hu.hibridlevel.restproduct.service.exception.ProductNotFoundException;
import hu.hibridlevel.restproduct.service.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private ProductMapper productMapper;
    private StockService stockService;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, StockService stockService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.stockService = stockService;
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public ProductDto getProductDtoById(Integer id) {
        return productMapper.mapDtoFromEntity(getProductById(id));
    }


//    public ProductDto createProduct(ProductDto productDto) {
//        Product product = productMapper.mapEntityFromDto(productDto);
//        System.out.println(product);
//        Product savedProduct = productRepository.save(product);
//        return productMapper.mapDtoFromEntity(savedProduct);
//    }

    public ProductDto updateProduct(Integer id, ProductDto productDto) {
        Product product = getProductById(id);

        // Erre van a productMapper, használd azt az osztályt kérlek
        setProduct(product, productDto);

        Product updatedProduct = productRepository.save(product);
        return productMapper.mapDtoFromEntity(updatedProduct);
    }

    public List<ProductDto> getAllProducts() {
        return productMapper.mapDtoListFromEntityList(productRepository.findAll());
    }

    public List<ProductDto> getAllAvailableProducts() {
        return productMapper.mapDtoListFromEntityList(productRepository.getAvailableProducts());
    }

    public void deleteById(Integer id) {
        Product productById = getProductById(id);
//        productById.setStock(null);
        productRepository.delete(productById);
    }

    public ProductDto increaseProductCount(Integer id, Integer num) {
        Product product = getProductById(id);
        increaseCount(product, num);
        Product updatedProduct = productRepository.save(product);
        return productMapper.mapDtoFromEntity(updatedProduct);
    }

    public ProductDto decreaseProductCount(Integer id, Integer num) {
        Product product = getProductById(id);
        if (product.getCount() - num >= 0) {
            decreaseCount(product, num);
            Product updatedProduct = productRepository.save(product);
            return productMapper.mapDtoFromEntity(updatedProduct);
        } else {
            throw new ProductModificationException("Modification not allowed. Decrease number is:" + num + " actual count:" + product.getCount());
        }
    }

    public Product increaseCount(Product product, Integer num) {
        product.setCount(product.getCount() + num);
        return product;
    }

    public Product decreaseCount(Product product, Integer num) {
        product.setCount(product.getCount() - num);
        return product;
    }

//    public Product createProductFromProductDto(ProductDto productDto) {
//        Product product = new Product();
//        product.setName(productDto.getName());
//        product.setDescription(productDto.getDescription());
//        product.setCount(productDto.getCount());
//        return product;
//    }

    public Product setProduct(Product product, ProductDto productDto) {
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCount(productDto.getCount());
        product.setStock(stockService.getStockById(productDto.getStockId()));
        return product;
    }

    public ProductDto createNewProduct(ProductDto productDto) {
        Product newProduct = productMapper.mapEntityFromDto(productDto);
        newProduct.setStock2(stockService.getStockById(productDto.getStockId()));
        Product savedProduct = productRepository.save(newProduct);
        return productMapper.mapDtoFromEntity(savedProduct);
    }

    public List<ProductDto> getAllProductSFromStockById(Integer id){
        List<Product> productList = productRepository.getAllProductsFromStockWithStockId(id);
        return productMapper.mapDtoListFromEntityList(productList);
    }

}



