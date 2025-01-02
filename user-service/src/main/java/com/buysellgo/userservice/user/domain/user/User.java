package com.buysellgo.userservice.user.domain.user;

import com.buysellgo.userservice.common.entity.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", columnDefinition = "varchar(50)",nullable = false)
    private String email;

    @Column(name = "password", columnDefinition = "varchar(200)",nullable = false)
    private String password;

    @Column(name = "username", columnDefinition = "varchar(50)",nullable = false)
    private String username;

    @Column(name = "phone", columnDefinition = "varchar(30)", nullable = false)
    private String phone;

    @Column(name = "login_type", columnDefinition = "enum('COMMON','KAKAO','NAVER','GOOGLE')", nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(name = "status", columnDefinition = "enum('ENABLE','DISABLE')", nullable = false)
    @Enumerated(EnumType.STRING)
    private AcountStatus status;

    @Column(name = "role", columnDefinition = "enum('ADMIN','USER')", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "email_certified", columnDefinition = "boolean", nullable = false)
    private Boolean emailCertified;

    @Column(name = "created_at", columnDefinition = "timestamp", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp", nullable = false)
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "agree_PICU", columnDefinition = "boolean", nullable = false)
    private Boolean agreePICU;

    @Column(name = "agree_email", columnDefinition = "boolean", nullable = false)
    private Boolean agreeEmail;

    @Column(name = "agree_TOS", columnDefinition = "boolean", nullable = false)
    private Boolean agreeTOS;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public static User of(String email, String encodePassword, String username, String phone, LoginType loginType, Role role, Boolean emailCertified, Boolean agreePICU, Boolean agreeEmail, Boolean agreeTOS) {
        return User.builder()
                .email(email)
                .password(encodePassword)
                .username(username)
                .phone(phone)
                .loginType(loginType)
                .status(AcountStatus.ENABLE)
                .role(role)
                .emailCertified(emailCertified)
                .agreePICU(agreePICU)
                .agreeEmail(agreeEmail)
                .agreeTOS(agreeTOS)
                .build();
    }

    public Vo toVo(){
        return new Vo(userId, email, username, phone,
                loginType.toString(), status.toString(), role.toString(),
                emailCertified, agreePICU, agreeEmail, agreeTOS, version);
    }
    public record Vo(
            long userId,
            String email,
            String username,
            String phone,
            String loginType,
            String status,
            String role,
            Boolean emailCertified,
            Boolean agreePICU,
            Boolean agreeEmail,
            Boolean agreeTOS,
            long version

    ){}
}
