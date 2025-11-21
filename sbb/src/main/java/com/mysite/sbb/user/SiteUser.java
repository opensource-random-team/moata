package com.mysite.sbb.user;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity 
public class SiteUser{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(length=20, nullable=false)
	private String userId;
	
	@Column(length=200, nullable=false) // 비밀번호
	private String password;
	
	@Column(length=20, nullable=false, unique=true) // 전화번호
	private String phoneNumber;
	
	@Column(length=20)
	private String permission;
	
	@Column(length = 50)
    private LocalDateTime createdAt;
    
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
	
	// @OneToMany 구현해야 함.
	// private 
	
}