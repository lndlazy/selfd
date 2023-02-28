package net.xnzn.app.selfdevice.query.bean;


/**
 * 查询范围(1-当日查询,2-当周查询)
 */
public class YingYangRequest {

    private int analyseScope = 1;
    private Long custId;

    public int getAnalyseScope() {
        return analyseScope;
    }

    public void setAnalyseScope(int analyseScope) {
        this.analyseScope = analyseScope;
    }


    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }
}
