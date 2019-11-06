CREATE OR REPLACE FUNCTION COEUS.FN_FEED_INST_PROP
        (as_dev_proposal_num IN OSP$EPS_PROPOSAL.PROPOSAL_NUMBER%TYPE ,
         as_inst_prop_num IN OSP$PROPOSAL.PROPOSAL_NUMBER%TYPE,
         as_generate IN VARCHAR2,
         as_submission_type IN VARCHAR2,
          as_submission_status IN VARCHAR2,
       as_user_id IN VARCHAR2)
RETURN NUMBER IS
    ls_submission_type      VARCHAR2(1);
   li_sequence              NUMBER;
    li_count                        NUMBER;
--    ld_submit_date             OSP$EPS_PROP_APPROVAL.SUBMISSION_DATE%TYPE;
    ld_submit_date             OSP$ROUTING_DETAILS.SUBMISSION_DATE%TYPE;
    ls_inst_prop_number      OSP$PROPOSAL.PROPOSAL_NUMBER%TYPE;
   ls_proposal_type         OSP$EPS_PROPOSAL.PROPOSAL_TYPE_CODE%TYPE;
    ls_budget_status            OSP$EPS_PROPOSAL.BUDGET_STATUS%TYPE;
    ls_modular_budget_flag  OSP$BUDGET.MODULAR_BUDGET_FLAG%TYPE; --case 2760
    li_version                    OSP$BUDGET.VERSION_NUMBER%TYPE;
    li_total_direct_init        OSP$BUDGET_PERIODS.TOTAL_DIRECT_COST%TYPE;
    li_total_direct_tot        OSP$BUDGET.TOTAL_DIRECT_COST%TYPE;
    li_total_indirect_init    OSP$BUDGET_PERIODS.TOTAL_INDIRECT_COST%TYPE;
    li_total_indirect_tot    OSP$BUDGET.TOTAL_INDIRECT_COST%TYPE;
    ld_end_date_init            OSP$BUDGET_PERIODS.END_DATE%TYPE;
    ld_start_dt_init           OSP$BUDGET_PERIODS.START_DATE%TYPE;
    li_spec_rev_count            NUMBER;
    ls_spec_rev_indic         VARCHAR2(2);
    ll_SPECIAL_REVIEW_NUMBER OSP$EPS_PROP_SPECIAL_REVIEW.SPECIAL_REVIEW_NUMBER%TYPE;
   ll_SPECIAL_REVIEW_CODE     OSP$EPS_PROP_SPECIAL_REVIEW.SPECIAL_REVIEW_CODE%TYPE;
    ll_APPROVAL_TYPE_CODE     OSP$EPS_PROP_SPECIAL_REVIEW.APPROVAL_TYPE_CODE%TYPE;
   ll_PROTOCOL_NUMBER         OSP$EPS_PROP_SPECIAL_REVIEW.PROTOCOL_NUMBER%TYPE;
   ld_APP_DATE                 OSP$EPS_PROP_SPECIAL_REVIEW.APPLICATION_DATE%TYPE;
   ld_APPROVAL_DATE             OSP$EPS_PROP_SPECIAL_REVIEW.APPROVAL_DATE%TYPE;
    ll_COMMENTS                    OSP$EPS_PROP_SPECIAL_REVIEW.COMMENTS%TYPE;
    --**ENH010 begin
    li_oh_rate_class_code   OSP$BUDGET.OH_RATE_CLASS_CODE%TYPE;
    li_idc_type_code        NUMBER;
    li_cost_sharing_count    NUMBER;
    li_idc_rate_count            NUMBER;
    li_science_code_count   NUMBER;
    ls_cost_sharing_indic    VARCHAR2(2);
    ls_idc_rate_indic            VARCHAR2(2);
    ls_science_code_indic   VARCHAR2(2);
/* JM 6-2-2011 removed changes for key person per 4.4.2
    -- 3823: Key Person record needed in IP and Award - Start
    ls_key_person_indic        VARCHAR(2);
    li_key_person_count     NUMBER :=0;
    li_key_person_in_prev_seq       NUMBER :=0;
    is_ll_person_present  NUMBER :=0;
    ll_person_id OSP$PROPOSAL_KEY_PERSONS.PERSON_ID%TYPE;
    ls_person_name OSP$PROPOSAL_KEY_PERSONS.PERSON_NAME%TYPE;
    ls_project_role OSP$PROPOSAL_KEY_PERSONS.PROJECT_ROLE%TYPE;
    ls_faculty_flag OSP$PROPOSAL_KEY_PERSONS.FACULTY_FLAG%TYPE;
    ls_non_mit_person_flag OSP$PROPOSAL_KEY_PERSONS.NON_MIT_PERSON_FLAG%TYPE;
    ll_percentage_effort OSP$PROPOSAL_KEY_PERSONS.PERCENTAGE_EFFORT%TYPE;
*/    -- 3823: Key Person record needed in IP and Award -  End
    --**ENH010 end
    ls_osp_admin                VARCHAR2(9);  --ENH018
    ldt_CreateTimestamp        osp$proposal.CREATE_TIMESTAMP%type;
    li_spl_rev_in_prev_seq     NUMBER;
    insert_details_error        EXCEPTION;
    insert_prop_error            EXCEPTION;
    insert_invest_error        EXCEPTION;
/* JM 6-2-2011 added next two fields per 4.4.2 */
    insert_keyperson_error  EXCEPTION;
    insert_keyperson_units_error    EXCEPTION;
/* END */
/* JM 6-16-2014 export control special review and clinical trial */
    is_export_control  integer;
    export_control_spcode  integer;
/* JM END */

/* JM 3-31-2016 roll EIN_DUNS forward to IP */
    orgId  OSP$ORGANIZATION.ORGANIZATION_ID%TYPE;
    orgEIN  OSP$ORGANIZATION.FEDRAL_EMPLOYER_ID%TYPE;
    orgDUNS  OSP$ORGANIZATION.DUNS_NUMBER%TYPE;
/* JM END */

/* PP 1-2015 Genome research special review and other tab entry */
    genome_spcode       integer;
    is_genome_nih       integer;
/* PP END */
    insert_units_error        EXCEPTION;
    insert_spec_error            EXCEPTION;
    update_admin_error      EXCEPTION;
    insert_inv_cert_error   EXCEPTION;
--ENH066 Begin
    ls_code                         VARCHAR2(1);
--    ls_parameter_value                OSP$PARAMETER.VALUE%TYPE;
    parameter_error             EXCEPTION;
--ENH066 End
-- Case 3435 begin
    li_LogCount             number;
    ls_LogNumber            osp$proposal_log.PROPOSAL_NUMBER%type;
-- Case 3435 End
/*****************************************************
Case 1463 Fixed on 2/11/05
For revisions when development proposal was made a new
sequence of institute proposal, sponsor_proposal_number
was not being brought forward from institute proposal.
******************************************************/
/******************************************************
Case 3435 - Linking dev proposal with proposal log
*******************************************************/
--Case#1463 begin
   ls_SponsorProposalNumber OSP$PROPOSAL.SPONSOR_PROPOSAL_NUMBER%TYPE;
--Case#1463 End
-- This function is called from Powerbuilder after the successful approval of the
-- last stop in the proposal routing. The proposal's status has just been changed to
-- either Approved or Submitted.
-- The function is also called from Powerbuilder if the proposal has previously been
-- Approved, and is ready to be submitted.
-- It feeds the institute proposal tables.
--
-- arguments:
--            as_dev_proposal_num - the development proposal number
--       as_inst_prop_num    - the original inst prop number (for revisions)
--                                         NULL if not a revision
--            as_generate             - 'N' for DO NOT generate an inst prop number
--                                         'G' for DO generate an inst prop number
--       as_submission_type  - 'S' - electronic submission
--                             'P' - paper submission
--       as_submission_status- 'S' - proposal is being submitted
--                             'P' - Previously approved - now being submitted -
--                   CODE APPEARS TO BE LOOKING FOR AN 'S' or an 'A' not a 'P'
--                   and 'A' appears to be the approval only not being submitted
--       as_user_id               -  the logged on user
-- RETURNS:  1 for success
--       <0  for failure
--
-- Inserts rows into
--              OSP$PROPOSAL
--              OSP$PROPOSAL_ADMIN_DETAILS
--             OSP$PROPOSAL_INVESTIGATORS
--              OSP$PROPOSAL_SPECIAL_REVIEW
--              OSP$PROPOSAL_UNITS
--                OSP$PROPOSAL_INV_CERTIFICATION
--**ENH010 begin
--                OSP$PROPOSAL_COST_SHARING
--                OSP$PROPOSAL_IDC_TATE
--                OSP$PROPOSAL_SCIENCE_CODE
--**ENH010 end
--COEUSQA-3616 Transition Prop dev disclosures to inst proposals start
li_coi_return NUMBER;
--COEUSQA-3616 Transition Prop dev disclosures to inst proposals end

