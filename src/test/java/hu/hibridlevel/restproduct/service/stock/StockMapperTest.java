package hu.hibridlevel.restproduct.service.stock;

import hu.hibridlevel.restproduct.dto.StockDto;
import hu.hibridlevel.restproduct.model.Stock;
import hu.hibridlevel.restproduct.repository.StockRepository;
import hu.hibridlevel.restproduct.service.product.ProductMapper;
import hu.hibridlevel.restproduct.util.TestDataCreator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class StockMapperTest {

    private StockMapper underTest;

    private ProductMapper productMapper;

    @Mock
    private StockRepository stockRepository;

    private TestDataCreator testDataCreator;

    @Before
    public void setUpUnderTest() {
        testDataCreator = new TestDataCreator();
        underTest = new StockMapper(stockRepository, new ProductMapper(stockRepository));
    }

    @Test
    public void testMapDtoFromEntity(){
        StockDto expectedResult = testDataCreator.createStockDto();

        Stock stock = testDataCreator.createStock();
        StockDto actualResult = underTest.mapDtoFromEntity(stock);

        Assert.assertEquals(expectedResult,actualResult);

    }

    @Test
    public void testMapEntityFromDto(){
        Stock expectedResult = testDataCreator.createStock("test", testDataCreator.createProductList());

        StockDto stockDto = testDataCreator.createStockDto("test", testDataCreator.createProductDtoList());
        Stock actualResult = underTest.mapEntityFromDto(stockDto);

        Assert.assertEquals(expectedResult,actualResult);



    }


}
