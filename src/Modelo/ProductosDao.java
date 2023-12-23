/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JComboBox;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
  
public class ProductosDao {
    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
    
    public boolean RegistrarProducto(Productos pro){
        String sql ="INSERT INTO productos (codigo, nombre, proveedor, stock, precio) VALUES (?,?,?,?,?)";
        try{
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setString(3, pro.getProveedor());
            ps.setInt(4, pro.getStock());
            ps.setDouble(5, pro.getPrecio());
            ps.execute();
            return true;
        }catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }
    
    public void ConsultarProveedor(JComboBox proveedor){
        String sql = "SELECT nombre FROM proveedor ";
        try{
            con=cn.getConnection();
            ps =con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){  //ueve el cursor al siguiente registro y devuelve true si hay más registros para procesar
                proveedor.addItem(rs.getString("nombre")); //se agrega ese valor como un elemento al JComboBox proveedor. Esto llena el JComboBox con los nombres de proveedores obtenidos de la base de datos.
            }
        }catch(SQLException e){
            System.out.println(e.toString());
        }
    }
    
    public List ListarProductos(){
        
       List<Productos> Listarpro = new ArrayList(); //devuelve una lista de objetos de tipo Cliente.
       String sql = "SELECT * FROM productos";
       try{;
           con = cn.getConnection();
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery();
           while (rs.next ()){ // recorre cada fila en el ResultSet.
               Productos pro = new Productos ();
               
               //valor de la columna "id" de la fila actual del ResultSet y se asigna al atributo id del objeto cl
               pro.setId(rs.getInt("id")); 
               pro.setCodigo(rs.getString ("codigo"));
               pro.setNombre(rs.getString("nombre"));
               pro.setProveedor(rs.getString("proveedor"));
               pro.setStock(rs.getInt("stock"));
               pro.setPrecio(rs.getDouble("precio"));
               
              Listarpro.add(pro);
            
           }
           
       }catch(SQLException e){
           System.out.println(e.toString());
           
       }
       return Listarpro;
    }
    public boolean EliminarProducto(int id){
        String sql = "DELETE FROM productos WHERE id= ?";
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
    public boolean ModificarPrductos(Productos pro) {
        String sql = "UPDATE productos SET codigo=?, nombre =?, proveedor=?, stock=?, precio=? WHERE id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setString(3, pro.getProveedor());
            ps.setInt(4, pro.getStock());
            ps.setDouble(5, pro.getPrecio());
            ps.setInt(6, pro.getId());
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
    
    public Productos BuscarPro (String cod){
        Productos producto = new Productos ();
        String sql ="SELECT * FROM productos WHERE codigo = ? ";
        try{
            con= cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, cod); //asigna 'cod' como valor del primer parámetro en la consulta
            rs = ps.executeQuery();
            if(rs.next()){ //// Si se encontró un resultado, asignar los valores al objeto 'producto'
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setStock(rs.getInt("stock"));
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return producto;
    }

}
