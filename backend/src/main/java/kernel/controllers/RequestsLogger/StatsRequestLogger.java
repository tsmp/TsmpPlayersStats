package kernel.controllers.RequestsLogger;

import org.springframework.stereotype.Component;

@Component("StatsReqLogger")
public class StatsRequestLogger extends BaseRequestLogger
{
    StatsRequestLogger()
    {
        m_ControllerName = "Stats";
    }
}
