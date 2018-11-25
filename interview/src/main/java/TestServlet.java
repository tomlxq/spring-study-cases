import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends HttpServlet {

    public static final double DOUBLE = 15.;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Hello,this is a test");

        out.flush();
        out.close();
    }

    public void destroy() {
        System.err.println(getServletName() + "生命周期结束");
        ;
    }

    public void init() throws ServletException {
        System.out.println(getServletName() + "执行初始化");
    }
}
