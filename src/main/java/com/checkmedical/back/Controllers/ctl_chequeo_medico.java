package com.checkmedical.back.Controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.checkmedical.back.Models.mdl_chequeo_medico;
import com.checkmedical.back.services.svc_chequeo_medico;

@Controller
@RestController
public class ctl_chequeo_medico {
    @Autowired
    svc_chequeo_medico service;
    
    @GetMapping("/getChequeoMedicos")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_chequeo_medico> getChequeoMedicos() {
        return service.getChequeoMedicos();
    }

    @GetMapping("/getChequeoMedicoById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_chequeo_medico getChequeoMedicoById(@PathVariable int id) {
        return service.getChequeoMedicoById(id);
    }

    @PostMapping("/saveChequeoMedico")
    @ResponseStatus(HttpStatus.CREATED)
    String ChequeoMedico(@RequestBody mdl_chequeo_medico chequeoMedico
    ) {
        String mensaje = "ER|Existe un error interno y no pudo registrarse.";

        if (
            chequeoMedico.getIdCita() != 0 &&
            !chequeoMedico.getFechaEmision().equals("") &&
            !chequeoMedico.getFechaVencimiento().equals("")
        ) 
        {
            mensaje = "ER|No se pudo registrar el chequeo medico.";

            chequeoMedico.setEstado(1);
            chequeoMedico.setIpRegistra(chequeoMedico.capturarIp());

            try {
                if (service.saveChequeoMedico(chequeoMedico)) {
                    mensaje = "OK|Se registró el chequeo médico con éxito.";
                }
            } catch (Exception e) {
                e.printStackTrace();
                mensaje = "ER|Error al guardar el chequeo nedico.";
            }
        }

        return mensaje;
    }

    @PostMapping(value = "/savePDFChequeoMedico/{idCita}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    String savePDFChequeoMedico(@RequestParam("archivoPDF") MultipartFile archivoPDF, @PathVariable String idCita) {
        String mensaje = "ER|Existe un error interno y no pudo registrarse.";
        mensaje = "ER|No se pudo registrar el archivo PDF.";

        try {
            mdl_chequeo_medico chequeo = new mdl_chequeo_medico();
        
            String fechaHoraOriginal = chequeo.capturaraFecha();
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraOriginal);
        
            // Crear el formateador con el patrón deseado
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm");
        
            // Formatear la fecha y hora
            String fechaHoraFormateada = fechaHora.format(formatter);
        
            // Guardar el archivo PDF en la carpeta "chequeosMedicos" en la raíz del proyecto
            String nombreArchivo = "chequeoMedico" + idCita + "-" + fechaHoraFormateada + ".pdf";
        
            // Obtener el directorio de trabajo actual
            String rutaBase = System.getProperty("user.dir");
        
            // Verificar si la carpeta "chequeosMedicos" existe en la raíz del proyecto
            File carpetaChequeosMedicos = new File(rutaBase+"/back", "chequeosMedicos");
            if (!carpetaChequeosMedicos.exists()) {
                carpetaChequeosMedicos.mkdirs(); // Crear la carpeta si no existe
            }
        
            // Construir la ruta completa del destino final en la carpeta "chequeosMedicos"
            String rutaDestinoFinal = carpetaChequeosMedicos.getAbsolutePath() + File.separator + nombreArchivo;
        
            archivoPDF.transferTo(new File(rutaDestinoFinal));
        
            mensaje = "OK|Se registro el PDF.|"+nombreArchivo;
        } catch (IOException e) {
            e.printStackTrace();
            mensaje = "ER|Error al guardar el archivo PDF.";
        }
        
        return mensaje;
    }

    @GetMapping("descargarChequeoMedico/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        String baseDirectory = System.getProperty("user.dir");
        String filePath = baseDirectory + File.separator + "back" + File.separator + "chequeosMedicos" + File.separator
                + filename;

                System.out.println(filePath);

        Path file = Paths.get(filePath);
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
