package com.itm.edu.common.dto;

import lombok.*;
import java.util.UUID;

/**
 * DTO que describe un producto dentro de una orden:
 * su ID y la cantidad pedida.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderDTO {
    /** Identificador del producto */
    private UUID productId;
    /** Cantidad solicitada */
    private int quantity;
}
