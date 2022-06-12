package com.example.fitapp;

public class QuestionBiologicalAge {
    private int ID;
    private String QUESTION;
    private String OPT1;
    private String OPT2;
    private String OPT3;
    private String OPT4;

    public QuestionBiologicalAge()
    {
        ID=0;
        QUESTION="";
        OPT1="";
        OPT2="";
        OPT3="";
        OPT4="";

    }
    public QuestionBiologicalAge(String QUESTION, String OPT1, String OPT2, String OPT3, String OPT4) {

        this.QUESTION = QUESTION;
        this.OPT1 = OPT1;
        this.OPT2 = OPT2;
        this.OPT3 = OPT3;
        this.OPT4 = OPT4;

    }
    public int getID()
    {
        return ID;
    }
    public String getQUESTION() {
        return QUESTION;
    }
    public String getOPT1() {
        return OPT1;
    }
    public String getOPT2() {
        return OPT2;
    }
    public String getOPT3() {
        return OPT3;
    }
    public String getOPT4() {
        return OPT4;
    }


    public void setID(int id)
    {
        ID=id;
    }
    public void setQUESTION(String QUESTION) {
        this.QUESTION = QUESTION;
    }
    public void setOPT1(String OPT1) {
        this.OPT1 = OPT1;
    }
    public void setOPT2(String OPT2) {
        this.OPT2 = OPT2;
    }
    public void setOPT3(String OPT3) {
        this.OPT3 = OPT3;
    }
    public void setOPT4(String OPT4) {
        this.OPT4 = OPT4;
    }


}
