-- Insertar roles
INSERT IGNORE INTO roles (name) VALUES
('ROLE_ADMIN'), ('ROLE_USER');

-- Insertar usuarios
INSERT IGNORE INTO users (username, password, email, name, last_name, age, height, weight, gender, activity_level, enabled) VALUES
('itrebar', '$2a$10$U21WY06zsEaQsujpRS7H4.CqkhkvkOKIhwkBkSD.0VR9x./fsUDfC', 'user1@example.com', 'Imanol', 'Trespaderne', 25, 175.5, 70.0, 'M', 'MODERADO', true),
('user2', '$2a$10$U21WY06zsEaQsujpRS7H4.CqkhkvkOKIhwkBkSD.0VR9x./fsUDfC', 'user2@example.com', 'Lucía', 'Gómez', 30, 160.2, 60.0, 'F', 'ACTIVO', true),
('user3', '$2a$10$U21WY06zsEaQsujpRS7H4.CqkhkvkOKIhwkBkSD.0VR9x./fsUDfC', 'user3@example.com', 'Juan', 'López', 22, 180.0, 80.5, 'M', 'LIGERO', true),
('user4', '$2a$10$U21WY06zsEaQsujpRS7H4.CqkhkvkOKIhwkBkSD.0VR9x./fsUDfC', 'user4@example.com', 'Elena', 'Martínez', 28, 165.4, 55.0, 'F', 'SEDENTARIO', true),
('user5', '$2a$10$U21WY06zsEaQsujpRS7H4.CqkhkvkOKIhwkBkSD.0VR9x./fsUDfC', 'user5@example.com', 'Andrés', 'Sánchez', 35, 170.0, 75.0, 'M', 'MUY_ACTIVO', true);

-- Insertar user_roles
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
(1, 1), (2, 2), (3, 2), (4, 2), (5, 1);

-- Insertar alimentos
INSERT IGNORE INTO food (name, calories, proteins, carbohydrates, fats) VALUES
('Pollo', 165, 31.0, 0.0, 3.6),
('Arroz', 130, 2.7, 28.0, 0.3),
('Manzana', 52, 0.3, 14.0, 0.2),
('Brócoli', 34, 2.8, 7.0, 0.4),
('Salmón', 208, 20.0, 0.0, 13.0),
('Huevo', 155, 13.0, 1.1, 11.0),
('Avena', 389, 16.9, 66.3, 6.9),
('Plátano', 89, 1.1, 23.0, 0.3),
('Ternera magra', 250, 26.0, 0.0, 15.0),
('Pechuga de pavo', 135, 29.0, 0.0, 1.0),
('Lentejas cocidas', 116, 9.0, 20.0, 0.4),
('Garbanzos cocidos', 164, 9.0, 27.0, 2.6),
('Almendras', 579, 21.0, 22.0, 49.0),
('Nueces', 654, 15.0, 14.0, 65.0),
('Yogur natural', 61, 3.5, 5.0, 3.3),
('Leche entera', 60, 3.2, 5.0, 3.3),
('Pan integral', 247, 13.0, 41.0, 4.2),
('Queso mozzarella', 280, 28.0, 3.1, 17.0),
('Tofu', 76, 8.0, 1.9, 4.8),
('Espinacas', 23, 2.9, 3.6, 0.4),
('Atún enlatado', 132, 28.0, 0.0, 1.0),
('Merluza', 85, 18.0, 0.0, 1.0),
('Aceite de oliva', 884, 0.0, 0.0, 100.0),
('Pasta cocida', 131, 5.0, 25.0, 1.1),
('Patata cocida', 87, 2.0, 20.0, 0.1),
('Zanahoria', 41, 0.9, 10.0, 0.2),
('Tomate', 18, 0.9, 3.9, 0.2),
('Pepino', 16, 0.7, 3.6, 0.1),
('Pimiento rojo', 31, 1.0, 6.0, 0.3),
('Calabacín', 17, 1.2, 3.1, 0.3),
('Berenjena', 25, 1.0, 6.0, 0.2),
('Champiñones', 22, 3.1, 3.3, 0.3),
('Chía', 486, 17.0, 42.0, 31.0),
('Semillas de lino', 534, 18.0, 29.0, 42.0),
('Aguacate', 160, 2.0, 9.0, 15.0),
('Mango', 60, 0.8, 15.0, 0.4),
('Piña', 50, 0.5, 13.0, 0.1),
('Melón', 34, 0.8, 8.0, 0.2),
('Sandía', 30, 0.6, 8.0, 0.2),
('Fresas', 32, 0.7, 7.7, 0.3),
('Arándanos', 57, 0.7, 14.0, 0.3),
('Ciruela', 46, 0.7, 11.0, 0.3),
('Kiwis', 41, 0.8, 10.0, 0.5),
('Naranja', 47, 0.9, 12.0, 0.1),
('Uvas', 69, 0.7, 18.0, 0.2),
('Pan blanco', 265, 9.0, 49.0, 3.2),
('Croissant', 406, 8.0, 45.0, 21.0),
('Cacao en polvo', 228, 19.6, 57.9, 13.7),
('Miel', 304, 0.3, 82.0, 0.0),
('Azúcar', 387, 0.0, 100.0, 0.0);


