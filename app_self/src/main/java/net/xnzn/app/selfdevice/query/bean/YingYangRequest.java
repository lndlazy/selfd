package net.xnzn.app.selfdevice.query.bean;


/**
 * 查询范围(1-当日查询,2-当周查询)
 */
public class YingYangRequest {

    private int analyseScope = 1;
    private String custId;

    public int getAnalyseScope() {
        return analyseScope;
    }

    public void setAnalyseScope(int analyseScope) {
        this.analyseScope = analyseScope;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }
}
