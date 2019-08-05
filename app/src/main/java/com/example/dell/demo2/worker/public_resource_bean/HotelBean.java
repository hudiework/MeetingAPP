package com.example.dell.demo2.worker.public_resource_bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/29.
 */

public class HotelBean implements Serializable{
    private int hotelId;//房间id
    public String hotelName;//酒店名字
    public int roomPeopleNumber;//房间可容纳人数
    public int hotelBookRoomNumber;//酒店房间预定数量
    public int roomAvailableNumber;//房间可用数量
    public String hotelBookTime;//旅店预定时间
    public String hotelEndTime;//预定截止时间
    public String money;//酒店预定价格
    public String hotelAddress;//酒店地址
    public String hotelPhoneNumber;//酒店联系电话
    public String roomType;
    //房间类型分为standard,bigBed,business,luxury
    public String hotelCompany;//酒店隶属的公司
    public String hotelDetail;//酒店详细信息
    public float hotelEval;//酒店评价
    public int hotelState;//房间状态
    public int getHotelId() {
        return hotelId;
    }
    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
    public String getHotelName() {
        return hotelName;
    }
    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
    public int getHotelState() {
        return hotelState;
    }
    public void setHotelState(int hotelState) {
        this.hotelState = hotelState;
    }

    public int getRoomPeopleNumber() {
        return roomPeopleNumber;
    }
    public void setRoomPeopleNumber(int roomPeopleNumber) {
        this.roomPeopleNumber = roomPeopleNumber;
    }
    public int getHotelBookRoomNumber() {
        return hotelBookRoomNumber;
    }
    public void setHotelBookRoomNumber(int hotelBookRoomNumber) {
        this.hotelBookRoomNumber = hotelBookRoomNumber;
    }
    public int getRoomAvailableNumber() {
        return roomAvailableNumber;
    }
    public void setRoomAvailableNumber(int roomAvailableNumber) {
        this.roomAvailableNumber = roomAvailableNumber;
    }
    public String getHotelBookTime() {
        return hotelBookTime;
    }
    public void setHotelBookTime(String hotelBookTime) {
        this.hotelBookTime = hotelBookTime;
    }
    public String getHotelEndTime() {
        return hotelEndTime;
    }
    public void setHotelEndTime(String hotelEndTime) {
        this.hotelEndTime = hotelEndTime;
    }
    public String getMoney() {
        return money;
    }
    public void setMoney(String money) {
        this.money = money;
    }
    public String getHotelAddress() {
        return hotelAddress;
    }
    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }
    public String getHotelPhoneNumber() {
        return hotelPhoneNumber;
    }
    public void setHotelPhoneNumber(String hotelPhoneNumber) {
        this.hotelPhoneNumber = hotelPhoneNumber;
    }
    public String getRoomType() {
        return roomType;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    public String getHotelCompany() {
        return hotelCompany;
    }
    public void setHotelCompany(String hotelCompany) {
        this.hotelCompany = hotelCompany;
    }
    public String getHotelDetail() {
        return hotelDetail;
    }
    public void setHotelDetail(String hotelDetail) {
        this.hotelDetail = hotelDetail;
    }
    public float getHotelEval() {
        return hotelEval;
    }
    public void setHotelEval(float hotelEval) {
        this.hotelEval = hotelEval;
    }
    public HotelBean(String hotelName, int roomPeopleNumber,
                     int hotelBookRoomNumber, int roomAvailableNumber,
                     String hotelBookTime, String hotelEndTime, String money,
                     String hotelAddress, String hotelPhoneNumber, String roomType,
                     String hotelCompany, String hotelDetail, float hotelEval) {
        super();
        this.hotelName = hotelName;
        this.roomPeopleNumber = roomPeopleNumber;
        this.hotelBookRoomNumber = hotelBookRoomNumber;
        this.roomAvailableNumber = roomAvailableNumber;
        this.hotelBookTime = hotelBookTime;
        this.hotelEndTime = hotelEndTime;
        this.money = money;
        this.hotelAddress = hotelAddress;
        this.hotelPhoneNumber = hotelPhoneNumber;
        this.roomType = roomType;
        this.hotelCompany = hotelCompany;
        this.hotelDetail = hotelDetail;
        this.hotelEval = hotelEval;
    }
    @Override
    public String toString() {
        return "HostelBean [hotelName=" + hotelName + ", roomPeopleNumber="
                + roomPeopleNumber + ", hotelBookRoomNumber="
                + hotelBookRoomNumber + ", roomAvailableNumber="
                + roomAvailableNumber + ", hotelBookTime=" + hotelBookTime
                + ", hotelEndTime=" + hotelEndTime + ", money=" + money
                + ", hotelAddress=" + hotelAddress + ", hotelPhoneNumber="
                + hotelPhoneNumber + ", roomType=" + roomType
                + ", hotelCompany=" + hotelCompany + ", hotelDetail="
                + hotelDetail + ", hotelEval=" + hotelEval + "]";
    }
    //initConstructer
    public HotelBean(String hotelName, int roomPeopleNumber,
                     int roomAvailableNumber, String money, String hotelAddress,
                     String hotelPhoneNumber, String roomType, String hotelCompany) {
        super();
        this.hotelName = hotelName;
        this.roomPeopleNumber = roomPeopleNumber;
        this.roomAvailableNumber = roomAvailableNumber;
        this.money = money;
        this.hotelAddress = hotelAddress;
        this.hotelPhoneNumber = hotelPhoneNumber;
        this.roomType = roomType;
        this.hotelCompany = hotelCompany;
    }


    //空构造器
    public HotelBean() {
        super();
    }


    //客户端需要的属性
    public HotelBean(int hotelId, String hotelName,int roomPeopleNumber,
                     int roomAvailableNumber,String hotelAddress,
                     String hotelPhoneNumber, String roomType, String hotelDetail,float hotelEval,
                     String money,int hotelState) {
        super();
        this.hotelId=hotelId;
        this.hotelName = hotelName;
        this.roomPeopleNumber = roomPeopleNumber;
        this.roomAvailableNumber = roomAvailableNumber;
        this.money = money;
        this.hotelAddress = hotelAddress;
        this.hotelPhoneNumber = hotelPhoneNumber;
        this.hotelDetail=hotelDetail;
        this.roomType = roomType;
        this.hotelEval=hotelEval;
        this.hotelState = hotelState;
    }
}
