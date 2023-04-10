package kernel.controllers.RequestsLogger;

import org.springframework.stereotype.Component;

@Component("ServReqLogger")
public class ServReqLogger extends BaseRequestLogger
{
    ServReqLogger()
    {
        m_ControllerName = "Serv";
    }
}
