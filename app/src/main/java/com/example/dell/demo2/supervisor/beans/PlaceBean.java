package com.example.dell.demo2.supervisor.beans;

/**
 * Created by Administrator on 2018/3/25.
 */

public class PlaceBean{
        private int placeGalleryful;//场地容纳人数
        private String placeName; //场地规格(名称)
        private int placeNum; //场地个数
        private String placeAddress;//场地地点
        public int getPlaceGalleryful() {
            return placeGalleryful;
        }
        public void setPlaceGalleryful(int placeGalleryful) {
            this.placeGalleryful = placeGalleryful;
        }
        public String getPlaceName() {
            return placeName;
        }
        public void setPlaceName(String placeName) {
            this.placeName = placeName;
        }
        public int getPlaceNum() {
            return placeNum;
        }
        public void setPlaceNum(int placeNum) {
            this.placeNum = placeNum;
        }
        public String getPlaceAddress() {
            return placeAddress;
        }
        public void setPlaceAddress(String placeAddress) {
            this.placeAddress = placeAddress;
        }

}