-- JM 6-26-2013 return values from stored procedure for contract admin
as_unit_number OSP$UNIT.UNIT_NUMBER%TYPE;
rt_admin_id  OSP$UNIT.OSP_ADMINISTRATOR%TYPE;
rt_administrator  osp$person.full_name%TYPE;
-- JM END

BEGIN
    /* JM 11-4-2011 added catch for Pre-Application proposal types;
        don't want to generate institute proposal for these */
    SELECT proposal_type_code into ls_proposal_type
    from coeus.osp$eps_proposal
    where proposal_number = as_dev_proposal_num;

    if ls_proposal_type <> 2 then
    /* END */
    dbms_output.put_line('as_inst_prop_num is ' || as_inst_prop_num);
    IF as_submission_status <> 'P' THEN
        -- if status is 'P', rows have already been inserted into admin details
        BEGIN
        /*SELECT MIN(SUBMISSION_DATE)
        INTO   ld_submit_date
        FROM   OSP$EPS_PROP_APPROVAL
        WHERE  PROPOSAL_NUMBER = as_dev_proposal_num;*/
        SELECT MIN(SUBMISSION_DATE)
        INTO   ld_submit_date
        FROM   OSP$ROUTING_DETAILS
        WHERE  ROUTING_NUMBER = (SELECT MAX(TO_NUMBER(ROUTING_NUMBER))
                                      FROM OSP$ROUTING
                                 WHERE MODULE_ITEM_KEY = as_dev_proposal_num
                                 AND   MODULE_CODE = 3);
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(-20100, 'No data found when selecting ' ||
                    ' from OSP$EPS_PROP_APPROVAL in fn_feed_inst_prop');
                RETURN -1;
        END;
        dbms_output.put_line('before insert into admin details table');
        INSERT INTO OSP$PROPOSAL_ADMIN_DETAILS
            (DEV_PROPOSAL_NUMBER,
            INST_PROPOSAL_NUMBER,
             INST_PROP_SEQUENCE_NUMBER,
             DATE_SUBMITTED_BY_DEPT,
            DATE_RETURNED_TO_DEPT,
             DATE_APPROVED_BY_OSP,
            DATE_SUBMITTED_TO_AGENCY,
            INST_PROP_CREATE_DATE,
            INST_PROP_CREATE_USER,
            SIGNED_BY,
            SUBMISSION_TYPE )
        VALUES
            (as_dev_proposal_num,
            NULL,
            1,
              ld_submit_date    ,
            NULL,
            SYSDATE,
            NULL,
            NULL,
            NULL,
            as_user_id,
            NULL);
          IF SQL%ROWCOUNT = 0 THEN
            RAISE insert_details_error;
          END IF;
        INSERT INTO OSP$PROPOSAL_INV_CERTIFICATION
         ( PROPOSAL_NUMBER,
               PERSON_ID,
                CERTIFIED_FLAG,
                DATE_CERTIFIED,
                DATE_RECEIVED_BY_OSP,
                 UPDATE_USER,
                 UPDATE_TIMESTAMP )
        SELECT   as_dev_proposal_num,
                PERSON_ID,
                'N',
                NULL,
                NULL,
                as_user_id,
                SYSDATE
        FROM     OSP$EPS_PROP_INVESTIGATORS
        WHERE    OSP$EPS_PROP_INVESTIGATORS.proposal_number = as_dev_proposal_num;
        IF SQL%ROWCOUNT = 0 THEN
            RAISE insert_inv_cert_error;
        END IF;
         IF as_submission_status  = 'A'  THEN
            -- this is an Approval only - no submission yet. We are done.
            RETURN 1;
         END IF;
    END IF;
    dbms_output.put_line('as_generate is ' || as_generate || ' and as inst prop num is ' ||
        as_inst_prop_num);
    IF as_generate = 'G' THEN
        --Case 3435 Begin
         --generate new number only if a proposal log has not been generated for the
         --dev proposal
         select count(*)
         into li_LogCount
         from OSP$EPS_PROP_PROPOSAL_LOG_LINK
         where DEV_PROPOSAL_NUMBER = as_dev_proposal_num;
         if li_LogCount = 1 then
            select INST_PROPOSAL_NUMBER
            into ls_inst_prop_number
            from OSP$EPS_PROP_PROPOSAL_LOG_LINK
            where DEV_PROPOSAL_NUMBER = as_dev_proposal_num;
            update osp$proposal_log
            set LOG_STATUS = 'S'
            where PROPOSAL_NUMBER = ls_inst_prop_number;
         else
            ls_inst_prop_number := fn_get_next_inst_prop_number;
         end if;
        --Case 3435 End
        if ls_inst_prop_number = '' then
            -- error
            return -1;
        end if;
        li_sequence := 1;
        ldt_CreateTimestamp := sysdate;
    ELSE
        -- do not generate a new institute proposal number
        -- as_inst_prop_num contains the original institute proposal number for a revision
        -- we want to insert new a sequence number in original proposal
        --fix031 commented out following if statement
        --if  not(as_inst_prop_num is NULL) then
            BEGIN
                ls_inst_prop_number := as_inst_prop_num;
                dbms_output.put_line('ls_inst_prop_number is ' || ls_inst_prop_number);
                SELECT MAX(sequence_number)
                INTO   li_sequence
                FROM   OSP$PROPOSAL
                WHERE  PROPOSAL_NUMBER = ls_inst_prop_number;
                EXCEPTION
                WHEN NO_DATA_FOUND THEN
                    RAISE_APPLICATION_ERROR(-20100, 'No data found when selecting ' ||
                    ' max sequence number from OSP$PROPOSAL in fn_feed_inst_prop');
                    RETURN -1;
            END;
            --Case#1463 Begin
            BEGIN
                 SELECT sponsor_proposal_number, create_timestamp
                 INTO ls_SponsorProposalNumber, ldt_CreateTimestamp
                 FROM OSP$PROPOSAL
                 WHERE proposal_number = ls_inst_prop_number AND
                        sequence_number = li_sequence;
                EXCEPTION
                WHEN NO_DATA_FOUND THEN
                --case 3620 start
                --    RAISE_APPLICATION_ERROR(-20100, 'No data found when selecting ' ||
                --    ' Sponsor Proposal Number from OSP$PROPOSAL in fn_feed_inst_prop');
                --    RETURN -1;
                    ls_SponsorProposalNumber := NULL;
                --case 3620 end
            END;
            --case#1463 End
            li_Sequence := li_sequence + 1;
        --end if;
        --Case 3435 Begin
         --We are not generating a new inst proposal.
         --Check to see if this dev proposal is linked to a proposal log
         --if Yes, make the log Void
         --Update link table to reflect new inst proposal
         select count(*)
         into li_LogCount
         from OSP$EPS_PROP_PROPOSAL_LOG_LINK
         where DEV_PROPOSAL_NUMBER = as_dev_proposal_num;
         if li_LogCount > 0 then
            select INST_PROPOSAL_NUMBER
            into ls_LogNumber
            from OSP$EPS_PROP_PROPOSAL_LOG_LINK
            where DEV_PROPOSAL_NUMBER = as_dev_proposal_num;
            --Update status of old proposal log to Void
            update osp$proposal_log
            set LOG_STATUS = 'V',
                COMMENTS = COMMENTS || ' *** ' || 'Dev Proposal linked to inst proposal# ' || ls_inst_prop_number
            where PROPOSAL_NUMBER = ls_LogNumber;
            update OSP$EPS_PROP_PROPOSAL_LOG_LINK
            set INST_PROPOSAL_NUMBER = ls_inst_prop_number,
                SEQUENCE_NUMBER = li_Sequence
            where DEV_PROPOSAL_NUMBER = as_dev_proposal_num;
         end if;
    END IF;
    ----------------------------------------------------
    -- now update the admin details table for submission
    ----------------------------------------------------
    dbms_output.put_line('before update of proposal admin details');
    UPDATE OSP$PROPOSAL_ADMIN_DETAILS
    SET    INST_PROPOSAL_NUMBER = ls_inst_prop_number,
             INST_PROP_SEQUENCE_NUMBER = li_sequence,
              INST_PROP_CREATE_DATE = SYSDATE,
             INST_PROP_CREATE_USER = as_user_id,
             DATE_SUBMITTED_TO_AGENCY = SYSDATE,
             SUBMISSION_TYPE = as_submission_type
    WHERE  DEV_PROPOSAL_NUMBER = as_dev_proposal_num;
        IF SQL%NOTFOUND THEN
                    RAISE update_admin_error;
          END IF;
    --------------------------------------------------
    -- now insert row into institute proposal table
    --------------------------------------------------
    -- first get other info we need from budget tables and special review
    BEGIN
    dbms_output.put_line('before select from budget status');
    SELECT BUDGET_STATUS
    INTO   ls_budget_status
    FROM   OSP$EPS_PROPOSAL
    WHERE  PROPOSAL_NUMBER = as_dev_proposal_num;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20100, 'No data found when selecting ' ||
                    ' budget status from OSP$EPS_PROPOSAL in fn_feed_inst_prop');
        RETURN -1;
    END;
    BEGIN
    dbms_output.put_line('before selects from budget stuff');
    IF ls_budget_status = 'C' THEN
    --Case 3364 Begin
        BEGIN
            SELECT         VERSION_NUMBER
                INTO    li_version
                FROM   OSP$BUDGET
                WHERE  PROPOSAL_NUMBER = as_dev_proposal_num
                AND     FINAL_VERSION_FLAG    = 'Y';
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                li_version := 0;
        END;
        IF li_version = 0 THEN
        BEGIN
            SELECT         MAX(VERSION_NUMBER)
                INTO    li_version
                FROM   OSP$BUDGET
                WHERE  PROPOSAL_NUMBER = as_dev_proposal_num;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                li_version := 1;
        END;
        END IF;
        IF li_Version IS NULL THEN
            li_version := 1;
        END IF;
     -- Case 3364 End
        --case 2760 start
        SELECT MODULAR_BUDGET_FLAG
        INTO   ls_modular_budget_flag
        FROM   OSP$BUDGET
        WHERE  PROPOSAL_NUMBER = as_dev_proposal_num
        AND      VERSION_NUMBER = li_version;
        IF ls_modular_budget_flag = 'N' THEN
        --case 2760 end
            SELECT TOTAL_DIRECT_COST,
                     TOTAL_INDIRECT_COST,
                     VERSION_NUMBER,
                     OH_RATE_CLASS_CODE    --FOR ENH010
            INTO   li_total_direct_tot, li_total_indirect_tot, li_version, li_oh_rate_class_code
            FROM   OSP$BUDGET
            WHERE  PROPOSAL_NUMBER = as_dev_proposal_num
            AND     VERSION_NUMBER = li_version;
            SELECT TOTAL_DIRECT_COST,
                     TOTAL_INDIRECT_COST,
                     START_DATE,
                     END_DATE
            INTO   li_total_direct_init,
                    li_total_indirect_init,
                     ld_start_dt_init,
                    ld_end_date_init
            FROM   OSP$BUDGET_PERIODS
            WHERE     PROPOSAL_NUMBER = as_dev_proposal_num
            AND    BUDGET_PERIOD = 1
            AND    VERSION_NUMBER = li_version;
            --case 2760 start
          ELSE
              SELECT     OH_RATE_CLASS_CODE
              INTO    li_oh_rate_class_code
              FROM   OSP$BUDGET
              WHERE  PROPOSAL_NUMBER = as_dev_proposal_num
              AND     VERSION_NUMBER = li_version;
              BEGIN
                  SELECT SUM(TOTAL_DIRECT_COST)
                  INTO   li_total_direct_tot
                  FROM   OSP$BUDGET_MODULAR
                  WHERE  PROPOSAL_NUMBER = as_dev_proposal_num
                  AND    VERSION_NUMBER    = li_version;
              EXCEPTION
                  WHEN NO_DATA_FOUND THEN
                  li_total_direct_tot:= 0;
              END;
              BEGIN
                  SELECT SUM(FUNDS_REQUESTED)
                  INTO   li_total_indirect_tot
                  FROM   OSP$BUDGET_MODULAR_IDC
                  WHERE  PROPOSAL_NUMBER = as_dev_proposal_num
                  AND    VERSION_NUMBER    = li_version;
              EXCEPTION
                  WHEN NO_DATA_FOUND THEN
                  li_total_indirect_tot:= 0;
              END;
              SELECT  START_DATE, END_DATE
            INTO    ld_start_dt_init,
                    ld_end_date_init
            FROM   OSP$BUDGET_PERIODS
            WHERE     PROPOSAL_NUMBER = as_dev_proposal_num
            AND    BUDGET_PERIOD = 1
            AND    VERSION_NUMBER = li_version;
              BEGIN
                  SELECT TOTAL_DIRECT_COST
                  INTO   li_total_direct_init
                  FROM   OSP$BUDGET_MODULAR
                  WHERE  PROPOSAL_NUMBER = as_dev_proposal_num
                  AND    BUDGET_PERIOD = 1
                  AND    VERSION_NUMBER    = li_version;
            EXCEPTION
                    WHEN NO_DATA_FOUND THEN
                    li_total_direct_init:= 0;
            END;
        BEGIN
                  SELECT SUM(FUNDS_REQUESTED)
                  INTO   li_total_indirect_init
                  FROM   OSP$BUDGET_MODULAR_IDC
                  WHERE  PROPOSAL_NUMBER = as_dev_proposal_num
                  AND    BUDGET_PERIOD = 1
                  AND    VERSION_NUMBER    = li_version;
        EXCEPTION
                  WHEN NO_DATA_FOUND THEN
                  li_total_indirect_init:= 0;
          END;
              END IF;
        --case 2760 end
    ELSE
            dbms_output.put_line(' no budget');
        -- no budget
        li_total_direct_tot := 0;
        li_total_indirect_tot := 0;
        li_total_direct_init := 0;
       li_total_indirect_init := 0;
      SELECT  REQUESTED_START_DATE_INITIAL, REQUESTED_END_DATE_INITIAL
      INTO    ld_start_dt_init, ld_end_date_init
        FROM    OSP$EPS_PROPOSAL
        WHERE   PROPOSAL_NUMBER = as_dev_proposal_num;
    END IF;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20100, 'No data found when selecting ' ||
                    ' from OSP$BUDGET OR BUDGET_PERIODS in fn_feed_inst_prop');
        RETURN -1;
    END;
    -- get special review info
    /*
    -- 3847: Unique constraint error with resubmission - Start
--     SELECT COUNT(*)
--         INTO   li_spec_rev_count
--         FROM   OSP$EPS_PROP_SPECIAL_REVIEW
--          WHERE  PROPOSAL_NUMBER = as_dev_proposal_num;
    SELECT VALUE
    INTO ls_parameter_value
    FROM OSP$PARAMETER
    WHERE PARAMETER = 'SPL_REV_TYPE_CODE_HUMAN';
    IF as_generate <> 'G' THEN
        SELECT COUNT(*)
        INTO   li_spec_rev_count
        FROM   OSP$EPS_PROP_SPECIAL_REVIEW
         WHERE  PROPOSAL_NUMBER = as_dev_proposal_num
        AND    PROTOCOL_NUMBER NOT IN ( SELECT DISTINCT PROTOCOL_NUMBER
                                                   FROM OSP$PROPOSAL_SPECIAL_REVIEW
                                        WHERE PROPOSAL_NUMBER = ls_inst_prop_number
                                        AND SPECIAL_REVIEW_CODE = ls_parameter_value );
    ELSE
        SELECT COUNT(*)
        INTO   li_spec_rev_count
        FROM   OSP$EPS_PROP_SPECIAL_REVIEW
         WHERE  PROPOSAL_NUMBER = as_dev_proposal_num;
    END IF;
    -- 3847: Unique constraint error with resubmission - End
    */
    SELECT COUNT(*)
    INTO   li_spec_rev_count
    FROM   OSP$EPS_PROP_SPECIAL_REVIEW
       WHERE  PROPOSAL_NUMBER = as_dev_proposal_num;
     /* JM 6-19-2014 set indicator for export control */
     if li_spec_rev_count = 0 then
        li_spec_rev_count := FN_VU_IS_EXPORT_CONTROL(as_dev_proposal_num);
     end if;
     /* JM END */     
     if li_spec_rev_count = 0 then
        li_spec_rev_count := FN_VU_IS_GENOME_NIH(as_dev_proposal_num);
     end if;
    -- 4154: Problems in IRB Linking - Start
