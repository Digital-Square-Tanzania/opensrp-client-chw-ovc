package org.smartregister.chw.ovc.util;

public interface Constants {
    enum SaveType {SUBMIT_AND_CLOSE, AUTO_SUBMIT}


    int REQUEST_CODE_GET_JSON = 2244;

    String ENCOUNTER_TYPE = "encounter_type";

    String STEP_ONE = "step1";

    String STEP_TWO = "step2";

    public static final String MVC_HEAD_OF_HOUSEHOLD_ENROLLMENT = "MVC Head of Household Enrollment";

    public static final String MVC_CHILD_REGISTRATION = "MVC Child Registration";

    public static final String TYPE_OF_VULNERABILITY = "select_type_of_vulnerability";

    public static final String REASONS_OF_VULNERABILITY = "reasons_of_vulnerability";

    public static final String MVC_HAS_BIRTH_CERTIFICATE = "mvc_has_birth_certificate";

    public static final String MVC_BIRTH_CERTIFICATE_NUMBER = "birth_certificate_number";

    public static final String MVC_LEVEL_OF_EDUCATION = "level_of_education";

    public static final String YES = "yes";

    interface JSON_FORM_EXTRA {
        String JSON = "json";
        String ENCOUNTER_TYPE = "encounter_type";

        String DELETE_EVENT_ID = "deleted_event_id";

        String DELETE_FORM_SUBMISSION_ID = "deleted_form_submission_id";
    }

    interface EVENT_TYPE {
        String MVC_HEAD_OF_HOUSEHOLD_REGISTRATION = "MVC Head of Household Enrollment";

        String MVC_CHILD_REGISTRATION = "MVC Child Registration";

        String MVC_HOUSEHOLD_SERVICES_VISIT = "MVC Household Services";

        String MVC_CHILD_SERVICES_VISIT = "MVC Child Services";

        String DELETE_EVENT = "Delete Event";
    }

    interface FORMS {
        String MVC_VISIT_TYPE_FORM = "mvc_visit_type";

        String MVC_EDUCATION_AND_PSYCHOSOCICAL_SUPPORT_FORM = "mvc_education_and_psychosocial";

        String MVC_NEED_AND_RISK_ASSESSMENT_FORM = "mvc_need_and_risk_assessment";

        String MVC_CHILD_PROTECTION_FORM = "mvc_child_protection";

        String MVC_REFERRALS_FORM = "mvc_referrals";

        String MVC_HEALTHCARE_AND_NUTRITION_STATUS_FORM = "mvc_healthcare_and_nutrition";

        String OVC_HIV_RISK_ASSESSMENT_FORM = "mvc_hiv_risk_assessment";

        String MVC_HEAD_OF_HOUSEHOLD_ENROLLMENT = "mvc_head_of_household_enrollment";

        String MVC_CHILD_ENROLLMENT = "mvc_child_enrollment";

        String MVC_HOUSEHOLD_SERVICES = "mvc_household_services";
    }

    interface TABLES {
        String OVC_REGISTER = "ec_ovc_register";

        String OVC_FOLLOW_UP = "ec_ovc_follow_up_visit";

    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";

        String FAMILY_BASE_ENTITY_ID = "FAMILY_BASE_ENTITY_ID";

        String ACTION = "ACTION";

        String OVC_FORM_NAME = "OVC_FORM_NAME";

        String EDIT_MODE = "editMode";

        String MEMBER_PROFILE_OBJECT = "MemberObject";

    }

    interface ACTIVITY_PAYLOAD_TYPE {
        String REGISTRATION = "REGISTRATION";
        String FOLLOW_UP_VISIT = "FOLLOW_UP_VISIT";
    }

    interface CONFIGURATION {
        String OVC_REGISTRATION_CONFIGURATION = "ovc_registration";
    }

}