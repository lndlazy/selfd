package net.xnzn.app.selfdevice.query.bean;

import java.io.Serializable;

public class OrderListRequest implements Serializable {

    private Page page;
    private object object;

    public OrderListRequest(int type, int currentPage, int pageSize) {
        this.page = new Page(pageSize, currentPage);
        this.object = new object(type);
    }

    public OrderListRequest(int type, String searchDate, int currentPage, int pageSize) {
        this.page = new Page(pageSize, currentPage);
        this.object = new object(type, searchDate);
    }


    public class object implements Serializable {
        private int type;
        private String searchDate;

        public object(int type) {
            this.type = type;
        }

        public object(int type, String searchDate) {
            this.type = type;
            this.searchDate = searchDate;
        }
    }

    public class Page implements Serializable {
        private int size;
        private int current;

        public Page(int size, int current) {
            this.size = size;
            this.current = current;
        }
    }


}
