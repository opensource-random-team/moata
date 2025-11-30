package com.mysite.sbb.recommendation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.mysite.sbb.post.Post;
import com.mysite.sbb.user.SiteUser;

public interface RecommendationRepository extends JpaRepository<Recommendation, RecommendationId> {

    boolean existsById(RecommendationId id);

    int countByPost(Post post);  // 추천 개수(좋아요 개수)
    
    List<Recommendation> findByUser(SiteUser user);

    boolean existsByUserAndPost(SiteUser user, Post post);
    
	void deleteByPost(Post post);
}
