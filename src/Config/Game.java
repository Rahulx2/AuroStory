/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import java.util.ArrayList;
import java.util.List;
import handling.login.Balloon;

/**
 *
 * @author AbbelGamer
 */
public class Game {
    // Custom Balloon Tips on the Login Screen
    public static final List<Balloon> lBalloon = new ArrayList<>();

    public static List<Balloon> getBalloons() {
        // Point 0,0 Coordinates: 232, 107
        // Point 96,0 Coordinates: 328, 107
        if (lBalloon.isEmpty()) {
            lBalloon.add(new Balloon("Bienvenidos a MapleStory Auro!", 236, 122));
            lBalloon.add(new Balloon("Welcome to AuroStory!", 0, 276));
            lBalloon.add(new Balloon("Bem-vido ao AuroStory!", 196, 263));
        }
        return lBalloon;
    }   
}
