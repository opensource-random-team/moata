package com.mysite.sbb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysite.sbb.comment.CommentService;
import com.mysite.sbb.post.PostRepository;
import com.mysite.sbb.post.Post;
import com.mysite.sbb.post.PostService;
import com.mysite.sbb.recommendation.RecommendationRepository;
import com.mysite.sbb.recommendation.Recommendation;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import com.mysite.sbb.user.UserRepository;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class ProfileController {

	private final UserService userService;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final RecommendationRepository recommendationRepository;
	
	@GetMapping("/profile")
	public String profilePage(Model model)
	{
	    //유저 체크
	    String check_user = userService.getCurrentUserId();
	    if (check_user == null) {
	        return "redirect:/login?needLogin3";
	    }

	    SiteUser user = userService.getCurrentUser();
	    List<Post> myPosts = postRepository.findByUserOrderByCreatedAtDesc(user);
	    List<Recommendation> likedList = recommendationRepository.findByUser(user);

	    model.addAttribute("user", user);
	    model.addAttribute("myPosts", myPosts);
	    model.addAttribute("likedList", likedList);

	    return "profile";
	}

	
	@GetMapping("/profile/edit")
	public String profileEditPage(Model model)
	{
		//유저 체크
		String check_user = userService.getCurrentUserId();
	    if (check_user == null)
    	{
	    	return "redirect:/login?needLogin3";
    	}

	    SiteUser user = userService.getCurrentUser();
	    model.addAttribute("user", user);
	    return "profile_edit";
	}
	
	@PostMapping("/profile/edit")
	public String editProfileSubmit(@ModelAttribute SiteUser formUser) {

	    SiteUser user = userService.getCurrentUser();

	    user.setPhoneNumber(formUser.getPhoneNumber());

	    userRepository.save(user);

	    return "redirect:/profile?edited";
	}
	
	@PostMapping("/profile/delete")
	public String deleteAccount() {
	    SiteUser user = userService.getCurrentUser(); // 현재 로그인 사용자
	    if(user != null) {
	        userRepository.delete(user); // DB에서 삭제
	    }
	    return "redirect:/logout"; // 삭제 후 로그아웃
	}
}
