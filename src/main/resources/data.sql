INSERT INTO mpa_rating (id, name)
SELECT * FROM (
                  SELECT 1, 'G' UNION ALL
                  SELECT 2, 'PG' UNION ALL
                  SELECT 3, 'PG-13' UNION ALL
                  SELECT 4, 'R' UNION ALL
                  SELECT 5, 'NC-17'
              ) AS data(new_id, new_name)
WHERE NOT EXISTS (
    SELECT 1 FROM mpa_rating WHERE mpa_rating.id = data.new_id
);

INSERT INTO genre (id, name)
SELECT * FROM (
                  SELECT 1, 'Комедия' UNION ALL
                  SELECT 2, 'Драма' UNION ALL
                  SELECT 3, 'Мультфильм' UNION ALL
                  SELECT 4, 'Триллер' UNION ALL
                  SELECT 5, 'Документальный' UNION ALL
                  SELECT 6, 'Боевик'
              ) AS data(new_id, new_name)
WHERE NOT EXISTS (
    SELECT 1 FROM genre WHERE genre.id = data.new_id
);