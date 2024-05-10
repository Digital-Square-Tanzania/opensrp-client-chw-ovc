package org.smartregister.chw.ovc.model;

import static com.vijay.jsonwizard.constants.JsonFormConstants.ENCOUNTER_TYPE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.FIELDS;
import static com.vijay.jsonwizard.constants.JsonFormConstants.GLOBAL;
import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.VALUE;
import static org.smartregister.AllConstants.OPTIONS;
import static org.smartregister.chw.ovc.util.Constants.MVC_CHILD_REGISTRATION;
import static org.smartregister.chw.ovc.util.Constants.MVC_HEAD_OF_HOUSEHOLD_ENROLLMENT;
import static org.smartregister.chw.ovc.util.Constants.REASONS_OF_VULNERABILITY;
import static org.smartregister.chw.ovc.util.Constants.STEP_ONE;
import static org.smartregister.chw.ovc.util.Constants.TYPE_OF_VULNERABILITY;
import static org.smartregister.chw.ovc.util.Constants.YES;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.chw.ovc.contract.OvcRegisterContract;
import org.smartregister.chw.ovc.dao.OvcDao;
import org.smartregister.chw.ovc.util.OvcJsonFormUtils;

public class BaseOvcRegisterModel implements OvcRegisterContract.Model {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject jsonObject = OvcJsonFormUtils.getFormAsJson(formName);

        if (jsonObject.has(GLOBAL)) {
            jsonObject.getJSONObject(GLOBAL).put("age", OvcDao.getClientAge(entityId));
            jsonObject.getJSONObject(GLOBAL).put("gender", OvcDao.getClientSex(entityId));
        }

        if (jsonObject.getString(ENCOUNTER_TYPE).equalsIgnoreCase(MVC_HEAD_OF_HOUSEHOLD_ENROLLMENT) || jsonObject.getString(ENCOUNTER_TYPE).equalsIgnoreCase(MVC_CHILD_REGISTRATION)) {
            JSONArray fields = jsonObject.getJSONObject(STEP_ONE).getJSONArray(FIELDS);

            for (int i = 0; i < fields.length(); i++) {
                if (fields.getJSONObject(i).getString(KEY).equalsIgnoreCase(TYPE_OF_VULNERABILITY) || fields.getJSONObject(i).getString(KEY).equalsIgnoreCase(REASONS_OF_VULNERABILITY)) {
                    if (OvcDao.getClientDisabilities(entityId).equalsIgnoreCase(YES)) {
                        fields.getJSONObject(i).getJSONArray(OPTIONS).getJSONObject(0).put(VALUE, true);
                    } else {
                        fields.getJSONObject(i).getJSONArray(OPTIONS).remove(0);
                    }
                }
            }
        }

        OvcJsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);

        return jsonObject;
    }

}
