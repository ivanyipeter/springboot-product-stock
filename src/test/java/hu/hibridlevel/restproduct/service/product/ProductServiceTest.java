package hu.hibridlevel.restproduct.service.product;

import hu.hibridlevel.restproduct.dto.ProductDto;
import hu.hibridlevel.restproduct.model.Product;
import hu.hibridlevel.restproduct.repository.ProductRepository;
import hu.hibridlevel.restproduct.service.exception.ProductNotFoundException;
import hu.hibridlevel.restproduct.service.stock.StockService;
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
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private StockService stockService;

    private ProductService underTest;

    private TestDataCreator testDataCreator;

    @Before
    public void setUpUnderTest() {
        underTest = new ProductService(productRepository, productMapper, stockService);
        testDataCreator = new TestDataCreator();
    }

    @Test
    public void testGetproductById_shouldReturnProduct_whenValidDataProvided() {
        Product product = testDataCreator.createProduct(1, "test", "test", 1);
        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(product));

        Product actualResult = underTest.getProductById(1);

        Assert.assertEquals(product, actualResult);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testGetProductById_ShouldThrowProductNotFoundException_WhenInvalidIdProvided() {
        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        underTest.getProductById(1);
    }

    @Test
    public void testGetProductDtoById_shouldReturnProductDto_whenValidIdProvided() {
        ProductDto expectedResult = testDataCreator.createProductDto(1, "test", "test", 1);

        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(new Product()));
        Mockito.when(productMapper.mapDtoFromEntity(new Product())).thenReturn(expectedResult);

        ProductDto actualResult = underTest.getProductDtoById(1);

        Assert.assertEquals(expectedResult, actualResult);
    }

//    @Test
//    public void testCreateProductFromProductDto() {
//        ProductDto productDto = new ProductDto("test", "test", 1);
//        Product expectedResult = new Product("test", "test", 1);
//
//        Product actualResult = underTest.createProductFromProductDto(productDto);
//
//        Assert.assertEquals(expectedResult, actualResult);
//    }

    @Test
    public void testIncreaseProductCount_shouldReturnIncreasedCount_whenValidNumberProvided() {
        ProductDto expectedResult = testDataCreator.createProductDto(1, "test", "test", 2);
        Product product = testDataCreator.createProduct(1, "test", "test", 1);

        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);
        Mockito.when(productMapper.mapDtoFromEntity(Mockito.any(Product.class))).thenReturn(expectedResult);

        ProductDto actualResult = underTest.increaseProductCount(1, 1);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIncreaseCount() {
        Product expectedResult = new Product("test", "test", 2);
        Product product = new Product("test", "test", 1);

        Product actualResult = underTest.increaseCount(product, 1);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDecreaseCount() {
        Product expectedResult = new Product("test", "test", 1);
        Product product = new Product("test", "test", 2);

        Product actualResult = underTest.decreaseCount(product, 1);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSetProduct() {
        Product expectedResult = new Product("updated", "updated", 2);
        Product product = new Product("test", "test", 1);
        ProductDto productDto = new ProductDto("updated", "updated", 2);

        Product actualResult = underTest.setProduct(product, productDto);

        Assert.assertEquals(expectedResult, actualResult);
    }

}

