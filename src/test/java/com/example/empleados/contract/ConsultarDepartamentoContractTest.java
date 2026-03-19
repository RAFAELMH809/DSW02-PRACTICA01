package com.example.empleados.contract;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.empleados.config.SecurityConfig;
import com.example.empleados.controller.DepartamentoController;
import com.example.empleados.dto.DepartamentoResponse;
import com.example.empleados.service.DepartamentoService;

@WebMvcTest(controllers = DepartamentoController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "app.security.username=admin",
        "app.security.password=admin123"
})
class ConsultarDepartamentoContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartamentoService departamentoService;

    @Test
    void shouldGetDepartamentoByClaveAndList() throws Exception {
        DepartamentoResponse response = new DepartamentoResponse();
        response.setClave("TI");
        response.setNombre("Tecnologia");
        response.setEmployeeCount(2);

        Mockito.when(departamentoService.obtenerPorClave("TI")).thenReturn(response);
        Mockito.when(departamentoService.listar(any()))
                .thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/api/v2/departamentos/TI")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clave").value("TI"));

        mockMvc.perform(get("/api/v2/departamentos")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].clave").value("TI"));
    }
}
