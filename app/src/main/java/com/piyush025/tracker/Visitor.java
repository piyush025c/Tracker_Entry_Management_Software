//Class to store guest information and related check-in details.
// .
package com.piyush025.tracker;

public class Visitor {

    public String gname;
    public String gmob;
    public String gemail;
    public String gcheckin;
    public String gcheckout;
    public String hname;
    public String hmob;
    public String hemail;
    public String haddress;
    public String visitid;

    public Visitor(String gname, String gmob, String gemail, String gcheckin, String gcheckout, String hname, String hmob, String hemail, String haddress,String visitid) {
        this.gname = gname;
        this.gmob = gmob;
        this.gemail = gemail;
        this.gcheckin = gcheckin;
        this.gcheckout = gcheckout;
        this.hname = hname;
        this.hmob = hmob;
        this.hemail = hemail;
        this.haddress = haddress;
        this.visitid=visitid;
    }
}
