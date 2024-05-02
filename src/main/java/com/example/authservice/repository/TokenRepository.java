package com.example.authservice.repository;

import com.example.authservice.model.Token;
import com.example.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    List<Token> findByUser(User user);

//    @Query("""
//            select t from Token inner join User u on t.user.id = u.id
//            where t.user.id = :userId and t.loggedOut = false
//            """)
//    List<Token> findAllTokensByUser(Long userId);
}
