package kernel.response;

import kernel.entity.ClGame;
import kernel.entity.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

public class SiteStruct
{
    @NoArgsConstructor
    @Getter
    @Setter
    public static class PlayerInfoStruct
    {
        public Player player;
        public List<ClGame> games;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class PlayerStruct
    {
        public List<String> nicknames;
        public List<String> addresses;
        public Integer playerId;
        public Optional<String> hwid;
        public Optional<String> info;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class SearchResponce
    {
        public int resultsCnt;
        public int pagesCnt;
        public int firstNumber;
        public List<PlayerStruct> players;
    }
}
