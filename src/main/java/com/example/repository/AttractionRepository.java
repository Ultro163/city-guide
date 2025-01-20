package com.example.repository;

import com.example.model.Attraction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttractionRepository extends JpaRepository<Attraction, Long> {

    @EntityGraph(attributePaths = {"category", "city", "location"})
    @Query("""
                SELECT a
                FROM Attraction a
                JOIN FETCH a.category c
                JOIN FETCH a.location l
                JOIN FETCH a.city ci
                LEFT JOIN FETCH a.attractionReviews r
                WHERE calculate_distance(:userLat, :userLon, l.lat, l.lon) <= :radius
                AND (:categoryId IS NULL OR c.id = :categoryId)
                AND (:minRating IS NULL OR :minRating <= COALESCE((
                    SELECT AVG(r.rating)
                    FROM AttractionReview r
                    WHERE r.attraction = a
                ), 0))
                ORDER BY
                    CASE WHEN :sortBy = 'rating' AND :sortDirection = 'asc'
                         THEN (SELECT AVG(r.rating) FROM AttractionReview r WHERE r.attraction = a) END ASC NULLS FIRST,
                    CASE WHEN :sortBy = 'rating' AND :sortDirection = 'desc'
                         THEN (SELECT AVG(r.rating) FROM AttractionReview r WHERE r.attraction = a) END DESC NULLS LAST,
                    CASE WHEN :sortBy = 'distance' AND :sortDirection = 'asc' THEN
                        calculate_distance(:userLat, :userLon, l.lat, l.lon) END ASC,
                    CASE WHEN :sortBy = 'distance' AND :sortDirection = 'desc' THEN
                        calculate_distance(:userLat, :userLon, l.lat, l.lon) END DESC
            """)
    List<Attraction> getNearestAttractions(
            @Param("userLat") double userLat,
            @Param("userLon") double userLon,
            @Param("radius") double radius,
            @Param("categoryId") Long categoryId,
            @Param("minRating") Double minRating,
            @Param("sortBy") String sortBy,
            @Param("sortDirection") String sortDirection,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"category", "city", "location"})
    @Query("""
                SELECT a
                FROM Attraction a
                JOIN FETCH a.category c
                JOIN FETCH a.location l
                JOIN FETCH a.city ci
                LEFT JOIN FETCH a.attractionReviews r
                WHERE (a.city.id = :cityId)
                AND (:categoryId IS NULL OR c.id = :categoryId)
                AND (:minRating IS NULL OR :minRating <= COALESCE((
                    SELECT AVG(r.rating)
                    FROM AttractionReview r
                    WHERE r.attraction = a
                ), 0))
                ORDER BY
                    CASE WHEN :sortBy = 'rating' AND :sortDirection = 'asc'
                         THEN (SELECT AVG(r.rating) FROM AttractionReview r WHERE r.attraction = a) END ASC NULLS FIRST,
                    CASE WHEN :sortBy = 'rating' AND :sortDirection = 'desc'
                         THEN (SELECT AVG(r.rating) FROM AttractionReview r WHERE r.attraction = a) END DESC NULLS LAST,
                    CASE WHEN :sortBy = 'distance' AND :sortDirection = 'asc' THEN
                        calculate_distance(:userLat, :userLon, l.lat, l.lon) END ASC,
                    CASE WHEN :sortBy = 'distance' AND :sortDirection = 'desc' THEN
                        calculate_distance(:userLat, :userLon, l.lat, l.lon) END DESC
            """)
    List<Attraction> getAttractionsInCity(
            @Param("cityId") long cityId,
            @Param("userLat") double userLat,
            @Param("userLon") double userLon,
            @Param("categoryId") Long categoryId,
            @Param("minRating") Double minRating,
            @Param("sortBy") String sortBy,
            @Param("sortDirection") String sortDirection,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"category", "city", "location", "attractionReviews"})
    Optional<Attraction> getAttractionById(Long id);
}