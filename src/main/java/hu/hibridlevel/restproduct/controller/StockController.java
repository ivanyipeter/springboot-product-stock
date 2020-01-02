package hu.hibridlevel.restproduct.controller;


import hu.hibridlevel.restproduct.dto.ProductDto;
import hu.hibridlevel.restproduct.dto.StockDto;
import hu.hibridlevel.restproduct.service.product.ProductService;
import hu.hibridlevel.restproduct.service.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping(path = "/admin")
public class StockController {

    private StockService stockService;

    private ProductService productService;

    @Autowired
    public StockController(StockService stockService, ProductService productService) {
        this.stockService = stockService;
        this.productService = productService;
    }

    @RequestMapping(path = "/stock/{id}", method = RequestMethod.GET)
    public ResponseEntity getStockById(@PathVariable("id") Integer id) {
        StockDto stockDtoById = stockService.getStockDtoById(id);
//        List<ProductDto> x = stockDtoById.getProducts();

        return ResponseEntity.ok(stockDtoById);
    }

    @RequestMapping(path = "/stock", method = RequestMethod.GET)
    public ResponseEntity getAllStock() {
        List<StockDto> all = stockService.findAll();
//        for (StockDto s : all) {
//            List<ProductDto> x = s.getProducts();
//        }
        return ResponseEntity.ok(all);
    }

    @RequestMapping(path = "/stock", method = RequestMethod.POST)
    public ResponseEntity createNewStock(@RequestBody StockDto stockDto) {
        StockDto createNewStockDto = stockService.createStock(stockDto);
        return ResponseEntity.ok(createNewStockDto.getId());
    }

    @RequestMapping(path = "/stock/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteStockById(@PathVariable("id") Integer id) {
        stockService.deleteStockById(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(path = "/stock/{id}", method = RequestMethod.PATCH)
    public ResponseEntity updateStockName(@PathVariable("id") Integer id, @RequestBody StockDto stockDto) {
        StockDto updateStockDto = stockService.updateStockName(id, stockDto);
        return ResponseEntity.ok(updateStockDto);
    }

    @RequestMapping(path ="/stock/{id}/products", method = RequestMethod.GET)
    public ResponseEntity getProductsByStockId(@PathVariable("id") Integer id){
        List<ProductDto> productDtoList = productService.getAllProductSFromStockById(id);
        return ResponseEntity.ok(productDtoList);
    }

}
