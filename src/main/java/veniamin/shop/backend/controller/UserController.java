package veniamin.shop.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import veniamin.shop.backend.constant.PathConstants;
import veniamin.shop.backend.dto.response.UserRespDTO;
import veniamin.shop.backend.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.USER_CONTROLLER_PATH)
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<UserRespDTO> me() {
    return ResponseEntity.ok(userService.getCurrentUserInfo());
  }

  @PostMapping("/logout")
  public void logout() {
    userService.logout();
  }
}
