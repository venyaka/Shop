package veniamin.shop.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import veniamin.shop.backend.constant.PathConstants;
import veniamin.shop.backend.dto.request.UpdateCurrentUserReqDTO;
import veniamin.shop.backend.dto.response.UserRespDTO;
import veniamin.shop.backend.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.USER_CONTROLLER_PATH)
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    @Operation(summary = "Получение информации о текущем авторизированном пользователе")
    public UserRespDTO getUserInfo() {
        return userService.getCurrentUserInfo();
    }

    @PatchMapping("/update")
    @Operation(summary = "Обновление текущего авторизированного пользователя")
    public UserRespDTO updateCurrentUser(
            @ModelAttribute UpdateCurrentUserReqDTO updateCurrentUserReqDTO) {
        return userService.updateCurrentUser(updateCurrentUserReqDTO);
    }

    @GetMapping("/me")
    @Operation(summary = "Получение профиля текущего пользователя")
    public ResponseEntity<UserRespDTO> me() {
        return ResponseEntity.ok(userService.getCurrentUserInfo());
    }

    @PostMapping("/logout")
    @Operation(summary = "Выход пользователя из системы")
    public void logout() {
        userService.logout();
    }
}
