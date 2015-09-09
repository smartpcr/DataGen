package org.finra.test.datagen;

import java.util.Arrays;
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
    public TestDataRange withFirms(String... firms) {
        if(firms != null && firms.length>0) {
            this.firms = Arrays.asList(firms);
        }
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

    private List<RecordType> recordTypes;
    public List<RecordType> getRecordTypes() {
    	return this.recordTypes;
    }
    public TestDataRange withRecordTypes(List<RecordType> recordTypes) {
        this.recordTypes = recordTypes;
        return this;
    }
    public TestDataRange withRecordTypes(RecordType... recordTypes) {
        this.recordTypes = Arrays.asList(recordTypes);
        return this;
    }

	private long lastFirmOrderId = 99900082;
	public long getLastFirmOrderId() {
	    return this.lastFirmOrderId;
	}
	public TestDataRange withLastFirmOrderId(long lastFirmOrderId) {
	    this.lastFirmOrderId = lastFirmOrderId;
	    return this;
	}

	private long lastExchangeOrderId = 9533198;
	public long getLastExchangeOrderId() {
	    return this.lastExchangeOrderId;
	}
	public TestDataRange withLastExchangeOrderId(long lastExchangeOrderId) {
	    this.lastExchangeOrderId = lastExchangeOrderId;
	    return this;
	}

	private long lastOffExchangeTradeId = 777777777001L;
	public long getLastOffExchangeTradeId() {
	    return this.lastOffExchangeTradeId;
	}
	public TestDataRange withLastOffExchangeTradeId(long lastOffExchangeTradeId) {
	    this.lastOffExchangeTradeId = lastOffExchangeTradeId;
	    return this;
	}
}
