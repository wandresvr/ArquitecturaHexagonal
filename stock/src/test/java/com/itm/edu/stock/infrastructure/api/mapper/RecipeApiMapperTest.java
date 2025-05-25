package com.itm.edu.stock.infrastructure.api.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.itm.edu.stock.application.dto.CreateRecipeCommand;
import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.application.dto.RecipeIngredientResponse;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.UpdateRecipeRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeIngredientDto;
import com.itm.edu.stock.infrastructure.api.dto.RecipeResponseDto;

class RecipeApiMapperTest {

    private RecipeApiMapper mapper;
    private UUID recipeId;
    private UUID ingredientId;
    private CreateRecipeRequestDto createRequestDto;
    private UpdateRecipeRequestDto updateRequestDto;
    private RecipeResponse recipeResponse;

    @BeforeEach
    void setUp() {
        mapper = new RecipeApiMapper();
        recipeId = UUID.randomUUID();
        ingredientId = UUID.randomUUID();

        CreateRecipeIngredientDto ingredientDto = new CreateRecipeIngredientDto();
        ingredientDto.setIngredientId(ingredientId);
        ingredientDto.setQuantity(new BigDecimal("500"));
        ingredientDto.setUnit("gramos");

        createRequestDto = new CreateRecipeRequestDto();
        createRequestDto.setName("Torta de Chocolate");
        createRequestDto.setDescription("Torta esponjosa de chocolate");
        createRequestDto.setInstructions("1. Mezclar ingredientes\n2. Hornear");
        createRequestDto.setPreparationTime(30);
        createRequestDto.setDifficulty("Medio");
        createRequestDto.setIngredients(Arrays.asList(ingredientDto));

        updateRequestDto = new UpdateRecipeRequestDto();
        updateRequestDto.setName("Torta de Chocolate Actualizada");
        updateRequestDto.setDescription("Torta esponjosa de chocolate actualizada");
        updateRequestDto.setInstructions("1. Mezclar ingredientes\n2. Hornear por 35 minutos");
        updateRequestDto.setPreparationTime(35);
        updateRequestDto.setDifficulty("Fácil");
        updateRequestDto.setIngredients(Arrays.asList(ingredientDto));

        recipeResponse = RecipeResponse.builder()
            .id(recipeId)
            .name("Torta de Chocolate")
            .description("Torta esponjosa de chocolate")
            .instructions("1. Mezclar ingredientes\n2. Hornear")
            .preparationTime(30)
            .difficulty("Medio")
            .cost(new BigDecimal("10.00"))
            .ingredients(Arrays.asList(
                RecipeIngredientResponse.builder()
                    .id(UUID.randomUUID())
                    .recipeId(recipeId)
                    .ingredientId(ingredientId)
                    .ingredientName("Harina")
                    .quantity(new BigDecimal("500"))
                    .unit("gramos")
                    .build()
            ))
            .build();
    }

