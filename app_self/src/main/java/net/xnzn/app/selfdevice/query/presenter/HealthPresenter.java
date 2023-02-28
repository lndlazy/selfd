package net.xnzn.app.selfdevice.query.presenter;

import net.xnzn.app.selfdevice.query.view.HealthView;

public class HealthPresenter {

    private HealthView healthView;

    public HealthPresenter(HealthView healthView) {
        this.healthView = healthView;

    }

    public class Request {
        private int current = 1;
        private Long custId;
        private String queryDate;
        private int size = 100;

        public Request(int current, Long custId, String queryDate, int size) {
            this.current = current;
            this.custId = custId;
            this.queryDate = queryDate;
            this.size = size;
        }
    }


    public void queryHealthRecords(int current, Long custId, String queryDate, int size) {

        Request request = new Request(current, custId, queryDate, size);


    }
}