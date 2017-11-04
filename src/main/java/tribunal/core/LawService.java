package tribunal.core;

import tribunal.tool.Logger;
import tribunal.tool.Scanner;
import tribunal.tool.StringColor;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LawService {


    private Logger log = new Logger(this.getClass().getName());
    private LinkedList<Law> allLawList;
    private File[] jars;
    private static LawService instance;


    public LawService(){
        this.allLawList = new LinkedList<>();
        this.jars = Scanner.getInstance().getJars();
    }


    public static LawService getInstance() {
        if (instance == null)
            instance = new LawService();
        return instance;
    }


    public void registryLaws() {
        for(File jar: jars) {
            try {
                List<Class<?>> plugins = Scanner.getInstance().scanPlugins(jar.getName(), LawManager.class);
                for (Class<?> plugin : plugins) {
                    log.debugln("Invoke method of : " + plugin.getName() + ".loadLows()");
                    Object inst = plugin.newInstance();
                    LinkedList<Law> result = ((LawManager) inst).loadLaws();
                    this.allLawList.addAll(result);
                }
            } catch (Exception e) {
                log.debug("Lows not found", StringColor.RED);
            }
        }
    }


    public List containsLaw(String context) {
        LinkedList<Object> result = null;
        for(Law rule: this.allLawList){
            result = new LinkedList<>();
            String regex = rule.getLawRegex();
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(context);
            if (m.find()){
                result.add(rule.getClassName());
                for(int i=1; i<=m.groupCount(); i++)
                    result.add(m.group(i));
                return result;
            }
            else{
                result.add("NotFound");
            }
        }
        return result;
    }


    public List containsLaw(String context, String executor) {
        LinkedList<Object> result = null;
        for (Law rule : this.allLawList) {
            result = new LinkedList<>();
            String regex = rule.getLawRegex();
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(context);
            if (m.find()) {
                for(Object executable: rule.getExecutorIds()) {
                    if (executor.equals(executable.toString()) || executable.toString().equals("anyone")){
                        result.add(rule.getClassName());
                        for (int i = 1; i<=m.groupCount(); i++)
                            result.add(m.group(i));
                        return result;
                    }
                }
                result.add("PermissionError");
                return result;
            } else {
                result.add("NotFound");
            }
        }
        return result;
    }
}
