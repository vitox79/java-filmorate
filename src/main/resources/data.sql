INSERT INTO Genres (name)
SELECT 'Комедия' UNION
SELECT 'Драма' UNION
SELECT 'Мультфильм' UNION
SELECT 'Триллер' UNION
SELECT 'Документальный' UNION
SELECT 'Боевик'
WHERE NOT EXISTS (SELECT 1 FROM Genres WHERE name IN ('Комедия', 'Драма', 'Мультфильм', 'Триллер', 'Документальный', 'Боевик'));

INSERT INTO MPA (name)
SELECT 'G' UNION
SELECT 'PG' UNION
SELECT 'PG-13' UNION
SELECT 'R' UNION
SELECT 'NC-17'
WHERE NOT EXISTS (SELECT 1 FROM MPA WHERE name IN ('G', 'PG', 'PG-13', 'R', 'NC-17'));