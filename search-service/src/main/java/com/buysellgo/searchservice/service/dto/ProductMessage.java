package com.buysellgo.searchservice.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductMessage(
        @JsonProperty("productId") long productId,
        @JsonProperty("productName") String productName,
        @JsonProperty("price") int price,
        @JsonProperty("companyName") String companyName,
        @JsonProperty("sellerId") long sellerId,
        @JsonProperty("productImage") String productImage,
        @JsonProperty("description") String description,
        @JsonProperty("productStock") int productStock,
        @JsonProperty("discountRate") int discountRate,
        @JsonProperty("deliveryFee") int deliveryFee,
        @JsonProperty("mainCategory") String mainCategory,
        @JsonProperty("subCategory") String subCategory,
        @JsonProperty("season") String season,
        @JsonProperty("version") long version,
        @JsonProperty("createdAt") long createdAt,
        @JsonProperty("updatedAt") long updatedAt
) {}

