SELECT crd_nb, firm_mp_id, mbr_id
FROM prtcp_dm
WHERE
  firm_mp_id IN (@firms)
  OR mbr_id IN (@firms)