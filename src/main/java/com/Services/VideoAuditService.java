package com.Services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class VideoAuditService {

    @Value("${camara.ip}")
    private String cameraIp;

    @Value("${camara.usuario}")
    private String user;

    @Value("${camara.password}")
    private String password;


    public String generarUrlClipVideo(LocalDateTime fechaVenta) {
        System.out.println("La hora es" + fechaVenta.getHour());
        // Calcular intervalo: 30s antes y 30s después de la venta
        LocalDateTime inicio = fechaVenta.minusSeconds(30);
        LocalDateTime fin = fechaVenta.plusSeconds(30);

        // Formato ISO-8601 requerido por la mayoría de las cámaras IP / NVRs
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
        String startTime = inicio.format(formatter);
        String endTime = fin.format(formatter);
        System.out.println("calcule el tiempo");

        // URL de reproducción RTSP / HTTP con filtro de tiempo (Playback)
        return String.format(
            "rtsp://" + user + ":" + password +"@%s:554/Streaming/tracks/101?starttime=%s&endtime=%s",
            cameraIp, startTime, endTime
        );
    }
}
