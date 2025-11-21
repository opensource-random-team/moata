package com.mysite.sbb.post;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> 
{
	//검색&조회
	List<Post> findByTitleContainingOrContentContainingOrderByIdDesc(String title, String content);
	
	List<Post> findByCategoryOrderByIdDesc(String category);
	
}