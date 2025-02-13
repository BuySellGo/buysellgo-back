package com.buysellgo.userservice.domain.admin;

import com.buysellgo.userservice.common.entity.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id", columnDefinition = "bigint")
    private long adminId;

    @Column(name = "email", columnDefinition = "varchar(50)",nullable = false, unique = true)
    private String email;

    @Column(name = "password", columnDefinition = "varchar(200)",nullable = false)
    private String password;

    @Column(name = "role", columnDefinition = "enum('ADMIN')", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public static Admin of(String email, String encodePassword, Role role) {
        return Admin.builder()
                .email(email)
                .password(encodePassword)
                .role(role)
                .build();
    }

    public Vo toVo(){
        return new Vo(adminId, email, role.toString());
    }

    public record Vo(
            long adminId,
            String email,
            String role
    ){}
}
