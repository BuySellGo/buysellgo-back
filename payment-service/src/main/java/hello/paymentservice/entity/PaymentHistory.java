package hello.paymentservice.entity;

import hello.productservice.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_order")
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_history_id")
    private Long paymentHistoryId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Product orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column
    private int price;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_method")
    private PayMethod payMethod;

    @Column(length = 20)
    private String status;

    @Column(name = "paid_at")
    private Timestamp dateTime;

    @Column(name = "is_review")
    private Boolean isReview;

}
