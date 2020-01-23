/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.utilities;

import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
import edu.mit.coeuslite.utils.bean.SubHeaderBean;
import java.util.Vector;

/**
 *
 * @author Mr
 */
public class TestXmlParse {
    public static void main(String[] arg){
        ReadProtocolDetails rpd=new ReadProtocolDetails();
        Vector vectorMenu=rpd.readXMLDataForSubHeader("/edu/dartmouth/coeuslite/coi/xml/COISubMenu.xml");
        System.out.println(vectorMenu.size());

        for(int i=0;i<vectorMenu.size();i++){
            SubHeaderBean  subheaderBean=new SubHeaderBean();
            subheaderBean=(SubHeaderBean)vectorMenu.get(i);

            System.out.println(subheaderBean.getSubHeaderId());
            System.out.println(subheaderBean.getSubHeaderName());
            System.out.println(subheaderBean.getSubHeaderLink());
            System.out.println(subheaderBean.getProtocolSearchLink());




        }


    }



}
