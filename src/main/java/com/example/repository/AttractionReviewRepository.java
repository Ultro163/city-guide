package com.example.repository;

import com.example.model.AttractionReview;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AttractionReviewRepository extends JpaRepository<AttractionReview, Long> {
    Optional<AttractionReview> findByAttractionIdAndAuthorId(long arrID, long userId);

    @EntityGraph(attributePaths = {"author"})
    @Query("""
            SELECT ar
            FROM AttractionReview ar
            WHERE ar.attraction.id = :attId
            ORDER BY
                CASE WHEN :sortDirection = 'asc' THEN ar.rating END ASC NULLS FIRST,
                CASE WHEN :sortDirection = 'desc' THEN ar.rating END DESC NULLS LAST
            """)
    List<AttractionReview> findByAttractionId(long attId, String sortDirection);
}