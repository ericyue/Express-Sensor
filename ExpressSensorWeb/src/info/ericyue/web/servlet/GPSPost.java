package info.ericyue.web.servlet;

import info.ericyue.web.util.DBUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement; 
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/GPSPost")
public class GPSPost extends HttpServlet {
    public void init() throws ServletException{}
	public void destroy() {
		super.destroy();
	}
	public GPSPost() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out= response.getWriter();
		String id=request.getParameter("id");
		String datetime=request.getParameter("date");
		String lng=request.getParameter("lng");
		String lat=request.getParameter("lat");
		String sql="insert into location (id,date,lng,lat) values(?,?,?,?)";
		//建立数据库连接
		DBUtil util=new DBUtil();
		Connection conn=util.openConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,id);
			pstmt.setString(2,datetime);
			pstmt.setString(3,lng);
			pstmt.setString(4, lat);
			if(pstmt.executeUpdate()>0){
				out.print("1");
			}
			else
				out.print("0");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
