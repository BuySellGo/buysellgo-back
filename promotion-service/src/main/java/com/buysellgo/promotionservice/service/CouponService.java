package com.buysellgo.promotionservice.service;

import com.buysellgo.promotionservice.dto.CouponRequestDto;
import com.buysellgo.promotionservice.dto.IssuedCouponResponseDto;
import com.buysellgo.promotionservice.entity.Coupon;
import com.buysellgo.promotionservice.entity.QCoupon;
import com.buysellgo.promotionservice.entity.QCouponIssuance;
import com.buysellgo.promotionservice.repository.CouponIssuanceRepository;
import com.buysellgo.promotionservice.repository.CouponRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponIssuanceRepository couponIssuanceRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public Coupon createCoupon(CouponRequestDto couponRequestDto) {

        Coupon coupon = couponRepository.save(couponRequestDto.toEntity());
        log.info("Coupon Created: {} ", coupon);

        return coupon;
    }

    public Page<Coupon.Vo> getCouponList(Pageable pageable) {
        Page<Coupon> couponPage = couponRepository.findAll(pageable);

        if (couponPage.getTotalElements() <= 0) {
            log.warn("No COUPON found for the given page request: {}", pageable);
            return Page.empty();
        }

        return couponPage.map(coupon -> {
            try {
                return coupon.toVo();
            } catch (Exception e) {
                log.error("Error converting coupon to Vo: {}", coupon);
                throw new RuntimeException(e);
            }
        });
    }

    public void deleteCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new EntityNotFoundException("Coupon not found for couponId" + couponId)
        );

        couponRepository.deleteById(couponId);
    }

    public Page<IssuedCouponResponseDto> getIssuedCouponList(Long userId, Pageable pageable) {

        QCoupon coupon = QCoupon.coupon;
        QCouponIssuance couponIssuance = QCouponIssuance.couponIssuance;

//        System.out.println("===============================>");
//        System.out.println("userId = " + userId);

//        BooleanExpression userCondition = couponIssuance.userId.eq(userId);
//        List<IssuedCouponResponseDto> issuedCouponResponseDtoList = jpaQueryFactory
//                .select(couponIssuance.id,
//                        couponIssuance.userId,
//                        coupon.id,
//                        coupon.couponTitle,
//                        coupon.discountRate,
//                        couponIssuance.createdAt)
//                .from(couponIssuance)
//                .leftJoin(couponIssuance.coupon, coupon)
//                .where(userCondition)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch()
//                .stream()
//                .map(tuple -> new IssuedCouponResponseDto(
//                        tuple.get(couponIssuance.id),
//                        tuple.get(couponIssuance.userId),
//                        tuple.get(coupon.id),
//                        tuple.get(coupon.couponTitle),
//                        tuple.get(coupon.discountRate),
//                        tuple.get(couponIssuance.createdAt)
//                ))
////                .collect(Collectors.toList());

        // 페이징 처리: offset과 limit를 pageable에서 가져옴.
        JPAQuery<Tuple> query = jpaQueryFactory
                .select(couponIssuance.id,
                        couponIssuance.userId,
                        coupon.id,
                        coupon.couponTitle,
                        coupon.discountRate,
                        couponIssuance.createdAt)
                .from(couponIssuance)
                .leftJoin(couponIssuance.coupon, coupon)
                .where(couponIssuance.userId.eq(userId));

        // Pageable 처리
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());

        // 데이터 가져오기
        List<Tuple> tuples = query.fetch();
        if(tuples.isEmpty()){
            log.warn("No issued coupon found");
            return Page.empty();
        }

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(couponIssuance.count())
                .from(couponIssuance)
                .leftJoin(couponIssuance.coupon, coupon);

        // 전체 카운트를 가져와서 페이징 결과로 반환
        Long totalCount = countQuery.fetchOne();
        totalCount = totalCount == null ? 0 : totalCount;

        // 결과를 DTO 또는 VO로 변환
        List<IssuedCouponResponseDto> issuedCouponResponseDtoList
                = tuples.stream()
                .map(tuple -> new IssuedCouponResponseDto(
                        tuple.get(couponIssuance.id),
                        tuple.get(couponIssuance.userId),
                        tuple.get(coupon.id),
                        tuple.get(coupon.couponTitle),
                        tuple.get(coupon.discountRate),
                        tuple.get(couponIssuance.createdAt)
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(issuedCouponResponseDtoList, pageable, totalCount);

    }
}
