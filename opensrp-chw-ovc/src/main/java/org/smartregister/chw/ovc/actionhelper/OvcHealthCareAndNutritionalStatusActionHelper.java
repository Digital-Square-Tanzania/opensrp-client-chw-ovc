package org.smartregister.chw.ovc.actionhelper;

import static org.smartregister.client.utils.constants.JsonFormConstants.JSON_FORM_KEY.GLOBAL;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.domain.VisitDetail;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.JsonFormUtils;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public abstract class OvcHealthCareAndNutritionalStatusActionHelper extends OvcVisitActionHelper {
    private String childReceivedVaccinations;
    private String healthCareServiceProvided;

    private MemberObject memberObject;
    private String jsonString;

    public OvcHealthCareAndNutritionalStatusActionHelper(MemberObject memberObject) {
        this.memberObject = memberObject;
    }

    @Override
    public void onJsonFormLoaded(String jsonString, Context context, Map<String, List<VisitDetail>> details) {
        this.jsonString = jsonString;
        super.onJsonFormLoaded(jsonString, context, details);
    }

    @Override
    public String getPreProcessed() {
        try {
            JSONObject form = new JSONObject(jsonString);
            form.getJSONObject(GLOBAL).put("age", memberObject.getAge());
            return form.toString();
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            childReceivedVaccinations = JsonFormUtils.getValue(payload, "child_received_vaccinations");
            healthCareServiceProvided = JsonFormUtils.getValue(payload, "health_care_service_provided");
            processNutritionAction(healthCareServiceProvided);
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    public abstract void processNutritionAction(String healthCareServiceProvided);

    @Override
    public String evaluateSubTitle() {
        return null;
    }

    @Override
    public BaseOvcVisitAction.Status evaluateStatusOnPayload() {
        if (StringUtils.isNotBlank(childReceivedVaccinations)) {
            return BaseOvcVisitAction.Status.COMPLETED;
        } else
            return BaseOvcVisitAction.Status.PENDING;
    }
}
