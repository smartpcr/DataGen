@OET @OrdersDetails
Feature: OET detail DM API Tests

  Background:

  @OrdersDetails @OET @AllRecordTypes
  Scenario: Make sure fields from all record types are included (cmn, fo, eo, oet)
    Given records that matches search criteria exist in all 3 record types
      | RecordType     | issue_sym_id | orgnl_exctn_dt |
      | New_Orders     | APPL         | 2015-01-28     |
      | Process_Orders | AAPL         | 2015-01-28     |
      | NED_Orders     | AAPL         | 2015-01-28     |
      | OET_Orders     | AAPL         | 2015-01-28     |
    When Run DM API query
      | RecordType | StartDate  | EndDate    | Symbol | Firms | AllRelated |
      | fo,eo,oet  | 2015-01-28 | 2015-01-28 | AAPL   |       | false      |
    Then I should get 4 records

  @OrdersDetails @OET @EmptyIssues
  Scenario: Make sure symbol mapped to empty issue id (-9, null) can be retrieved from search
    Given OET records that have both valid issue_id and empty issue_id
      | RecordType | issue_sym_id | orgnl_exctn_dt | issue_id |
      | OET_Orders | AAPL         | 2015-01-28     | 3789     |
      | OET_Orders | AAP0         | 2015-01-28     | 3789     |
      | OET_Orders | AAPL         | 2015-01-28     | -9       |
      | OET_Orders | AAPL         | 2015-01-28     | NULL     |
      | OET_Orders | GOOG         | 2015-01-28     | -9       |
      | OET_Orders | MSFT         | 2015-01-28     | NULL     |
    When Run DM API query with Symbol=AAPL and Date=2015-01-28 and only OET record type checked
      | RecordType | StartDate  | EndDate    | Symbol | Firms | AllRelated |
      | oet        | 2015-01-28 | 2015-01-28 | AAPL   |       | false      |
    Then I should get 4 records

  @OrdersDetails @OET @LateReport
  Scenario: Make sure records reported later than execution date can be retrieved
    Given OET records that have both same-day reporting and late reporting
      | RecordType | issue_sym_id | orgnl_exctn_dt | trade_rpt_dt |
      | OET_Orders | AAPL         | 2015-01-28     | 2015-01-28   |
      | OET_Orders | AAPL         | 2015-01-28     | 2015-01-29   |
      | OET_Orders | AAPL         | 2015-01-29     | 2015-01-29   |
      | OET_Orders | GOOG         | 2015-01-28     | 2015-01-28   |
      | OET_Orders | GOOG         | 2015-01-28     | 2015-01-29   |
    When Run DM API query with Symbol=AAPL and Date=2015-01-28 and only OET record type checked
      | RecordType | StartDate  | EndDate    | Symbol | Firms | AllRelated |
      | oet        | 2015-01-28 | 2015-01-28 | AAPL   |       | false      |
    Then I should get 2 records


  @OrdersDetails @OET @AllRelatedFirms
  Scenario: Make sure all related firms can be retrieved using firm_ref table
    Given OET records that have both same-day reporting and late reporting
      | RecordType | issue_sym_id | orgnl_exctn_dt | rptg_exctn_firm_mp_id | cntra_exctn_firm_mp_id |
      | OET_Orders | AAPL         | 2015-01-28     | MLCO                  | NULL                   |
      | OET_Orders | AAPL         | 2015-01-28     | MLEX                  | NULL                   |
      | OET_Orders | AAPL         | 2015-01-28     | MLEX                  | NULL                   |
      | OET_Orders | AAPL         | 2015-01-28     | MLUC                  | NULL                   |
    And the following firm_ref records
      | firm_mp_id | ns_cstmr_id |
      | MLCO       | 447         |
      | MLEX       | 447         |
      | MLEX       | 5608        |
      | MLUC       | 5608        |
    When Run DM API query with Symbol=AAPL and Date=2015-01-28 and Firm=MLEX and AllRelatedFirms=True and only OET record type checked
      | RecordType | StartDate  | EndDate    | Symbol | Firms | AllRelated |
      | oet        | 2015-01-28 | 2015-01-28 | AAPL   | MLEX  | true       |
    Then I should get 3 records


  @OrdersDetails @OET @NotFound
  Scenario: Make sure when user searches OET only records and firm not present in records, 0 record should be returned
    Given firm "NITE" or ns_cstmr_id "16030" does not exist in OET records for ExecutionDate="2015-01-28"
      | RecordType  | orgnl_exctn_dt | rptg_exctn_firm_mp_id | cntra_exctn_firm_mp_id |
      | OATS_Orders | 2015-01-28     | NITE                  | NULL                   |
      | NED_Orders  | 2015-01-28     | NITE                  | NULL                   |
      | NED_Orders  | 2015-01-28     | NITE                  | NULL                   |
      | OET_Orders  | 2015-01-29     | NITE                  | NULL                   |
    And the following firm_ref records
      | firm_mp_id | ns_cstmr_id |
      | NITE       | 16030       |
      | NITE       | -9          |
    When Run DM API query with Date=2015-01-28 and Firm=NITE and AllRelatedFirms=True and only OET record type checked
      | RecordType | StartDate  | EndDate    | Symbol | Firms | AllRelated |
      | oet        | 2015-01-28 | 2015-01-28 |        | NITE  | true       |
    Then I should get 0 records


  @OrdersDetails @OET @EmptyExecutionTime
  Scenario: Make sure execution time is able to pick value from assumed execution time when original execution time is not available
    Given An OET record with empty oet_exctn_ts but non-empty oet_assmd_exctn_ts
      | RecordType | orgnl_exctn_dt | issue_sym_id | rptg_exctn_firm_mp_id | cntra_exctn_firm_mp_id | oet_exctn_ts | oet_assmd_exctn_ts      |
      | OET_Orders | 2015-01-28     | AAPL         | MLCO                  | NULL                   | NULL         | 2015-01-28 19:19:38.420 |
    And the following firm_ref records
      | firm_mp_id | ns_cstmr_id |
      | NITE       | 16030       |
      | NITE       | -9          |
    When Run DM API query with Date=2015-01-28 and Firm=MLCO and Symbol=AAPL and AllRelatedFirms=False and only OET record type checked
      | RecordType | StartDate  | EndDate    | Symbol | Firms | AllRelated |
      | oet        | 2015-01-28 | 2015-01-28 | AAPL   | MLCO  | false      |
    Then I should get 1 record with 'oet_exctn_ts'='2015-01-28 19:19:38.420'


  @OrdersDetails @OET @FirmMappingsToTwoFields
  Scenario: Make sure both fields are used to search OET records

  Scenario Template:
    Given OET records with with oet_rptg_exctn_firm_mp_id=BCEP and oet_cntra_exctn_firm_mp_id=CHK2
      | RecordType | orgnl_exctn_dt | rptg_exctn_firm_mp_id | cntra_exctn_firm_mp_id |
      | OET_Orders | 2015-01-28     | BCEP                  | CHK2                   |
    When Run DM API query with Date=2015-01-28 and Firm=BCEP and AllRelatedFirms=False and only OET record type checked
      | RecordType | StartDate  | EndDate    | Symbol | Firms   | AllRelated |
      | oet        | 2015-01-28 | 2015-01-28 |        | <Firms> | false      |
    Then I should get 1 record

    Examples:
      | Firms |
      | BCEP  |
      | CHK2  |