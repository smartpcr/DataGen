WITH issueSymbols AS (
  SELECT issue_id, xchng_sym_id as issue_sym_id
  FROM issue_ref
  WHERE
    xchng_sym_id=@symbol
  UNION
  SELECT issue_id, trd_sym_id as issue_sym_id
  FROM issue_ref
  WHERE
    trd_sym_id=@symbol
  UNION
  SELECT issue_id, siac_sym_id as issue_sym_id
  FROM issue_ref
  WHERE
    siac_sym_id=@symbol
  UNION
  SELECT issue_id, oats_rptng_sym_id as issue_sym_id
  FROM issue_ref
  WHERE
    oats_rptng_sym_id=@symbol
  UNION
  SELECT issue_id, nyse_sym_id as issue_sym_id
  FROM issue_ref
  WHERE
    nyse_sym_id=@symbol
  UNION
  SELECT issue_id, arca_sym_id as issue_sym_id
  FROM issue_ref
  WHERE
    arca_sym_id=@symbol
  UNION
  SELECT issue_id, cms_dot_dlmtd_sym_id as issue_sym_id
  FROM issue_ref
  WHERE
    cms_dot_dlmtd_sym_id=@symbol
)
SELECT
  issue_id,
  issue_sym_id
FROM issueSymbols