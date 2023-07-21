package com.checkmedical.back.Controllers;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.checkmedical.back.Models.mdl_cita;
import com.checkmedical.back.Models.mdl_clinica;
import com.checkmedical.back.Models.mdl_persona;
import com.checkmedical.back.services.svc_cita;
import com.checkmedical.back.services.svc_clinica;
import com.checkmedical.back.services.svc_persona;

@Controller
@RestController
public class ctl_cita {
    
    @Autowired
    svc_cita service;

    @Autowired
    svc_persona servicePersona;

    @Autowired
    svc_clinica serviceClinica;

    @GetMapping("/getRecordatorios/{estado}")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_cita> getRecordatorios(@PathVariable List<Integer> estado) {
        return service.getRecordatorios(estado);
    }

    @GetMapping("/getRecordatorioById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_cita getRecordatorio(@PathVariable int id) {
        return service.getRecordatorioById(id);
    }

    @GetMapping("/getRecordatoriosByIdPersonaAndEstado/{idPersona}/{estado}")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_cita> getRecordatoriosByIdPersonaAndEstado(@PathVariable int idPersona, @PathVariable int estado) {
        return service.getRecordatoriosByIdPersonaAndEstado(idPersona, estado);
    }

    @PostMapping("/saveRecordatorio")
    @ResponseStatus(HttpStatus.CREATED)
    String saveRecordatorio(@RequestBody mdl_cita recordatorio) {
        String mensaje = "ER|Existe un error interno y no pudo registrarse.";

        if (
            recordatorio.getIdClinica() != 0 &&
            recordatorio.getIdPersona() != 0
            ) 
        {
            mensaje = "ER|No se pudo registrar el recordatorio.";

            recordatorio.setUsuarioRegistra(recordatorio.getId());
            recordatorio.setEstado(1);
            recordatorio.setIpRegistra(recordatorio.capturarIp());

            if (service.saveRecordatorio(recordatorio)) {
                mensaje = "OK|Se registro el recordatorio con exito.";
            }
        }

        return mensaje;
    }

    @PutMapping("/eliminarRecordatorio")
    @ResponseStatus(HttpStatus.OK)
    String EliminarRecordatorio(@RequestBody mdl_cita recordatorio) {
        String mensaje = "ER|Existe un error interno y no pudo eliminar.";
        boolean confirmacion = false;
        if (recordatorio.getId() != 0) {
            mensaje = "ER|No se pudo eliminar la información.";

            confirmacion = service.eliminarRecordatorio(recordatorio.getId());

            if (confirmacion) {
                mensaje = "OK|Se elimino el recordatorio.";
            }
        }

        return mensaje;
    }

    @PutMapping("/confirmarRecordatorio")
    @ResponseStatus(HttpStatus.OK)
    String confirmarRecordatorio(@RequestBody mdl_cita recordatorio) {
        String mensaje = "ER|Existe un error interno y no pudo confirmar.";
        boolean confirmacion = false;
        if (recordatorio.getId() != 0) {
            mensaje = "ER|No se pudo confirmar la asistencia.";

            confirmacion = service.confirmarRecordatorio(recordatorio.getId());

            if (confirmacion) {
                mensaje = "OK|Se confirmo las asistencia.";
            }
        }

        return mensaje;
    }

