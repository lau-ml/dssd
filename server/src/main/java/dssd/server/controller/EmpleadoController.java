package dssd.server.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/employees")
public class EmpleadoController {

    @PreAuthorize("hasAuthority('PERMISO_VER_EMPLEADOS')")
    @GetMapping("/get-all")
    public String obtenerEmpleados() {
        return "Obtener empleados";
    }

    @PreAuthorize("hasAuthority('PERMISO_CREAR_EMPLEADOS')")
    @GetMapping("/create")
    public String crearEmpleado() {
        return "Crear empleado";
    }

    @PreAuthorize("hasAuthority('PERMISO_ELIMINAR_EMPLEADOS')")
    @DeleteMapping ("/delete")
    public String eliminarEmpleado() {
        return "Eliminar empleado";
    }


    @PreAuthorize("hasAuthority('PERMISO_MODIFICAR_EMPLEADOS')")
    @PutMapping("/update")
    public String modificarEmpleado() {
        return "Modificar empleado";
    }


}
