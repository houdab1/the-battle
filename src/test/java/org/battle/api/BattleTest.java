package org.battle.api;

import org.battle.domain.Card;
import org.battle.domain.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleTest {
    Battle game1;
    Battle game2;

    @Before
    public void setUp() throws Exception {
        List<List<String>> playersCards = List.of(
                List.of("Two", "Three"),
                List.of("Four", "Five")
        );

        this.game1 = Battle.createNewGameFromPlayersAndGames(3, 3);
        this.game2 = Battle.createNewGameFromListOfPlayers(playersCards);
    }

    @Test
    public void check_players_are_constructed(){
        List<Player> players2 = game2.getPlayers();
        assert(players2.size() == 2);
        assert(players2.get(0).getCards().size() == 2);
        assert(players2.get(1).getCards().size() == 2);

        List<Player> players1 = game1.getPlayers();
        assert(players1.size() == 3);

        game1.distributeCardsToPlayers();
        assert(players1.get(0).getCards().size() == 17);
        assert(players1.get(1).getCards().size() == 17);
        assert(players1.get(2).getCards().size() == 17);
    }

    @Test
    public void verify_player_2_wins() {
        game2.setTotalCards(4);
        game2.launchGames();
        Assert.assertEquals(List.of(1, 2), game2.playersRanking());
        Map<Integer, List<Card>> expected = new HashMap<>();
        expected.put(0, List.of(Card.TWO, Card.FOUR, Card.THREE, Card.FIVE));
        Assert.assertEquals(expected, game2.historyOfCards());
    }
}