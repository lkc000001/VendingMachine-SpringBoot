package com.vendingmachine.backend.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vendingmachine.backend.entity.UserFunction;
import com.vendingmachine.backend.vo.PermissionVo;
import com.vendingmachine.backend.vo.UserFunctionProjection;

@Repository
public interface UserFunctionRepository extends JpaRepository<UserFunction, Long>{

	@Query( value = "WITH p AS ( select uf.FUNCTION_ID, uf.ENABLED, uf.USERFUNCTION_ID " +
			 		"  			from USERFUNCTION uf " + 
					" 			where uf.USER_ID = ?1 ) " +
					"SELECT f.functionid, f.functionname, f.functionshowname, " +
					"		f.functionurl, f.functionsort, f.enabled, " +
					"		f.type, COALESCE(p.ENABLED, 0) AS permissionenabled, " +
					"		p.USERFUNCTION_ID AS userfunctionid " +
			 		"FROM [FUNCTION] f " +
			 		"LEFT JOIN p ON p.FUNCTION_ID = f.functionid " + 
					"WHERE f.enabled = 1 " + 
					"ORDER BY f.functionsort",
			nativeQuery = true)
	List<UserFunctionProjection> queryUserPermission(Long userId);


}
