package com.buysellgo.userservice.domain.user;

import com.buysellgo.userservice.common.entity.Authorization;
import com.buysellgo.userservice.common.entity.BaseEntity;
import com.buysellgo.userservice.common.entity.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "bigint")
    private long userId;

    @Column(name = "email", columnDefinition = "varchar(50)",nullable = false, unique = true)
    private String email;

    @Column(name = "password", columnDefinition = "varchar(200)",nullable = false)
    private String password;

    @Column(name = "username", columnDefinition = "varchar(50)",nullable = false, unique = true)
    private String username;

    @Column(name = "phone", columnDefinition = "varchar(30)", nullable = false)
    private String phone;

    @Column(name = "login_type", columnDefinition = "enum('COMMON','KAKAO','NAVER','GOOGLE')", nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(name = "status", columnDefinition = "enum('AUTHORIZED','UNAUTHORIZED')", nullable = false)
    @Enumerated(EnumType.STRING)
    private Authorization status;

    @Column(name = "role", columnDefinition = "enum('USER')", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "email_certified", columnDefinition = "boolean", nullable = false)
    private boolean emailCertified;

    @Column(name = "agree_PICU", columnDefinition = "boolean", nullable = false)
    private boolean agreePICU;

    @Column(name = "agree_email", columnDefinition = "boolean", nullable = false)
    private boolean agreeEmail;

    @Column(name = "agree_TOS", columnDefinition = "boolean", nullable = false)
    private boolean agreeTOS;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Profile profile;

    public static User of(String email, String encodePassword, String username, String phone, LoginType loginType, Role role, Boolean emailCertified, Boolean agreePICU, Boolean agreeEmail, Boolean agreeTOS) {
        return User.builder()
                .email(email)
                .password(encodePassword)
                .username(username)
                .phone(phone)
                .loginType(loginType)
                .status(Authorization.AUTHORIZED)
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
            boolean emailCertified,
            boolean agreePICU,
            boolean agreeEmail,
            boolean agreeTOS,
            long version

    ){}
}
