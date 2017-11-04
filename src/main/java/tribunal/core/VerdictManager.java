package tribunal.core;


import tribunal.tool.Logger;
import tribunal.tool.Scanner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * プラグインの管理を行う
 */
public class VerdictManager {

    private Logger log = new Logger(this.getClass().getName()); // ロガー
    private Map<String, Object> pluginMap = new HashMap<>();    // Verdictインスタンス保存用


    public void init(String jarName) throws Exception {
        List<Class<?>> plugins = Scanner.getInstance().scanPlugins(jarName, Verdict.class);
        for(Class<?> plugin: plugins){
            String className = plugin.getName();
            Object instance = plugin.newInstance();
            pluginMap.put(className, instance);

            log.debugln("Invoke method of : " + plugin.getName() + ".init()");
            ((Verdict) instance).init();
        }
    }


    public void start() throws Exception {

    }


    public Object call(String className, Object[] args) throws Exception {
        Object result = null;
        Object inst = pluginMap.get(className);
        log.debugln("Invoke method of : " + className + ".call()");
        for(Object arg: args)
            log.debugln("Argument : " + arg);
        result = ((Verdict) inst).call(args);
        return result;
    }
}
