@Details @Data @Filter
Feature: fix data constraints from filter expressions

  Scenario: Make sure filter condition is satisfied in generated data
    Given detail data based on display rule version 8
    When generate data with the following constraints
      | Count | FilterExpression                                                                |
      | 10    | cmn_rec_type='Firm Order' AND cmn_event_type_cd='OR' AND fo_issue_sym_id='AAPL' |