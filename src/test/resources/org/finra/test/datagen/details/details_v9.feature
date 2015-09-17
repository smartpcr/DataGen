@Details @Data
Feature: Generate staged data for details page for v9

  Scenario Template: stage test data for user mart
    Given search parameters
      | Field                             | Value                    |
      | Version                           | 9                        |
      | Symbol                            | <symbol>                 |
      | Firms                             | <firms>                  |
      | AllRelatedFirms                   | <allRelated>             |
      | FromDate                          | <fromDate>               |
      | FromTime                          | <fromTime>               |
      | lastFirmOrderId                   | <lastFirmOrderId>        |
      | lastExchangeOrderId               | <lastExchangeOrderId>    |
      | lastOffExchangeTradeId            | <lastOffExchangeTradeId> |
      | ToDate                            | <toDate>                 |
      | ToTime                            | <toTime>                 |
      | RecordCount                       | <recCount>               |
      | RecordTypes                       | <recTypes>               |
      | UserId                            | <userId>                 |
      | RefId                             | <refId>                  |
      | IncludeDerivedFields              | true                     |
      | DefaultFieldValueFillerPercentage | 50                       |
    And user tracking record
      | Field              | Value                  |
      | ExcelFile          | filter_N_testdata.xlsx |
      | TrackSheet         | user_mart_trckg        |
      | ConfigSheet        | user_mart_trckg_dm     |
      | TrackingDataSource | track                  |
      | TrackingSchemaName | sawtrckowner           |
      | TrackingTableName  | user_mart_trckg        |
    And mart table record
      | Field          | Value                  |
      | ExcelFile      | filter_N_testdata.xlsx |
      | DataSheet      | <userId>_<refId>       |
      | ConfigSheet    | <userId>_<refId>_dm    |
      | MartDataSource | mart                   |
      | MartSchemaName | sawmartowner           |
      | MartTableName  | event_tmplt_v8         |
    When generate tracking data
    And generate tracking config
    And generate mart data
    And apply static data only when userId="filter3" and refId="100005"
      | cmn_rec_type | cmn_event_type_cd | cmn_buy_sell_cd | cmn_issue_sym_id | cmn_mkt_class_cd | cmn_bd_nb | cmn_firm_mp_id | cmn_event_dt | cmn_event_tm        | cmn_event_qt | fo_event_type_cd | fo_oats_roe_id | fo_issue_sym_id |
      | Firm Order   | OR                |                 | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900083       | AAPL            |
      | Firm Order   | OR                |                 | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900084       | AAP L           |
      | Firm Order   | OR                |                 | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900085       | AAPL!           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900086       | AAP@L           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900087       | AAP#L           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900088       | AAP$L           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900089       | %AAPL           |
      | Firm Order   | OR                | SS              | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | NW               | 99900090       | AAPL^           |
      | Firm Order   | OR                | SS              | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900091       | AAPL&           |
      | Firm Order   | OR                | SS              | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900092       | AAPL*           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900093       | A(APL           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900094       | AA)PL           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900095       | AA-PL           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900096       | AAP_L           |
      | Firm Order   | OR                | SS              | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900097       | AAP+L           |
      | Firm Order   | OR                | SS              | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900098       | AAP=L           |
      | Firm Order   | OR                | SS              | AAPL             | NNM              | 5608      | EWTT           | 2014-06-03   | 1970-01-01 09:00:00 | 100          | OR               | 99900099       | AAP&lt;L        |
      | Firm Order   | OR                | SS              | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900100       | AA,PL           |
      | Firm Order   | OR                | SS              | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900101       | AA&gt;PL        |
      | Firm Order   | OR                | SS              | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900102       | A.APL           |
      | Firm Order   | OR                | SS              | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900103       | AAP?L           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900104       | A/APL           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900105       | AA:PL           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900106       | AA;PL           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900107       | AAP"L           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 99900108       | A'APL           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 1694342587366  | AAPL[           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 1694342612876  | AAPL]           |
      | Firm Order   | OR                | SS              | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 1694342615578  | AAPL{           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 1694342615552  | AAPL}           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 1694292194854  | AAP\|L          |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 1694342612998  | AAP\L           |
      | Firm Order   | OR                | B               | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 1694342615563  | AAP~L           |
      | Firm Order   | OR                | SS              | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | OR               | 1694342589990  | AAP`L           |
    And apply static data only when userId="filter3" and refId="100005"
      | cmn_rec_type   | cmn_event_type_cd | cmn_issue_sym_id | cmn_mkt_class_cd | cmn_bd_nb | cmn_firm_mp_id | cmn_event_dt | cmn_event_tm            | cmn_event_qt | cmn_buy_sell_cd | eo_rec_unique_id                      | eo_orgnl_trade_dt | eo_order_rcvd_tm        | eo_issue_sym_id | eo_issue_id | eo_mkt_class_cd |
      | Exchange Order | OR                | AAPL             | NNM              | 29        | MLCO           | 2014-06-03   | 1970-01-01 11:52:31.123 | 800          | B               | J_2015-02-11_1D1KT401AHUU_EX_20062479 | 2014-06-03        | 1970-01-01 11:52:31.123 | AAPL%           | 8           | NNM             |
      | Exchange Order | OR                | AAPL             | NNM              | 29        | MLCO           | 2014-06-03   | 1970-01-01 11:52:31.123 | 800          | B               | J_2015-02-11_1D1KT401AHUU_EX_20062496 | 2014-06-03        | 1970-01-01 11:52:31.123 | AAPL%           | 8           | NNM             |
    And apply static data only when userId="filter3" and refId="100006"
      | cmn_rec_type   | cmn_event_type_cd | cmn_issue_sym_id | cmn_mkt_class_cd | cmn_bd_nb | cmn_firm_mp_id | cmn_event_dt | cmn_event_tm            | cmn_event_qt | cmn_buy_sell_cd | eo_rec_unique_id                      | eo_orgnl_trade_dt | eo_order_rcvd_tm        | eo_issue_sym_id | eo_issue_id | eo_mkt_class_cd |
      | Firm Order     | OR                | AAPL             | NNM              | 29        | EWTT           | 2014-06-03   | 1970-01-01 11:52:31.123 | 800          | B               | J_2015-02-11_1D1KT401AHUU_EX_20062479 | 2014-06-03        | 1970-01-01 11:52:31.123 | AAPL%           | 8           | NNM             |
      | Exchange Order | OR                | AAPL             | NNM              | 29        | EWTT           | 2014-06-03   | 1970-01-01 11:52:31.123 | 800          | B               | J_2015-02-11_1D1KT401AHUU_EX_20062496 | 2014-06-03        | 1970-01-01 11:52:31.123 | AAPL%           | 8           | NNM             |
    And generate mart config
    Then user track record should be populated
    And data mart table should be populated
    And mart config table should be populated
    Examples:
      | symbol | firms     | allRelated | fromDate   | fromTime | toDate     | toTime   | recCount | recTypes  | userId       | refId    | lastFirmOrderId | lastExchangeOrderId | lastOffExchangeTradeId |
      | AAPL   |           | N          | 2014-06-25 | 11:00:00 | 2014-06-25 | 11:00:00 | 248      | fo,eo     | filter3      | 100005   | 919999999001    | 818888888001        | 717777777001           |
      | AAPL   | HM03      | N          | 2014-06-25 | 11:00:00 | 2014-06-25 | 11:00:00 | 43       | fo,eo     | filter3      | 100006   | 919999999249    | 818888888249        | 717777777249           |
      | AAPL   |           | N          | 2014-06-25 | 11:00:00 | 2014-06-25 | 11:00:00 | 27       | fo,eo     | sheppjak     | test0004 | 919999999292    | 818888888292        | 717777777292           |
      | AAPL   | NM03,HM04 | N          | 2014-06-25 | 11:00:00 | 2014-06-25 | 11:00:00 | 43       | fo,eo     | filter3      | 100007   | 919999999319    | 818888888319        | 717777777319           |
      | AAPL   | MICO,MLEX | Y          | 2014-01-20 | 00:00:00 | 2014-01-27 | 23:59:59 | 50       | fo,eo,oet | filter3      | 100008   | 919999999362    | 818888888362        | 717777777362           |
      | AAPL   | MICO,MLEX | Y          | 2014-01-20 | 00:00:00 | 2014-01-27 | 23:59:59 | 100      | fo,eo,oet | filter3      | 100009   | 919999999412    | 818888888412        | 717777777412           |
      | AAPL   |           | N          | 2014-06-25 | 10:00:00 | 2014-06-25 | 10:00:00 | 27       | fo,eo     | Tst_Analyst1 | 1111112  | 919999999512    | 818888888512        | 717777777512           |
