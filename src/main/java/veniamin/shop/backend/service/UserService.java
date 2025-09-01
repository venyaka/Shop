package veniamin.shop.backend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import veniamin.shop.backend.dto.request.UpdateCurrentUserReqDTO;
import veniamin.shop.backend.dto.response.UserRespDTO;
import veniamin.shop.backend.entity.User;

public interface UserService extends UserDetailsService {

    UserRespDTO getCurrentUserInfo();

    UserRespDTO updateCurrentUser(UpdateCurrentUserReqDTO updateCurrentUserReqDTO);

    UserRespDTO getUserById(Long id);

    UserRespDTO getUserByEmail(String email);

    UserRespDTO getResponseDTO(User user);

    void logout();}
