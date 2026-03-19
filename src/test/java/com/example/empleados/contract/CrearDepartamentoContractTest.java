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
import com.example.empleados.controller.DepartamentoController;
import com.example.empleados.dto.DepartamentoRequest;
import com.example.empleados.dto.DepartamentoResponse;
import com.example.empleados.service.DepartamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = DepartamentoController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "app.security.username=admin",
        "app.security.password=admin123"
})
class CrearDepartamentoContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DepartamentoService departamentoService;

    @Test
    void shouldCreateDepartamento() throws Exception {
        DepartamentoRequest request = new DepartamentoRequest();
        request.setClave("rh");
        request.setNombre("Recursos Humanos");

        DepartamentoResponse response = new DepartamentoResponse();
        response.setClave("RH");
        response.setNombre("Recursos Humanos");
        response.setEmployeeCount(0);

        Mockito.when(departamentoService.crear(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/api/v2/departamentos")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clave").value("RH"))
                .andExpect(jsonPath("$.employeeCount").value(0));
    }
}
