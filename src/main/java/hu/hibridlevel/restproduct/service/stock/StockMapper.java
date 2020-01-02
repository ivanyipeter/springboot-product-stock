package hu.hibridlevel.restproduct.service.stock;

import hu.hibridlevel.restproduct.dto.StockDto;
import hu.hibridlevel.restproduct.model.Stock;
import hu.hibridlevel.restproduct.repository.StockRepository;
import hu.hibridlevel.restproduct.service.product.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockMapper {

    private StockRepository stockRepository;
    private ProductMapper productMapper;

    @Autowired
    public StockMapper(StockRepository stockRepository, ProductMapper productMapper) {
        this.stockRepository = stockRepository;
        this.productMapper = productMapper;
    }

    public StockDto mapDtoFromEntity(Stock stock) {
        StockDto stockDto = new StockDto();
        stockDto.setId(stock.getId());
        stockDto.setName(stock.getName());
        stockDto.setProducts(productMapper.mapDtoListFromEntityList(stock.getProducts()));
        return stockDto;
    }

    public StockDto mapDtoFromEntityForNewStock(Stock stock) {
        StockDto stockDto = new StockDto();
        stockDto.setId(stock.getId());
        stockDto.setName(stock.getName());
        return stockDto;
    }

    public Stock mapEntityFromDto(StockDto stockDto) {
        Stock stock = new Stock();
        stock.setName(stockDto.getName());
        stock.setProducts(productMapper.mapEntityListFromDtoList(stockDto.getProducts()));
        return stock;
    }

    public List<StockDto> mapDtoListFromEntityList(List<Stock> stockList) {
        List<StockDto> stockDtoList = new ArrayList<>();
        for (Stock stock : stockList) {
            stockDtoList.add(mapDtoFromEntity(stock));
        }
        return stockDtoList;
    }



}
