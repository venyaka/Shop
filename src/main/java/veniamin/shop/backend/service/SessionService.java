package veniamin.shop.backend.service;

import veniamin.shop.backend.entity.UserSession;

public interface SessionService {

    UserSession saveNewSession(Long userId);

}
