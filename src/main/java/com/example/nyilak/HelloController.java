package com.example.nyilak;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class HelloController {
    @FXML private Pane pnJatek;
    @FXML private Button gomb;

    Label[][] lt = new Label[6][10];
    int[][] t = new int[6][10];

    String[] iconsN = { "left", "right", "up", "down", "null" };
    Image[] icons = new Image[6];
    DatagramSocket socket = null;

    Thread thread = null;

    //private Image left = "L";

    @FXML public void initialize(){
        for(int i = 0;i<4;i++) icons[i] = new Image(getClass().getResourceAsStream(iconsN[i]+".png"));
        for(int s = 0;s<6;s++){
            for(int o = 0;o<10;o++){
                lt[s][o] = new Label("");
                lt[s][o].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("null.png"))));
                lt[s][o].setTranslateX(10+o*48);
                lt[s][o].setTranslateY(10+s*48);
                lt[s][o].setStyle("-fx-border-color: lightgrey");
                pnJatek.getChildren().add(lt[s][o]);
            }
        }
        try {
            socket = new DatagramSocket(678);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                fogad();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
    int db = 0;
    int dbX = 0;
    String sor = "";
    @FXML private void onBalPressed(){
        if(db!=10){
            setKep(0, dbX, db);
            db++;
            gomb.setDisable(true);
            sor+="L";
        }
        else{gomb.setDisable(false);}
    }
    @FXML private void onJobbPressed(){
        if(db!=10){
            setKep(1, dbX, db);
            db++;
            gomb.setDisable(true);
            sor+="R";
        }
        else{gomb.setDisable(false);}
    }
    @FXML private void onFelPressed(){
        if(db!=10){
            setKep(2, dbX, db);
            db++;
            gomb.setDisable(true);
            sor+="U";
        }
        else{gomb.setDisable(false);}
    }
    @FXML private void onLePressed(){
        if(db!=10){
            setKep(3, dbX, db);
            db++;
            gomb.setDisable(true);
            sor+="D";
        }
        else{gomb.setDisable(false);}
    }

    @FXML private void onTorolPressed(){
        for(int s = 0;s<6;s++){
            for(int o = 0;o<10;o++){
                lt[s][o].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("null.png"))));
                lt[s][o].setStyle("-fx-border-color: white; -fx-border-color: lightgrey");
            }
        }
        db = 0;
        dbX = 0;
    }




    private void fogad() { // Külön szálon!
        byte[] adat = new byte[256];
        DatagramPacket packet = new DatagramPacket(adat, adat.length);
        while (true) {
            try {
                socket.receive(packet);
                String uzenet = new String(adat, 0, packet.getLength(), "utf-8");
                String ip = packet.getAddress().getHostAddress();
                int port = packet.getPort();
                Platform.runLater(() -> onFogad(uzenet, ip, port));
            } catch (Exception e) { e.printStackTrace(); }
        }

    }

    private void onFogad(String uzenet, String ip, int port) {
        System.out.printf("%s", uzenet);
        int k = 0;
        String[] s = uzenet.split(";");
        int pont = Integer.parseInt(s[0]);
        int proba = Integer.parseInt(s[1]);
        String megoldas = s[2];
        for(int i=0;i<megoldas.length();i++){
            if(megoldas.charAt(i) == '1') lt[dbX][i].setStyle("-fx-background-color: green");
        }
        dbX++;
    }

    private void kuld(String uzenet, String ip, int port) {
        try {
            byte[] adat = uzenet.getBytes("UTF-8");
            InetAddress ipv4 = Inet4Address.getByName(ip);
            DatagramPacket packet = new DatagramPacket(adat, adat.length, ipv4, port);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML private void onKuldClick(){
        if(dbX!=6){

            db=0;
        }
        kuld(sor, "10.201.2.19", 678);

        System.out.printf("%s ", sor);
        sor = "";
    }
    private void setKep(int id, int s, int o){
        t[s][o] = id;
        lt[s][o].setGraphic(new ImageView(icons[id]));
    }
}