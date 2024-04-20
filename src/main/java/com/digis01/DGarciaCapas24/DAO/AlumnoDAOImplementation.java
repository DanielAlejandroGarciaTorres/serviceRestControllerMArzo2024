/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.digis01.DGarciaCapas24.DAO;

import com.digis01.DGarciaCapas24.ML.Alumno;
import com.digis01.DGarciaCapas24.ML.AlumnoDireccion;
import com.digis01.DGarciaCapas24.ML.Colonia;
import com.digis01.DGarciaCapas24.ML.Direccion;
import com.digis01.DGarciaCapas24.ML.Estado;
import com.digis01.DGarciaCapas24.ML.Municipio;
import com.digis01.DGarciaCapas24.ML.Pais;
import com.digis01.DGarciaCapas24.ML.Result;
import com.digis01.DGarciaCapas24.ML.Rol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ALIEN 34
 */
@Repository
public class AlumnoDAOImplementation implements AlumnoDAO {

    private JdbcTemplate jdbcTemplate; // de encarga de recupera y/o enviar datos a nuestra bd
    private EntityManager entityManager; // me permitetrabajar con JPA

    @Autowired //inyección 
    public AlumnoDAOImplementation(DataSource dataSource, EntityManager entityManager) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.entityManager = entityManager;
    }

    //    @Override
