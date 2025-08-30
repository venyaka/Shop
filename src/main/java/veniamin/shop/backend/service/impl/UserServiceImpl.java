package veniamin.shop.backend.service.impl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import veniamin.shop.backend.dto.response.UserRespDTO;
import veniamin.shop.backend.entity.User;
import veniamin.shop.backend.exception.NotFoundException;
import veniamin.shop.backend.exception.errors.NotFoundError;
import veniamin.shop.backend.repository.UserRepository;
import veniamin.shop.backend.service.UserService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return userRepository
                .findById(Long.valueOf(id))
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
        userRespDTO.setUsername(user.getUsername());
        userRespDTO.setEmail(user.getEmail());
        userRespDTO.setFirstName(user.getFirstName());
        userRespDTO.setLastName(user.getLastName());
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
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        if (id == null || id.isEmpty()) {
            throw new NotFoundException(NotFoundError.USER_NOT_FOUND);
        }
        return userRepository.findById(Long.valueOf(id)).orElseThrow(() -> new NotFoundException(NotFoundError.USER_NOT_FOUND));
    }
//    private User getCurrentUser(){
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByUsername(username).get();
//        return user;
//    }
}
