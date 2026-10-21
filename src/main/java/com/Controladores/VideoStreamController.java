package com.Controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/video")
@CrossOrigin(origins = "*")
public class VideoStreamController {

    /**
     * Verifica si el archivo HLS (.m3u8) se está generando correctamente en disco.
     */
    @GetMapping("/estado")
    public ResponseEntity<String> verificarEstadoStream() {
        File streamFile = new File("src/main/resources/static/live/stream.m3u8");
        if (streamFile.exists()) {

            return ResponseEntity.ok("Streaming activo");
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("El archivo de transmisión aún no se ha generado");
        }
    }

@GetMapping("/ver")
    public void redirigirAReproductor(HttpServletResponse response) throws IOException {
        response.sendRedirect("/streaming.html");
}
}