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
@Table(name= "ckm_clinicas")
@AllArgsConstructor
@NoArgsConstructor
public class mdl_clinica {
    
    @Id
    @Column(name="ID")
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="CLINICA")
    @Getter @Setter
    private String clinica;

    @Column(name="DIRECCION")
    @Getter @Setter
    private String direccion;

    @Column(name="ESTADO")
    @Getter @Setter
    private int estado;

    @Column(name="USUARIO_REGISTRA")
    @Getter @Setter
    private int usuarioRegistra;

    @Column(name="USUARIO_MODIFICA")
    @Getter @Setter
    private Integer uduarioModifica;

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

    public String capturarIp() {
        String miVariable = "";
        try {
            InetAddress ip = InetAddress.getLocalHost();
            miVariable = ip.getHostAddress();
        } catch (Exception e) {
            miVariable = "0000000000";
        }
        return miVariable;
    }    

    public String capturaraFecha(){
        LocalDateTime currentDate = LocalDateTime.now();
        return currentDate.toString();
    }
}
