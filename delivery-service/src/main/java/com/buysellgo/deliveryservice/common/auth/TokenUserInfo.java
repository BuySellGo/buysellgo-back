package com.buysellgo.deliveryservice.common.auth;

import com.buysellgo.deliveryservice.common.entity.Role;
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
