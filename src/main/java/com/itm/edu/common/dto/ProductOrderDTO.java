package com.itm.edu.common.dto;

import lombok.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * DTO que describe un producto dentro de una orden:
 * su ID y la cantidad pedida.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /** Identificador del producto */
    private UUID productId;
    /** Cantidad solicitada */
    private int quantity;
}
