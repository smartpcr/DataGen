@Details @Data @focus
Feature: Generate staged data for details page for v8

  Scenario Template: stage test data for user mart
    Given search parameters
      | Field                             | Value                    |
      | Version                           | 8                        |
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
      | Field              | Value                    |
      | ExcelFile          | filter_N-1_testdata.xlsx |
      | TrackSheet         | user_mart_trckg          |
      | ConfigSheet        | user_mart_trckg_dm       |
      | TrackingDataSource | track                    |
      | TrackingSchemaName | sawtrckowner             |
      | TrackingTableName  | user_mart_trckg          |
    And mart table record
      | Field          | Value                    |
      | ExcelFile      | filter_N-1_testdata.xlsx |
      | DataSheet      | <userId>_<refId>         |
      | ConfigSheet    | <userId>_<refId>_dm      |
      | MartDataSource | mart                     |
      | MartSchemaName | sawmartowner             |
      | MartTableName  | event_tmplt_v8           |
    When generate tracking data
    And generate tracking config
    And generate mart data
    And apply static data only when userId="filter2" and refId="100003"
      | cmn_rec_type | cmn_event_type_cd | cmn_issue_sym_id | cmn_mkt_class_cd | cmn_bd_nb | cmn_firm_mp_id | cmn_event_dt | cmn_event_tm        | cmn_event_qt | cmn_buy_sell_cd | fo_oats_roe_id | fo_order_rcvd_dt    | fo_ns_cstmr_id | fo_firm_order_id     | fo_issue_sym_id |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          |                 | 99900083       | 2014-06-03 09:00:00 | 447            | 1la707pq5819         | AAPL            |
      | Firm Order   | NW                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          |                 | 99900083       | 2014-06-03 09:00:00 | 447            | 1302465Wee2240602A   | AAPL            |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          |                 | 99900084       | 2014-06-03 09:00:00 | 447            | oats_roe_id1         | AAP L           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          |                 | 99900085       | 2014-06-03 09:00:00 | 447            | 1la707pq31347        | AAPL!           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900086       | 2014-06-03 09:00:00 | 447            | 1la707pq33906        | AAP@L           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900087       | 2014-06-03 09:00:00 | 447            | 1la707pq5820         | AAP#L           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900088       | 2014-06-03 09:00:00 | 447            | 1la707pq31333        | AAP$L           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900089       | 2014-06-04 09:00:00 | 447            | 1la707pq5817         | %AAPL           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 99900090       | 2014-06-03 00:00:00 | 29             | 00023063089ORCA0-001 | AAPL^           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 99900091       | 2014-06-04 09:00:00 | 447            | 1la707pq33921        | AAPL&           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 99900092       | 2014-06-03 09:00:00 | 447            | 1la707pq31223        | AAPL*           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900093       | 2014-06-04 09:00:00 | 447            | 1la707pq5708         | A(APL           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900094       | 2014-06-03 09:00:00 | 447            | 1la707pq31341        | AA)PL           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900095       | 2014-06-04 09:00:00 | 447            | 1la707pq5704         | AA-PL           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900096       | 2014-06-03 09:00:00 | 447            | 1la707pq5659         | AAP_L           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 99900097       | 2014-06-04 09:00:00 | 447            | 1la707pq31182        | AAP+L           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 99900098       | 2014-06-03 09:00:00 | 447            | 1la707pq8319         | AAP=L           |
      | Firm Order   | OR                | AAPL             | NNM              | 5608      | EWTT           | 2014-06-03   | 1970-01-01 09:00:00 | 100          | SS              | 99900099       | 2014-06-04 09:00:00 | 447            | 1la707pq8342         | AAP&lt;L        |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 99900100       | 2014-06-03 09:00:00 | 5608           | 1303055WEE2240602A   | AA,PL           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 99900101       | 2014-06-03 09:00:00 | 447            | 1la707pq5827         | AA&gt;PL        |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 99900102       | 2014-06-03 09:00:00 | 447            | 1la707pq5834         | A.APL           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 99900103       | 2014-06-04 09:00:00 | 447            | 1la707pq33932        | AAP?L           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900104       | 2014-06-03 09:00:00 | 447            | 1la707pq33908        | A/APL           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900105       | 2014-06-03 09:00:00 | 447            | 1la707pq33910        | AA:PL           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900106       | 2014-06-04 09:00:00 | 447            | 1la707pq5821         | AA;PL           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900107       | 2014-06-03 09:00:00 | 447            | 1la707pq5824         | AAP"L           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900108       | 2014-06-04 09:00:00 | 447            | 1la707pq31338        | A'APL           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 1694342587366  | 2014-06-03 09:00:00 | 447            | 1la707pq5712         | AAPL[           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 1694342612876  | 2014-06-03 09:00:00 | 447            | 1la707pq31222        | AAPL]           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 1694342615578  | 2014-06-03 09:00:00 | 447            | 1la707pq33924        | AAPL{           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 1694342615552  | 2014-06-03 09:00:00 | 447            | 1la707pq33898        | AAPL}           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 1694292194854  | 2014-06-03 09:00:00 | 5608           | 1302489WEE2240602A   | AAP\|L          |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 1694342612998  | 2014-06-03 09:00:00 | 447            | 1la707pq31344        | AAP\L           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 1694342615563  | 2014-06-03 09:00:00 | 447            | 1la707pq33909        | AAP~L           |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 1694342589990  | 2014-06-03 09:00:00 | 447            | 1la707pq8336         | AAP`L           |
    And apply static data only when userId="filter2" and refId="100003"
      | cmn_rec_type   | cmn_event_type_cd | cmn_issue_sym_id | cmn_mkt_class_cd | cmn_bd_nb | cmn_firm_mp_id | cmn_event_dt | cmn_event_tm            | cmn_event_qt | cmn_buy_sell_cd | eo_rec_unique_id                      | eo_orgnl_trade_dt | eo_order_rcvd_tm        | eo_issue_sym_id | eo_issue_id | eo_mkt_class_cd | eo_event_type_cd |
      | Exchange Order | OR                | AAPL             | NNM              | 29        | MLCO           | 2014-06-03   | 1970-01-01 11:52:31.123 | 800          | B               | J_2015-02-11_1D1KT401AHUU_EX_20062479 | 2014-06-03        | 1970-01-01 11:52:31.123 | AAPL%           | 8           | NNM             | OR               |
      | Exchange Order | OR                | AAPL             | NNM              | 29        | MLCO           | 2014-06-03   | 1970-01-01 11:52:31.123 | 800          | B               | J_2015-02-11_1D1KT401AHUU_EX_20062496 | 2014-06-03        | 1970-01-01 11:52:31.123 | AAPL%           | 8           | NNM             | OR               |
      | Exchange Order | AD                | AAPL             | NNM              | 29        | MLCO           | 2014-06-03   | 1970-01-01 11:52:31.123 | 800          | B               | J_2015-02-11_1D1KT401AHUU_EX_20062496 | 2014-06-03        | 1970-01-01 11:52:31.123 | AAPL%           | 8           | NNM             | AD               |
      | Exchange Order | NW                | AAPL             | NNM              | 29        | MLCO           | 2014-06-03   | 1970-01-01 11:52:31.123 | 800          | B               | J_2015-02-11_1D1KT401AHUU_EX_20062496 | 2014-06-03        | 1970-01-01 11:52:31.123 | AAPL%           | 8           | NNM             | NW               |
    And apply static data only when userId="filter2" and refId="100004"
      | cmn_rec_type | cmn_event_type_cd | cmn_issue_sym_id | cmn_mkt_class_cd | cmn_bd_nb | cmn_firm_mp_id | cmn_event_dt | cmn_event_tm        | cmn_event_qt | cmn_buy_sell_cd | fo_oats_roe_id | fo_order_rcvd_dt    | fo_ns_cstmr_id | fo_rtd_shrs_qt | fo_issue_sym_id | fo_route_pr  | fo_firm_mp_id | fo_order_shrs_qt | fo_oats_orgng_order_oats_prcsg_dt | fo_oats_for_id |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          |                 | 99900057       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 627.79000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          |                 | 99900058       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 625.70000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          |                 | 99900059       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 627.22000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900060       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 626.46000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900061       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 627.60000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900062       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 630.45000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900063       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 628.74000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 99900064       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 628.36000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 99900065       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 622.36000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 99900066       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 622.36000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900067       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 622.36000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900068       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 622.36000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900069       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 622.36000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | B               | 99900070       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 622.36000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 99900071       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 622.36000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          | SS              | 99900072       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 622.36000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
      | Firm Order   | OR                | AAPL             | NNM              | 5608      | EWTT           | 2014-06-03   | 1970-01-01 09:00:00 | 100          | SS              | 99900073       | 2014-06-03 09:00:00 | 5608           | 800            | AAPL            | 622.36000000 | EWTT          | 800              | 2014-06-03                        | 54391106       |
    And apply static data only when userId="filter2" and refId="100004"
      | cmn_rec_type   | cmn_event_type_cd | cmn_issue_sym_id | cmn_mkt_class_cd | cmn_bd_nb | cmn_firm_mp_id | cmn_event_dt | cmn_event_tm            | cmn_event_qt | cmn_buy_sell_cd | eo_rec_unique_id                      | eo_orgnl_trade_dt | eo_order_rcvd_tm        | eo_issue_sym_id | eo_issue_id | eo_mkt_class_cd |
      | Exchange Order | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 11:52:31.123 | 800          | B               | J_2015-02-11_1D1KT401AHUU_EX_20062479 | 2014-06-03        | 1970-01-01 11:52:31.123 | AAPL            | 8           | NNM             |
    And apply static data only when userId="filter2" and refId="100005"
      | cmn_rec_type | cmn_event_type_cd | cmn_issue_sym_id | cmn_mkt_class_cd | cmn_bd_nb | cmn_firm_mp_id | cmn_event_dt | cmn_event_tm        | cmn_event_qt | cmn_buy_sell_cd | fo_oats_roe_id | fo_order_rcvd_dt    | fo_ns_cstmr_id | fo_rtd_shrs_qt | fo_issue_sym_id | fo_route_pr  | fo_firm_mp_id | fo_order_shrs_qt | fo_oats_orgng_order_oats_prcsg_dt | fo_oats_for_id |
      | Firm Order   | OR                | AAPL             | NNM              | 447       | MLCO           | 2014-06-03   | 1970-01-01 09:00:00 | 800          |                 | 99900057       | 2014-06-03 09:00:00 | 447            | 800            | AAPL            | 627.79000000 | MLCO          | 800              | 2014-06-03                        | 54392173       |
    And generate mart config
    Then user track record should be populated
    And data mart table should be populated
    And mart config table should be populated
    Examples:
      | symbol | firms     | allRelated | fromDate   | fromTime | toDate     | toTime   | recCount | recTypes | userId       | refId    | lastFirmOrderId | lastExchangeOrderId | lastOffExchangeTradeId |
      | AAPL   |           | N          | 2014-06-25 | 10:00:00 | 2014-06-25 | 10:00:00 | 238      | fo,eo    | filter2      | 100003   | 909999999412    | 808888888412        | 707777777412           |
      | AAPL   | HM01      | N          | 2014-06-25 | 10:00:00 | 2014-06-25 | 10:00:00 | 43       | fo,eo    | filter2      | 100004   | 909999999650    | 808888888650        | 707777777650           |
      | AAPL   |           | N          | 2014-06-25 | 10:00:00 | 2014-06-25 | 10:00:00 | 27       | fo,eo    | sheppjak     | test0003 | 909999999673    | 808888888673        | 707777777673           |
      | AAPL   |           | N          | 2014-06-25 | 10:00:00 | 2014-06-25 | 10:00:00 | 43       | fo,eo    | filter2      | 100005   | 909999999700    | 808888888700        | 707777777700           |
      | AAPL   | HM01,HM02 | N          | 2014-06-25 | 10:00:00 | 2014-06-25 | 10:00:00 | 43       | fo,eo    | filter2      | 100006   | 909999999743    | 808888888743        | 707777777743           |
      | AAPL   |           | N          | 2014-06-25 | 10:00:00 | 2014-06-25 | 10:00:00 | 27       | fo,eo    | tst_analyst1 | 1111111  | 909999999770    | 808888888770        | 707777777770           |
