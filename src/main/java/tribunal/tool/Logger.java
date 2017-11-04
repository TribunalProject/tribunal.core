package tribunal.tool;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    String packageName;

    public Logger(String packageName) {
        this.packageName = packageName;
    }


    public void print(String message) {
        System.out.print(StringColor.WHITE + message + StringColor.WHITE);
    }

    public void print(String message, String color) {
        System.out.print(color + message + StringColor.WHITE);
    }


    public void println(String message) {
        System.out.println(StringColor.WHITE + message + StringColor.WHITE);
    }

    public void println(String message, String color) {
        if (color == null)
            color = "";
        System.out.println(color + message + StringColor.WHITE);
    }


    public void debug(String message) {
        this.debug(message, null);
    }

    public void debug(String message, String color) {
        if (color == null)
            color = "";
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String date = d.format(new Date());
        System.out.print(color + "[" + date + "] <" + packageName + "> " + message + StringColor.WHITE);
    }


    public void debugln(String message) {
        this.debugln(message, null);
    }

    public void debugln(String message, String color) {
        if (color == null)
            color = "";
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String date = d.format(new Date());
        System.out.println(color + "[" + date + "] <" + packageName + "> " + message + StringColor.WHITE);
    }

    public void reset() {
        this.print(StringColor.WHITE);
    }

    public void error(String message, Exception e) {
        this.debugln(message, StringColor.RED);
        this.debug("", StringColor.RED);
        e.printStackTrace();
        this.print("");
    }

    public void error(Exception e) {
        this.debug("", StringColor.RED);
        e.printStackTrace();
        this.print("");
    }

}
