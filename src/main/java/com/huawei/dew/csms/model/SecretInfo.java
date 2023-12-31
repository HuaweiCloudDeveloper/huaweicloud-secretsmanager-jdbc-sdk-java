package com.huawei.dew.csms.model;

import java.io.Serializable;

public class SecretInfo implements Serializable, Cloneable {

    private String id;

    private String name;

    private String versionId;

    private String value;

    private String type;

    private Long createTime;

    private Boolean autoRotation;

    private String rotationConfig;

    private String rotationPeriod;

    private Long lastRotationTime;

    private String enterPriseProjectId;

    private Short freezeFlag;

    public SecretInfo() {
    }

    public SecretInfo(String id, String name, String versionId, String value, String type, Long createTime, Boolean autoRotation, String rotationConfig, String rotationPeriod, Long lastRotationTime) {
        this.id = id;
        this.name = name;
        this.versionId = versionId;
        this.value = value;
        this.type = type;
        this.createTime = createTime;
        this.autoRotation = autoRotation;
        this.rotationConfig = rotationConfig;
        this.rotationPeriod = rotationPeriod;
        this.lastRotationTime = lastRotationTime;
    }


    @Override
    public SecretInfo clone() throws CloneNotSupportedException {
        return (SecretInfo) super.clone();
    }

    @Override
    public String toString() {
        return "credentialInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", versionId='" + versionId + '\'' +
                ", type='" + type + '\'' +
                ", createTime='" + createTime + '\'' +
                ", autoRotation=" + autoRotation +
                ", rotationConfig='" + rotationConfig + '\'' +
                ", rotationPeriod='" + rotationPeriod + '\'' +
                ", lastRotationTime=" + lastRotationTime +
                ", enterPriseProjectId='" + enterPriseProjectId + '\'' +
                ", freezeFlag=" + freezeFlag +
                '}';
    }

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

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Boolean getAutoRotation() {
        return autoRotation;
    }

    public void setAutoRotation(Boolean autoRotation) {
        this.autoRotation = autoRotation;
    }

    public String getRotationConfig() {
        return rotationConfig;
    }

    public void setRotationConfig(String rotationConfig) {
        this.rotationConfig = rotationConfig;
    }

    public String getRotationPeriod() {
        return rotationPeriod;
    }

    public void setRotationPeriod(String rotationPeriod) {
        this.rotationPeriod = rotationPeriod;
    }

    public Long getLastRotationTime() {
        return lastRotationTime;
    }

    public void setLastRotationTime(Long lastRotationTime) {
        this.lastRotationTime = lastRotationTime;
    }

    public String getEnterPriseProjectId() {
        return enterPriseProjectId;
    }

    public void setEnterPriseProjectId(String enterPriseProjectId) {
        this.enterPriseProjectId = enterPriseProjectId;
    }

    public Short getFreezeFlag() {
        return freezeFlag;
    }

    public void setFreezeFlag(Short freezeFlag) {
        this.freezeFlag = freezeFlag;
    }
}
