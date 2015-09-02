WITH crd AS (
  SELECT crd_nb
  FROM prtcp_dm
  WHERE
    crd_nb <> -9
    AND (
      firm_mp_id IN (@firms)
      OR mbr_id IN (@firms)
    )
),
participantIds AS (
  SELECT prtcp_dm_id
  FROM
    prtcp_dm t1
    INNER JOIN crd t2 ON t2.crd_nb=t1.crd_nb
  WHERE
    firm_mp_id IN (@firms)
    OR mbr_id IN (@firms)
)
SELECT
  t1.crd_nb,
  t1.firm_mp_id,
  t1.mbr_id
FROM
  prtcp_dm t1
  INNER JOIN participantIds t2 ON t2.prtcp_dm_id=t1.prtcp_dm_id