package org.smartregister.chw.ovc.model;

import org.json.JSONObject;
import org.smartregister.chw.ovc.contract.OvcRegisterContract;
import org.smartregister.chw.ovc.dao.OvcDao;
import org.smartregister.chw.ovc.util.OvcJsonFormUtils;

public class BaseOvcRegisterModel implements OvcRegisterContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject jsonObject = OvcJsonFormUtils.getFormAsJson(formName);


        if (jsonObject.has("global")) {
            jsonObject.getJSONObject("global").put("age", OvcDao.getClientAge(entityId));
            jsonObject.getJSONObject("global").put("gender", OvcDao.getClientSex(entityId));
        }

        OvcJsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);

        return jsonObject;
    }

}
