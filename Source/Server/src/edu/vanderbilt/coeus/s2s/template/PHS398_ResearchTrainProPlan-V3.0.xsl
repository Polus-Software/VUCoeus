<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:PHS398_ResearchTrainingProgramPlan_3_0="http://apply.grants.gov/forms/PHS398_ResearchTrainingProgramPlan_3_0-V3.0"
	xmlns:att="http://apply.grants.gov/system/Attachments-V1.0"
	xmlns:codes="http://apply.grants.gov/system/UniversalCodes-V2.0"
	xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:glob="http://apply.grants.gov/system/Global-V1.0"
	xmlns:globLib="http://apply.grants.gov/system/GlobalLibrary-V2.0"
	xmlns:xdt="http://www.w3.org/2005/xpath-datatypes" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:header="http://apply.grants.gov/system/Header-V1.0"
	xmlns:footer="http://apply.grants.gov/system/Footer-V1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:output version="1.0" method="xml" encoding="UTF-8" indent="no"/>
    <xsl:param name="SV_OutputFormat" select="'PDF'"/>
    <xsl:variable name="XML" select="/"/>
    <xsl:variable name="fo:layout-master-set">
        <fo:layout-master-set>
            <fo:simple-page-master master-name="default-page" page-height="11in" page-width="8.5in" margin-left="0.6in" margin-right="0.6in">
                <fo:region-body margin-top="0.99in" margin-bottom="0.79in"/>
                <fo:region-before extent="0.79in"/>
                <fo:region-after extent="0.3in"/>
            </fo:simple-page-master>
        </fo:layout-master-set>
    </xsl:variable>
    
    <xsl:template match="/">
        <fo:root>
            <xsl:copy-of select="$fo:layout-master-set"/>
            <fo:page-sequence master-reference="default-page" initial-page-number="1" format="1">
                <xsl:call-template name="headerall"/>
         		<!-- JM do not want to display footer
                <xsl:call-template name="footer"/>
                -->
                <fo:flow flow-name="xsl-region-body">
                    <fo:block>
                    	<xsl:for-each select="$XML">
                        	<fo:inline-container>
                            	<fo:block>
                                	<xsl:text>&#x2029;</xsl:text>
                                </fo:block>
                            </fo:inline-container>
                            <fo:table font-size="9px" table-layout="fixed" 
                            	width="100%" border="solid 1pt black" 
                            	margin="5">
	                            <fo:table-column column-width="40%"/>
	                            <fo:table-column column-width="60%"/>
	                            <fo:table-body start-indent="0pt">
	                            
	                            	<!-- Introduction -->
									<fo:table-row>
										<fo:table-cell font-size="10pt" padding="7" display-align="before"
											font-weight="bold" number-columns-spanned="2">
					                         <fo:block>
					                         	Introduction
					                         </fo:block>
					                     </fo:table-cell>
					                 </fo:table-row>
					
									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												1. Introduction to Application
												<fo:block> </fo:block>
												&#160;&#160;&#160;&#160;(for Resubmission and Revision)
											</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:for-each
													select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
													<xsl:for-each
														select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
														<xsl:for-each
															select="PHS398_ResearchTrainingProgramPlan_3_0:IntroductionToApplication">
															<xsl:for-each
																select="PHS398_ResearchTrainingProgramPlan_3_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates />
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when
																			test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template" />
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template" />
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

									<!-- Training Program Section -->
									<fo:table-row>
										<fo:table-cell font-size="10pt" padding="7" display-align="before"
											font-weight="bold" number-columns-spanned="2"
											border-top="solid 1pt black">
                                 			<fo:block>
												Training Program Section
	                                        </fo:block>
	                                    </fo:table-cell>
	                                </fo:table-row>

									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
											2. * Program Plan
											</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:for-each
													select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
													<xsl:for-each
														select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
														<xsl:for-each
															select="PHS398_ResearchTrainingProgramPlan_3_0:ProgramPlan">
															<xsl:for-each
																select="PHS398_ResearchTrainingProgramPlan_3_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates />
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when
																			test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template" />
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template" />
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
									
									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												3. Plan for Instruction in the
												<fo:block> </fo:block>
												&#160;&#160;&#160;&#160;Responsible Conduct of Research
											</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:for-each
													select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
													<xsl:for-each
														select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
														<xsl:for-each
															select="PHS398_ResearchTrainingProgramPlan_3_0:ResponsibleConductOfResearch">
															<xsl:for-each
																select="PHS398_ResearchTrainingProgramPlan_3_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates />
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when
																			test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template" />
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template" />
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
									
									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												4. Plan for Instruction in Methods
												<fo:block> </fo:block>
												&#160;&#160;&#160;&#160;for Enhancing Reproducibility
											</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:for-each
													select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
													<xsl:for-each
														select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
														<xsl:for-each
															select="PHS398_ResearchTrainingProgramPlan_3_0:MethodsForEnhancingReproducibility">
															<xsl:for-each
																select="PHS398_ResearchTrainingProgramPlan_3_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates />
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when
																			test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template" />
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template" />
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
				
									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												5. Multiple PD/PI Leadership Plan
												<fo:block> </fo:block>
												&#160;&#160;&#160;&#160;(if applicable)
											</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:for-each
													select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
													<xsl:for-each
														select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
														<xsl:for-each
															select="PHS398_ResearchTrainingProgramPlan_3_0:MultiplePDPILeadershipPlan">
															<xsl:for-each
																select="PHS398_ResearchTrainingProgramPlan_3_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates />
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when
																			test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template" />
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template" />
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
									
									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												6. Progress Report (for RENEWAL
												<fo:block> </fo:block>
												&#160;&#160;&#160;&#160;applications only)							
											</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:for-each
													select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
													<xsl:for-each
														select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
														<xsl:for-each
															select="PHS398_ResearchTrainingProgramPlan_3_0:ProgressReport">
															<xsl:for-each
																select="PHS398_ResearchTrainingProgramPlan_3_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates />
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when
																			test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template" />
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template" />
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
					
									<!-- Faculty, Trainees and Training Record Section -->
									<fo:table-row>
										<fo:table-cell font-size="10pt" padding="7" display-align="before"
											font-weight="bold" number-columns-spanned="2"
											border-top="solid 1pt black">
											<fo:block>
												Faculty, Trainees and Training Record Section
		                                  	</fo:block>
		                              	</fo:table-cell>
		                          	</fo:table-row>
					
									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												7. Participating Faculty Biosketches
											</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:for-each
													select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
													<xsl:for-each
														select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
														<xsl:for-each
															select="PHS398_ResearchTrainingProgramPlan_3_0:ParticipatingFacultyBiosketches">
															<xsl:for-each
																select="PHS398_ResearchTrainingProgramPlan_3_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates />
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when
																			test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template" />
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template" />
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
					
									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												8. Letters of Support
											</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:for-each
													select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
													<xsl:for-each
														select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
														<xsl:for-each
															select="PHS398_ResearchTrainingProgramPlan_3_0:LettersOfSupport">
															<xsl:for-each
																select="PHS398_ResearchTrainingProgramPlan_3_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates />
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when
																			test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template" />
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template" />
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
					
									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												9. Data Tables
											</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:for-each
													select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
													<xsl:for-each
														select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
														<xsl:for-each
															select="PHS398_ResearchTrainingProgramPlan_3_0:DataTables">
															<xsl:for-each
																select="PHS398_ResearchTrainingProgramPlan_3_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates />
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when
																			test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template" />
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template" />
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
					
									<!-- Other Training Program Section -->
									<fo:table-row>
										<fo:table-cell font-size="10pt" padding="7" display-align="before"
											font-weight="bold" number-columns-spanned="2"
											border-top="solid 1pt black">
											<fo:block>
                                            	Other Training Program Section
		                                  	</fo:block>
		                              	</fo:table-cell>
		                          	</fo:table-row>
					
									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												10. Human Subjects
											</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:for-each
													select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
													<xsl:for-each
														select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
														<xsl:for-each
															select="PHS398_ResearchTrainingProgramPlan_3_0:HumanSubjects">
															<xsl:for-each
																select="PHS398_ResearchTrainingProgramPlan_3_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates />
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when
																			test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template" />
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template" />
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
									
									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
										<fo:block>
											11. Data Safety Monitoring Plan
										</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:for-each
													select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
													<xsl:for-each
														select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
														<xsl:for-each
															select="PHS398_ResearchTrainingProgramPlan_3_0:DataSafetyMonitoringPlan">
															<xsl:for-each
																select="PHS398_ResearchTrainingProgramPlan_3_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates />
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when
																			test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template" />
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template" />
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
									
									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												12. Vertebrate Animals
											</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:for-each
													select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
													<xsl:for-each
														select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
														<xsl:for-each
															select="PHS398_ResearchTrainingProgramPlan_3_0:VertebrateAnimals">
															<xsl:for-each
																select="PHS398_ResearchTrainingProgramPlan_3_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates />
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when
																			test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template" />
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template" />
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
					
									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block  font-size="9px">
												13. Select Agent Research
											</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:for-each
													select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
													<xsl:for-each
														select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
														<xsl:for-each
															select="PHS398_ResearchTrainingProgramPlan_3_0:SelectAgentResearch">
															<xsl:for-each
																select="PHS398_ResearchTrainingProgramPlan_3_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates />
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when
																			test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template" />
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template" />
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
					
									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												14. Consortium/Contractual
												Arrangements
											</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:for-each
													select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
													<xsl:for-each
														select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
														<xsl:for-each
															select="PHS398_ResearchTrainingProgramPlan_3_0:ConsortiumContractualArrangements">
															<xsl:for-each
																select="PHS398_ResearchTrainingProgramPlan_3_0:attFile">
																<xsl:for-each select="att:FileName">
																	<xsl:variable name="value-of-template">
																		<xsl:apply-templates />
																	</xsl:variable>
																	<xsl:choose>
																		<xsl:when
																			test="contains(string($value-of-template),'&#x2029;')">
																			<fo:block>
																				<xsl:copy-of select="$value-of-template" />
																			</fo:block>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:inline>
																				<xsl:copy-of select="$value-of-template" />
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

									<!-- Appendix -->
									<fo:table-row>
										<fo:table-cell font-size="10pt" padding="7" display-align="before"
											font-weight="bold" number-columns-spanned="2"
											border-top="solid 1pt black">
                                      		<fo:block>
												Appendix
	                                        </fo:block>
	                                    </fo:table-cell>
	                                </fo:table-row>

									<fo:table-row>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												15. Appendix
											</fo:block>
										</fo:table-cell>
										<fo:table-cell font-size="9pt" padding="7" display-align="before">
											<fo:block>
												<xsl:if test="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0/PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments/PHS398_ResearchTrainingProgramPlan_3_0:Appendix/att:AttachedFile">
													<fo:table table-layout="fixed" width="100%" border-spacing="2pt">
														<fo:table-column column-width="proportional-column-width(1)" />
														<fo:table-body start-indent="0pt">
															<xsl:for-each select="//PHS398_ResearchTrainingProgramPlan_3_0:PHS398_ResearchTrainingProgramPlan_3_0">
																<xsl:for-each select="PHS398_ResearchTrainingProgramPlan_3_0:ResearchTrainingProgramPlanAttachments">
																	<xsl:for-each select="PHS398_ResearchTrainingProgramPlan_3_0:Appendix">
																		<xsl:for-each select="att:AttachedFile">
																			<fo:table-row>
																				<fo:table-cell display-align="before">
																					<fo:block>
																						<xsl:for-each select="att:FileName">
																							<xsl:variable name="value-of-template">
																								<xsl:apply-templates/>
																							</xsl:variable>
																							<xsl:choose>
																								<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																									<fo:block>
																										<xsl:copy-of select="$value-of-template" />
																									</fo:block>
																								</xsl:when>
																								<xsl:otherwise>
																									<fo:inline>
																										<xsl:copy-of select="$value-of-template" />
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
															</xsl:for-each>
														</fo:table-body>
													</fo:table>
												</xsl:if>
											</fo:block>
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
				<fo:inline-container>
					<fo:block>
						<xsl:text>&#x2029;</xsl:text>
					</fo:block>
				</fo:inline-container>
				<fo:table 
                  space-before.optimum="0pt"
                  space-after.optimum="0pt"
                  table-layout="fixed" width="100%" border-spacing="2pt">
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-body start-indent="0pt">
						<fo:table-row>
							<fo:table-cell padding="0" number-columns-spanned="2" height="20"
								display-align="center">
								<fo:block/>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row>
							<fo:table-cell padding="0" number-columns-spanned="2" 
								text-align="center" display-align="center">
								<fo:block>
									<fo:inline font-weight="bold" font-size="12px">
										<xsl:text>PHS 398 Research Training Program Plan</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>

						<fo:table-row margin="0">
							<fo:table-cell padding="0" number-columns-spanned="2" 
								text-align="right" display-align="center">
								<fo:block font-size="6px">
									<fo:inline>
										<xsl:text>OMB Number: 0925-0001</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						
						<fo:table-row margin="0">
							<fo:table-cell padding="0" number-columns-spanned="2" 
								text-align="right" display-align="center">
								<fo:block font-size="6px">
									<fo:inline>
										<xsl:text>Expiration Date: 10/31/2018</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:block>
		</fo:static-content>
	</xsl:template>
	
    <xsl:template name="footer">
	    <fo:static-content flow-name="xsl-region-after">
	       <fo:table width="100%"
	          space-before.optimum="0pt"
	          space-after.optimum="0pt"
	          
	          table-layout="fixed">
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
	                      <fo:inline font-size="8px">Tracking Number: <xsl:value-of select="/*/*/footer:Grants_govTrackingNumber"/>
	                      </fo:inline>
	                   </fo:block>
	                </fo:table-cell>
	                <fo:table-cell hyphenate="true" language="en" line-height="10pt"
	                 padding-start="0pt"
	                 padding-end="0pt"
	                 padding-before="1pt"
	                 padding-after="1pt"
	                 display-align="before"
	                 text-align="right"
	                 border-style="solid"
	                 border-width="0pt"
	                 border-color="white">
	                   <fo:block><fo:inline font-size="8px">Funding Opportunity Number: <xsl:value-of select="/*/*/header:OpportunityID"/></fo:inline>
	                             <fo:inline font-size="8px">.       Received Date: <xsl:value-of select="/*/*/footer:ReceivedDateTime"/></fo:inline></fo:block>
	                </fo:table-cell>
	             </fo:table-row>
	          </fo:table-body>
	       </fo:table>
	    </fo:static-content>
    </xsl:template>

	<xsl:template name="double-backslash">
		<xsl:param name="text" />
		<xsl:param name="text-length" />
		<xsl:variable name="text-after-bs" select="substring-after($text, '\')" />
		<xsl:variable name="text-after-bs-length" select="string-length($text-after-bs)" />
		<xsl:choose>
			<xsl:when test="$text-after-bs-length = 0">
				<xsl:choose>
					<xsl:when test="substring($text, $text-length) = '\'">
						<xsl:value-of select="concat(substring($text,1,$text-length - 1), '\\')" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$text" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of
					select="concat(substring($text,1,$text-length - $text-after-bs-length - 1), '\\')" />
				<xsl:call-template name="double-backslash">
					<xsl:with-param name="text" select="$text-after-bs" />
					<xsl:with-param name="text-length" select="$text-after-bs-length" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>