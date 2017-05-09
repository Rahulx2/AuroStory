package Config.Logs;

import java.io.*;
import java.util.*;
import tools.MaplePacketCreator;

/**
 *
 * @author Soulfist
 */

public class IPLog {
    
    BufferedWriter w = null;
    BufferedReader b = null;
    private static IPLog instance = null;
    final List<String> ip = new ArrayList<String>();
    
    private IPLog() {
        try {
            w = new BufferedWriter(new FileWriter("IPLog.txt", true));
            b = new BufferedReader(new FileReader("IPLog.txt"));
            w.append("\n\n\n\n");
        } catch (IOException i) { i.printStackTrace(System.out); }
    }
    
   public static IPLog getInstance() { //works together with disable()
        if (instance == null)
            instance = new IPLog();
            return instance;
    }
    
    public List<String> getIP() {
        return ip;
    }
    
    public String generateTime() {
        return new Date().toString(); //deprecated class ftw
    }
    
    public void disable() {
        try {
            if (w != null) w.close();
            if (b != null) b.close();
            instance = null;
        } catch (IOException io) {
            io.printStackTrace(System.out);
        }
    }   
    
    public void makeLog() {
        synchronized (w) {
            try {
                for (int i = 0; i < ip.size(); i++) {
                    w.newLine();
                    w.append(ip.get(i));
                }
            } catch (IOException io) {
                io.printStackTrace(System.out);
            }
            disable();
        }
        ip.clear();
    }
    
    public synchronized void add(String a) { //constantly adding
        ip.add(a);
    }
}  