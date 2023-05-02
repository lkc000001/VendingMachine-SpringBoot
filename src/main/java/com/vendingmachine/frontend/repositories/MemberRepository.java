package com.vendingmachine.frontend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vendingmachine.frontend.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>{
	
	@Query(	value = "SELECT * " +
				"FROM MEMBER " +
				"WHERE ((?1 IS NULL OR ?1 = '') OR MEMBER_ID LIKE ?1) " +
				"  AND ((?2 IS NULL OR ?2 = '') OR MEMBER_NAME LIKE ?2) " + 
				"  AND ((?3 IS NULL OR ?3 = '') OR ENABLED = ?3) ",
			nativeQuery = true)
	Page<Member> queryMember(String memberId, String memberName, String enabled, 
								  Pageable pageable );
}
