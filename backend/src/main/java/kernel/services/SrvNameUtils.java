package kernel.services;

import kernel.entity.ServerName;
import kernel.repository.ServerNamesRepoJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SrvNameUtils
{
    @Autowired
    private ServerNamesRepoJPA serverNamesRepoJPA;

    public Integer GetSrvNameId(String name)
    {
        ServerName srvName = null;
        List<ServerName> lst = serverNamesRepoJPA.findAll();

        for (ServerName srvn: lst)
        {
            if(srvn.getName().equals(name))
            {
                srvName = srvn;
                break;
            }
        }

        if(srvName == null)
        {
            //System.out.println(name+" not found, created");
            srvName = new ServerName();
            srvName.setName(name);
            serverNamesRepoJPA.save(srvName);
            return srvName.getId();
        }

        //System.out.println(name+"found");
        return srvName.getId();
    }
}
