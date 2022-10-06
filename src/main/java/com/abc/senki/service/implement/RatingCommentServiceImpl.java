package com.abc.senki.service.implement;

import com.abc.senki.model.entity.RatingCommentEntity;
import com.abc.senki.repositories.RatingCommentRepository;
import com.abc.senki.service.RatingCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RatingCommentServiceImpl implements RatingCommentService {
    @Autowired
    private RatingCommentRepository ratingCommentRepository;
    @Override
    public void saveComment(RatingCommentEntity ratingComment) {
        ratingCommentRepository.save(ratingComment);
    }
}
