WITH crb AS (
  SELECT ns_cstmr_id
  FROM
    firm_dm
  WHERE
    firm_mp_id IN (@firms)
)
SELECT
  t1.firm_mp_id,
  t1.ns_cstmr_id
FROM
  firm_dm t1
  INNER JOIN crb t2 ON t2.ns_cstmr_id=t1.ns_cstmr_id