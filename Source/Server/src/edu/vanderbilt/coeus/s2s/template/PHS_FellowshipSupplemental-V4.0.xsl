<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:PHS_Fellowship_Supplemental_4_0="http://apply.grants.gov/forms/PHS_Fellowship_Supplemental_4_0-V4.0" 
xmlns:att="http://apply.grants.gov/system/Attachments-V1.0" xmlns:codes="http://apply.grants.gov/system/UniversalCodes-V2.0" 
xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:glob="http://apply.grants.gov/system/Global-V1.0" 
xmlns:globLib="http://apply.grants.gov/system/GlobalLibrary-V2.0" xmlns:xdt="http://www.w3.org/2005/xpath-datatypes" 
xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xmlns:header="http://apply.grants.gov/system/Header-V1.0"
xmlns:footer="http://apply.grants.gov/system/Footer-V1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:output version="1.0" method="xml" encoding="UTF-8" indent="no"/>
	<xsl:param name="SV_OutputFormat" select="'PDF'"/>
	<xsl:variable name="XML" select="/"/>
	<xsl:variable name="fo:layout-master-set">
		<fo:layout-master-set>
			<fo:simple-page-master master-name="default-page" page-height="11.0in" page-width="8.5in" 
				margin-left="0.5in" margin-right="0.5in">
				<fo:region-body margin-top="0.7in" margin-bottom="0.5in"/>
				<fo:region-before extent="0.7in"/>
				<fo:region-after extent=".3in" />
			</fo:simple-page-master>
		</fo:layout-master-set>
	</xsl:variable>
	<xsl:template match="/">
		<fo:root>
			<xsl:copy-of select="$fo:layout-master-set"/>
			<fo:page-sequence master-reference="default-page" initial-page-number="1" format="1">
				<xsl:call-template name="headerall"/>

				<fo:flow flow-name="xsl-region-body">
					<fo:block>
						<xsl:for-each select="$XML">
							<fo:table font-size="9pt" width="100%" border="solid 1px black">
								<fo:table-column column-width="46%"/>
								<fo:table-column column-width="54%"/>
								<fo:table-body>
									
									<!-- Introduction START -->
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" padding="6px" 
											padding-bottom="0px" display-align="center">
											<fo:block font-weight="bold" font-size="10pt">
												Introduction
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 1. Introduction -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												1. Introduction
											</fo:block>
											<fo:block font-size="6pt" margin-left="10px">
												(for Resubmission applications)
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Introduction">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:IntroductionToApplication">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>		
									
									<!-- Fellowship Applicant Section START -->
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" padding="6px" 
											padding-bottom="0px" display-align="center"
											border-top="1px solid black">
											<fo:block font-weight="bold" font-size="10pt">
												Fellowship Applicant Section
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 2. Background and Goals -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												2. * Applicant's Background and Goals for Fellowship Training
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:FellowshipApplicant">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:BackgroundandGoals">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>									

									<!-- Research Training Plan Section START -->
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" padding="6px" 
											padding-bottom="0px" display-align="center"
											border-top="1px solid black">
											<fo:block font-weight="bold" font-size="10pt">
												Research Training Plan Section
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 3. Specific Aims -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												3. * Specific Aims
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:ResearchTrainingPlan">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:SpecificAims">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 4. Research Strategy -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												4. * Research Strategy
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:ResearchTrainingPlan">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:ResearchStrategy">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 5. Respective Contributions -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												5. * Respective Contributions
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:ResearchTrainingPlan">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:RespectiveContribution">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 6. Selection of Sponsor and Institution -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												6. * Selection of Sponsor and Institution
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:ResearchTrainingPlan">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:SponsorandInstitution">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 7. Progress Report Publication List -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												7. Progress Report Publication List
											</fo:block>
											<fo:block font-size="6pt" margin-left="10px">
												(for Renewal applications)
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:ResearchTrainingPlan">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:ProgressReportPublicationList">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 8. Training in the Responsible Conduct of Research -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												8. * Training in the Responsible Conduct of Research
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:ResearchTrainingPlan">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:TrainingInResponsibleConductOfResearch">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									<!-- Sponsor(s), Collaborator(s) and Consultant(s) Section START -->
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" padding="6px" 
											padding-bottom="0px" display-align="center"
											border-top="1px solid black">
											<fo:block font-weight="bold" font-size="10pt">
												Sponsor(s), Collaborator(s) and Consultant(s) Section
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 9. Sponsor and Co-Sponsor Statements -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												9. Sponsor and Co-Sponsor Statements
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Sponsors">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:SponsorAndCoSponsorStatements">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 10. Letters of Support from Collaborators, Contributors and Consultants -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												10. Letters of Support from Collaborators, Contributors and Consultants
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Sponsors">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:LettersOfSupport">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>

									<!-- Institutional Environment and Commitment to Training Section START -->
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" padding="6px" 
											padding-bottom="0px" display-align="center"
											border-top="1px solid black">
											<fo:block font-weight="bold" font-size="10pt">
												Institutional Environment and Commitment to Training Section
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 11. Description of Institutional Environment and Commitment to Training -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												11. Description of Institutional Environment and Commitment to Training
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:InstitutionalEnvironment">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:InstitutionalEnvironmentCommitmenttoTraining">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>

									<!-- Other Research Training Plan Section START -->
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" padding="6px" 
											padding-bottom="0px" display-align="center"
											border-top="1px solid black">
											<fo:block font-weight="bold" font-size="10pt">
												Other Research Training Plan Section
											</fo:block>
										</fo:table-cell>
									</fo:table-row>

									<!--  Vertebrate Animals -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before"
											font-weight="bold" text-decoration="underline" 
											number-columns-spanned="2" padding-left="10px">
											<fo:block>
												Vertebrate Animals
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- Vertebrate Animals Used? -->
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" display-align="center">
											<fo:block margin-left="20px" margin-right="20px" 
												border="1px solid black" padding="6px" font-size="9pt">
												<fo:block>
													The following item is taken from the Research &amp; Related Other Project Information 
													form and repeated here for your reference. Any change to this item must be made on the 
													Research &amp; Related Other Project Information form.
												</fo:block>
												<fo:block text-align="center" padding="6px">
													<fo:inline>
														<xsl:text>Are Vertebrate Animals Used? &#160;&#160;&#160;&#160;&#160;&#160;</xsl:text>
													</fo:inline>
													<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:OtherResearchTrainingPlan">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:VertebrateAnimalsUsed">
																<xsl:choose>
																	<xsl:when test="string(.)='Y: Yes'">
																		<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																			<xsl:text>&#x2714;</xsl:text>
																		</fo:inline>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:inline border="solid 1pt black">
																			<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																		</fo:inline>
																	</xsl:otherwise>
																</xsl:choose>
																<fo:inline>
																	<xsl:text> Yes&#160;&#160;&#160;&#160;&#160;&#160; </xsl:text>
																</fo:inline>
																<xsl:choose>
																	<xsl:when test="string(.)='N: No'">
																		<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																			<xsl:text>&#x2714;</xsl:text>
																		</fo:inline>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:inline border="solid 1pt black">
																			<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																		</fo:inline>
																	</xsl:otherwise>
																</xsl:choose>
																<fo:inline>
																	<xsl:text> No</xsl:text>
																</fo:inline>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</fo:block>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 12. Are vertebrate animals euthanized? -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="center">
											<fo:block>
												12. Are vertebrate animals euthanized?
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="center">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:OtherResearchTrainingPlan">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AreAnimalsEuthanized">
															<xsl:choose>
																<xsl:when test="string(.)='Y: Yes'">
																	<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																		<xsl:text>&#x2714;</xsl:text>
																	</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline border="solid 1pt black">
																		<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
															<fo:inline>
																<xsl:text> Yes&#160;&#160;&#160;&#160;&#160;&#160; </xsl:text>
															</fo:inline>
															<xsl:choose>
																<xsl:when test="string(.)='N: No'">
																	<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																		<xsl:text>&#x2714;</xsl:text>
																	</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline border="solid 1pt black">
																		<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
															<fo:inline>
																<xsl:text> No</xsl:text>
															</fo:inline>
														</xsl:for-each>
														<xsl:if test="not(//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0/PHS_Fellowship_Supplemental_4_0:OtherResearchTrainingPlan/PHS_Fellowship_Supplemental_4_0:AreAnimalsEuthanized)">
															<fo:inline border="solid 1pt black">
																<fo:leader leader-length="7.5pt" leader-pattern="space"/>
															</fo:inline>
															<fo:inline>
																<xsl:text> Yes&#160;&#160;&#160;&#160;&#160;&#160; </xsl:text>
															</fo:inline>
															<fo:inline border="solid 1pt black">
																<fo:leader leader-length="7.5pt" leader-pattern="space"/>
															</fo:inline>
															<fo:inline>
																<xsl:text> No</xsl:text>
															</fo:inline>
														</xsl:if>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- Euthanization info -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="center">
											<fo:block>
												If "<fo:inline font-weight="bold">Yes</fo:inline>"" to euthanasia
											</fo:block>
											<fo:block margin-left="10px">
												Is method consistent with American Veterinary Medical Association (AVMA) guidelines?
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="center">
											<fo:block>									
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:OtherResearchTrainingPlan">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AVMAConsistentIndicator">
															<xsl:choose>
																<xsl:when test="string(.)='Y: Yes'">
																	<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																		<xsl:text>&#x2714;</xsl:text>
																	</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline border="solid 1pt black">
																		<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
															<fo:inline>
																<xsl:text> Yes&#160;&#160;&#160;&#160;&#160;&#160; </xsl:text>
															</fo:inline>
															<xsl:choose>
																<xsl:when test="string(.)='N: No'">
																	<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																		<xsl:text>&#x2714;</xsl:text>
																	</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline border="solid 1pt black">
																		<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
															<fo:inline>
																<xsl:text> No</xsl:text>
															</fo:inline>
														</xsl:for-each>
														<!-- Also display a blank box if section missing -->
														<xsl:if test="not(//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0/PHS_Fellowship_Supplemental_4_0:OtherResearchTrainingPlan/PHS_Fellowship_Supplemental_4_0:AVMAConsistentIndicator)">
															<fo:inline border="solid 1pt black">
																<fo:leader leader-length="7.5pt" leader-pattern="space"/>
															</fo:inline>
															<fo:inline>
																<xsl:text> Yes&#160;&#160;&#160;&#160;&#160;&#160; </xsl:text>
															</fo:inline>
															<fo:inline border="solid 1pt black">
																<fo:leader leader-length="7.5pt" leader-pattern="space"/>
															</fo:inline>
															<fo:inline>
																<xsl:text> No</xsl:text>
															</fo:inline>
														</xsl:if>	
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- Method and Scientific Justification -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block margin-top="2px">
												If "<fo:inline font-weight="bold">No</fo:inline>" to AVMA guidelines, 
												describe method and provide scientific justification
											</fo:block>
										</fo:table-cell>
										<fo:table-cell display-align="before" padding="4px">
											<fo:block-container border="1px solid black" height="40px" 
												margin-right="15px" padding="2px">
												<fo:block>
													<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:OtherResearchTrainingPlan">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:EuthanasiaMethodDescription">
																<xsl:variable name="value-of-template">
																	<xsl:apply-templates/>
																</xsl:variable>
																<xsl:choose>
																	<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																		<fo:block>
																			<xsl:copy-of select="$value-of-template"/>
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:inline>
																			<xsl:copy-of select="$value-of-template"/>
																		</fo:inline>
																	</xsl:otherwise>
																</xsl:choose>																				
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</fo:block>
											</fo:block-container>
										</fo:table-cell>
									</fo:table-row>														
									<!-- 13. Vertebrate Animals -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												13. Vertebrate Animals
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:OtherResearchTrainingPlan">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:VertebrateAnimals">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									<!-- spacer row -->
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" 
											border-bottom="1px solid black">
											<fo:block break-after="page">
												<xsl:text>&#xA;</xsl:text>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>	
									
									<!-- Other Research Training Plan Information -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before"
											font-weight="bold" text-decoration="underline" 
											number-columns-spanned="2" padding-left="10px" border-top="1px solid black">
											<fo:block>
												Other Research Training Plan Information
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 14. Select Agent Research -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												14. Select Agent Research
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:OtherResearchTrainingPlan">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:SelectAgentResearch">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 15. Resource Sharing Plan -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												15. Resource Sharing Plan
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:OtherResearchTrainingPlan">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:ResourceSharingPlan">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 16. Authentication of Key Biological and/or Chemical Resources -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												16. Authentication of Key Biological and/or Chemical Resources
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:OtherResearchTrainingPlan">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:KeyBiologicalAndOrChemicalResources">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>

									<!-- Additional Information Section START -->
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" padding="6px" 
											padding-bottom="0px" display-align="center"
											border-top="1px solid black">
											<fo:block font-weight="bold" font-size="10pt">
												Additional Information Section
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 17. Human Embryonic Stem Cells -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before"
											font-weight="bold" text-decoration="underline" 
											number-columns-spanned="2" padding-left="10px">
											<fo:block>
												17. Human Embryonic Stem Cells
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before" padding-left="10px">
											<fo:block>
												* Does the proposed project involve human embryonic stem cells?
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:StemCells">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:isHumanStemCellsInvolved">
																<xsl:choose>
																	<xsl:when test="string(.)='Y: Yes'">
																		<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																			<xsl:text>&#x2714;</xsl:text>
																		</fo:inline>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:inline border="solid 1pt black">
																			<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																		</fo:inline>
																	</xsl:otherwise>
																</xsl:choose>
																<fo:inline>
																	<xsl:text> Yes&#160;&#160;&#160;&#160;&#160;&#160; </xsl:text>
																</fo:inline>
																<xsl:choose>
																	<xsl:when test="string(.)='N: No'">
																		<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																			<xsl:text>&#x2714;</xsl:text>
																		</fo:inline>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:inline border="solid 1pt black">
																			<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																		</fo:inline>
																	</xsl:otherwise>
																</xsl:choose>
																<fo:inline>
																	<xsl:text> No</xsl:text>
																</fo:inline>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									<!-- Stem Cell Lines -->
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" 
											padding-left="15px" padding-right="15px" 
											padding-top="0px" padding-bottom="5px">
											<fo:block font-size="9pt">
												If the proposed project involves human embryonic stem cells, list
												below the registration number of the specific cell line(s) from
												the following list:
												<fo:inline text-decoration="underline" color="blue">https://stemcells.nih.gov/research/registry/</fo:inline>.
												Or, if a specific stem cell line cannot be referenced at this time,
												please check the box indicating that one from the registry will
												be used:
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" display-align="center" text-align="center" 
											padding-top="6px" padding-bottom="12px">
	                                        <fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:StemCells">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:StemCellsIndicator">
																<xsl:choose>
																	<xsl:when test="string(.)='Y: Yes'">
																		<fo:inline font-family="ZapfDingbats" font-size="9pt" border="solid 1pt black">
																			<xsl:text>&#x2714;</xsl:text>
																		</fo:inline>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:inline border="solid 1pt black">
																			<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																		</fo:inline>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:for-each>
															<!-- Also display a blank box if section missing -->
															<xsl:if test="not(//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0/PHS_Fellowship_Supplemental_4_0:AdditionalInformation/PHS_Fellowship_Supplemental_4_0:StemCells/PHS_Fellowship_Supplemental_4_0:StemCellsIndicator)">
																<fo:inline border="solid 1pt black">
																	<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																</fo:inline>
															</xsl:if>	
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
												<fo:inline>
													<xsl:text>&#160;&#160; Specific stem cell line cannot be referenced at this time. One from the registry will be used.</xsl:text>
												</fo:inline>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="6" padding-left="15px">
											<fo:block font-size="9pt">Cell Line(s):</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" height="100%" 
											padding-left="30px" padding-right="30px"
											padding-top="10px" padding-bottom="10px">
											<fo:block>
												<xsl:choose>
													<xsl:when 
														test="count(//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0/PHS_Fellowship_Supplemental_4_0:AdditionalInformation/PHS_Fellowship_Supplemental_4_0:StemCells/PHS_Fellowship_Supplemental_4_0:CellLines) > 0">
														<fo:table>
															<fo:table-body>
																<xsl:for-each
																	select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0/PHS_Fellowship_Supplemental_4_0:AdditionalInformation/PHS_Fellowship_Supplemental_4_0:StemCells/PHS_Fellowship_Supplemental_4_0:CellLines">
																	<xsl:call-template name="stemCells">
																		<xsl:with-param name="index" select="position()" />
																		<xsl:with-param name="value" select="." />
																	</xsl:call-template>
																</xsl:for-each>
															</fo:table-body>
														</fo:table>
													</xsl:when>
												</xsl:choose>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 18. Alternate Phone Number -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												18. Alternate Phone Number:
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
	                                            <xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
	                                                <xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
	                                                    <xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AlernatePhoneNumber">
	                                                        <xsl:variable name="value-of-template">
	                                                            <xsl:apply-templates/>
	                                                        </xsl:variable>
	                                                        <xsl:choose>
	                                                            <xsl:when test="contains(string($value-of-template),'&#x2029;')">
	                                                                <fo:block>
	                                                                    <xsl:copy-of select="$value-of-template"/>
	                                                                </fo:block>
	                                                            </xsl:when>
	                                                            <xsl:otherwise>
	                                                                <fo:inline>
	                                                                    <xsl:copy-of select="$value-of-template"/>
	                                                                </fo:inline>
	                                                            </xsl:otherwise>
	                                                        </xsl:choose>
	                                                    </xsl:for-each>
	                                                </xsl:for-each>
	                                            </xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- 19. Degree Sought During Proposed Award -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before" 
											number-columns-spanned="2" padding-bottom="0px">
											<fo:block>
												19. Degree Sought During Proposed Award:
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
																	
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2">
											<fo:block margin-left="10px">
												<fo:table width="90%">
													<fo:table-column column-width="50%"/>
													<fo:table-column column-width="25%"/>
													<fo:table-column column-width="25%"/>
													<fo:table-body>
														<fo:table-row>
															<fo:table-cell display-align="after">
																<fo:block>
																	Degree:
																</fo:block>
															</fo:table-cell>
															<fo:table-cell display-align="after">
																<fo:block>
																	If &quot;other&quot;, please indicate degree type:
																</fo:block>
															</fo:table-cell>
															<fo:table-cell display-align="after">
																<fo:block>
																	Expected Completion Date (month/year):
																</fo:block>
															</fo:table-cell>
														</fo:table-row>																														
														<fo:table-row>
															<fo:table-cell padding-top="4px">		
																<fo:block border-bottom="1px dotted gray">
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
																				<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:GraduateDegreeSought">
																					<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:DegreeType">
																						<xsl:variable name="value-of-template">
																							<xsl:apply-templates/>
																						</xsl:variable>
																						<xsl:choose>
																							<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																								<fo:block>
																									<xsl:copy-of select="$value-of-template"/>
																								</fo:block>
																							</xsl:when>
																							<xsl:otherwise>
																								<fo:block>
																									<xsl:copy-of select="$value-of-template"/><xsl:text>&#160;</xsl:text>
																								</fo:block>
																							</xsl:otherwise>
																						</xsl:choose>
																					</xsl:for-each>
																				</xsl:for-each>
																				<!-- Keep blank space here if section missing -->
																				<xsl:if test="not(//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0/PHS_Fellowship_Supplemental_4_0:AdditionalInformation/PHS_Fellowship_Supplemental_4_0:GraduateDegreeSought/PHS_Fellowship_Supplemental_4_0:DegreeType)">
																					<xsl:text>&#160;</xsl:text>
																					<fo:block></fo:block>
																				</xsl:if>																													
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding-top="4px">
																<fo:block border-bottom="1px dotted gray">
			 						                            	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:GraduateDegreeSought">
																				<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:OtherDegreeTypeText">
																					<xsl:variable name="value-of-template">
																						<xsl:apply-templates/>
																					</xsl:variable>
																					<xsl:choose>
																						<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																							<fo:block>
																								<xsl:copy-of select="$value-of-template"/>
																							</fo:block>
																						</xsl:when>
																						<xsl:otherwise>
																							<fo:block>
																								<xsl:copy-of select="$value-of-template"/><xsl:text>&#160;</xsl:text>
																							</fo:block>
																						</xsl:otherwise>
																					</xsl:choose>
																				</xsl:for-each>
																			</xsl:for-each>
																			<!-- Keep blank space here if section missing -->
																			<xsl:if test="not(//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0/PHS_Fellowship_Supplemental_4_0:AdditionalInformation/PHS_Fellowship_Supplemental_4_0:GraduateDegreeSought/PHS_Fellowship_Supplemental_4_0:OtherDegreeTypeText)">
																				<xsl:text>&#160;</xsl:text>
																				<fo:block></fo:block>
																			</xsl:if>
																		</xsl:for-each>
																	</xsl:for-each>													
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding-top="4px">
																<fo:block border-bottom="1px dotted gray">												
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:GraduateDegreeSought">
																				<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:DegreeDate">
																					<xsl:variable name="value-of-template">
																						<xsl:apply-templates/>
																					</xsl:variable>
																					<xsl:choose>
																						<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																							<fo:block>
																								<xsl:copy-of select="$value-of-template"/>
																							</fo:block>
																						</xsl:when>
																						<xsl:otherwise>
																							<fo:block>
																								<xsl:copy-of select="$value-of-template"/><xsl:text>&#160;</xsl:text>
																							</fo:block>
																						</xsl:otherwise>
																					</xsl:choose>
																				</xsl:for-each>
																			</xsl:for-each>
																			<!-- Keep blank space here if section missing -->
																			<xsl:if test="not(//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0/PHS_Fellowship_Supplemental_4_0:AdditionalInformation/PHS_Fellowship_Supplemental_4_0:GraduateDegreeSought/PHS_Fellowship_Supplemental_4_0:DegreeDate)">
																				<xsl:text>&#160;</xsl:text>
																				<fo:block></fo:block>
																			</xsl:if>																													
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
													</fo:table-body>
												</fo:table>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>

									<!-- 20. Field of Training for Current Proposal -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before" 
											number-columns-spanned="2" padding-top="10px" padding-bottom="0px">
											<fo:block>
												20. * Field of Training for Current Proposal:
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="center" number-columns-spanned="2"
											padding-left="30px" padding-right="20px">
											<fo:block-container border="1px solid black" padding="4px">
												<fo:block>
													<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:FieldOfTraining">
																<xsl:variable name="value-of-template">
																	<xsl:apply-templates/>
																</xsl:variable>
																<xsl:choose>
																	<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																		<fo:block>
																			<xsl:copy-of select="$value-of-template"/>
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:inline>
																			<xsl:copy-of select="$value-of-template"/>
																		</fo:inline>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</fo:block>
											</fo:block-container>
										</fo:table-cell>
									</fo:table-row>
         
         							<!-- 21. Current Or Prior Kirschstein-NRSA Support -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before" border-top="1px solid black">
											<fo:block>
												21. * Current Or Prior Kirschstein-NRSA Support?
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before" border-top="1px solid black">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:CurrentPriorNRSASupportIndicator">
															<xsl:choose>
																<xsl:when test="string(.)='Y: Yes'">
																	<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																		<xsl:text>&#x2714;</xsl:text>
																	</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline border="solid 1pt black">
																		<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
															<fo:inline>
																<xsl:text> Yes&#160;&#160;&#160;&#160;&#160;&#160; </xsl:text>
															</fo:inline>
															<xsl:choose>
																<xsl:when test="string(.)='N: No'">
																	<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																		<xsl:text>&#x2714;</xsl:text>
																	</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline border="solid 1pt black">
																		<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
															<fo:inline>
																<xsl:text> No</xsl:text>
															</fo:inline>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before" number-columns-spanned="2"
											padding-left="20px">
											<fo:block font-style="italic">
												If yes, please identify current and prior Kirschstein-NRSA support below:
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
														
									<!-- Kirschstein support table START -->
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" 
											display-align="center" text-align="center">
											<fo:block>
												<fo:table table-layout="fixed" width="90%" 
													margin-left="20px" margin-right="20px">
													<fo:table-column column-width="18%"/>
													<fo:table-column column-width="20%"/>
													<fo:table-column column-width="18%"/>
													<fo:table-column column-width="18%"/>
													<fo:table-column column-width="26%"/>
													<fo:table-body start-indent="0pt">
														<fo:table-row>
															<fo:table-cell padding="2px" display-align="center">
																<fo:block>
																	Level*
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="2px" display-align="center">
																<fo:block>
																	Type*
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="2px" display-align="center">
																<fo:block>
																	<fo:inline>
																		<xsl:text>Start Date (if known)</xsl:text>
																	</fo:inline>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="2pt" display-align="center">
																<fo:block>
																	<fo:inline>
																		<xsl:text>End Date (if known)</xsl:text>
																	</fo:inline>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="2pt" display-align="center">
																<fo:block>
																	<fo:inline>
																		<xsl:text>Grant Number (if known)</xsl:text>
																	</fo:inline>
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
														<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
																<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:CurrentPriorNRSASupport">
																	<xsl:call-template name="kirschstein">
																		<xsl:with-param name="index" select="position()" />
																	</xsl:call-template>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</fo:table-body>
												</fo:table>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- Kirschstein support table END -->
					
         							<!-- 22. Applications for Concurrent Support -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before" padding-top="10px">
											<fo:block>
												22. * Applications for Concurrent Support?
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before" padding-top="10px">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:ConcurrentSupport">
															<xsl:choose>
																<xsl:when test="string(.)='Y: Yes'">
																	<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																		<xsl:text>&#x2714;</xsl:text>
																	</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline border="solid 1pt black">
																		<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
															<fo:inline>
																<xsl:text> Yes&#160;&#160;&#160;&#160;&#160;&#160; </xsl:text>
															</fo:inline>
															<xsl:choose>
																<xsl:when test="string(.)='N: No'">
																	<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																		<xsl:text>&#x2714;</xsl:text>
																	</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline border="solid 1pt black">
																		<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
															<fo:inline>
																<xsl:text> No</xsl:text>
															</fo:inline>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before" 
											padding-top="0px" padding-left="22px">
											<fo:block font-style="italic">
												If yes, please describe in an attached file:
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before" padding-top="0px">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:ConcurrentSupportDescription">
															<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates/>
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template"/>
																			</fo:inline>
																		</xsl:otherwise>
																	</xsl:choose>
																</xsl:for-each>
															</xsl:for-each>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>	
									
									<!-- 23. Citizenship -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before"	number-columns-spanned="2">
											<fo:block>
												23. * Citizenship
											</fo:block>
										</fo:table-cell>	
									</fo:table-row>			
									<fo:table-row>
										<fo:table-cell padding="2px" padding-left="20px" display-align="before">
											<fo:block>
												<fo:inline font-weight="bold">
													U.S. Citizen
												</fo:inline>
												<fo:inline padding-left="16px" padding-right="0px">
													U.S. Citizen or Non-Citizen National?
												</fo:inline>																	
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="2px" padding-left="6px" display-align="before">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:USCitizen">
															<xsl:choose>
																<xsl:when test="string(.)='Y: Yes'">
																	<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																		<xsl:text>&#x2714;</xsl:text>
																	</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline border="solid 1pt black">
																		<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
															<fo:inline>
																<xsl:text> Yes&#160;&#160;&#160;&#160;&#160;&#160; </xsl:text>
															</fo:inline>
															<xsl:choose>
																<xsl:when test="string(.)='N: No'">
																	<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																		<xsl:text>&#x2714;</xsl:text>
																	</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline border="solid 1pt black">
																		<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
															<fo:inline>
																<xsl:text> No</xsl:text>
															</fo:inline>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell padding="2px" padding-left="20px" display-align="before">
											<fo:block>
												<fo:inline font-weight="bold">
													Non-U.S. Citizen
												</fo:inline>																
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="2px" padding-left="6px" display-align="center">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:NonUSCitizen">
															<xsl:choose>
																<xsl:when test="string(.)='With a Permanent U.S. Resident Visa'">
																	<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																		<xsl:text>&#x2714;</xsl:text>
																	</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline border="solid 1pt black">
																		<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
															<fo:inline>
																With a Permanent U.S. Resident Visa
															</fo:inline>
														</xsl:for-each>
														<!-- Also display a blank box if section missing -->
														<xsl:if test="not(//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0/PHS_Fellowship_Supplemental_4_0:AdditionalInformation/PHS_Fellowship_Supplemental_4_0:NonUSCitizen)">
															<fo:inline border="solid 1pt black">
																<fo:leader leader-length="7.5pt" leader-pattern="space"/>
															</fo:inline>
															<fo:inline>
																With a Permanent U.S. Resident Visa
															</fo:inline>																				
														</xsl:if>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>				
									<fo:table-row>
										<fo:table-cell>
											<fo:block>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="2px" padding-left="6px" display-align="center">
											<fo:block>
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:NonUSCitizen">
															<xsl:choose>
																<xsl:when test="string(.)='With a Temporary U.S. Visa'">
																	<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																		<xsl:text>&#x2714;</xsl:text>
																	</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline border="solid 1pt black">
																		<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
															<fo:inline>
																With a Temporary U.S. Visa
															</fo:inline>
														</xsl:for-each>
														<!-- Also display a blank box if section missing -->
														<xsl:if test="not(//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0/PHS_Fellowship_Supplemental_4_0:AdditionalInformation/PHS_Fellowship_Supplemental_4_0:NonUSCitizen)">
															<fo:inline border="solid 1pt black">
																<fo:leader leader-length="7.5pt" leader-pattern="space"/>
															</fo:inline>
															<fo:inline>
																With a Temporary U.S. Visa
															</fo:inline>																			
														</xsl:if>																				
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>				
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" padding="6px" padding-left="20px"
											padding-right="20px" display-align="center">
											<fo:block>
												<fo:inline>
													If you are a non-U.S. citizen with a temporary visa who has applied for permanent resident status and expect to hold a permanent resident visa by the earliest possible start date of the award, please also check here.
												</fo:inline>																	 
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:PermanentResidentByAwardIndicator">
															<xsl:choose>
																<xsl:when test="string(.)='Y: Yes'">
																	<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																		<xsl:text>&#x2714;</xsl:text>
																	</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline border="solid 1pt black">
																		<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
														</xsl:for-each>
														<!-- Also display a blank box if section missing -->
														<xsl:if test="not(//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0/PHS_Fellowship_Supplemental_4_0:AdditionalInformation/PHS_Fellowship_Supplemental_4_0:PermanentResidentByAwardIndicator)">
															<fo:inline border="solid 1pt black">
																<fo:leader leader-length="7.5pt" leader-pattern="space"/>
															</fo:inline>																				
														</xsl:if>																																								
													</xsl:for-each>
												</xsl:for-each>																		
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									<!-- 24. Change of Sponsoring Institution -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<fo:inline>
													24.&#160;
												</fo:inline>
				
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:ChangeOfInstitution">
															<xsl:choose>
																<xsl:when test="string(.)='Y: Yes'">
																	<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																		<xsl:text>&#x2714;</xsl:text>
																	</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline border="solid 1pt black">
																		<fo:leader leader-length="10pt" leader-pattern="space"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
												<fo:inline>
													Change of Sponsoring Institution
												</fo:inline>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												Name of Former Institution:
											</fo:block>
											<fo:block border="1px solid black" padding="4px" 
												margin-left="0px" margin-right="15px">
												<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
													<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
														<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:FormerInstitution">
															<xsl:variable name="value-of-template">
																<xsl:apply-templates/>
															</xsl:variable>
															<xsl:choose>
																<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																	<fo:block>
																		<xsl:copy-of select="$value-of-template"/>
																	</fo:block>
																</xsl:when>
																<xsl:otherwise>
																	<fo:inline>
																		<xsl:copy-of select="$value-of-template"/>
																	</fo:inline>
																</xsl:otherwise>
															</xsl:choose>
														</xsl:for-each>
													</xsl:for-each>
												</xsl:for-each>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									<!-- spacer row -->
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" 
											border-bottom="1px solid black">
											<fo:block break-after="page">
												<xsl:text>&#xA;</xsl:text>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>	
									
									<!-- Budget Section START -->
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" padding="6px" 
											padding-bottom="0px" display-align="center"
											border-top="1px solid black">
											<fo:block font-weight="bold" font-size="10pt">
												Budget Section
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<!-- All Fellowship Applicants -->
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before"
											text-decoration="underline" font-style="italic"
											number-columns-spanned="2" padding-left="20px">
											<fo:block>
												All Fellowship Applicants:
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" padding-left="20px" padding-right="20px">
											<fo:block>
												<!-- Tuition and Fees table START -->
												<fo:table font-size="8pt">
													<fo:table-column column-width="1.5in" />
													<fo:table-column column-width="1.0in" />
													<fo:table-column column-width="2.2in" />
													<fo:table-column column-width="1.0in" />
													<fo:table-column column-width="0.5in" />
													<fo:table-column column-width="0.8in" />																																																				
													<fo:table-body>
														<fo:table-row>
															<fo:table-cell display-align="before">
																<fo:block>
																	25. * Tuition and Fees:
																</fo:block>
															</fo:table-cell>
															<fo:table-cell number-columns-spanned="3" display-align="before">
																<fo:block margin-left="40px">
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:TuitionAndFeesRequested">
																				<xsl:choose>
																					<xsl:when test="string(.)='N: No'">
																						<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																							<xsl:text>&#x2714;</xsl:text>
																						</fo:inline>
																					</xsl:when>
																					<xsl:otherwise>
																						<fo:inline border="solid 1pt black">
																							<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																						</fo:inline>
																					</xsl:otherwise>
																				</xsl:choose>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																	<fo:inline font-size="8pt" padding-left="6px">
																		None Requested
																	</fo:inline>
																	<fo:inline padding-left="20px">
																		<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																				<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:TuitionAndFeesRequested">
																					<xsl:choose>
																						<xsl:when test="string(.)='Y: Yes'">
																							<fo:inline border="solid 1pt black" font-family="ZapfDingbats" font-size="9px ">
																								<xsl:text>&#x2714;</xsl:text>
																							</fo:inline>
																						</xsl:when>
																						<xsl:otherwise>
																							<fo:inline border="solid 1pt black">
																								<fo:leader leader-length="7.5pt" leader-pattern="space"/>
																							</fo:inline>
																						</xsl:otherwise>
																					</xsl:choose>
																				</xsl:for-each>
																			</xsl:for-each>
																		</xsl:for-each>
																		<fo:inline font-size="8pt" padding-left="6px">
																			Funds Requested
																		</fo:inline>
																	</fo:inline>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell number-columns-spanned="2">
																<fo:block></fo:block>
															</fo:table-cell>
														</fo:table-row>
														
														<fo:table-row>
															<fo:table-cell number-columns-spanned="2">
																<fo:block></fo:block>
															</fo:table-cell>
															<fo:table-cell padding="8px 6px 1px 6px" display-align="center">
																<fo:block>
																	Year 1
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="8px 6px 1px 6px" display-align="center" 
																border-bottom="1px dotted gray" number-columns-spanned="2">
																<fo:block>
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:TuitionRequestedYear1">
																				<fo:inline>
																					<xsl:text>$</xsl:text>
																				</fo:inline>
																				<fo:inline>
																					<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																				</fo:inline>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block></fo:block>
															</fo:table-cell>
														</fo:table-row>
														
														<fo:table-row>
															<fo:table-cell number-columns-spanned="2">
																<fo:block></fo:block>
															</fo:table-cell>
															<fo:table-cell padding="3px 6px 1px 6px" display-align="center">
																<fo:block>
																	Year 2
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="3px 6px 1px 6px" display-align="center" 
																border-bottom="1px dotted gray" number-columns-spanned="2">
																<fo:block>
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:TuitionRequestedYear2">
																				<fo:inline>
																					<xsl:text>$</xsl:text>
																				</fo:inline>
																				<fo:inline>
																					<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																				</fo:inline>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block></fo:block>
															</fo:table-cell>
														</fo:table-row>
														
														<fo:table-row>
															<fo:table-cell number-columns-spanned="2">
																<fo:block></fo:block>
															</fo:table-cell>
															<fo:table-cell padding="3px 6px 1px 6px" display-align="center">
																<fo:block>
																	Year 3
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="3px 6px 1px 6px" display-align="center" 
																border-bottom="1px dotted gray" number-columns-spanned="2">
																<fo:block>
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:TuitionRequestedYear3">
																				<fo:inline>
																					<xsl:text>$</xsl:text>
																				</fo:inline>
																				<fo:inline>
																					<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																				</fo:inline>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block></fo:block>
															</fo:table-cell>
														</fo:table-row>														
														
														<fo:table-row>
															<fo:table-cell number-columns-spanned="2">
																<fo:block></fo:block>
															</fo:table-cell>
															<fo:table-cell padding="3px 6px 1px 6px" display-align="center">
																<fo:block>
																	Year 4
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="3px 6px 1px 6px" display-align="center" 
																border-bottom="1px dotted gray" number-columns-spanned="2">
																<fo:block>
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:TuitionRequestedYear4">
																				<fo:inline>
																					<xsl:text>$</xsl:text>
																				</fo:inline>
																				<fo:inline>
																					<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																				</fo:inline>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block></fo:block>
															</fo:table-cell>
														</fo:table-row>
														
														<fo:table-row>
															<fo:table-cell number-columns-spanned="2">
																<fo:block></fo:block>
															</fo:table-cell>
															<fo:table-cell padding="3px 6px 1px 6px" display-align="center">
																<fo:block>
																	Year 5
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="3px 6px 1px 6px" display-align="center" 
																border-bottom="1px dotted gray" number-columns-spanned="2">
																<fo:block>
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:TuitionRequestedYear5">
																				<fo:inline>
																					<xsl:text>$</xsl:text>
																				</fo:inline>
																				<fo:inline>
																					<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																				</fo:inline>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block></fo:block>
															</fo:table-cell>
														</fo:table-row>														
														
														<fo:table-row>
															<fo:table-cell number-columns-spanned="2">
																<fo:block></fo:block>
															</fo:table-cell>
															<fo:table-cell padding="3px 6px 1px 6px" display-align="center">
																<fo:block>
																	Year 6 <fo:inline font-style="italic">(when applicable)</fo:inline>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="3px 6px 1px 6px" display-align="center" 
																border-bottom="1px dotted gray" number-columns-spanned="2">
																<fo:block>
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:TuitionRequestedYear6">
																				<fo:inline>
																					<xsl:text>$</xsl:text>
																				</fo:inline>
																				<fo:inline>
																					<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																				</fo:inline>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block></fo:block>
															</fo:table-cell>
														</fo:table-row>														
														
														<fo:table-row>
															<fo:table-cell number-columns-spanned="2">
																<fo:block></fo:block>
															</fo:table-cell>
															<fo:table-cell padding="8px 6px 1px 6px" display-align="center">
																<fo:block font-weight="bold">
																	Total Funds Requested:
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="8px 6px 1px 6px" display-align="center" 
																border-bottom="1px dotted gray" number-columns-spanned="2">
																<fo:block>
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:TuitionRequestedTotal">
																				<fo:inline>
																					<xsl:text>$</xsl:text>
																				</fo:inline>
																				<fo:inline>
																					<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																				</fo:inline>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block></fo:block>
															</fo:table-cell>
														</fo:table-row>
														<!-- space row -->
														<fo:table-row>
															<fo:table-cell number-columns-spanned="6" height="10px">
																<fo:block></fo:block>
															</fo:table-cell>
														</fo:table-row>															
													</fo:table-body>
												</fo:table>
												<!-- Tuition and Fees table END -->
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									<!-- Senior Fellowship Applicants Only -->
									<fo:table-row>
										<fo:table-cell padding="2px 6px 2px 6px" display-align="before"
											text-decoration="underline" font-style="italic"
											number-columns-spanned="2" padding-left="20px">
											<fo:block>
												Senior Fellowship Applicants Only:
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" padding-left="20px" padding-right="20px">
											<fo:block>
												<!-- Salary and Stipends table START -->
												<fo:table font-size="8pt">
													<fo:table-column column-width="2.8in" />
													<fo:table-column column-width="1.3in" />
													<fo:table-column column-width="1.2in" />
													<fo:table-column column-width="1.2in" />
													<fo:table-column column-width="0.5in" />
													<fo:table-body>
														<fo:table-row>
															<fo:table-cell padding="6px" padding-top="2px" display-align="after">
																<fo:block>
																	26. Present Institutional Base Salary:
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="6px" padding-top="2px" display-align="after">
																<fo:block padding-bottom="2px">
																	Amount
																</fo:block>
																<fo:block border-bottom="1px dotted gray" margin-right="20px" font-size="8pt">
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:InstitutionalBaseSalary">
																				<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Amount">
																					<fo:inline>
																						<xsl:text>$</xsl:text>
																					</fo:inline>
																					<fo:inline>
																						<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																					</fo:inline>
																				</xsl:for-each>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>															
															<fo:table-cell padding="6px" padding-top="2px" display-align="after">
																<fo:block padding-bottom="2px">
																	Academic Period
																</fo:block>
																<fo:block border-bottom="1px dotted gray" margin-right="20px">
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:InstitutionalBaseSalary">
																				<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AcademicPeriod">
																					<xsl:variable name="value-of-template">
																						<xsl:apply-templates/>
																					</xsl:variable>
																					<xsl:choose>
																						<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																							<fo:block>
																								<xsl:copy-of select="$value-of-template"/>
																							</fo:block>
																						</xsl:when>
																						<xsl:otherwise>
																							<fo:inline>
																								<xsl:copy-of select="$value-of-template"/>
																							</fo:inline>
																						</xsl:otherwise>
																					</xsl:choose>
																				</xsl:for-each>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>																	
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="6px" padding-top="2px" display-align="after">
																<fo:block padding-bottom="2px">
																	Number of Months
																</fo:block>
																<fo:block border-bottom="1px dotted gray" margin-right="20px">
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:InstitutionalBaseSalary">
																				<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:NumberOfMonths">
																					<xsl:variable name="value-of-template">
																						<xsl:apply-templates/>
																					</xsl:variable>
																					<xsl:choose>
																						<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																							<fo:block>
																								<xsl:copy-of select="$value-of-template"/>
																							</fo:block>
																						</xsl:when>
																						<xsl:otherwise>
																							<fo:inline>
																								<xsl:copy-of select="$value-of-template"/>
																							</fo:inline>
																						</xsl:otherwise>
																					</xsl:choose>
																				</xsl:for-each>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>																
																</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block></fo:block>
															</fo:table-cell>
														</fo:table-row>
														<fo:table-row>
															<fo:table-cell padding="6px" display-align="after" number-columns-spanned="6">
																<fo:block>
																	27. Stipends/Salary During First Year of Proposal Fellowship:
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
														<fo:table-row>
															<fo:table-cell padding="6px" padding-top="2px" 
																padding-left="16px" display-align="after">
																<fo:block>
																	a. Federal Stipend Requested:
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="6px" padding-top="2px" display-align="after">
																<fo:block padding-bottom="2px">
																	Amount
																</fo:block>
																<fo:block border-bottom="1px dotted gray" margin-right="20px">
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:FederalStipendRequested">
																				<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Amount">
																					<fo:inline>
																						<xsl:text>$</xsl:text>
																					</fo:inline>
																					<fo:inline>
																						<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																					</fo:inline>
																				</xsl:for-each>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="6px" padding-top="2px" display-align="after">	
																<fo:block padding-bottom="2px">
																	Number of Months
																</fo:block>
																<fo:block border-bottom="1px dotted gray" margin-right="20px">
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:FederalStipendRequested">
																				<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:NumberOfMonths">
																					<xsl:variable name="value-of-template">
																						<xsl:apply-templates/>
																					</xsl:variable>
																					<xsl:choose>
																						<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																							<fo:block>
																								<xsl:copy-of select="$value-of-template"/>
																							</fo:block>
																						</xsl:when>
																						<xsl:otherwise>
																							<fo:inline>
																								<xsl:copy-of select="$value-of-template"/>
																							</fo:inline>
																						</xsl:otherwise>
																					</xsl:choose>
																				</xsl:for-each>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
														<fo:table-row>
															<fo:table-cell padding="6px" padding-top="2px" 
																padding-left="16px" display-align="after">
																<fo:block>
																	b. Supplementation from other sources:
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="6px" padding-top="2px" display-align="after">
																<fo:block padding-bottom="2px">
																	Amount
																</fo:block>
																<fo:block border-bottom="1px dotted gray" margin-right="20px">
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:SupplementationFromOtherSources">
																				<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Amount">
																					<fo:inline>
																						<xsl:text>$</xsl:text>
																					</fo:inline>
																					<fo:inline>
																						<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																					</fo:inline>
																				</xsl:for-each>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding="6px" padding-top="2px" display-align="after">	
																<fo:block padding-bottom="2px">
																	Number of Months
																</fo:block>
																<fo:block border-bottom="1px dotted gray" margin-right="20px">
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:SupplementationFromOtherSources">
																				<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:NumberOfMonths">
																					<xsl:variable name="value-of-template">
																						<xsl:apply-templates/>
																					</xsl:variable>
																					<xsl:choose>
																						<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																							<fo:block>
																								<xsl:copy-of select="$value-of-template"/>
																							</fo:block>
																						</xsl:when>
																						<xsl:otherwise>
																							<fo:inline>
																								<xsl:copy-of select="$value-of-template"/>
																							</fo:inline>
																						</xsl:otherwise>
																					</xsl:choose>
																				</xsl:for-each>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
														<fo:table-row>
															<fo:table-cell>
																<fo:block></fo:block>
															</fo:table-cell>
															<fo:table-cell padding="6px" padding-top="2px" display-align="after"
																number-columns-spanned="3">
																<fo:block padding-bottom="2px">
																	Type (sabbatical leave, salary, etc.)
																</fo:block>
																<fo:block border-bottom="1px dotted gray" margin-right="20px">
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:SupplementationFromOtherSources">
																				<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Type">
																					<xsl:variable name="value-of-template">
																						<xsl:apply-templates/>
																					</xsl:variable>
																					<xsl:choose>
																						<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																							<fo:block>
																								<xsl:copy-of select="$value-of-template"/>
																							</fo:block>
																						</xsl:when>
																						<xsl:otherwise>
																							<fo:inline>
																								<xsl:copy-of select="$value-of-template"/>
																							</fo:inline>
																						</xsl:otherwise>
																					</xsl:choose>
																				</xsl:for-each>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block></fo:block>
															</fo:table-cell>
														</fo:table-row>
														<fo:table-row>
															<fo:table-cell>
																<fo:block></fo:block>
															</fo:table-cell>
															<fo:table-cell padding="6px" padding-top="2px" display-align="after"
																number-columns-spanned="3">
																<fo:block padding-bottom="2px">
																	Source
																</fo:block>
																<fo:block border-bottom="1px dotted gray" margin-right="20px">
																	<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																		<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Budget">
																			<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:SupplementationFromOtherSources">
																				<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Source">
																					<xsl:variable name="value-of-template">
																						<xsl:apply-templates/>
																					</xsl:variable>
																					<xsl:choose>
																						<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																							<fo:block>
																								<xsl:copy-of select="$value-of-template"/>
																							</fo:block>
																						</xsl:when>
																						<xsl:otherwise>
																							<fo:inline>
																								<xsl:copy-of select="$value-of-template"/>
																							</fo:inline>
																						</xsl:otherwise>
																					</xsl:choose>
																				</xsl:for-each>
																			</xsl:for-each>
																		</xsl:for-each>
																	</xsl:for-each>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell>
																<fo:block></fo:block>
															</fo:table-cell>
														</fo:table-row>
													</fo:table-body>
												</fo:table>
												<!-- Salary and Stipends table END -->
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									<!-- Appendix START -->
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" padding="6px" 
											padding-bottom="0px" display-align="center" 
											border-top="1px solid black">
											<fo:block font-weight="bold" font-size="10pt">
												Appendix
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												28.&#160;Appendix
											</fo:block>
										</fo:table-cell>
										<fo:table-cell padding="6px" display-align="before">
											<fo:block>
												<xsl:if test="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0/PHS_Fellowship_Supplemental_4_0:Appendix/att:AttachedFile">
													<fo:table table-layout="fixed" width="100%">
														<fo:table-column column-width="proportional-column-width(1)"/>
														<fo:table-body>
															<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
																<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Appendix">
																	<xsl:for-each select="att:AttachedFile">
																		<fo:table-row>
																			<fo:table-cell display-align="center">
																				<fo:block>
																					<xsl:for-each select="att:FileName"> 
																						<xsl:variable name="value-of-template">
																							<xsl:apply-templates/>
																						</xsl:variable>
																						<xsl:choose>
																							<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																								<fo:block>
																									<xsl:copy-of select="$value-of-template"/>
																								</fo:block>
																							</xsl:when>
																							<xsl:otherwise>
																								<fo:inline>
																									<xsl:copy-of select="$value-of-template"/>
																								</fo:inline>
																							</xsl:otherwise>
																						</xsl:choose>
																					</xsl:for-each>
																				</fo:block>
																			</fo:table-cell>
																		</fo:table-row>
																	</xsl:for-each>
																</xsl:for-each>
															</xsl:for-each>
														</fo:table-body>
													</fo:table>
												</xsl:if>
											</fo:block>
										</fo:table-cell>
																			    

									</fo:table-row>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2" padding="2pt" display-align="center">
											<fo:block></fo:block>
										</fo:table-cell>
									</fo:table-row>

									
								</fo:table-body>
							</fo:table>
							
						</xsl:for-each>
					</fo:block>
					<fo:block id="SV_RefID_PageTotal"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
	<xsl:template name="headerall">
		<fo:static-content flow-name="xsl-region-before">
			<fo:block>
				<fo:table table-layout="fixed" width="100%">
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-body start-indent="0pt">
						<fo:table-row>
							<fo:table-cell height="15px" number-columns-spanned="2" 
								padding="2px" display-align="center">
								<fo:block/>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell number-columns-spanned="2" text-align="center" display-align="before">
								<fo:block>
									<fo:inline font-weight="bold" font-size="12pt">
										<xsl:text>PHS Fellowship Supplemental Form</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell number-columns-spanned="2" text-align="right" display-align="before">
								<fo:block text-align="end" font-size="6px">
									OMB Number: 0925-0001
								</fo:block>
								<fo:block text-align="end" font-size="6px">
									Expiration Date: 03/31/2020
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:block>
		</fo:static-content>
	</xsl:template>
	
	<xsl:template name="double-backslash">
		<xsl:param name="text"/>
		<xsl:param name="text-length"/>
		<xsl:variable name="text-after-bs" select="substring-after($text, '\')"/>
		<xsl:variable name="text-after-bs-length" select="string-length($text-after-bs)"/>
		<xsl:choose>
			<xsl:when test="$text-after-bs-length = 0">
				<xsl:choose>
					<xsl:when test="substring($text, $text-length) = '\'">
						<xsl:value-of select="concat(substring($text,1,$text-length - 1), '\\')"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$text"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat(substring($text,1,$text-length - $text-after-bs-length - 1), '\\')"/>
				<xsl:call-template name="double-backslash">
					<xsl:with-param name="text" select="$text-after-bs"/>
					<xsl:with-param name="text-length" select="$text-after-bs-length"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="stemCells">
		<xsl:param name="index" />
		<xsl:param name="value" />
		<xsl:variable name="colCount" select="10" />

		<fo:table-cell width="0.6in">
			<xsl:if test="(floor (($index - 1) mod $colCount)) = 0">
				<xsl:attribute name="starts-row">true</xsl:attribute>
			</xsl:if>
			<fo:block border="1px solid black" height="12px"
				font-size="9pt" display-align="center" text-align="center" 
				padding-top="1.5px" margin="2px">
				<xsl:value-of select="$value" />
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	
	<xsl:template name="kirschstein">
		<xsl:param name="index" />

		<fo:table-row>
			<fo:table-cell border-bottom="dotted 1pt gray" padding="2px" padding-left="10px" display-align="center">
				<fo:block text-align="left">
					<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
						<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
							<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:CurrentPriorNRSASupport">
								<xsl:if test="position() = $index">
									<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Level">
										<xsl:variable name="value-of-template">
											<xsl:apply-templates/>
										</xsl:variable>
										<xsl:choose>
											<xsl:when test="contains(string($value-of-template),'&#x2029;')">
												<fo:block>
													<xsl:copy-of select="$value-of-template"/>
												</fo:block>
											</xsl:when>
											<xsl:otherwise>
												<fo:inline>
													<xsl:copy-of select="$value-of-template"/>
												</fo:inline>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-bottom="dotted 1pt gray" padding="2px" padding-left="10px" display-align="center">
				<fo:block text-align="left">
					<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
						<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
							<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:CurrentPriorNRSASupport">
								<xsl:if test="position() = $index">
									<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:Type">
										<xsl:variable name="value-of-template">
											<xsl:apply-templates/>
										</xsl:variable>
										<xsl:choose>
											<xsl:when test="contains(string($value-of-template),'&#x2029;')">
												<fo:block>
													<xsl:copy-of select="$value-of-template"/>
												</fo:block>
											</xsl:when>
											<xsl:otherwise>
												<fo:inline>
													<xsl:copy-of select="$value-of-template"/>
												</fo:inline>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-bottom="dotted 1pt gray" padding="2px" display-align="center" text-align="center">
				<fo:block>
					<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
						<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
							<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:CurrentPriorNRSASupport">
								<xsl:if test="position() = $index">
									<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:StartDate">
										<fo:inline>
											<xsl:value-of select="format-number(number(substring(string(string(.)), 6, 2)), '00')"/>
											<xsl:text>/</xsl:text>
											<xsl:value-of select="format-number(number(substring(string(string(.)), 9, 2)), '00')"/>
											<xsl:text>/</xsl:text>
											<xsl:value-of select="format-number(number(substring(string(string(string(.))), 1, 4)), '0000')"/>
										</fo:inline>
									</xsl:for-each>
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-bottom="dotted 1pt gray" padding="2px" display-align="center" text-align="center">
				<fo:block>
					<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
						<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
							<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:CurrentPriorNRSASupport">
								<xsl:if test="position() = $index">
									<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:EndDate">
										<fo:inline>
											<xsl:value-of select="format-number(number(substring(string(string(.)), 6, 2)), '00')"/>
											<xsl:text>/</xsl:text>
											<xsl:value-of select="format-number(number(substring(string(string(.)), 9, 2)), '00')"/>
											<xsl:text>/</xsl:text>
											<xsl:value-of select="format-number(number(substring(string(string(string(.))), 1, 4)), '0000')"/>
										</fo:inline>
									</xsl:for-each>
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-bottom="dotted 1pt gray" padding="2px" padding-left="10px" display-align="center">
				<fo:block text-align="left">
					<xsl:for-each select="//PHS_Fellowship_Supplemental_4_0:PHS_Fellowship_Supplemental_4_0">
						<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:AdditionalInformation">
							<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:CurrentPriorNRSASupport">
								<xsl:if test="position() = $index">
									<xsl:for-each select="PHS_Fellowship_Supplemental_4_0:GrantNumber">
										<xsl:variable name="value-of-template">
											<xsl:apply-templates/>
										</xsl:variable>
										<xsl:choose>
											<xsl:when test="contains(string($value-of-template),'&#x2029;')">
												<fo:block>
													<xsl:copy-of select="$value-of-template"/>
												</fo:block>
											</xsl:when>
											<xsl:otherwise>
												<fo:inline>
													<xsl:copy-of select="$value-of-template"/>
												</fo:inline>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
																			
	</xsl:template>
	
</xsl:stylesheet>