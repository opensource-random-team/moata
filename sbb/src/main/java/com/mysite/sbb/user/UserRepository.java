package com.mysite.sbb.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser,Integer>
{
	Optional<SiteUser> findByUserId(String userId);
	
	boolean existsByUserId(String userId);
	
	boolean existsByPhoneNumber(String phoneNumber);
	
	void deleteByUserId(String userId);
}