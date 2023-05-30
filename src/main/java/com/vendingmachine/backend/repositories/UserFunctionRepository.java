package com.vendingmachine.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vendingmachine.backend.entity.UserFunction;
import com.vendingmachine.backend.vo.UserFunctionProjection;

@Repository
public interface UserFunctionRepository extends JpaRepository<UserFunction, Long>{

	@Query( value = "WITH p AS ( select uf.FUNCTION_ID, uf.ENABLED, uf.USERFUNCTION_ID " +
			 		"  			from USERFUNCTION uf " + 
					" 			where uf.USER_ID = ?1 ) " +
					"SELECT f.functionId, f.functionName, f.functionShowName, " +
					"		f.functionUrl, f.functionSort, f.functionGroup, f.enabled, " +
					"		f.type, COALESCE(p.ENABLED, 0) AS permissionEnabled, " +
					"		p.USERFUNCTION_ID AS userFunctionId " +
			 		"FROM [FUNCTION] f " +
			 		"LEFT JOIN p ON p.FUNCTION_ID = f.functionid " + 
					"ORDER BY f.functionsort",
			nativeQuery = true)
	List<UserFunctionProjection> queryUserPermission(Long userId);

	@Query( value = "SELECT * " +
					"FROM [USERFUNCTION] " + 
					"WHERE ENABLED = '1' " + 
					"  AND USER_ID = ?1 ",
			nativeQuery = true)
	List<UserFunction> queryUserFunctionByUserIdIsEnable(Long userId);
}
