package com.sku.fitizen.controller.store;

import com.sku.fitizen.domain.store.CartItem;
import com.sku.fitizen.domain.store.Product;
import com.sku.fitizen.service.store.CartService;
import com.sku.fitizen.service.store.ShopService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private CartService cartService;
    private  ShopService shopService;
    Path projectRoot = Paths.get("").toAbsolutePath(); // 현재 프로젝트 루트 경로
    Path IMAGE_DIR = projectRoot.resolve("src/main/resources/static/ShopImage");
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("")
    public String list(Authentication authentication, Model model, HttpSession session) {
        if (authentication != null) {
            // 사용자 권한 리스트 가져오기
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    model.addAttribute("userRole", "ROLE_ADMIN");
                    break;
                }
            }
        }
        String userId = (String) session.getAttribute("userId");
        model.addAttribute("list",shopService.getAllProducts());
        return "shopList";
    }

    @GetMapping("/addProduct")
    public String add(Model model) {
        model.addAttribute("product",new Product());
        return "addProduct";
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addProduct(
            @RequestParam("prname") String prname,
            @RequestParam("prdesc") String prdesc,
            @RequestParam("prprice") int prprice,
            @RequestParam("primage") MultipartFile primage) {
        String formattedDesc = prdesc.replace("\n", "<br>");
        Map<String, String> response = new HashMap<>();

        try {
            // 파일이 비어있지 않으면 저장
            if (!primage.isEmpty()) {
                String originalFilename = primage.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String randomString = shopService.generateRandomString(5);
                String hashedFileName = originalFilename+ "_" +randomString +fileExtension;

                // resources/static/image 경로 설정
                File imageDir = new File(IMAGE_DIR.toString());
                if (!imageDir.exists()) {
                    imageDir.mkdirs();
                }

                Path filePath = Paths.get(imageDir.getAbsolutePath(), hashedFileName);  // 파일 저장 경로 설정
                primage.transferTo(filePath.toFile());  // 파일을 저장 경로에 저장

                // 데이터베이스에 파일명과 함께 다른 정보 저장
                Product product = new Product();
                product.setPrname(prname);
                product.setPrdesc(formattedDesc);
                product.setPrprice(prprice);
                product.setPrimage(hashedFileName);  // 파일명만 설정

                shopService.addProduct(product);
                response.put("message", "상품이 성공적으로 추가되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "이미지가 없습니다.");
                return ResponseEntity.status(400).body(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.put("message", "파일 저장 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "상품 추가 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

        @GetMapping("/shopDetail/{prid}")
        public String showProductDetail ( @PathVariable int prid, Authentication authentication, Model model, HttpSession session){
            if (authentication != null) {
                // 사용자 권한 리스트 가져오기
                for (GrantedAuthority authority : authentication.getAuthorities()) {
                    if (authority.getAuthority().equals("ROLE_ADMIN")) {
                        model.addAttribute("userRole", "ROLE_ADMIN");
                        break;
                    }
                }
            }
        Product product = shopService.getProductById(prid);
            String userId = (String) session.getAttribute("userId");
            model.addAttribute("product", product);
            model.addAttribute("userId", userId);
            return "shopDetail";
        }

    @DeleteMapping("/delete/{prid}")
    public ResponseEntity<String> deleteProduct(@PathVariable("prid") int prid) {
        try {
            shopService.deleteProduct(prid); // 서비스에서 상품 삭제
            return ResponseEntity.ok("상품이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 삭제 중 오류가 발생했습니다.");
        }
    }


    @PostMapping("/cart/add")
        @ResponseBody
        public ResponseEntity<Map<String, Object>> addToCart ( @RequestParam("prid") int productId,
        @RequestParam("userId") String userId, @RequestParam("qty") int qty, HttpSession session){
            CartItem existingCartItem = cartService.findCartItemByUserIdAndProductId(userId, productId);
            Map<String, Object> response = new HashMap<>();
            // 로그인 여부 확인
            if (userId == null) {
                response.put("status", "error");
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // JSON 응답으로 반환
            }
            System.out.println(existingCartItem);
        System.out.println("Product ID: " + productId + ", User ID: " + userId + ", Qty: " + qty);
            System.out.println(qty);
            if (existingCartItem != null) {
                // 장바구니에 해당 상품이 있으면 수량 증가
                existingCartItem.setQty(existingCartItem.getQty() + qty);
                cartService.updateCartItem(existingCartItem);
                response.put("status", "success");
                response.put("message", "장바구니에 상품 수량이 업데이트되었습니다.");
            } else {
                // 장바구니에 없으면 새로 추가
                Product product = cartService.getProductById(productId);  // 상품 정보 조회
                CartItem newCartItem = new CartItem();
                newCartItem.setProduct_id(productId);
                newCartItem.setUser_id(userId);
                newCartItem.setQty(qty);
                newCartItem.setProduct_price(product.getPrprice());  // 제품 가격 설정
                newCartItem.setPrice(product.getPrprice() * newCartItem.getQty());
                newCartItem.setProduct_name(product.getPrname());  // 제품명 설정
                cartService.insertCartItem(newCartItem);
                response.put("status", "success");
                response.put("message", "장바구니에 새 상품이 추가되었습니다.");

            }
            // 최종 응답 반환
            return ResponseEntity.ok(response);
        }



}
