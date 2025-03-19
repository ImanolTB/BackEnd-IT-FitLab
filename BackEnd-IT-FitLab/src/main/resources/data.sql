-- Insertar roles
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_USER');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_ADMIN');

-- Insertar usuarios
INSERT IGNORE INTO users (id, username, password, email, name, last_name, age, height, weight, gender, activity_level, enabled)
VALUES
(1, 'itrebar', '$2a$10$6ai67s52EqUlqxbbLtPDc.31QAwDV5iMuB6O5Z/QJn1cSYjmw8OW2', 'usuario1@ejemplo.com', 'Imanol', 'Trespaderne', 30, 1.75, 70.5, 'M', 'MODERADO', true),
(2, 'user', '$2a$10$6ai67s52EqUlqxbbLtPDc.31QAwDV5iMuB6O5Z/QJn1cSYjmw8OW2', 'admin@ejemplo.com', 'Administrador', 'Principal', 35, 1.80, 80.0, 'f', 'ACTIVO', true);

-- Asignar roles a usuarios
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (1, 2);
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (2, 1);

-- Insertar programas de entrenamiento
INSERT IGNORE INTO training_programmes (id, name, duration_weeks, user_id)
VALUES
(1, 'Entrenamiento de Fuerza', 12, 1),
(2, 'Programa de Definición', 8, 2);

-- Insertar entrenamientos
INSERT IGNORE INTO workouts (id, name, description, session_number, training_program_id)
VALUES
(1, 'Pecho y Tríceps', 'Rutina enfocada en la fuerza del tren superior', 1, 1),
(2, 'Piernas y Glúteos', 'Ejercicios para fortalecer el tren inferior', 2, 2);

-- Insertar ejercicios
INSERT IGNORE INTO exercises (id, name, video_url, muscle_group)
VALUES
(1, 'Press de Banca', 'https://ejemplo.com/press-banca', 'Pectoral'),
(2, 'Sentadillas', 'https://ejemplo.com/sentadillas', 'Pierna');

-- Relacionar entrenamientos con ejercicios
INSERT IGNORE INTO workout_exercises (workout_id, exercise_id, sets, repetitions, weight)
VALUES
(1, 1, 4, 10, 80.0),
(2, 2, 5, 12, 60.0);

-- Insertar alimentos
INSERT IGNORE INTO food (id, name, calories, proteins, carbohydrates, fats)
VALUES
(1, 'Pollo', 165, 31, 0, 3.6),
(2, 'Arroz', 130, 2.7, 28, 0.3);

-- Insertar dietas
INSERT IGNORE INTO diets (id, name, description, duration_weeks, user_id)
VALUES
(1, 'Dieta de Volumen', 'Plan de alimentación para ganar masa muscular', 12, 1),
(2, 'Dieta de Definición', 'Plan de alimentación para reducir grasa corporal', 8, 2);

-- Relacionar dietas con alimentos
INSERT IGNORE INTO diet_food (diet_id, food_id, day_week, meal_type, quantity)
VALUES
(1, 1, 'Lunes', 'Almuerzo', 200),
(1, 2, 'Lunes', 'Cena', 150),
(2, 1, 'Martes', 'Almuerzo', 180),
(2, 2, 'Martes', 'Cena', 140);
