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


public class dt {
        Connection con ;
        Statement stmt ;
        int MaDanToc ;
        String TenDanToc ;
       

    public dt() {
    }
      public void openConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/manh?useUnicode=true&characterEncoding=UTF-8", "root", "manh2103");
        }
        private dt Parameter (HttpServletRequest request) {
        dt DanToc = new dt();
            DanToc.MaDanToc = Integer.parseInt(request.getParameter("MaDanToc"));
            DanToc.TenDanToc = request.getParameter("TenDanToc");
            
        try {
           
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return DanToc;
    }
        public void displayData(PrintWriter out) 
        {
        try {
            openConnection();
            stmt = con.createStatement();
            String sql = "select * from DanToc";
            ResultSet rs = stmt.executeQuery(sql);
            out.println("<table border=1 width=50% height=50%>");
            out.println("<tr><th>Mã Dân Tộc</th><th>Tên Dân Tộc</th></tr>");
            while (rs.next()) {
                int MaDanToc = rs.getInt("MaDanToc");
                String TenDanToc = rs.getString("TenDanToc");
                out.println("<tr><td>" + MaDanToc + "</td><td>"+ TenDanToc + "</td>"+"<td><a href='XemDSDT?MaDanTocXoa=" + MaDanToc + "'>Xóa</a></td>"+
                "<td><form method='post'>" +
                "<input type='hidden' name='MaDanToc' value='" + MaDanToc + "'>" +
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
           dt dt = Parameter(request);
           String sql = "INSERT INTO DanToc (MaDanToc , TenDanToc) VALUES (?, ?)";

            try (PreparedStatement statement = con.prepareStatement(sql))
            {   
                statement.setInt(1, dt.MaDanToc);
                statement.setString(2, dt.TenDanToc);

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

            // Retrieve the maKhach parameter from the request
            String MaDanTocParam = request.getParameter("MaDanTocXoa");
            if (MaDanTocParam != null && !MaDanTocParam.isEmpty()) {
                int MaDanToc = Integer.parseInt(MaDanTocParam);

                // Start a transaction
                con.setAutoCommit(false);

                try {
                    xoatubang("DanToc", "MaDanToc", MaDanToc);
                    xoatubang("NhanVien", "MaDanToc", MaDanToc);
                    
                    con.commit();

                    out.println("Xóa  thành công!");
                } catch (Exception e) {
                    // Rollback the transaction if an error occurs
                    con.rollback();
                    out.println("Lỗi: " + e.getMessage());
                } finally {
                    // Reset auto-commit to true
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
        String searchMaDanToc = req.getParameter("searchMaDanToc");
        try {
            openConnection();
            String sql = "SELECT * FROM DanToc WHERE MaDanToc=?";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setString(1, searchMaDanToc);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    out.println("<style>");
                    out.println("body");
                    out.println("table {border-collapse: collapse; width: 80%;}");
                    out.println("th, td {border: 1px solid #dddddd; text-align: left; padding: 8px;}");
                    out.println("th {background-color: #f2f2f2;}");
                    out.println("</style>");
                    out.println("<table %>");
                    out.println("<tr><th>Mã Dân Tộc</th><th>Tên Dân Tộc</th></tr>");
                    out.println("<tr><td>" + resultSet.getString("MaDanToc") + "</td><td>" + resultSet.getString("TenDanToc") + "</td> "+ "</td>"+"<td><a href='XemDSDT?MaDanTocXoa=" + resultSet.getString("MaDanToc") + "'>Xóa</a></td>"
                    +"<td><form method='post'>" +
                    "<input type='hidden' name='MaDanToc' value='" + resultSet.getString("MaDanToc") + "'>" +
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
            dt dt = Parameter(req);
            String sql = "UPDATE DanToc SET TenDanToc=? WHERE MaDanToc=?";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                
                statement.setString(1, dt.TenDanToc);
                statement.setInt(2, dt.MaDanToc);
                
               
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    out = res.getWriter();
                    out.println("Dữ liệu đã được cập nhật thành công!");
                }else {
                    out = res.getWriter();
                    out.println("Không thể cập nhật dữ liệu! .");
                }
           
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
        }
        
}

