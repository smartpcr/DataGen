WITH issueIds AS (
  SELECT issue_id
  FROM symbol_dm
  WHERE issue_symbol_id=@symbolId
  UNION
  SELECT issue_id
  FROM issue_ref
  WHERE
    xchng_sym_id=@symbolId
    OR trd_sym_id=@symbolId
    OR sisc_sym_id=@symbolId
    OR oats_rptng_sym_id=@symbolId
    OR nyse_sym_id=@symbolId
    OR arca_sym_id=@symbolId
    OR cms_dot_dlmtd_sym_id=@symbolId
)
SELECT
  s.issue_id,
  s.issue_sym_id
FROM
  symbol_dm s
  INNER JOIN issueIds i ON i.issue_id=s.issue_id