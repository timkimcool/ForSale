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

![client](https://lh3.googleusercontent.com/APlPzBa3DACcQ990MR001BFXJbark7V0BmdvweyWiGdJaZ9kyiVvt_std7rsmpYsB2IgUdAG7s1NX7qgOJnoLF66rMh4u0DcLmVw_TZQE9rjlHM8zLx7IgTJ-77ZVSCCAFoHcQoeCYEiZVfB9uYLXlx3X6zbEtAJFJlBoojV5iXvt8BayktpYI7tghBHi-b56Eb8RFRi8FHLzyKLOp5bQYNZw9cR7QCuTwUCgqCGCE2fE4fsY7lg2OYKwRxp8f1igwrH-yzXONYENvznIk97Te_knHW1eeE6_2ES57hgdVQvxBUTQW8Rg6Z_iPL-FlIc4QuxabXz9Nyl8MAYL-xrBTSF7XTOV3GWNhcd18WIRAvyD62zGIveCalL5WCECTTGpU7u36Ejn2N8TIPrnc0U4FTHJTF8fBYiKMkQ-gSgpsCjVKTr2VioGDeIvWATlqjqkOU5dVlhp_BCroEPU7g7jUHOZOK3Gw75rTpVUtDxJ21U5aTikPEQ_GMPMwXvfN5CGCdvmA7qpYqGfdQjV6ZGoINMpM4yf9bT1Fzucw2UIIoiBHsfCVLfQNX_F_eYfdnLsifNls7bTTxJJFLy3RzOiULwE3bEPSKUBtD8dJA3mM_JDnY8afjDzJ_DRLYTdmW4dsgGtEGq0bTKcDc_V6y4Pchg8D_Air0JepZD_1e9vz6f1V8-yhxZCJhAjnhmpLIwXyKAKu3RtrMuCuDl82xGNuM=w624-h356-no?authuser=0)


### Client
1. Run ForSaleGame.Java
2. Click Join and enter name, IP address and port number of server

### Server
Once all the players joined the game, click start on the server.

![server](https://lh3.googleusercontent.com/pw/AM-JKLVZuTGJbCDUjitxEr3Y1ynBNVzALyN86Ea_im86bpK630Np0YkEMnNes4Zg7qcsrg4h_-9gfZmv7ZbtDOW94LHCXN1P_4tWYT9tNFybMMyu8JOUsrLvV1qMx3fjfeG7baJAlVJXa8IIfGoTuy_vyoiD=w469-h238-no?authuser=0)

![ui](https://lh3.googleusercontent.com/0C5CNWqzrToQgcKM3kcN5tHPS-U3_EhPjHcDOi69E4RozKRpuYBqq87nPAU6jTUZwYq6YNOt7_LEFMFo41oL2XtoQsYkSZHIxhnFLBjncnPi2s6qeMl_iYTUbufz4mDiLD0Y70OzHNGaOaleXq9pNgJBWTve7l7PTVStT5RwcF_yVbbrPfQc1U6pjoRqP3lv9YIs8XNNYwcwcJMre55iKIj0kYBWBQVNDmTyuNkg9Jife65dhTIl7gv93UzNHTQd0CEuu0klyfYy-NnaBlv1rYuyF-0zKgj61Gjb5MXNu5aO-bE4zH6qqwhUEbmiVC3b0GoMGO9ca6HuItUpK5N8xezsTNSvSIM2tgpR7aXft9BjoQi2NmlC3yrHgcsbaWk6R5ZmDf5ORVlZ0ozYsBT57aGTMs7fiXpGtggl3w0xslIkRg-7Bk7hq9_zsXJoIrbOi-1EZQ-vyzA479CFRo01hoTzzUvZBGLEeY0EDgr30ldqFwexSvL6b533cOb6FyYmuavpK3AZTFlm_X0Dh1GIwHiq1afoLyAbgkymH6sDOnb1NncZ2w7Z7sKHNdnNt2mXHz7BJdrpJOQr18vYsKD8BKUG5Nq7IHlp_A6ZW8v5bMNUrh-OqHFshOPveCu5pSetHsFVtKWD-pZvOwZoKHNIavE4M-7SsAIhI6aMZjpjakV7O5NoyRRXkq3qcNGgVHxKqnPP5kufXhopqfE5PS6J4yo=w624-h470-no?authuser=0)