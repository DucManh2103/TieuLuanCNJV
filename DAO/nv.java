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


public class nv {
        Connection con ;
        Statement stmt ;
        int MaNhanVien ;
        String TenNhanVien ;
        String SoCMND ;
        String PhaiNu ;
        String NgaySinh ;
        int    MaDanToc ;
        int    MaTrinhDoVanHoa ;
        int    MaTonGiao ;
        String GhiChu;

    public nv() {
    }
      public void openConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/manh?useUnicode=true&characterEncoding=UTF-8", "root", "manh2103");
        }
        private nv Parameter (HttpServletRequest request) {
        nv nhanvien = new nv();
            nhanvien.MaNhanVien = Integer.parseInt(request.getParameter("MaNhanVien"));
            nhanvien.TenNhanVien = request.getParameter("TenNhanVien");
            nhanvien.SoCMND = request.getParameter("SoCMND");
            nhanvien.PhaiNu = request.getParameter("PhaiNu");
            nhanvien.NgaySinh = request.getParameter("NgaySinh");
            nhanvien.MaDanToc = Integer.parseInt(request.getParameter("MaDanToc"));
            nhanvien.MaTrinhDoVanHoa = Integer.parseInt(request.getParameter("MaTrinhDoVanHoa"));
            nhanvien.MaTonGiao = Integer.parseInt(request.getParameter("MaTonGiao"));
            nhanvien.GhiChu = request.getParameter("GhiChu");
        try {
           
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return nhanvien;
    }
        public void displayData(PrintWriter out) 
        {
        try {
            openConnection();
            stmt = con.createStatement();
            String sql = "select * from NhanVien";
            ResultSet rs = stmt.executeQuery(sql);
            out.println("<table border=1 width=50% height=50%>");
            out.println("<tr><th>Mã Nhân Viên</th><th>Tên Nhân Viên</th><th>Số CMND</th><th>Giới Tính</th><th>Ngày Sinh</th><th>Mã Dân Tộc</th><th>Mã Trình Độ Văn Hóa</th><th>Mã Tôn Giáo</th><th>Ghi Chú</th></tr>");
            while (rs.next()) {
                int MaNhanVien = rs.getInt("MaNhanVien");
                String TenNhanVien = rs.getString("TenNhanVien");
                String SoCMND = rs.getString("SoCMND");
                String PhaiNu = rs.getString("PhaiNu");
                String NgaySinh = rs.getString("NgaySinh");
                String MaDanToc = rs.getString("MaDanToc");
                String MaTrinhDoVanHoa = rs.getString("MaTrinhDoVanHoa");
                String MaTonGiao = rs.getString("MaTonGiao");
                String GhiChu = rs.getString("GhiChu");
                out.println("<tr><td>" + MaNhanVien + "</td><td>" + TenNhanVien + "</td><td>" + SoCMND + "</td><td>" + PhaiNu + "</td><td>"+ NgaySinh + "</td><td>" + MaDanToc + "</td><td>" + MaTrinhDoVanHoa + "</td><td>" + MaTonGiao + "</td><td>" + GhiChu + "</td>"+"<td><a href='XemDSNV?MaNhanVienXoa=" + MaNhanVien + "'>Xóa</a></td>"+
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
           nv nv = Parameter(request);
           String sql = "INSERT INTO NhanVien (MaNhanVien ,TenNhanVien,SoCMND, PhaiNu, NgaySinh,MaDanToc,MaTrinhDoVanHoa,MaTonGiao,GhiChu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = con.prepareStatement(sql))
            {   
                statement.setInt(1, nv.MaNhanVien);
                statement.setString(2, nv.TenNhanVien);
                statement.setString(3, nv.SoCMND);
                statement.setString(4, nv.PhaiNu);
                statement.setString(5, nv.NgaySinh);
                statement.setInt(6, nv.MaDanToc);
                statement.setInt(7, nv.MaTrinhDoVanHoa);
                statement.setInt(8, nv.MaTonGiao);
                statement.setString(9, nv.GhiChu);

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
                    xoatubang("NhanVien", "MaNhanVien", MaNhanVien);
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
            String sql = "SELECT * FROM NhanVien WHERE MaNhanVien=?";
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
                    out.println("<tr><th>Mã Nhân Viên</th><th>Tên Nhân Viên</th><th>Số CMND</th><th>Giới Tính</th><th>Ngày Sinh</th><th>Mã Dân Tộc</th><th>Mã Trình Độ Văn Hóa</th><th>Mã Tôn Giáo</th><th>Ghi Chú</th></tr>");
                    out.println("<tr><td>" + resultSet.getString("MaNhanVien") + "</td><td>" + resultSet.getString("TenNhanVien") + "</td><td>" + resultSet.getString("SoCMND") + "</td><td>" + resultSet.getString("PhaiNu") + "</td><td>"+ resultSet.getString("NgaySinh") + "</td><td>" + resultSet.getString("MaDanToc") + "</td><td>" + resultSet.getString("MaTrinhDoVanHoa") + "</td><td>" + resultSet.getString("MaTonGiao") + "</td><td>" + resultSet.getString("GhiChu") + "</td>"+"<td><a href='XemDS?MaNhanVienXoa=" + resultSet.getString("MaNhanVien") + "'>Xóa</a></td>"
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
            nv nv = Parameter(req);
            String sql = "UPDATE NhanVien SET TenNhanVien=?, SoCMND=?, PhaiNu=?, NgaySinh=?, MaDanToc=?, MaTrinhDoVanHoa=?, MaTonGiao=?, GhiChu=? WHERE MaNhanVien=?";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                
              
                statement.setString(1, nv.TenNhanVien);
                statement.setString(2, nv.SoCMND);
                statement.setString(3, nv.PhaiNu);
                statement.setString(4, nv.NgaySinh);
                statement.setInt(5, nv.MaDanToc);
                statement.setInt(6, nv.MaTrinhDoVanHoa);
                statement.setInt(7, nv.MaTonGiao);
                statement.setString(8, nv.GhiChu);
                statement.setInt(9, nv.MaNhanVien);
                
               
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
