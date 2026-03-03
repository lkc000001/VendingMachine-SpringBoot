package com.vendingmachine.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vendingmachine.backend.entity.Group;
import com.vendingmachine.backend.vo.SelectDataProjection;

public interface GroupRepository extends JpaRepository<Group, Long> {

	@Query( value = "SELECT groupid AS id, groupname AS name " +
					"FROM [GROUP] " +
					"WHERE enabled = '1' ",
			nativeQuery = true)
	List<SelectDataProjection> getGroupNameSelectData();

}
