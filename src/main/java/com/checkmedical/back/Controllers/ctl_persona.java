package com.checkmedical.back.Controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.checkmedical.back.Models.mdl_persona;
import com.checkmedical.back.services.svc_persona;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.ByteArrayOutputStream;

@Controller
@RestController
public class ctl_persona {

    @Autowired
    svc_persona service;

    @GetMapping("/getPersonas/{id_perfil}/{estado}")
    @ResponseStatus(HttpStatus.CREATED)
    List<mdl_persona> getPersonas(@PathVariable int id_perfil, @PathVariable List<Integer> estado) {

        List<mdl_persona> listPersonas = null;
        listPersonas = service.getPersonasByIdPerfilAndEstadoIn(id_perfil, estado);

        return listPersonas;
    }

    @GetMapping("/singIn/{correo}/{password}")
    @ResponseStatus(HttpStatus.OK)
    List<mdl_persona> singIn(@PathVariable String correo, @PathVariable String password) {
        return service.singIn(correo, password);
    }

    @GetMapping("/getPersonaById/{id}")
    @ResponseStatus(HttpStatus.OK)
    mdl_persona getPersona(@PathVariable int id) {
        return service.getPersonaById(id);
    }

    @PostMapping("/savePersona")
    @ResponseStatus(HttpStatus.CREATED)
    String savePersona(@RequestBody mdl_persona persona) {
        String mensaje = "ER|Existe un error interno y no pudo registrarse.";

        if (!persona.getNombres().equals("") && !persona.getNombres().isEmpty() &&
                !persona.getApellidoPaterno().equals("") && !persona.getApellidoPaterno().isEmpty() &&
                !persona.getApellidoMaterno().equals("") && !persona.getApellidoMaterno().isEmpty() &&
                persona.getTipoDocumento() != 0 &&
                !persona.getNroDocumento().equals("") && !persona.getNroDocumento().isEmpty() &&
                !persona.getFechaNacimiento().equals("") && !persona.getFechaNacimiento().isEmpty() &&
                !persona.getCorreo().equals("") && !persona.getCorreo().isEmpty() &&
                !persona.getPassword().equals("") && !persona.getPassword().isEmpty()) {
            mensaje = "ER|No se pudo registrar a la persona.";

            /*
             * if(service.confirmarCorreo(persona.getCorreo()) == false){
             * return "ER|El correo ya esta registrado.";
             * }
             * else if(service.confirmarNroDocumento(persona.getNroDocumento()) == false){
             * return "ER|El Nro de docuemento ya esta registrado.";
             * }
             */

            persona.setUsuarioRegistra(persona.getId());
            persona.setEstado(1);
            persona.setIpRegistra(persona.capturarIp());

            if (service.savePersona(persona)) {
                mensaje = "OK|Se registro a la persona con exito.";
            }
        }

        return mensaje;
    }

    @PutMapping("/actualizarEstadoPersona")
    @ResponseStatus(HttpStatus.OK)
    String actualizarEstadoPersona(@RequestBody mdl_persona persona) {
        String mensaje = "ER|Existe un error interno y no pudo actualizar.";
        boolean confirmacion = false;
        if (persona.getId() != 0) {
            mensaje = "ER|No se pudo actualizar la información.";

            if (persona.getEstado() == 1) {
                confirmacion = service.deshabilitarPersona(persona.getId());
            } else if (persona.getEstado() == 0) {
                confirmacion = service.habilitarPersona(persona.getId());
            } else if (persona.getEstado() == 2) {
                confirmacion = service.EliminarPersona(persona.getId());
            }

            if (confirmacion) {
                mensaje = persona.getEstado() == 2 ? "OK|Se elimino al usuario." : "OK|Se actualizó el estado.";
            }
        }

        return mensaje;
    }

