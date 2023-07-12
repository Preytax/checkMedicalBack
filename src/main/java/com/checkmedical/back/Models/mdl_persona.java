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
@Table(name= "ckm_personas")
@AllArgsConstructor
@NoArgsConstructor
public class mdl_persona {
    
    @Id
    @Column(name="ID")
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter @Setter
    @Column(name = "ID_SEDE")
    private int idSede;
    
    @Getter @Setter
    @Column(name = "ID_AMBIENTE")
    private int idAmbiente;
    
    @Getter @Setter
    @Column(name = "CORREO")
    private String correo;

    @Getter @Setter
    @Column(name = "PASSWORD")
    private String password;

    @Getter @Setter
    @Column(name = "ID_DOCUMENTO")
    private int tipoDocumento;

    @Getter @Setter
    @Column(name = "NRO_DOCUMENTO")
    private String nroDocumento;

    @Getter @Setter
    @Column(name = "DIRECCION")
    private String direccion;

    @Getter @Setter
    @Column(name = "ID_CARGO")
    private int idCargo;

    @Getter @Setter
    @Column(name = "ID_PERFIL")
    private int idPerfil;

    @Getter @Setter
    @Column(name = "ESTADO")
    private int estado;

    @Getter @Setter
    @Column(name = "NOMBRES")
    private String nombres;

    @Getter @Setter
    @Column(name = "APELLIDO_PATERNO")
    private String apellidoPaterno;

    @Getter @Setter
    @Column(name = "APELLIDO_MATERNO")
    private String apellidoMaterno;

    @Getter @Setter
    @Column(name = "FECHA_NACIMIENTO")
    private String fechaNacimiento;

    @Getter @Setter
    @Column(name = "USUARIO_REGISTRA")
    private int usuarioRegistra;

    @Getter @Setter
    @Column(name = "USUARIO_MODIFICA")
    private Integer usuarioModifica;

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
