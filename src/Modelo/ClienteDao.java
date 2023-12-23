/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author lourd
 */
public class ClienteDao {
    Conexion cn = new Conexion(); //stablecer la conexión con la base de datos.
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    public boolean RegistrarCliente(Cliente cl){ //Cliente es el nombre de la clase y cl es el nombre de la variable.
        String sql = "INSERT INTO clientes (dni,nombre,telefono,direccion,razon) VALUES (?,?,?,?,?)";
        try{
            con = cn.getConnection();
            ps = con.prepareStatement(sql); //utilizando la conexión con y la consulta SQL sql
            ps.setInt(1, cl.getDni()); //toma el DNI del objeto Cliente (cl) y lo asigna al primer parámetro de la consulta
            ps.setString(2, cl.getNombre ());
            ps.setInt(3, cl.getTelefono());
            ps.setString(4, cl.getDireccion());
            ps.setString(5, cl.getRazon());
            ps.execute(); //se ejecuta la consulta utilizando el objeto ps valores proporcionados en la base de datos.
            return true;
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }finally{ //se ejecutará siempre, sin importar si hubo o no excepciones
            try{
                con.close(); //intenta cerrar la conexión a la base de datos utilizando el método close() en la variable con
            }catch (SQLException e){
                System.out.println(e.toString ()); // La descripción de la excepción se imprime en la consola.
            }
            
        }
    }
    
    public List ListarCliente(){
        
       List<Cliente> ListarCl = new ArrayList(); //devuelve una lista de objetos de tipo Cliente.
       String sql = "SELECT * FROM clientes";
       try{;
           con = cn.getConnection();
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery();
           while (rs.next ()){ // recorre cada fila en el ResultSet.
               Cliente cl = new Cliente ();
               
               //valor de la columna "id" de la fila actual del ResultSet y se asigna al atributo id del objeto cl
               cl.setId(rs.getInt("id")); 
               cl.setDni(rs.getInt ("dni"));
               cl.setNombre(rs.getString("nombre"));
               cl.setTelefono(rs.getInt("telefono"));
               cl.setDireccion(rs.getString("direccion"));
               cl.setRazon(rs.getString("razon"));
               
               ListarCl.add(cl);
           }
           
       }catch(SQLException e){
           System.out.println(e.toString());
           
       }
       return ListarCl;
    }
    
    public boolean EliminarCliente(int id){
        String sql = "DELETE FROM clientes WHERE id= ?";
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        }catch (SQLException e){
            System.out.println(e.toString());
            return false;
        }finally{
            try {
                con.close();
            } catch (SQLException ex) {
               System.out.println(ex.toString());
            }
        }
    }

    public boolean ModificarCliente(Cliente cl) {
        String sql = "UPDATE clientes SET dni=?, nombre =?, telefono=?, direccion=?, razon=? WHERE id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cl.getDni());
            ps.setString(2, cl.getNombre());
            ps.setInt(3, cl.getTelefono());
            ps.setString(4, cl.getDireccion());
            ps.setString(5, cl.getRazon());
            ps.setInt(6, cl.getId());
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

}

