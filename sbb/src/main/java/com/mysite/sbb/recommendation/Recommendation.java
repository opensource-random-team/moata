package com.mysite.sbb.recommendation;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.post.Post;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recommendation")
public class Recommendation {

    @EmbeddedId
    private RecommendationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")  // 복합키 userId 매핑
    @JoinColumn(name = "user_id")
    private SiteUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId") // 복합키 postId 매핑
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
