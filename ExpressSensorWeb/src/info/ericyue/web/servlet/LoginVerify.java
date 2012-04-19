package info.ericyue.web.servlet;
import info.ericyue.web.dao.UserDao;
import info.ericyue.web.dao.impl.UserDaoImpl;
import info.ericyue.web.entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginVerify")
public class LoginVerify extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public LoginVerify() {
        super();
        
    }
    public void init() throws ServletException{}
	public void destroy() {
		super.destroy();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out= response.getWriter();
		UserDao dao=new UserDaoImpl();
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		User u=dao.login(username, password); 
		if(u!=null){
			out.print("1");	
		}
		else{
			out.print("0");
		}
		out.flush();
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		doGet(request,response);
	}
}
