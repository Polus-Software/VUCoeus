CREATE OR REPLACE procedure COEUS.dw_get_a_apprvd_subcontracts
   ( AW_MIT_AWARD_NUMBER IN OSP$AWARD_APPROVED_SUBCONTRACT.MIT_AWARD_NUMBER%TYPE,
      cur_generic IN OUT result_sets.cur_generic) is

begin

open cur_generic for
    /* JM 8-13-2013 added organization id; 11/23/2013 removed match on max sequence to get all subs; 6-22-2016 reworked for location type */
    SELECT s.*, l.location_type_desc
    FROM OSP$AWARD_APPROVED_SUBCONTRACT s, OSP$LOCATION_TYPE l
    WHERE s.MIT_AWARD_NUMBER = AW_MIT_AWARD_NUMBER
    AND s.LOCATION_TYPE_CODE = l.LOCATION_TYPE_CODE;

end;
/
