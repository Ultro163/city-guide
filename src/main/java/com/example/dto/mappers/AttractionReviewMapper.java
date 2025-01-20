package com.example.dto.mappers;

import com.example.dto.review.AttractionReviewDto;
import com.example.dto.review.NewAttractionReviewDto;
import com.example.dto.review.ReviewDtoForAttraction;
import com.example.dto.review.UpdateAttractionReviewDto;
import com.example.model.AttractionReview;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class, AttractionMapper.class})
public interface AttractionReviewMapper {
    AttractionReview toEntity(AttractionReviewDto attractionReviewDto);

    @Mapping(source = "attractionId", target = "attraction.id")
    @Mapping(source = "authorId", target = "author.id")
    AttractionReview toEntity(NewAttractionReviewDto newAttractionReviewDto);

    AttractionReviewDto toAttractionReviewDto(AttractionReview attractionReview);

    @Mapping(source = "attractionId", target = "attraction.id")
    @Mapping(source = "authorId", target = "author.id")
    AttractionReview toEntity(UpdateAttractionReviewDto updateAttractionReviewDto);

    AttractionReview toEntity(ReviewDtoForAttraction reviewDtoForAttraction);

    @InheritInverseConfiguration(name = "toEntity")
    ReviewDtoForAttraction toReviewDtoForAttraction(AttractionReview attractionReview);
}