package com.mysite.sbb.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.sbb.post.Post;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
    
 // 댓글 리스트 가져오기 (postId 기준)
    List<Comment> findByPostIdOrderByCreatedAtAsc(Integer postId);
    
    // 댓글 개수 세기
    int countByPostId(Integer postId);
    
    void deleteByPost(Post post);
}


