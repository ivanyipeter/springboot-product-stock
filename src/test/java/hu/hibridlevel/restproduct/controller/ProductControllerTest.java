package hu.hibridlevel.restproduct.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.hibridlevel.restproduct.TestConfig;
import hu.hibridlevel.restproduct.dto.ProductDto;
import hu.hibridlevel.restproduct.model.Product;
import hu.hibridlevel.restproduct.model.Stock;
import hu.hibridlevel.restproduct.repository.ProductRepository;
import hu.hibridlevel.restproduct.repository.StockRepository;
import hu.hibridlevel.restproduct.service.exception.ErrorResponse;
import hu.hibridlevel.restproduct.service.product.ProductMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Ignore
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductMapper productMapper;

    private Stock stock = new Stock("testStock", new ArrayList<>());
    private Product product1 = new Product("testProduct1", "testDescription1", 1);
    private Product product2 = new Product("testProduct2", "testDescription2", 2);

    @Before
    public void setupTest() {
        product1.setStock(stock);
        product2.setStock(stock);
        stockRepository.save(stock);
        productRepository.save(product1);
        productRepository.save(product2);
    }

    @After
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    @Test
    public void testPing() throws Exception {
        mockMvc.perform(get("/admin/ping"))
                .andExpect(content().string("Pong"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProductById_withValidIdProvided_shouldReturnProduct() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/product/{id}", product1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        Product actualResult = objectMapper.readValue(result.getResponse().getContentAsString(), Product.class);

        Assert.assertEquals(product1, actualResult);
    }

    @Test
    public void testGetProductById_withInValidIdProvided_shouldReturnNotFoundResponse() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/product/1000"))
                .andExpect(jsonPath("$.error", is("Product not found with requested id: " + 1000)))
                .andExpect(status().is(404)).andReturn();

        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
        Assert.assertEquals(errorResponse.getError(), "Product not found with requested id: " + 1000);
        Assert.assertEquals(errorResponse.getStatus(), 404);
    }

    @Test
    public void testGetAllProducts() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/product/")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        List<ProductDto> actualProductDtoList = Arrays.asList(objectMapper.readValue(contentAsString, ProductDto[].class));
        List<ProductDto> expectedProductDtoList = productMapper.mapDtoListFromEntityList(productRepository.findAll());
        Assert.assertEquals(expectedProductDtoList, actualProductDtoList);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteById_withValidIdProvided_shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/admin/product/{id}", product1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));

        Optional<Product> optionalProduct = productRepository.findById(product1.getId());
        Assert.assertTrue(optionalProduct.isEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteById_withInvalidData_shouldReturnNotFound() throws Exception {
        MvcResult result = mockMvc.perform(delete("/admin/product/1000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404)).andReturn();

        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
        Assert.assertEquals(errorResponse.getError(), "Product not found with requested id: " + 1000);
        Assert.assertEquals(errorResponse.getStatus(), 404);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateNewProduct_withValidData_shouldReturnNewProduct() throws Exception {
        ProductDto productDto = new ProductDto("testCreateNewProductName", "test", 1);
        productDto.setStockId(1);
        String productDtoJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(post("/admin/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productDtoJson))
                .andExpect(status().isOk());

        Optional<Product> product = productRepository.getProductByName("testCreateNewProductName");

        Assert.assertTrue(product.isPresent());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testIncreaseProductCount_withValidData_shouldIncreaseCount() throws Exception {
        Integer id = product1.getId();
        int num = 1;

        mockMvc.perform(patch("/admin/product/{id}/increase/{num}", id, num)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<Product> product = productRepository.findById(id);

        Assert.assertEquals(2, product.get().getCount());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testIncreaseProductCount_withInValidData_shouldReturnViolation() throws Exception {
        Integer id = product1.getId();
        int num = -1;

        MvcResult result = mockMvc.perform(patch("/admin/product/{id}/increase/{num}", id, num)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400)).andReturn();

        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
        Assert.assertEquals(errorResponse.getError(), "increaseProductCount.inc: must be greater than or equal to 1");
        Assert.assertEquals(errorResponse.getStatus(), 400);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDecreaseProductCount_withValidData_shouldDecreaseCount() throws Exception {
        Integer id = product1.getId();
        int num = 1;

        mockMvc.perform(patch("/admin/product/{id}/decrease/{num}", id, num)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<Product> product = productRepository.findById(id);

        Assert.assertEquals(0, product.get().getCount());
    }

    @Test()
    @WithMockUser(roles = "ADMIN")
    public void testDecreaseProductCount_withInvalidDecreaseCount_shouldReturnViolation() throws Exception {
        Integer id = product1.getId();
        int num = -1;

        MvcResult result = mockMvc.perform(patch("/admin/product/{id}/decrease/{num}", id, num)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400)).andReturn();

        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
        Assert.assertEquals(errorResponse.getError(), "decreaseProductCount.dec: must be greater than or equal to 1");
        Assert.assertEquals(errorResponse.getStatus(), 400);

    }

    @Test()
    @WithMockUser(roles = "ADMIN")
    public void testDecreaseProductCount_withInvalidId_shouldReturnException() throws Exception {
        Integer id = 1000;
        int num = 1;

        MvcResult result = mockMvc.perform(patch("/admin/product/{id}/decrease/{num}", id, num)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404)).andReturn();

        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
        Assert.assertEquals(errorResponse.getError(), "Product not found with requested id: " + 1000);
        Assert.assertEquals(errorResponse.getStatus(), 404);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateProduct_withValidData_shouldUpdateUser() throws Exception {
        ProductDto productDto = new ProductDto("testUpdateProductName", "testUpdateProductDescription", 10);
        String productDtoJson = objectMapper.writeValueAsString(productDto);
        Integer id = product1.getId();

        MvcResult result = mockMvc.perform(put("/admin//product/{id}/update", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(productDtoJson))
                .andExpect(status().isOk())
                .andReturn();

        Optional<Product> product = productRepository.findById(id);
//        final ProductDto dto = objectMapper.readValue(result.getResponse().getContentAsString(), ProductDto.class);
//
//        productRepository.findById(id).ifPresent(p -> {
//            Assert.assertEquals(p.getId(), dto.getId());
//            Assert.assertEquals(p.getName(), dto.getName());
//            Assert.assertEquals(p.getDescription(), dto.getDescription());
//            Assert.assertEquals(p.getCount(), dto.getCount());
//        });

        Assert.assertEquals(product.get().getName(), productDto.getName());
        Assert.assertEquals(product.get().getDescription(), productDto.getDescription());
        Assert.assertEquals(product.get().getCount(), productDto.getCount());
    }

}
