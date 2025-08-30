package veniamin.shop.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import veniamin.shop.backend.entity.User;

public interface MailService {

    void sendUserVerificationMail(User user, HttpServletRequest request);

    void sendPasswordRestoreMail(User user, HttpServletRequest request);
}
