package com.mysite.sbb.user;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    //암호화 객체?
    private final PasswordEncoder passwordEncoder;

    // 모든 유저 목록 가져오기
    public List<SiteUser> getList() {
        return this.userRepository.findAll();
    }
    
    // id(key)로 유저 가져오기
    public SiteUser getUser(int id) {
        return userRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    // 중복 확인하기
    public boolean isUserDuplicate(String userId) {
    		return userRepository.existsByUserId(userId);
    }
    /*
    public SiteUser login(String userId, String password) {
        SiteUser user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("아이디가 존재하지 않습니다.");
        }

        if (user.getPassword() == null || !user.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user; // 로그인 성공 → user 반환
    }
    */
    public void signup(String userId, String password, String phoneNumber, String passwordRe) {
    		if(userRepository.existsByUserId(userId)) {
    			throw new RuntimeException("이미 사용 중인 아이디입니다.");
    			
    		}
    		
    		if(userRepository.existsByPhoneNumber(phoneNumber)) {
    			throw new RuntimeException("이미 등록된 전화번호입니다.");
    		}
    		
    		if(!password.equals(passwordRe)) {
    			throw new RuntimeException("비밀번호를 다시 입력하십시오.");
    		}
    		
    		
    		SiteUser user = new SiteUser();
    		user.setUserId(userId);
    		//비밀번호 암호화
    		user.setPassword(passwordEncoder.encode(password));
    		// user.setEmail(email);
    		user.setPhoneNumber(phoneNumber);
    		user.setPermission("user");
    		
    		userRepository.save(user);
    }
    
    public String getCurrentUserId() //현재 로그인한 유저 아이디 리턴
    {
    		var auth=SecurityContextHolder.getContext().getAuthentication();

    		 if (auth == null || !(auth.getPrincipal() instanceof UserDetails)) 
    		 {
    			 return null; // 로그인 안 한 상태
    		 }

	    UserDetails user = (UserDetails) auth.getPrincipal();
	    return user.getUsername();
    		
    }
    
   
    public int getUserCount() 
    {
        return (int) userRepository.count();
    }
	
}
