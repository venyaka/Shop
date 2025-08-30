package veniamin.shop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import veniamin.shop.backend.entity.User;
import veniamin.shop.backend.entity.UserSession;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    List<UserSession> findByUserAndEndTime(User user, LocalDateTime localDateTime);

    UserSession findFirstByUserOrderByStartTimeDesc(User user);

}
