package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Post;
import java.util.List;

public interface PostRepositoryCustom {

    public List<Post> findAllForUser(Long userId);

    public List<Post> findAllForAdmin();
}
