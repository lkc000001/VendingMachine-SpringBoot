package com.vendingmachine.frontend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vendingmachine.frontend.entity.Member;
import com.vendingmachine.frontend.entity.MemberOrder;

@Repository
public interface MemberOrderRepository extends JpaRepository<MemberOrder, Long>{
	
	@Query(	value = "SELECT * " +
				"FROM MEMBERORDER " +
				"WHERE ((?1 IS NULL OR ?1 = '') OR ORDER_NO LIKE ?1) " +
				"  AND ((?2 IS NULL OR ?2 = '') OR MEMBER_ID LIKE ?2) " + 
				"  AND (?3 IS NULL OR PRODUCT_ID = TO_NUMBER(?3)) " + 
				"  AND ((?4 IS NULL OR ?4 = '') OR CREATE_TIME >= TO_DATE(?4)) " +
				"  AND ((?5 IS NULL OR ?5 = '') OR CREATE_TIME <= TO_DATE(?5)) ",
			nativeQuery = true)
	Page<MemberOrder> queryMemberOrder(String orderNo, String memberId, Long productId, 
									   String createTimeStart, String createTimeEnd, Pageable pageable );
	
	@Query(	value = "SELECT (NVL(MAX(SUBSTR(ORDER_NO,0,13)), TO_CHAR(SYSDATE,'YYYYMMDD')||'00000')+1) " +
			"FROM MEMBERORDER WHERE SUBSTR(ORDER_NO,0,8) = TO_CHAR(SYSDATE,'YYYYMMDD')", 
	nativeQuery = true)
	String getOrderNoMaxId();
	
	List<MemberOrder> findByMemberId(String memberId);
	
	List<MemberOrder> findByWalletId(Long walletId);
}
