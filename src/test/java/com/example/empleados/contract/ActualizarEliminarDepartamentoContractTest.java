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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.empleados.config.SecurityConfig;
import com.example.empleados.controller.DepartamentoController;
import com.example.empleados.dto.DepartamentoResponse;
import com.example.empleados.dto.DepartamentoUpdateRequest;
import com.example.empleados.service.DepartamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = DepartamentoController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "app.security.username=admin",
        "app.security.password=admin123"
})
class ActualizarEliminarDepartamentoContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DepartamentoService departamentoService;

    @Test
    void shouldUpdateAndDeleteDepartamento() throws Exception {
        DepartamentoUpdateRequest request = new DepartamentoUpdateRequest();
        request.setNombre("Gestion Humana");

        DepartamentoResponse response = new DepartamentoResponse();
        response.setClave("RH");
        response.setNombre("Gestion Humana");
        response.setEmployeeCount(0);

        Mockito.when(departamentoService.actualizar(Mockito.eq("RH"), Mockito.any())).thenReturn(response);

        mockMvc.perform(put("/api/v2/departamentos/RH")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Gestion Humana"));

        mockMvc.perform(delete("/api/v2/departamentos/RH")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
                .andExpect(status().isNoContent());
    }
}