--     IF li_spec_rev_count > 0 THEN
--         ls_spec_rev_indic := 'P0';
--     ELSE
--       ls_spec_rev_indic := 'N0';
--     END IF;
    IF li_spec_rev_count > 0 THEN -- If Special Review data is present in Development Proposal
        ls_spec_rev_indic := 'P1';
    ELSE
      IF as_generate <> 'G' THEN  -- If the mode is NOT generate new IP
          SELECT COUNT(*)           --  Get the total number of Special Review Count from the Prev Sequence of IP
        INTO li_spl_rev_in_prev_seq
        FROM OSP$PROPOSAL_SPECIAL_REVIEW
        WHERE PROPOSAL_NUMBER =  as_inst_prop_num
        AND SEQUENCE_NUMBER = li_Sequence - 1;
         IF li_spl_rev_in_prev_seq > 0    THEN     -- If there are no Special Reviews in Previous Sequence of IP
            ls_spec_rev_indic := 'N1';
         ELSE
          ls_spec_rev_indic := 'N0';
         END IF;
      ELSE                                -- If the Mode is Generate New IP
         ls_spec_rev_indic := 'N0';
      END IF;
    END IF;
    -- 4154: Problems in IRB Linking - End
    --**ENH010 begin
    -- get cost sharing info
    SELECT COUNT(*)
   INTO   li_cost_sharing_count
    FROM   OSP$EPS_PROP_COST_SHARING
     WHERE  PROPOSAL_NUMBER = as_dev_proposal_num;
    IF li_cost_sharing_count > 0 THEN
        ls_cost_sharing_indic := 'P0';
    ELSE
      ls_cost_sharing_indic := 'N0';
    END IF;
    -- get idc rate(under recovery) info
    SELECT COUNT(*)
   INTO   li_idc_rate_count
    FROM   OSP$EPS_PROP_IDC_RATE
     WHERE  PROPOSAL_NUMBER = as_dev_proposal_num;
    IF li_idc_rate_count > 0 THEN
        ls_idc_rate_indic := 'P0';
    ELSE
      ls_idc_rate_indic := 'N0';
    END IF;
    -- get science code info
    SELECT COUNT(*)
   INTO   li_science_code_count
    FROM   OSP$EPS_PROP_SCIENCE_CODE
     WHERE  PROPOSAL_NUMBER = as_dev_proposal_num;
    IF li_science_code_count > 0 THEN
        ls_science_code_indic := 'P0';
    ELSE
      ls_science_code_indic := 'N0';
    END IF;
