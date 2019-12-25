package com.hg.orcdiscern.http.resultbean;

/**
 * @author Zhangzhenguo
 * @create 2019/10/25
 * @Email 18311371235@163.com
 * @Describe
 */
public class IDSide {

    /**
     * type : 第二代身份证背面
     * issue_authority : 崇明县公安局
     * head_covered : false
     * head_blurred : false
     * error_msg : OK
     * error_code : 0
     * validity : 2014.02.19-2034.02.19
     * cropped_image : /9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAIBAQEBAQIBAQECAgICAgQDAgICAgUEBAMEBgUGBgYFBgYGBwkIBgcJBwYGCAsICQoKCgoKBggLDAsKDAkKCgr/2wBDAQICAgICAgUDAwUKBwY上海合合信息科技发展有限公司 保密HCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgr/wAARCAEsAdQDASIAAhEBAxEB/8QAHwAAAQUB…..
     * time_cost : {"preprocess":178,"recognize":153}
     * complete : true
     * gray_image : false
     * border_covered : false
     */

    private String type;
    private String issue_authority;
    private boolean head_covered;
    private boolean head_blurred;
    private String error_msg;
    private String error_code;
    private String validity;
    private String cropped_image;
    private TimeCostBean time_cost;
    private boolean complete;
    private boolean gray_image;
    private boolean border_covered;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIssue_authority() {
        return issue_authority;
    }

    public void setIssue_authority(String issue_authority) {
        this.issue_authority = issue_authority;
    }

    public boolean isHead_covered() {
        return head_covered;
    }

    public void setHead_covered(boolean head_covered) {
        this.head_covered = head_covered;
    }

    public boolean isHead_blurred() {
        return head_blurred;
    }

    public void setHead_blurred(boolean head_blurred) {
        this.head_blurred = head_blurred;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getCropped_image() {
        return cropped_image;
    }

    public void setCropped_image(String cropped_image) {
        this.cropped_image = cropped_image;
    }

    public TimeCostBean getTime_cost() {
        return time_cost;
    }

    public void setTime_cost(TimeCostBean time_cost) {
        this.time_cost = time_cost;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isGray_image() {
        return gray_image;
    }

    public void setGray_image(boolean gray_image) {
        this.gray_image = gray_image;
    }

    public boolean isBorder_covered() {
        return border_covered;
    }

    public void setBorder_covered(boolean border_covered) {
        this.border_covered = border_covered;
    }

    public static class TimeCostBean {
        /**
         * preprocess : 178
         * recognize : 153
         */

        private int preprocess;
        private int recognize;

        public int getPreprocess() {
            return preprocess;
        }

        public void setPreprocess(int preprocess) {
            this.preprocess = preprocess;
        }

        public int getRecognize() {
            return recognize;
        }

        public void setRecognize(int recognize) {
            this.recognize = recognize;
        }
    }
}
