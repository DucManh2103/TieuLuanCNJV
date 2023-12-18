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


public class qtct {
        Connection con ;
        Statement stmt ;
        int MaNhanVien ;
        int MaPhongBan ;
        String NgayBatDau;

    public qtct() {
    }
      public void openConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/manh?useUnicode=true&characterEncoding=UTF-8", "root", "manh2103");
        }
        private qtct Parameter (HttpServletRequest request) {
        qtct QuaTrinhCongTac = new qtct();
            QuaTrinhCongTac.MaNhanVien = Integer.parseInt(request.getParameter("MaNhanVien"));
            QuaTrinhCongTac.MaPhongBan = Integer.parseInt(request.getParameter("MaPhongBan"));
            QuaTrinhCongTac.NgayBatDau = request.getParameter("NgayBatDau");
            
        try {
           
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return QuaTrinhCongTac;
    }
        public void displayData(PrintWriter out) 
        {
        try {
            openConnection();
            stmt = con.createStatement();
            String sql = "select * from QuaTrinhCongTac";
            ResultSet rs = stmt.executeQuery(sql);
            out.println("<table border=1 width=50% height=50%>");
            out.println("<tr><th>Mã Nhân Viên</th><th>Mã Phòng Ban</th><th>Ngày Bắt Đầu</th></tr>");
            while (rs.next()) {
                int MaNhanVien = rs.getInt("MaNhanVien");
                int MaPhongBan = rs.getInt("MaPhongBan");
                String NgayBatDau = rs.getString("NgayBatDau"); 
                out.println("<tr><td>" + MaNhanVien + "</td><td>" + MaPhongBan + "</td><td>" + NgayBatDau + "</td>"+"<td><a href='XemDSQTCT?MaNhanVienXoa=" + MaNhanVien + "'>Xóa</a></td>"+
                "<td><form method='post'>" +
                "<input type='hidden' name='MaNhanVien' value='" + MaNhanVien + "'>" +
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
           qtct qtct = Parameter(request);
           String sql = "INSERT INTO QuaTrinhCongTac (MaNhanVien ,MaPhongBan,NgayBatDau) VALUES ( ?, ?, ?)";

            try (PreparedStatement statement = con.prepareStatement(sql))
            {   
                statement.setInt(1, qtct.MaNhanVien);
                statement.setInt(2, qtct.MaPhongBan);
                statement.setString(3, qtct.NgayBatDau);
               

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
            String MaNhanVienParam = request.getParameter("MaNhanVienXoa");
            if (MaNhanVienParam != null && !MaNhanVienParam.isEmpty()) {
                int MaNhanVien = Integer.parseInt(MaNhanVienParam);

                // Start a transaction
                con.setAutoCommit(false);

                try {
                    
                    xoatubang("QuaTrinhCongTac", "MaNhanVien", MaNhanVien);
                    
                    con.commit();

                    out.println("Xóa thành công!");
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
        String searchMaNhanVien = req.getParameter("searchMaNhanVien");
        try {
            openConnection();
            String sql = "SELECT * FROM QuaTrinhCongTac WHERE MaNhanVien=?";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setString(1, searchMaNhanVien);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    out.println("<style>");
                    out.println("body");
                    out.println("table {border-collapse: collapse; width: 80%;}");
                    out.println("th, td {border: 1px solid #dddddd; text-align: left; padding: 8px;}");
                    out.println("th {background-color: #f2f2f2;}");
                    out.println("</style>");
                    out.println("<table %>");
                    out.println("<tr><th>Mã Nhân Viên</th><th>Mã Phòng Ban</th><th>Ngày Bắt Đầu</th></tr>");
                    out.println("<tr><td>" + resultSet.getInt("MaNhanVien") + "</td><td>" + resultSet.getInt("MaPhongBan") + "</td><td>" + resultSet.getString("NgayBatDau") + "</td><td>"+ resultSet.getString("NgaySinh") + "</td>"+"<td><a href='XemDSQTCT?MaNhanVienXoa=" + resultSet.getString("MaNhanVien") + "'>Xóa</a></td>"
                    +"<td><form method='post'>" +
                    "<input type='hidden' name='MaNhanVien' value='" + resultSet.getString("MaNhanVien") + "'>" +
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
            qtct qtct = Parameter(req);
            String sql = "UPDATE QuaTrinhCongTac SET MaPhongBan=?, NgayBatDau=? WHERE MaNhanVien=?";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                
              
                statement.setInt(1, qtct.MaPhongBan);
                statement.setString(2, qtct.NgayBatDau);
                statement.setInt(3, qtct.MaNhanVien);
    
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

