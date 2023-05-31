package com.ColdPitch.domain.repository;

import com.ColdPitch.domain.entity.File;
import com.ColdPitch.domain.entity.FileStatus;
import com.ColdPitch.domain.entity.dto.file.FileUploadResponse;
import com.ColdPitch.domain.entity.dto.file.QFileUploadResponse;
import com.ColdPitch.domain.repository.support.Querydsl4RepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.ColdPitch.domain.entity.QFile.*;

@Repository
public class FileQueryRepository extends Querydsl4RepositorySupport{
    public FileQueryRepository() {
        super(File.class);
    }

    public List<FileUploadResponse> findAll() {
        return select(new QFileUploadResponse(file.name
                , file.path
                , file.size
                , file.status))
                .from(file)
                .where(noneDelete())
                .fetch();
    }

    public List<FileUploadResponse> findByPath(String path) {
        return select(new QFileUploadResponse(file.name
                , file.path
                , file.size
                , file.status))
                .from(file)
                .where(pathEq(path))
                .fetch();
    }

    private static BooleanExpression pathEq(String path) {
        return StringUtils.hasText(path) ? file.path.eq(path) : null;
    }


    private static BooleanExpression noneDelete() {
        return file.status.ne(FileStatus.D);
    }
}
