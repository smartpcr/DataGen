package org.finra.test.datagen.rule;

import org.finra.test.datagen.RecordType;
import org.finra.test.datagen.TestDataRange;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 9/1/2015.
 */
public class RowContext {
	private int rowIndex;
	private int totalRowCount;

    private RecordType recordType;
    public RecordType getRecordType() {
        return this.recordType;
    }

    private String symbol;
    public String getSymbol() {
        return this.symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    private int issueId;
    public int getIssueId() {
        return this.issueId;
    }
    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    private String firm;
    public String getFirm() {
        return this.firm;
    }
    public void setFirm(String firm) {
        this.firm = firm;
    }

    private long crdNumber;
    public long getCrdNumber() {
        return this.crdNumber;
    }
    public void setCrdNumber(long crdNumber) {
        this.crdNumber = crdNumber;
    }

    private String memberId;
    public String getMemberId() {
        return this.memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

	private long lastFirmOrderId;
	public long getLastFirmOrderId() {
		return this.lastFirmOrderId;
	}

	private long lastExchangeOrderId;
	public long getLastExchangeOrderId() {
		return this.lastExchangeOrderId;
	}

	private long lastOffExchangeTradeId;
	public long getLastOffExchangeTradeId() {
	    return this.lastOffExchangeTradeId;
	}

	public RowContext(int totalRowCount, TestDataRange range) {
		this.rowIndex = -1;
		this.lastExchangeOrderId = range.getLastExchangeOrderId();
		this.lastFirmOrderId = range.getLastFirmOrderId();
		this.lastOffExchangeTradeId = range.getLastOffExchangeTradeId();
		this.totalRowCount = totalRowCount;
	}

	public boolean next() {
		this.recordType = RecordType.random();
		if(this.totalRowCount>rowIndex+1){
			this.rowIndex++;
			switch (this.recordType){
				case FirmOrder:
					this.lastFirmOrderId++;
					break;
				case ExchangeOrder:
					this.lastExchangeOrderId++;
					break;
				case OffExchangeTrade:
					this.lastOffExchangeTradeId++;
					break;
			}
			return true;
		}
		return false;
	}

    public void reset() {
        this.recordType = RecordType.Unknown;
        this.symbol = null;
        this.issueId = 0;
        this.firm=null;
        this.crdNumber = -9;
        this.memberId = null;
    }
}
