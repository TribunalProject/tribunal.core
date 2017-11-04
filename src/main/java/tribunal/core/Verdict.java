package tribunal.core;


public interface Verdict {

    void init();

    Object call(Object[] args);
}