/* JM 6-2-2011 removed changes for key person per 4.4.2
    -- 3823: Key Person record needed in IP and Award - Start
    SELECT COUNT(*)
    INTO   li_key_person_count
    FROM   OSP$EPS_PROP_KEY_PERSONS
       WHERE  PROPOSAL_NUMBER = as_dev_proposal_num;
    IF li_key_person_count > 0 THEN
        ls_key_person_indic    := 'P1';
    ELSE
      IF as_generate <> 'G' THEN  -- If the mode is NOT generate new IP
          SELECT COUNT(*)           --  Get the total number of SKey Person Count from the Prev Sequence of IP
        INTO li_key_person_in_prev_seq
        FROM OSP$PROPOSAL_KEY_PERSONS
        WHERE PROPOSAL_NUMBER =  as_inst_prop_num
        AND SEQUENCE_NUMBER = li_Sequence - 1;
         IF li_key_person_in_prev_seq > 0    THEN     -- If there are no Key Person in Previous Sequence of IP
            ls_key_person_indic := 'N1';
         ELSE
          ls_key_person_indic := 'N0';
         END IF;
      ELSE                                -- If the Mode is Generate New IP
         ls_key_person_indic := 'N0';
      END IF;
    END IF;
    -- 3823: Key Person record needed in IP and Award - End
*/    --**ENH010 end
    --**ENH018 begin
    -- JM 6-26-2013 update to use new procedure to get unit contract admin
    /*
    SELECT     OSP$UNIT.osp_administrator
    INTO         ls_osp_admin
    FROM         OSP$EPS_PROP_UNITS, OSP$UNIT
    WHERE     OSP$EPS_PROP_UNITS.PROPOSAL_NUMBER = as_dev_proposal_num
        AND      OSP$EPS_PROP_UNITS.LEAD_UNIT_FLAG = 'Y'
        AND      OSP$EPS_PROP_UNITS.UNIT_NUMBER = OSP$UNIT.UNIT_NUMBER(+);
        */
        SELECT OWNED_BY_UNIT INTO as_unit_number
        FROM OSP$EPS_PROPOSAL
        WHERE PROPOSAL_NUMBER = as_dev_proposal_num;

        VU_GET_UNIT_CONTRACT_ADMIN(as_unit_number,rt_admin_id,rt_administrator);

        ls_osp_admin := rt_admin_id;
        -- JM END
    --**ENH018 end
    INSERT INTO OSP$PROPOSAL
         (     PROPOSAL_NUMBER ,
                  SPONSOR_PROPOSAL_NUMBER,
                  SEQUENCE_NUMBER,
                  PROPOSAL_TYPE_CODE,
                  CURRENT_ACCOUNT_NUMBER,
                  TITLE,
                SPONSOR_CODE,
                PRIME_SPONSOR_CODE,   --ENH013
                ROLODEX_ID,
                NSF_CODE,                --ENH012
                NOTICE_OF_OPPORTUNITY_CODE,
                GRAD_STUD_HEADCOUNT,
                GRAD_STUD_PERSON_MONTHS,
                TYPE_OF_ACCOUNT,
                ACTIVITY_TYPE_CODE,
                REQUESTED_START_DATE_INITIAL,
                REQUESTED_START_DATE_TOTAL,
                REQUESTED_END_DATE_INITIAL,
                REQUESTED_END_DATE_TOTAL,
                TOTAL_DIRECT_COST_INITIAL,
                TOTAL_DIRECT_COST_TOTAL,
                TOTAL_INDIRECT_COST_INITIAL,
                TOTAL_INDIRECT_COST_TOTAL,
                NUMBER_OF_COPIES,
                DEADLINE_DATE,
                DEADLINE_TYPE,
                MAIL_BY,
                MAIL_TYPE,
                MAIL_ACCOUNT_NUMBER,
                SUBCONTRACT_FLAG,
                COST_SHARING_INDICATOR,
                IDC_RATE_INDICATOR,
                SPECIAL_REVIEW_INDICATOR,
                SCIENCE_CODE_INDICATOR,  --ENH010
                STATUS_CODE,
            --ENH018 begin
                CREATE_TIMESTAMP,
             INITIAL_CONTRACT_ADMIN,
             IP_REVIEW_REQ_TYPE_CODE,
             REVIEW_SUBMISSION_DATE,
             REVIEW_RECEIVE_DATE,
             REVIEW_RESULT_CODE,
             IP_REVIEWER,
             IP_REVIEW_ACTIVITY_INDICATOR ,
            --ENH018 end
                CURRENT_AWARD_NUMBER,         --ENH068
                UPDATE_TIMESTAMP,
                UPDATE_USER,
                CFDA_NUMBER,-- Added for Case # 2097 Enhancement
                OPPORTUNITY, -- Added for Case # 2097 Enhancement
                AWARD_TYPE_CODE, -- Added for Case 2162
                KEY_PERSON_INDICATOR )
    SELECT   ls_inst_prop_number,
    /* JM 6-19-2014 roll agency_routing_identifier into sponsor_proposal_number where populated */
    DECODE(PROPOSAL_TYPE_CODE,1,AGENCY_ROUTING_IDENTIFIER,ls_SponsorProposalNumber),
                --ls_SponsorProposalNumber, --Case#1464 prior to this fix we were setting NULL here
    /* JM END */
                li_Sequence,
                  PROPOSAL_TYPE_CODE,
                  NULL,
                  TITLE,
                SPONSOR_CODE,
                PRIME_SPONSOR_CODE,      -- ENH013
                MAILING_ADDRESS_ID,
                NSF_CODE,                    --ENH012
                NOTICE_OF_OPPORTUNITY_CODE,
                NULL,
                NULL,
                NULL,
                ACTIVITY_TYPE_CODE,
                ld_start_dt_init,
                REQUESTED_START_DATE_INITIAL,
                ld_end_date_init ,
                REQUESTED_END_DATE_INITIAL ,
                li_total_direct_init,
                li_total_direct_tot,
                li_total_indirect_init,
                li_total_indirect_tot,
                NUMBER_OF_COPIES,
                DEADLINE_DATE,
                DEADLINE_TYPE,
                MAIL_BY,
                MAIL_TYPE,
                MAIL_ACCOUNT_NUMBER,
                SUBCONTRACT_FLAG,
--**ENH010 begin
--                'N0',
--                'N0'.
                ls_cost_sharing_indic,
                ls_idc_rate_indic,
--**ENH010 end
                ls_spec_rev_indic,
                ls_science_code_indic,    --ENH010
                1,
--ENH018 begin
                ldt_CreateTimestamp,   --case4142
             ls_osp_admin,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             'N0',
--ENH018 end
                CURRENT_AWARD_NUMBER,    --ENH068
                SYSDATE,
                as_user_id,
                CFDA_NUMBER,-- Added for Case # 2097 Enhancement
                PROGRAM_ANNOUNCEMENT_NUMBER, -- Added for Case # 2097 Enhancement
                AWARD_TYPE_CODE, -- Added for Case # 2162
/* JM 6-2-2011 removed changes for key person per 4.4.2
                ls_key_person_indic    -- 3823: Key Person record needed in IP and Award
*/                'NO'
    FROM        OSP$EPS_PROPOSAL
    WHERE    OSP$EPS_PROPOSAL.PROPOSAL_NUMBER = as_dev_proposal_num;
    IF SQL%ROWCOUNT = 0 THEN
            RAISE insert_prop_error;
     END IF;
