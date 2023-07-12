package com.checkmedical.back.Controllers;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
