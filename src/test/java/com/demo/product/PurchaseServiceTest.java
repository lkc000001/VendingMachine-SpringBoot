package com.demo.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vendingmachine.backend.entity.Product;
import com.vendingmachine.backend.entity.Purchase;
import com.vendingmachine.backend.repositories.ProductRepository;
import com.vendingmachine.backend.repositories.PurchaseRepository;
import com.vendingmachine.backend.service.PurchaseService;
import com.vendingmachine.backend.service.impl.PurchaseServiceImpl;
import com.vendingmachine.backend.vo.PurchaseVo;
import com.vendingmachine.enums.SaveFunc;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private ProductRepository productRepository;
    
    @BeforeEach
    void setUp() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    //成功
    @Test
    void save_add_success() {
        // given
        PurchaseVo vo = new PurchaseVo();
        vo.setProductId(1L);
        vo.setQuantity(10);
        vo.setBeforeQuantity(0);

        Product product = new Product();
        product.setStock(100);

        Purchase savedPurchase = new Purchase();

        Mockito.when(purchaseRepository.save(Mockito.any(Purchase.class)))
                .thenReturn(savedPurchase);

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        Mockito.when(purchaseRepository.queryAverageCost(1L))
                .thenReturn(50.0);

        // when
        String result = purchaseService.save(vo, SaveFunc.ADD);

        // then
        assertEquals("新增採購單成功", result);

        assertEquals(110, product.getStock());
        assertEquals(50.0, product.getCost());

        assertThat(result).isEqualTo("新增採購單成功");
        
        Mockito.verify(purchaseRepository).save(Mockito.any(Purchase.class));
        Mockito.verify(productRepository).findById(1L);
    }

    @Test
    void save_add_fail_when_product_not_found() {
        // given
        PurchaseVo vo = new PurchaseVo();
        vo.setProductId(1L);
        vo.setQuantity(10);

        Mockito.when(purchaseRepository.save(Mockito.any()))
                .thenReturn(new Purchase());

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        // when
        String result = purchaseService.save(vo, SaveFunc.ADD);

        // then
        assertThat(result).isEqualTo("新增更新庫存失敗");
    }

}
