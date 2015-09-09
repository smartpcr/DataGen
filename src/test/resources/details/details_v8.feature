@Details @Data
Feature: Generate staged data for details page for version 8 (N)

  Scenario: stage test data for user mart

  Scenario Outline:
    Given search parameters
      | Field           | Value        |
      | Version         | 8            |
      | Symbol          | <symbol>     |
      | Firms           | <firms>      |
      | AllRelatedFirms | <allRelated> |
      | FromDate        | <fromDate>   |
      | FromTime        | <fromTime>   |
      | ToDate          | <toDate>     |
      | ToTime          | <toTime>     |
      | RecordCount     | <recCount>   |
      | RecordTypes     | <recTypes>   |
      | UserId          | <userId>     |
      | RefId           | <refId>      |
    And user_tracking record
      | Field       | Value                               |
      | ExcelFile   | saw_analyzer_filter_N_testdata.xlsx |
      | TrackSheet  | user_mart_trckg                     |
      | ConfigSheet | user_mart_trckg_dm                  |
    And mart table record
      | Field       | Value                               |
      | ExcelFile   | saw_analyzer_filter_N_testdata.xlsx |
      | DataSheet   | <userId>_<refId>                    |
      | ConfigSheet | <userId>_<refId>_dm                 |
    When generate data
    Then user track record should be populated
    And data mart table should be populated
    And mart config table should be populated
    Examples:
      | symbol | firms     | allRelated | fromDate   | fromTime | toDate     | toTime   | recCount | recTypes | userId   | refId    |
      | AAPL   |           | N          | 2014-06-25 | 11:00:00 | 2014-06-25 | 11:00:00 | 248      | fo,eo    | filter3  | 100005   |
      | AAPL   | HM03      | N          | 2014-06-25 | 11:00:00 | 2014-06-25 | 11:00:00 | 43       | fo,eo    | filter3  | 100006   |
      | AAPL   |           | N          | 2014-06-25 | 11:00:00 | 2014-06-25 | 11:00:00 | 27       | fo,eo    | sheppjak | test0004 |
      | AAPL   | NM03,HM04 | N          | 2014-06-25 | 11:00:00 | 2014-06-25 | 11:00:00 | 43       | fo,eo    | filter3  | 100007   |
      | AAPL   | MICO,MLEX | Y          | 2014-01-20 | 00:00:00 | 2014-01-27 | 23:59:59 | 50       | fo,eo    | filter3  | 100008   |
