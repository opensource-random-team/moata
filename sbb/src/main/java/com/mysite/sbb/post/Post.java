package com.mysite.sbb.post;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.*;

import com.mysite.sbb.user.SiteUser; // 유저 엔티티 가져오기

@Getter
@Setter
@Entity 
public class Post{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@ManyToOne //fk 걔 ㅇㅇ
	private SiteUser user;

    @Column(length=100)
    private String category; 

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private Integer viewCnt=0;

    @Column(length = 50)
    private LocalDateTime createdAt;
    
    @Column()
    private Integer Off=0;
    
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
    @Column(length = 50)
    private LocalDateTime modiftDate;
    
    
}