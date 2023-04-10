package kernel.controllers.RequestsLogger;

import org.springframework.stereotype.Component;

@Component("SpecReqLogger")
public class SpecReqLogger extends BaseRequestLogger
{
    SpecReqLogger()
    {
        m_ControllerName = "Spec";
    }
}
