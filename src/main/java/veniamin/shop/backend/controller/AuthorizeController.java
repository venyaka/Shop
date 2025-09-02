package veniamin.shop.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import veniamin.shop.backend.constant.PathConstants;
import veniamin.shop.backend.dto.request.RegisterReqDTO;
import veniamin.shop.backend.dto.request.UserAuthorizeReqDTO;
import veniamin.shop.backend.dto.response.TokenRespDTO;
import veniamin.shop.backend.service.AuthorizeService;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.AUTHORIZE_CONTROLLER_PATH)
public class AuthorizeController {

    private final AuthorizeService authorizeService;


    @PostMapping("/login")
    @Operation(summary = "Эндпоинт для авторизации: принимает email и password и устанавливает HttpOnly cookies с access/refresh токенами")
    public ResponseEntity<TokenRespDTO> authorizeUser(@Valid @RequestBody UserAuthorizeReqDTO userAuthorizeDTO, HttpServletResponse response) {
        // Вызываем сервис для генерации токенов
        TokenRespDTO tokens = authorizeService.authorizeUser(userAuthorizeDTO).getBody();
        if (tokens != null) {
            String access = tokens.getAccessToken().replaceFirst("^Bearer\\s+", "");
            String refresh = tokens.getRefreshToken().replaceFirst("^Bearer\\s+", "");

            // HttpOnly cookies. SameSite=Lax; Secure опустим для локалки
            ResponseCookie accessCookie = ResponseCookie.from("accessToken", access)
                    .httpOnly(true)
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(15 * 60) // 15 мин
                    .build();
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refresh)
                    .httpOnly(true)
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(7 * 24 * 60 * 60) // 7 дней
                    .build();

            response.addHeader("Set-Cookie", accessCookie.toString());
            response.addHeader("Set-Cookie", refreshCookie.toString());
        }
        // Клиент (JS) выполнит редирект сам после 200 OK
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация нового пользователя. После регистрации требуется подтверждение email")
    public void registerUser(@Valid @RequestBody RegisterReqDTO registerDTO, HttpServletRequest request) {
        authorizeService.registerUser(registerDTO, request);
    }

    @PostMapping("/verificateCode")
    @Operation(summary = "Повторная отправка верификационного кода на почту")
    public void sendVerificationCode(@RequestParam String email, HttpServletRequest request) {
        authorizeService.sendVerificationCode(email, request);
    }

    @RequestMapping(value = "/verification", method = { RequestMethod.GET, RequestMethod.POST })
    @Operation(summary = "Верификация зарегистрированного пользователя по почте и токену")
    public void verificateUser(@RequestParam String email,
                               @RequestParam(name = "token") String token,
                               HttpServletRequest request, HttpServletResponse response) throws java.io.IOException {
        authorizeService.verificateUser(email, token);
        response.sendRedirect("/");
    }

    // Отдельный refresh эндпоинт по новой архитектуре не используется (рефреш делаем тихо в фильтре)
}
