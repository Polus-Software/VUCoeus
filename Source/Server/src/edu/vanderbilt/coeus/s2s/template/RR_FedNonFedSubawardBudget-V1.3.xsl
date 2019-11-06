<?xml version="1.0" encoding="UTF-8"?>
<!-- $Revision:   1.4  $ -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:att="http://apply.grants.gov/system/Attachments-V1.0" 
xmlns:fo="http://www.w3.org/1999/XSL/Format" 
xmlns:footer="http://apply.grants.gov/system/Footer-V1.0" 
xmlns:glob="http://apply.grants.gov/system/Global-V1.0" 
xmlns:RR_FedNonFed_SubawardBudget_1_3="http://apply.grants.gov/forms/RR_FedNonFed_SubawardBudget_1_3-V1.3" 
xmlns:RR_FedNonFedBudget_1_2="http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2" 
xmlns:globLib="http://apply.grants.gov/system/GlobalLibrary-V2.0" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:variable name="fo:layout-master-set">
		<fo:layout-master-set>
			<fo:simple-page-master master-name="AB" page-height="8.5in" page-width="11in" margin-left="0.3in" margin-right="0.3in">
				<fo:region-body margin-top="0.3in" margin-bottom=".4in"/>
				<fo:region-after extent=".3in"/>
			</fo:simple-page-master>
			<fo:simple-page-master master-name="main" page-height="11in" page-width="8.5in" margin-left="0.4in" margin-right="0.4in">
				<fo:region-body margin-top="0.5in" margin-bottom="0.5in"/>
				<fo:region-after extent=".5in"/>
			</fo:simple-page-master>
			<fo:page-sequence-master master-name="primary">
				<fo:single-page-master-reference master-reference="main"/>
			</fo:page-sequence-master>
			<fo:page-sequence-master master-name="summary">
				<fo:single-page-master-reference master-reference="main"/>
			</fo:page-sequence-master>
			<fo:simple-page-master master-name="default-page" page-height="11in" page-width="8.5in" margin-left="0.34in" margin-right="0.34in">
				<fo:region-body margin-top="0.5in" margin-bottom="0.5in" font-family="Helvetica,Times,Courier" font-size="8pt"/>
				<fo:region-after extent=".5in"/>
			</fo:simple-page-master>
		</fo:layout-master-set>
	</xsl:variable>
	<xsl:template match="RR_FedNonFed_SubawardBudget_1_3:RR_FedNonFed_SubawardBudget_1_3">
		<fo:root>
			<xsl:copy-of select="$fo:layout-master-set"/>
			<fo:page-sequence master-reference="default-page" initial-page-number="1" format="1">
				<fo:static-content flow-name="xsl-region-after">
					<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" table-layout="fixed">
						<fo:table-column column-width="proportional-column-width(1)"/>
						<fo:table-column column-width="proportional-column-width(1)"/>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell hyphenate="true" language="en" padding-start="0pt" padding-end="0pt" padding-before="1pt" padding-after="1pt" display-align="before" text-align="left" border-style="solid" border-width="0pt" border-color="white">
									<fo:block>
										<fo:inline font-size="6px" font-weight="bold">Tracking Number: <xsl:value-of select="/*/*/footer:Grants_govTrackingNumber"/>
										</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell hyphenate="true" language="en" line-height="9pt" padding-start="0pt" padding-end="0pt" padding-before="1pt" padding-after="1pt" display-align="before" text-align="right" border-style="solid" border-width="0pt" border-color="white">
									<fo:block>
										<fo:inline font-size="6px" font-weight="bold">OMB Number: 4040-0001</fo:inline>
									</fo:block>
									<fo:block>
										<fo:inline font-size="6px" font-weight="bold">Expiration Date: 10/31/2019</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<fo:table width="100%">
						<fo:table-column column-width="0.2in"/>
						<fo:table-column/>
						<fo:table-column column-width="0.2in"/>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell width="0.2in">
									<fo:block>&#160;</fo:block>
								</fo:table-cell>
								<fo:table-cell width="7.5in">
									<fo:block text-align="center" font-family="Helvetica,Times,Courier" font-size="11pt" font-weight="bold">R&amp;R Subaward Budget (Fed/Non-Fed) Attachment(s) Form</fo:block>
									<fo:block>&#160;</fo:block>
									<fo:block font-size="8pt" hyphenate="true" language="en">
										<fo:inline font-weight="bold" font-size="8pt">Instructions:</fo:inline>
                                    On this form, you will attach the R&amp;R Subaward Budget (Fed/Non-Fed) files for your grant application. Complete the subawardee budget(s) in accordance with the R&amp;R (Fed/Non-Fed) budget instructions. Please remember that any files you attach must be a PDF document.
                           </fo:block>
									<fo:block line-height="4pt">&#160;</fo:block>
									<fo:block>&#160;</fo:block>
									<fo:block>&#160;</fo:block>
									<fo:block font-size="8pt" hyphenate="true" language="en">
										<fo:inline font-weight="bold" font-size="8pt">Important:</fo:inline>  
                               Please attach your subawardee budget file(s) with the file name of the subawardee organization.  Each file name must be unique. 
                           </fo:block>
									<fo:block>&#160;</fo:block>
								</fo:table-cell>
								<fo:table-cell width="0.2in">
									<fo:block>&#160;</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:table width="100%">
						<fo:table-column/>
						<fo:table-body>
							<xsl:if test="RR_FedNonFed_SubawardBudget_1_3:ATT1!=''">
								<xsl:call-template name="attach_block">
									<xsl:with-param name="block_num"/>
									<xsl:with-param name="block_title">1) Please attach Attachment 1</xsl:with-param>
									<xsl:with-param name="filename">
										<xsl:value-of select="RR_FedNonFed_SubawardBudget_1_3:ATT1"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="RR_FedNonFed_SubawardBudget_1_3:ATT2!=''">
								<xsl:call-template name="attach_block">
									<xsl:with-param name="block_num"/>
									<xsl:with-param name="block_title">2) Please attach Attachment 2</xsl:with-param>
									<xsl:with-param name="filename">
										<xsl:value-of select="RR_FedNonFed_SubawardBudget_1_3:ATT2"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="RR_FedNonFed_SubawardBudget_1_3:ATT3!=''">
								<xsl:call-template name="attach_block">
									<xsl:with-param name="block_num"/>
									<xsl:with-param name="block_title">3) Please attach Attachment 3</xsl:with-param>
									<xsl:with-param name="filename">
										<xsl:value-of select="RR_FedNonFed_SubawardBudget_1_3:ATT3"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="RR_FedNonFed_SubawardBudget_1_3:ATT4!=''">
								<xsl:call-template name="attach_block">
									<xsl:with-param name="block_num"/>
									<xsl:with-param name="block_title">4) Please attach Attachment 4</xsl:with-param>
									<xsl:with-param name="filename">
										<xsl:value-of select="RR_FedNonFed_SubawardBudget_1_3:ATT4"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="RR_FedNonFed_SubawardBudget_1_3:ATT5!=''">
								<xsl:call-template name="attach_block">
									<xsl:with-param name="block_num"/>
									<xsl:with-param name="block_title">5) Please attach Attachment 5</xsl:with-param>
									<xsl:with-param name="filename">
										<xsl:value-of select="RR_FedNonFed_SubawardBudget_1_3:ATT5"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="RR_FedNonFed_SubawardBudget_1_3:ATT6!=''">
								<xsl:call-template name="attach_block">
									<xsl:with-param name="block_num"/>
									<xsl:with-param name="block_title">6) Please attach Attachment 6</xsl:with-param>
									<xsl:with-param name="filename">
										<xsl:value-of select="RR_FedNonFed_SubawardBudget_1_3:ATT6"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="RR_FedNonFed_SubawardBudget_1_3:ATT7!=''">
								<xsl:call-template name="attach_block">
									<xsl:with-param name="block_num"/>
									<xsl:with-param name="block_title">7) Please attach Attachment 7</xsl:with-param>
									<xsl:with-param name="filename">
										<xsl:value-of select="RR_FedNonFed_SubawardBudget_1_3:ATT7"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="RR_FedNonFed_SubawardBudget_1_3:ATT8!=''">
								<xsl:call-template name="attach_block">
									<xsl:with-param name="block_num"/>
									<xsl:with-param name="block_title">8) Please attach Attachment 8</xsl:with-param>
									<xsl:with-param name="filename">
										<xsl:value-of select="RR_FedNonFed_SubawardBudget_1_3:ATT8"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="RR_FedNonFed_SubawardBudget_1_3:ATT9!=''">
								<xsl:call-template name="attach_block">
									<xsl:with-param name="block_num"/>
									<xsl:with-param name="block_title">9) Please attach Attachment 9</xsl:with-param>
									<xsl:with-param name="filename">
										<xsl:value-of select="RR_FedNonFed_SubawardBudget_1_3:ATT9"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="RR_FedNonFed_SubawardBudget_1_3:ATT10!=''">
								<xsl:call-template name="attach_block">
									<xsl:with-param name="block_num"/>
									<xsl:with-param name="block_title">10) Please attach Attachment 10</xsl:with-param>
									<xsl:with-param name="filename">
										<xsl:value-of select="RR_FedNonFed_SubawardBudget_1_3:ATT10"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</fo:table-body>
					</fo:table>
				</fo:flow>
			</fo:page-sequence>
			
			<xsl:for-each select="RR_FedNonFed_SubawardBudget_1_3:BudgetAttachments/RR_FedNonFedBudget_1_2:RR_FedNonFedBudget_1_2">
				<xsl:for-each select="RR_FedNonFedBudget_1_2:BudgetYear">
					<fo:page-sequence master-reference="AB" format="1">
						<fo:static-content flow-name="xsl-region-after">
							<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" table-layout="fixed">
								<fo:table-column column-width="proportional-column-width(1)"/>
								<fo:table-column column-width="proportional-column-width(1)"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" 
										padding-start="0pt" 
										padding-end="0pt" 
										padding-before="1pt" 
										padding-after="1pt" 
										display-align="before" 
										text-align="left" 
										border-style="solid" 
										border-width="0pt" 
										border-color="white">
											<fo:block>
												<fo:inline font-size="6px" font-weight="bold">Tracking Number: <xsl:value-of select="/*/*/footer:Grants_govTrackingNumber"/>
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell hyphenate="true" language="en" 
										line-height="9pt" 
										padding-start="0pt" 
										padding-end="0pt" 
										padding-before="1pt" 
										padding-after="1pt" 
										display-align="before" 
										text-align="right" 
										border-style="solid" 
										border-width="0pt" 
										border-color="white">
											<fo:block>
												<fo:inline font-size="6px" font-weight="bold">OMB Number: 4040-0001</fo:inline>
											</fo:block>
											<fo:block>
												<fo:inline font-size="6px" font-weight="bold">Expiration Date: 10/31/2019</fo:inline>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:static-content>
						<fo:flow flow-name="xsl-region-body">
							<fo:block>
				               <xsl:call-template name="SingleYearPart1">
				                  <xsl:with-param name="year"><xsl:value-of select="position()"></xsl:value-of></xsl:with-param>
				               </xsl:call-template>
							</fo:block>
						</fo:flow>
					</fo:page-sequence>
					<fo:page-sequence master-reference="primary" format="1">
						<fo:static-content flow-name="xsl-region-after">
							<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" table-layout="fixed">
								<fo:table-column column-width="proportional-column-width(1)"/>
								<fo:table-column column-width="proportional-column-width(1)"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" 
										padding-start="0pt" 
										padding-end="0pt" 
										padding-before="1pt" 
										padding-after="1pt" 
										display-align="before" 
										text-align="left" 
										border-style="solid" 
										border-width="0pt" 
										border-color="white">
											<fo:block>
												<fo:inline font-size="6px" font-weight="bold">Tracking Number: <xsl:value-of select="/*/*/footer:Grants_govTrackingNumber"/>
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell hyphenate="true" language="en" 
										line-height="9pt" 
										padding-start="0pt" 
										padding-end="0pt" 
										padding-before="1pt" 
										padding-after="1pt" 
										display-align="before" 
										text-align="right" 
										border-style="solid" 
										border-width="0pt" 
										border-color="white">
											<fo:block>
												<fo:inline font-size="6px" font-weight="bold">OMB Number: 4040-0001</fo:inline>
											</fo:block>
											<fo:block>
												<fo:inline font-size="6px" font-weight="bold">Expiration Date: 10/31/2019</fo:inline>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:static-content>
						
						<fo:flow flow-name="xsl-region-body">
							<fo:block></fo:block>
								<xsl:call-template name="SingleYearPart2">
				                  <xsl:with-param name="year"><xsl:value-of select="position()"></xsl:value-of></xsl:with-param>
								</xsl:call-template>
						</fo:flow>
					</fo:page-sequence>
				</xsl:for-each>
				
			<!-- ====================================== new section SUMMARY ===============================-->
			<fo:page-sequence master-reference="summary" format="1">
				<fo:static-content flow-name="xsl-region-after">
					<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" table-layout="fixed">
						<fo:table-column column-width="proportional-column-width(1)"/>
						<fo:table-column column-width="proportional-column-width(1)"/>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell hyphenate="true" language="en" padding-start="0pt" padding-end="0pt" padding-before="1pt" padding-after="1pt" display-align="before" text-align="left" border-style="solid" border-width="0pt" border-color="white">
									<fo:block>
										<fo:inline font-size="6px" font-weight="bold">Tracking Number: <xsl:value-of select="/*/*/footer:Grants_govTrackingNumber"/>
										</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell hyphenate="true" language="en" line-height="9pt" padding-start="0pt" padding-end="0pt" padding-before="1pt" padding-after="1pt" display-align="before" text-align="right" border-style="solid" border-width="0pt" border-color="white">
									<fo:block>
										<fo:inline font-size="6px" font-weight="bold">OMB Number: 4040-0001</fo:inline>
									</fo:block>
									<fo:block>
										<fo:inline font-size="6px" font-weight="bold">Expiration Date: 10/31/2019</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<fo:table width="100%" space-before.optimum="3pt" space-after.optimum="2pt">
						<fo:table-column/>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell text-align="center" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="before">
									<fo:block>
										<fo:inline font-weight="bold" font-size="10pt">RESEARCH &amp; RELATED BUDGET (TOTAL FED + NON-FED) - Cumulative Budget</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<fo:block>
						<fo:leader leader-pattern="space"/>
					</fo:block>
					<fo:block>
						<fo:leader leader-pattern="space"/>
					</fo:block>
					<fo:block font-size="8pt">
						<fo:table width="550pt" space-before.optimum="3pt" space-after.optimum="2pt">
							<fo:table-column column-width="45%"/>
							<fo:table-column column-width="20%"/>
							<fo:table-column column-width="20%"/>
							<fo:table-column column-width="15%"/>
							<fo:table-body>
								<!--=================== ROWS Begin ======================-->
								<xsl:for-each select="RR_FedNonFedBudget_1_2:BudgetSummary">
									<fo:table-row>
										<fo:table-cell>
											<fo:block/>
										</fo:table-cell>
										<fo:table-cell text-align="center">
											<fo:block>
												<fo:inline font-size="8pt" font-weight="bold">Total Federal (&#36;)</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="center">
											<fo:block>
												<fo:inline font-size="8pt" font-weight="bold">Total Non-Federal (&#36;)</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="center">
											<fo:block>
												<fo:inline font-size="8pt" font-weight="bold">*Totals (&#36;)</fo:inline>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="4">
											<fo:block>&#160;</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block font-weight="bold">Section A, Senior/Key Person</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedSeniorKeyPerson/RR_FedNonFedBudget_1_2:FederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedSeniorKeyPerson/RR_FedNonFedBudget_1_2:FederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedSeniorKeyPerson/RR_FedNonFedBudget_1_2:FederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedSeniorKeyPerson/RR_FedNonFedBudget_1_2:NonFederalSummary) or RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedSeniorKeyPerson/RR_FedNonFedBudget_1_2:NonFederalSummary=''">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedSeniorKeyPerson/RR_FedNonFedBudget_1_2:NonFederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedSeniorKeyPerson/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedSeniorKeyPerson/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedSeniorKeyPerson/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block font-weight="bold">Section B, Other Personnel</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherPersonnel/RR_FedNonFedBudget_1_2:FederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherPersonnel/RR_FedNonFedBudget_1_2:FederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number (RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherPersonnel/RR_FedNonFedBudget_1_2:FederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherPersonnel/RR_FedNonFedBudget_1_2:NonFederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherPersonnel/RR_FedNonFedBudget_1_2:NonFederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number (RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherPersonnel/RR_FedNonFedBudget_1_2:NonFederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherPersonnel/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherPersonnel/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherPersonnel/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>Total Number Other Personnel</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block/>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block/>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalNoOtherPersonnel = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalNoOtherPersonnel)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalNoOtherPersonnel, '#,##0')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block font-weight="bold">Total  Salary, Wages and Fringe Benefits (A + B)</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedPersonnel/RR_FedNonFedBudget_1_2:FederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedPersonnel/RR_FedNonFedBudget_1_2:FederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedPersonnel/RR_FedNonFedBudget_1_2:FederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedPersonnel/RR_FedNonFedBudget_1_2:NonFederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedPersonnel/RR_FedNonFedBudget_1_2:NonFederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedPersonnel/RR_FedNonFedBudget_1_2:NonFederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedPersonnel/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedPersonnel/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedPersonnel/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block font-weight="bold">Section C, Equipment</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedEquipment/RR_FedNonFedBudget_1_2:FederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedEquipment/RR_FedNonFedBudget_1_2:FederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedEquipment/RR_FedNonFedBudget_1_2:FederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedEquipment/RR_FedNonFedBudget_1_2:NonFederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedEquipment/RR_FedNonFedBudget_1_2:NonFederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedEquipment/RR_FedNonFedBudget_1_2:NonFederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedEquipment/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedEquipment/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedEquipment/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block font-weight="bold">Section D, Travel</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTravel/RR_FedNonFedBudget_1_2:FederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTravel/RR_FedNonFedBudget_1_2:FederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTravel/RR_FedNonFedBudget_1_2:FederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTravel/RR_FedNonFedBudget_1_2:NonFederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTravel/RR_FedNonFedBudget_1_2:NonFederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTravel/RR_FedNonFedBudget_1_2:NonFederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTravel/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTravel/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTravel/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">1.&#160;&#160;</fo:inline>Domestic</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeDomesticTravelCosts/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeDomesticTravelCosts/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeDomesticTravelCosts/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeDomesticTravelCosts/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeDomesticTravelCosts/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeDomesticTravelCosts/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeDomesticTravelCosts/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeDomesticTravelCosts/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeDomesticTravelCosts/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">2.&#160;&#160;</fo:inline>Foreign</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeForeignTravelCosts/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeForeignTravelCosts/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeForeignTravelCosts/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeForeignTravelCosts/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeForeignTravelCosts/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeForeignTravelCosts/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeForeignTravelCosts/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeForeignTravelCosts/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeForeignTravelCosts/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block font-weight="bold">Section E, Participant/Trainee Support Costs</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTraineeCosts/RR_FedNonFedBudget_1_2:FederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTraineeCosts/RR_FedNonFedBudget_1_2:FederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTraineeCosts/RR_FedNonFedBudget_1_2:FederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTraineeCosts/RR_FedNonFedBudget_1_2:NonFederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTraineeCosts/RR_FedNonFedBudget_1_2:NonFederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTraineeCosts/RR_FedNonFedBudget_1_2:NonFederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTraineeCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTraineeCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedTraineeCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">1.&#160;&#160;</fo:inline>Tuition/Fees/Health Insurance</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTraineeTuitionFeesHealthInsurance/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeTraineeTuitionFeesHealthInsurance/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTraineeTuitionFeesHealthInsurance/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTraineeTuitionFeesHealthInsurance/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeTraineeTuitionFeesHealthInsurance/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTraineeTuitionFeesHealthInsurance/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTraineeTuitionFeesHealthInsurance/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeTraineeTuitionFeesHealthInsurance/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTraineeTuitionFeesHealthInsurance/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">2.&#160;&#160;</fo:inline>Stipends</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTraineeStipends/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeTraineeStipends/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTraineeStipends/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTraineeStipends/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeTraineeStipends/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTraineeStipends/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTraineeStipends/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeTraineeStipends/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTraineeStipends/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">3.&#160;&#160;</fo:inline>Travel</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTraineeTravel/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeTraineeTravel/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTraineeTravel/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTraineeTravel/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeTraineeTravel/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTraineeTravel/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTraineeTravel/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeTraineeTravel/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTraineeTravel/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">4.&#160;&#160;</fo:inline>Subsistence</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTraineeSubsistence/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeTraineeSubsistence/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTraineeSubsistence/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTraineeSubsistence/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeTraineeSubsistence/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTraineeSubsistence/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTraineeSubsistence/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeTraineeSubsistence/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTraineeSubsistence/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">5.&#160;&#160;</fo:inline>Other&#160;</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeOtherTraineeCost/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeOtherTraineeCost/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeOtherTraineeCost/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeOtherTraineeCost/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeOtherTraineeCost/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeOtherTraineeCost/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeOtherTraineeCost/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeOtherTraineeCost/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeOtherTraineeCost/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">6.&#160;&#160;</fo:inline>Number of Participants/Trainees</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block/>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block/>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeNoofTrainees = '' or not(RR_FedNonFedBudget_1_2:CumulativeNoofTrainees)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeNoofTrainees, '###')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block font-weight="bold">Section F, Other Direct Costs</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherDirectCosts/RR_FedNonFedBudget_1_2:FederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherDirectCosts/RR_FedNonFedBudget_1_2:FederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherDirectCosts/RR_FedNonFedBudget_1_2:FederalSummary,'#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherDirectCosts/RR_FedNonFedBudget_1_2:NonFederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherDirectCosts/RR_FedNonFedBudget_1_2:NonFederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherDirectCosts/RR_FedNonFedBudget_1_2:NonFederalSummary,'#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherDirectCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherDirectCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedOtherDirectCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary,'#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">1.&#160;&#160;</fo:inline>Materials and Supplies</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeMaterialAndSupplies/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeMaterialAndSupplies/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeMaterialAndSupplies/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeMaterialAndSupplies/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeMaterialAndSupplies/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeMaterialAndSupplies/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeMaterialAndSupplies/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeMaterialAndSupplies/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeMaterialAndSupplies/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">2.&#160;&#160;</fo:inline>Publication Costs</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativePublicationCosts/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativePublicationCosts/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativePublicationCosts/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativePublicationCosts/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativePublicationCosts/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativePublicationCosts/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativePublicationCosts/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativePublicationCosts/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativePublicationCosts/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">3.&#160;&#160;</fo:inline>Consultant Services</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeConsultantServices/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeConsultantServices/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeConsultantServices/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeConsultantServices/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeConsultantServices/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeConsultantServices/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeConsultantServices/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeConsultantServices/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeConsultantServices/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">4.&#160;&#160;</fo:inline>ADP/Computer Services</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeADPComputerServices/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeADPComputerServices/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeADPComputerServices/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeADPComputerServices/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeADPComputerServices/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeADPComputerServices/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeADPComputerServices/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeADPComputerServices/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeADPComputerServices/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">5.&#160;&#160;</fo:inline>Subawards/Consortium/Contractual Costs</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeSubawardConsortiumContractualCosts/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeSubawardConsortiumContractualCosts/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeSubawardConsortiumContractualCosts/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeSubawardConsortiumContractualCosts/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeSubawardConsortiumContractualCosts/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeSubawardConsortiumContractualCosts/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeSubawardConsortiumContractualCosts/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeSubawardConsortiumContractualCosts/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeSubawardConsortiumContractualCosts/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">6.&#160;&#160;</fo:inline>Equipment or Facility Rental/User Fees</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeEquipmentFacilityRentalFees/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeEquipmentFacilityRentalFees/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeEquipmentFacilityRentalFees/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeEquipmentFacilityRentalFees/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeEquipmentFacilityRentalFees/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeEquipmentFacilityRentalFees/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeEquipmentFacilityRentalFees/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeEquipmentFacilityRentalFees/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeEquipmentFacilityRentalFees/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">7.&#160;&#160;</fo:inline>Alterations and Renovations</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeAlterationsAndRenovations/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeAlterationsAndRenovations/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeAlterationsAndRenovations/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeAlterationsAndRenovations/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeAlterationsAndRenovations/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeAlterationsAndRenovations/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeAlterationsAndRenovations/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeAlterationsAndRenovations/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeAlterationsAndRenovations/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">8.&#160;&#160;</fo:inline>Other 1</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeOther1DirectCost/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeOther1DirectCost/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeOther1DirectCost/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeOther1DirectCost/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeOther1DirectCost/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeOther1DirectCost/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeOther1DirectCost/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeOther1DirectCost/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeOther1DirectCost/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">9.&#160;&#160;</fo:inline>Other 2</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeOther2DirectCost/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeOther2DirectCost/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeOther2DirectCost/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeOther2DirectCost/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeOther2DirectCost/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeOther2DirectCost/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeOther2DirectCost/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeOther2DirectCost/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeOther2DirectCost/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block>
												<fo:inline font-weight="bold">10.&#160;</fo:inline>Other 3</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeOther3DirectCost/RR_FedNonFedBudget_1_2:Federal = '' or not(RR_FedNonFedBudget_1_2:CumulativeOther3DirectCost/RR_FedNonFedBudget_1_2:Federal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeOther3DirectCost/RR_FedNonFedBudget_1_2:Federal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeOther3DirectCost/RR_FedNonFedBudget_1_2:NonFederal = '' or not(RR_FedNonFedBudget_1_2:CumulativeOther3DirectCost/RR_FedNonFedBudget_1_2:NonFederal)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeOther3DirectCost/RR_FedNonFedBudget_1_2:NonFederal, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeOther3DirectCost/RR_FedNonFedBudget_1_2:TotalFedNonFed = '' or not(RR_FedNonFedBudget_1_2:CumulativeOther3DirectCost/RR_FedNonFedBudget_1_2:TotalFedNonFed)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeOther3DirectCost/RR_FedNonFedBudget_1_2:TotalFedNonFed, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block font-weight="bold">Section G, Direct Costs (A thru F)</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectCosts/RR_FedNonFedBudget_1_2:FederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectCosts/RR_FedNonFedBudget_1_2:FederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectCosts/RR_FedNonFedBudget_1_2:FederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectCosts/RR_FedNonFedBudget_1_2:NonFederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectCosts/RR_FedNonFedBudget_1_2:NonFederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectCosts/RR_FedNonFedBudget_1_2:NonFederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block font-weight="bold">Section H, Indirect Costs</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedIndirectCost/RR_FedNonFedBudget_1_2:FederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedIndirectCost/RR_FedNonFedBudget_1_2:FederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedIndirectCost/RR_FedNonFedBudget_1_2:FederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedIndirectCost/RR_FedNonFedBudget_1_2:NonFederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedIndirectCost/RR_FedNonFedBudget_1_2:NonFederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedIndirectCost/RR_FedNonFedBudget_1_2:NonFederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedIndirectCost/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedIndirectCost/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedIndirectCost/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block font-weight="bold">Section I, Total Direct and Indirect Costs (G + H)</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectIndirectCosts/RR_FedNonFedBudget_1_2:FederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectIndirectCosts/RR_FedNonFedBudget_1_2:FederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectIndirectCosts/RR_FedNonFedBudget_1_2:FederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectIndirectCosts/RR_FedNonFedBudget_1_2:NonFederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectIndirectCosts/RR_FedNonFedBudget_1_2:NonFederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectIndirectCosts/RR_FedNonFedBudget_1_2:NonFederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectIndirectCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectIndirectCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalFundsRequestedDirectIndirectCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block font-weight="bold">Section J, Fee</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeFee = '' or not(RR_FedNonFedBudget_1_2:CumulativeFee)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeFee, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block/>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block/>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell hyphenate="true" language="en" padding-before="3pt" padding-after="3pt">
											<fo:block font-weight="bold">Section K, Total Costs and Fee (I + J)</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalCostsFee/RR_FedNonFedBudget_1_2:FederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalCostsFee/RR_FedNonFedBudget_1_2:FederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalCostsFee/RR_FedNonFedBudget_1_2:FederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalCostsFee/RR_FedNonFedBudget_1_2:NonFederalSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalCostsFee/RR_FedNonFedBudget_1_2:NonFederalSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalCostsFee/RR_FedNonFedBudget_1_2:NonFederalSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="right">
											<fo:block>
												<xsl:choose>
													<xsl:when test="RR_FedNonFedBudget_1_2:CumulativeTotalCostsFee/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary = '' or not(RR_FedNonFedBudget_1_2:CumulativeTotalCostsFee/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary)">
														<fo:inline>&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:CumulativeTotalCostsFee/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary, '#,##0.00')" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"/>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!--============ ROWS End ================================-->
								</xsl:for-each>
							</fo:table-body>
						</fo:table>
						
						<fo:footnote>
							<fo:inline> </fo:inline>
							<fo:footnote-body>
								<fo:block font-size="8pt">RESEARCH &amp; RELATED Budget (Total Fed + Non-Fed)</fo:block>
							</fo:footnote-body>
						</fo:footnote>

					</fo:block>
					<!--================== end new section summary ======================= -->
				</fo:flow>
			</fo:page-sequence>
         </xsl:for-each>
		</fo:root>
	</xsl:template>
	
	<xsl:template name="attach_block">
		<xsl:param name="block_num"/>
		<xsl:param name="block_title"/>
		<xsl:param name="filename"/>
		<xsl:element name="fo:table-row">
			<xsl:element name="fo:table-cell">
				<xsl:attribute name="font-size">8pt</xsl:attribute>
				<xsl:element name="fo:table">
					<xsl:attribute name="border-color">black</xsl:attribute>
					<xsl:attribute name="width">100%</xsl:attribute>
					<xsl:element name="fo:table-column"/>
					<xsl:element name="fo:table-body">
						<xsl:element name="fo:table-row">
							<xsl:element name="fo:table-cell">
								<xsl:element name="fo:table">
									<xsl:attribute name="width">100%</xsl:attribute>
									<xsl:element name="fo:table-column">
										<xsl:attribute name="column-width">0.2in</xsl:attribute>
									</xsl:element>
									<xsl:element name="fo:table-column">
										<xsl:attribute name="column-width">2.0in</xsl:attribute>
									</xsl:element>
									<xsl:element name="fo:table-column">
										<xsl:attribute name="column-width">2.0in</xsl:attribute>
									</xsl:element>
									<fo:table-column column-width="2.0in"/>
									<xsl:element name="fo:table-body">
										<xsl:element name="fo:table-row">
											<xsl:element name="fo:table-cell">
												<xsl:attribute name="line-height">15pt</xsl:attribute>
												<xsl:attribute name="hyphenate">true</xsl:attribute>
												<xsl:attribute name="font-weight">bold</xsl:attribute>
												<xsl:element name="fo:block">
													<xsl:value-of select="$block_num"/>
												</xsl:element>
											</xsl:element>
											<xsl:element name="fo:table-cell">
												<xsl:attribute name="line-height">15pt</xsl:attribute>
												<xsl:attribute name="hyphenate">true</xsl:attribute>
												<xsl:attribute name="font-weight">bold</xsl:attribute>
												<xsl:element name="fo:block">
													<xsl:value-of select="$block_title"/>
												</xsl:element>
											</xsl:element>
											<xsl:element name="fo:table-cell">
												<xsl:attribute name="line-height">15pt</xsl:attribute>
												<xsl:attribute name="hyphenate">true</xsl:attribute>
												<xsl:element name="fo:block">
													<xsl:value-of select="$filename"/>
												</xsl:element>
											</xsl:element>
										</xsl:element>
									</xsl:element>
								</xsl:element>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<!--==============   Single Year Template ======================================-->
	<xsl:template name="SingleYearPart1">
		<xsl:param name="year"/>
		<fo:block>
			<!--  Page header -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column column-width="10%"/>
				<fo:table-column column-width="8%"/>
				<fo:table-column column-width="15%"/>
				<fo:table-column column-width="15%"/>
				<fo:table-column column-width="10%"/>
				<fo:table-column column-width="13%"/>
				<fo:table-column column-width="13%"/>
				<fo:table-column column-width="16%"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block />
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="6" text-align="center" 
							padding-bottom="8" display-align="before">
							<fo:block font-weight="bold" font-size="10pt">
								RESEARCH &amp; RELATED BUDGET (TOTAL FED + NON-FED) - BUDGET PERIOD&#160;<xsl:value-of select="$year"/>
							</fo:block>
						</fo:table-cell>
   						<fo:table-cell line-height="9pt" display-align="before" text-align="right">
							<fo:block>
								<fo:inline>OMB Number: 4040-0001</fo:inline>
							</fo:block>
							<fo:block>
								<fo:inline>Expiration Date: 10/31/2019</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
								&#160;
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell number-columns-spanned="2" padding-bottom="4">
							<fo:block font-weight="bold">
								* ORGANIZATIONAL DUNS:&#160;&#160;
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-bottom="4">
							<fo:block>
								<xsl:value-of select="../RR_FedNonFedBudget_1_2:DUNSID"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-bottom="4">
							<fo:block font-weight="bold">
								Enter name of Organization:&#160;&#160;
							</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="3" padding-bottom="4">
							<fo:block>
								<xsl:value-of select="../RR_FedNonFedBudget_1_2:OrganizationName"/>
							</fo:block>
                        </fo:table-cell>
					</fo:table-row>
                    <fo:table-row>
						<fo:table-cell padding-bottom="6" display-align="after">
							<fo:block>
								<fo:inline font-weight="bold">
									* Budget Type:&#160;&#160;&#160;&#160;&#160;&#160;
								</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-bottom="6" display-align="after">
							<fo:block>
								<xsl:for-each select="../RR_FedNonFedBudget_1_2:BudgetType">
									<xsl:choose>
										<xsl:when test=".='Project'">
											<fo:inline font-family="ZapfDingbats" font-size="10pt">&#x25cf;</fo:inline>
										</xsl:when>
										<xsl:otherwise>
											<fo:inline font-family="ZapfDingbats" font-size="10pt">&#x274d;</fo:inline>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:for-each>
								<fo:inline padding-left="2">
									Project
								</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-bottom="6" number-columns-spanned="2" display-align="after">
							<fo:block>
								<xsl:for-each select="../RR_FedNonFedBudget_1_2:BudgetType">
									<xsl:choose>
										<xsl:when test=".='Subaward/Consortium'">
											<fo:inline font-family="ZapfDingbats" font-size="10pt">&#x25cf;</fo:inline>
										</xsl:when>
										<xsl:otherwise>
											<fo:inline font-family="ZapfDingbats" font-size="10pt">&#x274d;</fo:inline>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:for-each>
								<fo:inline padding-left="2">
									Subaward/Consortium
								</fo:inline>
							</fo:block>
							<fo:block />
						</fo:table-cell>
						<fo:table-cell padding-bottom="6" display-align="after">
							<fo:block>
								<fo:inline font-weight="bold">
									Budget Period:&#160;&#160;
								</fo:inline>
								<xsl:value-of select="$year"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="center" padding-bottom="6" display-align="after">
							<fo:block>
								<fo:inline font-weight="bold">
									* Start Date:&#160;
								</fo:inline>
								<xsl:call-template name="formatDate">
									<xsl:with-param name="value" select="RR_FedNonFedBudget_1_2:BudgetPeriodStartDate"/>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="center" padding-bottom="6" display-align="after">
							<fo:block>
								<fo:inline font-weight="bold">
								* End Date:&#160;
								</fo:inline>
								<xsl:call-template name="formatDate">
									<xsl:with-param name="value" select="RR_FedNonFedBudget_1_2:BudgetPeriodEndDate"/>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block />
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
								&#160;
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<!-- Section A.  Senior/Key Person -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell border-bottom="1px solid black" text-align="left" padding="2" display-align="before">
							<fo:block>
								<fo:inline font-weight="bold">A. Senior/Key Person</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
				
			<xsl:for-each select="RR_FedNonFedBudget_1_2:KeyPersons">
				<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt" margin-top="6">
					<fo:table-column column-width="4%"/>
					<fo:table-column column-width="6%"/>
					<fo:table-column column-width="5%"/>
					<fo:table-column column-width="8%"/>
					<fo:table-column column-width="5%"/>
					<fo:table-column column-width="12%"/>
					<fo:table-column column-width="6%"/>
					<fo:table-column column-width="5%"/>
					<fo:table-column column-width="5%"/>
					<fo:table-column column-width="5%"/>
					<fo:table-column column-width="7%"/>
					<fo:table-column column-width="7%"/>
					<fo:table-column column-width="9%"/>
					<fo:table-column column-width="8%"/>
					<fo:table-column column-width="8%"/>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell padding="2" display-align="before" font-weight="bold">
								<fo:block>
									Prefix
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2" display-align="before" font-weight="bold">
								<fo:block>
									* First
								</fo:block>
								<fo:block>
									Name
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2" display-align="before" font-weight="bold">
								<fo:block>
									Middle
								</fo:block>
								<fo:block>
									Name
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2" display-align="before" font-weight="bold">
								<fo:block>
									* Last
								</fo:block>
								<fo:block>
									Name
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2" display-align="before" font-weight="bold">
								<fo:block>
									Suffix
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2" display-align="before" font-weight="bold">
								<fo:block>
									* Project Role
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2" display-align="before" font-weight="bold" text-align="center">
								<fo:block>
									Base
								</fo:block>
								<fo:block>
									Salary (&#36;)
								</fo:block>
							</fo:table-cell>
							<fo:table-cell text-align="center" padding="2" display-align="before" font-weight="bold">
								<fo:block>
									Cal.
								</fo:block>
								<fo:block>
									Months
								</fo:block>
							</fo:table-cell>
							<fo:table-cell text-align="center" padding="2" display-align="before" font-weight="bold">
								<fo:block>
									Acad.
								</fo:block>
								<fo:block>
									Months
								</fo:block>
							</fo:table-cell>
							<fo:table-cell text-align="center" padding="2" display-align="before" font-weight="bold">
								<fo:block>
									Sum.
								</fo:block>
								<fo:block>
									Months
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2" display-align="before" font-weight="bold" text-align="center">
								<fo:block>
									* Req.
								</fo:block>
								<fo:block>
									Salary (&#36;)
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2" display-align="before" font-weight="bold" text-align="center">
								<fo:block>
									* Fringe
								</fo:block>
								<fo:block>
									Ben. (&#36;)
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2" display-align="before" font-weight="bold" text-align="center">
								<fo:block>
									* Total
								</fo:block>
								<fo:block>
									(Sal &amp; FB)
								</fo:block>
								<fo:block>
									(Fed + 
								</fo:block>
								<fo:block>
									Non-Fed) (&#36;)
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2" display-align="before" font-weight="bold" text-align="center">
								<fo:block>
									* Federal (&#36;)
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2" display-align="before" font-weight="bold" text-align="center">
								<fo:block>
									* Non-
								</fo:block>
								<fo:block>
									Federal (&#36;)
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<!-- Key person rows -->
						<xsl:for-each select="RR_FedNonFedBudget_1_2:KeyPerson">
						<fo:table-row>
							<fo:table-cell line-height="9pt" text-align="left" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Name">
										<xsl:for-each select="globLib:PrefixName">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="left" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Name">
										<xsl:for-each select="globLib:FirstName">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="left" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Name">
										<xsl:for-each select="globLib:MiddleName">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="left" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Name">
										<xsl:for-each select="globLib:LastName">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="left" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Name">
										<xsl:for-each select="globLib:SuffixName">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="left" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:ProjectRole">
										<fo:inline font-size="8pt">
											<xsl:apply-templates/>
										</fo:inline>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:BaseSalary">
											<fo:inline font-size="8pt">
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:CalendarMonths">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:AcademicMonths">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:SummerMonths">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:RequestedSalary">
											<fo:inline font-size="8pt">
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:FringeBenefits">
											<fo:inline font-size="8pt">
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Total/RR_FedNonFedBudget_1_2:TotalFedNonFed">
											<fo:inline font-size="8pt">
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Total/RR_FedNonFedBudget_1_2:Federal">
											<fo:inline font-size="8pt">
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Total/RR_FedNonFedBudget_1_2:NonFederal">
											<fo:inline font-size="8pt">
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						</xsl:for-each>
				
						<!-- Section A totals -->	
						<fo:table-row>
							<fo:table-cell>
								<fo:block>
									&#160;
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell number-columns-spanned="4" text-align="left" padding="2" display-align="before">
								<fo:block>
									<fo:inline font-weight="bold">Additional Senior Key Persons:</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell number-columns-spanned="5" text-align="left" padding="2" display-align="before">
								<fo:block>
									File Name:&#160;&#160;
									<xsl:for-each select="RR_FedNonFedBudget_1_2:AttachedKeyPersons">
										<xsl:for-each select="att:FileName">
											<xsl:apply-templates/>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell number-columns-spanned="3" text-align="right" padding="2" display-align="before">
								<fo:block>
									Mime Type:&#160;&#160;
									<xsl:for-each select="RR_FedNonFedBudget_1_2:AttachedKeyPersons">
										<xsl:for-each select="att:MimeType">
											<xsl:apply-templates/>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell number-columns-spanned="12" text-align="right" padding="2" display-align="before">
								<fo:block>
									<fo:inline font-size="8pt" font-weight="bold">Total Funds requested for all Senior Key Persons in the attached file</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalFundForAttachedKeyPersons/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalFundForAttachedKeyPersons/RR_FedNonFedBudget_1_2:FederalSummary">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalFundForAttachedKeyPersons/RR_FedNonFedBudget_1_2:NonFederalSummary">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>
								<fo:block>
									&#160;
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell number-columns-spanned="12" text-align="right" padding="2" display-align="before">
								<fo:block>
									<fo:inline font-weight="bold">Total Senior/Key Person</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalFundForKeyPersons/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary">
										<fo:inline font-weight="bold">
											<xsl:value-of select="format-number(., '#,##0.00')"/>
										</fo:inline>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalFundForKeyPersons/RR_FedNonFedBudget_1_2:FederalSummary">
										<fo:inline font-weight="bold">
											<xsl:value-of select="format-number(., '#,##0.00')"/>
										</fo:inline>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalFundForKeyPersons/RR_FedNonFedBudget_1_2:NonFederalSummary">
										<fo:inline font-weight="bold">
											<xsl:value-of select="format-number(., '#,##0.00')"/>
										</fo:inline>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>
								<fo:block>
									&#160;
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>
								<fo:block>
									&#160;
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					 </fo:table-body>
				</fo:table>
			</xsl:for-each>
		</fo:block>
		
		<fo:block>
			<!-- Section B.  Other Personnel -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell border-bottom="1px solid black" text-align="left" padding="2" display-align="before">
							<fo:block>
								<fo:inline font-weight="bold">B. Other Personnel</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
				
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt" margin-top="6">
				<fo:table-column column-width="10%"/>
				<fo:table-column column-width="21%"/>
				<fo:table-column column-width="8%"/>
				<fo:table-column column-width="8%"/>
				<fo:table-column column-width="8%"/>
				<fo:table-column column-width="9%"/>
				<fo:table-column column-width="9%"/>
				<fo:table-column column-width="9%"/>
				<fo:table-column column-width="9%"/>
				<fo:table-column column-width="9%"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell text-align="center" padding="2" display-align="before" font-weight="bold">
							<fo:block>
								* Number of
							</fo:block>
							<fo:block>
								Personnel
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="2" display-align="before" font-weight="bold">
							<fo:block>
								* Project Role
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="center" padding="2" display-align="before" font-weight="bold">
							<fo:block>
								Cal.
							</fo:block>
							<fo:block>
								Months
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="center" padding="2" display-align="before" font-weight="bold">
							<fo:block>
								Acad.
							</fo:block>
							<fo:block>
								Months
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="center" padding="2" display-align="before" font-weight="bold">
							<fo:block>
								Sum.
							</fo:block>
							<fo:block>
								Months
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="2" display-align="before" font-weight="bold" text-align="center">
							<fo:block>
								* Req.
							</fo:block>
							<fo:block>
								Salary (&#36;)
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="2" display-align="before" font-weight="bold" text-align="center">
							<fo:block>
								* Fringe
							</fo:block>
							<fo:block>
								Ben. (&#36;)
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="2" display-align="before" font-weight="bold" text-align="center">
							<fo:block>
								* Total
							</fo:block>
							<fo:block>
								(Sal &amp; FB)
							</fo:block>
							<fo:block>
								(Fed + 
							</fo:block>
							<fo:block>
								Non-Fed) (&#36;)
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="2" display-align="before" font-weight="bold" text-align="center">
							<fo:block>
								* Federal (&#36;)
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="2" display-align="before" font-weight="bold" text-align="center">
							<fo:block>
								* Non-
							</fo:block>
							<fo:block>
								Federal (&#36;)
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<!-- Other personnel rows -->
					<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherPersonnel">
						<!-- Post Doctoral Associates -->
						<fo:table-row>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:PostDocAssociates">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:NumberOfPersonnel">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="left" padding="2" display-align="before">
								<fo:block>
									Post Doctoral Associates
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:PostDocAssociates">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:CalendarMonths">
												<fo:inline font-size="8pt">
													<xsl:apply-templates/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:PostDocAssociates">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:AcademicMonths">
												<fo:inline font-size="8pt">
													<xsl:apply-templates/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:PostDocAssociates">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:SummerMonths">
												<fo:inline font-size="8pt">
													<xsl:apply-templates/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:PostDocAssociates">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:RequestedSalary">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:PostDocAssociates">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:FringeBenefits">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:PostDocAssociates">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:TotalFedNonFed">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:PostDocAssociates">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:Federal">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:PostDocAssociates">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:NonFederal">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<!-- Graduate Students -->
						<fo:table-row>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:GraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:NumberOfPersonnel">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="left" padding="2" display-align="before">
								<fo:block>
									Graduate Students
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:GraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:CalendarMonths">
												<fo:inline font-size="8pt">
													<xsl:apply-templates/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:GraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:AcademicMonths">
												<fo:inline font-size="8pt">
													<xsl:apply-templates/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:GraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:SummerMonths">
												<fo:inline font-size="8pt">
													<xsl:apply-templates/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:GraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:RequestedSalary">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:GraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:FringeBenefits">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:GraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:TotalFedNonFed">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:GraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:Federal">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:GraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:NonFederal">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<!-- Undergraduate Students -->
						<fo:table-row>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:UndergraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:NumberOfPersonnel">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="left" padding="2" display-align="before">
								<fo:block>
									Undergraduate Students
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:UndergraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:CalendarMonths">
												<fo:inline font-size="8pt">
													<xsl:apply-templates/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:UndergraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:AcademicMonths">
												<fo:inline font-size="8pt">
													<xsl:apply-templates/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:UndergraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:SummerMonths">
												<fo:inline font-size="8pt">
													<xsl:apply-templates/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:UndergraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:RequestedSalary">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:UndergraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:FringeBenefits">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:UndergraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:TotalFedNonFed">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:UndergraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:Federal">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:UndergraduateStudents">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:NonFederal">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<!-- Secretarial/Clerical -->
						<fo:table-row>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:SecretarialClerical">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:NumberOfPersonnel">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="left" padding="2" display-align="before">
								<fo:block>
									Secretarial/Clerical
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:SecretarialClerical">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:CalendarMonths">
												<fo:inline font-size="8pt">
													<xsl:apply-templates/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:SecretarialClerical">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:AcademicMonths">
												<fo:inline font-size="8pt">
													<xsl:apply-templates/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:SecretarialClerical">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:SummerMonths">
												<fo:inline font-size="8pt">
													<xsl:apply-templates/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:SecretarialClerical">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:RequestedSalary">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:SecretarialClerical">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:FringeBenefits">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:SecretarialClerical">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:TotalFedNonFed">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:SecretarialClerical">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:Federal">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:SecretarialClerical">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Compensation">
											<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:NonFederal">
												<fo:inline font-size="8pt">
													<xsl:value-of select="format-number(., '#,##0.00')"/>
												</fo:inline>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<!-- Other -->
						<xsl:for-each select="RR_FedNonFedBudget_1_2:Other">
							<fo:table-row>
								<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
									<fo:block>
										<xsl:for-each select="RR_FedNonFedBudget_1_2:NumberOfPersonnel">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell line-height="9pt" text-align="left" padding="2" display-align="before">
									<fo:block>
										<xsl:for-each select="RR_FedNonFedBudget_1_2:ProjectRole">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
									<fo:block>
										<xsl:for-each select="RR_FedNonFedBudget_1_2:CalendarMonths">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
									<fo:block>
										<xsl:for-each select="RR_FedNonFedBudget_1_2:AcademicMonths">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell line-height="9pt" text-align="center" padding="2" display-align="before">
									<fo:block>
										<xsl:for-each select="RR_FedNonFedBudget_1_2:SummerMonths">
											<fo:inline font-size="8pt">
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
									<fo:block>
										<xsl:for-each select="RR_FedNonFedBudget_1_2:RequestedSalary">
											<fo:inline font-size="8pt">
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
									<fo:block>
										<xsl:for-each select="RR_FedNonFedBudget_1_2:FringeBenefits">
											<fo:inline font-size="8pt">
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
									<fo:block>
										<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:TotalFedNonFed">
											<fo:inline font-size="8pt">
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
									<fo:block>
										<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:Federal">
											<fo:inline font-size="8pt">
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell line-height="9pt" text-align="right" padding="2" display-align="before">
									<fo:block>
										<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherTotal/RR_FedNonFedBudget_1_2:NonFederal">
											<fo:inline font-size="8pt">
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					
						<!-- Section B totals -->	
						<fo:table-row>
							<fo:table-cell line-height="9pt" font-weight="bold" text-align="center" padding="2" display-align="before">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherPersonnelTotalNumber">
										<fo:inline font-size="8pt">
											<xsl:apply-templates/>
										</fo:inline>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" font-weight="bold" text-align="left" padding="2" display-align="before">
								<fo:block>
									Total Number Other Personnel
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" font-weight="bold" text-align="right" padding="2" display-align="before" number-columns-spanned="5">
								<fo:block>
									Total Other Personnel
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" font-weight="bold" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:if test="RR_FedNonFedBudget_1_2:TotalOtherPersonnelFund/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary!=''">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalOtherPersonnelFund/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary">
											<fo:inline font-size="8pt" font-weight="bold">
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" font-weight="bold" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:if test="RR_FedNonFedBudget_1_2:TotalOtherPersonnelFund/RR_FedNonFedBudget_1_2:FederalSummary!=''">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalOtherPersonnelFund/RR_FedNonFedBudget_1_2:FederalSummary">
											<fo:inline font-size="8pt" font-weight="bold">
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell line-height="9pt" font-weight="bold" text-align="right" padding="2" display-align="before">
								<fo:block>
									<xsl:if test="RR_FedNonFedBudget_1_2:TotalOtherPersonnelFund/RR_FedNonFedBudget_1_2:NonFederalSummary!=''">
										<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalOtherPersonnelFund/RR_FedNonFedBudget_1_2:NonFederalSummary">
											<fo:inline font-size="8pt" font-weight="bold">
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</xsl:if>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
								&#160;
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell line-height="9pt" font-weight="bold" text-align="right" padding="2" display-align="before" number-columns-spanned="7">
							<fo:block>
								Total Salary, Wages and Fringe Benefits (A+B)
							</fo:block>
						</fo:table-cell>
						<fo:table-cell line-height="9pt" font-weight="bold" text-align="right" padding="2" display-align="before">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalCompensation/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary">
									<fo:inline font-size="8pt" font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell line-height="9pt" font-weight="bold" text-align="right" padding="2" display-align="before">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalCompensation/RR_FedNonFedBudget_1_2:FederalSummary">
									<fo:inline font-size="8pt" font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell line-height="9pt" font-weight="bold" text-align="right" padding="2" display-align="before">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalCompensation/RR_FedNonFedBudget_1_2:NonFederalSummary">
									<fo:inline font-size="8pt" font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
								&#160;
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
								&#160;
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
								&#160;
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>

			<fo:footnote>
				<fo:inline> </fo:inline>
				<fo:footnote-body>
					<fo:block font-size="8pt">RESEARCH &amp; RELATED Budget {A-B} (Total Fed + Non-Fed)</fo:block>
				</fo:footnote-body>
			</fo:footnote>

		</fo:block>
	</xsl:template>

	<xsl:template name="SingleYearPart2">
		<xsl:param name="year"/>
		<fo:block>
			<!--  Page header -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column column-width="14%"/>
				<fo:table-column column-width="9%"/>
				<fo:table-column column-width="19%"/>
				<fo:table-column column-width="22%"/>
				<fo:table-column column-width="18%"/>
				<fo:table-column column-width="18%"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell number-columns-spanned="6" text-align="center" 
							padding-bottom="8" display-align="before">
							<fo:block font-weight="bold" font-size="10pt">
								RESEARCH &amp; RELATED BUDGET (TOTAL FED + NON-FED) - BUDGET PERIOD&#160;<xsl:value-of select="$year"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
								&#160;
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell number-columns-spanned="2" padding-bottom="4">
							<fo:block font-weight="bold">
								* ORGANIZATIONAL DUNS:&#160;&#160;
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-bottom="4">
							<fo:block>
								<xsl:value-of select="../RR_FedNonFedBudget_1_2:DUNSID"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-bottom="4">
							<fo:block font-weight="bold">
								Enter name of Organization:&#160;&#160;
							</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="3" padding-bottom="4">
							<fo:block>
								<xsl:value-of select="../RR_FedNonFedBudget_1_2:OrganizationName"/>
							</fo:block>
                        </fo:table-cell>
					</fo:table-row>
                    <fo:table-row>
						<fo:table-cell padding-bottom="6" display-align="after">
							<fo:block>
								<fo:inline font-weight="bold">
									* Budget Type:&#160;&#160;&#160;&#160;&#160;&#160;
								</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-bottom="6" display-align="after">
							<fo:block>
								<xsl:for-each select="../RR_FedNonFedBudget_1_2:BudgetType">
									<xsl:choose>
										<xsl:when test=".='Project'">
											<fo:inline font-family="ZapfDingbats" font-size="10pt">&#x25cf;</fo:inline>
										</xsl:when>
										<xsl:otherwise>
											<fo:inline font-family="ZapfDingbats" font-size="10pt">&#x274d;</fo:inline>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:for-each>
								<fo:inline padding-left="2">
									Project
								</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-bottom="6" display-align="after">
							<fo:block>
								<xsl:for-each select="../RR_FedNonFedBudget_1_2:BudgetType">
									<xsl:choose>
										<xsl:when test=".='Subaward/Consortium'">
											<fo:inline font-family="ZapfDingbats" font-size="10pt">&#x25cf;</fo:inline>
										</xsl:when>
										<xsl:otherwise>
											<fo:inline font-family="ZapfDingbats" font-size="10pt">&#x274d;</fo:inline>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:for-each>
								<fo:inline padding-left="2">
									Subaward/Consortium
								</fo:inline>
							</fo:block>
							<fo:block />
						</fo:table-cell>
						<fo:table-cell padding-bottom="6" display-align="after">
							<fo:block>
								<fo:inline font-weight="bold">
									Budget Period:&#160;&#160;
								</fo:inline>
								<xsl:value-of select="$year"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="center" padding-bottom="6" display-align="after">
							<fo:block>
								<fo:inline font-weight="bold">
									* Start Date:&#160;
								</fo:inline>
								<xsl:call-template name="formatDate">
									<xsl:with-param name="value" select="RR_FedNonFedBudget_1_2:BudgetPeriodStartDate"/>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="center" padding-bottom="6" display-align="after">
							<fo:block>
								<fo:inline font-weight="bold">
								* End Date:&#160;
								</fo:inline>
								<xsl:call-template name="formatDate">
									<xsl:with-param name="value" select="RR_FedNonFedBudget_1_2:BudgetPeriodEndDate"/>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block />
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
								&#160;
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<!-- Section C.  Equipment Description -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell border-bottom="1px solid black" text-align="left" padding="2" display-align="before">
							<fo:block>
								<fo:inline font-weight="bold">C. Equipment Description</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:block>
				<fo:inline font-size="8pt" font-weight="bold">List items and dollar amount for each item exceeding $5,000</fo:inline>
			</fo:block>
							
			<fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt" font-size="8pt">
				<fo:table-column column-width="55%"/>
				<fo:table-column column-width="11%"/>
				<fo:table-column column-width="14%"/>
				<fo:table-column column-width="20%"/>
				<fo:table-header>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline font-weight="bold" padding-left="10px">*Equipment Item</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Non-Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Total (Fed + Non-Fed) (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
				<xsl:if test="string-length(RR_FedNonFedBudget_1_2:Equipment)=0">
					<fo:table-row>
						<fo:table-cell >
							<fo:block/>
						</fo:table-cell>
					</fo:table-row>
				</xsl:if>
				<xsl:for-each select="RR_FedNonFedBudget_1_2:Equipment">
					<xsl:for-each select="RR_FedNonFedBudget_1_2:EquipmentList">
						<fo:table-row line-height="12px">
							<fo:table-cell padding-left="10px">
								<fo:block>
									<fo:inline>
										<xsl:value-of select="RR_FedNonFedBudget_1_2:EquipmentItem"/>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell text-align="right">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:FundsRequested/RR_FedNonFedBudget_1_2:Federal">
										<fo:inline>
											<xsl:value-of select="format-number(., '#,##0.00')"/>
										</fo:inline>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell text-align="right">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:FundsRequested/RR_FedNonFedBudget_1_2:NonFederal">
										<fo:inline>
											<xsl:value-of select="format-number(., '#,##0.00')"/>
										</fo:inline>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell text-align="right">
								<fo:block>
									<xsl:for-each select="RR_FedNonFedBudget_1_2:FundsRequested/RR_FedNonFedBudget_1_2:TotalFedNonFed">
										<fo:inline>
											<xsl:value-of select="format-number(., '#,##0.00')"/>
										</fo:inline>
									</xsl:for-each>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</xsl:for-each>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>&#160;</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row line-height="12px">
						<fo:table-cell number-columns-spanned="2">
							<fo:block>
								<fo:inline font-weight="bold">* Additional Equipment: </fo:inline>
								<fo:inline padding-left="20px">File Name: </fo:inline>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Equipment/RR_FedNonFedBudget_1_2:AdditionalEquipmentsAttachment">
									<xsl:for-each select="att:FileName">
										<fo:inline>
											<xsl:apply-templates/>
										</fo:inline>
									</xsl:for-each>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="2">
							<fo:block>
								Mime Type: 
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Equipment/RR_FedNonFedBudget_1_2:AdditionalEquipmentsAttachment">
									<xsl:for-each select="att:MimeType">
										<fo:inline>
											<xsl:apply-templates/>
										</fo:inline>
									</xsl:for-each>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<!-- Equipment attachment -->
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>&#160;</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row line-height="12px">
						<fo:table-cell text-align="right" font-weight="bold">
							<fo:block>
								<fo:inline>Total funds requested for all equipment listed in the attached file</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Equipment/RR_FedNonFedBudget_1_2:TotalFundForAttachedEquipment/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Equipment/RR_FedNonFedBudget_1_2:TotalFundForAttachedEquipment/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline font-size="8pt" font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Equipment/RR_FedNonFedBudget_1_2:TotalFundForAttachedEquipment/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline font-size="8pt" font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
								</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<!-- Total Equipment -->
					<fo:table-row line-height="12px">
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">Total Equipment</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Equipment/RR_FedNonFedBudget_1_2:TotalFund/RR_FedNonFedBudget_1_2:FederalSummary">
									<fo:inline font-size="8pt" font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Equipment/RR_FedNonFedBudget_1_2:TotalFund/RR_FedNonFedBudget_1_2:NonFederalSummary">
									<fo:inline font-size="8pt" font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Equipment/RR_FedNonFedBudget_1_2:TotalFund/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary">
									<fo:inline font-size="8pt" font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>

			<fo:block>
				<fo:leader leader-pattern="space"/>
			</fo:block>
			
			<!-- Section D.  Travel -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell border-bottom="1px solid black" text-align="left" padding="2" display-align="before">
							<fo:block>
								<fo:inline font-weight="bold">D. Travel</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt" font-size="8pt">
				<fo:table-column column-width="55%"/>
				<fo:table-column column-width="11%"/>
				<fo:table-column column-width="14%"/>
				<fo:table-column column-width="20%"/>
				<fo:table-header>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>&#160;</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Non-Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Total (Fed + Non-Fed) (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>1. Domestic Travel Costs (Incl. Canada, Mexico, and U.S. Possessions)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Travel/RR_FedNonFedBudget_1_2:DomesticTravelCost/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline font-size="8pt">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Travel/RR_FedNonFedBudget_1_2:DomesticTravelCost/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline font-size="8pt">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Travel/RR_FedNonFedBudget_1_2:DomesticTravelCost/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>2. Foreign Travel Costs</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Travel/RR_FedNonFedBudget_1_2:ForeignTravelCost/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Travel/RR_FedNonFedBudget_1_2:ForeignTravelCost/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Travel/RR_FedNonFedBudget_1_2:ForeignTravelCost/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">Total Travel Costs</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Travel/RR_FedNonFedBudget_1_2:TotalTravelCost/RR_FedNonFedBudget_1_2:FederalSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Travel/RR_FedNonFedBudget_1_2:TotalTravelCost/RR_FedNonFedBudget_1_2:NonFederalSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Travel/RR_FedNonFedBudget_1_2:TotalTravelCost/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:block>
				<fo:leader leader-pattern="space"/>
			</fo:block>
			
			<!-- Section E.  Participant/Trainee Support Costs -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell border-bottom="1px solid black" text-align="left" padding="2" display-align="before">
							<fo:block>
								<fo:inline font-weight="bold">E. Participant/Trainee Support Costs</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt" font-size="8pt">
				<fo:table-column column-width="26%"/>
				<fo:table-column column-width="29%"/>
				<fo:table-column column-width="11%"/>
				<fo:table-column column-width="14%"/>
				<fo:table-column column-width="20%"/>
				<fo:table-header>
					<fo:table-row line-height="12px">
						<fo:table-cell number-columns-spanned="2">
							<fo:block>
								<fo:inline>&#160;</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Non-Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Total (Fed + Non-Fed) (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<fo:table-row line-height="12px">
						<fo:table-cell number-columns-spanned="2">
							<fo:block>
								<fo:inline>1. Tuition/Fees/Health Insurance</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:TuitionFeeHealthInsurance/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:TuitionFeeHealthInsurance/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:TuitionFeeHealthInsurance/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row line-height="12px">
						<fo:table-cell number-columns-spanned="2">
							<fo:block>
								<fo:inline>2. Stipends</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:Stipends/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:Stipends/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:Stipends/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row line-height="12px">
						<fo:table-cell number-columns-spanned="2">
							<fo:block>
								<fo:inline>3. Travel</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:ParticipantTravel/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:ParticipantTravel/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:ParticipantTravel/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row line-height="12px">
						<fo:table-cell number-columns-spanned="2">
							<fo:block>
								<fo:inline>4. Subsistence</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:Subsistence/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:Subsistence/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:Subsistence/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row line-height="12px">
						<fo:table-cell number-columns-spanned="2">
							<fo:block>
								<fo:inline>5. Other&#160;&#160;</fo:inline>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:Other">
									<xsl:for-each select="RR_FedNonFedBudget_1_2:Description">
										<fo:inline font-size="8pt">
											<xsl:apply-templates/>
										</fo:inline>
									</xsl:for-each>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:Other/RR_FedNonFedBudget_1_2:Cost/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:Other/RR_FedNonFedBudget_1_2:Cost/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:Other/RR_FedNonFedBudget_1_2:Cost/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell font-weight="bold">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:ParticipantTraineeNumber">
									<fo:inline text-align="left">
										<xsl:apply-templates/>
									</fo:inline>
								</xsl:for-each>
								<fo:inline>&#160;&#160;Number of Participants/Trainees</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right" font-weight="bold">
							<fo:block>
								<fo:inline text-align="right">Total Participant/Trainee Support Costs</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right" font-weight="bold">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:TotalCost/RR_FedNonFedBudget_1_2:FederalSummary">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right" font-weight="bold">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:TotalCost/RR_FedNonFedBudget_1_2:NonFederalSummary">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right" font-weight="bold">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:ParticipantTraineeSupportCosts/RR_FedNonFedBudget_1_2:TotalCost/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>

			<fo:footnote>
				<fo:inline> </fo:inline>
				<fo:footnote-body>
					<fo:block font-size="8pt">RESEARCH &amp; RELATED Budget {C-E} (Total Fed + Non-Fed)</fo:block>
				</fo:footnote-body>
			</fo:footnote>
			
			<fo:block break-after="page">
				<xsl:text>&#xA;</xsl:text>
			</fo:block>

			<!--  Page header -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column column-width="14%"/>
				<fo:table-column column-width="9%"/>
				<fo:table-column column-width="19%"/>
				<fo:table-column column-width="22%"/>
				<fo:table-column column-width="18%"/>
				<fo:table-column column-width="18%"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell number-columns-spanned="6" text-align="center" 
							padding-bottom="8" display-align="before">
							<fo:block font-weight="bold" font-size="10pt">
								RESEARCH &amp; RELATED BUDGET (TOTAL FED + NON-FED) - BUDGET PERIOD&#160;<xsl:value-of select="$year"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
								&#160;
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell number-columns-spanned="2" padding-bottom="4">
							<fo:block font-weight="bold">
								* ORGANIZATIONAL DUNS:&#160;&#160;
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-bottom="4">
							<fo:block>
								<xsl:value-of select="../RR_FedNonFedBudget_1_2:DUNSID"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-bottom="4">
							<fo:block font-weight="bold">
								Enter name of Organization:&#160;&#160;
							</fo:block>
						</fo:table-cell>
						<fo:table-cell number-columns-spanned="3" padding-bottom="4">
							<fo:block>
								<xsl:value-of select="../RR_FedNonFedBudget_1_2:OrganizationName"/>
							</fo:block>
                        </fo:table-cell>
					</fo:table-row>
                    <fo:table-row>
						<fo:table-cell padding-bottom="6" display-align="after">
							<fo:block>
								<fo:inline font-weight="bold">
									* Budget Type:&#160;&#160;&#160;&#160;&#160;&#160;
								</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-bottom="6" display-align="after">
							<fo:block>
								<xsl:for-each select="../RR_FedNonFedBudget_1_2:BudgetType">
									<xsl:choose>
										<xsl:when test=".='Project'">
											<fo:inline font-family="ZapfDingbats" font-size="10pt">&#x25cf;</fo:inline>
										</xsl:when>
										<xsl:otherwise>
											<fo:inline font-family="ZapfDingbats" font-size="10pt">&#x274d;</fo:inline>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:for-each>
								<fo:inline padding-left="2">
									Project
								</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-bottom="6" display-align="after">
							<fo:block>
								<xsl:for-each select="../RR_FedNonFedBudget_1_2:BudgetType">
									<xsl:choose>
										<xsl:when test=".='Subaward/Consortium'">
											<fo:inline font-family="ZapfDingbats" font-size="10pt">&#x25cf;</fo:inline>
										</xsl:when>
										<xsl:otherwise>
											<fo:inline font-family="ZapfDingbats" font-size="10pt">&#x274d;</fo:inline>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:for-each>
								<fo:inline padding-left="2">
									Subaward/Consortium
								</fo:inline>
							</fo:block>
							<fo:block />
						</fo:table-cell>
						<fo:table-cell padding-bottom="6" display-align="after">
							<fo:block>
								<fo:inline font-weight="bold">
									Budget Period:&#160;&#160;
								</fo:inline>
								<xsl:value-of select="$year"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="center" padding-bottom="6" display-align="after">
							<fo:block>
								<fo:inline font-weight="bold">
									* Start Date:&#160;
								</fo:inline>
								<xsl:call-template name="formatDate">
									<xsl:with-param name="value" select="RR_FedNonFedBudget_1_2:BudgetPeriodStartDate"/>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="center" padding-bottom="6" display-align="after">
							<fo:block>
								<fo:inline font-weight="bold">
								* End Date:&#160;
								</fo:inline>
								<xsl:call-template name="formatDate">
									<xsl:with-param name="value" select="RR_FedNonFedBudget_1_2:BudgetPeriodEndDate"/>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block />
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
								&#160;
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<!-- Section F.  Other Direct Costs -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell border-bottom="1px solid black" text-align="left" padding="2" display-align="before">
							<fo:block>
								<fo:inline font-weight="bold">F. Other Direct Costs</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt" font-size="8pt">
				<fo:table-column column-width="55%"/>
				<fo:table-column column-width="11%"/>
				<fo:table-column column-width="14%"/>
				<fo:table-column column-width="20%"/>
				<fo:table-header>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>&#160;</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Non-Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Total (Fed + Non-Fed) (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>1. Materials and Supplies</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:MaterialsSupplies/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:MaterialsSupplies/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:MaterialsSupplies/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>2. Publication Costs</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:PublicationCosts/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:PublicationCosts/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:PublicationCosts/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>3. Consultant Services</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:ConsultantServices/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:ConsultantServices/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:ConsultantServices/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>4. ADP/Computer Services</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:ADPComputerServices/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:ADPComputerServices/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:ADPComputerServices/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>5. Subaward/Consortium/Contractual Costs</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:SubawardConsortiumContractualCosts/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:SubawardConsortiumContractualCosts/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:SubawardConsortiumContractualCosts/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>6. Equipment or Facility Rental/User Fees</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:EquipmentRentalFee/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:EquipmentRentalFee/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:EquipmentRentalFee/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>	
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>7. Alterations and Renovations</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:AlterationsRenovations/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:AlterationsRenovations/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:AlterationsRenovations/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>	
					<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:Others">
						<xsl:for-each select="RR_FedNonFedBudget_1_2:Other">
							<fo:table-row line-height="12px">
								<fo:table-cell>
									<fo:block>
										<fo:inline>
											<xsl:value-of select="position()+7"/>.&#160;</fo:inline>
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Description">
											<fo:inline>
												<xsl:apply-templates/>
											</fo:inline>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="right">
									<fo:block>
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Cost/RR_FedNonFedBudget_1_2:Federal">
											<fo:inline>
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="right">
									<fo:block>
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Cost/RR_FedNonFedBudget_1_2:NonFederal">
											<fo:inline>
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell text-align="right">
									<fo:block>
										<xsl:for-each select="RR_FedNonFedBudget_1_2:Cost/RR_FedNonFedBudget_1_2:TotalFedNonFed">
											<fo:inline>
												<xsl:value-of select="format-number(., '#,##0.00')"/>
											</fo:inline>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:for-each>
					<fo:table-row>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">Total Other Direct Costs</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:TotalOtherDirectCost/RR_FedNonFedBudget_1_2:FederalSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:TotalOtherDirectCost/RR_FedNonFedBudget_1_2:NonFederalSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:OtherDirectCosts/RR_FedNonFedBudget_1_2:TotalOtherDirectCost/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:block>
				<fo:leader leader-pattern="space"/>
			</fo:block>
			
			<!-- Section G.  Direct Costs -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell border-bottom="1px solid black" text-align="left" padding="2" display-align="before">
							<fo:block>
								<fo:inline font-weight="bold">G. Direct Costs</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt" font-size="8pt">
				<fo:table-column column-width="55%"/>
				<fo:table-column column-width="11%"/>
				<fo:table-column column-width="14%"/>
				<fo:table-column column-width="20%"/>
				<fo:table-header>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>&#160;</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Non-Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Total (Fed + Non-Fed) (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<fo:table-row line-height="12px">
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">Total Direct Costs (A thru F)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:DirectCosts/RR_FedNonFedBudget_1_2:FederalSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:DirectCosts/RR_FedNonFedBudget_1_2:NonFederalSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:DirectCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>

			<fo:block>
				<fo:leader leader-pattern="space"/>
			</fo:block>
			
			<!-- Section H.  Indirect Costs -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell border-bottom="1px solid black" text-align="left" padding="2" display-align="before">
							<fo:block>
								<fo:inline font-weight="bold">H. Indirect Costs</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt" font-size="8pt">
				<fo:table-column column-width="30%"/>
				<fo:table-column column-width="12%"/>
				<fo:table-column column-width="13%"/>
				<fo:table-column column-width="11%"/>
				<fo:table-column column-width="14%"/>
				<fo:table-column column-width="20%"/>
				<fo:table-header>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline font-weight="bold">Indirect Cost Type</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell font-weight="bold" text-align="right">
							<fo:block>
								Indirect Cost
							</fo:block>
							<fo:block>
								 Rate (%)
							</fo:block>
						</fo:table-cell>
						<fo:table-cell font-weight="bold" text-align="right">
							<fo:block>
								Indirect Cost
							</fo:block>
							<fo:block>
							 	Base ($)
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Non-Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Total (Fed + Non-Fed) (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
				<xsl:for-each select="RR_FedNonFedBudget_1_2:IndirectCosts">
					<xsl:for-each select="RR_FedNonFedBudget_1_2:IndirectCost">
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:CostType">
									<fo:inline>
										<xsl:apply-templates/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Rate">
									<fo:inline>
										<xsl:apply-templates/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Base">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:FundRequested/RR_FedNonFedBudget_1_2:Federal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:FundRequested/RR_FedNonFedBudget_1_2:NonFederal">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:FundRequested/RR_FedNonFedBudget_1_2:TotalFedNonFed">
									<fo:inline>
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:for-each>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								&#160;
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row line-height="12px">
						<fo:table-cell font-weight="bold" text-align="right" number-columns-spanned="3">
							<fo:block>
								<fo:inline>Total Indirect Costs</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell font-weight="bold" text-align="right">
							<fo:block>
								<fo:inline>
									<xsl:choose>
										<xsl:when test="RR_FedNonFedBudget_1_2:TotalIndirectCosts/RR_FedNonFedBudget_1_2:FederalSummary">
											<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:TotalIndirectCosts/RR_FedNonFedBudget_1_2:FederalSummary, '#,##0.00')"/>
										</xsl:when>
										<xsl:otherwise>0.00</xsl:otherwise>
									</xsl:choose>
								</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell font-weight="bold" text-align="right">
							<fo:block>
								<fo:inline>
									<xsl:choose>
										<xsl:when test="RR_FedNonFedBudget_1_2:TotalIndirectCosts/RR_FedNonFedBudget_1_2:NonFederalSummary">
											<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:TotalIndirectCosts/RR_FedNonFedBudget_1_2:NonFederalSummary, '#,##0.00')"/>
										</xsl:when>
										<xsl:otherwise>0.00</xsl:otherwise>
									</xsl:choose>
								</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell font-weight="bold" text-align="right">
							<fo:block>
								<fo:inline>
									<xsl:choose>
										<xsl:when test="RR_FedNonFedBudget_1_2:TotalIndirectCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary">
											<xsl:value-of select="format-number(RR_FedNonFedBudget_1_2:TotalIndirectCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary, '#,##0.00')"/>
										</xsl:when>
										<xsl:otherwise>0.00</xsl:otherwise>
									</xsl:choose>
								</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								&#160;
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					</xsl:for-each>

					<fo:table-row>
						<fo:table-cell font-size="7pt">
                            <fo:block font-weight="bold">
								Cognizant Federal Agency
							</fo:block>
							<fo:block>
								(Agency Name, POC Name and
							</fo:block>
							<fo:block>
								 Phone Number)
							</fo:block>	
						</fo:table-cell>							
						<fo:table-cell number-columns-spanned="4">
							<fo:block>
								<xsl:value-of select="RR_FedNonFedBudget_1_2:CognizantFederalAgency" />
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:block>
				<fo:leader leader-pattern="space"/>
			</fo:block>
			
			<!-- Section I.  Total Direct and Indirect Costs -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell border-bottom="1px solid black" text-align="left" padding="2" display-align="before">
							<fo:block>
								<fo:inline font-weight="bold">I. Total Direct and Indirect Costs</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt" font-size="8pt">
				<fo:table-column column-width="55%"/>
				<fo:table-column column-width="11%"/>
				<fo:table-column column-width="14%"/>
				<fo:table-column column-width="20%"/>
				<fo:table-header>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>&#160;</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Non-Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Total (Fed + Non-Fed) (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<fo:table-row line-height="12px">
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">Total Direct and Indirect Costs (G + H)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalCosts/RR_FedNonFedBudget_1_2:FederalSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalCosts/RR_FedNonFedBudget_1_2:NonFederalSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalCosts/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>

			<fo:block>
				<fo:leader leader-pattern="space"/>
			</fo:block>
			
			<!-- Section J.  Fee -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell border-bottom="1px solid black" text-align="left" padding="2" display-align="before">
							<fo:block>
								<fo:inline font-weight="bold">J. Fee</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt" font-size="8pt">
				<fo:table-column column-width="55%"/>
				<fo:table-column column-width="11%"/>
				<fo:table-column column-width="14%"/>
				<fo:table-column column-width="20%"/>
				<fo:table-header>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>&#160;</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block />
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block />
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<fo:table-row line-height="12px">
						<fo:table-cell text-align="right">
							<fo:block />
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:Fee">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block />
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block />
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:block>
				<fo:leader leader-pattern="space"/>
			</fo:block>
			
			<!-- Section K.  Total Costs and Fee -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell border-bottom="1px solid black" text-align="left" padding="2" display-align="before">
							<fo:block>
								<fo:inline font-weight="bold">K. Total Costs and Fee</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt" font-size="8pt">
				<fo:table-column column-width="55%"/>
				<fo:table-column column-width="11%"/>
				<fo:table-column column-width="14%"/>
				<fo:table-column column-width="20%"/>
				<fo:table-header>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>&#160;</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Non-Federal (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">*Total (Fed + Non-Fed) (&#36;)</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<fo:table-row line-height="12px">
						<fo:table-cell text-align="right">
							<fo:block>
								<fo:inline font-weight="bold">Total Costs and Fee (I + J)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalCostsFee/RR_FedNonFedBudget_1_2:FederalSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalCostsFee/RR_FedNonFedBudget_1_2:NonFederalSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block>
								<xsl:for-each select="RR_FedNonFedBudget_1_2:TotalCostsFee/RR_FedNonFedBudget_1_2:TotalFedNonFedSummary">
									<fo:inline font-weight="bold">
										<xsl:value-of select="format-number(., '#,##0.00')"/>
									</fo:inline>
								</xsl:for-each>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>

			<fo:block>
				<fo:leader leader-pattern="space"/>
			</fo:block>
			
			<!-- Section L.  Budget Justifcation -->
			<fo:table width="100%" space-before.optimum="0pt" space-after.optimum="0pt" font-size="8pt">
				<fo:table-column/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell border-bottom="1px solid black" text-align="left" padding="2" display-align="before">
							<fo:block>
								<fo:inline font-weight="bold">L. * Budget Justification</fo:inline>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt" font-size="8pt">
				<fo:table-column column-width="20%"/>
				<fo:table-column column-width="55%"/>
				<fo:table-column column-width="25%"/>
				<fo:table-body>
					<fo:table-row line-height="12px">
						<fo:table-cell>
							<fo:block>
								<fo:inline>(Only attach one file.)</fo:inline>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block>
								<fo:inline>File Name: </fo:inline>
								<xsl:value-of select="../RR_FedNonFedBudget_1_2:BudgetJustificationAttachment/att:FileName"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block>
								<fo:inline>Mime Type: </fo:inline>
								<xsl:value-of select="../RR_FedNonFedBudget_1_2:BudgetJustificationAttachment/att:MimeType"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell text-align="right">
							<fo:block />
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<fo:footnote>
				<fo:inline> </fo:inline>
				<fo:footnote-body>
					<fo:block font-size="8pt">RESEARCH &amp; RELATED Budget {F-K} (Total Fed + Non-Fed)</fo:block>
				</fo:footnote-body>
			</fo:footnote>

		</fo:block>
	</xsl:template>
	<!--===========================End Single Year Template===========-->
	
	<!--========================== date template =================-->
	<xsl:template name="formatDate">
		<xsl:param name="value"/>
		<xsl:value-of select="format-number(substring($value,6,2), '00')"/>
		<xsl:text>-</xsl:text>
		<xsl:value-of select="format-number(substring($value,9,2), '00')"/>
		<xsl:text>-</xsl:text>
		<xsl:value-of select="format-number(substring($value,1,4), '0000')"/>
	</xsl:template>
</xsl:stylesheet>