---------------------------------
    IF li_spec_rev_count > 0 THEN
        -- since the comments column in special review is a LONG datatype, have to use a cursor
        DECLARE CURSOR c_spec_review IS
        SELECT  SPECIAL_REVIEW_NUMBER ,
                SPECIAL_REVIEW_CODE     ,
                  APPROVAL_TYPE_CODE         ,
                PROTOCOL_NUMBER             ,
                APPLICATION_DATE         ,
                APPROVAL_DATE         ,
                  COMMENTS
        FROM   OSP$EPS_PROP_SPECIAL_REVIEW
        WHERE  OSP$EPS_PROP_SPECIAL_REVIEW.proposal_number = as_dev_proposal_num;
        BEGIN
            OPEN c_spec_review ;
            LOOP
                dbms_output.put_line('looping in spec review fetch');
                FETCH c_spec_review
                INTO  ll_SPECIAL_REVIEW_NUMBER ,
                       ll_SPECIAL_REVIEW_CODE     ,
                         ll_APPROVAL_TYPE_CODE         ,
                     ll_PROTOCOL_NUMBER             ,
                      ld_APP_DATE         ,
                      ld_APPROVAL_DATE         ,
                        ll_COMMENTS        ;
                EXIT WHEN c_spec_review%NOTFOUND;
                dbms_output.put_line('before insert into spec review, spec rev number is ' ||
                    TO_CHAR(ll_special_review_number) || ' code is ' ||
                    TO_CHAR(ll_SPECIAL_REVIEW_CODE) || ' app type code is ' ||
                    TO_CHAR(ll_APPROVAL_TYPE_CODE) || ' prot number is ' ||
                     ll_PROTOCOL_NUMBER || ' inst prop number is ' || ls_inst_prop_number
                    || ' sequence is ' || TO_CHAR(li_sequence));
                /*
                -- 3847: Unique constraint error with resubmission - Start
                -- Get the newly added Human Subject Special Review count
                IF as_generate <> 'G' AND ll_SPECIAL_REVIEW_CODE = ls_parameter_value THEN
                    SELECT COUNT(*)
                    INTO ll_is_proto_in_prev_seq
                    FROM OSP$PROPOSAL_SPECIAL_REVIEW
                    WHERE PROPOSAL_NUMBER = ls_inst_prop_number
                    AND PROTOCOL_NUMBER = ll_PROTOCOL_NUMBER
                    AND SPECIAL_REVIEW_CODE = ls_parameter_value;
                END IF;
                -- Insert the Special Review data only if
                --  The protocol is not there in the Previous sequence of  Institute Proposal
                --  OR The special review type is not HUman Subjects
                --  OR  It is request for generating new Institute proposal
                IF ((as_generate <> 'G' AND ll_is_proto_in_prev_seq = 0)
                   OR (as_generate <> 'G' AND ll_SPECIAL_REVIEW_CODE <> ls_parameter_value)
                   OR (as_generate = 'G')) THEN
               -- 3847: Unique constraint error with resubmission - End
               */
                    INSERT INTO OSP$PROPOSAL_SPECIAL_REVIEW
                      (     PROPOSAL_NUMBER,
                        SEQUENCE_NUMBER,
                         SPECIAL_REVIEW_NUMBER,
                         SPECIAL_REVIEW_CODE,
                         APPROVAL_TYPE_CODE,
                         PROTOCOL_NUMBER ,
                         APPLICATION_DATE,
                         APPROVAL_DATE   ,
                         COMMENTS        ,
                         UPDATE_USER     ,
                         UPDATE_TIMESTAMP )
                    VALUES (
                        ls_inst_prop_number,
                        li_sequence,
                         ll_SPECIAL_REVIEW_NUMBER ,
                          ll_SPECIAL_REVIEW_CODE     ,
                         ll_APPROVAL_TYPE_CODE         ,
                           ll_PROTOCOL_NUMBER             ,
                           ld_APP_DATE         ,
                           ld_APPROVAL_DATE             ,
                         ll_COMMENTS                ,
                        as_user_id,
                        SYSDATE);
            --    END IF;
            END LOOP;
            CLOSE c_spec_review ;
        END;
    END IF;
/* JM 6-16-2014 determine if dev prop requires export control special review */
is_export_control := FN_VU_IS_EXPORT_CONTROL(as_dev_proposal_num);
if is_export_control = 1 then
    SELECT S.SPECIAL_REVIEW_CODE INTO export_control_spcode FROM OSP$SPECIAL_REVIEW S WHERE S.DESCRIPTION = 'Export Control';
    SELECT NVL(MAX(SPECIAL_REVIEW_NUMBER),0) INTO ll_SPECIAL_REVIEW_NUMBER FROM OSP$EPS_PROP_SPECIAL_REVIEW
        WHERE PROPOSAL_NUMBER = as_dev_proposal_num;
    INSERT INTO OSP$PROPOSAL_SPECIAL_REVIEW (
        PROPOSAL_NUMBER,
        SEQUENCE_NUMBER,
        SPECIAL_REVIEW_NUMBER,
        SPECIAL_REVIEW_CODE,
        APPROVAL_TYPE_CODE,
        PROTOCOL_NUMBER ,
        APPLICATION_DATE,
        APPROVAL_DATE,
        COMMENTS,
        UPDATE_USER,
        UPDATE_TIMESTAMP )
    VALUES (
        ls_inst_prop_number,
        li_sequence,
        ll_SPECIAL_REVIEW_NUMBER + 1,
        export_control_spcode ,
        1, --approval type code
        null,  -- protocol number
        sysdate,  -- application_date,
        null, --approval date
        null, --comment
        'COEUS',
         SYSDATE);
end if;
/* JM END */

/* pp 1-2015  insert special review for genome NIH research */
is_genome_nih := FN_VU_IS_genome_nih(as_dev_proposal_num);
if is_genome_nih = 1 then
    SELECT S.SPECIAL_REVIEW_CODE INTO genome_spcode FROM OSP$SPECIAL_REVIEW S WHERE S.DESCRIPTION = 'NIH Genomic Data Sharing Policy';
    SELECT NVL(MAX(SPECIAL_REVIEW_NUMBER),0) INTO ll_SPECIAL_REVIEW_NUMBER FROM OSP$EPS_PROP_SPECIAL_REVIEW
        WHERE PROPOSAL_NUMBER = as_dev_proposal_num;
    INSERT INTO OSP$PROPOSAL_SPECIAL_REVIEW (
        PROPOSAL_NUMBER,
        SEQUENCE_NUMBER,
        SPECIAL_REVIEW_NUMBER,
        SPECIAL_REVIEW_CODE,
        APPROVAL_TYPE_CODE,
        PROTOCOL_NUMBER ,
        APPLICATION_DATE,
        APPROVAL_DATE,
        COMMENTS,
        UPDATE_USER,
        UPDATE_TIMESTAMP )
    VALUES (
        ls_inst_prop_number,
        li_sequence,
        ll_SPECIAL_REVIEW_NUMBER + 1,
        GENOME_SPCODE,
        1, --approval type code
        null,  -- protocol number
        sysdate,  -- application_date,
        null, --approval date
        null, --comment
        'COEUS',
         SYSDATE);
end if;
/* pp END */
--**ENH010 begin
IF ls_budget_status = 'C' THEN
    --------------------------------------------------------
    -- now insert into osp$proposal_cost_sharing table
    --------------------------------------------------------
   INSERT INTO OSP$PROPOSAL_COST_SHARING
         ( PROPOSAL_NUMBER,
           SEQUENCE_NUMBER,
           FISCAL_YEAR,
           COST_SHARING_PERCENTAGE,
           COST_SHARING_TYPE_CODE,
           SOURCE_ACCOUNT,
           AMOUNT,
           UPDATE_TIMESTAMP,
           UPDATE_USER )
  SELECT   ls_inst_prop_number,
           li_sequence,
           OSP$EPS_PROP_COST_SHARING.FISCAL_YEAR,
           OSP$EPS_PROP_COST_SHARING.COST_SHARING_PERCENTAGE,
           1,
           OSP$EPS_PROP_COST_SHARING.SOURCE_ACCOUNT,
           OSP$EPS_PROP_COST_SHARING.AMOUNT,
           SYSDATE,
           as_user_id
    FROM   OSP$EPS_PROP_COST_SHARING
     WHERE  OSP$EPS_PROP_COST_SHARING.PROPOSAL_NUMBER = as_dev_proposal_num
            AND    OSP$EPS_PROP_COST_SHARING.VERSION_NUMBER = li_version;
    --------------------------------------------------------
    -- now insert into osp$proposal_idc_rate table
    --------------------------------------------------------
        IF li_oh_rate_class_code = 1 THEN
            li_idc_type_code := 1;
        ELSE
            IF li_oh_rate_class_code = 2 THEN
                li_idc_type_code := 2;
            ELSE
                li_idc_type_code := 3;
            END IF;
        END IF;
     INSERT INTO OSP$PROPOSAL_IDC_RATE
         ( PROPOSAL_NUMBER,
           SEQUENCE_NUMBER,
           APPLICABLE_IDC_RATE,
           IDC_RATE_TYPE_CODE,
           FISCAL_YEAR,
           ON_CAMPUS_FLAG,
           UNDERRECOVERY_OF_IDC,
           SOURCE_ACCOUNT,
           UPDATE_TIMESTAMP,
           UPDATE_USER )
      SELECT ls_inst_prop_number,
               li_sequence,
             OSP$EPS_PROP_IDC_RATE.APPLICABLE_IDC_RATE,
                li_idc_type_code,
             OSP$EPS_PROP_IDC_RATE.FISCAL_YEAR,
             OSP$EPS_PROP_IDC_RATE.ON_CAMPUS_FLAG,
                 OSP$EPS_PROP_IDC_RATE.UNDERRECOVERY_OF_IDC,
             OSP$EPS_PROP_IDC_RATE.SOURCE_ACCOUNT,
             SYSDATE,
           as_user_id
    FROM OSP$EPS_PROP_IDC_RATE
     WHERE  OSP$EPS_PROP_IDC_RATE.PROPOSAL_NUMBER = as_dev_proposal_num
        AND    OSP$EPS_PROP_IDC_RATE.VERSION_NUMBER = li_version;
