### La Bataille
L'API proposée par ce projet donne accès à certaines fonctionnalités, notamment la création d'un jeu de deux manières différentes (exemples dans le Main) :

createNewGameFromPlayersAndGames(nbJoueurs, nbParties)
ou bien :
createNewGameFromListOfPlayers(listeCartes).

Une fois le jeu créé et lancé, l'utilisateur pourra accéder à l'historique des cartes et le classement des joueurs via les méthodes publiques historyOfCards() et
playersRanking().

NB: L'état actuel de l'API ne garantit pas la convergence de l'algorithme vers un seul vinqueur par partie et un choix a été fait alors pour arrêter le jeu 
quand une partie dure très longtemps (10000 passages hors bataille). Dans ce cas, le score des différents joueurs reste inchangé même si certains 
auraient quitté le jeu.