    @Test
    void testToCommand_FromCreateRequest_Success() {
        // Act
        CreateRecipeCommand result = mapper.toCommand(createRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(createRequestDto.getName(), result.getName());
        assertEquals(createRequestDto.getDescription(), result.getDescription());
        assertEquals(createRequestDto.getInstructions(), result.getInstructions());
        assertEquals(createRequestDto.getPreparationTime(), result.getPreparationTime());
        assertEquals(createRequestDto.getDifficulty(), result.getDifficulty());
        
        assertNotNull(result.getIngredients());
        assertEquals(1, result.getIngredients().size());
        assertEquals(ingredientId, result.getIngredients().get(0).getIngredientId());
        assertEquals(new BigDecimal("500"), result.getIngredients().get(0).getQuantity());
        assertEquals("gramos", result.getIngredients().get(0).getUnit());
    }

    @Test
    void testToCommand_FromUpdateRequest_Success() {
        // Act
        CreateRecipeCommand result = mapper.toCommand(updateRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(updateRequestDto.getName(), result.getName());
        assertEquals(updateRequestDto.getDescription(), result.getDescription());
        assertEquals(updateRequestDto.getInstructions(), result.getInstructions());
        assertEquals(updateRequestDto.getPreparationTime(), result.getPreparationTime());
        assertEquals(updateRequestDto.getDifficulty(), result.getDifficulty());
        
        assertNotNull(result.getIngredients());
        assertEquals(1, result.getIngredients().size());
        assertEquals(ingredientId, result.getIngredients().get(0).getIngredientId());
        assertEquals(new BigDecimal("500"), result.getIngredients().get(0).getQuantity());
        assertEquals("gramos", result.getIngredients().get(0).getUnit());
    }

    @Test
    void testToCommand_FromUpdateRequest_WithNullIngredients() {
        // Arrange
        updateRequestDto.setIngredients(null);

        // Act
        CreateRecipeCommand result = mapper.toCommand(updateRequestDto);

        // Assert
        assertNotNull(result);
        assertNull(result.getIngredients());
    }

    @Test
    void testToDto_Success() {
        // Act
        RecipeResponseDto result = mapper.toDto(recipeResponse);

        // Assert
        assertNotNull(result);
        assertEquals(recipeResponse.getId(), result.getId());
        assertEquals(recipeResponse.getName(), result.getName());
        assertEquals(recipeResponse.getDescription(), result.getDescription());
        assertEquals(recipeResponse.getInstructions(), result.getInstructions());
        assertEquals(recipeResponse.getPreparationTime(), result.getPreparationTime());
        assertEquals(recipeResponse.getDifficulty(), result.getDifficulty());
        assertEquals(recipeResponse.getCost(), result.getCost());
        
        assertNotNull(result.getIngredients());
        assertEquals(1, result.getIngredients().size());
        assertEquals(ingredientId, result.getIngredients().get(0).getId());
        assertEquals("Harina", result.getIngredients().get(0).getName());
        assertEquals(new BigDecimal("500"), result.getIngredients().get(0).getQuantity());
        assertEquals("gramos", result.getIngredients().get(0).getUnit());
    }

    @Test
    void testToDto_WithNullIngredients() {
        // Arrange
        RecipeResponse responseWithNullIngredients = RecipeResponse.builder()
            .id(recipeId)
            .name("Test Recipe")
            .ingredients(null)
            .build();

        // Act
        RecipeResponseDto result = mapper.toDto(responseWithNullIngredients);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getIngredients());
        assertTrue(result.getIngredients().isEmpty());
    }

    @Test
    void testToDto_WithEmptyIngredients() {
        // Arrange
        RecipeResponse responseWithEmptyIngredients = RecipeResponse.builder()
            .id(recipeId)
            .name("Test Recipe")
            .ingredients(new ArrayList<>())
            .build();

        // Act
        RecipeResponseDto result = mapper.toDto(responseWithEmptyIngredients);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getIngredients());
        assertTrue(result.getIngredients().isEmpty());
    }

    @Test
    void testToCommand_FromCreateRequest_WithNullRequest() {
        // Act
        CreateRecipeCommand result = mapper.toCommand((CreateRecipeRequestDto) null);

        // Assert
        assertNull(result);
    }

    @Test
    void testToCommand_FromUpdateRequest_WithNullRequest() {
        // Act
        CreateRecipeCommand result = mapper.toCommand((UpdateRecipeRequestDto) null);

        // Assert
        assertNull(result);
    }

    @Test
    void testToCommand_FromCreateRequest_WithEmptyFields() {
        // Arrange
        CreateRecipeRequestDto emptyRequest = new CreateRecipeRequestDto();
        emptyRequest.setName("");
        emptyRequest.setDescription("");
        emptyRequest.setInstructions("");
        emptyRequest.setPreparationTime(null);
        emptyRequest.setDifficulty("");
        emptyRequest.setIngredients(new ArrayList<>());

        // Act
        CreateRecipeCommand result = mapper.toCommand(emptyRequest);

        // Assert
        assertNotNull(result);
        assertEquals("", result.getName());
        assertEquals("", result.getDescription());
        assertEquals("", result.getInstructions());
        assertNull(result.getPreparationTime());
        assertEquals("", result.getDifficulty());
        assertNotNull(result.getIngredients());
        assertTrue(result.getIngredients().isEmpty());
    }

    @Test
    void testToCommand_FromUpdateRequest_WithEmptyFields() {
        // Arrange
        UpdateRecipeRequestDto emptyRequest = new UpdateRecipeRequestDto();
        emptyRequest.setName("");
        emptyRequest.setDescription("");
        emptyRequest.setInstructions("");
        emptyRequest.setPreparationTime(null);
        emptyRequest.setDifficulty("");
        emptyRequest.setIngredients(new ArrayList<>());

        // Act
        CreateRecipeCommand result = mapper.toCommand(emptyRequest);

        // Assert
        assertNotNull(result);
        assertEquals("", result.getName());
        assertEquals("", result.getDescription());
        assertEquals("", result.getInstructions());
        assertNull(result.getPreparationTime());
        assertEquals("", result.getDifficulty());
        assertNotNull(result.getIngredients());
        assertTrue(result.getIngredients().isEmpty());
    }

