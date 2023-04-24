package com.vendingmachine.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vendingmachine.backend.entity.Product;
import com.vendingmachine.backend.entity.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long>{
	
	
	@Query(	value = "SELECT * " +
				"FROM [PURCHASE] " +
				"WHERE (?1 IS NULL OR id = ?1) " +
				"  AND (?2 IS NULL OR productid = ?2) " + 
				"  AND (?3 IS NULL OR productcost >= ?3) " +
				"  AND (?4 IS NULL OR productcost <= ?4) " +
				"  AND (?5 IS NULL OR quantity >= ?5) " +
				"  AND (?6 IS NULL OR quantity <= ?6) " +
				"  AND ((?7 IS NULL OR ?7 = '') OR createtime >= ?7) " +
				"  AND ((?8 IS NULL OR ?8 = '') OR createtime <= ?8) ",
			nativeQuery = true)
	Page<Purchase> queryPurchase(Long purchaseId, Long productId, Double costStart, Double costEnd,
								 Integer qtyStart, Integer qtyEnd, String createTimeStart, 
								 String createTimeEnd, Pageable pageable);

	@Query(value = "WITH p AS (SELECT productcost * quantity AS totle, quantity " +
				   "FROM [PURCHASE] WHERE productid = ?1) " +
				   "SELECT CAST(SUM(totle)*1.0 / SUM(quantity) AS decimal(8,2)) FROM p", nativeQuery = true)
	Double queryAverageCost(Long productId);
}
