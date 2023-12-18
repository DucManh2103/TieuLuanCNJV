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


public class pb {
        Connection con ;
        Statement stmt ;
        int MaPhongBan ;
        String TenPhongBan ;
       

    public pb() {
    }
      public void openConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/manh?useUnicode=true&characterEncoding=UTF-8", "root", "manh2103");
        }
        private pb Parameter (HttpServletRequest request) {
        pb PhongBan = new pb();
            PhongBan.MaPhongBan = Integer.parseInt(request.getParameter("MaPhongBan"));
            PhongBan.TenPhongBan = request.getParameter("TenPhongBan");
            
        try {
           
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return PhongBan;
    }
        public void displayData(PrintWriter out) 
        {
        try {
            openConnection();
            stmt = con.createStatement();
            String sql = "select * from PhongBan";
            ResultSet rs = stmt.executeQuery(sql);
            out.println("<table border=1 width=50% height=50%>");
            out.println("<tr><th>Mã phòng Ban</th><th>Tên Phòng Ban</th></tr>");
            while (rs.next()) {
                int MaPhongBan = rs.getInt("MaPhongBan");
                String TenPhongBan = rs.getString("TenPhongBan");
                out.println("<tr><td>" + MaPhongBan + "</td><td>"+ TenPhongBan + "</td>"+"<td><a href='XemDSPB?MaPhongBanXoa=" + MaPhongBan + "'>Xóa</a></td>"+
                "<td><form method='post'>" +
                "<input type='hidden' name='MaPhongBan' value='" + MaPhongBan + "'>" +
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
           pb pb = Parameter(request);
           String sql = "INSERT INTO PhongBan (MaPhongBan , TenPhongBan) VALUES (?, ?)";

            try (PreparedStatement statement = con.prepareStatement(sql))
            {   
                statement.setInt(1, pb.MaPhongBan);
                statement.setString(2, pb.TenPhongBan);

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
            String MaPhongBanParam = request.getParameter("MaPhongBanXoa");
            if (MaPhongBanParam != null && !MaPhongBanParam.isEmpty()) {
                int MaPhongBan = Integer.parseInt(MaPhongBanParam);

                // Start a transaction
                con.setAutoCommit(false);

                try {
                    xoatubang("PhongBan", "MaPhongBan", MaPhongBan);
                    xoatubang("QuaTrinhCongTac", "MaPhongBan", MaPhongBan);
                    
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
        String searchMaPhongBan = req.getParameter("searchMaPhongBan");
        try {
            openConnection();
            String sql = "SELECT * FROM DanToc WHERE MaPhongBan=?";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setString(1, searchMaPhongBan);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    out.println("<style>");
                    out.println("body");
                    out.println("table {border-collapse: collapse; width: 80%;}");
                    out.println("th, td {border: 1px solid #dddddd; text-align: left; padding: 8px;}");
                    out.println("th {background-color: #f2f2f2;}");
                    out.println("</style>");
                    out.println("<table %>");
                    out.println("<tr><th>Mã Phòng Ban</th><th>Tên Phòng Ban</th></tr>");
                    out.println("<tr><td>" + resultSet.getString("MaPhongBan") + "</td><td>" + resultSet.getString("TenPhongBan") + "</td> "+ "</td>"+"<td><a href='XemDSPB?MaPhongBanXoa=" + resultSet.getString("MaPhongBan") + "'>Xóa</a></td>"
                    +"<td><form method='post'>" +
                    "<input type='hidden' name='MaPhongBan' value='" + resultSet.getString("MaPhongBan") + "'>" +
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
            pb pb = Parameter(req);
            String sql = "UPDATE PhongBan SET TenPhongBan=? WHERE MaPhongBan=?";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                
                statement.setString(1, pb.TenPhongBan);
                statement.setInt(2, pb.MaPhongBan);
                
               
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


