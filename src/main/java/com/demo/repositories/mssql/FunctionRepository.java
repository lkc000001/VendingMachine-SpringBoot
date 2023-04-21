package com.demo.repositories.mssql;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demo.entity.mssql.Function;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Long>{
	
	List<Function> findByEnabled(String enabled);
	
	@Query(	value = "SELECT * " +
	 				"FROM [FUNCTION] " +
	 				"WHERE ((?1 IS NULL OR ?1 = '') OR functionname LIKE ?1) " +
	 				"  AND ((?2 IS NULL OR ?2 = '') OR functionshowname LIKE ?2)" + 
	 				"  AND ((?3 IS NULL OR ?3 = '') OR functionurl LIKE ?3)" + 
	 				"  AND ((?4 IS NULL OR ?4 = '') OR enabled = ?4) " + 
	 				"  AND ((?5 IS NULL OR ?5 = '') OR type = ?5) " + 
	 				"  AND ((?6 IS NULL OR ?6 = '') OR functiongroup = ?6) ",
			nativeQuery = true)
	Page<Function> queryFunction(String functionName, String functionShowName, String apiglFunctionUrl, String enabled,
									  String type, String functionGroup, Pageable pageable);
	
	
}
