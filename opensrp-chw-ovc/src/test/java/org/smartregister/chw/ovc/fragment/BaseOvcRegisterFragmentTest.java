package org.smartregister.chw.ovc.fragment;

import static org.mockito.Mockito.times;

import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.ovc.activity.BaseOvcProfileActivity;
import org.smartregister.commonregistry.CommonPersonObjectClient;

public class BaseOvcRegisterFragmentTest {
    @Mock
    public BaseOvcRegisterFragment baseTestRegisterFragment;

    @Mock
    public CommonPersonObjectClient client;

    @Test(expected = Exception.class)
    public void openProfile() throws Exception {
        Whitebox.invokeMethod(baseTestRegisterFragment, "openProfile", client);
        PowerMockito.mockStatic(BaseOvcProfileActivity.class);
        BaseOvcProfileActivity.startProfileActivity(null, null);
        PowerMockito.verifyStatic(BaseOvcProfileActivity.class, times(1));

    }
}