END IF; -- budget tatus is 'C'
--------------------------------------------------------
    -- now insert into osp$proposal_science_code table
    --------------------------------------------------------
       INSERT INTO OSP$PROPOSAL_SCIENCE_CODE
         ( PROPOSAL_NUMBER,
           SEQUENCE_NUMBER,
           SCIENCE_CODE,
           UPDATE_TIMESTAMP,
           UPDATE_USER )
   SELECT     ls_inst_prop_number,
               li_sequence,
             OSP$EPS_PROP_SCIENCE_CODE.SCIENCE_CODE,
             SYSDATE,
           as_user_id
    FROM OSP$EPS_PROP_SCIENCE_CODE
     WHERE OSP$EPS_PROP_SCIENCE_CODE.PROPOSAL_NUMBER = as_dev_proposal_num;
--**ENH010 end
dbms_output.put_line('before insert into invest');
    --------------------------------------------------------
    -- now insert into proposal investigators table
/* JM 6-2-2011 Modified per 4.4.2 to always include PI and then only VU Faculty */
    --------------------------------------------------------
   INSERT INTO OSP$PROPOSAL_INVESTIGATORS
          ( PROPOSAL_NUMBER,
            SEQUENCE_NUMBER,
            PERSON_ID,
            PERSON_NAME,
            PRINCIPAL_INVESTIGATOR_FLAG,
            NON_MIT_PERSON_FLAG,
            FACULTY_FLAG,
            PERCENTAGE_EFFORT,
        UPDATE_TIMESTAMP,
        UPDATE_USER,
        MULTI_PI_FLAG,
        ACADEMIC_YEAR_EFFORT,
        SUMMER_YEAR_EFFORT,
        CALENDAR_YEAR_EFFORT)
      SELECT ls_inst_prop_number,
             li_sequence,
             OSP$EPS_PROP_INVESTIGATORS.PERSON_ID,
             OSP$EPS_PROP_INVESTIGATORS.PERSON_NAME,
             OSP$EPS_PROP_INVESTIGATORS.PRINCIPAL_INVESTIGATOR_FLAG,
             OSP$EPS_PROP_INVESTIGATORS.NON_MIT_PERSON_FLAG,
             OSP$EPS_PROP_INVESTIGATORS.FACULTY_FLAG,
             OSP$EPS_PROP_INVESTIGATORS.PERCENTAGE_EFFORT,
         SYSDATE,
         as_user_id,
         OSP$EPS_PROP_INVESTIGATORS.MULTI_PI_FLAG,
         OSP$EPS_PROP_INVESTIGATORS.ACADEMIC_YEAR_EFFORT,
         OSP$EPS_PROP_INVESTIGATORS.SUMMER_YEAR_EFFORT,
         OSP$EPS_PROP_INVESTIGATORS.CALENDAR_YEAR_EFFORT
        FROM OSP$EPS_PROP_INVESTIGATORS
           WHERE  OSP$EPS_PROP_INVESTIGATORS.PROPOSAL_NUMBER = as_dev_proposal_num
            /* JM 4-27-2016 only include native persons */
            AND (FN_VU_IS_EXTERNAL_PERSON(OSP$EPS_PROP_INVESTIGATORS.PERSON_ID) = 'N'
                OR OSP$EPS_PROP_INVESTIGATORS.PRINCIPAL_INVESTIGATOR_FLAG = 'Y')
            /* JM END */
            AND (OSP$EPS_PROP_INVESTIGATORS.PRINCIPAL_INVESTIGATOR_FLAG = 'Y'
                    OR (OSP$EPS_PROP_INVESTIGATORS.FACULTY_FLAG = 'Y'
                        AND OSP$EPS_PROP_INVESTIGATORS.NON_MIT_PERSON_FLAG = 'N'));

                      IF SQL%ROWCOUNT = 0 THEN
                        RAISE insert_invest_error;
                      END IF;
/* JM 6-2-2011 removed changes for key person per 4.4.2
    -- 3823: Key Person Records Needed in Inst Proposal and Award - Start
    --------------------------------------------------------
    -- Insert data into proposal key persons table
    --------------------------------------------------------
   IF li_key_person_count > 0 THEN
       DECLARE CURSOR C_PROP_KEY_PERSON IS
       SELECT PERSON_ID,
                 PERSON_NAME,
              PROJECT_ROLE,
              FACULTY_FLAG,
              NON_MIT_PERSON_FLAG,
              PERCENTAGE_EFFORT
        FROM  OSP$EPS_PROP_KEY_PERSONS
        WHERE PROPOSAL_NUMBER = as_dev_proposal_num;
        BEGIN
             OPEN C_PROP_KEY_PERSON;
             LOOP
             FETCH C_PROP_KEY_PERSON
             INTO ll_person_id,
                   ls_person_name,
                   ls_project_role,
                  ls_faculty_flag,
                  ls_non_mit_person_flag,
                  ll_percentage_effort;
             EXIT WHEN C_PROP_KEY_PERSON%NOTFOUND;
             SELECT COUNT(*)
             INTO is_ll_person_present
             FROM OSP$PROPOSAL_KEY_PERSONS
             WHERE PROPOSAL_NUMBER = ls_inst_prop_number
             AND SEQUENCE_NUMBER = li_sequence
             AND PERSON_ID = ll_person_id;

             IF is_ll_person_present = 0 THEN
                 INSERT INTO OSP$PROPOSAL_KEY_PERSONS
                ( PROPOSAL_NUMBER,
                  SEQUENCE_NUMBER,
                  PERSON_ID,
                  PERSON_NAME,
                  PROJECT_ROLE,
                  FACULTY_FLAG,
                  NON_MIT_PERSON_FLAG,
                  PERCENTAGE_EFFORT,
                  ACADEMIC_YEAR_EFFORT,
                  SUMMER_YEAR_EFFORT,
                  CALENDAR_YEAR_EFFORT,
                  UPDATE_TIMESTAMP,
                  UPDATE_USER)
                VALUES ( ls_inst_prop_number,
                            li_sequence,
                         ll_person_id,
                         ls_person_name,
                         ls_project_role,
                         ls_faculty_flag,
                         ls_non_mit_person_flag,
                         ll_percentage_effort,
                         0,
                         0,
                         0,
                         SYSDATE,
                         as_user_id);
            END IF;
            END LOOP;
            CLOSE C_PROP_KEY_PERSON;
            END;
        END IF;
    -- 3823: Key Person Records Needed in Inst Proposal and Award - End
*/
/* JM 6-2-2011 added per 4.4.2 */
---------------------------------------------------
 -- now insert into proposal Investigators from key persons table
        -- Only insert Vanderbilt persons.
        -- Modified RAY 2-4-2007
        -- Modified PGP 12-2007   check for duplicate people on investigator
        --                        tab and key person tab
