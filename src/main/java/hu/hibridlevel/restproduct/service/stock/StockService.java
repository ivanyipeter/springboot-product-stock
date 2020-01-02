package hu.hibridlevel.restproduct.service.stock;

import hu.hibridlevel.restproduct.dto.StockDto;
import hu.hibridlevel.restproduct.model.Stock;
import hu.hibridlevel.restproduct.repository.StockRepository;
import hu.hibridlevel.restproduct.service.exception.StockModificationException;
import hu.hibridlevel.restproduct.service.exception.StockNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {


    private StockRepository stockRepository;
    private StockMapper stockMapper;

    @Autowired
    public StockService(StockRepository stockRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
    }

    public Stock getStockById(Integer id) {
        return stockRepository.findById(id).orElseThrow(() -> new StockNotFoundException(id));
    }

    public StockDto getStockDtoById(Integer id) {
        return stockMapper.mapDtoFromEntity(getStockById(id));
    }

    public List<StockDto> findAll() {
        return stockMapper.mapDtoListFromEntityList(stockRepository.findAll());
    }

    public void deleteStockById(Integer id) {
        Stock stock = getStockById(id);
        stockRepository.delete(stock);
    }

    public StockDto updateStockName(Integer id, StockDto stockDto) {
        if (isStockExists(stockDto)) {
//            Stock stock = getStockById(id);
//            stock.setName(stockDto.getName());
            Stock stock = updateName(id, stockDto.getName());
            Stock updatedStock = stockRepository.save(stock);
            return stockMapper.mapDtoFromEntity(updatedStock);
        } else {
            throw new StockModificationException("Stock name already exists.");
        }
    }

    public Stock updateName(Integer id, String name) {
        Stock stock = getStockById(id);
        stock.setName(name);
        return stock;
    }

    public StockDto createStock(StockDto stockDto) {
        if (isStockExists(stockDto)) {
            Stock stock = new Stock(stockDto.getName());
            stockRepository.save(stock);
            return stockMapper.mapDtoFromEntityForNewStock(stock);

        } else {
            throw new StockModificationException("Stock name already exists.");
        }
    }

    public boolean isStockExists(StockDto stockDto) {
        return !stockRepository.getStockByName(stockDto.getName()).isPresent();
    }



}
