


CREATE DATABASE IF NOT EXISTS IT_FitLab;
USE IT_FitLab;


--1. Tabla roles

--CREATE TABLE IF NOT EXISTS roles (
 -- id BIGINT PRIMARY KEY AUTO_INCREMENT,
 -- name VARCHAR(50) UNIQUE NOT NULL
--);

-- 2. Tabla users

CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
username VARCHAR(50) UNIQUE NOT NULL,
 password VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  name VARCHAR(100),
  last_name VARCHAR(100),
 age INT,
  height DECIMAL(5,2),
 weight DECIMAL(5,2),
  gender ENUM('M','F'),
  activity_level ENUM('SEDENTARIO','LIGERO','MODERADO','ACTIVO','MUY_ACTIVO')
);

-- 3. Tabla UserRole

--CREATE TABLE IF NOT EXISTS user_roles (
 -- user_id BIGINT NOT NULL,
 -- role_id BIGINT NOT NULL,
 -- PRIMARY KEY (user_id, role_id),
 -- FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
 -- FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
--);

-- 4. TABLA Alimento

CREATE TABLE IF NOT EXISTS food (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  calories INT,
  proteins DECIMAL(5,2),
  carbohydrates DECIMAL(5,2),
  fats DECIMAL(5,2)
);

-- 5. TABLA Dieta

CREATE TABLE IF NOT EXISTS diets (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(100),
  duration_weeks INT,
  user_id BIGINT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
  ON DELETE CASCADE ON UPDATE CASCADE
);

-- 6. Tabla Dieta_Alimento

CREATE TABLE IF NOT EXISTS diet_food (

  diet_id INT NOT NULL,
  food_id INT NOT NULL,
  quantity DECIMAL(5,2),
  day_week ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO'),
  meal_type ENUM('DESAYUNO','ALMUERZO','MERIENDA','CENA','SNACK'),
  PRIMARY KEY(diet_id, food_id, day_week, meal_type),
  FOREIGN KEY (diet_id) REFERENCES diets(id)
  ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (food_id) REFERENCES food(id)
  ON DELETE CASCADE ON UPDATE CASCADE
) ;

-- 7. TABLA Programa_Entrenamiento

CREATE TABLE IF NOT EXISTS training_programmes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  duration_weeks INT,
  user_id BIGINT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)

);


-- 8. TABLA Entrenamiento

CREATE TABLE IF NOT EXISTS workouts (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(100),
  training_program_id INT NOT NULL,
    FOREIGN KEY (training_program_id) REFERENCES training_programmes(id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ;

-- 9. TABLA Ejercicio

CREATE TABLE IF NOT EXISTS exercises (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  video_url VARCHAR(255),
  muscle_group ENUM('PECTORAL', 'ESPALDA', 'HOMBRO', 'BRAZO', 'PIERNA')
);

-- 10. TABLA Entrenamiento_Ejercicio

CREATE TABLE IF NOT EXISTS workout_exercises (
  workout_id INT NOT NULL,
  exercise_id INT NOT NULL,
  sets INT,
  repetitions INT,
  weight DECIMAL(5,2),
  PRIMARY KEY (workout_id, exercise_id),
  FOREIGN KEY (workout_id) REFERENCES workouts(id)
  ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (exercise_id) REFERENCES exercises(id)
  ON DELETE CASCADE ON UPDATE CASCADE
);
