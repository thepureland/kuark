CREATE OR REPLACE FUNCTION RANDOM_UUID()
 RETURNS uuid
 LANGUAGE c
 PARALLEL SAFE
AS '$libdir/pgcrypto', $function$pg_random_uuid$function$;

-- 剩余部分与h2完全一致