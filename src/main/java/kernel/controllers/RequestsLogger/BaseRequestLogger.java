package kernel.controllers.RequestsLogger;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class BaseRequestLogger
{
    public void Log(HttpServletRequest request, String params, String funcName)
    {
        String str = "";
        str += m_ControllerName + ": ";
        str += request.getRemoteAddr() + " requests " + funcName + ". ";
        str += params;

        System.out.println(str);
    }

    protected String m_ControllerName;
}
