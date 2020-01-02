package hu.hibridlevel.restproduct.service.stock;

import hu.hibridlevel.restproduct.dto.StockDto;
import hu.hibridlevel.restproduct.model.Stock;
import hu.hibridlevel.restproduct.repository.StockRepository;
import hu.hibridlevel.restproduct.service.exception.StockNotFoundException;
import hu.hibridlevel.restproduct.util.TestDataCreator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    private StockService underTest;


    private TestDataCreator testDataCreator;

    @Before
    public void setUpUnderTest() {
        underTest = new StockService(stockRepository, stockMapper);
        testDataCreator = new TestDataCreator();
    }

    @Test
    public void testGetStockById_withValidId_shouldReturnStock() {
        Stock expectedResult = testDataCreator.createStock();
        Mockito.when(stockRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(expectedResult));

        Stock actualResult = underTest.getStockById(1);

        Assert.assertEquals(expectedResult, actualResult);

    }

    @Test(expected = StockNotFoundException.class)
    public void testGetStockById_withInValidId_shouldReturnException(){
        Mockito.when(stockRepository.findById(1)).thenReturn(Optional.empty());

        Stock actualResult = underTest.getStockById(1);
    }

    @Test
    public void testGetStockDtokById() {
        StockDto expectedResult = testDataCreator.createStockDto();
        Mockito.when(stockRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(new Stock()));
        Mockito.when(stockMapper.mapDtoFromEntity(new Stock())).thenReturn(expectedResult);

        StockDto actualResult = underTest.getStockDtoById(1);

        Assert.assertEquals(expectedResult, actualResult);

    }

    @Test
    public void testUpdateStock_withValidName_shouldReturnUpdatedStockName() {
        StockDto expectedReult = testDataCreator.createStockDto(1, "updatedName", testDataCreator.createProductDtoList());
        StockDto stockDto = testDataCreator.createStockDto(1, "updatedName", testDataCreator.createProductDtoList());
        Stock stock = testDataCreator.createStock(1, "name", testDataCreator.createProductList());

        Mockito.when(stockRepository.findById(1)).thenReturn(Optional.of(stock));
        Mockito.when(stockMapper.mapDtoFromEntity(stock)).thenReturn(stockDto);
        Mockito.when(stockRepository.save(stock)).thenReturn(stock);

        StockDto actualResult = underTest.updateStockName(1, stockDto);

        Assert.assertEquals(expectedReult, actualResult);
    }

    @Test
    public void testUpdateName() {
        Stock expectedResult = testDataCreator.createStock(1, "updatedName", testDataCreator.createProductList());
        Stock stock = testDataCreator.createStock(1, "test", testDataCreator.createProductList());
        Mockito.when(stockRepository.findById(1)).thenReturn(Optional.of(stock));

        Stock actualResult = underTest.updateName(1, "updatedName");

        Assert.assertEquals(expectedResult, actualResult);

    }

}
