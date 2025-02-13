package com.buysellgo.helpdeskservice.common.auth;

import com.buysellgo.helpdeskservice.common.entity.Role;
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
