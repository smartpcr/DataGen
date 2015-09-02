SELECT ns_cstmr_id, firm_mp_id
FROM
  firm_dm
WHERE
  firm_mp_id IN (@firms)