    @Test
    void testToCommand_FromCreateRequest_WithMaximumValues() {
        // Arrange
        CreateRecipeRequestDto maxRequest = new CreateRecipeRequestDto();
        maxRequest.setName("Test Recipe With Very Long Name That Should Still Be Valid");
        maxRequest.setDescription("Test Description With Maximum Length That Should Be Accepted By The System And Still Process Correctly");
        maxRequest.setInstructions("1. First instruction with maximum length\n2. Second instruction with maximum length\n3. Third instruction with maximum length");
        maxRequest.setPreparationTime(Integer.MAX_VALUE);
        maxRequest.setDifficulty("Very Difficult And Complex Recipe That Requires Expert Skills");

        CreateRecipeIngredientDto maxIngredient = new CreateRecipeIngredientDto();
        maxIngredient.setIngredientId(ingredientId);
        maxIngredient.setQuantity(BigDecimal.valueOf(999999.99));
        maxIngredient.setUnit("kilogramos");
        maxRequest.setIngredients(Arrays.asList(maxIngredient));

        // Act
        CreateRecipeCommand result = mapper.toCommand(maxRequest);

        // Assert
        assertNotNull(result);
        assertEquals(maxRequest.getName(), result.getName());
        assertEquals(maxRequest.getDescription(), result.getDescription());
        assertEquals(maxRequest.getInstructions(), result.getInstructions());
        assertEquals(maxRequest.getPreparationTime(), result.getPreparationTime());
        assertEquals(maxRequest.getDifficulty(), result.getDifficulty());
        assertNotNull(result.getIngredients());
        assertEquals(1, result.getIngredients().size());
        assertEquals(ingredientId, result.getIngredients().get(0).getIngredientId());
        assertEquals(maxIngredient.getQuantity(), result.getIngredients().get(0).getQuantity());
        assertEquals(maxIngredient.getUnit(), result.getIngredients().get(0).getUnit());
    }

    @Test
    void testToDto_WithNullResponse() {
        // Act
        RecipeResponseDto result = mapper.toDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testToDto_WithNullFields() {
        // Arrange
        RecipeResponse nullFieldsResponse = RecipeResponse.builder()
            .id(recipeId)
            .name(null)
            .description(null)
            .instructions(null)
            .preparationTime(null)
            .difficulty(null)
            .cost(null)
            .ingredients(null)
            .build();

        // Act
        RecipeResponseDto result = mapper.toDto(nullFieldsResponse);

        // Assert
        assertNotNull(result);
        assertEquals(recipeId, result.getId());
        assertNull(result.getName());
        assertNull(result.getDescription());
        assertNull(result.getInstructions());
        assertNull(result.getPreparationTime());
        assertNull(result.getDifficulty());
        assertNull(result.getCost());
        assertNotNull(result.getIngredients());
        assertTrue(result.getIngredients().isEmpty());
    }

    @Test
    void testToDto_WithMultipleIngredients() {
        // Arrange
        RecipeResponse responseWithMultipleIngredients = RecipeResponse.builder()
            .id(recipeId)
            .name("Test Recipe")
            .ingredients(Arrays.asList(
                RecipeIngredientResponse.builder()
                    .id(UUID.randomUUID())
                    .recipeId(recipeId)
                    .ingredientId(ingredientId)
                    .ingredientName("Harina")
                    .quantity(new BigDecimal("500"))
                    .unit("gramos")
                    .build(),
                RecipeIngredientResponse.builder()
                    .id(UUID.randomUUID())
                    .recipeId(recipeId)
                    .ingredientId(UUID.randomUUID())
                    .ingredientName("Azúcar")
                    .quantity(new BigDecimal("250"))
                    .unit("gramos")
                    .build()
            ))
            .build();

        // Act
        RecipeResponseDto result = mapper.toDto(responseWithMultipleIngredients);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getIngredients());
        assertEquals(2, result.getIngredients().size());
        
        // Verificar primer ingrediente
        assertEquals("Harina", result.getIngredients().get(0).getName());
        assertEquals(new BigDecimal("500"), result.getIngredients().get(0).getQuantity());
        assertEquals("gramos", result.getIngredients().get(0).getUnit());
        
        // Verificar segundo ingrediente
        assertEquals("Azúcar", result.getIngredients().get(1).getName());
        assertEquals(new BigDecimal("250"), result.getIngredients().get(1).getQuantity());
        assertEquals("gramos", result.getIngredients().get(1).getUnit());
    }
} 