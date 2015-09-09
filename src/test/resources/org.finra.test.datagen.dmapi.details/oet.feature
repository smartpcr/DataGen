@OET @OrdersDetails
Feature: OET detail DM API Tests

  @OrdersDetails @OET @Issue @Symbol @Firm
  Scenario: Make sure reference data is populated
    Given records in issue_ref
      | issue_id | efctv_dt   | xprtn_dt   | xchng_sym_id | trd_sym_id | arca_sym_id | cms_dot_dlmtd_sym_id | nyse_sym_id | oats_rptng_sym_id | siac_sym_id | issue_nm                        | issue_short_nm |
      | -08011   | 2015-08-01 | 2049-07-31 | OET1         | OET2       |             |                      |             |                   |             | Off Exchange Trade Test Issue 1 | OET Test 1     |
      | -08012   | 2015-08-01 | 2049-07-31 | OET2         | OET1       | OET2        | OET2                 | OET2        | OET2              | OET2        | Off Exchange Trade Test Issue 2 | OET Test 2     |
      | -08013   | 2015-08-01 | 2049-07-31 | OET3         | OET3       |             | OET4                 | OET3        | OET3              |             | Off Exchange Trade Test Issue 3 | OET Test 3     |
      | -08014   | 2015-08-01 | 2049-07-31 | OET4         | OET4       |             |                      |             |                   | OET4        | Off Exchange Trade Test Issue 4 | OET Test 4     |
      | -08015   | 2015-08-01 | 2049-07-31 | OET5         | OET5       | OET5        |                      |             | OET5              |             | Off Exchange Trade Test Issue 5 | OET Test 5     |
    And records in firm_ref
      | firm_mp_id | efctv_dt   | xprtn_dt   | ns_cstmr_id | cmpny_nm   | mpid_nb  | creat_user_id | creat_ts            |
      | OETA       | 2015-08-01 | 2049-07-31 | 98765001    | OET Firm A | 12345001 | Tester        | 2015-09-08 11:11:11 |
      | OETA       | 2015-08-01 | 2049-07-31 | 98765002    | OET Firm A | 12345001 | Tester        | 2015-09-08 11:11:11 |
      | OETB       | 2015-08-01 | 2049-07-31 | 98765002    | OET Firm B | 12345002 | Tester        | 2015-09-08 11:11:11 |
      | OETB       | 2015-08-01 | 2049-07-31 | 98765003    | OET Firm B | 12345002 | Tester        | 2015-09-08 11:11:11 |
      | OETC       | 2015-08-01 | 2049-07-31 | 98765003    | OET Firm C | 12345003 | Tester        | 2015-09-08 11:11:11 |
      | OETD       | 2015-08-01 | 2049-07-31 | 98765004    | OET Firm D | 12345004 | Tester        | 2015-09-08 11:11:11 |
      | OETE       | 2015-08-01 | 2049-07-31 | 98765005    | OET Firm E | 98765005 | Tester        | 2015-09-08 11:11:11 |

  @OrdersDetails @OET @AllRecordTypes
  Scenario: Make sure fields from all record types are included (cmn, fo, eo, oet)
    Given records in OATS new orders
      | oats_roe_id  | issue_sym_id | event_dt   | firm_mp_id |
      | 999999999013 | OET1         | 2015-01-28 | OETA       |
    And records in OATS processed orders
      | oats_roe_id  | issue_sym_id | event_dt   | firm_mp_id |
      | 999999998012 | OET2         | 2015-01-28 | OETB       |
    And records in NED orders
      | rec_unique_id                      | issue_sym_id | orgnl_trade_dt | firm_mp_id |
      | J_2015-01-20_UF1KTC00051J_EX_82497 | OET1         | 2015-01-28     | OETB       |
    And records in OET orders
      | oet_unique_id | issue_sym_id | orgnl_exctn_dt | firm_mp_id |
      | 777777777001  | OET1         | 2015-01-28     | OETA       |
      | 777777777002  | OET1         | 2015-01-28     | OETD       |
    And records in issue_ref
      | issue_id | efctv_dt   | xprtn_dt   | xchng_sym_id | trd_sym_id | arca_sym_id | cms_dot_dlmtd_sym_id | nyse_sym_id | oats_rptng_sym_id | siac_sym_id |
      | -08011   | 2015-08-01 | 2049-07-31 | OET1         | OET2       |             |                      |             |                   |             |
    And records in firm_ref
      | firm_mp_id | efctv_dt   | xprtn_dt   | ns_cstmr_id | cmpny_nm   | mpid_nb  | creat_user_id | creat_ts            |
      | OETA       | 2015-08-01 | 2049-07-31 | 98765001    | OET Firm A | 12345001 | Tester        | 2015-09-08 11:11:11 |
      | OETA       | 2015-08-01 | 2049-07-31 | 98765002    | OET Firm A | 12345001 | Tester        | 2015-09-08 11:11:11 |
      | OETB       | 2015-08-01 | 2049-07-31 | 98765002    | OET Firm B | 12345002 | Tester        | 2015-09-08 11:11:11 |
      | OETB       | 2015-08-01 | 2049-07-31 | 98765003    | OET Firm B | 12345002 | Tester        | 2015-09-08 11:11:11 |
      | OETC       | 2015-08-01 | 2049-07-31 | 98765003    | OET Firm C | 12345003 | Tester        | 2015-09-08 11:11:11 |
      | OETD       | 2015-08-01 | 2049-07-31 | 98765004    | OET Firm D | 12345004 | Tester        | 2015-09-08 11:11:11 |
      | OETE       | 2015-08-01 | 2049-07-31 | 98765005    | OET Firm E | 98765005 | Tester        | 2015-09-08 11:11:11 |
    When Run DM API query
      | RecordType | StartDate  | EndDate    | Symbol | Firms | AllRelated |
      | fo,eo,oet  | 2015-01-28 | 2015-01-28 | OET1   | OETA  | true       |
    Then I should get 4 records (2 OATS, 1 NED, 1 OET)

  @OrdersDetails @OET @EmptyIssues
  Scenario: Make sure symbol mapped to empty issue id (-9, null) can be retrieved from search
    Given records in OET orders that have both valid issue_id and empty issue_id
      | oet_unique_id | issue_sym_id | orgnl_exctn_dt | issue_id |
      | 777777777003  | OET4         | 2015-01-27     | -08014   |
      | 777777777004  | OET4         | 2015-01-27     | -9       |
      | 777777777005  | OET4         | 2015-01-27     | NULL     |
    When Run DM API query with Symbol=AAPL and Date=2015-01-27 and only OET record type checked
      | RecordType | StartDate  | EndDate    | Symbol | Firms | AllRelated |
      | oet        | 2015-01-27 | 2015-01-27 | OET4   |       | false      |
    Then I should get 3 records

  @OrdersDetails @OET @LateReport
  Scenario: Make sure records reported later than execution date can be retrieved and it should also include records with empty issues
    Given records in OET orders that have both same-day reporting and late reporting
      | rec_unique_id | issue_sym_id | orgnl_exctn_dt | trade_rpt_dt | issue_id |
      | 777777777006  | OET1         | 2015-01-20     | 2015-01-20   | -08011   |
      | 777777777007  | OET1         | 2015-01-20     | 2015-01-21   | -08011   |
      | 777777777008  | OET1         | 2015-01-21     | 2015-01-21   | -08011   |
      | 777777777009  | OET5         | 2015-01-20     | 2015-01-20   | -08015   |
      | 777777777010  | OET5         | 2015-01-20     | 2015-01-21   | -08015   |
      | 777777777011  | OET5         | 2015-01-21     | 2015-01-21   | -08015   |
      | 777777777012  | OET1         | 2015-01-20     | 2015-01-20   | -9       |
      | 777777777013  | OET1         | 2015-01-20     | 2015-01-20   | NULL     |
      | 777777777014  | OET1         | 2015-01-20     | 2015-01-21   | -9       |
      | 777777777015  | OET1         | 2015-01-20     | 2015-01-21   | NULL     |
    When Run DM API query with Symbol=OET1 and Date=2015-01-20 and only OET record type checked
      | RecordType | StartDate  | EndDate    | Symbol | Firms | AllRelated |
      | oet        | 2015-01-20 | 2015-01-20 | OET1   |       | false      |
    Then I should get 6 records


  @OrdersDetails @OET @AllRelatedFirms
  Scenario: Make sure all related firms can be retrieved using firm_ref table
    Given records in OET orders that have both same-day reporting and late reporting
      | rec_unique_id | issue_sym_id | orgnl_exctn_dt | trade_rpt_dt | rptg_exctn_firm_mp_id | cntra_exctn_firm_mp_id |
      | 777777777016  | OET1         | 2015-01-22     | 2015-01-22   | OETA                  | NULL                   |
      | 777777777017  | OET1         | 2015-01-22     | 2015-01-22   | OETB                  | NULL                   |
      | 777777777018  | OET1         | 2015-01-22     | 2015-01-22   | OETC                  | NULL                   |
      | 777777777019  | OET1         | 2015-01-22     | 2015-01-22   | OETD                  | NULL                   |
    And records in firm_ref
      | firm_mp_id | ns_cstmr_id |
      | OETA       | 98765001    |
      | OETA       | 98765002    |
      | OETB       | 98765002    |
      | OETB       | 98765003    |
      | OETC       | 98765003    |
    When Run DM API query with Symbol=OET1 and Date=2015-01-22 and Firm=OETA and AllRelatedFirms=True and only OET record type checked
      | RecordType | StartDate  | EndDate    | Symbol | Firms | AllRelated |
      | oet        | 2015-01-22 | 2015-01-22 | OET1   | OETA  | true       |
    Then I should get 2 records


  @OrdersDetails @OET @NotFound
  Scenario: Make sure when user searches OET only records and firm not present in firm_ref, 0 record should be returned
    Given records in OET orders that firm "OETO" or ns_cstmr_id "98765000" does not exist in OET records for ExecutionDate="2015-01-28"
      | rec_unique_id | issue_sym_id | orgnl_exctn_dt | rptg_exctn_firm_mp_id | cntra_exctn_firm_mp_id |
      | 777777777021  | OET0         | 2015-01-28     | OETO                  | NULL                   |
    And records in issue_ref
      | issue_id | issue_sym_id |
      | -08010   | OET0         |
    And records NOT in firm_ref
      | firm_mp_id | ns_cstmr_id |
      | OETO       | 98765000    |
      | OETO       | -9          |
    When Run DM API query with Date=2015-01-28 and Firm=OETO and AllRelatedFirms=True and only OET record type checked
      | RecordType | StartDate  | EndDate    | Symbol | Firms | AllRelated |
      | oet        | 2015-01-28 | 2015-01-28 |        | OETO  | true       |
    Then I should get 0 records


  @OrdersDetails @OET @EmptyExecutionTime
  Scenario: Make sure execution time is able to pick value from assumed execution time when original execution time is not available
    Given records in OET orders with empty oet_exctn_ts but non-empty oet_assmd_exctn_ts
      | rec_unique_id | orgnl_exctn_dt | issue_sym_id | rptg_exctn_firm_mp_id | cntra_exctn_firm_mp_id | oet_exctn_ts | oet_assmd_exctn_ts      |
      | 777777777022  | 2015-01-23     | OET1         | OETA                  | NULL                   | NULL         | 2015-01-23 19:19:38.420 |
    And records in firm_ref
      | firm_mp_id | ns_cstmr_id |
      | OETA       | 98765001    |
      | OETA       | -9          |
    When Run DM API query with Date=2015-01-23 and Firm=OETA and Symbol=OET1 and AllRelatedFirms=False and only OET record type checked
      | RecordType | StartDate  | EndDate    | Symbol | Firms | AllRelated |
      | oet        | 2015-01-23 | 2015-01-23 | OET1   | OETA  | false      |
    Then I should get 1 record with 'assmd_exctn_ts'='2015-01-23 19:19:38.420'


  @OrdersDetails @OET @FirmMappingsToTwoFields
  Scenario: Make sure both fields are used to search OET records

  Scenario Template:
    Given records in OET orders with oet_rptg_exctn_firm_mp_id=OETA and oet_cntra_exctn_firm_mp_id=OETC
      | RecordType | orgnl_exctn_dt | rptg_exctn_firm_mp_id | cntra_exctn_firm_mp_id |
      | OET_Orders | 2015-01-26     | OETA                  | OETC                   |
    And records in firm_ref
      | firm_mp_id | ns_cstmr_id |
      | OETA       | 98765001    |
      | OETA       | 98765002    |
      | OETB       | 98765002    |
      | OETB       | 98765003    |
      | OETC       | 98765003    |
    When Run DM API query with Date=2015-01-26 and Firm=<Firms> and AllRelatedFirms=False and only OET record type checked
      | RecordType | StartDate  | EndDate    | Symbol | Firms   | AllRelated |
      | oet        | 2015-01-26 | 2015-01-26 |        | <Firms> | false      |
    Then I should get 1 record
    Examples:
      | Firms |
      | OETA  |
      | OETC  |