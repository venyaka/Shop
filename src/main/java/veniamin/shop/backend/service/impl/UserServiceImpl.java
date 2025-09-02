package veniamin.shop.backend.service.impl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import veniamin.shop.backend.dto.request.UpdateCurrentUserReqDTO;
import veniamin.shop.backend.dto.response.UserRespDTO;
import veniamin.shop.backend.entity.Role;
import veniamin.shop.backend.entity.User;
import veniamin.shop.backend.exception.AuthorizeException;
import veniamin.shop.backend.exception.NotFoundException;
import veniamin.shop.backend.exception.errors.AuthorizedError;
import veniamin.shop.backend.exception.errors.NotFoundError;
import veniamin.shop.backend.repository.UserRepository;
import veniamin.shop.backend.service.UserService;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NotFoundError.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public UserRespDTO getCurrentUserInfo() {
        User user = this.getCurrentUser();
        return getResponseDTO(user);
    }

    @Override
    @Transactional
    public UserRespDTO updateCurrentUser(UpdateCurrentUserReqDTO updateCurrentUserReqDTO) {
        User user = this.getCurrentUser();

        user = userRepository.save(user);

        if ( updateCurrentUserReqDTO.getFirstName() != null ) {
            user.setFirstName( updateCurrentUserReqDTO.getFirstName() );
        }
        if ( updateCurrentUserReqDTO.getLastName() != null ) {
            user.setLastName( updateCurrentUserReqDTO.getLastName() );
        }
        userRepository.save(user);

        return getResponseDTO(user);
    }


    @Override
    @Transactional
    public UserRespDTO getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(NotFoundError.USER_NOT_FOUND);
        }

        return getResponseDTO(optionalUser.get());
    }


    @Transactional
    @Override
    public UserRespDTO getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(NotFoundError.USER_NOT_FOUND);
        }
        return getResponseDTO(optionalUser.get());
    }

    @Override
    public UserRespDTO getResponseDTO(User user) {
        UserRespDTO userRespDTO = new UserRespDTO();
        userRespDTO.setId(user.getId());
        userRespDTO.setEmail(user.getEmail());
        userRespDTO.setFirstName(user.getFirstName());
        userRespDTO.setLastName(user.getLastName());
        Set<String> roleNames = user.getRoles().stream().map(Role::name).collect(Collectors.toSet());
        userRespDTO.setRoles(roleNames);
        return userRespDTO;
    }

    @Override
    @Transactional
    public void logout() {
        User user = this.getCurrentUser();
        user.setRefreshToken(null);
        userRepository.save(user);
        SecurityContextHolder.clearContext();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null || "anonymousUser".equals(authentication.getName())) {
            throw new AuthorizeException(AuthorizedError.NOT_CORRECT_TOKEN);
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(NotFoundError.USER_NOT_FOUND));
    }
}
