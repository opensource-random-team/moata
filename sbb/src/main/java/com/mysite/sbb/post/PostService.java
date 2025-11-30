package com.mysite.sbb.post;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserRepository;
import com.mysite.sbb.recommendation.RecommendationRepository;
import com.mysite.sbb.comment.CommentRepository;


@Service
@RequiredArgsConstructor
public class PostService 
{

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RecommendationRepository recommendationRepository;
    private final CommentRepository commentRepository;

    public Post writePost(String title, String content, String category, String userId) 
    {
    	
    		//userId로 SiteUser 조회
    	 	SiteUser user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setCategory(category);
        post.setUser(user);
        //post.setViewCnt(0);

        return postRepository.save(post);
        
    }
    
    public List<Post> searchPosts(String key) 
    {
        return postRepository.findByTitleContainingOrContentContainingOrderByIdDesc(key, key);
    }

    public void stopRecruit(Integer postId) 
    {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        post.setOff(1);  // 모집 중단

        postRepository.save(post);
    }
    
    public int getPostCount() 
    {
        return (int) postRepository.count();
    }
    
    public void updatePost(Integer id, String title, String content, String category) 
    {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        post.setTitle(title);
        post.setContent(content);
        post.setCategory(category);

        postRepository.save(post);
    }
    
    @Transactional
    public void deletePost(Integer id, String userId) 
    {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 본인 글 아닌데 삭제 요청 → 막기
        if (!post.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        
        recommendationRepository.deleteByPost(post); //글에 있는 좋아요 삭제, 디비 옵션에 자동삭제 있는데 귀찮아서 안 씀
        commentRepository.deleteByPost(post);

        postRepository.delete(post);
    }
    
    public Post getPost(Integer id) 
    {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Transactional
    public void deleteUserByUsername(String username) 
    {
    	Optional<SiteUser> userOpt = userRepository.findByUserId(username);

    	if (userOpt.isPresent()) {
    	    SiteUser user = userOpt.get(); // 실제 엔티티 꺼내기
    	    postRepository.deleteAllByUser(user);
    	    userRepository.delete(user);
    	} else {
    	    // 사용자 없으면 예외 처리하거나 그냥 리턴
    	    throw new RuntimeException("User not found: " + username);
    	}

    }


}
