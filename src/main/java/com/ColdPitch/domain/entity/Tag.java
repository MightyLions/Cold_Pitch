package com.ColdPitch.domain.entity;

import com.ColdPitch.domain.entity.tag.TagName;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;
import java.util.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Enumerated(EnumType.STRING)
    private TagName name;

    @Column(nullable = false)
    private Date registrationDate;

    @Column(nullable = false)
    private Date lastUpdatedDate;

    @Column()
    private String iconPath;

    @Column(nullable = false)
    private String description;

    @Override
    public String toString() {
        return super.toString() +
                ", tagId=" + tagId +
                ", name='" + name + '\'' +
                ", registrationDate=" + registrationDate +
                ", lastUpdatedDate=" + lastUpdatedDate +
                ", iconPath='" + iconPath + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
