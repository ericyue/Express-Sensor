package info.ericyue.web.servlet;

import info.ericyue.web.util.DBUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Query
 */
@WebServlet("/Query")
public class QueryForSend extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryForSend() {
        super();
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		response.setCharacterEncoding("gbk");
		PrintWriter out= response.getWriter();
		String id=request.getParameter("id");
		String sql="select trade_number,receiver_name,receiver_address,receiver_phone,pay_type,trade_cash,goods_status from tradeinfo where deliverer_id="+id;
		DBUtil util=new DBUtil();
		Connection conn=util.openConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();			
			while(rs.next()){
				out.print(rs.getString("trade_number")+"/");
				out.print(rs.getString("receiver_name")+"/");
				out.print(rs.getString("receiver_address")+"/");
				out.print(rs.getString("receiver_phone")+"/");
				out.print(rs.getString("pay_type")+"/");
				out.print(rs.getString("trade_cash")+"/");
				out.print(rs.getString("goods_status")+"#");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
