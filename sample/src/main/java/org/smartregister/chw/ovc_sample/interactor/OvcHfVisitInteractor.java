package org.smartregister.chw.ovc_sample.interactor;

import org.smartregister.chw.ovc.domain.MemberObject;
import org.smartregister.chw.ovc.interactor.BaseOvcHfVisitInteractor;
import org.smartregister.chw.ovc_sample.activity.EntryActivity;

public class OvcHfVisitInteractor extends BaseOvcHfVisitInteractor {
    @Override
    public MemberObject getMemberClient(String memberID) {
        return EntryActivity.getSampleMember();
    }
}
