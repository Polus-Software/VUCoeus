CREATE OR REPLACE procedure COEUS.vu_inst_prop_feed_award (
    AV_MIT_AWARD_NUMBER IN osp$AWARD_FUNDING_PROPOSALS.MIT_AWARD_NUMBER%TYPE := NULL,
    AV_SEQUENCE_NUMBER IN osp$AWARD_FUNDING_PROPOSALS.SEQUENCE_NUMBER%TYPE := NULL,
    AV_PROPOSAL_NUMBER IN osp$AWARD_FUNDING_PROPOSALS.PROPOSAL_NUMBER%TYPE := NULL,
    AV_PROP_SEQUENCE_NUMBER IN osp$AWARD_FUNDING_PROPOSALS.PROP_SEQUENCE_NUMBER%TYPE := NULL,
    AV_UPDATE_TIMESTAMP IN osp$AWARD_FUNDING_PROPOSALS.UPDATE_TIMESTAMP%TYPE := NULL,
    AV_UPDATE_USER IN osp$AWARD_FUNDING_PROPOSALS.UPDATE_USER%TYPE := NULL
)

IS

sqlstr varchar2(4000);

BEGIN

/* JM 7-30-2013
    SUBCONTRACTS:
    rolls forward subcontracts from inst prop to award;
    looks for matches by org ID or name, deletes them and replaces */
    FOR rec IN (select * from coeus.osp$proposal_approved_sub
        where proposal_number = AV_PROPOSAL_NUMBER
        and sequence_number = AV_PROP_SEQUENCE_NUMBER) LOOP

    delete from coeus.osp$award_approved_subcontract
    where mit_award_number = AV_MIT_AWARD_NUMBER
    and sequence_number = AV_SEQUENCE_NUMBER
    and (organization_id = rec.organization_id
    or upper(subcontractor_name) = upper(rec.subcontractor_name))
    and location_type_code = rec.location_type_code;
    commit;

    INSERT INTO COEUS.OSP$AWARD_APPROVED_SUBCONTRACT (
        MIT_AWARD_NUMBER,
        SEQUENCE_NUMBER,
        SUBCONTRACTOR_NAME,
        LOCATION_TYPE_CODE,
        ORGANIZATION_ID,
        AMOUNT,
        UPDATE_TIMESTAMP,
        UPDATE_USER
     )
    VALUES(
        AV_MIT_AWARD_NUMBER,
        AV_SEQUENCE_NUMBER,
        rec.subcontractor_name,
        rec.location_type_code,
        rec.organization_id,
        rec.amount,
        AV_UPDATE_TIMESTAMP,
        AV_UPDATE_USER);
END LOOP;
COMMIT;
/* END SUBCONTRACTS */

end;
/
