package veniamin.shop.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @Operation(summary = "Эндпоинт для авторизации, принимает два request body с  - email и  password, " +
            "в которых хранится email и пароль соответственно, " +
            "возвращает response с header'ом Authorization в формате Bearer jwtTokenInStringFormat и refreshToken в том же формате")
    public ResponseEntity<TokenRespDTO> authorizeUser(@Valid @RequestBody UserAuthorizeReqDTO userAuthorizeDTO) {
        return authorizeService.authorizeUser(userAuthorizeDTO);
    }

    @PostMapping("/register")
    @Operation(summary = "Эндпоинт для регистрации нового пользователя, после регистрации для доступа к основным частям сайта нужно пройти верификацию")
    public void registerUser(@Valid @RequestBody RegisterReqDTO registerDTO, HttpServletRequest request) {
        authorizeService.registerUser(registerDTO, request);
    }

    @PostMapping("/verificateCode")
    @Operation(summary = "Повторная посылка верификационного кода на почту")
    public void sendVerificationCode(@RequestParam String email, HttpServletRequest request) {
        authorizeService.sendVerificationCode(email, request);
    }

    @PostMapping("/verification")
    @Operation(summary = "Верификации зарегистрированного пользователя по почте и токену")
    public void verificateUser(@RequestParam(required = true) String email,
                               @RequestParam(required = true) String verificationToken,
                               HttpServletRequest request) {
        authorizeService.verificateUser(email, verificationToken);
    }
}
