package com.itm.edu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private String name;
    private String email;
    private String phone;
} 