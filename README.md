# For Sale
This project was developed using Java to practive JavaFX (for UI) and websockets (for multiplayer gameplay).

## Description
For Sale is a quick, fun game nominally about buying and selling real estate. During the game's two distinct phases, players first bid for several buildings then, after all buildings have been bought, sell the buildings for the greatest profit possible.
For more information check out [BoardGameGeek](https://boardgamegeek.com/boardgame/172/sale).
[Rules](https://www.ultraboardgames.com/for-sale/game-rules.php)

## Installation
1. Install JavaFX [here](https://gluonhq.com/products/javafx/)
2. Create a User Library: Eclipse -> Window -> Preferences -> Java -> Build Path -> User Libraries -> New. Name it JavaFX and include all the jars under the lib folder from JavaFX.
3. Right click ForSale project in Project Explorer -> Build Path -> Configure Build Path -> Libraries -> Add External Jars... and include all the jars under lib folder from JavaFX
4. Run -> Run Configurations -> Arguments and add VM arguments: *--module-path <location of javafx lib folder from step 1> --add-modules=javafx.controls*

## Starting Game
### Server
1. Run ForSaleGame.Java
2. Click create and specify port number

![client](https://lh3.googleusercontent.com/pw/AM-JKLW3R4rqRY3ZCTzLL5ruJJc7jlo07ZhEEKA3C7yHmSSGL1FtfDW63xpOE1Iyf82biVTHvvRLw8cZIKafjSDsXa2gnnaqwon-6X04Vdw-PlDCo6UskB2RGrD5EtiUCZninlpGfGvb3UQLpRUwXhn1Kxf3=w624-h356-no?authuser=0)


### Client
1. Run ForSaleGame.Java
2. Click Join and enter name, IP address and port number of server

### Server
Once all the players joined the game, click start on the server.

![server](https://lh3.googleusercontent.com/pw/AM-JKLVZuTGJbCDUjitxEr3Y1ynBNVzALyN86Ea_im86bpK630Np0YkEMnNes4Zg7qcsrg4h_-9gfZmv7ZbtDOW94LHCXN1P_4tWYT9tNFybMMyu8JOUsrLvV1qMx3fjfeG7baJAlVJXa8IIfGoTuy_vyoiD=w469-h238-no?authuser=0)

![ui](https://lh3.googleusercontent.com/pw/AM-JKLXXXaJDQQ0sL0waOly6hl6OUPd2pyZN_MELk-8OmYXWG1_8TdQO5hRZqcvNiH9iiCMPM6IZDrUcCQSK4ng3YQ6Q89CBrr7J-jAMnUP8zs6klduKE4NUPKqD2lQMyft7BKoYzSiA1c-8zNioUEEVJEtL=w992-h797-no?authuser=0)