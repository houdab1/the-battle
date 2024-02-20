package org.battle.api;

import lombok.Builder;
import lombok.Getter;
import org.battle.domain.Card;
import org.battle.domain.GameType;
import org.battle.domain.Player;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.logging.Logger;


@Getter
@Builder
public class Battle {
    private static final Logger log = Logger.getLogger(Battle.class.getPackage().getName());
    private GameType gameType;

    private int totalCards = 52;
    private List<List<String>> playersCards;
    private int nbPlayers;
    private List<Player> players;
    private int nbGames;
    private Map<Integer, LinkedList<Card>> deskHistory;
    private LinkedList<Card> deskHistoryPerGame;

    public static Battle createNewGameFromPlayersAndGames(int nbPlayers, int nbGames) {
        List<Player> players = Player.playersOf(nbPlayers);
        return Battle.builder()
                .gameType(GameType.RANDOM)
                .totalCards(52)
                .deskHistory(new HashMap<>())
                .nbPlayers(nbPlayers)
                .nbGames(nbGames)
                .players(players)
                .build();
    }

    public static Battle createNewGameFromListOfPlayers(List<List<String>> playersCards) {
        List<Player> players = Player.playersOf(playersCards);
        return Battle.builder()
                .gameType(GameType.GIVEN_CARDS)
                .deskHistory(new HashMap<>())
                .nbGames(1)
                .nbPlayers(players.size())
                .players(players)
                .build();
    }

    public final void launchGames() {
        for (int i = 0; i < this.nbGames; i++) {
            this.deskHistoryPerGame = new LinkedList<>();
            if (gameType == GameType.RANDOM) {
                distributeCardsToPlayers();
            }
            int count = 0;
            while (!isOver()) {
                count = count + 1;
                play();
                if (count > 10000) {
                    log.log(Level.INFO, "Looks like an infinite Game!");
                    break;
                }
            }
            this.deskHistory.put(i, this.deskHistoryPerGame);
        }
    }

    public Map<Integer, LinkedList<Card>> historyOfCards() {
        return this.deskHistory;
    }

    public List<Integer> playersRanking() {
        players.sort(Comparator.comparing(Player::getScore));
        return players.stream().map(Player::getId).collect(Collectors.toList());
    }

    private void play() {
        List<Card> cardsOnDesk = new ArrayList<>();
        playOnce(this.players, cardsOnDesk);
    }

    private void playOnce(List<Player> players, List<Card> cardsOnDesk) {
        players = pollCards(players, cardsOnDesk);

        List<Player> remainingPlayers = playersHavingMostValuableCard(
                players,
                cardsOnDesk.subList(cardsOnDesk.size() - players.size(), cardsOnDesk.size())
        );

        if (remainingPlayers.size() == 1) {
            log.log(Level.FINE, String.format("Player %s gets the cards", remainingPlayers.get(0).getId()));
            giveAllCardsToWinner(cardsOnDesk, remainingPlayers.get(0));
            clearDesk(players, cardsOnDesk);
        } else {
            log.log(Level.FINE, "Battle!");
            remainingPlayers = pollCards(remainingPlayers, cardsOnDesk);
            playOnce(remainingPlayers, cardsOnDesk);
        }
    }
    private void clearDesk(List<Player> players, List<Card> cardsOnDesk) {
        for (Player p : players) {
            p.shownCards.remove();
        }
        this.deskHistoryPerGame.addAll(cardsOnDesk);
    }

    private List<Player> pollCards(List<Player> players, List<Card> cardsOnDesk) {
        players = removePlayersWithEmptyCardsSet(players);
        for (Player p : players) {
            Card polledCard = p.getCards().poll();
            p.shownCards.add(polledCard);
            cardsOnDesk.add(polledCard);
        }
        return players;
    }

    private List<Player> removePlayersWithEmptyCardsSet(List<Player> players) {
        return players.stream().filter(player -> !player.getCards().isEmpty()).collect(Collectors.toList());
    }

    private void giveAllCardsToWinner(List<Card> cardsOnDesk, Player player) {
        player.getCards().addAll(cardsOnDesk);
    }

    private List<Player> playersHavingMostValuableCard(List<Player> players, List<Card> cardsOnDesk) {
        try {
            Optional<Card> max = cardsOnDesk.stream().max(Enum::compareTo);
        } catch (NullPointerException e) {
            e.getMessage();
        }
        Optional<Card> max = cardsOnDesk.stream().max(Enum::compareTo);
        List<Player> result = new ArrayList<>();
        for (Player p: players) {
            if (p.shownCards.getLast() == max.get()) {
                result.add(p);
            }
        }
        return result;
    }

    private boolean isOver() {
        int nbCards = (totalCards / nbPlayers) * nbPlayers;
        for (Player p : this.players) {
            if (p.getCards().size() == nbCards) {
                log.log(Level.INFO, String.format("Player %s wins the game", p.getId()));
                p.setScore(p.getScore() + 1);
                return true;
            }
        }
        return false;
    }

    void distributeCardsToPlayers() {
        int nbOfCardsPerPlayer = totalCards / this.nbPlayers;
        List<Card> pack = new ArrayList<>();
        for (Card c : Card.values()) {
            pack.addAll(Collections.nCopies(4, c));
        }

        Collections.shuffle(pack);

        for (int i = 0; i < this.nbPlayers * nbOfCardsPerPlayer; i++) {
            for (Player p : this.players) {
                if (p.getId() - 1 == i % this.nbPlayers) {
                    p.addCardToPlayer(pack.get(i));
                }
            }
        }
    }
    
    List<Player> getPlayers() {
        return players;
    }

    void setTotalCards(int nb) {
        this.totalCards = nb;
    }

}
