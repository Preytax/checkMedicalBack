package com.checkmedical.back.Controllers;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.checkmedical.back.Models.mdl_clinica;
import com.checkmedical.back.services.svc_clinica;

@Controller
@RestController
public class ctl_clinica {
    
    @Autowired
    svc_clinica service;

    @GetMapping("/getClinicas/{estado}")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_clinica> getClinicas(@PathVariable List<Integer> estado) {
        return service.getClinicasByEstadoIn(estado);
    }

    @GetMapping("/getClinicaById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_clinica getClinicaById(@PathVariable int id) {
        return service.getClinicaById(id);
    }

    @PutMapping("/actualizarEstadoClinica")
    @ResponseStatus(HttpStatus.OK)
    String actualizarEstadoClinica(@RequestBody mdl_clinica clinica) {
        String mensaje = "ER|Existe un error interno y no pudo actualizar.";
        boolean confirmacion = false;
        if (clinica.getId() != 0) {
            mensaje = "ER|No se pudo actualizar la información.";

            if (clinica.getEstado() == 1) {
                confirmacion = service.deshabilitarClinica(clinica.getId());
            } else if (clinica.getEstado() == 0) {
                confirmacion = service.habilitarClinica(clinica.getId());
            } else if (clinica.getEstado() == 2) {
                confirmacion = service.eliminarClinica(clinica.getId());
            }

            if (confirmacion) {
                mensaje = clinica.getEstado() == 2 ? "OK|Se elimino la Clinica." : "OK|Se actualizó el estado.";
            }
        }

        return mensaje;
    }

    @PostMapping("/saveClinica")
    @ResponseStatus(HttpStatus.CREATED)
    String savePersona(@RequestBody mdl_clinica clinica) {
        String mensaje = "ER|Existe un error interno y no pudo registrarse.";

        if (
            !clinica.getClinica().equals("") && !clinica.getClinica().isEmpty() &&
            !clinica.getDireccion().equals("") && !clinica.getDireccion().isEmpty()
            ) 
        {
            mensaje = "ER|No se pudo registrar la clinica.";

            /*
             * if(service.confirmarCorreo(persona.getCorreo()) == false){
             * return "ER|El correo ya esta registrado.";
             * }
             * else if(service.confirmarNroDocumento(persona.getNroDocumento()) == false){
             * return "ER|El Nro de docuemento ya esta registrado.";
             * }
             */

            clinica.setUsuarioRegistra(clinica.getId());
            clinica.setEstado(1);
            clinica.setIpRegistra(clinica.capturarIp());

            if (service.saveClinica(clinica)) {
                mensaje = "OK|Se registro la clinica con exito.";
            }
        }

        return mensaje;
    }

    // EXPORTAR EXCEL
    @GetMapping("/exportarClinicas")
    public ResponseEntity<byte[]> exportToExcel() {
        // Consulta la entidad utilizando JPA y obtén los datos que deseas exportar
        List<mdl_clinica> clinicas = service.getClinicas();

        try (Workbook workbook = new XSSFWorkbook()) {
            // Crea un libro de trabajo de Excel
            Sheet sheet = workbook.createSheet("Clinicas");// agregar fecha de descarga

            // Crea la fila de encabezado
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("CLINICA");
            headerRow.createCell(2).setCellValue("DIRECCION");
            headerRow.createCell(3).setCellValue("ESTADO");

            // Llena las filas con los datos de la entidad
            int rowNum = 1;
            for (mdl_clinica clinica : clinicas) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(clinica.getId());
                row.createCell(1).setCellValue(clinica.getClinica());
                row.createCell(2).setCellValue(clinica.getDireccion());
                row.createCell(3).setCellValue(clinica.getEstado());
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
                headers.add("Content-Disposition", "attachment; filename=Clinicas.xlsx");

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

    @PostMapping("/saveClinicaMasivo/{idAmbiente}/{idSede}")
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
                    mdl_clinica clinica = new mdl_clinica();

                    if (row.getCell(0) != null) {
                        clinica.setId((int) row.getCell(0).getNumericCellValue());
                    }

                    clinica.setClinica(row.getCell(1).getStringCellValue());
                    clinica.setDireccion(row.getCell(2).getStringCellValue());
                    clinica.setEstado((int) row.getCell(3).getNumericCellValue());

                    //Campos de auditoria
                    clinica.setUsuarioRegistra(clinica.getId());
                    clinica.setIpRegistra(clinica.capturarIp());
                    
                    // Registrar clinica
                    service.saveClinica(clinica);
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