//    public Boolean Add(Alumno alumno) {
//        //Logica para ADD
//        //Query 
//        return true;
//    }
//    @Override
//    public Alumno GetById(int idAlumno) {
//        String query = "SELECT IdAlumno, Nombre, ApellidoPaterno, UserName, IdRol, FechaNacimiento FROM Alumno WHERE IdAlumno = " + idAlumno;
//        Alumno alumno = jdbcTemplate.queryForObject(query, new AlumnoRowMapper());
//        return alumno;
//    }
    @Override
    public Result GetAllSP() { // lo hagan con un sp getall
//        String query = "SELECT IdAlumno, Nombre FROM Alumno";
//        return jdbcTemplate.query(query, new AlumnoRowMapper());
        Result result = new Result();
        try {
            List<AlumnoDireccion> alumnosDirecciones = jdbcTemplate.execute("{CALL AlumnoGetAll(?)}", (CallableStatementCallback<List<AlumnoDireccion>>) callableStatement -> {
                callableStatement.registerOutParameter(1, Types.REF_CURSOR);
                callableStatement.execute();
                ResultSet rs = (ResultSet) callableStatement.getObject(1);
                List<AlumnoDireccion> alumnos = new ArrayList<>();
                AlumnoRowMapper alumnoRowMapper = new AlumnoRowMapper();
                while (rs.next()) {
                    alumnos.add(alumnoRowMapper.mapRow(rs, rs.getRow()));
                }
                return alumnos;
            });
            result.Object = alumnosDirecciones;
            result.Correct = true;
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.Ex = ex;
        }
        return result;
    }

    @Override
    public Result AddSP(AlumnoDireccion alumnoDireccion) {
        Result result = new Result();

        try {
            int rowsAffected = jdbcTemplate.execute("{CALL AlumnoAdd(?, ?, ?, ?, ? , ?, ?, ?, ?, ? )}", (CallableStatementCallback<Integer>) callableStatement -> {
                callableStatement.setString("pNombre", alumnoDireccion.Alumno.getNombre());
                callableStatement.setString("pApellidoPaterno", alumnoDireccion.Alumno.getApellidoPaterno());
                callableStatement.setString("pUserName", alumnoDireccion.Alumno.getUserName());
                callableStatement.setInt("pIdRol", alumnoDireccion.Alumno.Rol.getIdRol());
                java.sql.Date fechaNacimientoSql = new java.sql.Date(alumnoDireccion.Alumno.getFechaNacimiento().getTime());
                callableStatement.setDate("pFechaNacimiento", fechaNacimientoSql);
                callableStatement.setString("pCalle", alumnoDireccion.Direccion.getCalle());
                callableStatement.setString("pNumeroInterior", alumnoDireccion.Direccion.getNumeroInterior());
                callableStatement.setString("pNumeroExterior", alumnoDireccion.Direccion.getNumeroExterior());
                callableStatement.setInt("pIdColonia", alumnoDireccion.Direccion.Colonia.getIdColonia());
                callableStatement.registerOutParameter("IdRecuperado", Types.NUMERIC);

                callableStatement.execute(); // ejecuta mi query
                return callableStatement.getUpdateCount(); // 
            });

            if (rowsAffected != 0) {
                result.Correct = true;
            }

        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.Ex = ex;
        }
        return result;
    }

    @Override
    public Result UpdateSP(AlumnoDireccion alumnoDireccion) {
        Result result = new Result();

        try {
            int rowsAffected = jdbcTemplate.execute("{CALL AlumnoUpdate(?, ?, ?, ?, ? , ?, ?, ?, ?, ? )}", (CallableStatementCallback<Integer>) callableStatement -> {
                callableStatement.setInt("pIdAlumno", alumnoDireccion.Alumno.getIdAlumno());
                callableStatement.setString("pNombre", alumnoDireccion.Alumno.getNombre());
                callableStatement.setString("pApellidoPaterno", alumnoDireccion.Alumno.getApellidoPaterno());
                callableStatement.setString("pUserName", alumnoDireccion.Alumno.getUserName());
                callableStatement.setInt("pIdRol", alumnoDireccion.Alumno.Rol.getIdRol());
                java.sql.Date fechaNacimientoSql = new java.sql.Date(alumnoDireccion.Alumno.getFechaNacimiento().getTime());
                callableStatement.setDate("pFechaNacimiento", fechaNacimientoSql);
                callableStatement.setString("pCalle", alumnoDireccion.Direccion.getCalle());
                callableStatement.setString("pNumeroInterior", alumnoDireccion.Direccion.getNumeroInterior());
                callableStatement.setString("pNumeroExterior", alumnoDireccion.Direccion.getNumeroExterior());
                callableStatement.setInt("pIdColonia", alumnoDireccion.Direccion.Colonia.getIdColonia());

                callableStatement.execute(); // ejecuta mi query
                return callableStatement.getUpdateCount(); // 
            });

            if (rowsAffected != 0) {
                result.Correct = true;
            }

        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.Ex = ex;
        }
        return result;
    }

    @Override
    public Result GetByIdSP(int idAlumno) {
        Result result = new Result();
        try {
            AlumnoDireccion alumnoDireccion = jdbcTemplate.execute("{CALL AlumnoGetById(?,?)}", (CallableStatementCallback<AlumnoDireccion>) callableStatement -> {
                callableStatement.setInt("pIdAlumno", idAlumno);
                callableStatement.registerOutParameter("datosCursor", Types.REF_CURSOR);
                callableStatement.execute();
                ResultSet rs = (ResultSet) callableStatement.getObject("datosCursor");
                AlumnoRowMapper alumnoRowMapper = new AlumnoRowMapper();
                if (rs.next()) {
                    return alumnoRowMapper.mapRow(rs, rs.getRow());
                } else {
                    return null;
                }
            });
            result.Object = alumnoDireccion;
            result.Correct = true;
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.Ex = ex;
        }
        return result;
    }

    @Override
    public Result GetAllJPA(com.digis01.DGarciaCapas24.JPA.Alumno alumnoBusqueda) {
        //JPQL
        Result result = new Result();
        
        TypedQuery<com.digis01.DGarciaCapas24.JPA.Alumno> query = entityManager.createQuery("FROM Alumno WHERE "
                + "UPPER(Nombre) LIKE UPPER(:NombreAlumno)", com.digis01.DGarciaCapas24.JPA.Alumno.class);
        
        query.setParameter("NombreAlumno", "%" + alumnoBusqueda.getNombre() + "%");
        
        List<com.digis01.DGarciaCapas24.JPA.Alumno> alumnos = query.getResultList();
        List<com.digis01.DGarciaCapas24.JPA.AlumnoDireccion> alumnosDireccion = new ArrayList<>();
        
        
        for (com.digis01.DGarciaCapas24.JPA.Alumno alumno : alumnos) {
            
            com.digis01.DGarciaCapas24.JPA.AlumnoDireccion alumnoDireccion = new com.digis01.DGarciaCapas24.JPA.AlumnoDireccion();
            alumnoDireccion.alumno = alumno;
            
            
            TypedQuery<com.digis01.DGarciaCapas24.JPA.Direccion> queryDireccion = entityManager.createQuery("FROM Direccion WHERE Alumno.IdAlumno =: idAlumno", com.digis01.DGarciaCapas24.JPA.Direccion.class);
            queryDireccion.setParameter("idAlumno", alumno.getIdAlumno());

            try {
                
                com.digis01.DGarciaCapas24.JPA.Direccion direccion = queryDireccion.getSingleResult();
                alumnoDireccion.direccion = direccion;
                
            } catch (Exception ex) {
                continue;
            } finally {
                alumnosDireccion.add(alumnoDireccion);
            }

        }
        result.Correct = true;
        result.Object = alumnosDireccion;

        return result;
    }

    @Override
    @Transactional
    public Result AddJPA(AlumnoDireccion alumnoDireccion) {
        Result result = new Result();

        com.digis01.DGarciaCapas24.JPA.Alumno alumno = new com.digis01.DGarciaCapas24.JPA.Alumno();
        alumno.setNombre(alumnoDireccion.Alumno.getNombre());
        alumno.setApellidoPaterno(alumnoDireccion.Alumno.getApellidoPaterno());
        alumno.setUserName(alumnoDireccion.Alumno.getUserName());
        alumno.Rol = new com.digis01.DGarciaCapas24.JPA.Rol();
        alumno.Rol.setIdRol(alumnoDireccion.Alumno.Rol.getIdRol());
        alumno.setFechaNacimiento(alumnoDireccion.Alumno.getFechaNacimiento());
        alumno.setImagen(alumnoDireccion.Alumno.getImagen());
//        alumno.setImagen(null);

        entityManager.persist(alumno);

        com.digis01.DGarciaCapas24.JPA.Direccion direccion = new com.digis01.DGarciaCapas24.JPA.Direccion();
        direccion.setCalle(alumnoDireccion.Direccion.getCalle());
        direccion.setNumeroInterior(alumnoDireccion.Direccion.getNumeroInterior());
        direccion.setNumeroExterior(alumnoDireccion.Direccion.getNumeroExterior());
        direccion.Colonia = new com.digis01.DGarciaCapas24.JPA.Colonia();
        direccion.Colonia.setIdColonia(alumnoDireccion.Direccion.Colonia.getIdColonia());
        direccion.Alumno = new com.digis01.DGarciaCapas24.JPA.Alumno();
        direccion.Alumno.setIdAlumno(alumno.getIdAlumno());

        entityManager.persist(direccion);

        return result;
    }

    @Override
    @Transactional
    public Result UpdateJPA(AlumnoDireccion alumnoDireccion) {
        Result result = new Result();

        com.digis01.DGarciaCapas24.JPA.Alumno alumno = new com.digis01.DGarciaCapas24.JPA.Alumno();
        alumno.setIdAlumno(alumnoDireccion.Alumno.getIdAlumno());
        alumno.setNombre(alumnoDireccion.Alumno.getNombre());
        alumno.setApellidoPaterno(alumnoDireccion.Alumno.getApellidoPaterno());
        alumno.setUserName(alumnoDireccion.Alumno.getUserName());
        alumno.Rol = new com.digis01.DGarciaCapas24.JPA.Rol();
        alumno.Rol.setIdRol(alumnoDireccion.Alumno.Rol.getIdRol());
        alumno.setFechaNacimiento(alumnoDireccion.Alumno.getFechaNacimiento());

        entityManager.merge(alumno); // actualiza los datos

        com.digis01.DGarciaCapas24.JPA.Direccion direccion = new com.digis01.DGarciaCapas24.JPA.Direccion();
        direccion.setCalle(alumnoDireccion.Direccion.getCalle());
        direccion.setNumeroInterior(alumnoDireccion.Direccion.getNumeroInterior());
        direccion.setNumeroExterior(alumnoDireccion.Direccion.getNumeroExterior());
        direccion.Colonia = new com.digis01.DGarciaCapas24.JPA.Colonia();
        direccion.Colonia.setIdColonia(alumnoDireccion.Direccion.Colonia.getIdColonia());
        direccion.Alumno = new com.digis01.DGarciaCapas24.JPA.Alumno();
        direccion.Alumno.setIdAlumno(alumno.getIdAlumno());

        entityManager.persist(direccion);

        return result;
    }

    @Override
    public Result GetByIdJPA(int idAlumno) {
        Result result = new Result();
        TypedQuery<com.digis01.DGarciaCapas24.JPA.Alumno> query = entityManager.createQuery("FROM Alumno WHERE IdAlumno =: idAlumno", com.digis01.DGarciaCapas24.JPA.Alumno.class);
        query.setParameter("idAlumno", idAlumno);
        com.digis01.DGarciaCapas24.JPA.Alumno alumnoJPA = query.getSingleResult();
        
        AlumnoDireccion alumnoDireccion = new AlumnoDireccion();  // alumno dirección es de ML 
        alumnoDireccion.Alumno = new Alumno();
        alumnoDireccion.Alumno.setIdAlumno(alumnoJPA.getIdAlumno());
        alumnoDireccion.Alumno.setNombre(alumnoJPA.getNombre());
        alumnoDireccion.Alumno.setApellidoPaterno(alumnoJPA.getApellidoPaterno());
        alumnoDireccion.Alumno.setUserName(alumnoJPA.getUserName());
        alumnoDireccion.Alumno.setFechaNacimiento(alumnoJPA.getFechaNacimiento());
        alumnoDireccion.Alumno.Rol = new Rol();
        alumnoDireccion.Alumno.Rol.setIdRol(alumnoJPA.Rol.getIdRol());

        TypedQuery<com.digis01.DGarciaCapas24.JPA.Direccion> queryDireccion = entityManager.createQuery("FROM Direccion WHERE Alumno.IdAlumno =: idAlumno", com.digis01.DGarciaCapas24.JPA.Direccion.class);
        queryDireccion.setParameter("idAlumno",idAlumno);

        try {
            com.digis01.DGarciaCapas24.JPA.Direccion direccionJPA = queryDireccion.getSingleResult();
            alumnoDireccion.Direccion = new Direccion();
            alumnoDireccion.Direccion.setIdDIreccion(direccionJPA.getIdDireccion());
            alumnoDireccion.Direccion.setCalle(direccionJPA.getCalle());
            alumnoDireccion.Direccion.setNumeroInterior(direccionJPA.getNumeroInterior());
            alumnoDireccion.Direccion.setNumeroExterior(direccionJPA.getNumeroExterior());
            alumnoDireccion.Direccion.Colonia = new Colonia();
            alumnoDireccion.Direccion.Colonia.setIdColonia(direccionJPA.Colonia.getIdColonia());
            alumnoDireccion.Direccion.Colonia.setNombre(direccionJPA.Colonia.getNombre());
            alumnoDireccion.Direccion.Colonia.Municipio = new Municipio();
            alumnoDireccion.Direccion.Colonia.Municipio.setIdMunicipio(direccionJPA.Colonia.Municipio.getIdMunicipio());
            alumnoDireccion.Direccion.Colonia.Municipio.setNombre(direccionJPA.Colonia.Municipio.getNombre());
            alumnoDireccion.Direccion.Colonia.Municipio.Estado = new Estado();
            alumnoDireccion.Direccion.Colonia.Municipio.Estado.setIdEstado(direccionJPA.Colonia.Municipio.Estado.getIdEstado());
            alumnoDireccion.Direccion.Colonia.Municipio.Estado.setNombre(direccionJPA.Colonia.Municipio.Estado.getNombre());
            alumnoDireccion.Direccion.Colonia.Municipio.Estado.Pais = new Pais();
            alumnoDireccion.Direccion.Colonia.Municipio.Estado.Pais.setIdPais(direccionJPA.Colonia.Municipio.Estado.Pais.getIdPais());
            alumnoDireccion.Direccion.Colonia.Municipio.Estado.Pais.setNombre(direccionJPA.Colonia.Municipio.Estado.Pais.getNombre());
        } catch (Exception ex) {
            
        
        }
        
        result.Object = alumnoDireccion;
        
        return result;
    }

    @Override
    public Result GetByUserName(String UserName, String Password) { // Usuario usuario 
        Result result = new Result();
        
        //TypedQuery -- From Usuario Where UserName =: UserNameBusqueda
        // usuarioJPA -- > singleResult {id, nombre, apellidoPaterno, ...}
        
        
        // SI UsuarioJPA.UserName = UserName
            // SI UduarioJPA.Password = Password
                // result.Correct = true
            // SINO
                //  result.Correct = false
        //SINO
            // result.Correct = false
            
        return result;
    }
}
