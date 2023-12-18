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


public class tdvh {
        Connection con ;
        Statement stmt ;
        int MaTrinhDoVanHoa ;
        String TenTrinhDo ;
        String GhiChu;

    public tdvh() {
    }
      public void openConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/manh?useUnicode=true&characterEncoding=UTF-8", "root", "manh2103");
        }
        private tdvh Parameter (HttpServletRequest request) {
        tdvh TrinhDoVanHoa = new tdvh();
            TrinhDoVanHoa.MaTrinhDoVanHoa = Integer.parseInt(request.getParameter("MaTrinhDoVanHoa"));
            TrinhDoVanHoa.TenTrinhDo = request.getParameter("TenTrinhDo");
            TrinhDoVanHoa.GhiChu = request.getParameter("GhiChu");
            
        try {
           
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return TrinhDoVanHoa;
    }
        public void displayData(PrintWriter out) 
        {
        try {
            openConnection();
            stmt = con.createStatement();
            String sql = "select * from TrinhDoVanHoa";
            ResultSet rs = stmt.executeQuery(sql);
            out.println("<table border=1 width=50% height=50%>");
            out.println("<tr><th>Mã Trình Độ Văn Hóa</th><th>Tên Trình Độ Văn Hóa</th><th>Ghi Chú</th></tr>");
            while (rs.next()) {
                int MaTrinhDoVanHoa = rs.getInt("MaTrinhDoVanHoa");
                String TenTrinhDo = rs.getString("TenTrinhDo");
                String GhiChu = rs.getString("GhiChu"); 
                out.println("<tr><td>" + MaTrinhDoVanHoa + "</td><td>" + TenTrinhDo + "</td><td>" + GhiChu + "</td>"+"<td><a href='XemDSTDVH?MaTrinhDoVanHoaXoa=" + MaTrinhDoVanHoa + "'>Xóa</a></td>"+
                "<td><form method='post'>" +
                "<input type='hidden' name='MaTrinhDoVanHoa' value='" + MaTrinhDoVanHoa + "'>" +
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
           tdvh tdvh = Parameter(request);
           String sql = "INSERT INTO TrinhDoVanHoa (MaTrinhDoVanHoa ,TenTrinhDo,GhiChu) VALUES ( ?, ?, ?)";

            try (PreparedStatement statement = con.prepareStatement(sql))
            {   
                statement.setInt(1, tdvh.MaTrinhDoVanHoa);
                statement.setString(2, tdvh.TenTrinhDo);
                statement.setString(3, tdvh.GhiChu);
               

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
            String MaTrinhDoVanHoaParam = request.getParameter("MaTrinhDoVanHoaXoa");
            if (MaTrinhDoVanHoaParam != null && !MaTrinhDoVanHoaParam.isEmpty()) {
                int MaTrinhDoVanHoa = Integer.parseInt(MaTrinhDoVanHoaParam);

                // Start a transaction
                con.setAutoCommit(false);

                try {
                     xoatubang("TrinhDoVanHoa", "MatrinhDoVanHoa", MaTrinhDoVanHoa);
                    xoatubang("NhanVien", "MaTrinhDoVanHoa", MaTrinhDoVanHoa);
                    
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
        String searchMaTrinhDoVanHoa = req.getParameter("searchMaTrinhDoVanHoa");
        try {
            openConnection();
            String sql = "SELECT * FROM TrinhDoVanHoa WHERE MaTrinhDoVanHoa=?";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setString(1, searchMaTrinhDoVanHoa);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    out.println("<style>");
                    out.println("body");
                    out.println("table {border-collapse: collapse; width: 80%;}");
                    out.println("th, td {border: 1px solid #dddddd; text-align: left; padding: 8px;}");
                    out.println("th {background-color: #f2f2f2;}");
                    out.println("</style>");
                    out.println("<table %>");
                    out.println("<tr><th>Mã Trình Độ Văn Hóa</th><th>Tên Trình Độ</th><th>Ghi Chú</th></tr>");
                    out.println("<tr><td>" + resultSet.getInt("MaTrinhDoVanHoa") + "</td><td>" + resultSet.getString("TenTrinhDo") + "</td><td>" + resultSet.getString("GhiChu") + "</td><td>"+"<td><a href='XemDSTDVH?MaTrinhDoVanHoaXoa=" + resultSet.getString("MaTrinhDoVanHoa") + "'>Xóa</a></td>"+
                    "<td><form method='post'>" +
                    "<input type='hidden' name='MaTrinhDoVanHoa' value='" + resultSet.getString("MaTrinhDoVanHoa") + "'>" +
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
            tdvh tdvh = Parameter(req);
            String sql = "UPDATE TrinhDoVanHoa SET TenTrinhDo=?, GhiChu=? WHERE MaTrinhDoVanHoa=?";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                
              
               statement.setInt(1, tdvh.MaTrinhDoVanHoa);
                statement.setString(2, tdvh.TenTrinhDo);
                statement.setString(3, tdvh.GhiChu);
    
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


