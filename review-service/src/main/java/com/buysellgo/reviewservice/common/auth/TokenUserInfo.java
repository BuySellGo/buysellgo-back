package com.buysellgo.reviewservice.common.auth;

import com.buysellgo.reviewservice.common.entity.Role;
import lombok.*;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenUserInfo {

    private String email;
    private Role role;
    private long id;

}
