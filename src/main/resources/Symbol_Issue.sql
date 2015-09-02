WITH issueIds AS (
  SELECT issue_id
  FROM symbol_dm
  WHERE issue_sym_id=@symbol
  UNION
  SELECT issue_id
  FROM issue_ref
  WHERE
    xchng_sym_id=@symbol
    OR trd_sym_id=@symbol
    OR siac_sym_id=@symbol
    OR oats_rptng_sym_id=@symbol
    OR nyse_sym_id=@symbol
    OR arca_sym_id=@symbol
    OR cms_dot_dlmtd_sym_id=@symbol
)
SELECT
  s.issue_id,
  s.issue_sym_id
FROM
  symbol_dm s
  INNER JOIN issueIds i ON i.issue_id=s.issue_id