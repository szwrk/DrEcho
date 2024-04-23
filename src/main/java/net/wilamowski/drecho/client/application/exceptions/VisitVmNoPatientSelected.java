package net.wilamowski.drecho.client.application.exceptions;
/*Author:
 Arkadiusz Wilamowski
 https://github.com/szwrk
*/
public class VisitVmNoPatientSelected extends GeneralUiException {
    private static final String defaultHeaderKey = "e.015.header";
    private static final String defaultContentMsgKey = "e.015.msg";

    public VisitVmNoPatientSelected() {
        super( defaultHeaderKey , defaultContentMsgKey );
    }
}
