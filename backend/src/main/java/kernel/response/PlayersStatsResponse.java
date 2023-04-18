package kernel.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PlayersStatsResponse
{
    int num;
    String nicknames;
    int frags;
    float kd;
    int arts;
    int achievements;
    int hoursIngame;
    int id;
}
