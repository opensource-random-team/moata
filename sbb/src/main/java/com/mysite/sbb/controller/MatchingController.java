package com.mysite.sbb.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserRepository;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MatchingController {
	
	private final UserRepository userRepository;
	private final UserService userService;
	
	@GetMapping("/matching")
	public String matchingPage(Model model) {
		
	    String check_user=userService.getCurrentUserId();
	    if(check_user == null)
		{
			return "redirect:/login?needLogin5";
		}

	    // 현재 로그인 유저
	    SiteUser currentUser = userService.getCurrentUser();
	    
	    // 모든 유저 목록
	    List<SiteUser> userList = userService.getList();

	    model.addAttribute("currentUser", currentUser);
	    model.addAttribute("userList", userList);

	    return "matching";
	}

	
	@PostMapping("/user/location")
	@ResponseBody
	public String updateLocation(@RequestParam Double lat, @RequestParam Double lon, Principal principal) {
		
		Optional<SiteUser> userOpt = userRepository.findByUserId(principal.getName());

        if (userOpt.isEmpty()) {
            return "user not found";
        }

        SiteUser user = userOpt.get();
        
	    user.setLatitude(lat);
	    user.setLongitude(lon);
	    userRepository.save(user);

	    return "ok";
	}
	
	@PostMapping("/setRoute")
    public ResponseEntity<String> setRoute(
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam String departure,
            @RequestParam String destination,
            Principal principal) {
		Optional<SiteUser> userOpt = userRepository.findByUserId(principal.getName());
        SiteUser user = userOpt.get();
        user.setLatitude(lat);
        user.setLongitude(lon);
        user.setDeparture(departure);
        user.setDestination(destination);
        userRepository.save(user);

        return ResponseEntity.ok("출발지/도착지 저장 완료");
    }
	
	@PostMapping("/user/setRoute")
	@ResponseBody
	public String setRoute(
	        @RequestParam double lat,
	        @RequestParam double lon,
	        @RequestParam String departure,
	        @RequestParam String destination) {

	    SiteUser user = userService.getCurrentUser();
	    if (user == null) return "NOT_LOGGED_IN";

	    user.setLatitude(lat);
	    user.setLongitude(lon);
	    user.setDeparture(departure);
	    user.setDestination(destination);

	    userRepository.save(user);
	    return "OK";
	}

	
	// 근처 사용자 조회
	@GetMapping("/user/nearby")
	@ResponseBody
	public List<Map<String, Object>> nearbyUsers(@RequestParam Double lat,
	                                             @RequestParam Double lon,
	                                             @RequestParam Double radius) {

	    double latDistance = radius / 111.0;
	    double lonDistance = radius / (111.0 * Math.cos(Math.toRadians(lat)));

	    double latMin = lat - latDistance;
	    double latMax = lat + latDistance;
	    double lonMin = lon - lonDistance;
	    double lonMax = lon + lonDistance;

	    List<SiteUser> candidates = userRepository.findUsersInBoundingBox(latMin, latMax, lonMin, lonMax);

	    SiteUser currentUser = userService.getCurrentUser();
	    
	    List<Map<String, Object>> result = new ArrayList<>();
	    for (SiteUser u : candidates) {
	    	
	    	if (u.getUserId().equals(currentUser.getUserId())) continue; // 자기 자신 제외

	        // 위치 null인 경우 스킵
	        if (u.getLatitude() == null || u.getLongitude() == null) continue;

	        double distance = vincentyDistance(lat, lon, u.getLatitude(), u.getLongitude());
	        if (distance <= radius) {

	            Map<String, Object> map = new HashMap<>();
	            map.put("username", u.getUsername());
	            map.put("distance", distance);
	            map.put("latitude", u.getLatitude());
	            map.put("longitude", u.getLongitude());

	            // 🔥 추가한 부분: 출발지 / 도착지
	            map.put("departure", u.getDeparture());
	            map.put("destination", u.getDestination());
	            
	            map.put("userId", u.getUserId());  // 추가

	            result.add(map);
	        }
	    }

	    result.sort(Comparator.comparingDouble(m -> (Double)m.get("distance")));
	    return result;
	}


    // Vincenty 거리 계산 (km 단위)
    private double vincentyDistance(double lat1, double lon1, double lat2, double lon2) {
        final double a = 6378137;
        final double f = 1 / 298.257223563;
        final double b = (1 - f) * a;

        double φ1 = Math.toRadians(lat1);
        double φ2 = Math.toRadians(lat2);
        double U1 = Math.atan((1 - f) * Math.tan(φ1));
        double U2 = Math.atan((1 - f) * Math.tan(φ2));
        double L = Math.toRadians(lon2 - lon1);
        double λ = L;
        double sinσ, cosσ, σ, sinα, cos2α, cos2σm;
        double λP;
        int iterLimit = 100;
        do {
            double sinλ = Math.sin(λ), cosλ = Math.cos(λ);
            sinσ = Math.sqrt(
                Math.pow(Math.cos(U2) * sinλ, 2) +
                Math.pow(Math.cos(U1) * Math.sin(U2) - Math.sin(U1) * Math.cos(U2) * cosλ, 2)
            );
            if (sinσ == 0) return 0;
            cosσ = Math.sin(U1) * Math.sin(U2) + Math.cos(U1) * Math.cos(U2) * cosλ;
            σ = Math.atan2(sinσ, cosσ);
            sinα = Math.cos(U1) * Math.cos(U2) * sinλ / sinσ;
            cos2α = 1 - sinα * sinα;
            cos2σm = cos2α != 0 ? cosσ - 2 * Math.sin(U1) * Math.sin(U2) / cos2α : 0;
            double C = f / 16 * cos2α * (4 + f * (4 - 3 * cos2α));
            λP = λ;
            λ = L + (1 - C) * f * sinα *
                (σ + C * sinσ * (cos2σm + C * cosσ * (-1 + 2 * cos2σm * cos2σm)));
        } while (Math.abs(λ - λP) > 1e-12 && --iterLimit > 0);

        double u2 = cos2α * (a * a - b * b) / (b * b);
        double A = 1 + u2 / 16384 * (4096 + u2 * (-768 + u2 * (320 - 175 * u2)));
        double B = u2 / 1024 * (256 + u2 * (-128 + u2 * (74 - 47 * u2)));
        double Δσ = B * sinσ * (cos2σm + B / 4 * (cosσ * (-1 + 2 * cos2σm * cos2σm)
                      - B / 6 * cos2σm * (-3 + 4 * sinσ * sinσ) * (-3 + 4 * cos2σm * cos2σm)));
        double s = b * A * (σ - Δσ); // meters
        return s / 1000.0; // km
    }
}