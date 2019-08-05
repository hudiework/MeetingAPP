package com.example.dell.demo2.supervisor.beans;

/**
 * Created by Administrator on 2018/3/25.
 */

public class HotelBean {
    private int hotelGalleryful; //房间容纳人数
    private int hotelRoomNum;//房间数量
    private int hotelRepast; //房间是否提供早餐

    public int getHotelGalleryful() {
        return hotelGalleryful;
    }
    public void setHotelGalleryful(int hotelGalleryful) {
        this.hotelGalleryful = hotelGalleryful;
    }
    public int getHotelRoomNum() {
        return hotelRoomNum;
    }
    public void setHotelRoomNum(int hotelRoomNum) {
        this.hotelRoomNum = hotelRoomNum;
    }
    public int getHotelRepast() {
        return hotelRepast;
    }
    public void setHotelRepast(int hotelRepast) {
        this.hotelRepast = hotelRepast;
    }
}
