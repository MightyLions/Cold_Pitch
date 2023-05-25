package com.ColdPitch.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "user_tag_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User user;

    @ManyToOne(fetch = LAZY)
    private Tag tag;


    //연관관계 편의 메소드
    public void setUser(User user) {
        this.user = user;
        if (!user.getUserTags().contains(this)) {
            user.getUserTags().add(this);
        }
    }

    public void setTag(Tag tag) {
        this.tag = tag;
        if (!tag.getUserTags().contains(this)) {
            tag.getUserTags().add(this);
        }
    }

    public UserTag(User user, Tag tag) {
        setUser(user);
        setTag(tag);
    }

    public void delete() {
        this.user.getUserTags().remove(this);
        this.tag.getUserTags().remove(this);
    }
}
