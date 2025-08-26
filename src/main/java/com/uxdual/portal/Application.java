package com.uxdual.portal;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "UX Dual Commerce Portal",
        version = "1.0",
        description = "Multi-tenant commerce management portal with real-time QR payment confirmation"
    )
)
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
