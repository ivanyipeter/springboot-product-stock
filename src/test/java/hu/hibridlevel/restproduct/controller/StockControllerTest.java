package hu.hibridlevel.restproduct.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.hibridlevel.restproduct.TestConfig;
import hu.hibridlevel.restproduct.dto.StockDto;
import hu.hibridlevel.restproduct.model.Product;
import hu.hibridlevel.restproduct.model.Stock;
import hu.hibridlevel.restproduct.repository.StockRepository;
import hu.hibridlevel.restproduct.service.stock.StockMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockRepository stockRepository;

    private Stock stock = new Stock("testStock", new ArrayList<>());
    private Product product1 = new Product("testProduct1", "testDescription1", 1);
    private Product product2 = new Product("testProduct2", "testDescription2", 2);

    @Before
    public void setupTest() {
        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);
        stock.setProducts(productList);
        stockRepository.save(stock);
    }

    @After
    public void cleanup() {
        stockRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetStockById_withValidId_shouldReturnStock() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/stock/{id}", stock.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        StockDto actualResult = objectMapper.readValue(result.getResponse().getContentAsString(), StockDto.class);
        StockDto stockDto = stockMapper.mapDtoFromEntity(stock);

        Assert.assertEquals(stockDto, actualResult);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional // Transactional because of the lazy initialization used in Stock entity
    public void testGetAllStock() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/stock")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        List<StockDto> actualResult = Arrays.asList(
                objectMapper.readValue(result.getResponse().getContentAsString(), StockDto[].class)
        );
        List<StockDto> expectedResult = stockMapper.mapDtoListFromEntityList(stockRepository.findAll());
//        List<StockDto> expectedResult = stockMapper.mapDtoListFromEntityList(
//                stockRepository.getAllStockWithJoinFetchForTesting()
//        );

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteStockById_withValidId_shouldDeleteStock() throws Exception {
        MvcResult result = mockMvc.perform(delete("/admin/stock/{id}", stock.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204)).andReturn();

        Optional<Stock> optionalStock = stockRepository.findById(stock.getId());

        Assert.assertTrue(optionalStock.isEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateStockName_withValidName_shouldUpdateStockName() throws Exception {
        StockDto stockDto = new StockDto("updatedName");
        String jsonStockDto = objectMapper.writeValueAsString(stockDto);
        MvcResult result = mockMvc.perform(patch("/admin/stock/{id}", stock.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStockDto))
                .andExpect(status().isOk()).andReturn();

        stockRepository.findById(stock.getId()).ifPresent(p -> Assert.assertEquals(stockDto.getName(), p.getName()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateNewStock() throws Exception {
        StockDto stockDto = new StockDto("newStock");
        String jsonStockDto = objectMapper.writeValueAsString(stockDto);
        MvcResult result = mockMvc.perform(post("/admin/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStockDto))
                .andExpect(status().isOk()).andReturn();

        Optional<Stock> optionalStock = stockRepository.getStockByName("newStock");

        Assert.assertTrue(optionalStock.isPresent());
    }
}
