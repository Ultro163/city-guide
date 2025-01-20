package com.example.service;

import com.example.model.Attraction;

import java.util.List;

public interface AttractionService extends CrudService<Attraction, Long> {

    List<Attraction> getNearestAttractions(
            double userLat,
            double userLon,
            double radius,
            Long categoryId,
            Double minRating,
            int limit,
            String sortBy,
            String sortDirection);

    List<Attraction> getAttractionsInCity(
            long cityId,
            double userLat,
            double userLon,
            Long categoryId,
            Double minRating,
            int limit,
            String sortBy,
            String sortDirection);
}