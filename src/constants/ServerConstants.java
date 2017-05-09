/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation version 3 as published by
the Free Software Foundation. You may not use, modify or distribute
this program under any other version of the GNU Affero General Public
License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package constants;

public class ServerConstants {
    // Rate Configuration

    public static int EXP_RATE = 166;
    public static int MESO_RATE = 100;
    public static final byte DROP_RATE = 10;
    public static final byte BOSS_DROP_RATE = 3;
    // Login Configuration
    public static final byte NUM_WORLDS = 1;
    public static final byte FLAG = 3;
    public static final int CHANNEL_NUMBER = 6;
    public static final int CHANNEL_LOAD = 150;
    public static final String EVENT_MESSAGE = "Welcome To the new and Improved ImprovedStoryv83";
//    public static final String EVENT_MESSAGE = "";
    public static final long RANKING_INTERVAL = 3600000;
    public static final boolean IS_TEST = false;
    public static final boolean enableCooldowns = false;
    // Channel Configuration
    public static String SERVER_MESSAGE = "Welcome to ImprovedStoryv83";
    public static final String EVENTS = "OmegaPQ TTPQ automsg"; //automsg KerningPQ Boats Subway OmegaPQ TTPQ
    // IP Configuration
    public static String HOST = "24.211.97.25"; //217.23.15.242 -dedi //89.46.37.163 -vps //62.30.71.63 -home
    // Debug Configuration
    public static final boolean DEBUG = false;
    // Database Configuration
    public static String url = "jdbc:mysql://localhost:3306/shootsouce?autoReconnect=true";
    public static String user = "root";
    public static String password = "";
}
