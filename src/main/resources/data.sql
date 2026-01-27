INSERT INTO mpa_rating (id, name)
SELECT * FROM (
                  SELECT 1, '0+' UNION ALL
                  SELECT 2, '6+' UNION ALL
                  SELECT 3, '12+' UNION ALL
                  SELECT 4, '16+' UNION ALL
                  SELECT 5, '18+'
              ) AS data(new_id, new_name)
WHERE NOT EXISTS (
    SELECT 1 FROM mpa_rating WHERE mpa_rating.id = data.new_id
);