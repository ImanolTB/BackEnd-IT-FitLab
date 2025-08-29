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

## ▶️ Ejecución en desarrollo

1. Clonar el repositorio
   ```bash
   git clone https://github.com/usuario/it-fitlab-backend.git
   cd it-fitlab-backend
Crear un archivo .env con las variables de entorno (DB, JWT, rutas de subida, etc.).
👉 Recuerda añadir .env al .gitignore.

Levantar la base de datos con Docker:

bash
Copiar código
docker compose up -d
Arrancar el backend:

bash
Copiar código
./mvnw spring-boot:run
Acceder a la API en:

bash
Copiar código
http://localhost:8080/api/v1
📖 Documentación API
Durante el desarrollo, la documentación Swagger está disponible en:

bash
Copiar código
http://localhost:8080/swagger-ui/index.html
En producción, esta documentación se deshabilita por motivos de seguridad.

📂 Endpoints principales
Usuarios → /api/v1/user/...

Dietas → /api/v1/diets/...

Alimentos → /api/v1/food/...

Entrenamientos → /api/v1/trainingProgrammes/...

Workouts → /api/v1/workouts/...

Ejercicios → /api/v1/exercises/...

Reseñas → /api/v1/reviews/...

Auth → /api/v1/login

🐳 Despliegue en producción
Despliegue mediante Dockerfile multistage

Configuración con .env y keystore JWT (.jks)

Proxy inverso con Nginx y SSL (Let's Encrypt)

Backend corriendo en contenedor aislado (sin exponer directamente el puerto 8080)

📜 Este proyecto se desarrolla como parte del TFG IT FitLab.
