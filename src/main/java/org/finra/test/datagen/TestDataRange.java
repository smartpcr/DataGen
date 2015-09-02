package org.finra.test.datagen;

import java.util.Date;
import java.util.List;

/**
 * Created by xd on 9/2/2015.
 */
public class TestDataRange {
    private String symbol;
    public String getSymbol() {
        return this.symbol;
    }
    public TestDataRange withSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    private List<String> firms;
    public List<String> getFirms() {
        return this.firms;
    }
    public TestDataRange withFirms(List<String> firms) {
        this.firms = firms;
        return this;
    }

    private Date startDate;
    public Date getStartDate() {
        return this.startDate;
    }
    public TestDataRange withStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    private Date endDate;
    public Date getEndDate() {
        return this.endDate;
    }
    public TestDataRange withEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    private boolean relatedFirms;
    public boolean getRelatedFirms() {
        return this.relatedFirms;
    }
    public TestDataRange withRelatedFirms(boolean relatedFirms) {
        this.relatedFirms = relatedFirms;
        return this;
    }
}