---------------------------------------------------
/* JM 7-11-2011 added percent effort fields now available in prop dev */
   INSERT INTO OSP$PROPOSAL_INVESTIGATORS
          ( PROPOSAL_NUMBER,
            SEQUENCE_NUMBER,
            PERSON_ID,
            PERSON_NAME,
            PRINCIPAL_INVESTIGATOR_FLAG,
            NON_MIT_PERSON_FLAG,
            FACULTY_FLAG,
            PERCENTAGE_EFFORT,
            ACADEMIC_YEAR_EFFORT,
            SUMMER_YEAR_EFFORT,
            CALENDAR_YEAR_EFFORT,
            UPDATE_TIMESTAMP,
            UPDATE_USER)
      SELECT ls_inst_prop_number,
             li_sequence,
             EPKP.PERSON_ID,
             EPKP.PERSON_NAME,
             'N',
             EPKP.NON_MIT_PERSON_FLAG,
             EPKP.FACULTY_FLAG,
             EPKP.PERCENTAGE_EFFORT,
             EPKP.ACADEMIC_YEAR_EFFORT,
             EPKP.SUMMER_YEAR_EFFORT,
             EPKP.CALENDAR_YEAR_EFFORT,
             SYSDATE,
             as_user_id
        FROM OSP$EPS_PROP_KEY_PERSONS EPKP
        WHERE EPKP.NON_MIT_PERSON_FLAG = 'N'
        AND EPKP.PROPOSAL_NUMBER = as_dev_proposal_num
        /* JM 4-27-2016 only include native persons */
        AND FN_VU_IS_EXTERNAL_PERSON(EPKP.PERSON_ID) = 'N'
        /* JM END */
        AND NOT EXISTS (SELECT 'x'
                 FROM coeus.osp$eps_prop_investigators A
   WHERE a.proposal_number = epkp.proposal_number
     AND a.person_id = epkp.person_id);
/* JM 7-11-2011 end */

      -- IF SQL%ROWCOUNT = 0 then
    --  RAISE insert_keyperson_error;
      -- end if;
/* END */

/* JM 05-22-2013 subcontracts
    11-26-2013 check for final budget only; pull from organization tab for name
*/
------------------------------------------------------------------
-- now insert into OSP$PROPOSAL_APPROVED_SUB table
------------------------------------------------------------------
INSERT INTO OSP$PROPOSAL_APPROVED_SUB
(PROPOSAL_NUMBER,SEQUENCE_NUMBER,SUBCONTRACTOR_NAME,LOCATION_TYPE_CODE,
ORGANIZATION_ID,AMOUNT,UPDATE_TIMESTAMP,UPDATE_USER)
SELECT ls_inst_prop_number, li_sequence SEQUENCE_NUMBER,
S.LOCATION_NAME SUBCONTRACTOR_NAME, S.LOCATION_TYPE_CODE, S.ORGANIZATION_ID,
NVL(Q.AMOUNT,0) AMOUNT, SYSDATE UPDATE_TIMESTAMP, as_user_id UPDATE_USER
FROM OSP$EPS_PROP_SITES S, (
SELECT B.PROPOSAL_NUMBER, B.VERSION_NUMBER, B.ORGANIZATION_ID,
SUM((D.DIRECT_COST + D.INDIRECT_COST)) AMOUNT
from OSP$BUDGET_SUB_AWARDS b,
OSP$BUDGET_SUB_AWARDS_DETAILS d,
OSP$BUDGET g
where g.proposal_number = b.proposal_number
and g.version_number = b.version_number
and b.proposal_number = d.proposal_number
and b.version_number = d.version_number
and b.sub_award_number = d.sub_award_number
and g.final_version_flag = 'Y'
group by B.PROPOSAL_NUMBER, B.VERSION_NUMBER, B.ORGANIZATION_ID) Q
where S.PROPOSAL_NUMBER = Q.PROPOSAL_NUMBER(+)
and S.ORGANIZATION_ID = Q.ORGANIZATION_ID(+)
-- JM 1-22-2016 added billing agreement
AND S.LOCATION_TYPE_CODE IN (3,5,6)
and S.PROPOSAL_NUMBER = as_dev_proposal_num;
/* JM END */

    ---------------------------------------------------
    -- now insert into proposal units table
    ---------------------------------------------------
/* JM 6-2-2011 added per 4.4.2 to only add to Proposal_units if the lead_unit flag = 'Y' */

   INSERT INTO OSP$PROPOSAL_UNITS
          ( PROPOSAL_NUMBER,
            SEQUENCE_NUMBER,
            UNIT_NUMBER,
            LEAD_UNIT_FLAG,
            PERSON_ID,
             UPDATE_TIMESTAMP,
             UPDATE_USER )
      SELECT ls_inst_prop_number,
             li_sequence,
             OSP$EPS_PROP_UNITS.UNIT_NUMBER,
             OSP$EPS_PROP_UNITS.LEAD_UNIT_FLAG,
             OSP$EPS_PROP_UNITS.PERSON_ID,
              SYSDATE,
                 as_user_id
      FROM OSP$EPS_PROP_UNITS
    WHERE  OSP$EPS_PROP_UNITS.PROPOSAL_NUMBER = as_dev_proposal_num
       and osp$EPS_PROP_UNITS.LEAD_UNIT_FLAG = 'Y';

                  IF SQL%ROWCOUNT = 0 THEN
                    RAISE insert_units_error;
                  END IF;
/* JM 6-2-2011 added per 4.4.2 */
/*--------------------------------------------------------------
       insert Investigators into proposal units (only investigators that are VU faculty)
       Created 10/30/2009 CK
-----------------------------------------------------------------*/
    INSERT INTO OSP$PROPOSAL_UNITS
          ( PROPOSAL_NUMBER,
            SEQUENCE_NUMBER,
            UNIT_NUMBER,
            LEAD_UNIT_FLAG,
            PERSON_ID,
            UPDATE_TIMESTAMP,
            UPDATE_USER )
     SELECT ls_inst_prop_number,
            li_sequence,
            P.HOME_UNIT,
            'N',
            P.PERSON_ID,
            SYSDATE,
            as_user_id
       FROM COEUS.OSP$PERSON P, COEUS.OSP$EPS_PROP_INVESTIGATORS pi
          WHERE  p.person_id = pi.PERSON_ID
             AND pi.FACULTY_FLAG = 'Y'
             AND pi.NON_MIT_PERSON_FLAG = 'N'
             AND pi.PROPOSAL_NUMBER = as_dev_proposal_num
             AND NOT EXISTS (select 'x'                     --already have primary pi's unit
                               from COEUS.OSP$EPS_PROP_UNITS a
                               where a.PROPOSAL_NUMBER = pi.PROPOSAL_NUMBER
                                 and a.PERSON_ID = pi.person_id
                                 and a.LEAD_UNIT_FLAG = 'Y');

---------------------------------------------------
 -- now insert into proposal units from key persons table
        -- Only insert Vanderbilt persons.
        -- get units from osp$person home unit
        -- Modified RAY 2-4-2007
        -- Modified PGP 12-2007
        -- JM 9-26-2012 This section replaces the new key person unit logic
        --  introduced in 4.5
---------------------------------------------------
   INSERT INTO OSP$PROPOSAL_UNITS
          ( PROPOSAL_NUMBER,
            SEQUENCE_NUMBER,
            UNIT_NUMBER,
            LEAD_UNIT_FLAG,
            PERSON_ID,
          UPDATE_TIMESTAMP,
          UPDATE_USER )
      SELECT ls_inst_prop_number,
             li_sequence,
             P.HOME_UNIT,
             'N',
             P.PERSON_ID,
           SYSDATE,
           as_user_id
      FROM OSP$PERSON P,
            OSP$EPS_PROP_KEY_PERSONS EPKP
          WHERE  p.person_id = epkp.person_id
          AND EPKP.NON_MIT_PERSON_FLAG = 'N'
          AND EPKP.PROPOSAL_NUMBER = as_dev_proposal_num
          AND NOT EXISTS (SELECT 'x'
                   FROM coeus.osp$eps_prop_investigators A
           WHERE a.proposal_number = epkp.proposal_number
      AND a.person_id = epkp.person_id);

    --  IF SQL%ROWCOUNT = 0 then
   --  RAISE insert_keyperson_units_error;
     -- end if;

/* END */
--ENH066 Begin
------------------------
-- Copy custom data
-------------------------
    ls_code := Get_Parameter_Value('COEUS_MODULE_PROPOSAL');
    IF ls_code = '' OR ls_code IS NULL THEN
        RAISE parameter_error;
    END IF;
    INSERT INTO OSP$PROPOSAL_CUSTOM_DATA
         ( PROPOSAL_NUMBER,
           SEQUENCE_NUMBER,
              COLUMN_NAME,
              COLUMN_VALUE,
             UPDATE_TIMESTAMP,
             UPDATE_USER )
     SELECT ls_inst_prop_number,
            li_sequence,
            OSP$EPS_PROP_CUSTOM_DATA.COLUMN_NAME,
            OSP$EPS_PROP_CUSTOM_DATA.COLUMN_VALUE,
                SYSDATE,
                as_user_id
     FROM OSP$EPS_PROP_CUSTOM_DATA
      WHERE OSP$EPS_PROP_CUSTOM_DATA.PROPOSAL_NUMBER = as_dev_proposal_num
                AND OSP$EPS_PROP_CUSTOM_DATA.COLUMN_NAME IN (SELECT OSP$CUSTOM_DATA_ELEMENT_USAGE.COLUMN_NAME
                FROM  OSP$CUSTOM_DATA_ELEMENT_USAGE
                WHERE OSP$CUSTOM_DATA_ELEMENT_USAGE.MODULE_CODE = TO_NUMBER(ls_code));
