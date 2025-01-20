CREATE OR REPLACE FUNCTION calculate_distance(
    userLat DOUBLE PRECISION,
    userLon DOUBLE PRECISION,
    lat DOUBLE PRECISION,
    lon DOUBLE PRECISION
)
    RETURNS DOUBLE PRECISION AS
$$
BEGIN
    RETURN 6371 * acos(
            cos(radians(userLat)) * cos(radians(lat)) *
            cos(radians(lon) - radians(userLon)) +
            sin(radians(userLat)) * sin(radians(lat))
                  );
END;
$$ LANGUAGE plpgsql;