package kernel.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PlayerInfoResponse
{
    String nicknames;
    int frags;
    float kd;
    int arts;
    int achievements;
    int hoursIngame;
    String favouriteWeapon;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class GameStruct
    {
        String serverName;
        String mapName;
        String date;
        Integer id;
    }

    List<GameStruct> games;
}


