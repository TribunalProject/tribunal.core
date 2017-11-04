package tribunal.tool;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Scanner {

    private Logger log = new Logger(this.getClass().getName());
    private static Scanner instance;
    private File[] jars;


    public static Scanner getInstance(){
        if(instance == null)
            return new Scanner();
        return instance;
    }


    /**
     * Jarを探す
     *
     * @return
     */
    public File[] getJars() {
        if(jars == null) {

            log.debugln("Scan start : " + System.getProperty("user.dir") + "/modules");
            File jarDir = new File(System.getProperty("user.dir") + "/modules");

            jars = jarDir.listFiles(pathname -> pathname.getName().endsWith(".jar"));

            if (jars == null) {
                log.debugln("Plugin is not found.");
                System.exit(1);
            }

            for (File jar : jars)
                log.debugln("Find => " + jar.getName());
        }
        return jars;
    }

    /**
     * Jarの中から特定のクラスを探し出す
     * @param jarName
     * @param searchTarget
     * @return
     */
    public List<Class<?>> scanPlugins(String jarName, Class searchTarget) {
        List<Class<?>> plugins = null;
        try {
            File pluginDir = new File(System.getProperty("user.dir") + "/modules/" + jarName);
            log.debugln("Load success : " + pluginDir.getName());
            plugins = loadClassesInJar(pluginDir.toString(), searchTarget);
        } catch (Exception e) {
            log.error(e);
        }
        return plugins;
    }


    private List<Class<?>> loadClassesInJar(String jarPath, Class<?> i) throws IOException {
        URL jarUrl = new File(jarPath).toURI().toURL();
        URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{jarUrl});
        List<Class<?>> result = new ArrayList<Class<?>>();
        JarFile jarFile = new JarFile(jarPath);

        for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ){
            // ファイル要素に限って（＝ディレクトリをはじいて）スキャン
            JarEntry jarEntry = entries.nextElement();
            if (jarEntry.isDirectory())
                continue;

            // classファイルに限定
            String fileName = jarEntry.getName();
            if (!fileName.endsWith(".class"))
                continue;

            // classファイルをクラスとしてロード。
            Class<?> clazz;
            try {
                clazz = urlClassLoader.loadClass(fileName.substring(0, fileName.length() - 6).replace('/', '.'));
            } catch (ClassNotFoundException e) {
                continue;
            }

            // iの派生型であることを確認
            if (!i.isAssignableFrom(clazz))
                continue;

            // 引数なしコンストラクタを持つことを確認
            try {
                clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                continue;
            }

            result.add((Class<?>) clazz);
        }
        return result;
    }
}
