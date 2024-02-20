package org.battle.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Player {

    private final int id;
    private int score;
    private LinkedList<Card> cards;
    public LinkedList<Card> shownCards = new LinkedList<>();

    private Player(int id) {
        this.id = id;
        this.cards = new LinkedList<>();
    }

    private Player(int id, LinkedList<Card> cards) {
        this.id = id;
        this.cards = cards;
    }

    public static List<Player> playersOf(int nbPlayers) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < nbPlayers; i++) {
            players.add(new Player(i + 1));
        }
        return players;
    }

    public static List<Player> playersOf(List<List<String>> playersCards) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playersCards.size(); i++) {
            List<String> cardsNames = playersCards.get(i);
            LinkedList<Card> cards = new LinkedList<>();
            for (String s: cardsNames){
                cards.add(Card.valueOf(s.toUpperCase()));
            }
            players.add(new Player(i + 1, cards));
        }
        return players;
    }

    public int getId() {
        return this.id;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }

    public LinkedList<Card> getCards() {
        return this.cards;
    }

    public void addCardToPlayer(Card card) {
        this.cards.add(card);
    }

}
