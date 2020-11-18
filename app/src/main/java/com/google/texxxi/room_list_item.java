package com.google.texxxi;

public class room_list_item{
    String start_list;
    String arrive_list;
    String time_list_hour;
    String time_list_min;
    //String join_list;

    public room_list_item(String sl,String al,String tlh,String tlm){
        this.start_list = sl ;
        this.arrive_list = al;
        this.time_list_hour = tlh;
        this.time_list_min= tlm;
    }

    public String getStart_list() {
        return this.start_list; }

    public String getArrive_list() {

        return this.arrive_list;
    }

    public String getTime_list_hour() {
        return this.time_list_hour;
    }

    public String getTime_list_min() {
        return this.time_list_min;
    }
  /*  public String getJoin_list(){
        return this.join_list;
    }
*/
    public void setStart_list(String sl) {
        start_list = sl;

    }
    public void setArrive_list(String al) {
        arrive_list = al;
    }

    public void setTime_list_hour(String tlh) {
        time_list_hour = tlh;
    }

    public void setTime_list_min(String tlm) {
        time_list_min = tlm;
    }
/*
    public void setJoin_list(String jl) {
        join_list = jl;
    }
*/


}
