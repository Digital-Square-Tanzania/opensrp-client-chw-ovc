package org.smartregister.chw.ovc.actionhelper;

import static org.smartregister.client.utils.constants.JsonFormConstants.GLOBAL;
import static org.smartregister.client.utils.constants.JsonFormConstants.READ_ONLY;

import android.content.Context;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ovc.dao.OvcDao;
import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.domain.VisitDetail;
import org.smartregister.chw.ovc.model.BaseOvcVisitAction;
import org.smartregister.chw.ovc.util.JsonFormUtils;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class MvcHivRiskAssessmentActionHelper extends OvcVisitActionHelper {
    private String childEverTestedForHiv;

    private JSONObject jsonForm;

    private MemberObject memberObject;

    public MvcHivRiskAssessmentActionHelper(MemberObject memberObject) {
        this.memberObject = memberObject;
    }

    @Override
    public void onJsonFormLoaded(String jsonString, Context context, Map<String, List<VisitDetail>> details) {
        super.onJsonFormLoaded(jsonString, context, details);
        try {
            jsonForm = new JSONObject(jsonString);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    /**
     * Update the preprocessed form to pass client age as global parameter
     *
     * @return the updated form
     */
    @Override
    public String getPreProcessed() {
        try {
            JSONObject global = jsonForm.getJSONObject(GLOBAL);
            global.put("age", memberObject.getAge());

            boolean isLivingWithHiv = OvcDao.isReasonsOfVulnerabilityLivingWithHiv(memberObject.getBaseEntityId());
            if (isLivingWithHiv) {
                JSONArray fields = jsonForm.getJSONObject(JsonFormConstants.STEP1).getJSONArray(JsonFormConstants.FIELDS);
                JSONObject childEverTestedForHiv = org.smartregister.util.JsonFormUtils.getFieldJSONObject(fields, "child_ever_tested_for_hiv");
                if (childEverTestedForHiv != null) {
                    childEverTestedForHiv.put(JsonFormUtils.VALUE, "yes");
                    childEverTestedForHiv.put(READ_ONLY, true);
                }

                JSONObject hivResults = org.smartregister.util.JsonFormUtils.getFieldJSONObject(fields, "hiv_results");
                if (hivResults != null) {
                    hivResults.put(JsonFormUtils.VALUE, "positive");
                    childEverTestedForHiv.put(READ_ONLY, true);
                }
            }

            return jsonForm.toString();
        } catch (JSONException e) {
            Timber.e(e);
        }
        return null;
    }


    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            childEverTestedForHiv = JsonFormUtils.getValue(payload, "child_ever_tested_for_hiv");
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    @Override
    public String evaluateSubTitle() {
        return null;
    }

    @Override
    public BaseOvcVisitAction.Status evaluateStatusOnPayload() {
        if (StringUtils.isNotBlank(childEverTestedForHiv)) {
            return BaseOvcVisitAction.Status.COMPLETED;
        } else
            return BaseOvcVisitAction.Status.PENDING;
    }
}
