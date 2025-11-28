package com.mysite.sbb.post;
import java.util.List;

import com.mysite.sbb.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PostRepository extends JpaRepository<Post, Integer> 
{
	//검색&조회
	List<Post> findByTitleContainingOrContentContainingOrderByIdDesc(String title, String content);
	
	List<Post> findByCategoryOrderByIdDesc(String category);
	
	List<Post> findByUserOrderByCreatedAtDesc(SiteUser user);
	
	@Transactional
	void deleteAllByUser(SiteUser user);
	
}