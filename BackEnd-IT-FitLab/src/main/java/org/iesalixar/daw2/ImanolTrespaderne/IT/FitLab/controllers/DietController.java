package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.FoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UpdateUserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.DietService;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.FoodService;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.UserService;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diets")
@Tag(name = "Dietas", description = "Operaciones relacionadas con la gestión de dietas")
public class DietController {
    private static final Logger logger = LoggerFactory.getLogger(DietController.class);

    @Autowired
    private DietService dietService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private FoodService foodService;

    /**
     * Obtener todas las dietas.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener todas las dietas", description = "Solo accesible para administradores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de dietas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DietDTO.class))),
            @ApiResponse(responseCode = "204", description = "No hay dietas registradas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<DietDTO>> getAllDiets() {
        logger.info("Solicitando la lista de todas las dietas...");
        try {
            List<DietDTO> diets = dietService.getAllDiets();
            if (diets.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(diets);
        } catch (Exception e) {
            logger.error("Error al listar las dietas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @Operation(summary = "Obtener una dieta por ID", description = "Debe ser el propietario de la dieta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dieta encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DietDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Dieta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getDietById(@PathVariable Long id) {
        logger.info("Buscando dieta con ID: {}", id);
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            dietService.validateOwnership(id, username);
            return ResponseEntity.ok(dietService.getDietById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error interno al obtener la dieta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    @Operation(summary = "Obtener las dietas del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de dietas del usuario"),
            @ApiResponse(responseCode = "404", description = "No se encontraron dietas"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping("/user")
    public ResponseEntity<?> getDietByUsername() {
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            List<DietDTO> diets = dietService.getDietByUsername(username);

            if (diets.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron dietas para el usuario.");
            }

            return ResponseEntity.ok(diets);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error al obtener dietas por username: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    @Operation(summary = "Obtener alimentos de una dieta por día de la semana")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de alimentos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodDTO.class))),
            @ApiResponse(responseCode = "400", description = "Día inválido"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta accion"),
            @ApiResponse(responseCode = "404", description = "No se encontraron alimentos"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping("/{id}/day/{dayOfWeek}")
    public ResponseEntity<?> getFoodsByDietAndDay(@PathVariable Long id, @PathVariable String dayOfWeek) {
        logger.info("Obteniendo alimentos de la dieta {} para el día {}", id, dayOfWeek);
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            dietService.validateOwnership(id, username);

            DayOfTheWeek dayEnum;
            try {
                dayEnum = DayOfTheWeek.valueOf(dayOfWeek.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Día inválido: " + dayOfWeek);
            }

            List<FoodDTO> foods = foodService.getFoodsByDay(id, dayEnum);
            return foods.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron alimentos para el día " + dayOfWeek)
                    : ResponseEntity.ok(foods);

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al obtener alimentos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }
    @Operation(summary = "Crear una nueva dieta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dieta creada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DietDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PostMapping
    public ResponseEntity<?> createDiet(@Valid @RequestBody DietDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(dietService.createDiet(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la dieta.");
        }
    }
    @Operation(summary = "Actualizar una dieta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dieta actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiet(@PathVariable Long id, @Valid @RequestBody DietDTO dto) {
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            dietService.validateOwnership(id, username);
            return ResponseEntity.ok(dietService.updateDiet(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al actualizar la dieta.");
        }
    }
    @Operation(summary = "Eliminar una dieta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dieta eliminada con éxito"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Dieta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiet(@PathVariable Long id) {
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            DietDTO diet = dietService.getDietById(id);
            if (!dietService.isAdmin(username) && !diet.getUser().getUsername().equals(username)) {
                throw new SecurityException("No autorizado");
            }
            dietService.deleteDiet(id);
            return ResponseEntity.ok("Dieta con ID " + id + " eliminada con éxito.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar la dieta.");
        }
    }

}
