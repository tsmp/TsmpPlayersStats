package kernel.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class GameInfoResponse
{
    String date;
    String server;
    String map;
    Integer kills;
    Integer deaths;
    Float kd;
    Integer killsAi;
    Integer killsOneLife;
    Float hoursIngame;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class WpnStat
    {
        String wpnName;
        Integer hits;
    }

    List<WpnStat> wpnStats;
}
