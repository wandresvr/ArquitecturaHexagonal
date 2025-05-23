package com.itm.edu.stock.infrastructure.api.controllers;

import com.itm.edu.stock.application.ports.input.CreateIngredientUseCase;
import com.itm.edu.stock.application.ports.input.GetIngredientUseCase;
import com.itm.edu.stock.infrastructure.api.dto.CreateIngredientRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.IngredientResponseDto;
import com.itm.edu.stock.infrastructure.api.dto.ErrorResponse;
import com.itm.edu.stock.infrastructure.api.mapper.IngredientApiMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ingredients")
@RequiredArgsConstructor
@Tag(name = "Ingredientes", description = "API para la gestión de ingredientes")
public class IngredientController {

    private final CreateIngredientUseCase createIngredientUseCase;
    private final GetIngredientUseCase getIngredientUseCase;
    private final IngredientApiMapper ingredientMapper;

    @Operation(summary = "Crear un nuevo ingrediente", description = "Crea un nuevo ingrediente con los datos especificados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ingrediente creado exitosamente",
            content = @Content(schema = @Schema(implementation = IngredientResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos del ingrediente inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<IngredientResponseDto> createIngredient(@RequestBody CreateIngredientRequestDto request) {
        var command = ingredientMapper.toCommand(request);
        var response = createIngredientUseCase.createIngredient(command);
        return ResponseEntity.ok(ingredientMapper.toResponseDto(response));
    }

    @Operation(summary = "Obtener todos los ingredientes", description = "Retorna una lista de todos los ingredientes disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ingredientes obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = IngredientResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<IngredientResponseDto>> getAllIngredients() {
        var ingredients = getIngredientUseCase.getAllIngredients();
        var response = ingredients.stream()
                .map(ingredientMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener un ingrediente por ID", description = "Retorna un ingrediente específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ingrediente encontrado exitosamente",
            content = @Content(schema = @Schema(implementation = IngredientResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Ingrediente no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponseDto> getIngredientById(@PathVariable UUID id) {
        var ingredient = getIngredientUseCase.getIngredientById(id);
        return ResponseEntity.ok(ingredientMapper.toResponseDto(ingredient));
    }

    @Operation(summary = "Actualizar un ingrediente", description = "Actualiza un ingrediente existente con nuevos datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ingrediente actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = IngredientResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos del ingrediente inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Ingrediente no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponseDto> updateIngredient(
            @Parameter(description = "ID del ingrediente", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody CreateIngredientRequestDto request) {
        var command = ingredientMapper.toCommand(request);
        var response = createIngredientUseCase.updateIngredient(id, command);
        return ResponseEntity.ok(ingredientMapper.toResponseDto(response));
    }

    @Operation(summary = "Eliminar un ingrediente", description = "Elimina un ingrediente específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ingrediente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Ingrediente no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable UUID id) {
        createIngredientUseCase.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener ingredientes por proveedor", description = "Retorna una lista de ingredientes filtrados por proveedor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ingredientes obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = IngredientResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Proveedor inválido",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/supplier/{supplier}")
    public ResponseEntity<List<IngredientResponseDto>> getIngredientsBySupplier(
            @Parameter(description = "Nombre del proveedor", required = true)
            @PathVariable String supplier) {
        var responses = getIngredientUseCase.getIngredientsBySupplier(supplier);
        var dtos = responses.stream()
                .map(ingredientMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
} 