package com.vendingmachine.frontend.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vendingmachine.frontend.entity.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>{
	
	@Query(	value = "SELECT * " +
				"FROM WALLET " +
				"WHERE ((?1 IS NULL OR ?1 = '') OR WALLET_NO LIKE ?1) " + 
				"  AND ((?2 IS NULL OR ?2 = '') OR MEMBER_ID LIKE ?2) " +
				"  AND ((?3 IS NULL OR ?3 = '') OR CREATE_TIME >= ?3) " +
				"  AND ((?4 IS NULL OR ?4 = '') OR CREATE_TIME <= ?4) ",
			nativeQuery = true)
	List<Wallet> queryWallet(String walletNo, String memberId, 
							 Timestamp createTimeStart, Timestamp createTimeEnd);
	
	@Query(	value = "SELECT SUM(AMOUNT) FROM WALLET WHERE MEMBER_ID = ?1", nativeQuery = true)
	Long getBalance(String memberId);
	
	@Query(	value = "SELECT (NVL(MAX(SUBSTR(WALLET_NO,0,13)), TO_CHAR(SYSDATE,'YYYYMMDD')||'00000')+1) " +
					"FROM WALLET WHERE SUBSTR(WALLET_NO,0,8) = TO_CHAR(SYSDATE,'YYYYMMDD')", 
			nativeQuery = true)
	String getWalletNoMaxId();
	
	Page<Wallet> findByMemberIdAndAmountGreaterThan(String memberId, Integer Amount, Pageable pageable);
}
