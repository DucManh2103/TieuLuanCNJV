package DAO;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class tg {
        Connection con ;
        Statement stmt ;
        int MaTonGiao ;
        String TenTonGiao ;
       

    public tg() {
    }
      public void openConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/manh?useUnicode=true&characterEncoding=UTF-8", "root", "manh2103");
        }
        private tg Parameter (HttpServletRequest request) {
        tg TonGiao = new tg();
            TonGiao.MaTonGiao = Integer.parseInt(request.getParameter("MaTonGiao"));
            TonGiao.TenTonGiao = request.getParameter("TenTonGiao");
            
        try {
           
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return TonGiao;
    }
        public void displayData(PrintWriter out) 
        {
        try {
            openConnection();
            stmt = con.createStatement();
            String sql = "select * from TonGiao";
            ResultSet rs = stmt.executeQuery(sql);
            out.println("<table border=1 width=50% height=50%>");
            out.println("<tr><th>Mã Tôn Giáo</th><th>Tên Tôn Giáo</th></tr>");
            while (rs.next()) {
                int MaTonGiao = rs.getInt("MatonGiao");
                String TenTonGiao = rs.getString("TenTonGiao");
                out.println("<tr><td>" + MaTonGiao + "</td><td>"+ TenTonGiao + "</td>"+"<td><a href='XemDSTG?MaTonGiaoXoa=" + MaTonGiao + "'>Xóa</a></td>"+
                "<td><form method='post'>" +
                "<input type='hidden' name='MaTonGiao' value='" + MaTonGiao + "'>" +
                "<input type='submit' value='Sửa'>" +
                "</form></td></tr>");
            }
            out.println("</table>");
            con.close();
            } catch (Exception e) {
            out.println("Lỗi: " + e.getMessage());
            }
        }
        public void nhap(HttpServletRequest request, HttpServletResponse response)
        {
        try {
           openConnection();
           tg tg = Parameter(request);
           String sql = "INSERT INTO TonGiao (MaTonGiao , TenTonGiao) VALUES (?, ?)";

            try (PreparedStatement statement = con.prepareStatement(sql))
            {   
                statement.setInt(1, tg.MaTonGiao);
                statement.setString(2, tg.TenTonGiao);

                // Thực hiện chèn dữ liệu
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    PrintWriter out = response.getWriter();
         
                    System.out.println("Dữ liệu đã được chèn thành công!");
                } 
                else 
                {
                    PrintWriter out = response.getWriter();
                  
                }
            }
            } catch (Exception e) 
            {
                   e.printStackTrace();
                    
            }
        } 
       public void xoatubang(String tableName, String columnName, int value) throws Exception 
       {
        String sql = "DELETE FROM " + tableName + " WHERE " + columnName + " = ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) 
            {
            statement.setInt(1, value);
            statement.executeUpdate();
            }
       }
        public void xoa(HttpServletRequest request, HttpServletResponse response ,PrintWriter out)
        {
           try {
            openConnection();

            
            String MaTonGiaoParam = request.getParameter("MaTonGiaoXoa");
            if (MaTonGiaoParam != null && !MaTonGiaoParam.isEmpty()) {
                int MaTonGiao = Integer.parseInt(MaTonGiaoParam);

                
                con.setAutoCommit(false);

                try {
                    xoatubang("TonGiao", "MaTonGiao", MaTonGiao);
                    xoatubang("NhanVien", "MaTonGiao", MaTonGiao);
                    
                    con.commit();

                    out.println("Xóa  thành công!");
                } catch (Exception e) {
                    
                    con.rollback();
                    out.println("Lỗi: " + e.getMessage());
                } finally {
                  
                    con.setAutoCommit(true);
                }
            } 
        } catch (Exception e) {
            out.println("Lỗi: " + e.getMessage());
        } finally {
            
            out.close();
        }
    }
        public void TK(HttpServletRequest req, HttpServletResponse res ,PrintWriter out)
        {
        String searchMaTonGiao = req.getParameter("searchMaTonGiao");
        try {
            openConnection();
            String sql = "SELECT * FROM DanToc WHERE MaTonGiao=?";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setString(1, searchMaTonGiao);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    out.println("<style>");
                    out.println("body");
                    out.println("table {border-collapse: collapse; width: 80%;}");
                    out.println("th, td {border: 1px solid #dddddd; text-align: left; padding: 8px;}");
                    out.println("th {background-color: #f2f2f2;}");
                    out.println("</style>");
                    out.println("<table %>");
                    out.println("<tr><th>Mã Tôn Giáo</th><th>Tên Tôn Giáo</th></tr>");
                    out.println("<tr><td>" + resultSet.getString("MaTonGiao") + "</td><td>" + resultSet.getString("TenTonGiao") + "</td> "+ "</td>"+"<td><a href='XemDSTG?MaTonGiaoXoa=" + resultSet.getString("MaTonGiao") + "'>Xóa</a></td>"
                    +"<td><form method='post'>" +
                    "<input type='hidden' name='MaTonGiao' value='" + resultSet.getString("MaTonGiao") + "'>" +
                    "<input type='submit' value='Sửa'>" +
                    "</form></td></tr>");
                } else {
                 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
        }
        public void sua(HttpServletRequest req, HttpServletResponse res ,PrintWriter out)
        {
        res.setContentType("text/html");
        try {
            openConnection();
            tg tg = Parameter(req);
            String sql = "UPDATE TonGiao SET TenTonGiao=? WHERE MaTonGiao=?";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                
                statement.setString(1, tg.TenTonGiao);
                statement.setInt(2, tg.MaTonGiao);
                
               
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    out = res.getWriter();
                    out.println("Dữ liệu đã được cập nhật thành công!");
                }else {
                    out = res.getWriter();
                    out.println("Không thể cập nhật dữ liệu!");
                }
           
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
        }
        
}


