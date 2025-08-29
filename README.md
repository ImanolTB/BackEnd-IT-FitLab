# 🏋️‍♂️ IT FitLab - Backend

Este repositorio contiene el **backend de IT FitLab**, una aplicación fullstack para la gestión de **entrenamientos** y **dietas personalizadas**.  
El backend está desarrollado con **Java 21 + Spring Boot**, expone una **API REST** y gestiona la persistencia de datos en **MySQL/MariaDB**.

---

## ⚙️ Tecnologías utilizadas

- **Java 21 (OpenJDK 21.0.5)**  
- Spring Boot  
- Spring Data JPA  
- MySQL / MariaDB  
- JWT (autenticación segura)  
- Docker + Docker Compose  


---

## 🚀 Requisitos previos

- Tener instalado **Java 21**  
- Tener instalado **Maven**  
- Docker y Docker Compose (para la base de datos y despliegues)  

---
▶️ Ejecución en desarrollo

1️⃣ Clonar el repositorio

git clone https://github.com/usuario/it-fitlab-backend.git
cd it-fitlab-backend


2️⃣ Crear archivo .env con las variables de entorno
Ejemplo de configuración mínima:

DB_URL=jdbc:mysql://localhost:3306/itfitlab

DB_USER=prueba

DB_PASSWORD=1111

JWT_SECRET=miClaveSecreta

UPLOAD_PATH=/app/videos


⚠️ Recuerda añadir .env al .gitignore para no subirlo a GitHub.

3️⃣ Levantar la base de datos con Docker

docker compose up -d


4️⃣ Arrancar el backend con Maven

./mvnw spring-boot:run


5️⃣ Acceder a la API

http://localhost:8080/api/v1

📖 Documentación API

Durante el desarrollo, puedes usar Swagger UI:

http://localhost:8080/swagger-ui/index.html


🔒 En producción, esta documentación se encuentra deshabilitada por seguridad.

📂 Endpoints principales
Usuarios → /api/v1/user/...

Dietas → /api/v1/diets/...

Alimentos → /api/v1/food/...

Entrenamientos → /api/v1/trainingProgrammes/...

Workouts → /api/v1/workouts/...

Ejercicios → /api/v1/exercises/...

Reseñas → /api/v1/reviews/...

Auth → /api/v1/login

🌐 Entorno de producción

- VPS con Ubuntu Server 24.04 LTS

- Despliegue con Dockerfiles multistage (Spring Boot, NodeJS, MariaDB, phpMyAdmin)

- Nginx como proxy inverso + certificado SSL con Let's Encrypt

- Frontend expuesto en puerto 3000

- Backend en contenedor aislado (sin exponer 8080 directamente)
  

📜 Este proyecto se desarrolla como parte del TFG IT FitLab.
