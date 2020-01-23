/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.services;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2CertificationBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mr Lijo Joy
 */
public class CoiCertificationService {

    private CoiCertificationService() {
    }
    private static CoiCertificationService instance = null;

    public static CoiCertificationService getInstance() {
        if (instance == null) {
            instance = new CoiCertificationService();
        }
        return instance;
    }

    public String saveOrUpdate(Coiv2CertificationBean coiv2CertificationBean, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        PersonInfoBean userInfoBean = (PersonInfoBean) session.getAttribute("LOGGED_IN_PERSON");
//        coiv2CertificationBean.setCertitifcationTimeStamp(new Date());
//        coiv2CertificationBean.setCertificationBy(userInfoBean.getUserId());
//        CoiDisclosureBean currDisclosure = (CoiDisclosureBean) session.getAttribute("disclosureBeanSession");
//        coiv2CertificationBean.setDisclosureNumber(currDisclosure.getCoiDisclosureNumber());
//        coiv2CertificationBean.setSequenceNumber(currDisclosure.getSequenceNumber());
        WebTxnBean webTxnBean = new WebTxnBean();
        coiv2CertificationBean.setUpdateUser(userInfoBean.getUserId());
        webTxnBean.getResults(request, "updateCertitificationCoiv2", coiv2CertificationBean);
        return "success";
    }
}
