
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
('Salmón', 208, 20.0, 0.0, 13.0);

-- Insertar dietas
INSERT IGNORE INTO diets (name, description, duration_weeks, user_id) VALUES
('Dieta Fit', 'Alta en proteínas', 4, 1),
('Dieta Vegana', 'Solo vegetal', 6, 2),
('Dieta Keto', 'Baja en carbos', 8, 3),
('Dieta Mediterránea', 'Equilibrada', 5, 4),
('Dieta Detox', 'Depurativa', 3, 5);

-- Insertar diet_food
INSERT IGNORE INTO diet_food (diet_id, food_id, quantity, day_week, meal_type) VALUES
(1, 1, 150.0, 'LUNES', 'ALMUERZO'),
(1, 2, 100.0, 'LUNES', 'ALMUERZO'),
(2, 3, 1.0, 'MARTES', 'DESAYUNO'),
(3, 5, 120.0, 'MIERCOLES', 'CENA'),
(4, 4, 200.0, 'JUEVES', 'ALMUERZO');

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
