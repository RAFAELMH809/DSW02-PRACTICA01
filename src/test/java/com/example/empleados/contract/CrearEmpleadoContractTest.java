package com.example.empleados.contract;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.empleados.config.SecurityConfig;
import com.example.empleados.controller.EmpleadoController;
import com.example.empleados.dto.EmpleadoRequest;
import com.example.empleados.dto.EmpleadoResponse;
import com.example.empleados.service.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = EmpleadoController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "app.security.username=admin",
        "app.security.password=admin123"
})
class CrearEmpleadoContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmpleadoService empleadoService;

    @Test
    void shouldCreateEmpleadoAndReturnGeneratedKey() throws Exception {
        EmpleadoRequest request = new EmpleadoRequest();
        request.setNombre("Juan");
        request.setDireccion("Calle 1");
        request.setTelefono("555-123");

        EmpleadoResponse response = new EmpleadoResponse();
        response.setClave("EMP-1");
        response.setNombre("Juan");
        response.setDireccion("Calle 1");
        response.setTelefono("555-123");

        Mockito.when(empleadoService.crear(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/api/v2/empleados")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clave").value("EMP-1"));
    }
}
