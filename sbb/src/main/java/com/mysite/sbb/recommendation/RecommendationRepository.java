package com.mysite.sbb.recommendation;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.sbb.post.Post;

public interface RecommendationRepository extends JpaRepository<Recommendation, RecommendationId> {

    boolean existsById(RecommendationId id);

    int countByPost(Post post);  // 추천 개수(좋아요 개수)
}
