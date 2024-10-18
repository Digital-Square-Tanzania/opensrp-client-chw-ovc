package org.smartregister.chw.ovc.dao;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.OvcLibrary;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.util.Constants;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.dao.AbstractDao;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class OvcDao extends AbstractDao {
    public static void closeOvcMemberFromRegister(String baseEntityID) {
        String sql = "update ec_malaria_confirmation set is_closed = 1 where base_entity_id = '" + baseEntityID + "'";
        updateDB(sql);
    }

    public static boolean isRegisteredForOvc(String baseEntityID) {
        String sql = "SELECT count(p.base_entity_id) count FROM " + Constants.TABLES.OVC_REGISTER + " p " + "WHERE p.base_entity_id = '" + baseEntityID + "' AND p.is_closed = 0";

        DataMap<Integer> dataMap = cursor -> getCursorIntValue(cursor, "count");

        List<Integer> res = readData(sql, dataMap);
        if (res == null || res.size() != 1) return false;

        return res.get(0) > 0;
    }

    public static boolean isReasonsOfVulnerabilityLivingWithHiv(String baseEntityID) {
        String sql = "SELECT reasons_of_vulnerability FROM " + Constants.TABLES.OVC_REGISTER + " p " + "WHERE p.base_entity_id = '" + baseEntityID + "' AND p.is_closed = 0";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "reasons_of_vulnerability");

        List<String> res = readData(sql, dataMap);
        if (res == null || res.size() != 1) return false;

        try {
            return res.get(0).contains("living_with_hiv");
        } catch (Exception e) {
            Timber.e(e);
            return false;
        }
    }

    public static int getClientAge(String baseEntityID) {
        String sql = "SELECT  dob  FROM ec_family_member WHERE base_entity_id = '" + baseEntityID + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "dob");

        List<String> res = readData(sql, dataMap);
        if (res == null || res.size() != 1) return 0;


        int age;
        try {
            age = (new Period(new DateTime(res.get(0)), new DateTime())).getYears();
        } catch (Exception e) {
            Timber.e(e);
            return 0;
        }
        return age;
    }

    public static String getClientSex(String baseEntityID) {
        String sql = "SELECT  gender  FROM ec_family_member WHERE base_entity_id = '" + baseEntityID + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "gender");

        List<String> res = readData(sql, dataMap);
        if (res == null || res.size() != 1) return null;

        return res.get(0);
    }

    public static String getClientDisabilities(String baseEntityID) {
        String sql = "SELECT  disabilities  FROM ec_family_member WHERE base_entity_id = '" + baseEntityID + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "disabilities");

        List<String> res = readData(sql, dataMap);
        if (res == null || res.size() != 1) return "no";

        if (StringUtils.isNotBlank(res.get(0))) {
            return res.get(0).toLowerCase();
        } else {
            return "no";
        }
    }

    public static String getClientBirthCertificate(String baseEntityID) {
        String sql = "SELECT  birth_cert_num  FROM ec_child WHERE base_entity_id = '" + baseEntityID + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "birth_cert_num");

        List<String> res = readData(sql, dataMap);
        if (res == null || res.size() != 1) return null;

        try {
            return res.get(0).toLowerCase();
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }

    public static boolean hasChildrenUnder5(String baseEntityID) {
        String sql = "SELECT COUNT(*) as count FROM ec_family_member \n" +
                "INNER JOIN (\n" +
                "\t\tSELECT ec_family.base_entity_id  FROM ec_family_member \n" +
                "\t\tINNER JOIN ec_family ON ec_family.family_head  = ec_family_member.base_entity_id\n" +
                "\t\tWHERE ec_family_member.entity_type <> 'ec_independent_client'\n" +
                "\t\tAND ec_family_member.base_entity_id = '" + baseEntityID + "'\n" +
                ") as T1 on T1.base_entity_id = ec_family_member.relational_id\n" +
                "WHERE \n" +
                "\t(date('Now') - date(ec_family_member.dob)) < 5";

        DataMap<Integer> dataMap = cursor -> getCursorIntValue(cursor, "count");

        List<Integer> res = readData(sql, dataMap);
        return res.get(0) > 0;
    }

    public static MemberObject getMember(String baseEntityID) {
        String sql = "select m.base_entity_id , m.unique_id , m.relational_id , m.dob , m.first_name , m.middle_name , m.last_name , m.gender , m.phone_number , m.other_phone_number , f.first_name family_name ,f.primary_caregiver , f.family_head , f.village_town ,fh.first_name family_head_first_name , fh.middle_name family_head_middle_name , fh.last_name family_head_last_name, fh.phone_number family_head_phone_number ,  pcg.first_name pcg_first_name , pcg.last_name pcg_last_name , pcg.middle_name pcg_middle_name , pcg.phone_number  pcg_phone_number , mr.* from ec_family_member m inner join ec_family f on m.relational_id = f.base_entity_id inner join " + Constants.TABLES.OVC_REGISTER + " mr on mr.base_entity_id = m.base_entity_id left join ec_family_member fh on fh.base_entity_id = f.family_head left join ec_family_member pcg on pcg.base_entity_id = f.primary_caregiver where m.base_entity_id ='" + baseEntityID + "' ";
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        DataMap<MemberObject> dataMap = cursor -> {
            MemberObject memberObject = new MemberObject();

            memberObject.setFirstName(getCursorValue(cursor, "first_name", ""));
            memberObject.setMiddleName(getCursorValue(cursor, "middle_name", ""));
            memberObject.setLastName(getCursorValue(cursor, "last_name", ""));
            memberObject.setAddress(getCursorValue(cursor, "village_town"));
            memberObject.setGender(getCursorValue(cursor, "gender"));
            memberObject.setUniqueId(getCursorValue(cursor, "unique_id", ""));
            memberObject.setDob(getCursorValue(cursor, "dob"));
            memberObject.setFamilyBaseEntityId(getCursorValue(cursor, "relational_id", ""));
            memberObject.setRelationalId(getCursorValue(cursor, "relational_id", ""));
            memberObject.setPrimaryCareGiver(getCursorValue(cursor, "primary_caregiver"));
            memberObject.setFamilyName(getCursorValue(cursor, "family_name", ""));
            memberObject.setPhoneNumber(getCursorValue(cursor, "phone_number", ""));
            memberObject.setBaseEntityId(getCursorValue(cursor, "base_entity_id", ""));
            memberObject.setFamilyHead(getCursorValue(cursor, "family_head", ""));
            memberObject.setFamilyHeadPhoneNumber(getCursorValue(cursor, "pcg_phone_number", ""));
            memberObject.setFamilyHeadPhoneNumber(getCursorValue(cursor, "family_head_phone_number", ""));


            memberObject.setHivStatus(getCursorValue(cursor, "hiv_status_after_testing", ""));

            if (StringUtils.isBlank(memberObject.getHivStatus())) {
                memberObject.setHivStatus(getCursorValue(cursor, "hiv_results", ""));
            }


            memberObject.setCtcNumber(getCursorValue(cursor, "ctc_number", ""));

            String familyHeadName = getCursorValue(cursor, "family_head_first_name", "") + " " + getCursorValue(cursor, "family_head_middle_name", "");

            familyHeadName = (familyHeadName.trim() + " " + getCursorValue(cursor, "family_head_last_name", "")).trim();
            memberObject.setFamilyHeadName(familyHeadName);

            String familyPcgName = getCursorValue(cursor, "pcg_first_name", "") + " " + getCursorValue(cursor, "pcg_middle_name", "");

            familyPcgName = (familyPcgName.trim() + " " + getCursorValue(cursor, "pcg_last_name", "")).trim();
            memberObject.setPrimaryCareGiverName(familyPcgName);

            return memberObject;
        };

        List<MemberObject> res = readData(sql, dataMap);
        if (res == null || res.size() != 1) return null;

        return res.get(0);
    }

    public static Event getEventByFormSubmissionId(String formSubmissionId) {
        String sql = "select json from event where formSubmissionId = '" + formSubmissionId + "'";
        DataMap<Event> dataMap = (c) -> {
            Event event;
            try {
                event = (Event) OvcLibrary.getInstance().getEcSyncHelper().convert(new JSONObject(getCursorValue(c, "json")), Event.class);
            } catch (JSONException e) {
                Timber.e(e);
                return null;
            }

            return event;
        };
        return (Event) AbstractDao.readSingleValue(sql, dataMap);
    }
}
