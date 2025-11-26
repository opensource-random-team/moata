package com.mysite.sbb.recommendation;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class RecommendationId implements Serializable // 복합키 사용
{

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "post_id")
    private Integer postId;
}
