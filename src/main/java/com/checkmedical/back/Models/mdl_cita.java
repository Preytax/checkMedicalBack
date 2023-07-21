package com.checkmedical.back.Models;

import java.net.InetAddress;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "ckm_recordatorios")
@AllArgsConstructor
@NoArgsConstructor
public class mdl_cita {
    
    @Id
    @Column(name="ID")
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter @Setter
    @Column(name = "RECORDATORIO")
    private String recordatorio;

    @Getter @Setter
    @Column(name = "ID_PERSONA")
    private int idPersona;

    @Getter @Setter
    @Column(name = "ID_CLINICA")
    private int idClinica;

    @Getter @Setter
    @Column(name = "AMBIENTE")
    private String ambiente;

    @Getter @Setter
    @Column(name = "FECHA_CITA")
    private String fechaCita;

    @Getter @Setter
    @Column(name = "ESTADO")
    private int estado;

    @Getter @Setter
    @Column(name = "USUARIO_REGISTRA")
    private int usuarioRegistra;

    @Getter @Setter
    @Column(name = "USUARIO_MODIFICA")
    private int usuarioModifica;

    @Getter @Setter
    @Column(name = "FECHA_REGISTRA")
    private String fechaRegistro;

    @Getter @Setter
    @Column(name = "FECHA_MODIFICA")
    private String fechaModifica;
    
    @Getter @Setter
    @Column(name = "IP_REGISTRA")
    private String ipRegistra;

    @Getter @Setter
    @Column(name = "IP_MODIFICA")
    private String ipModifica;


    //Metodos
    public String capturarIp() {
        String miIp = "";
        try {
            InetAddress ip = InetAddress.getLocalHost();
            miIp = ip.getHostAddress();
        } catch (Exception e) {
            miIp = "0000000000";
        }
        return miIp;
    }    

    public String capturaraFecha(){
        LocalDateTime currentDate = LocalDateTime.now();
        return currentDate.toString();
    }
}
