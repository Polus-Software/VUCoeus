CREATE OR REPLACE procedure COEUS.dw_get_user
   ( AW_USER_ID IN osp$user.user_id%TYPE,
      cur_generic IN OUT result_sets.cur_generic) is 

ll_Count    number;

begin

 
  select count(*) 
  into ll_Count
  from osp$user
  where UPPER(USER_ID) = UPPER(AW_USER_ID);
  
  if ll_Count = 0 then
  
     open cur_generic for
     select p.user_name as user_id,
            p.full_name as user_name,
            'N' as non_mit_person_flag,
            p.person_id,
            null as user_type,
            p.home_unit as unit_number,
            p.status as status,
            p.update_timestamp,
            p.update_user,
            u.unit_name 
     from osp$person p, osp$unit u
     where upper(p.user_name) = upper(AW_USER_ID) and
           p.home_unit = u.unit_number (+);
     
  else
       open cur_generic for
       SELECT US.*,
                UN.UNIT_NAME
        FROM OSP$USER US,
                OSP$UNIT UN  
       WHERE UPPER(USER_ID) = UPPER(AW_USER_ID)
          AND UN.UNIT_NUMBER = US.UNIT_NUMBER ;
  end if;
  
end;
/
