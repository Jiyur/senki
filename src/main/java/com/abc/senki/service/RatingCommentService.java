package com.abc.senki.service;

import com.abc.senki.model.entity.RatingCommentEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public interface RatingCommentService {
    void saveComment(RatingCommentEntity ratingComment);
}
