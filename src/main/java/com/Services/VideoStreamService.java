// package com.Services;

// import jakarta.annotation.PostConstruct;
// import jakarta.annotation.PreDestroy;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// import java.io.File;
// import java.io.IOException;


// @Service
// public class VideoStreamService {

//     @Value("${dvr.rtsp.url}")
//     private String rtspUrl;

//     private Process ffmpegProcess;

//     @PostConstruct
//     public void iniciarTransmisionHls() {
        
//         try {
//             // 1. Determinar la ruta de FFmpeg (Soporta ejecutable local o instalado en el sistema)
//             String rutaFFmpeg = obtenerRutaFFmpeg();

//             // 2. Asegurarse de que la carpeta de destino estática exista (src/main/resources/static/live)
//             File carpetaLive = new File("src/main/resources/static/live");
//             if (!carpetaLive.exists()) {
//                 carpetaLive.mkdirs();
//             }

//             // 3. Crear y configurar el proceso de FFmpeg
//             //proceso para camara dc200
//             // ProcessBuilder builder = new ProcessBuilder(
//             //     rutaFFmpeg,                        // Se usa la variable con la ruta resuelta
//             //     "-rtsp_transport", "tcp",
//             //     "-i", rtspUrl,
//             //     "-c:v", "copy",                    // Copia rápida del video (no consume casi CPU)
//             //     "-c:a", "aac",
//             //     "-f", "hls",
//             //     "-hls_time", "2",
//             //     "-hls_list_size", "5",
//             //     "-hls_flags", "delete_segments",
//             //     "src/main/resources/static/live/stream.m3u8"
//             // );
//             ProcessBuilder builder = new ProcessBuilder(
//         rutaFFmpeg,
//         "-rtsp_transport", "tcp",   // Indispensable para DVRs para evitar pérdida de cuadros
//         "-i", rtspUrl,
//         "-c:v", "copy",             // Copia directa del video sin recodificar (baja carga de CPU)
//         "-c:a", "aac",
//         "-f", "hls",
//         "-hls_time", "2",
//         "-hls_list_size", "5",
//         "-hls_flags", "delete_segments",
//         "src/main/resources/static/live/stream.m3u8"
//     );

//             // Heredar logs en la consola de Spring para ver si hay errores de conexión
//             builder.redirectError(ProcessBuilder.Redirect.INHERIT);

//             System.out.println("Iniciando FFmpeg usando la ruta: " + rutaFFmpeg);
//             ffmpegProcess = builder.start();

//         } catch (IOException e) {
//             System.err.println("Error al iniciar la transmisión de video: " + e.getMessage());
//         }
//     }

//     /**
//      * Método auxiliar para buscar el ejecutable de FFmpeg.
//      * Revisa primero si está en la carpeta bin/ local de la app,
//      * y si no, asume que está instalado en las Variables de Entorno del sistema.
//      */
//     /**private String obtenerRutaFFmpeg() {
//         // Intenta buscar bin/ffmpeg.exe (Windows) o bin/ffmpeg (Linux) dentro de la carpeta del proyecto
//         File ffmpegLocalWindows = new File("bin/ffmpeg.exe");
//         File ffmpegLocalLinux = new File("bin/ffmpeg");

//         if (ffmpegLocalWindows.exists()) {
//             return ffmpegLocalWindows.getAbsolutePath();
//         } else if (ffmpegLocalLinux.exists()) {
//             return ffmpegLocalLinux.getAbsolutePath();
//         }

//         // Si no está en la carpeta local del proyecto, llama al comando global de las Variables de Entorno
//         return "ffmpeg";
//     }*/
// private String obtenerRutaFFmpeg() {
//     // 1. Prueba en la carpeta donde Java está ejecutando actualmente
//     File ffmpegLocal = new File("bin/ffmpeg.exe");
//     if (ffmpegLocal.exists()) {
//         return ffmpegLocal.getAbsolutePath();
//     }

//     // 2. Ruta fija en C:\ffmpeg\ffmpeg.exe (a prueba de cualquier cambio de folder en el IDE)
//     File ffmpegFijo = new File("C:/ffmpeg/ffmpeg.exe");
//     if (ffmpegFijo.exists()) {
//         System.out.println("--> Se encontró FFmpeg en la ruta fija: " + ffmpegFijo.getAbsolutePath());
//         return ffmpegFijo.getAbsolutePath();
    
//     }

//     System.err.println("No se encontró en: " + ffmpegLocal.getAbsolutePath());
//     return "ffmpeg";
// }
//     @PreDestroy
//     public void detenerTransmision() {
//         if (ffmpegProcess != null && ffmpegProcess.isAlive()) {
//             ffmpegProcess.destroy();
//             System.out.println("Proceso de FFmpeg detenido al cerrar Spring Boot.");
//         }
//     }
// }
