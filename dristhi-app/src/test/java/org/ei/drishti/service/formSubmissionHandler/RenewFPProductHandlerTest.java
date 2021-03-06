package org.ei.drishti.service.formSubmissionHandler;

import org.robolectric.RobolectricTestRunner;
import org.ei.drishti.domain.form.FormSubmission;
import org.ei.drishti.service.EligibleCoupleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.ei.drishti.util.FormSubmissionBuilder.create;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class RenewFPProductHandlerTest {
    @Mock
    private EligibleCoupleService ecService;

    private RenewFPProductHandler handler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        handler = new RenewFPProductHandler(ecService);
    }

    @Test
    public void shouldDelegateFormSubmissionHandlingToECService() throws Exception {
        FormSubmission submission = create().withFormName("renew_fp_product").withInstanceId("instance id 1").withVersion("122").build();

        handler.handle(submission);

        verify(ecService).renewFPProduct(submission);
    }

}
