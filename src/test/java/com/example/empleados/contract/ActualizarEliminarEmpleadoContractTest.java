package com.example.empleados.contract;

import com.example.empleados.config.SecurityConfig;
import com.example.empleados.controller.EmpleadoController;
import com.example.empleados.dto.EmpleadoRequest;
import com.example.empleados.dto.EmpleadoResponse;
import com.example.empleados.service.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "app.security.username=admin",
        "app.security.password=admin123"
})
class ActualizarEliminarEmpleadoContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

        @MockitoBean
    private EmpleadoService empleadoService;

    @Test
    void shouldUpdateAndDeleteEmpleado() throws Exception {
        EmpleadoRequest request = new EmpleadoRequest();
        request.setNombre("Luis");
        request.setDireccion("Calle 3");
        request.setTelefono("555-789");
        request.setDepartamentoClave("SIN_DEPTO");
        EmpleadoResponse response = new EmpleadoResponse();
        response.setClave("EMP-2");
        response.setNombre("Luis");
        response.setDireccion("Calle 3");
        response.setTelefono("555-789");
        response.setDepartamentoClave("SIN_DEPTO");
        Mockito.when(empleadoService.actualizar(Mockito.eq("EMP-2"), Mockito.any())).thenReturn(response);

        mockMvc.perform(put("/api/v2/empleados/EMP-2")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/v2/empleados/EMP-2")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
                .andExpect(status().isNoContent());
    }
}



