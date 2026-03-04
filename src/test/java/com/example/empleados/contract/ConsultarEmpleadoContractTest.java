package com.example.empleados.contract;

import com.example.empleados.config.SecurityConfig;
import com.example.empleados.controller.EmpleadoController;
import com.example.empleados.dto.EmpleadoResponse;
import com.example.empleados.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "app.security.username=admin",
        "app.security.password=admin123"
})
class ConsultarEmpleadoContractTest {

    @Autowired
    private MockMvc mockMvc;

        @MockitoBean
    private EmpleadoService empleadoService;

    @Test
    void shouldListAndGetEmpleado() throws Exception {
        EmpleadoResponse empleado = new EmpleadoResponse();
        empleado.setClave("EMP-10");
        empleado.setNombre("Ana");
        empleado.setDireccion("Calle 2");
        empleado.setTelefono("555-456");

        Mockito.when(empleadoService.listar()).thenReturn(List.of(empleado));
        Mockito.when(empleadoService.obtenerPorClave("EMP-10")).thenReturn(empleado);

        mockMvc.perform(get("/empleados")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clave").value("EMP-10"));

        mockMvc.perform(get("/empleados/EMP-10")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clave").value("EMP-10"));
    }
}