--ENH066 End
/* JM 6-17-2014 roll forward clinical trial questionnaire answers to IP */
INSERT INTO OSP$PROPOSAL_CUSTOM_DATA ( PROPOSAL_NUMBER,SEQUENCE_NUMBER,COLUMN_NAME,COLUMN_VALUE,UPDATE_TIMESTAMP,UPDATE_USER )
    SELECT ls_inst_prop_number, li_sequence, 'CLINICAL_TRIAL_PHASE_1', NVL(FN_VU_GET_QNR_QUEST_ANS(3,as_dev_proposal_num,5,1010),'N'), SYSDATE, as_user_id
    FROM DUAL;
    
INSERT INTO OSP$PROPOSAL_CUSTOM_DATA ( PROPOSAL_NUMBER,SEQUENCE_NUMBER,COLUMN_NAME,COLUMN_VALUE,UPDATE_TIMESTAMP,UPDATE_USER )
    SELECT ls_inst_prop_number, li_sequence, 'CLINICAL_TRIAL_PHASE_2', NVL(FN_VU_GET_QNR_QUEST_ANS(3,as_dev_proposal_num,5,1011),'N'), SYSDATE, as_user_id
    FROM DUAL;
    
INSERT INTO OSP$PROPOSAL_CUSTOM_DATA ( PROPOSAL_NUMBER,SEQUENCE_NUMBER,COLUMN_NAME,COLUMN_VALUE,UPDATE_TIMESTAMP,UPDATE_USER )
    SELECT ls_inst_prop_number, li_sequence, 'CLINICAL_TRIAL_PHASE_3', NVL(FN_VU_GET_QNR_QUEST_ANS(3,as_dev_proposal_num,5,3),'N'), SYSDATE, as_user_id
    FROM DUAL;
    
INSERT INTO OSP$PROPOSAL_CUSTOM_DATA ( PROPOSAL_NUMBER,SEQUENCE_NUMBER,COLUMN_NAME,COLUMN_VALUE,UPDATE_TIMESTAMP,UPDATE_USER )
    SELECT ls_inst_prop_number, li_sequence, 'CLINICAL_TRIAL_PHASE_4', NVL(FN_VU_GET_QNR_QUEST_ANS(3,as_dev_proposal_num,5,1012),'N'), SYSDATE, as_user_id
    FROM DUAL;

/* JM END */

/* JM 3-31-2016 get EIN_DUNS from organization for IP */
select organization_id into orgId
from OSP$EPS_PROP_SITES
where proposal_number = as_dev_proposal_num
and location_type_code = 1;

select duns_number, fedral_employer_id into orgDUNS, orgEIN
from OSP$ORGANIZATION
where organization_id = orgId;

INSERT INTO OSP$PROPOSAL_CUSTOM_DATA ( PROPOSAL_NUMBER,SEQUENCE_NUMBER,COLUMN_NAME,COLUMN_VALUE,UPDATE_TIMESTAMP,UPDATE_USER )
    SELECT ls_inst_prop_number, li_sequence, 'EIN_DUNS', orgEIN || ' / ' || orgDUNS, SYSDATE, as_user_id
    FROM DUAL;

/* JM END */

/* pp 1-2015 create custom entry for genome research */
INSERT INTO OSP$PROPOSAL_CUSTOM_DATA ( PROPOSAL_NUMBER,SEQUENCE_NUMBER,COLUMN_NAME,COLUMN_VALUE,UPDATE_TIMESTAMP,UPDATE_USER )
    SELECT ls_inst_prop_number, li_sequence, 'GENOME_RESEARCH', NVL(FN_VU_GET_QNR_QUEST_ANS(3,as_dev_proposal_num,5,1050),'N'), SYSDATE, as_user_id
    FROM DUAL;
/* pp end * / 

/* pp 1-2015 create custom entry for electronic flag    set value to 'Y' if feed from proposal development default will be 'N' for manually created proposals*/
INSERT INTO OSP$PROPOSAL_CUSTOM_DATA ( PROPOSAL_NUMBER,SEQUENCE_NUMBER,COLUMN_NAME,COLUMN_VALUE,UPDATE_TIMESTAMP,UPDATE_USER )
---    SELECT ls_inst_prop_number, li_sequence, 'IP_ELECTRONIC_FLAG', decode(as_submission_type,'S','Y','N'), SYSDATE, as_user_id
     SELECT ls_inst_prop_number, li_sequence, 'IP_ELECTRONIC_FLAG', 'Y', SYSDATE, as_user_id
    FROM DUAL;
/* pp end * / 
--Case 2148 Begin
   UPDATE OSP$PROPOSAL_PER_CREDIT_SPLIT cs
   SET credit = (SELECT credit FROM OSP$EPS_PROP_PER_CREDIT_SPLIT
                 WHERE proposal_number = as_dev_proposal_num AND
                    person_id = cs.person_id AND
                    inv_credit_type_code = cs.inv_credit_type_code)
    WHERE proposal_number = ls_inst_prop_number AND
    sequence_number = li_sequence;
    UPDATE OSP$PROPOSAL_UNIT_CREDIT_SPLIT cs
    SET credit = (SELECT credit FROM OSP$EPS_PROP_UNIT_CREDIT_SPLIT
                 WHERE proposal_number = as_dev_proposal_num AND
                    person_id = cs.person_id AND
                    unit_number = cs.unit_number AND
                    inv_credit_type_code = cs.inv_credit_type_code)
    WHERE proposal_number = ls_inst_prop_number AND
    sequence_number = li_sequence;
--Case 2148 End

--COEUSQA-3616 Transition Prop dev disclosures to inst proposals start
li_coi_return :=fn_coi_prop_transition(as_dev_proposal_num,ls_inst_prop_number);
--COEUSQA-3616 Transition Prop dev disclosures to inst proposals end

/* JM */
end if;
/* END */
RETURN 1;
--------------------
-- MAIN exception handler
---------------------
        EXCEPTION
                WHEN insert_details_error THEN
                    RAISE_APPLICATION_ERROR(-20100, 'Error in inserting rows in ' ||
                    ' OSP$PROPOSAL_ADMIN_DETAILS in fn_feed_inst_prop');
                    RETURN -1;
                WHEN  insert_inv_cert_error THEN
                    RAISE_APPLICATION_ERROR(-20100, 'Error in inserting rows in ' ||
                    ' OSP$PROPOSAL_INV_CERTIFICATION in fn_feed_inst_prop');
                    RETURN -2;
                WHEN update_admin_error THEN
                    RAISE_APPLICATION_ERROR(-20100,'Error in updating rows in ' ||
                    ' OSP$PROPOSAL_ADMIN_DETAILS in fn_feed_inst_prop');
                    RETURN -3;
                WHEN insert_prop_error THEN
                    RAISE_APPLICATION_ERROR(-20100, 'Error in inserting rows in ' ||
                    ' OSP$PROPOSAL in fn_feed_inst_prop');
                    RETURN -4;
                WHEN insert_invest_error THEN
                    RAISE_APPLICATION_ERROR(-20100, 'Error in inserting rows in ' ||
                    ' OSP$PROPOSAL_INVESTIGATORS in fn_feed_inst_prop');
                RETURN -5;
                WHEN insert_units_error THEN
                    RAISE_APPLICATION_ERROR(-20100, 'Error in inserting rows in ' ||
                    ' OSP$PROPOSAL_UNITS in fn_feed_inst_prop');
                RETURN -6;
                WHEN insert_spec_error THEN
                    RAISE_APPLICATION_ERROR(-20100, 'Error in inserting rows in ' ||
                    ' OSP$PROPOSAL_SPECIAL_REVIEW in fn_feed_inst_prop');
                RETURN -7;
            --ENH066 Begin
                WHEN parameter_error THEN
                    RAISE_APPLICATION_ERROR(-20100, 'Missing COEUS_MODULE_DEV_PROPOSAL parameter in ' ||
                    ' OSP$PARAMETER table');
                RETURN -8;
            --ENH066 End
/* JM 6-2-2011 added per 4.4.2 */
                WHEN insert_keyperson_error THEN
                     RAISE_APPLICATION_ERROR(-20100, 'Error in inserting rows in ' ||
                     ' OSP$PROPOSAL_INVESTIGATORS in fn_feed_inst_prop at key person insert');
                RETURN -9;
                WHEN insert_keyperson_units_error THEN
                     RAISE_APPLICATION_ERROR(-20100, 'Error in inserting rows in ' ||
                     ' OSP$PROPOSAL_UNITS in fn_feed_inst_prop at key persons unit insert');
                RETURN -10;
/* END */

END Fn_Feed_Inst_Prop ;
/
