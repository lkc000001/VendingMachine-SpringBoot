package com.vendingmachine.frontend.repositories;

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
				"WHERE (?1 IS NULL OR WALLET_ID = TO_NUMBER(?1)) " +
				"  AND ((?2 IS NULL OR ?2 = '') OR WALLET_NO LIKE ?2) " + 
				"  AND ((?3 IS NULL OR ?3 = '') OR MEMBER_ID >= TO_DATE(?4)) " +
				"  AND ((?4 IS NULL OR ?4 = '') OR CREATE_TIME >= TO_DATE(?4)) " +
				"  AND ((?5 IS NULL OR ?5 = '') OR CREATE_TIME <= TO_DATE(?5)) ",
			nativeQuery = true)
	Page<Wallet> queryWallet(Long walletId, String walletNo, String memberId, 
							 String createTimeStart, String createTimeEnd, Pageable pageable );
	
	@Query(	value = "SELECT SUM(AMOUNT) FROM WALLET WHERE MEMBER_ID = ?1", nativeQuery = true)
	Long getBalance(String memberId);
	
	@Query(	value = "SELECT (NVL(MAX(SUBSTR(WALLET_NO,0,13)), TO_CHAR(SYSDATE,'YYYYMMDD')||'00000')+1) " +
					"FROM WALLET WHERE SUBSTR(WALLET_NO,0,8) = TO_CHAR(SYSDATE,'YYYYMMDD')", 
			nativeQuery = true)
	String getWalletNoMaxId();
}
