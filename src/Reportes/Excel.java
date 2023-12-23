package Reportes;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import Modelo.Conexion;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
public class Excel {
    public static void reporte() {
 
        Workbook book = new XSSFWorkbook();  //  representa un libro de Excel en memoria, utilizando el formato XLSX.
        Sheet sheet = book.createSheet("Productos"); //Se crea una hoja en el libro de Excel llamada "Productos" 
 
        try {
            InputStream is = new FileInputStream("src/img/logo.png"); //Se carga un logotipo 
            byte[] bytes = IOUtils.toByteArray(is); //El logotipo se convierte en un arreglo de bytes.
            int imgIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_PNG); 
            //el logotipo se agrega al libro de Excel (book) como una imagen de tipo PNG
            //addPicture devuelve un índice que se utiliza posteriormente para hacer referencia a esta imagen en la hoja de Excel.
            is.close(); // se cierra el flujo de entrada (InputStream) para liberar los recursos.
 
            CreationHelper help = book.getCreationHelper(); // para ayudar en la creación de objetos gráficos y otros elementos en una hoja de Excel.
            Drawing draw = sheet.createDrawingPatriarch();
            //utilizando la hoja de Excel (sheet) como base. Drawing representa la capa de dibujo de la hoja de Excel donde se pueden colocar objetos gráficos
 
            ClientAnchor anchor = help.createClientAnchor(); //definir la posición y el tamaño del objeto gráfico que se va a insertar. 
            anchor.setCol1(0);
            anchor.setRow1(1); //en la columna 0 y la fila 1 de la hoja de Excel.
            Picture pict = draw.createPicture(anchor, imgIndex); //loca el logotipo en la ubicación especificada en la hoja de Excel.
            pict.resize(1, 3); //se ajusta el tamaño del logotipo  1 unidad de ancho de columna y un alto de 3
 
            CellStyle tituloEstilo = book.createCellStyle();
            //utilizando el libro de Excel (book) CellStyle define cómo se mostrará el contenido de una celda e formato, alineación, fuente
            tituloEstilo.setAlignment(HorizontalAlignment.CENTER);
            //alineación horizontal del estilo del título
            tituloEstilo.setVerticalAlignment(VerticalAlignment.CENTER);
            // el texto estará centrado verticalmente en las celdas.
            Font fuenteTitulo = book.createFont();//e utiliza para definir las propiedades de fuente del texto en una celda.
            fuenteTitulo.setFontName("Arial");
            fuenteTitulo.setBold(true);
            //texto del título estará en negritas.
            fuenteTitulo.setFontHeightInPoints((short) 14);
            tituloEstilo.setFont(fuenteTitulo);
 
            Row filaTitulo = sheet.createRow(1); // Se crea una nueva fila en la hoja de Excel en la posición 
            Cell celdaTitulo = filaTitulo.createCell(1); //sta celda se utilizará para contener el título del informe.
            celdaTitulo.setCellStyle(tituloEstilo); //título esté formateado según las especificaciones de estilo 
            celdaTitulo.setCellValue("Reporte de Productos");//etiqueta del título en el informe.
 
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3));
             //Se fusionan las celdas desde la fila 1, columna 1 hasta la fila 2, columna 3 en la hoja de Excel. E
            String[] cabecera = new String[]{"Código", "Nombre", "Precio", "Stock"};
 
            CellStyle headerStyle = book.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex()); //color de fondo del estilo btener el índice del color azul claro predefinido en la paleta de colores de Excel.
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
 
            Font font = book.createFont();
            font.setFontName("Arial");
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setFontHeightInPoints((short) 12);
            headerStyle.setFont(font);
 
            Row filaEncabezados = sheet.createRow(4); // insertar los encabezados de las columnas.
 
            for (int i = 0; i < cabecera.length; i++) {
                Cell celdaEnzabezado = filaEncabezados.createCell(i); //. Cada celda corresponde a un encabezado de columna.
                celdaEnzabezado.setCellStyle(headerStyle);
                celdaEnzabezado.setCellValue(cabecera[i]);
            }
 
            Conexion con = new Conexion();
            PreparedStatement ps;
            ResultSet rs;
            Connection conn = con.getConnection();
 
            int numFilaDatos = 5;
 
            CellStyle datosEstilo = book.createCellStyle(); //e utilizará para formatear las celdas que contienen datos.
            datosEstilo.setBorderBottom(BorderStyle.THIN);
            datosEstilo.setBorderLeft(BorderStyle.THIN);
            datosEstilo.setBorderRight(BorderStyle.THIN);
            datosEstilo.setBorderBottom(BorderStyle.THIN);
 
            ps = conn.prepareStatement("SELECT codigo, nombre, precio, stock FROM productos");
            rs = ps.executeQuery();
 
            int numCol = rs.getMetaData().getColumnCount();
             //Se obtiene el número de columnas en el resultado de la consulta utilizando getMetaData()
            while (rs.next()) {
                Row filaDatos = sheet.createRow(numFilaDatos); //Se crea una nueva fila llamada filaDatos en la hoja de Excel en la posición numFilaDatos.
 
                for (int a = 0; a < numCol; a++) {
 
                    Cell CeldaDatos = filaDatos.createCell(a);
                    CeldaDatos.setCellStyle(datosEstilo);
                    CeldaDatos.setCellValue(rs.getString(a + 1));
                }
 
 
                numFilaDatos++;
            }
            sheet.autoSizeColumn(0);
            //e ajusta automáticamente el ancho de las columnas en la hoja de Excel para que se ajusten al contenido.
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            
            sheet.setZoom(150);
            //zoom del 150% para la hoja de Exce
            String fileName = "productos";
            String home = System.getProperty("user.home"); //Se obtiene la ruta del directorio de inicio del usuario actual utilizando 
            File file = new File(home + "/Downloads/" + fileName + ".xlsx"); //epresenta el archivo Excel que se va a crear
            FileOutputStream fileOut = new FileOutputStream(file);
            book.write(fileOut); //Se escribe el contenido del libro de Excel
            fileOut.close();
            Desktop.getDesktop().open(file); //brir el archivo Excel recién creado con la aplicación predeterminada asociada a los archivos de Excel
            JOptionPane.showMessageDialog(null, "Reporte Generado");
 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
}