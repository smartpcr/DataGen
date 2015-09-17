
Feature: can parse feature file

  Scenario: can read new orders seed file
    Given version 9
    Given a seed file with name "new_orders.seed"
    When parse seed file
    Then I should get 124 table columns
      | name                    | ordinal | dbType  | size |
      | oats_roe_id             | 1       | BigInt  | 0    |
      | desk_spcl_hndlg_comb_cd | 124     | Varchar | 20   |

  Scenario: can read new orders summary seed file
    Given a seed file with name "new_orders_summary.seed"
    When parse seed file
    Then I should get 124 table columns
      | name                    | ordinal | dbType  | size |
      | oats_roe_id             | 1       | BigInt  | 0    |
      | desk_spcl_hndlg_comb_cd | 124     | Varchar | 20   |

  Scenario: can read process orders seed file
    Given a seed file with name "process_orders.seed"
    When parse seed file
    Then I should get 124 table columns
      | name                    | ordinal | dbType  | size |
      | oats_roe_id             | 1       | BigInt  | 0    |
      | desk_spcl_hndlg_comb_cd | 124     | Varchar | 20   |

  Scenario: can read process orders summary seed file
    Given a seed file with name "process_orders_summary.seed"
    When parse seed file
    Then I should get 124 table columns
      | name                    | ordinal | dbType  | size |
      | oats_roe_id             | 1       | BigInt  | 0    |
      | desk_spcl_hndlg_comb_cd | 124     | Varchar | 20   |

  Scenario: can read ned orders seed file
    Given a seed file with name "ned_orders.seed"
    When parse seed file
    Then I should get 141 table columns
      | name            | ordinal | dbType  | size |
      | REC_UNIQUE_ID   | 1       | Varchar | 120  |
      | CONTRA_CPCTY_CD | 141     | Varchar | 1    |

  Scenario: can read ned orders summary seed file
    Given a seed file with name "ned_orders_summary.seed"
    When parse seed file
    Then I should get 141 table columns
      | name            | ordinal | dbType  | size |
      | REC_UNIQUE_ID   | 1       | Varchar | 120  |
      | CONTRA_CPCTY_CD | 141     | Varchar | 1    |

  Scenario: can read off exchange trades seed file
    Given a seed file with name "off_exchange_trades.seed"
    When parse seed file
    Then I should get 120 table columns
      | name                          | ordinal | dbType  | size |
      | TRADE_RPT_DT                  | 1       | Date    | 0    |
      | PREV_TRD_FINRA_CNTRA_CNTRL_NB | 120     | Varchar | 30   |
