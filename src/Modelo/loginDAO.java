
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginDAO {
   Connection con; // establecer la conexión con una base de datos
   PreparedStatement ps; //ejecutar consultas SQL preparadas
   ResultSet rs; //almacenar los resultados de una consulta a la base de datos
   Conexion cn = new Conexion();
   
   public login log(String correo, String pass){
       login l = new login();
       String sql = "SELECT * FROM usuarios WHERE correo=? AND pass= ?";
        try {
           con = cn.getConnection();
           ps = con.prepareStatement(sql);
           ps.setString(1, correo); //valor del parametro (1)
           ps.setString(2, pass);////valor del parametro (1)
           rs= ps.executeQuery(); //Se ejecuta la consulta y  el resultado se almacena en un objeto ResultSet
           if (rs.next()){ //verifica si el ResultSet tiene al menos una fila de resultados
               l.setId(rs.getInt("id"));// obtiene el valor de la columna "id" del resultado y se establece en la propiedad "id" del objeto l
               l.setNombre(rs.getString("nombre"));
               l.setCorreo(rs.getString("correo"));
               l.setPass(rs.getString("pass"));
               l.setRol(rs.getString("rol")); 
           }
       }catch (SQLException e){
           System.out.println(e.toString());
       }
        return l;
   }
   
   
   public boolean Registrar(login reg){
       String sql = "INSERT INTO usuarios (nombre, correo, pass, rol) VALUES(?,?,?,?)";
       try{
           con= cn.getConnection();
           ps = con.prepareStatement(sql);
           ps.setString(1, reg.getNombre());
           ps.setString(2, reg.getCorreo());
           ps.setString(3, reg.getPass());
           ps.setString(4, reg.getRol());
           ps.execute();
           
       }catch(SQLException e){
           System.out.println(e.toString());
           
       }
      return false;
   }
}
