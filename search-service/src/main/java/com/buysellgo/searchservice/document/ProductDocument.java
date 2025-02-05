package com.buysellgo.searchservice.document;

import com.buysellgo.searchservice.service.dto.ProductMessage;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document(indexName = "products")
public class ProductDocument {

    @Id
    private Long productId;

    @Field(type = FieldType.Text)
    private String productName;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Integer)
    private int price;

    @Field(type = FieldType.Text)
    private String companyName;

    @Field(type = FieldType.Long)
    private Long sellerId;

    @Field(type = FieldType.Text)
    private String productImage;

    @Field(type = FieldType.Integer)
    private int productStock;

    @Field(type = FieldType.Integer)
    private int discountRate;

    @Field(type = FieldType.Integer)
    private int deliveryFee;

    @Field(type = FieldType.Keyword)
    private String mainCategory;

    @Field(type = FieldType.Keyword)
    private String subCategory;

    @Field(type = FieldType.Keyword)
    private String season;

    @Field(type = FieldType.Long)
    private long version;

    @Field(type = FieldType.Long)
    private long createdAt;

    @Field(type = FieldType.Long)
    private long updatedAt;


    public static ProductDocument from(ProductMessage message) {
        return ProductDocument.builder()
                .productId(message.productId())
                .productName(message.productName())
                .price(message.price())
                .companyName(message.companyName())
                .sellerId(message.sellerId())
                .productImage(message.productImage())
                .description(message.description())
                .productStock(message.productStock())
                .discountRate(message.discountRate())
                .deliveryFee(message.deliveryFee())
                .mainCategory(message.mainCategory())
                .subCategory(message.subCategory())
                .season(message.season())
                .version(message.version())
                .createdAt(message.createdAt())
                .updatedAt(message.updatedAt())
                .build();
    }
}
