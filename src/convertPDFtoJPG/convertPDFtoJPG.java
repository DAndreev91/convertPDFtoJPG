/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package convertPDFtoJPG;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import oracle.jdbc.OracleDriver;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;

/**
 *
 * @author HOME
 */
public class convertPDFtoJPG {

    final private static String driverName = "oracle.jdbc.OracleDriver";
    private static final String url = "jdbc:oracle:thin:@131.108.39.3:1521:reg";
    private static final String password = "n#Fp7691";
    private static final String username_db = "ADMIN";
    private static oracle.sql.ARRAY vReturnArray;
         
    
    public static oracle.sql.ARRAY PDFSorter(String filein, String fileout) throws IOException, SQLException {
        //Connection con = DriverManager.getConnection(url, username_db, password);
        
        Properties prp = new Properties();
        prp.setProperty("user", "SYS as SYSDBA");
        prp.setProperty("password", "g7YC5d#k");
        Connection con = new OracleDriver().connect(url, prp);
        ArrayDescriptor descr = new ArrayDescriptor("VARR",con);
        String clear_name = null;
        File file_in = new File(filein);
        File file_out = new File(fileout);
        ArrayList array = new ArrayList();
        if (!file_out.exists()) {
            file_out.mkdir();
        }
        File clear_file = new File(file_out, file_in.getName().replace(".pdf", ""));
        clear_name = new File(file_out, file_in.getName().replace(".pdf", "")).getAbsolutePath();
        PDDocument pd = PDDocument.load(file_in);
        int pa = 0;
        List<PDPage> pdlist = pd.getDocumentCatalog().getAllPages();
        for (PDPage page : pdlist) {
            ++pa;
            System.out.println(clear_name + "_" + String.format("%04d", pa) + ".png");
            array.add(clear_file.getName() + "_" + String.format("%04d", pa) + ".png"); //Выдаёт только имя файлов, не полный путь
            BufferedImage bf = page.convertToImage();
            boolean writeImage = ImageIOUtil.writeImage(bf, clear_name + "_" + String.format("%04d", pa) + ".png", 750);
        }
        pd.close();
        //Array res_arr = con.createArrayOf("ARR_TYPE", array.toArray());
        ARRAY res_arr = new ARRAY(descr,con,array.toArray());
        return res_arr;
    }
    
    public static void main(String[] args) throws IOException, SQLException {
        File file_in = new File(args[0]);
        File file_out = new File(args[1]);
        //vReturnArray = new oracle.sql.ARRAY();
        String err = null;
        convertPDFtoJPG.PDFSorter(args[0], args[1]);
    }
    
}
