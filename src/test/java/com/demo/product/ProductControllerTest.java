package com.demo.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.vendingmachine.VendingMachineSpringBootApplication;
import com.vendingmachine.backend.controller.GlobalController;
import com.vendingmachine.backend.controller.ProductController;
import com.vendingmachine.backend.entity.Product;
import com.vendingmachine.backend.service.ProductService;

/*@WebMvcTest(
	    controllers = ProductController.class,
	    excludeFilters = {
	        @ComponentScan.Filter(
	            type = FilterType.ASSIGNABLE_TYPE,
	            classes = GlobalController.class
	        )
	    }
	)*/
@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = VendingMachineSpringBootApplication.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getProduct_success() throws Exception {
        // given
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Apple");
        product.setPrice(100);
        product.setStock(50);
        product.setEnabled("Y");
        product.setCreateTime(
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse("2025-01-01 10:00:00")
        );

        Mockito.when(productService.getProduct(1L))
               .thenReturn(product);

        // when + then
        mockMvc.perform(get("/Product/getProduct/1"))
        		.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.productName").value("Apple"))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.stock").value(60))
                .andExpect(jsonPath("$.enabled").value("Y"))
                .andExpect(jsonPath("$.createTime")
                        .value("2025-01-01 10:00:00"));

        // 驗證 service 有被呼叫
        Mockito.verify(productService).getProduct(1L);
    	/*mockMvc.perform(get("/Product/getProduct/1"))
        .andExpect(status().isOk());*/
    }
}
