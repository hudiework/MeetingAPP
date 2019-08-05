package com.example.dell.demo2.supervisor.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public class WorkerBean implements Serializable{
    private String workerId;
    private String workerName;
    private String workerCompany;
    private String workerMobilePhone;
    private List<String> functionList;

    public WorkerBean(String workerName, String workerCompany, String workerMobilePhone) {
        this.workerName = workerName;
        this.workerCompany = workerCompany;
        this.workerMobilePhone = workerMobilePhone;
    }
    public WorkerBean(){};

    public String getWorkerId() {
        return workerId;
    }
    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }
    public String getWorkerName() {
        return workerName;
    }
    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }
    public String getWorkerCompany() {
        return workerCompany;
    }
    public void setWorkerCompany(String workerCompany) {
        this.workerCompany = workerCompany;
    }
    public String getWorkerMobilePhone() {
        return workerMobilePhone;
    }
    public void setWorkerMobilePhone(String workerMobilePhone) {
        this.workerMobilePhone = workerMobilePhone;
    }
    public List<String> getFunctionList() {
        return functionList;
    }
    public void setFunctionList(List<String> functionList) {
        this.functionList = functionList;
    }

}
