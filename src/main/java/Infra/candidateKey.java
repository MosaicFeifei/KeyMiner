package Infra;

public class candidateKey {

    private String predicate;
    private String object;

    public candidateKey(String pred, String obj)
    {
        this.predicate=pred;
        this.object=obj;
    }

    public String getPredicate()
    {
        return predicate;
    }

    public String getObject()
    {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }
}
