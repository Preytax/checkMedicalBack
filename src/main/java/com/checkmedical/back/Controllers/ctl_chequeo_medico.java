package com.checkmedical.back.Controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/getChequeoMedicoByIdPersona/{idPersona}")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_chequeo_medico> getByIdPersona(@PathVariable int idPersona) {
        return service.getByIdPersona(idPersona);
    }

    @PostMapping("/saveChequeoMedico")
    @ResponseStatus(HttpStatus.CREATED)
    String ChequeoMedico(@RequestBody mdl_chequeo_medico chequeoMedico
    ) {
        String mensaje = "ER|Existe un error interno y no pudo registrarse.";

        if (
            chequeoMedico.getIdPersona() != 0 &&
            chequeoMedico.getIdCita() != 0 &&
            !chequeoMedico.getFechaEmision().equals("") &&
            !chequeoMedico.getFechaVencimiento().equals("")
        ) 
        {
            mensaje = "ER|No se pudo registrar el chequeo medico.";

            chequeoMedico.setEstado(1);
            chequeoMedico.setIpRegistra(chequeoMedico.capturarIp());

            System.out.println(chequeoMedico.getIdPersona()); 
            System.out.println(chequeoMedico.getIdCita()); 
            System.out.println(chequeoMedico.getFechaEmision()); 
            System.out.println(chequeoMedico.getFechaVencimiento()); 

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

    // EXPORTAR EXCEL
    @GetMapping("/exportarCheuqeosMedicos")
    public ResponseEntity<byte[]> exportToExcel() {
        // Consulta la entidad utilizando JPA y obtén los datos que deseas exportar
        List<mdl_chequeo_medico> clinicas = service.getChequeoMedicos();

        try (Workbook workbook = new XSSFWorkbook()) {
            // Crea un libro de trabajo de Excel
            Sheet sheet = workbook.createSheet("Clinicas");// agregar fecha de descarga

            // Crea la fila de encabezado
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("ID CITA");
            headerRow.createCell(2).setCellValue("ID PERSONA");
            headerRow.createCell(3).setCellValue("ESTADO");
            headerRow.createCell(4).setCellValue("F. EMISION");
            headerRow.createCell(5).setCellValue("F. VENCIMIENTO");

            // Llena las filas con los datos de la entidad
            int rowNum = 1;
            for (mdl_chequeo_medico clinica : clinicas) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(clinica.getId());
                row.createCell(1).setCellValue(clinica.getIdCita());
                row.createCell(2).setCellValue(clinica.getIdPersona());
                row.createCell(3).setCellValue(clinica.getEstado());
                row.createCell(4).setCellValue(clinica.getFechaEmision());
                row.createCell(5).setCellValue(clinica.getFechaVencimiento());
            }

            // Estilo para la cabecera

            // Estilo para datos de edicion en la cabecera
            CellStyle styleheaderEditable = workbook.createCellStyle();

            byte[] styleCabeceraEditable = new byte[] { (byte) 13, (byte) 110, (byte) 253 };
            XSSFColor colorCabeceraEditable = new XSSFColor(styleCabeceraEditable, null);

            styleheaderEditable.setFillForegroundColor(colorCabeceraEditable);
            styleheaderEditable.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerRow.getCell(0).setCellStyle(styleheaderEditable);

            Font headerFontEditable = workbook.createFont();
            headerFontEditable.setBold(true);
            headerFontEditable.setColor(IndexedColors.WHITE.getIndex());

            styleheaderEditable.setFont(headerFontEditable);

            // Estilo para datos en la cabecera
            CellStyle headerStyle = workbook.createCellStyle();

            byte[] styleCabecera = new byte[] { (byte) 51, (byte) 102, (byte) 153 };
            XSSFColor colorCabecera = new XSSFColor(styleCabecera, null);

            headerStyle.setFillForegroundColor(colorCabecera);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            headerStyle.setFont(headerFont);

            // Aplicar estilo a la cabecera
            for (int i = 1; i < headerRow.getLastCellNum(); i++) {
                headerRow.getCell(i).setCellStyle(headerStyle);
            }

            // Configura el flujo de salida y el tipo de contenido del archivo
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                byte[] excelBytes = outputStream.toByteArray();

                // Configura la respuesta para descargar el archivo
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "attachment; filename=chequemosMedicos.xlsx");

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(excelBytes);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
