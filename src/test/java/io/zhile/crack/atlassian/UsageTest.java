package io.zhile.crack.atlassian;

import io.zhile.crack.atlassian.keygen.Encoder;
import io.zhile.crack.atlassian.license.LicenseProperty;
import io.zhile.crack.atlassian.license.products.*;
import org.junit.Test;

public class UsageTest {

    @Test
    public void test(){
        String product = "conf";
        String serverID = "BTQL-KWBK-NRSQ-V352";
        String contactEMail = "test@qq.com";
        String organisation = "huangshun";
        String contactName = contactEMail;
        boolean dataCenter = true;

        LicenseProperty property;
        switch (product) {
            case "conf":
                property = new Confluence(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "jira":
                property = new JIRASoftware(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "questions":
                property = new Questions(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "tc":
                property = new TeamCalendars(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "bamboo":
                property = new Bamboo(contactName, contactEMail, serverID, organisation);
                break;
            case "bitbucket":
                property = new Bitbucket(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "fisheye":
                property = new FishEye(contactName, contactEMail, serverID, organisation);
                break;
            case "crucible":
                property = new Crucible(contactName, contactEMail, serverID, organisation);
                break;
            case "crowd":
                property = new Crowd(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "jc":
                property = new JIRACore(contactName, contactEMail, serverID, organisation);
                break;
            case "portfolio":
                property = new Portfolio(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "jsd":
                property = new JIRAServiceDesk(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "training":
                property = new Training(contactName, contactEMail, serverID, organisation);
                break;
            case "capture":
                property = new Capture(contactName, contactEMail, serverID, organisation);
                break;
            default:

                return;
        }

        try {
            String licenseCode = Encoder.encode(property.toString());

            System.out.println("Your license code(Don't copy this line!!!): \n");
            System.out.println(licenseCode);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
