package com.mysite.sbb.post;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserRepository;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

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
    
    public void deletePost(Integer id, String userId) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 본인 글 아닌데 삭제 요청 → 막기
        if (!post.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }



}
