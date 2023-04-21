package com.demo.repositories.mssql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.entity.mssql.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	AppUser findByAccount(String account);
	
	@Query(	value = "SELECT * " +
			"FROM [USER] " +
			"WHERE (?1 IS NULL OR userid = ?1) " +
			"  AND ((?2 IS NULL OR ?2 = '') OR branch LIKE ?2) " + 
			"  AND ((?3 IS NULL OR ?3 = '') OR groupname LIKE ?3) " + 
			"  AND (?4 IS NULL OR accountid = ?4) " +
			"  AND ((?5 IS NULL OR ?5 = '') OR account LIKE ?5) " +
			"  AND ((?6 IS NULL OR ?6 = '') OR name LIKE ?6) " +
			"  AND ((?7 IS NULL OR ?7 = '') OR enabled = ?7) ",
		nativeQuery = true)
	Page<AppUser> queryAppUser(Long userId, String branch, String groupName, Integer accountId,
							String account, String name, String enabled, Pageable pageable);

}
