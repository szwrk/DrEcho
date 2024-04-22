package net.wilamowski.drecho.client.application;
/*Author:
 Arkadiusz Wilamowski
 https://github.com/szwrk
*/

import net.wilamowski.drecho.client.application.exceptions.GeneralUiException;

public class NoPatientSelected extends GeneralUiException {

    private static final String defaultHeaderKey = "e.015.header";
    private static final String defaultContentMsgKey = "e.015.msg";

    public NoPatientSelected() {
        super( defaultHeaderKey , defaultContentMsgKey );
    }

    public NoPatientSelected(String headerKey , String contentMsgKey) {
        super( headerKey , contentMsgKey );
    }
}
