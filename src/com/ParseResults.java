package com;

/**
 * Created by KFrolov on 26.05.2017.
 */
public class ParseResults {
    private String result;
    private int border;

    public ParseResults(String result, int border) {
        this.result = result;
        this.border = border;
    }

    public String getResult() {

        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }
}
