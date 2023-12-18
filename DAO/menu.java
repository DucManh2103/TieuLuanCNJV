package DAO;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

public class menu {
    public void htmenu(HttpServletResponse res,PrintWriter out)
    {
        out.println("<div style='display: flex; justify-content: space-around; background-color: #f1f1f1; padding: 10px;'>");
        out.println("<a href='XemDSNV' style='text-decoration: none; color: #333;'>Nhân Viên</a>");
        out.println("<a href='XemDSPB' style='text-decoration: none; color: #333;'>Phòng Ban</a>");
        out.println("<a href='XemDSTG' style='text-decoration: none; color: #333;'>Tôn Giáo</a>");
        out.println("<a href='XemDSTDVH' style='text-decoration: none; color: #333;'>Trình Độ Văn Hóa</a>");
        out.println("<a href='XemDSDT' style='text-decoration: none; color: #333;'>Dân Tộc </a>");
        out.println("<a href='XemDSQTCT' style='text-decoration: none; color: #333;'>Quá Trình Công Tác </a>");
        out.println("<a href='index.html' style='text-decoration: none; color: #333;'>Đăng xuất </a>");
        out.println("</div>");


    }
}


