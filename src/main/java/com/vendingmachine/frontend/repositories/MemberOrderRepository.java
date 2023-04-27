package com.vendingmachine.frontend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vendingmachine.frontend.entity.MemberOrder;

@Repository
public interface MemberOrderRepository extends JpaRepository<MemberOrder, String>{
	
	@Query(	value = "SELECT * " +
				"FROM MEMBERORDER " +
				"WHERE ((?1 IS NULL OR ?1 = '') OR ORDERID LIKE ?1) " +
				"  AND (?2 IS NULL OR MEMBERID = TO_NUMBER(?2)) " + 
				"  AND (?3 IS NULL OR PRODUCTID = TO_NUMBER(?3)) " + 
				"  AND ((?4 IS NULL OR ?4 = '') OR CREATETIME >= TO_DATE(?4)) " +
				"  AND ((?5 IS NULL OR ?5 = '') OR CREATETIME <= TO_DATE(?5)) ",
			nativeQuery = true)
	Page<MemberOrder> queryMemberOrder(String orderId, Long memberId, Long productId, 
									   String createTimeStart, String createTimeEnd, Pageable pageable );
}
