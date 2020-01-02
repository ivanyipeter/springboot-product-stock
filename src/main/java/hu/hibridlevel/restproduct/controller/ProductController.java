package hu.hibridlevel.restproduct.controller;

import hu.hibridlevel.restproduct.dto.ProductDto;
import hu.hibridlevel.restproduct.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Validated
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/admin")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(path = "/ping", method = RequestMethod.GET)
    public ResponseEntity ping() {
        return ResponseEntity.ok("Pong");
    }

    @RequestMapping(path = "/product/{id}", method = RequestMethod.GET)
    public ResponseEntity findProductById(@PathVariable("id") Integer id) {
        ProductDto productDtoById = productService.getProductDtoById(id);
        return ResponseEntity.ok(productDtoById);
    }

    @RequestMapping(path = "/product/", method = RequestMethod.GET)
    public ResponseEntity getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

//    @RequestMapping(path = "/product/stock/{id}", method = RequestMethod.GET)
//    public ResponseEntity getAllProductsWithStockId(@PathVariable("id") Integer id) {
//        return ResponseEntity.ok(productService.getAllProductsWithStockId(id));
//    }

    @RequestMapping(path = "/product/available", method = RequestMethod.GET)
    public ResponseEntity getAvailableProducts() {
        return ResponseEntity.ok(productService.getAllAvailableProducts());
    }

    @RequestMapping(path = "/product/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(path = "/product", method = RequestMethod.POST)
    public ResponseEntity createNewProduct(@RequestBody ProductDto productDto) {
        ProductDto createNewProductDto = productService.createNewProduct(productDto);
        return ResponseEntity.ok(createNewProductDto);
    }

    @RequestMapping(path = "/product/{id}/increase/{num}", method = RequestMethod.PATCH)
    public ResponseEntity increaseProductCount(@PathVariable("id") Integer id, @PathVariable("num") @Min(1) Integer inc) {
        ProductDto productDto = productService.increaseProductCount(id, inc);
        return ResponseEntity.ok(productDto);
    }

    @RequestMapping(path = "/product/{id}/decrease/{num}", method = RequestMethod.PATCH)
    public ResponseEntity decreaseProductCount(@PathVariable("id") Integer id, @PathVariable("num") @Min(1) Integer dec) {
        ProductDto productDto = productService.decreaseProductCount(id, dec);
        return ResponseEntity.ok(productDto);
    }

    @RequestMapping(path = "/product/{id}/update", method = RequestMethod.PUT)
    public ResponseEntity updateProduct(@PathVariable("id") Integer id, @Valid @RequestBody ProductDto productDto) {
        ProductDto updateProductDto = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updateProductDto);
    }
}
