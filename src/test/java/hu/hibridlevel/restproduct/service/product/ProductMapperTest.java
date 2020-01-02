package hu.hibridlevel.restproduct.service.product;

import hu.hibridlevel.restproduct.dto.ProductDto;
import hu.hibridlevel.restproduct.model.Product;
import hu.hibridlevel.restproduct.repository.StockRepository;
import hu.hibridlevel.restproduct.service.stock.StockMapper;
import hu.hibridlevel.restproduct.util.TestDataCreator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class ProductMapperTest {

    private TestDataCreator testDataCreator;

    private ProductMapper underTest;

    private StockMapper stockMapper;

    @Mock
    private StockRepository stockRepository;

    @Before
    public void setUpUnderTest() {
        underTest = new ProductMapper(stockRepository);
        testDataCreator = new TestDataCreator();

    }

    @Test
    public void testMapProductDtoFromEntity() {
        ProductDto expectedResult = testDataCreator.createProductDto(1, "test", "test", 1);

        Product product = testDataCreator.createProduct(1, "test", "test", 1);

        ProductDto actualResult = underTest.mapDtoFromEntity(product);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMapDtoListFromEntityList() {
        List<ProductDto> expectedResult = testDataCreator.createProductDtoListWithoutId();
        List<Product> productList = testDataCreator.createProductList();

        List<ProductDto> actualResult = underTest.mapDtoListFromEntityList(productList);

        Assert.assertEquals(expectedResult, actualResult);
    }

}
