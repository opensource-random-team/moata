package com.mysite.sbb.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.sbb.chat.ChatMessage;
import com.mysite.sbb.user.UserRepository;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {
	
	private final UserService userService;
	
	private final UserService userService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat") // 클라이언트가 "/app/chat" 으로 보내면 여기 실행됨
    @SendTo("/topic/messages") // 받은 메시지를 "/topic/messages" 구독자에게 보냄
    public ChatMessage send(ChatMessage message, Principal principal) {
    	if (message.getFrom() == null) {
            message.setFrom(principal.getName()); // 로그인한 사용자 이름 채움
        }
        System.out.println("서버가 받은 메시지: " + message.getContent());
        return message; // 이게 클라이언트로 보내지는 내용
    }
    
    @MessageMapping("/private")
    public void sendPrivate(ChatMessage message, Principal principal) {
        // message.getTo() 에게 메시지 전송
        messagingTemplate.convertAndSendToUser(
            message.getTo(),
            "/queue/private",
            message
        );
    }

    
    @GetMapping("/chat")
    public String chatPage(@RequestParam(value = "target", required = false) String targetUser,
                           Model model,
                           Principal principal) {

     	String check_user = userService.getCurrentUserId();
     	
    	if (check_user == null) //로그인 상태인지 확인
    	 {
            return "redirect:/login?needLogin4";
        }
    	
        // 로그인한 사용자 이름
        model.addAttribute("username", principal.getName());
        // 글 작성자(상대방) 이름 받기
        model.addAttribute("targetUser", targetUser);

        return "chat";  // chat.html 로 이동
    }


}