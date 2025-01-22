package com.buysellgo.chatservice.common.auth;

import com.buysellgo.chatservice.common.entity.Role;
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
