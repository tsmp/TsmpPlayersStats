package kernel.controllers.RequestsLogger;

import org.springframework.stereotype.Component;

@Component("SiteReqLogger")
public class SiteReqLogger extends BaseRequestLogger
{
    SiteReqLogger()
    {
        m_ControllerName = "Site";
    }
}