    // EXPORTAR EXCEL
    @GetMapping("/exportarRecordatorios")
    public ResponseEntity<byte[]> exportToExcel() {
        // Consulta la entidad utilizando JPA y obtén los datos que deseas exportar
        List<mdl_cita> recordatorios = service.getRecordatorios();

        try (Workbook workbook = new XSSFWorkbook()) {
            // Crea un libro de trabajo de Excel
            Sheet sheet = workbook.createSheet("Recordatorios");// agregar fecha de descarga

            // Crea la fila de encabezado
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("F. CITA");
            headerRow.createCell(2).setCellValue("ID PERSONA");
            headerRow.createCell(3).setCellValue("ID CLINICA");
            headerRow.createCell(4).setCellValue("AMBIENTE");
            headerRow.createCell(5).setCellValue("COMENTARIO");
            headerRow.createCell(6).setCellValue("F. INICIO");
            headerRow.createCell(7).setCellValue("F. FINAL");

            // Llena las filas con los datos de la entidad
            int rowNum = 1;
            for (mdl_cita recordatorio : recordatorios) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(recordatorio.getId());
                row.createCell(1).setCellValue(recordatorio.getFechaCita());
                row.createCell(2).setCellValue(recordatorio.getIdPersona());
                row.createCell(3).setCellValue(recordatorio.getIdClinica());
                row.createCell(4).setCellValue(recordatorio.getAmbiente());
                row.createCell(5).setCellValue(recordatorio.getRecordatorio());
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
                headers.add("Content-Disposition", "attachment; filename=Recordatorios.xlsx");

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

    @PostMapping("/saveRecordatorioMasivo/{idAmbiente}/{idSede}")
    public ResponseEntity<Boolean> leerExcel(@RequestParam("file") MultipartFile file, @PathVariable int idAmbiente, @PathVariable  int idSede) {
        try {
            InputStream inputStream = file.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            Workbook workbook = WorkbookFactory.create(bufferedInputStream);
            Sheet sheet = workbook.getSheetAt(0); // Obtén la primera hoja del libro de trabajo
            int rowCount = sheet.getPhysicalNumberOfRows(); // Obtiene el número total de filas físicas en la hoja

            // Iterar sobre cada fila y verificar si contiene datos escritos
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row != null && !isEmptyRow(row)) {
                    mdl_cita cita = new mdl_cita();

                    if (row.getCell(0) != null) {
                        cita.setId((int) row.getCell(0).getNumericCellValue());

                        mdl_cita citaTemporal = new mdl_cita();
                        citaTemporal = service.getRecordatorioById((int) row.getCell(0).getNumericCellValue());
                        if(citaTemporal == cita){
                            return ResponseEntity.ok(false);
                        } else {
                            cita.setId((int) row.getCell(0).getNumericCellValue());
                        }
                    }

                    //Comprovacion de fecha de cita
                    Date fechaactual = new Date(System.currentTimeMillis());
                    String fechaCita = row.getCell(1).getLocalDateTimeCellValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                    Date fechaInicioDate = date.parse(fechaCita);

                    if(!fechaInicioDate.after(fechaactual)) {
                        return ResponseEntity.ok(false);
                    }

                    //Validar existencia del trabajador
                    mdl_persona persona = new mdl_persona();
                    mdl_persona personaTemporal = new mdl_persona();
                    int idPersona = (int) row.getCell(2).getNumericCellValue();

                    personaTemporal = servicePersona.getPersonaById(idPersona);

                    if(personaTemporal == persona){
                        return ResponseEntity.ok(false);
                    }

                    //Validar existencia del trabajador
                    mdl_clinica clinica = new mdl_clinica();
                    mdl_clinica clinicaTemporal = new mdl_clinica();
                    int idClinica = (int) row.getCell(2).getNumericCellValue();

                    clinicaTemporal = serviceClinica.getClinicaById(idClinica);

                    if(clinicaTemporal == clinica){
                        return ResponseEntity.ok(false);
                    }

                    cita.setFechaCita(fechaCita);
                    cita.setIdPersona(idPersona);
                    cita.setIdClinica((int) row.getCell(3).getNumericCellValue());
                    cita.setAmbiente(row.getCell(4).getStringCellValue());
                    cita.setRecordatorio(row.getCell(5).getStringCellValue());

                    //Campos de auditoria
                    cita.setUsuarioRegistra(cita.getId());
                    cita.setEstado(1);
                    cita.setIpRegistra(cita.capturarIp());
                    
                    // Registrar recordatorio
                    service.saveRecordatorio(cita);
                }
            }

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(false);
        }
    }

    // Método para verificar si una fila está vacía (sin datos escritos)
    private boolean isEmptyRow(Row row) {
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
