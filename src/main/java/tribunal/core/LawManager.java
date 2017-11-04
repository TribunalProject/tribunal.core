package tribunal.core;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import tribunal.tool.Logger;

import java.util.Iterator;
import java.util.LinkedList;


public class LawManager {


    private Logger log = new Logger(this.getClass().getName());
    private LinkedList<Law> pluginLawList;


    public LawManager(){
        this.pluginLawList = new LinkedList<>();
    }


    public LinkedList<Law> loadLaws() throws Exception {
        SAXReader reader = new SAXReader();
        String xmlDir = System.getProperty("user.dir") + "/laws/" + this.getClass().getPackage().getName() + ".xml";
        log.debugln("Laws Load from : " + xmlDir);
        Document document = reader.read(xmlDir);
        Element root = document.getRootElement();
        for (Iterator i = root.elementIterator(); i.hasNext();) {
            Law rule = new Law();
            log.debugln("========== Load data ==========");
            Element element = (Element) i.next();
            rule.setLawRegex(element.elementText("regex"));
            log.debugln("Regex : " + rule.getLawRegex());
            rule.setClassName(element.elementText("class"));
            log.debugln("Class : " + rule.getClassName());
            LinkedList executors = new LinkedList<>();
            for(Object e: element.element("executors").elements("executor")){
                executors.add(((Element) e).getStringValue());
                log.debugln("Executor : " + ((Element) e).getStringValue());
            }
            rule.setExecutorIds(executors);
            this.pluginLawList.add(rule);
            log.debugln("=================================");
        }
        log.debugln("Laws load finished.");
        return this.pluginLawList;
    }


    public LinkedList<Law> getLaws(){
        return this.pluginLawList;
    }
}
