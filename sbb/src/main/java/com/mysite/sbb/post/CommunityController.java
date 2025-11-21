package com.mysite.sbb.post;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommunityController 
{
	
	private final PostRepository postRepository;
	private final PostService postService;
	private final UserService userService;
	
	@GetMapping("/community")
	public String communityList(@RequestParam(value="category", required=false) String category,
	                            Model model)
	{
	    List<Post> postList;

	    if (category == null || category.equals("전체")) {
	        // 전체 게시글
	        postList = postRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
	    } else {
	        // 특정 카테고리만 필터링
	        postList = postRepository.findByCategoryOrderByIdDesc(category);
	    }

	    model.addAttribute("postList", postList);

	    // 유저 수, 게시글 수
	    model.addAttribute("userCount", userService.getUserCount());
	    model.addAttribute("postCount", postService.getPostCount());

	    // 현재 카테고리 표시용
	    model.addAttribute("currentCategory", category);
	    model.addAttribute("selectedCategory", category);

	    return "community";
	}

	
	@GetMapping("/community_detail/{id}")
    public String community_detail(@PathVariable("id")Integer id, Model model)
    {
		Post post = postRepository.findById(id).orElseThrow(()->new RuntimeException("게시글을 찾을 수 없습니다. "));
		model.addAttribute("post",post); //게시글 정보 전송
		
		String user = userService.getCurrentUserId();

	    if (user == null) {
	        return "redirect:/login?needLogin2";
	    }
		
	    model.addAttribute("user",user);
    		return "community_detail";
    }
	
	@PostMapping("/community_off/{id}")
	public String stopRecruit(@PathVariable Integer id) {

	    postService.stopRecruit(id); // 서비스에서 처리

	    return "redirect:/community_detail/" + id;
	}

	
	@GetMapping("/community_write")
	public String community_write(Model model) 
	{

	    String userId = userService.getCurrentUserId();

	    if (userId == null) {
	        return "redirect:/login?needLogin";
	    }

	    model.addAttribute("userId", userId);
	    return "community_write";
	}
	
	@PostMapping("/community_write")
	public String writeProcess(@RequestParam("title") String title,
	                           @RequestParam("content") String content,
	                           @RequestParam("category") String category,
	                           @RequestParam("userId") String userId) {

	    postService.writePost(title, content, category,userId);

	    return "redirect:/community";
	}
	
	@PostMapping("/community_search")
	public String coummunity_search(@RequestParam("key") String key, Model model)
	{
		List<Post> postList=postService.searchPosts(key);
		
		model.addAttribute("postList",postList);
		model.addAttribute("key",key);
		
		return "community";
	}
	
	@GetMapping("/community_edit/{id}")
	public String community_edit(@PathVariable Integer id, Model model) 
	{

	    Post post = postRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

	    String userId = userService.getCurrentUserId();

	    // 로그인 안되어 있으면 로그인 요구
	    if (userId == null) return "redirect:/login?needLogin";

	    // 본인 글 아닌데 들어오면 막기
	    if (!post.getUser().getUserId().equals(userId)) {
	        return "redirect:/community_detail/" + id + "?noPermission";
	    }

	    // 수정 페이지에 post 정보 전달
	    model.addAttribute("post", post);
	    model.addAttribute("userId", userId);

	    return "community_edit";
	}
	
	@PostMapping("/community_edit/{id}")
	public String community_edit_process(
	        @PathVariable Integer id,
	        @RequestParam String title,
	        @RequestParam String content,
	        @RequestParam String category
	) {
	    Post post = postRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

	    post.setTitle(title);
	    post.setContent(content);
	    post.setCategory(category);

	    postRepository.save(post);

	    return "redirect:/community_detail/" + id;
	}

	

	
	
}
