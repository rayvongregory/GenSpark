package controllers;

import gameClasses.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HvGApplication extends Application {
    private static final Button leftBtn = new Button("PLAY INTRO");
    private static final Button rightBtn = new Button("SKIP INTRO");
    private static final VBox gameLeftVBox = new VBox();
    private static final VBox gameCenterVBox = new VBox();
    private static final VBox gameTopLeftVBox = new VBox();
    private static final VBox gameBottomLeftVBox = new VBox();
    private static final GridPane gameGrid = new GridPane();
    private static final ScrollPane itemsScrollPane = new ScrollPane();
    private static final GridPane itemsGridPane = new GridPane();
    private static final Button gameBtn1 = new Button();
    private static final Button gameBtn2 = new Button();
    private static final Button gameBtn3 = new Button();
    private static final Button gameBtn4 = new Button();
    private static final Button gameBtn5 = new Button();
    private static final Label gameTopLabel = new Label();
    private static final Label playerName = new Label();
    private static final Label playerStats = new Label();
    private static Stage stage;
    private static int introPage = 0;
    private static int numPlayers;
    private static int numRows;
    private static ArrayList<Human> humans;
    private static ArrayList<Goblin> goblins;
    private static ArrayList<Item> items;
    private static Land land;


    private static void startScreen() {
        BorderPane bp = new BorderPane();
        VBox centerVBox = new VBox();
        HBox centerHBox = new HBox();
        centerVBox.setAlignment(Pos.CENTER);
        centerHBox.setAlignment(Pos.CENTER);
        centerHBox.setPadding(new Insets(50, 0, 0, 0));
        centerHBox.setSpacing(25);
        centerHBox.getChildren().addAll(leftBtn, rightBtn);
        leftBtn.setOnMouseClicked(e -> {
            try {
                playIntro();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        rightBtn.setOnMouseClicked(e -> configGame());
        Text centerText = new Text("Humans vs Goblins");
        centerVBox.getChildren().addAll(centerText, centerHBox);
        bp.setCenter(centerVBox);
        Scene scene = new Scene(bp, 1200, 600);
        Font.loadFont(HvGApplication.class.getResourceAsStream("/styles/WastedPersonalUseRegular-WyegG.ttf"), 18);
        Font.loadFont(HvGApplication.class.getResourceAsStream("/styles/MontserratLight-ywBvq.ttf"), 14);
        Font.loadFont(HvGApplication.class.getResourceAsStream("/styles/MontserratRegular-BWBEl.ttf"), 14);
        scene.getStylesheets().add(Objects.requireNonNull(HvGApplication.class.getResource("/styles/stylesheet.css")).toExternalForm());
        bp.getStyleClass().add("background");
        centerText.getStyleClass().add("title");
        stage.setResizable(false);
        stage.setTitle("Humans vs Goblins");
        stage.setScene(scene);
        stage.show();
    }

    private static void playIntro() throws IOException {
        BorderPane bp = new BorderPane();
        TextArea txtArea = new TextArea();
        Button leftBtn = new Button("BACK");
        Button rightBtn = new Button("NEXT");
        leftBtn.setOnMouseClicked(e -> startScreen());
        rightBtn.setOnMouseClicked(e -> {
            StringBuilder sb = new StringBuilder();
            try {
                for (String s : getIntroScript(++introPage)) {
                    sb.append(s);
                    sb.append("\n");
                }
            } catch (IOException ex) {
                configGame();
            }
            txtArea.setText(sb.toString());
            leftBtn.setOnMouseClicked(ev -> {
                sb.setLength(0);
                try {
                    for (String s : getIntroScript(--introPage)) {
                        sb.append(s);
                        sb.append("\n");
                    }
                } catch (IOException ex) {
                    startScreen();
                }
                txtArea.setText(sb.toString());
            });
        });
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.setSpacing(25);
        hb.setPadding(new Insets(0, 0, 50, 0));
        hb.getChildren().addAll(leftBtn, rightBtn);
        StringBuilder sb = new StringBuilder();
        for (String s : getIntroScript(1)) {
            sb.append(s);
            sb.append("\n");
        }
        txtArea.setText(sb.toString());
        txtArea.setEditable(false);
        txtArea.setFocusTraversable(false);
        bp.setCenter(txtArea);
        bp.setBottom(hb);
        bp.getStyleClass().add("background");
        Scene scene = new Scene(bp, 1200, 600);
        scene.getStylesheets().add(Objects.requireNonNull(HvGApplication.class.getResource("/styles/stylesheet.css")).toExternalForm());
        txtArea.getStyleClass().add("text-area");
        stage.setScene(scene);
        stage.show();
        introPage = 1;
    }

    private static List<String> getIntroScript(int i) throws IOException {
        return Files.readAllLines(Paths.get(String.format("src/main/java/textfiles/IntroductionScript%s", i)));
    }

    private static void configGame() {
        BorderPane bp = new BorderPane();
        Label label = new Label("How many humans and goblins are on each team?");
        leftBtn.setText("3");
        Button middleBtn = new Button("5");
        rightBtn.setText("7");
        leftBtn.setOnMouseClicked(e -> {
            try {
                createGamePieces(3);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        middleBtn.setOnMouseClicked(e -> {
            try {
                createGamePieces(5);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        rightBtn.setOnMouseClicked(e -> {
            try {
                createGamePieces(7);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        VBox vbox = new VBox();
        vbox.getChildren().add(label);
        HBox hbox = new HBox();
        hbox.getChildren().addAll(leftBtn, middleBtn, rightBtn);
        hbox.setPadding(new Insets(25, 0, 0, 0));
        hbox.setSpacing(25);
        hbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(hbox);
        vbox.setAlignment(Pos.CENTER);
        bp.setCenter(vbox);
        Scene scene = new Scene(bp, 1200, 600);
        scene.getStylesheets().add(Objects.requireNonNull(HvGApplication.class.getResource("/styles/stylesheet.css")).toExternalForm());
        bp.getStyleClass().add("background");
        label.getStyleClass().add("label");
        middleBtn.getStyleClass().add("button");
        stage.setScene(scene);
        stage.show();
    }

    private static void createGamePieces(int x) throws FileNotFoundException {
        numPlayers = x;
        land = new Land(numPlayers);
        numRows = land.getNumRows();
        humans = Human.generateHumans(numPlayers, numRows);
        goblins = Goblin.generateGoblins(numPlayers, numRows);
        items = Item.generateItems(numPlayers, numRows, humans, goblins);
        meetYourTeam();
    }

    private static void meetYourTeam() {
        GridPane gp = new GridPane();
        VBox leftVBox = new VBox();
        VBox rightVBox = new VBox();
        leftVBox.setAlignment(Pos.TOP_CENTER);
        rightVBox.setAlignment(Pos.TOP_CENTER);
        GridPane leftGridPane = new GridPane();
        GridPane rightGridPane = new GridPane();
        leftGridPane.setAlignment(Pos.TOP_CENTER);
        rightGridPane.setAlignment(Pos.TOP_CENTER);
        if (numPlayers != 7) {
            leftGridPane.setVgap(20);
            leftGridPane.setHgap(20);
            rightGridPane.setVgap(20);
            rightGridPane.setHgap(20);
        } else {
            leftGridPane.setVgap(10);
            leftGridPane.setHgap(10);
            rightGridPane.setVgap(10);
            rightGridPane.setHgap(10);
        }
        if (numPlayers == 3) {
            leftVBox.setPadding(new Insets(0, 0, 0, 225));
            rightVBox.setPadding(new Insets(0, 225, 0, 0));
            threePersonGridPane(leftGridPane, rightGridPane);
        }
        if (numPlayers == 5) {
            leftVBox.setPadding(new Insets(0, 0, 0, 100));
            rightVBox.setPadding(new Insets(0, 100, 0, 0));
            fivePersonGridPane(leftGridPane, rightGridPane);
        }
        if (numPlayers == 7) sevenPersonGridPane(leftGridPane, rightGridPane);
        Label leftLabel = new Label("Humans");
        leftVBox.getChildren().addAll(leftLabel, leftGridPane);
        Label rightLabel = new Label("Goblins");
        rightVBox.getChildren().addAll(rightLabel, rightGridPane);
        VBox centerVBox = new VBox();
        centerVBox.setAlignment(Pos.CENTER);
        Button centerButton = new Button("Start");
        centerButton.setOnMouseClicked(e -> initGame());
        centerVBox.getChildren().add(centerButton);
        gp.add(leftVBox, 0, 0);
        gp.add(centerVBox, 1, 0);
        gp.add(rightVBox, 2, 0);
        setColumnConstraints(gp);
        Scene scene = new Scene(gp, 1200, 600);
        scene.getStylesheets().add(Objects.requireNonNull(HvGApplication.class.getResource("/styles/stylesheet.css")).toExternalForm());
        Font.loadFont(HvGApplication.class.getResourceAsStream("/styles/WastedPersonalUseRegular-WyegG.ttf"), 18);
        gp.getStyleClass().add("background");
        leftLabel.setAlignment(Pos.TOP_CENTER);
        leftLabel.prefWidthProperty().bind(leftVBox.widthProperty());
        leftLabel.getStyleClass().add("title");
        rightLabel.setAlignment(Pos.TOP_CENTER);
        rightLabel.prefWidthProperty().bind(rightVBox.widthProperty());
        rightLabel.getStyleClass().add("title");
        stage.setScene(scene);
        stage.show();
    }

    private static void setColumnConstraints(GridPane gp) {
        ColumnConstraints cc1 = new ColumnConstraints();
        ColumnConstraints cc2 = new ColumnConstraints();
        ColumnConstraints cc3 = new ColumnConstraints();
        cc1.setPercentWidth(numPlayers == 7 ? 45 : 40);
        cc2.setPercentWidth(numPlayers == 7 ? 10 : 20);
        cc3.setPercentWidth(numPlayers == 7 ? 45 : 40);
        gp.getColumnConstraints().add(cc1);
        gp.getColumnConstraints().add(cc2);
        gp.getColumnConstraints().add(cc3);
    }

    private static void threePersonGridPane(GridPane left, GridPane right) {
        for (int i = 0; i < humans.size(); i++) {
            Label hl = new Label(humans.get(i).toString());
            Label gl = new Label(goblins.get(i).toString());
            hl.getStyleClass().add("box35");
            gl.getStyleClass().add("box35");
            left.add(hl, 0, i);
            right.add(gl, 0, i);
        }
    }

    private static void fivePersonGridPane(GridPane left, GridPane right) {
        int row = 0;
        int col = 0;
        for (int i = 0; i < humans.size(); i++) {
            Label hl = new Label(humans.get(i).toString());
            Label gl = new Label(goblins.get(i).toString());
            hl.getStyleClass().add("box35");
            gl.getStyleClass().add("box35");
            if (row == 2) {
                left.add(hl, col + 1, row, 2, 1);
                right.add(gl, col, row, 2, 1);
            } else {
                left.add(hl, col, row);
                right.add(gl, col, row);
            }
            if (col == 0) col++;
            else if (col == 1) {
                col--;
                row++;
            }
        }
    }

    private static void sevenPersonGridPane(GridPane left, GridPane right) {
        int row = 0;
        int col = 0;
        for (int i = 0; i < humans.size(); i++) {
            Label hl = new Label(humans.get(i).toString());
            Label gl = new Label(goblins.get(i).toString());
            hl.getStyleClass().add("box7");
            gl.getStyleClass().add("box7");
            if (row == 2) {
                left.add(hl, col + 1, row, 2, 1);
                right.add(gl, col + 1, row, 2, 1);
            } else {
                left.add(hl, col, row);
                right.add(gl, col, row);
            }
            if (col != 2) col++;
            else {
                col = 0;
                row++;
            }
        }
    }

    private static void initGame() {
        Human h = humans.get(0);
        GridPane outerGrid = new GridPane();

        playerName.setText(h.getName());
        playerStats.setText(h.getStats());
        playerName.getStyleClass().add("title");
        playerName.setAlignment(Pos.CENTER);
        playerName.setPrefWidth(360);

        smallerBtns();
        initGameBtns(h);
        gameBottomLeftVBox.setPadding(new Insets(30, 0, 0, 0));
        gameBottomLeftVBox.setSpacing(15);

        playerStats.setAlignment(Pos.TOP_CENTER);
        playerStats.setPrefWidth(360);
        playerStats.getStyleClass().add("player-stats");
        playerStats.setLineSpacing(0);
        gameTopLeftVBox.getChildren().addAll(playerName, playerStats);
        gameBottomLeftVBox.setAlignment(Pos.CENTER);
        gameTopLabel.setText(String.format("What's your move, %s?", h.getName()));

        gameTopLabel.setAlignment(Pos.CENTER);
        gameTopLabel.prefWidthProperty().bind(gameCenterVBox.widthProperty());
        gameTopLabel.setPrefHeight(100);
        populateGameGrid();
        gameGrid.setAlignment(Pos.CENTER);
        gameLeftVBox.getChildren().addAll(gameTopLeftVBox, gameBottomLeftVBox);
        gameCenterVBox.getChildren().addAll(gameTopLabel, gameGrid);
        ColumnConstraints cc1 = new ColumnConstraints();
        ColumnConstraints cc2 = new ColumnConstraints();
        cc1.setPercentWidth(30);
        cc2.setPercentWidth(70);
        outerGrid.getColumnConstraints().add(cc1);
        outerGrid.getColumnConstraints().add(cc2);
        outerGrid.add(gameLeftVBox, 0, 0);
        outerGrid.add(gameCenterVBox, 1, 0);

        Scene scene = new Scene(outerGrid, 1200, 600);
        scene.getStylesheets().add(Objects.requireNonNull(HvGApplication.class.getResource("/styles/stylesheet.css")).toExternalForm());
        Font.loadFont(HvGApplication.class.getResourceAsStream("/styles/LemonMilkLight-owxMq.otf"), 18);
        outerGrid.getStyleClass().add("background");
        stage.setScene(scene);
        stage.show();

    }

    private static void populateGameGrid() {
        gameGrid.getChildren().clear();
        for (int i = 0; i < numRows; i++) {
            Label l1 = new Label(String.valueOf(i));
            Label l2 = new Label(String.valueOf(i));
            gameGrid.add(l1, i + 1, 0);
            gameGrid.add(l2, 0, i + 1);
            l1.setPrefWidth(95);
            l1.setAlignment(Pos.CENTER);
            l1.setPadding(new Insets(0, 0, 5, 0));
            l2.setPadding(new Insets(0, 15, 0, 0));
        }
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numRows; j++) {
                ArrayList<GamePiece> gamePieces = land.getSquare(i, j, humans, goblins, items);
                StringBuilder sb = new StringBuilder();
                for (GamePiece gp : gamePieces) sb.append(gp.getIcon());
                Label l = sb.length() == 0 ? new Label("　") : new Label(sb.toString());
                l.setAlignment(Pos.CENTER);
                l.getStyleClass().add("border");
                gameGrid.add(l, i + 1, j + 1);
            }
        }
    }

    private static void smallerBtns() {
        gameBtn1.getStyleClass().add("game-btn");
        gameBtn2.getStyleClass().add("game-btn");
        gameBtn3.getStyleClass().add("game-btn");
        gameBtn4.getStyleClass().add("game-btn");
        gameBtn5.getStyleClass().add("game-btn");
    }

    private static void initGameBtns(Human h) {
        gameBottomLeftVBox.getChildren().clear();
        if (h.getHasNotMovedThisTurn()) {
            gameBtn1.setText("Move");
            gameBtn1.setOnMouseClicked(e -> setBtnsForMove(h));
            gameBottomLeftVBox.getChildren().add(gameBtn1);
        }
        if (h.getInventory().size() > 0) {
            gameBtn2.setText("Check inventory");
            gameBtn2.setOnMouseClicked(e -> {
                gameTopLabel.setText("Here are your items.");
                setBtnsForCheckInventory(h);
            });
            gameBottomLeftVBox.getChildren().add(gameBtn2);
        }
        if (Human.itemsHere(h.getPosition(), items)) {
            gameBtn3.setText("Search for items");
            gameBtn3.setOnMouseClicked(e -> setBtnsForSearchForItems(h));
            gameBottomLeftVBox.getChildren().add(gameBtn3);
        }
        if (h.getHasNotChangedName()) {
            gameBtn4.setText("Change name");
            gameBtn4.setOnMouseClicked(e -> setBtnsForChangeName(h));
            gameBottomLeftVBox.getChildren().add(gameBtn4);
        }
        gameBtn5.setText("End turn");
        gameBtn5.setOnMouseClicked(e -> handleEndTurn(h));
        gameBottomLeftVBox.getChildren().add(gameBtn5);
    }

    private static void setBtnsForMove(Human h) {
        gameBottomLeftVBox.getChildren().clear();
        gameTopLabel.setText("Where do you want to move?");
        if (h.canMoveUp()) {
            gameBtn1.setText("Move Up");
            gameBtn1.setOnMouseClicked(e -> {
                h.moveUp();
                handleAfterMove(h);
            });
            gameBottomLeftVBox.getChildren().add(gameBtn1);
        }
        if (h.canMoveDown()) {
            gameBtn2.setText("Move Down");
            gameBtn2.setOnMouseClicked(e -> {
                h.moveDown();
                handleAfterMove(h);
            });
            gameBottomLeftVBox.getChildren().add(gameBtn2);
        }

        if (h.canMoveLeft()) {
            gameBtn3.setText("Move Left");
            gameBtn3.setOnMouseClicked(e -> {
                h.moveLeft();
                handleAfterMove(h);
            });
            gameBottomLeftVBox.getChildren().add(gameBtn3);
        }

        if (h.canMoveRight()) {
            gameBtn4.setText("Move Right");
            gameBtn4.setOnMouseClicked(e -> {
                h.moveRight();
                handleAfterMove(h);
            });
            gameBottomLeftVBox.getChildren().add(gameBtn4);
        }

        gameBtn5.setText("Cancel");
        gameBtn5.setOnMouseClicked(e -> initGameBtns(h));
        gameBottomLeftVBox.getChildren().add(gameBtn5);
    }

    private static void handleAfterMove(Human h) {
        playerStats.setText(h.getStats());
        populateGameGrid();
        ArrayList<Goblin> goblinsHere = h.getGoblinsHere(goblins);
        if (goblinsHere.size() == 0) {
            initGameBtns(h);
            gameTopLabel.setText("Anything else, %s?".formatted(h.getName()));
        } else if (goblinsHere.size() == 1) {
            gameBottomLeftVBox.getChildren().clear();
            gameBottomLeftVBox.getChildren().add(gameBtn1);
            gameBtn1.setText("Battle");
            gameBtn1.setOnMouseClicked(ev -> {
                Goblin g = goblinsHere.get(0);
                int humansInventorySize = h.getInventory().size();
                int goblinInventorySize = g.getInventory().size();
                h.fightGoblin(g, items);
                if (h.getHealth() == 0) {
                    gameTopLabel.setText("%s attacked %s and lost...".formatted(h.getName(), g.getName()));
                    if (humansInventorySize > 0)
                        gameTopLabel.setText(gameTopLabel.getText() + String.format("\n%s has taken all of their items", g.getName()));
                    playerStats.setText(h.getStats());
                    populateGameGrid();
                    gameBtn1.setText("Next");
                    gameBtn1.setOnMouseClicked(event -> handleGoblinTurn());
                } else {
                    gameTopLabel.setText("%s has defeated %s in battle!".formatted(h.getName(), g.getName()));
                    if (goblinInventorySize > 1)
                        gameTopLabel.setText(gameTopLabel.getText() + "\nAnd it dropped some items...");
                    if (goblinInventorySize == 1)
                        gameTopLabel.setText(gameTopLabel.getText() + "\nAnd it dropped something...");
                    playerStats.setText(h.getStats());
                    populateGameGrid();
                    initGameBtns(h);
                    gameTopLabel.setText("Anything else, %s?".formatted(h.getName()));
                }
            });
        } else {
            gameBottomLeftVBox.getChildren().clear();
            gameBottomLeftVBox.getChildren().add(gameBtn1);
            gameBtn1.setText("Ambush");
            gameBtn1.setOnMouseClicked(ev -> {
                boolean goblinsHadItems = Human.goblinsHadItems(goblinsHere);
                h.ambushTheseGoblins(goblinsHere, items);
                gameTopLabel.setText("%s has ambushed several goblins!".formatted(h.getName()));
                if (goblinsHadItems) gameTopLabel.setText(gameTopLabel.getText() + "\nAnd it dropped some items...");
                playerStats.setText(h.getStats());
                populateGameGrid();
                initGameBtns(h);
                gameTopLabel.setText("Anything else, %s?".formatted(h.getName()));
            });
        }
    }

    private static void setBtnsForCheckInventory(Human h) {
        gameBottomLeftVBox.getChildren().clear();
        gameCenterVBox.getChildren().clear();
        gameCenterVBox.getChildren().add(gameTopLabel);

        itemsGridPane.getChildren().clear();
        itemsGridPane.getColumnConstraints().clear();
        gameBtn1.setText("Back to map");
        gameBtn1.setOnMouseClicked(e -> {
            gameCenterVBox.getChildren().remove(itemsScrollPane);
            gameCenterVBox.getChildren().add(gameGrid);
            populateGameGrid();
            initGameBtns(h);
            gameTopLabel.setText("Anything else, %s?".formatted(h.getName()));
        });
        gameBottomLeftVBox.getChildren().add(gameBtn1);

        gameCenterVBox.getChildren().add(itemsScrollPane);
        itemsScrollPane.setContent(itemsGridPane);
        itemsScrollPane.getStyleClass().add("background");
        itemsScrollPane.setFitToWidth(true);
        ColumnConstraints cc1 = new ColumnConstraints();
        ColumnConstraints cc2 = new ColumnConstraints();
        ColumnConstraints cc3 = new ColumnConstraints();
        cc1.setPercentWidth(33);
        cc2.setPercentWidth(34);
        cc3.setPercentWidth(33);
        itemsGridPane.setVgap(50);
        itemsGridPane.setPadding(new Insets(25, 0, 25, 0));
        itemsGridPane.getColumnConstraints().addAll(cc1, cc2, cc3);
        int col = 0;
        int row = 0;
        for (Item i : h.getInventory()) {
            VBox vb = new VBox();
            Button b1 = new Button();
            if (i.getHealthEffect() > 0) b1.setText("Eat item");
            else {
                if (i.isEquipped()) b1.setText("Unequip item");
                else b1.setText("Equip item");
            }
            Button b2 = new Button("Drop item");
            Label l = new Label(i.toString());

            if (!b1.getText().contains("Unequip")) b1.setOnMouseClicked(e -> eat_equipThis(h, i, "inventory"));
            else b1.setOnMouseClicked(e -> unequipThis(h, i));
            b2.setOnMouseClicked(e -> {
                if (i.isEquipped()) {
                    h.unequipThisItem(i);
                    playerStats.setText(h.getStats());
                }
                h.dropItem(i);
                items.add(i);
                setBtnsForCheckInventory(h);
            });
            vb.getChildren().addAll(l, b1, b2);
            vb.getStyleClass().add("item");
            b1.getStyleClass().add("item-btn");
            b2.getStyleClass().add("item-btn");
            itemsGridPane.add(vb, col, row);
            if (col == 3) {
                col = 0;
                row++;
            } else col++;
        }
        if (itemsGridPane.getChildren().size() == 0) {
            gameTopLabel.setText(gameTopLabel.getText() + "\nThere's nothing else here.");
        }
    }

    private static void setBtnsForSearchForItems(Human h) {
        gameBottomLeftVBox.getChildren().clear();
        gameCenterVBox.getChildren().clear();
        gameCenterVBox.getChildren().add(gameTopLabel);
        itemsGridPane.getChildren().clear();
        itemsGridPane.getColumnConstraints().clear();
        gameBtn1.setText("Back to map");
        gameBtn1.setOnMouseClicked(e -> {
            gameCenterVBox.getChildren().remove(itemsScrollPane);
            gameCenterVBox.getChildren().add(gameGrid);
            populateGameGrid();
            initGameBtns(h);
            gameTopLabel.setText("Anything else, %s?".formatted(h.getName()));
        });
        gameBottomLeftVBox.getChildren().add(gameBtn1);

        gameCenterVBox.getChildren().add(itemsScrollPane);
        itemsScrollPane.setContent(itemsGridPane);
        itemsScrollPane.getStyleClass().add("background");
        itemsScrollPane.setFitToWidth(true);
        ColumnConstraints cc1 = new ColumnConstraints();
        ColumnConstraints cc2 = new ColumnConstraints();
        ColumnConstraints cc3 = new ColumnConstraints();
        cc1.setPercentWidth(33);
        cc2.setPercentWidth(34);
        cc3.setPercentWidth(33);
        itemsGridPane.setVgap(50);
        itemsGridPane.setPadding(new Insets(25, 0, 25, 0));
        itemsGridPane.getColumnConstraints().addAll(cc1, cc2, cc3);
        int col = 0;
        int row = 0;
        for (Item i : Item.getItemsHere(h.getPosition())) {
            VBox vb = new VBox();
            Button b1 = new Button();
            if (i.getHealthEffect() > 0) b1.setText("Eat item");
            else b1.setText("Equip item");
            Button b2 = new Button("Store item");
            Label l = new Label(i.toString());
            b1.setOnMouseClicked(e -> eat_equipThis(h, i, "search"));
            b2.setOnMouseClicked(e -> {
                items.remove(i);
                h.setInventory(i);
                gameTopLabel.setText("%s stored a %s.".formatted(h.getName(), i.getName()));
                setBtnsForSearchForItems(h);
            });
            vb.getChildren().addAll(l, b1, b2);
            vb.getStyleClass().add("item");
            b1.getStyleClass().add("item-btn");
            b2.getStyleClass().add("item-btn");
            itemsGridPane.add(vb, col, row);
            if (col == 3) {
                col = 0;
                row++;
            } else col++;
        }
        if (itemsGridPane.getChildren().size() == 0) gameTopLabel.setText(gameTopLabel.getText() + "\nThere's nothing else here.");
        else gameTopLabel.setText("See anything you like?");
    }

    private static void eat_equipThis(Human h, Item i, String method) {
        items.remove(i);
        int healthEffect = i.getHealthEffect();
        String name = h.getName();
        if (healthEffect > 0) {
            h.eat(i);
            gameTopLabel.setText("%s's health increased by %s.".formatted(name, healthEffect));
        } else {
            int prevDmg = h.getDamage();
            int prevDef = h.getDefense();
            h.unequipItemLikeThis(i);
            if (!h.getInventory().contains(i)) {
                h.setInventory(i);
            }
            h.equipThisItem(i);
            int newDmg = h.getDamage();
            int newDef = h.getDefense();
            if (prevDmg > newDmg) {
                gameTopLabel.setText("%s's damage decreased by %s.".formatted(name, prevDmg - newDmg));
            } else if (prevDmg < newDmg) {
                gameTopLabel.setText("%s's damage increased by %s.".formatted(name, newDmg - prevDmg));
            } else if (prevDef > newDef) {
                gameTopLabel.setText("%s's defense decreased by %s.".formatted(name, prevDef - newDef));
            } else if (prevDef < newDef) {
                gameTopLabel.setText("%s's defense increased by %s.".formatted(name, newDef - prevDef));
            } else {
                gameTopLabel.setText("%s's stats did not change.".formatted(name));
            }
        }
        playerStats.setText(h.getStats());
        if (method.equals("inventory")) setBtnsForCheckInventory(h);
        else setBtnsForSearchForItems(h);
    }

    private static void unequipThis(Human h, Item i) {
        String name = h.getName();
        int prevDmg = h.getDamage();
        int prevDef = h.getDefense();
        h.unequipThisItem(i);
        int newDmg = h.getDamage();
        int newDef = h.getDefense();
        if (prevDmg > newDmg) {
            gameTopLabel.setText("%s's damage decreased by %s.".formatted(name, prevDmg - newDmg));
        } else if (prevDmg < newDmg) {
            gameTopLabel.setText("%s's damage increased by %s.".formatted(name, newDmg - prevDmg));
        } else if (prevDef > newDef) {
            gameTopLabel.setText("%s's defense decreased by %s.".formatted(name, prevDef - newDef));
        } else {
            gameTopLabel.setText("%s's defense increased by %s.".formatted(name, newDef - prevDef));
        }
        playerStats.setText(h.getStats());
        setBtnsForCheckInventory(h);
    }

    private static void setBtnsForChangeName(Human h) {
        gameBottomLeftVBox.getChildren().clear();
        TextField tf = new TextField();
        tf.setPromptText("Enter your new name");
        tf.setFocusTraversable(false);
        tf.getStyleClass().add("text-field");
        gameBtn1.setText("Confirm");
        gameBtn2.setText("Cancel");
        gameBtn1.setOnMouseClicked(e -> {
            String newName = tf.getText();
            if (newName.trim().length() == 0) {
                gameTopLabel.setText("Hm? Di'ja say something?");
                return;
            }
            if (newName.equals(h.getName())) {
                gameTopLabel.setText("That's already your name.");
                return;
            }
            h.setName(newName);
            playerName.setText(newName);
            gameTopLabel.setText("%s... cool!".formatted(newName));
            gameBottomLeftVBox.getChildren().removeAll(tf, gameBtn1);
            gameBtn2.setText("Back");
        });
        gameBtn2.setOnMouseClicked(e -> {
            initGameBtns(h);
            gameTopLabel.setText("Anything else, %s?".formatted(h.getName()));
        });
        gameBottomLeftVBox.getChildren().addAll(tf, gameBtn1, gameBtn2);
    }

    private static void handleEndTurn(Human h) {
        gameBottomLeftVBox.getChildren().clear();
        gameBottomLeftVBox.getChildren().add(gameBtn1);
        h.endTurn();
        if (goblins.size() == 0) {
            gameTopLabel.setText("We've defeated the goblins... now we rebuild.");
            gameBtn1.setText("End Game");
            gameBtn1.setOnMouseClicked(mouseEvent -> playAgain());
            return;
        }
        if (h.anotherHumanHere()) {
            Human.healHumansHere(h.getPosition());
            gameTopLabel.setText("We're stronger together!");
            playerStats.setText(h.getStats());
            gameBtn1.setText("Next");
            gameBtn1.setOnMouseClicked(e -> handleGoblinTurn());
        } else {
            handleGoblinTurn();
        }
    }

    public static void handleGoblinTurn() {
        gameBottomLeftVBox.getChildren().clear();
        Goblin g = goblins.get(0);
        playerName.setText(g.getName());
        playerStats.setText(g.getStats());
        Human movedTowardThisHuman = g.moveThisGoblin(humans);
        gameTopLabel.setText("%s moved towards %s...".formatted(g.getName(), movedTowardThisHuman.getName()));
        populateGameGrid();
        playerStats.setText(g.getStats());
        gameBottomLeftVBox.getChildren().add(gameBtn1);
        gameBtn1.setText("Next");
        gameBtn1.setOnMouseClicked(e -> {
            ArrayList<Human> humansHere = g.getHumansHere(humans);
            if (humansHere.size() == 0) {
                gameBtn1.setText("End Turn");
                g.pickUpItemsHere(items);
                populateGameGrid();
                if (g.anotherGoblinHere()) {
                    Goblin.healGoblinsHere(g.getPosition());
                    playerStats.setText(g.getStats());
                    gameTopLabel.setText("They're getting stronger...");
                }
                gameBtn1.setOnMouseClicked(ev -> {
                    g.endTurn();
                    Human next = humans.get(0);
                    initGameBtns(next);
                    playerName.setText(next.getName());
                    playerStats.setText(next.getStats());
                    gameTopLabel.setText(String.format("What's your move, %s?", next.getName()));
                });
                return;
            }
            boolean oneHuman = humansHere.size() == 1;
            if (oneHuman) {
                gameBtn1.setText("Battle");
                Human humanHere = humansHere.get(0);
                int humansInventorySize = humanHere.getInventory().size();
                int goblinInventorySize = g.getInventory().size();
                gameBtn1.setOnMouseClicked(ev -> {
                    g.fightTheHumansHere(humans, items);
                    if (humanHere.getHealth() == 0) {
                        gameTopLabel.setText("%s has defeated %s.".formatted(g.getName(), humanHere.getName()));
                        if (humansInventorySize > 0)
                            gameTopLabel.setText(gameTopLabel.getText() + "\nAnd it has taken all of their items");
                    } else {
                        gameTopLabel.setText("%s attacked %s and lost!".formatted(g.getName(), humanHere.getName()));
                        if (goblinInventorySize > 1)
                            gameTopLabel.setText(gameTopLabel.getText() + "\nAnd it dropped some items...");
                        if (goblinInventorySize == 1)
                            gameTopLabel.setText(gameTopLabel.getText() + "\nAnd it dropped something...");
                    }
                    playerStats.setText(g.getStats());
                    populateGameGrid();
                    if (g.getHealth() == 0) {
                        gameBtn1.setText("Next");
                        gameBtn1.setOnMouseClicked(event -> {
                            Human next = humans.get(0);
                            initGameBtns(next);
                            playerName.setText(next.getName());
                            playerStats.setText(next.getStats());
                            gameTopLabel.setText(String.format("What's your move, %s?", next.getName()));
                        });
                    } else {
                        gameBtn1.setText("End Turn");
                        gameBtn1.setOnMouseClicked(event -> {
                            if (humans.size() == 0) {
                                gameTopLabel.setText("The goblins now rule the Earth...");
                                gameBtn1.setText("End Game");
                                gameBtn1.setOnMouseClicked(mouseEvent -> playAgain());
                            } else {
                                g.endTurn();
                                Human next = humans.get(0);
                                initGameBtns(next);
                                playerName.setText(next.getName());
                                playerStats.setText(next.getStats());
                                gameTopLabel.setText(String.format("What's your move, %s?", next.getName()));
                            }
                        });
                    }
                });
            } else {
                gameBtn1.setText("Ambush");
                gameBtn1.setOnMouseClicked(ev -> {
                    boolean hadAtLeastOneItem = Human.hadItems(humansHere);
                    g.fightTheHumansHere(humans, items);
                    StringBuilder listOfNames = new StringBuilder();
                    for (int i = 0; i < humansHere.size(); i++) {
                        if (i == 0) {
                            listOfNames.append(humansHere.get(0));
                        } else if (i == humansHere.size() - 1 && i == 1) {
                            listOfNames.append(" and ").append(humansHere.get(1));
                        } else if (i == humansHere.size() - 1) {
                            listOfNames.append(", and ").append(humansHere.get(i));
                        } else {
                            listOfNames.append(", ").append(humansHere.get(i));
                        }
                    }
                    gameTopLabel.setText("%s ambushed %s.".formatted(g.getName(), listOfNames.toString()));
                    if (hadAtLeastOneItem)
                        gameTopLabel.setText(gameTopLabel.getText() + "\nIt took all of their items. Dammit!\"");
                    playerStats.setText(g.getStats());
                    populateGameGrid();
                    gameBtn1.setText("End Turn");
                    gameBtn1.setOnMouseClicked(event -> {
                        g.endTurn();
                        if (humans.size() == 0) {
                            gameTopLabel.setText("The goblins now rule the Earth...");
                            gameBtn1.setText("End Game");
                            gameBtn1.setOnMouseClicked(mouseEvent -> playAgain());
                        } else {
                            Human next = humans.get(0);
                            initGameBtns(next);
                            playerName.setText(next.getName());
                            playerStats.setText(next.getStats());
                            gameTopLabel.setText(String.format("What's your move, %s?", next.getName()));
                        }
                    });
                });
            }
        });
    }

    public static void playAgain() {
        gameTopLeftVBox.getChildren().clear();
        BorderPane bp = new BorderPane();
        Label label = new Label("Would you like to play again?");
        leftBtn.setText("Yes");
        rightBtn.setText("No");
        leftBtn.setOnMouseClicked(e -> configGame());
        rightBtn.setOnMouseClicked(e -> System.exit(0));
        VBox vbox = new VBox();
        vbox.getChildren().add(label);
        HBox hbox = new HBox();
        hbox.getChildren().addAll(leftBtn, rightBtn);
        hbox.setPadding(new Insets(25, 0, 0, 0));
        hbox.setSpacing(25);
        hbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(hbox);
        vbox.setAlignment(Pos.CENTER);
        bp.setCenter(vbox);
        Scene scene = new Scene(bp, 1200, 600);
        scene.getStylesheets().add(Objects.requireNonNull(HvGApplication.class.getResource("/styles/stylesheet.css")).toExternalForm());
        bp.getStyleClass().add("background");
        label.getStyleClass().add("label");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        HvGApplication.stage = stage;
        startScreen();
    }
}