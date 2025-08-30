package veniamin.shop.backend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import veniamin.shop.backend.dto.response.UserRespDTO;
import veniamin.shop.backend.entity.User;

public interface UserService extends UserDetailsService {

    UserRespDTO getCurrentUserInfo();

    UserRespDTO getUserById(Long id);

    UserRespDTO getUserByEmail(String email);

    UserRespDTO getResponseDTO(User user);

    void logout();}
