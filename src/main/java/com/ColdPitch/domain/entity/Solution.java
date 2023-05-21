package com.ColdPitch.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Solution extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solution_id")
    private Long id;

    @Column(length = 10, nullable = false)
    private String positivePercentage;

    @Column(length = 10, nullable = false)
    private String negativePercentage;

    @Column(nullable = false)
    private String reaction;

    @Column(nullable = false)
    private String feedback;

    @Column(nullable = false)
    private Long userId;

    public void setPositivePercentage(String positivePercentage) {
        this.positivePercentage = positivePercentage;
    }

    public void setNegativePercentage(String negativePercentage) {
        this.negativePercentage = negativePercentage;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "Solution {" +
            "id=" + id +
            ", positivePercentage='" + positivePercentage + '\'' +
            ", negativePercentage='" + negativePercentage + '\'' +
            ", reaction='" + reaction + '\'' +
            ", feedback='" + feedback + '\'' +
            ", userId=" + userId + "'" +
            ", " + super.toString() +
            '}';
    }
}
