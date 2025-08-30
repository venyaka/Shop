package veniamin.shop.backend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import veniamin.shop.backend.constant.PathConstants;
import veniamin.shop.backend.entity.User;
import veniamin.shop.backend.exception.BadRequestException;
import veniamin.shop.backend.exception.errors.BadRequestError;
import veniamin.shop.backend.filter.jwt.JwtUtils;
import veniamin.shop.backend.repository.UserRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader("Refresh") != null) {
            String refreshToken = request.getHeader("Refresh");
            refreshToken = refreshToken.replace("Bearer", "");
            if (!jwtUtils.validateRefreshToken(refreshToken)) {
                throw new BadRequestException(BadRequestError.NOT_CORRECT_REFRESH_TOKEN);
            }

            String email = jwtUtils.getUserEmailFromToken(refreshToken);
            User user = userRepository.findByEmail(email).get();
            user.setRefreshToken(jwtUtils.generateRandomSequence());
            refreshToken = jwtUtils.generateRefreshToken(user);

            String accessToken = jwtUtils.generateToken(user);
            response.addHeader("Authorization", "Bearer " + accessToken);
            response.addHeader("Refresh", "Bearer " + refreshToken);

            userRepository.saveAndFlush(user);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getRequestURI().equals(PathConstants.AUTHORIZE_CONTROLLER_PATH + "/refreshToken");
    }

}
