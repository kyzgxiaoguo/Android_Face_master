package com.hg.orcdiscern.http.resultbean;

/**
 * @author Zhangzhenguo
 * @create 2019/10/25
 * @Email 18311371235@163.com
 * @Describe
 */
public class IDPosBean {

    /**
     * type : 第二代身份证
     * birthday : 1992年08月22日
     * id_number : 44182718
     * address : 广东省清新县浸潭镇鸡见坑村委会下围村2号
     * sex : 男
     * people : 汉
     * head_portrait : {"image":"/9j/4AAQSkZJgKDBQNDAsLDBkSEw8UTgyPC4zNDL/"}
     * id_number_image : /9j/4AAQSkZJRgABAQEAYABgAADL/
     * border_covered : false
     * head_covered : false
     * head_blurred : false
     * name : 黄森林
     * cropped_image : 12321
     * complete : true
     * gray_image : false
     * time_cost : {"preprocess":295,"recognize":290}
     * error_code : 0
     * error_msg : ok
     */

    private String type;
    private String birthday;
    private String id_number;
    private String address;
    private String sex;
    private String people;
    private HeadPortraitBean head_portrait;
    private String id_number_image;
    private boolean border_covered;
    private boolean head_covered;
    private boolean head_blurred;
    private String name;
    private String cropped_image;
    private boolean complete;
    private boolean gray_image;
    private TimeCostBean time_cost;
    private String error_code;
    private String error_msg;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public HeadPortraitBean getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(HeadPortraitBean head_portrait) {
        this.head_portrait = head_portrait;
    }

    public String getId_number_image() {
        return id_number_image;
    }

    public void setId_number_image(String id_number_image) {
        this.id_number_image = id_number_image;
    }

    public boolean isBorder_covered() {
        return border_covered;
    }

    public void setBorder_covered(boolean border_covered) {
        this.border_covered = border_covered;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCropped_image() {
        return cropped_image;
    }

    public void setCropped_image(String cropped_image) {
        this.cropped_image = cropped_image;
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

    public TimeCostBean getTime_cost() {
        return time_cost;
    }

    public void setTime_cost(TimeCostBean time_cost) {
        this.time_cost = time_cost;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public static class HeadPortraitBean {
        /**
         * image : /9j/4AAQSkZJgKDBQNDAsLDBkSEw8UTgyPC4zNDL/
         */

        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public static class TimeCostBean {
        /**
         * preprocess : 295
         * recognize : 290
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
