package com.buysellgo.userservice.domain.user;

import java.time.LocalDate;

import com.buysellgo.userservice.common.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.*;            

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder        
@Table(name = "tbl_profile")
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id", columnDefinition = "bigint")
    private Long profileId;

    @Column(name = "profile_image", columnDefinition = "varchar(100)", nullable = false, unique = false)
    @Builder.Default
    private String profileImage = "https://via.placeholder.com/150";

    @Column(name = "ages", columnDefinition = "varchar(10)", nullable = false, unique = false)
    @Builder.Default
    private String ages = "0";

    @Column(name = "gender", columnDefinition = "enum('MALE','FEMALE','OTHER')", nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Gender gender = Gender.OTHER;

    @Column(name = "grade", columnDefinition = "enum('NORMAL','VIP')", nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Grade grade = Grade.NORMAL;

    @Column(name = "birthday", columnDefinition = "date", nullable = false, unique = false)
    @Builder.Default
    private LocalDate birthday = LocalDate.of(1900, 1, 1);

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public static Profile of(User user) {
        return Profile.builder()
                .user(user)
                .build();
    }

    public Vo toVo(){
        return new Vo(profileId, profileImage, ages, gender, grade, birthday, version);
    }


    public record Vo(
        Long profileId, 
        String profileImage, 
        String ages, 
        Gender gender, 
        Grade grade, 
        LocalDate birthday,
        long version) {}
}

