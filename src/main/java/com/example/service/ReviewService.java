package com.example.service;

import com.example.model.AttractionReview;

import java.util.List;

public interface ReviewService extends CrudService<AttractionReview, Long> {

    List<AttractionReview> getReviewForAttraction(long attId, String sortDirection);
}
