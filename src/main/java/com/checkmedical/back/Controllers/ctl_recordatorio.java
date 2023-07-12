package com.checkmedical.back.Controllers;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.checkmedical.back.Models.mdl_recordatorio;
import com.checkmedical.back.services.svc_recordatorio;

@Controller
@RestController
public class ctl_recordatorio {
    
    @Autowired
    svc_recordatorio service;

    @GetMapping("/getRecordatorios")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_recordatorio> getRecordatorios() {
        return service.getRecordatorios();
    }

    @GetMapping("/getRecordatorioById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_recordatorio getRecordatorio(@PathVariable int id) {
        return service.getRecordatorioById(id);
    }

    @GetMapping("/getRecordatorioByIdPersona/{idPersona}")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_recordatorio> getRecordatorioByIdPersona(@PathVariable int idPersona) {
        return service.getRecordatoriosByIdPersona(idPersona);
    }

    @PostMapping("/saveRecordatorio")
    @ResponseStatus(HttpStatus.CREATED)
    String saveRecordatorio(@RequestBody mdl_recordatorio recordatorio) {
        String mensaje = "ER|Existe un error interno y no pudo registrarse.";

        if (
            recordatorio.getIdActividad() != 0 &&
            !recordatorio.getFechaInicio().equals("") &&
            !recordatorio.getFechaFin().equals("") &&
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

    // EXPORTAR EXCEL
    @GetMapping("/exportarRecordatorios")
    public ResponseEntity<byte[]> exportToExcel() {
        // Consulta la entidad utilizando JPA y obtén los datos que deseas exportar
        List<mdl_recordatorio> recordatorios = service.getRecordatorios();

        try (Workbook workbook = new XSSFWorkbook()) {
            // Crea un libro de trabajo de Excel
            Sheet sheet = workbook.createSheet("Recordatorios");// agregar fecha de descarga

            // Crea la fila de encabezado
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("RECORDATORIO");
            headerRow.createCell(2).setCellValue("ID PERSONA");
            headerRow.createCell(3).setCellValue("F. INICIO");
            headerRow.createCell(4).setCellValue("F. FINAL");

            // Llena las filas con los datos de la entidad
            int rowNum = 1;
            for (mdl_recordatorio recordatorio : recordatorios) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(recordatorio.getId());
                row.createCell(1).setCellValue(recordatorio.getRecordatorio());
                row.createCell(2).setCellValue(recordatorio.getIdPersona());
                row.createCell(3).setCellValue(recordatorio.getFechaInicio());
                row.createCell(4).setCellValue(recordatorio.getFechaFin());
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
                    mdl_recordatorio recordatorio = new mdl_recordatorio();

                    if (row.getCell(0) != null) {
                        recordatorio.setId((int) row.getCell(0).getNumericCellValue());
                    }

                    recordatorio.setRecordatorio(row.getCell(1).getStringCellValue());
                    recordatorio.setIdPersona((int) row.getCell(2).getNumericCellValue());
                    recordatorio.setFechaInicio(String.valueOf(row.getCell(3).getLocalDateTimeCellValue().toLocalDate()));
                    recordatorio.setFechaFin(String.valueOf(row.getCell(4).getLocalDateTimeCellValue().toLocalDate()));

                    //Campos de auditoria
                    recordatorio.setUsuarioRegistra(recordatorio.getId());
                    recordatorio.setEstado(1);
                    recordatorio.setIpRegistra(recordatorio.capturarIp());

                    // Asigna otros atributos de la entidad según los datos de las celdas
                    System.out.println("esta es la recordatorio: "+recordatorio.getId());
                    System.out.println("esta es la recordatorio: "+recordatorio.getRecordatorio());
                    System.out.println("esta es la recordatorio: "+recordatorio.getIdPersona());
                    System.out.println("esta es la recordatorio: "+recordatorio.getFechaInicio());
                    System.out.println("esta es la recordatorio: "+recordatorio.getFechaFin());
                    

                    // Registrar recordatorio
                    service.saveRecordatorio(recordatorio);
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
