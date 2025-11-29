package com.mysite.sbb.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<SiteUser,Integer>
{
	Optional<SiteUser> findByUserId(String userId);
	
	boolean existsByUserId(String userId);
	
	boolean existsByPhoneNumber(String phoneNumber);
	
	void deleteByUserId(String userId);
	
	 @Query("SELECT u FROM SiteUser u WHERE u.latitude BETWEEN :latMin AND :latMax AND u.longitude BETWEEN :lonMin AND :lonMax")
	List<SiteUser> findUsersInBoundingBox(@Param("latMin") double latMin,
	                                      @Param("latMax") double latMax,
	                                      @Param("lonMin") double lonMin,
	                                      @Param("lonMax") double lonMax);
}