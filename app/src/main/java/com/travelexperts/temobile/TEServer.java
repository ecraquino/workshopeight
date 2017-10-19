/**
 * Created by 331426 on 10/3/2017.
 */
package com.travelexperts.temobile;


public class TEServer {

    String serverName;

    //public TEServer(String serverName) {
    //    this.serverName = serverName;
    //}

    //public TEServer() {

    //}

    public String getServerName() {
        //return "http://travelexperts.ddns.net:8080/workshopseven/webapi";
        return "http://travelexperts.ddns.net:8080/TEdata/cal";
        //return "http://10.163.112.121:8080/TEdata/cal";
        //return "http://10.163.101.69:8080/webapi/"
    }

    public String getServerName2() {
        //return "http://travelexperts.ddns.net:8080/workshopseven/webapi";
        return "http://travelexperts.ddns.net:8080/TEdata/cust";
        //return "http://10.163.101.69:8080/webapi/"
    }
}