-- Insertar dietas
INSERT IGNORE INTO diets (name, description, duration_weeks, user_id) VALUES
('Dieta Fit', 'Alta en proteínas', 4, 1),
('Dieta Vegana', 'Solo vegetal', 6, 2),
('Dieta Keto', 'Baja en carbos', 8, 3),
('Dieta Mediterránea', 'Equilibrada', 5, 4),
('Dieta Detox', 'Depurativa', 3, 5);

-- Insertar todas las comidas de la semana para 'itrebar' (dieta_id = 1)
INSERT IGNORE INTO diet_food (diet_id, food_id, quantity, day_week, meal_type) VALUES
-- LUNES
(1, 1, 150.0, 'LUNES', 'DESAYUNO'),
(1, 2, 100.0, 'LUNES', 'ALMUERZO'),
(1, 3, 1.0, 'LUNES', 'CENA'),
(1, 4, 50.0, 'LUNES', 'MERIENDA'),
(1, 5, 75.0, 'LUNES', 'SNACK'),
-- MARTES
(1, 2, 100.0, 'MARTES', 'DESAYUNO'),
(1, 1, 150.0, 'MARTES', 'ALMUERZO'),
(1, 5, 120.0, 'MARTES', 'CENA'),
(1, 3, 1.0, 'MARTES', 'MERIENDA'),
(1, 4, 80.0, 'MARTES', 'SNACK'),
-- MIÉRCOLES
(1, 3, 1.0, 'MIERCOLES', 'DESAYUNO'),
(1, 4, 80.0, 'MIERCOLES', 'ALMUERZO'),
(1, 5, 100.0, 'MIERCOLES', 'CENA'),
(1, 2, 90.0, 'MIERCOLES', 'MERIENDA'),
(1, 1, 120.0, 'MIERCOLES', 'SNACK'),
-- JUEVES
(1, 1, 130.0, 'JUEVES', 'DESAYUNO'),
(1, 2, 110.0, 'JUEVES', 'ALMUERZO'),
(1, 5, 90.0, 'JUEVES', 'CENA'),
(1, 3, 1.0, 'JUEVES', 'MERIENDA'),
(1, 4, 50.0, 'JUEVES', 'SNACK'),
-- VIERNES
(1, 4, 70.0, 'VIERNES', 'DESAYUNO'),
(1, 3, 2.0, 'VIERNES', 'ALMUERZO'),
(1, 5, 110.0, 'VIERNES', 'CENA'),
(1, 2, 100.0, 'VIERNES', 'MERIENDA'),
(1, 1, 140.0, 'VIERNES', 'SNACK'),
-- SÁBADO
(1, 5, 100.0, 'SABADO', 'DESAYUNO'),
(1, 4, 90.0, 'SABADO', 'ALMUERZO'),
(1, 1, 130.0, 'SABADO', 'CENA'),
(1, 2, 110.0, 'SABADO', 'MERIENDA'),
(1, 3, 1.0, 'SABADO', 'SNACK'),
-- DOMINGO
(1, 3, 1.0, 'DOMINGO', 'DESAYUNO'),
(1, 5, 120.0, 'DOMINGO', 'ALMUERZO'),
(1, 1, 150.0, 'DOMINGO', 'CENA'),
(1, 4, 75.0, 'DOMINGO', 'MERIENDA'),
(1, 2, 100.0, 'DOMINGO', 'SNACK');

-- Insertar programas de entrenamiento
INSERT IGNORE INTO training_programmes (name, duration_weeks, user_id, is_generic, training_level) VALUES
('Fuerza Básica', 6, 1, TRUE, 'PRINCIPIANTE'),
('Cardio Intermedio', 4, 2, TRUE, 'INTERMEDIO'),
('Musculación Avanzada', 8, 3, TRUE, 'AVANZADO'),
('HIIT Express', 3, 4, FALSE, 'INTERMEDIO'),
('Full Body Fit', 5, 5, FALSE, 'PRINCIPIANTE');

-- Insertar workouts
INSERT IGNORE INTO workouts (name, description, session_Number, training_program_id) VALUES
('Pecho Día 1', 'Entrenamiento de fuerza', 1, 1),
('Piernas Día 2', 'Ejercicio intenso', 2, 1),
('Cardio Básico', 'Ejercicio cardiovascular', 1, 2),
('Upper Body', 'Fuerza superior', 1, 3),
('HIIT 20 Min', 'Alta intensidad', 1, 4);

-- Insertar ejercicios
INSERT IGNORE INTO exercises (name, video_url, muscle_group) VALUES
('Press banca', 'https://video1.com', 'PECTORAL'),
('Sentadilla', 'https://video2.com', 'PIERNA'),
('Dominadas', 'https://video3.com', 'ESPALDA'),
('Curl bíceps', 'https://video4.com', 'BRAZO'),
('Press militar', 'https://video5.com', 'HOMBRO');

-- Insertar workout_exercises
INSERT IGNORE INTO workout_exercises (workout_id, exercise_id, sets, repetitions, weight) VALUES
(1, 1, 4, 10, 60.0),
(2, 2, 4, 12, 80.0),
(3, 3, 3, 15, 0.0),
(4, 4, 4, 10, 12.5),
(5, 5, 3, 12, 30.0);

-- Insertar training_reviews
INSERT IGNORE INTO training_reviews (user_id, training_programme_id, score, comment) VALUES
(1, 1, 5, 'Muy buen entrenamiento'),
(2, 1, 4, 'Completo y accesible'),
(3, 2, 3, 'Un poco fácil'),
(4, 3, 5, 'Ideal para fuerza'),
(5, 2, 4, 'Mejor de lo que esperaba');
