

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DAO.pb;
import DAO.menu;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@WebServlet("/XemDSPB")
public class XemDSPB extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public XemDSPB() {
        super();
    }
    pb in = new pb();
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<body>");
        menu menu = new menu();
        menu.htmenu(res, out);
     out.println("<style>");
out.println("  body {");
out.println("    font-family: Arial, sans-serif;");
out.println("    background-image: url('path/to/your/image.jpg');");
out.println("    background-size: cover;");
out.println("    background-repeat: no-repeat;");
out.println("    margin: 20px;");
out.println("  }");

out.println("  h2 {");
out.println("    background-color: #3498db;");
out.println("    color: white;");
out.println("    padding: 10px;");
out.println("    border-radius: 5px;");
out.println("  }");

out.println("  label {");
out.println("    display: block;");
out.println("    margin-bottom: 5px;");
out.println("    font-weight: bold;");
out.println("  }");

out.println("  input[type='text'] {");
out.println("    width: 300px;");
out.println("    padding: 8px;");
out.println("    margin-bottom: 10px;");
out.println("    box-sizing: border-box;");
out.println("  }");

out.println("  input[type='submit'] {");
out.println("    background-color: #3498db;");
out.println("    color: white;");
out.println("    padding: 10px 15px;");
out.println("    border: none;");
out.println("    border-radius: 4px;");
out.println("    cursor: pointer;");
out.println("  }");

out.println("  input[type='submit']:hover {");
out.println("    background-color: #45a049;");
out.println("  }");

out.println("  table {");
out.println("    border-collapse: collapse;");
out.println("    width: 80%;");
out.println("    margin-top: 20px;");
out.println("  }");

out.println("  th, td {");
out.println("    border: 1px solid #dddddd;");
out.println("    text-align: left;");
out.println("    padding: 8px;");
out.println("  }");

out.println("  th {");
out.println("    background-color: #f2f2f2;");
out.println("  }");
out.println("</style>");


        out.println("<h2>Tìm Kiếm Bảng Phòng Ban</h2>");
        out.println("<form method='get'>");
        out.println("  <label for='searchMaPhongBan'>Mã Phòng Ban:</label>");
        out.println("  <input type='text' id='searchMaPhongBan' name='searchMaPhongBan' required>");
        out.println("  <input type='submit' value='Tìm Kiếm'>");
        out.println("</form>");
        
        out.println("<h2>Form Nhập Dữ Liệu Bảng Phòng Ban</h2>");
        out.println("<form method='get'>");
        out.println("  <label for='MaDanToc'>Mã Phòng Ban:</label>");
        out.println("  <input type='text' id='MaPhongBan' name='MaPhongBan' required><br>");
        out.println("  <label for='tenKhach'>Tên Phòng Ban:</label>");
        out.println("  <input type='text' id='TenPhongBan' name='TenPhongBan' required><br>");
        out.println("  <input type='submit' value='Submit'>");
        out.println("</form>");
        out.println("</body>");
        out.println("<style>");
        out.println("body");
        out.println("table {border-collapse: collapse; width: 80%;}");
        out.println("th, td {border: 1px solid #dddddd; text-align: left; padding: 8px;}");
        out.println("th {background-color: #f2f2f2;}");
        out.println("</style>");
        out.println("</head>");
        out.println("</table>");
        out.println("</body></html>");
        
        
        String searchMaPhongBan = req.getParameter("searchMaPhongBan");
        if (searchMaPhongBan == null) {
            in.displayData(out);
        }
        else{
            in.TK(req, res, out);
        }
        in.xoa(req, res, out);
        in.nhap(req, res); 
               
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("text/html");
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Form Nhập Dữ Liệu</title>");
        out.println("</head>");
        out.println("<body>");
        String MaPhongBan = request.getParameter("MaPhongBan");
        out.println("<h2>Form Nhập Dữ Liệu Bảng Phòng Ban</h2>");
        out.println("<form method='post'>");
       
        out.println("  <input type='hidden' name='MaPhongBan' value='" + MaPhongBan + "'/>");
        out.println("  <label for='TenPhongBan'>Tên Phòng Ban:</label>");
        out.println("  <input type='text' id='TenPhongBan' name='TenPhongBan' required><br>");



        out.println("  <input type='submit' value='Submit'>");
        out.println("</form>");
        
        out.println("</body>");
        out.println("</html>");
        String TenPhongBan = request.getParameter("TenPhongBan");
        if(TenPhongBan!=null )
        {
        in.sua(request, response, out);
        }
        
    }
    
}




