@Details @Data
Feature: Generate staged data for details page for version 8 (N)

  Scenario Outline: stage test data for user mart
    Given search parameters
      | Field                  | Value                    |
      | Version                | 8                        |
      | Symbol                 | <symbol>                 |
      | Firms                  | <firms>                  |
      | AllRelatedFirms        | <allRelated>             |
      | FromDate               | <fromDate>               |
      | FromTime               | <fromTime>               |
      | lastFirmOrderId        | <lastFirmOrderId>        |
      | lastExchangeOrderId    | <lastExchangeOrderId>    |
      | lastOffExchangeTradeId | <lastOffExchangeTradeId> |
      | ToDate                 | <toDate>                 |
      | ToTime                 | <toTime>                 |
      | RecordCount            | <recCount>               |
      | RecordTypes            | <recTypes>               |
      | UserId                 | <userId>                 |
      | RefId                  | <refId>                  |
    And user tracking record
      | Field       | Value                               |
      | ExcelFile   | saw_analyzer_filter_N_testdata.xlsx |
      | TrackSheet  | user_mart_trckg                     |
      | ConfigSheet | user_mart_trckg_dm                  |
    And mart table record
      | Field       | Value                               |
      | ExcelFile   | saw_analyzer_filter_N_testdata.xlsx |
      | DataSheet   | <userId>_<refId>                    |
      | ConfigSheet | <userId>_<refId>_dm                 |
    When generate tracking data
    And generate detail data
    Then user track record should be populated
    And data mart table should be populated
    And mart config table should be populated
  Examples:
  | symbol | firms     | allRelated | fromDate   | fromTime | toDate     | toTime   | recCount | recTypes | userId   | refId    | lastFirmOrderId | lastExchangeOrderId | lastOffExchangeTradeId |
  | AAPL   |           | N          | 2014-06-25 | 11:00:00 | 2014-06-25 | 11:00:00 | 248      | fo,eo    | filter3  | 100005   | 999999999001    | 888888888001        | 777777777001           |
  | AAPL   | HM03      | N          | 2014-06-25 | 11:00:00 | 2014-06-25 | 11:00:00 | 43       | fo,eo    | filter3  | 100006   | 999999999249    | 888888888249        | 777777777249           |
  | AAPL   |           | N          | 2014-06-25 | 11:00:00 | 2014-06-25 | 11:00:00 | 27       | fo,eo    | sheppjak | test0004 | 999999999276    | 888888888276        | 777777777276           |
  | AAPL   | NM03,HM04 | N          | 2014-06-25 | 11:00:00 | 2014-06-25 | 11:00:00 | 43       | fo,eo    | filter3  | 100007   | 999999999319    | 888888888319        | 777777777319           |
  | AAPL   | MICO,MLEX | Y          | 2014-01-20 | 00:00:00 | 2014-01-27 | 23:59:59 | 50       | fo,eo    | filter3  | 100008   | 999999999369    | 888888888369        | 777777777369           |
