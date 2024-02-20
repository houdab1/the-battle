package org.battle;

import org.battle.api.Battle;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Battle game1 = Battle.createNewGameFromPlayersAndGames(4, 10);
        game1.launchGames();
        System.out.println(game1.historyOfCards());
        System.out.println(game1.playersRanking());

        List<List<String>> playersCards = List.of(
                List.of("Four", "Five"),
                List.of("Five", "Four")
        );

        Battle game2 = Battle.createNewGameFromListOfPlayers(playersCards);
        game2.launchGames();
        System.out.println(game2.historyOfCards());
        System.out.println(game2.playersRanking());


    }
}