package com.buysellgo.userservice.controller.info.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "role"
)
@JsonSubTypes({
  @JsonSubTypes.Type(value = UserUpdateReq.class, name = "USER"),
  @JsonSubTypes.Type(value = SellerUpdateReq.class, name = "SELLER"),
  @JsonSubTypes.Type(value = AdminUpdateReq.class, name = "ADMIN")
})
public abstract class InfoUpdateReq {
    // 공통 필드 및 메서드가 있다면 여기에 정의
}
