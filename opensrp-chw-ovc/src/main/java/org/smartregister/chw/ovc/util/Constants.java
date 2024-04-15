package org.smartregister.chw.ovc.util;

public interface Constants {
    enum SaveType {SUBMIT_AND_CLOSE, AUTO_SUBMIT}


    int REQUEST_CODE_GET_JSON = 2244;

    String ENCOUNTER_TYPE = "encounter_type";

    String STEP_ONE = "step1";

    String STEP_TWO = "step2";

    interface JSON_FORM_EXTRA {
        String JSON = "json";
        String ENCOUNTER_TYPE = "encounter_type";

        String DELETE_EVENT_ID = "deleted_event_id";

        String DELETE_FORM_SUBMISSION_ID = "deleted_form_submission_id";
    }

    interface EVENT_TYPE {
        String GBV_REGISTRATION = "GBV Registration";

        String GBV_FOLLOW_UP_VISIT = "GBV Follow-up Visit";

        String GBV_HOME_VISIT = "GBV Home Visit";

        String VOID_EVENT = "Void Event";

        String DELETE_EVENT = "Delete Event";
    }

    interface FORMS {
        String GBV_VISIT_TYPE = "gbv_visit_type";

        String OVC_EDUCATION_AND_PSYCHOSOCICAL_SUPPORT_FORM = "mvc_education_and_psychosocial";
    }

    interface TABLES {
        String GBV_REGISTER = "ec_gbv_register";

        String GBV_FOLLOW_UP = "ec_gbv_follow_up_visit";

    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";

        String FAMILY_BASE_ENTITY_ID = "FAMILY_BASE_ENTITY_ID";

        String ACTION = "ACTION";

        String GBV_FORM_NAME = "GBV_FORM_NAME";

        String EDIT_MODE = "editMode";

        String MEMBER_PROFILE_OBJECT = "MemberObject";

    }

    interface ACTIVITY_PAYLOAD_TYPE {
        String REGISTRATION = "REGISTRATION";
        String FOLLOW_UP_VISIT = "FOLLOW_UP_VISIT";
    }

    interface CONFIGURATION {
        String GBV_REGISTRATION_CONFIGURATION = "gbv_registration";
    }

}