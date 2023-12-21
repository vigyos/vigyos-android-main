package com.vigyos.vigyoscentercrm.Model;

import java.util.List;
import java.util.Map;

public class ResponseRecharge {

    private boolean success;
    private Operator operator_id;
    private Circle circle;
    private Plans plans;

    // getters and setters


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Operator getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(Operator operator_id) {
        this.operator_id = operator_id;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Plans getPlans() {
        return plans;
    }

    public void setPlans(Plans plans) {
        this.plans = plans;
    }

    public static class Operator {
        private String id;
        private String name;
        private String category;

        // getters and setters

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    public static class Circle {
        private String operator;
        private String circle;

        // getters and setters

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getCircle() {
            return circle;
        }

        public void setCircle(String circle) {
            this.circle = circle;
        }
    }

    public static class Plans {
        private boolean status;
        private int response_code;
        private Map<String, List<Plan>> info;

        // getters and setters

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public int getResponse_code() {
            return response_code;
        }

        public void setResponse_code(int response_code) {
            this.response_code = response_code;
        }

        public Map<String, List<Plan>> getInfo() {
            return info;
        }

        public void setInfo(Map<String, List<Plan>> info) {
            this.info = info;
        }
    }

    public static class Plan {
        private String rs;
        private String desc;
        private String validity;
        private String last_update;

        // getters and setters
        public String getRs() {
            return rs;
        }

        public void setRs(String rs) {
            this.rs = rs;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getValidity() {
            return validity;
        }

        public void setValidity(String validity) {
            this.validity = validity;
        }

        public String getLast_update() {
            return last_update;
        }

        public void setLast_update(String last_update) {
            this.last_update = last_update;
        }
    }
}
