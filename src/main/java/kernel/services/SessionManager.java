package kernel.services;

import kernel.entity.ActiveSession;
import kernel.repository.ActiveSessionsRepoJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class SessionManager
{
    @Autowired
    ActiveSessionsRepoJPA activeSessionsRepoJPA;

    private List<ActiveSession> m_SessionsCached;
    private boolean m_Synchronized = false;

    public SessionManager()
    {
        System.out.println("create SessionManager");
    }

    private void InitialSynchronize()
    {
        if(m_Synchronized)
            return;

        m_SessionsCached = activeSessionsRepoJPA.findAll();
    }

    public int Hash(String str)
    {
        int hash = 7;

        for (int i = 0; i < str.length(); i++)
        {
            int val = (int)str.charAt(i);

            if(val > 300)
                val -= 848;

            //System.out.println(Integer.toString(val));
            hash = hash * 31 + val;
        }

        return hash;
    }

    private int GenerateSessionKey()
    {
        int rndInt;
        boolean hasSame;
        Random rnd = new Random();

        do
        {
            rndInt = rnd.nextInt();
            hasSame = false;

            for(ActiveSession session: m_SessionsCached)
            {
                if(session.getSessionKey() == rndInt || rndInt == 0)
                    hasSame = true;
            }
        }
        while (hasSame);

        return rndInt;
    }

    public String StartSessionAdmin(String adminName)
    {
        InitialSynchronize();
        ActiveSession session = new ActiveSession();
        session.setSessionKey(GenerateSessionKey());
        session.setName(adminName);
        session.setSrvVer("");
        session.setIsAdmin(true);

        System.out.println("admin started new session");
        System.out.println(Integer.toString(session.getSessionKey()));

        m_SessionsCached.add(session);
        activeSessionsRepoJPA.save(session);
        return "id: " + session.getSessionKey();
    }

    public String StartSessionServer(String srv, String ver, int key)
    {
        InitialSynchronize();
        String strToHash = srv + ver + "PlayersBase3218";
        int hash = Hash(strToHash);
        System.out.println(strToHash+" hash: " + Integer.toString(hash));

        if(hash != key)
        {
            System.out.println("new session failed!");
            System.out.println("my key: " + Integer.toString(hash));
            System.out.println("his key: " + Integer.toString(key));
            return "validation failed!";
        }

        ActiveSession session = new ActiveSession();
        session.setSessionKey(GenerateSessionKey());
        session.setName(srv);
        session.setSrvVer(ver);
        session.setIsAdmin(false);

        System.out.println("started new session");
        System.out.println(Integer.toString(session.getSessionKey()));

        m_SessionsCached.add(session);
        activeSessionsRepoJPA.save(session);

        return "id: " + session.getSessionKey();
    }

    public ActiveSession GetSession(int key)
    {
        InitialSynchronize();

        for (ActiveSession session: m_SessionsCached)
        {
            if (session.getSessionKey() == key)
                return session;
        }

        System.out.println("Error: Session not found!");
        return null;
    }
}