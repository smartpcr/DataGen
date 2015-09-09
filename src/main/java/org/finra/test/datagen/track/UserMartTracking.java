package org.finra.test.datagen.track;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.finra.test.datagen.util.Records;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaodongli on 9/8/15.
 */
public class UserMartTracking {
    public String rqst_user_id;
    public String user_rfrnc_id;
    public String rqst_ts;
    public String crit_from_dt;
    public String crit_from_tm;
    public String crit_to_dt;
    public String crit_to_tm;
    public String crit_issue_sym_id;
    public String crit_firm_mp_id;
    public String crit_alt_rltd_firm;
    public String trckg_stts_cd="CMPLT";
    public String cmplt_ts;
    public String user_mart_creat_job_id;
    public String user_mart_tbl_nm;
    public String user_mart_rec_ct;
    public String user_mart_tmplt_id;
    public String user_mart_view_nm;
    public String src_type_tx;

    public static UserMartTracking parse(Map<String, Object> record) throws IllegalAccessException {
        UserMartTracking tracking = new UserMartTracking();
        for(Field field : UserMartTracking.class.getFields()){
            field.set(tracking, Records.getValue(record, field.getName()));
        }
        return tracking;
    }

    public static List<UserMartTracking> parseTable(List<Map<String, Object>> table) throws IllegalAccessException {
        List<UserMartTracking> trackings = new LinkedList<>();
        for(Map<String, Object> record: table){
            trackings.add(parse(record));
        }
        return trackings;
    }

    public static UserMartTracking find(List<UserMartTracking> trackings, final String userId, final String refId) {
        return Iterables.find(trackings, new Predicate<UserMartTracking>() {
            @Override
            public boolean apply(UserMartTracking userMartTracking) {
                return userMartTracking.rqst_user_id.equalsIgnoreCase(userId) &&
                    userMartTracking.user_rfrnc_id.equalsIgnoreCase(refId);
            }
        });
    }

    public Map<String, String> getRecord() throws IllegalAccessException {
        Map<String, String> record = new LinkedHashMap<>();
        for(Field field: UserMartTracking.class.getFields()){
            String fieldName = field.getName();
            Object fieldValue = field.get(this);
            String value = null;
            if(fieldValue!=null){
                value = fieldValue.toString().trim();
            }
            record.put(fieldName, value);
        }
        return record;
    }

    public void applyChanges(UserMartTracking newTracking) throws IllegalAccessException {
        for(Field field : UserMartTracking.class.getFields()){
            field.set(this, field.get(newTracking));
        }
    }
}