    // EXPORTAR EXCEL
    @GetMapping("/exportarPersonas/{idPerfil}/{estados}")
    public ResponseEntity<byte[]> exportToExcel(@PathVariable int idPerfil, @PathVariable List<Integer> estados) {
        // Consulta la entidad utilizando JPA y obtén los datos que deseas exportar
        List<mdl_persona> personas = service.getPersonasByIdPerfilAndEstadoIn(idPerfil, estados);

        try (Workbook workbook = new XSSFWorkbook()) {
            // Crea un libro de trabajo de Excel
            Sheet sheet = workbook.createSheet("Operadores");// agregar fecha de descarga

            // Crea la fila de encabezado
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("CORREO");
            headerRow.createCell(2).setCellValue("NOMBRES");
            headerRow.createCell(3).setCellValue("APELLIDO P.");
            headerRow.createCell(4).setCellValue("APELLIDO M.");
            headerRow.createCell(5).setCellValue("T. DOCUMENTO");
            headerRow.createCell(6).setCellValue("N. DOCUMENTO");
            headerRow.createCell(7).setCellValue("DIRECCIÓN");
            headerRow.createCell(8).setCellValue("F. NACIMIENTO");
            headerRow.createCell(9).setCellValue("ID ESTADO");

            // Llena las filas con los datos de la entidad
            int rowNum = 1;
            for (mdl_persona persona : personas) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(persona.getId());
                row.createCell(1).setCellValue(persona.getCorreo());
                row.createCell(2).setCellValue(persona.getNombres());
                row.createCell(3).setCellValue(persona.getApellidoPaterno());
                row.createCell(4).setCellValue(persona.getApellidoMaterno());
                row.createCell(5).setCellValue(persona.getTipoDocumento());
                row.createCell(6).setCellValue(persona.getNroDocumento());
                row.createCell(7).setCellValue(persona.getDireccion());
                row.createCell(8).setCellValue(persona.getFechaNacimiento());
                row.createCell(9).setCellValue(persona.getEstado());
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
                headers.add("Content-Disposition", "attachment; filename=Operadores.xlsx");

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

    @PostMapping("/savePersonaMasivo/{idAmbiente}/{idSede}")
    public ResponseEntity<Boolean> leerExcel(@RequestParam("file") MultipartFile file, @PathVariable int idAmbiente,
            @PathVariable int idSede) {
        try {
            InputStream inputStream = file.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            Workbook workbook = WorkbookFactory.create(bufferedInputStream);
            Sheet sheet = workbook.getSheetAt(0); // Obtén la primera hoja del libro de trabajo
            int rowCount = sheet.getPhysicalNumberOfRows(); // Obtiene el número total de filas físicas en la hoja

            // Iterar sobre cada fila y verificar si contiene datos escritos
            for (int i = 1; i < rowCount+1; i++) {
                Row row = sheet.getRow(i);
                if (row != null && !isEmptyRow(row)) {
                    mdl_persona persona = new mdl_persona();

                    if (row.getCell(0) != null) {
                        persona.setId((int) row.getCell(0).getNumericCellValue());
                    }

                    persona.setCorreo(row.getCell(1).getStringCellValue());
                    persona.setNombres(row.getCell(2).getStringCellValue());
                    persona.setApellidoPaterno(row.getCell(3).getStringCellValue());
                    persona.setApellidoMaterno(row.getCell(4).getStringCellValue());
                    persona.setTipoDocumento((int) row.getCell(5).getNumericCellValue());
                    persona.setNroDocumento(String.valueOf((int) row.getCell(6).getNumericCellValue()));
                    persona.setDireccion(row.getCell(7).getStringCellValue());
                    persona.setFechaNacimiento(
                            String.valueOf(row.getCell(8).getLocalDateTimeCellValue().toLocalDate()));

                    if (row.getCell(9) != null) {
                        persona.setPassword(DigestUtils.md5Hex(row.getCell(9).getStringCellValue()));
                    }

                    persona.setEstado((int) row.getCell(10).getNumericCellValue());
                    persona.setIdPerfil((int) row.getCell(11).getNumericCellValue());

                    persona.setIdAmbiente(idAmbiente);
                    persona.setIdSede(idSede);

                    // Campos de auditoria
                    persona.setUsuarioRegistra(persona.getId());
                    persona.setIpRegistra(persona.capturarIp());

                    // Registrar persona
                    service.savePersona(persona);
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