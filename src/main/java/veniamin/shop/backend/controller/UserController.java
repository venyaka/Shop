package veniamin.shop.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import veniamin.shop.backend.constant.PathConstants;
import veniamin.shop.backend.dto.request.UpdateCurrentUserReqDTO;
import veniamin.shop.backend.dto.response.UserRespDTO;
import veniamin.shop.backend.service.UserService;

import java.io.IOException;

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
    public void logout(HttpServletResponse response) throws IOException {
        userService.logout();
        // Стереть cookies на клиенте
        ResponseCookie access = ResponseCookie.from("accessToken", "").httpOnly(true).path("/").maxAge(0).sameSite("Lax").build();
        ResponseCookie refresh = ResponseCookie.from("refreshToken", "").httpOnly(true).path("/").maxAge(0).sameSite("Lax").build();
        response.addHeader("Set-Cookie", access.toString());
        response.addHeader("Set-Cookie", refresh.toString());
        response.sendRedirect("/login");
    }
}
