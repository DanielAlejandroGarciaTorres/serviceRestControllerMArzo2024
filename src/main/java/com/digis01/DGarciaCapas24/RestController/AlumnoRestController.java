/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.digis01.DGarciaCapas24.RestController;

import com.digis01.DGarciaCapas24.DAO.AlumnoDAOImplementation;
import com.digis01.DGarciaCapas24.JPA.Alumno;
import com.digis01.DGarciaCapas24.ML.Result;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ALIEN 34
 */
@RestController
@RequestMapping("/alumnoApi")
public class AlumnoRestController {
    
    
//    {
//     "nombre" : ,
//     "apellidoPaterno" :,
//     "userName" : ,
//    
//    }
    
    @Autowired
    private AlumnoDAOImplementation alumnoDAOImplementation;
    
        @GetMapping
        public List<Alumno> GetAll( ){

            com.digis01.DGarciaCapas24.JPA.Alumno alumno = new com.digis01.DGarciaCapas24.JPA.Alumno("", "");


            List<Alumno> alumnos = (List<Alumno>) alumnoDAOImplementation.GetAllJPA(alumno).Object;

            return alumnos;
        }
}
