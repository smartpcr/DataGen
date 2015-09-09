package org.finra.test.datagen;

import com.google.common.base.Strings;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.io.FileUtils;
import org.finra.test.datagen.util.DataSourceManager;
import org.finra.test.datagen.util.DbConnection;
import org.finra.test.datagen.util.DbUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by xd on 9/2/2015.
 */
public class ReferenceData {
    private TestDataRange range;
    private List<SymbolIssueTuple> symbolIssues;
    public List<SymbolIssueTuple> getSymbolIssues() {
        return this.symbolIssues;
    }

    private List<FirmCrdMemberTuple> firmCrdMembers;
    public List<FirmCrdMemberTuple> getFirmCrdMembers() {
        return this.firmCrdMembers;
    }

    private List<FirmCrdTuple> firmCrds;
    public List<FirmCrdTuple> getFirmCrds() {
        return this.firmCrds;
    }

    public ReferenceData(TestDataRange range) throws IOException, SQLException {
        this.range = range;
        this.populateRefData();
    }

    private void populateRefData() throws IOException, SQLException {
        DbConnection dbConn = DataSourceManager.getDbConnectionSettings("summary");

        String symbol = this.range.getSymbol();
        if(!Strings.isNullOrEmpty(symbol)) {
            String sql = readAllText("Symbol_Issue.sql");
            sql = sql.replace("@symbol", "'" + symbol + "'");
            this.symbolIssues = DbUtil.readList(dbConn, sql, symbolIssueTupleResultSetHandler);
        }
        List<String> firms = this.range.getFirms();
        if(firms!=null && firms.size()>0){
            StringBuilder inFirmSB = new StringBuilder();
            for(String firm : firms) {
                if(inFirmSB.length() > 0) {
                    inFirmSB.append(",");
                }
                inFirmSB.append("'" + firm + "'");
            }
            String inFirms = "(" + inFirmSB.toString() + ")";
            boolean relatedFirms = this.range.getRelatedFirms();
            if(relatedFirms){
                String sql = readAllText("Firm_Crb_Mbr_Related.sql");
                sql = sql.replace("@firms", inFirms);
                this.firmCrdMembers = DbUtil.readList(dbConn, sql, firmCrdMemberTupleResultSetHandler);

                String sql2 = readAllText("Firm_Crb_Related.sql");
                sql2 = sql2.replace("@firms", inFirms);
                this.firmCrds = DbUtil.readList(dbConn, sql2, firmCrdTupleResultSetHandler);
            }
            else {
                String sql = readAllText("Firm_Crb_Mbr.sql");
                sql = sql.replace("@firms", inFirms);
                this.firmCrdMembers = DbUtil.readList(dbConn, sql, firmCrdMemberTupleResultSetHandler);

                String sql2 = readAllText("Firm_Crb.sql");
                sql2 = sql2.replace("@firms", inFirms);
                this.firmCrds = DbUtil.readList(dbConn, sql2, firmCrdTupleResultSetHandler);
            }
        }
    }

    private String readAllText(String fileName) throws IOException {
        String filePath = this.getClass().getClassLoader().getResource(fileName).getFile();
        return FileUtils.fileRead(filePath);
    }

    public class SymbolIssueTuple {
        public String symbol;
        public int issueId;
    }

    final ResultSetHandler<SymbolIssueTuple> symbolIssueTupleResultSetHandler = new ResultSetHandler<SymbolIssueTuple>() {
        @Override
        public SymbolIssueTuple handle(ResultSet resultSet) throws SQLException {
            SymbolIssueTuple tuple = new SymbolIssueTuple();
            tuple.symbol = resultSet.getString("issue_sym_id");
            tuple.issueId = resultSet.getInt("issue_id");
            return tuple;
        }
    };

    public class FirmCrdMemberTuple {
        public String firm;
        public long crdNumber;
        public String memberId;
    }

    final ResultSetHandler<FirmCrdMemberTuple> firmCrdMemberTupleResultSetHandler = new ResultSetHandler<FirmCrdMemberTuple>() {
        @Override
        public FirmCrdMemberTuple handle(ResultSet resultSet) throws SQLException {
            FirmCrdMemberTuple tuple = new FirmCrdMemberTuple();
            tuple.firm = resultSet.getString("firm_mp_id");
            tuple.memberId = resultSet.getString("mbr_id");
            tuple.crdNumber = resultSet.getLong("crd_nb");
            return tuple;
        }
    };

    public class FirmCrdTuple {
        public String firm;
        public long customerId;
    }

    final ResultSetHandler<FirmCrdTuple> firmCrdTupleResultSetHandler = new ResultSetHandler<FirmCrdTuple>() {
        @Override
        public FirmCrdTuple handle(ResultSet resultSet) throws SQLException {
            FirmCrdTuple tuple = new FirmCrdTuple();
            tuple.firm = resultSet.getString("firm_mp_id");
            tuple.customerId = resultSet.getLong("ns_cstmr_id");
            return tuple;
        }
    };
}
