package com.sku.fitizen.controller;

import com.sku.fitizen.domain.Place;
import com.sku.fitizen.domain.Review;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.PlaceServiceImpl;
import com.sku.fitizen.service.ReviewService;
import com.sku.fitizen.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/kakao")
@SessionAttributes("user")  // 'user' 객체를 세션에 저장하도록 설정
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private PlaceServiceImpl placeService;

    @Autowired
    private UserService userService;

//    @ModelAttribute("user")
//    public User getCurrentUser(HttpSession session) {
//        String userId = (String) session.getAttribute("id");
//        if (userId != null) {
//            return userService.selectUser(userId);
//        }
//        return null;
//    }

    @GetMapping("/map")
    public String showMapPage(@RequestParam(value = "exercise",required = false) String exercise, Model model) {
        String keyword="";
        if (exercise.equals("swimming")) {
            keyword = "수영장";
        }else if (exercise.equals("cardio")) {
            keyword="공원";
        }
        else if (exercise.equals("basketball")) {
            keyword="농구";
        }
        else if (exercise.equals("health")) {
            keyword="헬스";
        }
        else if (exercise.equals("yoga")) {
            keyword="요가";
        }
        else if (exercise.equals("pilates")) {
            keyword="필라테스";
        }
        else if (exercise.equals("tabletennis")) {
            keyword="탁구";
        }else if (exercise.equals("badminton")) {
            keyword="배드민턴";
        }
        model.addAttribute("keyword", keyword);
        return "map";
    }

    @PostMapping("/reviewDetail")
    public String reviewDetail(@RequestParam Long placeId,
                               @RequestParam String place_name,
                               @RequestParam(required = false) String road_address_name,
                               @RequestParam String address_name,
                               @RequestParam(required = false) String phone,
                               @SessionAttribute(value = "user", required = false) User currentUser, // 세션에서 사용자 객체를 가져옴
                               Model model) {

        if (currentUser == null) {
            return "redirect:/login/login";  // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }

        model.addAttribute("placeId", placeId);
        model.addAttribute("place_name", place_name);
        model.addAttribute("road_address_name", road_address_name);
        model.addAttribute("address_name", address_name);
        model.addAttribute("phone", phone);
        model.addAttribute("currentUser", currentUser);

        Place place = placeService.findOrCreatePlace(placeId, place_name, road_address_name, address_name, phone);
        if (place == null) {
            return "error"; // 장소를 찾거나 생성할 수 없는 경우 에러 페이지로 리다이렉트
        }

        List<Review> reviews = reviewService.getReviewsByPlaceId(place.getId());
        model.addAttribute("place", place);
        model.addAttribute("reviews", reviews);
        return "th/reviewDetail";
    }

    @GetMapping("/reviewDetail/{placeId}")
    public String reviewDetail(@PathVariable Long placeId,
                               @SessionAttribute(value = "user", required = false)
                               User currentUser,  // 세션에서 사용자 객체를 가져옴
                               Model model) {

        if (currentUser == null) {
            return "redirect:/login/login";  // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }

        Place place = placeService.findPlaceById(placeId);
        if (place == null) {
            return "error"; // 장소를 찾을 수 없는 경우 에러 페이지로 리다이렉트
        } else {
            model.addAttribute("placeId", place.getId());
            model.addAttribute("place_name", place.getName());
            model.addAttribute("road_address_name", place.getRoadaddress());
            model.addAttribute("address_name", place.getAddress());
            model.addAttribute("phone", place.getPhone());
        }

        List<Review> reviews = reviewService.getReviewsByPlaceId(placeId);
        model.addAttribute("place", place);
        model.addAttribute("reviews", reviews);
        model.addAttribute("currentUser", currentUser);
        return "th/reviewDetail";
    }

    @PostMapping("/addReview")
    @ResponseBody
    public Map<String, Object> addReview(@RequestParam Long placeId,
                                         @RequestParam String content,
                                         @RequestParam int rating,
                                         @SessionAttribute(value = "user", required = false) User currentUser) {  // 세션에서 사용자 객체를 가져옴

        Map<String, Object> response = new HashMap<>();
        if (currentUser == null) {
            response.put("status", "redirect");
            response.put("url", "/login/login");
            return response;
        }

        Place place = placeService.findPlaceById(placeId);
        if (place == null) {
            response.put("status", "error");
            response.put("message", "Invalid Place ID");
            return response;
        }

        Review review = new Review();
        review.setPlaceId(placeId);
        review.setUserId(currentUser.getId());
        review.setContent(content);
        review.setRating(rating);
        reviewService.saveReview(review);

        response.put("status", "success");
        response.put("url", "/kakao/reviewDetail/" + placeId);
        return response;
    }

    @GetMapping("/editReview/{reviewId}")
    public String showEditReviewForm(@PathVariable Long reviewId,
                                     @SessionAttribute(value = "user", required = false) User currentUser,  // 세션에서 사용자 객체를 가져옴
                                     Model model) {

        if (currentUser == null) {
            return "redirect:/login/login";  // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }

        Review review = reviewService.getReviewById(reviewId);
        if (review == null || !review.getUserId().equals(currentUser.getId())) {
            return "redirect:/error?message=Invalid Review ID or Unauthorized Access";
        }

        Place place = placeService.findPlaceById(review.getPlaceId());
        if (place == null) {
            return "error";
        }

        model.addAttribute("place", place);
        model.addAttribute("review", review);
        return "th/editReview";
    }

    @PostMapping("/editReview")
    @ResponseBody
    public Map<String, Object> editReview(@RequestParam Long placeId,
                                          @RequestParam Long reviewId,
                                          @RequestParam String content,
                                          @RequestParam int rating,
                                          @SessionAttribute(value = "user", required = false) User currentUser) {  // 세션에서 사용자 객체를 가져옴

        Map<String, Object> response = new HashMap<>();

        if (currentUser == null) {
            response.put("status", "redirect");
            response.put("url", "/login/login");
            return response;
        }

        Review review = reviewService.getReviewById(reviewId);
        if (review == null || !review.getUserId().equals(currentUser.getId())) {
            response.put("status", "error");
            response.put("message", "Invalid Review ID or Unauthorized Access");
            return response;
        }

        review.setContent(content);
        review.setRating(rating);
        reviewService.updateReview(review);

        response.put("status", "success");
        response.put("url", "/kakao/reviewDetail/" + placeId);
        return response;
    }

    @PostMapping("/deleteReview")
    @ResponseBody
    public Map<String, Object> deleteReview(@RequestParam Long reviewId,
                                            @SessionAttribute(value = "user", required = false) User currentUser) {  // 세션에서 사용자 객체를 가져옴

        Map<String, Object> response = new HashMap<>();

        if (currentUser == null) {
            response.put("status", "redirect");
            response.put("url", "/login/login");
            return response;
        }

        Review review = reviewService.getReviewById(reviewId);
        if (review == null || !review.getUserId().equals(currentUser.getId())) {
            response.put("status", "error");
            response.put("message", "Invalid Review ID or Unauthorized Access");
            return response;
        }

        reviewService.deleteReview(reviewId);
        response.put("status", "success");
        return response;
    }

}
