package tribunal.core;

import java.util.List;

public class Law {


    private String LawRegex;
    private String className;
    private List ExecutorIds;


    public String getLawRegex() {
        return LawRegex;
    }

    public void setLawRegex(String LawRegex) {
        this.LawRegex = LawRegex;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List getExecutorIds() {
        return ExecutorIds;
    }

    public void setExecutorIds(List executorIds) {
        ExecutorIds = executorIds;
    }
}
