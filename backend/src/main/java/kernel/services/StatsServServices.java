package kernel.services;

import kernel.entity.ActiveSession;
import kernel.repository.PlayersRepoJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatsServServices
{
    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private PlayersRepoJPA playersRepoJPA;

    public String GenNewPlayerUID(int key)
    {
        ActiveSession session = sessionManager.GetSession(key);

        if (session == null)
            return "Error: not authorized!";

        return playersRepoJPA.NextValueUID().toString();
    }
}
