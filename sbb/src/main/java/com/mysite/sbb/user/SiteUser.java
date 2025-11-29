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
import jakarta.persistence.Transient;


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
	
	@Column(precision=10)
	private Double latitude;

	@Column(precision=10)
	private Double longitude;
	
	@Column(length=20)
	private String departure;    // 출발지
	
	@Column(length=20)
    private String destination;  // 도착지
	
	@Transient
    private Double distance; // DB 컬럼 아님, 계산용
     
	// getter/setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return userId; }
    public void setUsername(String username) { this.userId = username; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }
    
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
	
	// @OneToMany 구현해야 함.
	// private 
	
}