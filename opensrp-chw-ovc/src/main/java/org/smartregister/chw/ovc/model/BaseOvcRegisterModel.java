package org.smartregister.chw.ovc.model;

import static com.vijay.jsonwizard.constants.JsonFormConstants.ENCOUNTER_TYPE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.FIELDS;
import static com.vijay.jsonwizard.constants.JsonFormConstants.GLOBAL;
import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.VALUE;
import static org.smartregister.AllConstants.OPTIONS;
import static org.smartregister.chw.ovc.util.Constants.MVC_BIRTH_CERTIFICATE_NUMBER;
import static org.smartregister.chw.ovc.util.Constants.MVC_CHILD_REGISTRATION;
import static org.smartregister.chw.ovc.util.Constants.MVC_HAS_BIRTH_CERTIFICATE;
import static org.smartregister.chw.ovc.util.Constants.MVC_HEAD_OF_HOUSEHOLD_ENROLLMENT;
import static org.smartregister.chw.ovc.util.Constants.MVC_LEVEL_OF_EDUCATION;
import static org.smartregister.chw.ovc.util.Constants.REASONS_OF_VULNERABILITY;
import static org.smartregister.chw.ovc.util.Constants.STEP_ONE;
import static org.smartregister.chw.ovc.util.Constants.TYPE_OF_VULNERABILITY;
import static org.smartregister.chw.ovc.util.Constants.YES;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.contract.OvcRegisterContract;
import org.smartregister.chw.ovc.dao.OvcDao;
import org.smartregister.chw.ovc.util.OvcJsonFormUtils;

import timber.log.Timber;

public class BaseOvcRegisterModel implements OvcRegisterContract.Model {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject jsonObject = OvcJsonFormUtils.getFormAsJson(formName);
        JSONObject globalObject = jsonObject.optJSONObject(GLOBAL);

        if (globalObject != null) {
            globalObject.put("age", OvcDao.getClientAge(entityId));
            globalObject.put("gender", OvcDao.getClientSex(entityId));
        }

        String encounterType = jsonObject.optString(ENCOUNTER_TYPE);
        if (encounterType.equalsIgnoreCase(MVC_HEAD_OF_HOUSEHOLD_ENROLLMENT) || encounterType.equalsIgnoreCase(MVC_CHILD_REGISTRATION)) {
            processRegistrationFormFields(jsonObject, entityId);
        }

        if (encounterType.equalsIgnoreCase(MVC_CHILD_REGISTRATION)) {
            JSONArray fields = jsonObject.getJSONObject(STEP_ONE).getJSONArray(FIELDS);
            int age = OvcDao.getClientAge(entityId);

            for (int i = 0; i < fields.length(); i++) {
                JSONObject field = fields.getJSONObject(i);
                String key = field.getString(KEY);

                try {
                    if (key.equalsIgnoreCase(MVC_LEVEL_OF_EDUCATION)) {
                        if (age < 5) {
                            field.getJSONArray(OPTIONS).remove(5);
                            field.getJSONArray(OPTIONS).remove(4);
                            field.getJSONArray(OPTIONS).remove(3);
                            field.getJSONArray(OPTIONS).remove(2);
                        } else if (age < 10) {
                            field.getJSONArray(OPTIONS).remove(5);
                            field.getJSONArray(OPTIONS).remove(4);
                            field.getJSONArray(OPTIONS).remove(3);
                            field.getJSONArray(OPTIONS).remove(0);
                        } else if (age < 14) {
                            field.getJSONArray(OPTIONS).remove(5);
                            field.getJSONArray(OPTIONS).remove(4);
                            field.getJSONArray(OPTIONS).remove(1);
                            field.getJSONArray(OPTIONS).remove(0);
                        } else {
                            field.getJSONArray(OPTIONS).remove(0);
                            field.getJSONArray(OPTIONS).remove(1);
                        }

                    }
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        }

        OvcJsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);

        return jsonObject;
    }

    private void processRegistrationFormFields(JSONObject jsonObject, String entityId) throws JSONException {
        JSONArray fields = jsonObject.getJSONObject(STEP_ONE).getJSONArray(FIELDS);
        String clientDisabilities = OvcDao.getClientDisabilities(entityId);
        String clientBirthCertificate = OvcDao.getClientBirthCertificate(entityId);

        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            String key = field.getString(KEY);

            if (key.equalsIgnoreCase(TYPE_OF_VULNERABILITY) || key.equalsIgnoreCase(REASONS_OF_VULNERABILITY)) {
                processDisabilityOptions(field, clientDisabilities);
            } else if (key.equalsIgnoreCase(MVC_HAS_BIRTH_CERTIFICATE) && StringUtils.isNotBlank(clientBirthCertificate)) {
                field.put(VALUE, YES);
            } else if (key.equalsIgnoreCase(MVC_BIRTH_CERTIFICATE_NUMBER) && StringUtils.isNotBlank(clientBirthCertificate)) {
                field.put(VALUE, clientBirthCertificate);
            }
        }
    }

    private void processDisabilityOptions(JSONObject field, String clientDisabilities) throws JSONException {
        if (clientDisabilities != null && clientDisabilities.equalsIgnoreCase(YES)) {
            field.getJSONArray(OPTIONS).getJSONObject(0).put(VALUE, true);
        } else {
            field.getJSONArray(OPTIONS).remove(0);
        }
    }


}
