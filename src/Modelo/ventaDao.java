/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author lourd
 */
public class ventaDao {
    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
    int r;
    
    
    public int IdVenta(){
    int id =0;
    String sql = "SELECT MAX(id) FROM ventas";
    try{
        con= cn.getConnection();
        ps= con.prepareStatement(sql);
        rs = ps.executeQuery();
        if(rs.next()){
            id= rs.getInt(1);
        }
    }catch(SQLException e){
        System.out.println(e.toString());
    }
    return id;
}
    
    public int RegistrarVenta(Venta v){
        String sql = "INSERT INTO ventas (cliente, vendedor, total, fecha) VALUES (?,?,?,?)";
        try{
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, v.getCliente());
            ps.setString(2, v.getVendedor());
            ps.setDouble(3, v.getTotal());
            ps.setString(4, v.getFecha());
            ps.execute();
        }catch(SQLException e){
            System.out.println(e.toString());
        } finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e.toString());
                
            }
        }
      return r;
    }
    
    public int RegistrarDetalle(Detalle Dv){
        String sql = "INSERT INTO detalle (cod_pro, cantidad, precio, id_venta) VALUES (?,?,?,?)";
        try{
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, Dv.getCod_pro());
            ps.setInt(2, Dv.getCantidad());
            ps.setDouble(3, Dv.getPrecio());
            ps.setInt(4, Dv.getId());
            ps.execute();
        }catch (SQLException e){
            System.out.println(e.toString());
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.out.println(e.toString());
                
            }
        }
        return r;
    
    }
    public boolean ActualizarStock(int cant, String cod){
    
    String sql = "UPDATE productos SET stock = ? WHERE codigo =?";
    try{
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, cant);
        ps.setString(2, cod);
        ps.execute();
        
        return true;
    }catch (SQLException e){
        System.out.println(e.toString());
        return false;
    }
}
    
    public List ListarVentas() {

        List<Venta> ListaVenta = new ArrayList(); //devuelve una lista de objetos de tipo Cliente.
        String sql = "SELECT * FROM ventas";
        try {;
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) { // recorre cada fila en el ResultSet.
                Venta vent = new Venta();

                //valor de la columna "id" de la fila actual del ResultSet y se asigna al atributo id del objeto cl
                vent.setId(rs.getInt("id"));
                vent.setCliente(rs.getString("cliente"));
                vent.setVendedor(rs.getString("vendedor"));
                vent.setTotal(rs.getDouble("total"));

                ListaVenta.add(vent);

            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return ListaVenta;
    }
}
