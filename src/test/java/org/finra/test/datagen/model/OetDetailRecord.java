package org.finra.test.datagen.model;

import org.finra.test.datagen.ColumnMapping;
import org.finra.test.datagen.DbType;
import org.finra.test.datagen.util.TableColumn;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xiaodongli on 9/9/15.
 */
public class OetDetailRecord {
    @ColumnMapping(DbType= DbType.Date)
    public String TRADE_RPT_DT;
    @ColumnMapping(Size=29)
    public String TRADE_RPT_TS;
    @ColumnMapping(DbType= DbType.Date)
    public String ORGNL_EXCTN_DT;
    @ColumnMapping(Size=29)
    public String EXCTN_TS;
    @ColumnMapping(DbType= DbType.Date)
    public String CNTRA_RPT_DT;
    @ColumnMapping(Size=12)
    public String CNTRA_RPT_TM;
    @ColumnMapping(Size=12)
    public String CNTRA_RPT_TIME;
    @ColumnMapping(Size=29)
    public String CNTRA_RPT_TS;
    @ColumnMapping(Size=29)
    public String CNTRA_EXCTN_TS;
    @ColumnMapping(Size=29)
    public String ASSMD_EXCTN_TS;
    @ColumnMapping(Size=12)
    public String ACCPT_TM;
    @ColumnMapping(Size=12)
    public String ACCPT_TIME;
    @ColumnMapping(Size=29)
    public String ACCPT_TS;
    @ColumnMapping(Size=12)
    public String DCLN_TM;
    @ColumnMapping(Size=12)
    public String DCLN_TIME;
    @ColumnMapping(Size=29)
    public String DCLN_TS;
    @ColumnMapping(Size=29)
    public String CNCLN_TS;
    @ColumnMapping(Size=29)
    public String LCKD_IN_TRADE_TS;
    @ColumnMapping(Size=29)
    public String TRADE_BREAK_TS;
    @ColumnMapping(Size=29)
    public String TRADE_BRKN_TS;
    @ColumnMapping(DbType= DbType.Date)
    public String TRADE_STLMT_DT;
    @ColumnMapping(Size=14)
    public String ISSUE_SYM_ID;
    @ColumnMapping(DbType = DbType.Int)
    public String ISSUE_ID;
    @ColumnMapping(Size = 6)
    public String MKT_CLASS_CD;
    @ColumnMapping(Size = 1)
    public String MKT_CNTR_ID;
    @ColumnMapping(Size = 1)
    public String RLTD_MKT_CNTR_ID;
    @ColumnMapping(Size = 1)
    public String RPTD_SIDE_CD;
    @ColumnMapping(Size = 5)
    public String RPTG_SIDE_FIRM_MP_ID;
    @ColumnMapping(Size = 5)
    public String RPTG_EXCTN_FIRM_MP_ID;
    @ColumnMapping(Size = 5)
    public String CNTRA_SIDE_FIRM_MP_ID;
    @ColumnMapping(Size = 5)
    public String CNTRA_EXCTN_FIRM_MP_ID;
    @ColumnMapping(Size = 6)
    public String PRMRY_RPTG_SIDE_FIRM_MP_ID;
    @ColumnMapping(Size = 6)
    public String PRMRY_RPTG_EXCTN_FIRM_MP_ID;
    @ColumnMapping(Size = 6)
    public String PRMRY_CNTRA_SIDE_FIRM_MP_ID;
    @ColumnMapping(Size = 6)
    public String PRMRY_CNTRA_EXCTN_FIRM_MP_ID;
    @ColumnMapping(Size = 4)
    public String RPTG_SIDE_CLRG_NB;
    @ColumnMapping(Size = 20)
    public String RPTG_SIDE_BRNCH_SEQ_ID;
    @ColumnMapping(Size = 1)
    public String RPTG_SIDE_CPCTY_CD;
    @ColumnMapping(Size = 1)
    public String RPTG_SIDE_SHORT_CD;
    @ColumnMapping(Size = 4)
    public String CNTRA_CLRG_NB;
    @ColumnMapping(Size = 20)
    public String CNTRA_BRNCH_SEQ_ID;
    @ColumnMapping(Size = 1)
    public String CNTRA_CPCTY_CD;
    @ColumnMapping(Size = 2)
    public String CNTRA_SHORT_CD;
    @ColumnMapping(DbType = DbType.Int)
    public String EXCTN_QT;
    @ColumnMapping(DbType = DbType.Decimal)
    public String EXCTN_PR;
    @ColumnMapping(DbType = DbType.Int)
    public String RPTD_QT;
    @ColumnMapping(DbType = DbType.Decimal)
    public String RPTD_PR;
    @ColumnMapping(DbType = DbType.Decimal)
    public String CLRG_PR;
    @ColumnMapping(Size = 1)
    public String PBLSH_IND_CD;
    @ColumnMapping(Size = 1)
    public String MEDIA_RPT_FL;
    @ColumnMapping(Size = 1)
    public String TRADE_ST_CD;
    @ColumnMapping(Size = 4)
    public String TRADE_STLMT_MOD_CD;
    @ColumnMapping(Size = 4)
    public String TRD_THRU_EXMPT_MOD_CD;
    @ColumnMapping(Size = 4)
    public String TRADE_RPT_MOD_CD;
    @ColumnMapping(Size = 4)
    public String SRO_REQ_MOD_CD;
    @ColumnMapping(Size = 1)
    public String SYSTM_APNDD_MOD_FL;
    @ColumnMapping(Size = 4)
    public String ORGNL_MOD_CD;
    @ColumnMapping(Size = 1)
    public String RVRSL_FL;
    @ColumnMapping(Size = 1)
    public String CROVR_FL;
    @ColumnMapping(Size = 1)
    public String TRADE_THRU_EXMPT_FL;
    @ColumnMapping(Size = 1)
    public String CNTRA_ENTRY_FL;
    @ColumnMapping(Size = 1)
    public String XPLCT_FEE_FL;
    @ColumnMapping(Size = 1)
    public String CLRG_FL;
    @ColumnMapping(Size = 1)
    public String SPCL_TR_CD;
    @ColumnMapping(Size = 1)
    public String SPRVY_ENTRY_CD;
    @ColumnMapping(Size = 30)
    public String CNTRL_NB;
    @ColumnMapping(Size = 15)
    public String RPTG_SIDE_MEMO_TX;
    @ColumnMapping(Size = 1)
    public String PORTAL_TRADE_FL;
    @ColumnMapping(Size = 2)
    public String TRADE_SRC_CD;
    @ColumnMapping(Size = 4)
    public String CNTRA_I1I2_ID;
    @ColumnMapping(Size=30)
    public String CNTRA_CNTRL_NB;
    @ColumnMapping(Size = 15)
    public String OE_MEMO_TX;
    @ColumnMapping(Size = 2)
    public String PRMRY_XCHNG_CD;
    @ColumnMapping(Size = 1)
    public String RPT_TYPE_CD;
    @ColumnMapping(Size = 30)
    public String NO_WAS_LINK_NB;
    @ColumnMapping(Size = 1)
    public String INTD_MKT_CNTR_CD;
    @ColumnMapping(Size = 20)
    public String TRD_RFRNC_NB;
    @ColumnMapping(Size = 1)
    public String AVRTM_NSTCN_CD;
    @ColumnMapping(Size = 1)
    public String PR_OVRRD_CD;
    @ColumnMapping(Size = 1)
    public String AS_OF_FL;
    @ColumnMapping(DbType = DbType.Date)
    public String LAST_UPDT_DT;
    @ColumnMapping(Size = 12)
    public String LAST_UPDT_TM;
    @ColumnMapping(Size = 1)
    public String LCKD_IN_FL;
    @ColumnMapping(Size=30)
    public String NO_LINK_CNTRL_NB;
    @ColumnMapping(Size = 1)
    public String FIRM_TRD_MDFR_STLMT_TYPE_CD;
    @ColumnMapping(Size = 1)
    public String FIRM_TRD_MDFR_THRU_EXMPT_CD;
    @ColumnMapping(Size = 1)
    public String FIRM_TRD_MDFR_LATE_CD;
    @ColumnMapping(Size = 1)
    public String FIRM_TRD_MDFR_SRO_CD;
    @ColumnMapping(Size = 1)
    public String TRF_TRD_MDFR_SRO_CD;
    @ColumnMapping(Size = 1)
    public String TRF_TRD_MDFR_LATE_CD;
    @ColumnMapping(Size = 1)
    public String FINRA_TRD_MDFR_LATE_CD;
    @ColumnMapping(Size = 12)
    public String RPT_TRMNL_NB;
    @ColumnMapping(DbType = DbType.Int)
    public String TRADE_UNIT_QT;
    @ColumnMapping(Size = 1)
    public String RPTG_OBGTN_FL;
    @ColumnMapping(Size = 1)
    public String TRD_CRCTN_CLASS_CD;
    @ColumnMapping(Size = 1)
    public String CNTRA_RPTG_OBGTN_FL;
    @ColumnMapping(DbType= DbType.Date)
    public String FINRA_CNTRA_CNTRL_DT;
    @ColumnMapping(Size=30)
    public String FINRA_CNTRA_CNTRL_NB;
    @ColumnMapping(DbType= DbType.Date)
    public String FINRA_CNTRL_DT;
    @ColumnMapping(Size=30)
    public String FINRA_CNTRL_NB;
    @ColumnMapping(DbType= DbType.Date)
    public String FIRST_TRD_FINRA_CNTRL_DT;
    @ColumnMapping(Size=30)
    public String FIRST_TRD_FINRA_CNTRL_NB;
    @ColumnMapping(DbType= DbType.Date)
    public String PREV_TRD_FINRA_CNTRL_DT;
    @ColumnMapping(Size=30)
    public String PREV_TRD_FINRA_CNTRL_NB;
    @ColumnMapping(Size=1)
    public String SPCL_PRCSG_CD;
    @ColumnMapping(Size=30)
    public String TRF_CNTRA_CNTRL_NB;
    @ColumnMapping(Size=30)
    public String TRF_CNTRL_NB;
    @ColumnMapping(Size=20)
    public String RFRNC_NB;
    @ColumnMapping(Size=1)
    public String FINRA_TRD_MDFR_SRO_CD;
    @ColumnMapping(Size=12)
    public String FINRA_TRD_MDFR_THRU_EXMPT_TM;
    @ColumnMapping(Size=12)
    public String TRD_MDFR_THRU_EXMPT_TM;
    @ColumnMapping(Size=12)
    public String TRD_MDFR_SRO_TM;
    @ColumnMapping(Size=6)
    public String RFRNC_RPTG_FCLTY_CD;
    @ColumnMapping(DbType= DbType.Date)
    public String TRF_PRCSG_DT;
    @ColumnMapping(Size=31)
    public String REC_UNIQUE_ID;
    @ColumnMapping(DbType= DbType.Date)
    public String REC_LOAD_DT;
    @ColumnMapping(DbType= DbType.Date)
    public String FIRST_TRD_FINRA_CNTRA_CNTRL_DT;
    @ColumnMapping(Size=30)
    public String FIRST_TRD_FINRA_CNTRA_CNTRL_NB;
    @ColumnMapping(DbType= DbType.Date)
    public String PREV_TRD_FINRA_CNTRA_CNTRL_DT;
    @ColumnMapping(Size=30)
    public String PREV_TRD_FINRA_CNTRA_CNTRL_NB;

    private static Map<String, TableColumn> fieldToColumnMappings;
    public static Map<String, TableColumn> getFieldToColumnMappings() {
        if(fieldToColumnMappings!=null && fieldToColumnMappings.size()>0)
            return fieldToColumnMappings;
        synchronized (OetDetailRecord.class){
            fieldToColumnMappings=new LinkedHashMap<>();
            int ordinal=0;
            for(Field field : OetDetailRecord.class.getFields()) {
                TableColumn column = new TableColumn();
                column.name=field.getName();
                column.dbType = DbType.Varchar;
                column.size = 50;
                column.ordinal=++ordinal;
                ColumnMapping mappingAnnotation = field.getAnnotation(ColumnMapping.class);
                if(mappingAnnotation!=null){
                    column.dbType = mappingAnnotation.DbType();
                    column.size = mappingAnnotation.Size();
                    column.precision = mappingAnnotation.Precision();
                    column.scale = mappingAnnotation.Scale();
                }
                fieldToColumnMappings.put(field.getName(), column);
            }
        }
        return fieldToColumnMappings;
    }
}
