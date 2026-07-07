package com.innovatech.pasteleria.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VentaRequest {

    @NotNull(message = "Debe indicar el pastel a vender")
    private Long pastelId;

    @NotNull(message = "Debe indicar la cantidad a vender")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}
