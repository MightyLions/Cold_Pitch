package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.Post;
import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {
    public Optional<Post> findByIdForUser(Long postId);
    public Optional<Post> findByIdForAdmin(Long postId);

    public List<Post> findAllForUser(Long userId);

    public List<Post> findAllForAdmin();
}
