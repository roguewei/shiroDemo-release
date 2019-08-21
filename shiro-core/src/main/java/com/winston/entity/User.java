package com.winston.entity;

import java.io.Serializable;

public class User implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.username
     *
     * @mbg.generated
     */
    private String username;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.password
     *
     * @mbg.generated
     */
    private String password;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.rel_name
     *
     * @mbg.generated
     */
    private String relName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.sex
     *
     * @mbg.generated
     */
    private String sex;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.open_id
     *
     * @mbg.generated
     */
    private String openId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.openid_hex
     *
     * @mbg.generated
     */
    private String openidHex;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.nick_name
     *
     * @mbg.generated
     */
    private String nickName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.towns_id
     *
     * @mbg.generated
     */
    private Integer townsId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.mobile
     *
     * @mbg.generated
     */
    private String mobile;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.enable
     *
     * @mbg.generated
     */
    private Integer enable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.operator_type
     *
     * @mbg.generated
     */
    private String operatorType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.state
     *
     * @mbg.generated
     */
    private String state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.create_time
     *
     * @mbg.generated
     */
    private Long createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.create_opr
     *
     * @mbg.generated
     */
    private String createOpr;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.update_time
     *
     * @mbg.generated
     */
    private Long updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.update_opr
     *
     * @mbg.generated
     */
    private String updateOpr;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.last_logon
     *
     * @mbg.generated
     */
    private Long lastLogon;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table user
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.id
     *
     * @return the value of user.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.id
     *
     * @param id the value for user.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.username
     *
     * @return the value of user.username
     *
     * @mbg.generated
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.username
     *
     * @param username the value for user.username
     *
     * @mbg.generated
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.password
     *
     * @return the value of user.password
     *
     * @mbg.generated
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.password
     *
     * @param password the value for user.password
     *
     * @mbg.generated
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.rel_name
     *
     * @return the value of user.rel_name
     *
     * @mbg.generated
     */
    public String getRelName() {
        return relName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.rel_name
     *
     * @param relName the value for user.rel_name
     *
     * @mbg.generated
     */
    public void setRelName(String relName) {
        this.relName = relName == null ? null : relName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.sex
     *
     * @return the value of user.sex
     *
     * @mbg.generated
     */
    public String getSex() {
        return sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.sex
     *
     * @param sex the value for user.sex
     *
     * @mbg.generated
     */
    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.open_id
     *
     * @return the value of user.open_id
     *
     * @mbg.generated
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.open_id
     *
     * @param openId the value for user.open_id
     *
     * @mbg.generated
     */
    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.openid_hex
     *
     * @return the value of user.openid_hex
     *
     * @mbg.generated
     */
    public String getOpenidHex() {
        return openidHex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.openid_hex
     *
     * @param openidHex the value for user.openid_hex
     *
     * @mbg.generated
     */
    public void setOpenidHex(String openidHex) {
        this.openidHex = openidHex == null ? null : openidHex.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.nick_name
     *
     * @return the value of user.nick_name
     *
     * @mbg.generated
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.nick_name
     *
     * @param nickName the value for user.nick_name
     *
     * @mbg.generated
     */
    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.towns_id
     *
     * @return the value of user.towns_id
     *
     * @mbg.generated
     */
    public Integer getTownsId() {
        return townsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.towns_id
     *
     * @param townsId the value for user.towns_id
     *
     * @mbg.generated
     */
    public void setTownsId(Integer townsId) {
        this.townsId = townsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.mobile
     *
     * @return the value of user.mobile
     *
     * @mbg.generated
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.mobile
     *
     * @param mobile the value for user.mobile
     *
     * @mbg.generated
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.enable
     *
     * @return the value of user.enable
     *
     * @mbg.generated
     */
    public Integer getEnable() {
        return enable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.enable
     *
     * @param enable the value for user.enable
     *
     * @mbg.generated
     */
    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.operator_type
     *
     * @return the value of user.operator_type
     *
     * @mbg.generated
     */
    public String getOperatorType() {
        return operatorType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.operator_type
     *
     * @param operatorType the value for user.operator_type
     *
     * @mbg.generated
     */
    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType == null ? null : operatorType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.state
     *
     * @return the value of user.state
     *
     * @mbg.generated
     */
    public String getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.state
     *
     * @param state the value for user.state
     *
     * @mbg.generated
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.create_time
     *
     * @return the value of user.create_time
     *
     * @mbg.generated
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.create_time
     *
     * @param createTime the value for user.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.create_opr
     *
     * @return the value of user.create_opr
     *
     * @mbg.generated
     */
    public String getCreateOpr() {
        return createOpr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.create_opr
     *
     * @param createOpr the value for user.create_opr
     *
     * @mbg.generated
     */
    public void setCreateOpr(String createOpr) {
        this.createOpr = createOpr == null ? null : createOpr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.update_time
     *
     * @return the value of user.update_time
     *
     * @mbg.generated
     */
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.update_time
     *
     * @param updateTime the value for user.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.update_opr
     *
     * @return the value of user.update_opr
     *
     * @mbg.generated
     */
    public String getUpdateOpr() {
        return updateOpr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.update_opr
     *
     * @param updateOpr the value for user.update_opr
     *
     * @mbg.generated
     */
    public void setUpdateOpr(String updateOpr) {
        this.updateOpr = updateOpr == null ? null : updateOpr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.last_logon
     *
     * @return the value of user.last_logon
     *
     * @mbg.generated
     */
    public Long getLastLogon() {
        return lastLogon;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.last_logon
     *
     * @param lastLogon the value for user.last_logon
     *
     * @mbg.generated
     */
    public void setLastLogon(Long lastLogon) {
        this.lastLogon = lastLogon;
    }
}