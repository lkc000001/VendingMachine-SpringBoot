package com.demo.repositories.mssql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demo.entity.mssql.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	
	
	@Query(	value = "SELECT * " +
				"FROM [PRODUCT] " +
				"WHERE (?1 IS NULL OR id = ?1) " +
				"  AND ((?2 IS NULL OR ?2 = '') OR name LIKE ?2) " + 
				"  AND ((?3 IS NULL OR ?3 = '') OR classify LIKE ?3) " + 
				"  AND (?4 IS NULL OR price >= ?4) " +
				"  AND (?5 IS NULL OR price <= ?5) " +
				"  AND (?6 IS NULL OR cost >= ?6) " +
				"  AND (?7 IS NULL OR cost <= ?7) " +
				"  AND ((?8 IS NULL OR ?8 = '') OR image LIKE ?8) " + 
				"  AND ((?9 IS NULL OR ?9 = '') OR enabled = ?9) ",
			nativeQuery = true)
	Page<Product> queryProduct(Long productId, String productName, String classify, Integer priceStart,
								 Integer priceEnd, Integer costStart, Integer costEnd, String image,
								 String enabled, Pageable pageable);

}
