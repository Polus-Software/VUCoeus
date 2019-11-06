<?xml version="1.0" encoding="UTF-8"?>
<!--Designed and generated by Altova StyleVision Enterprise Edition 2008 rel. 2 - see http://www.altova.com/stylevision for more information.-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xdt="http://www.w3.org/2005/xpath-datatypes" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:output version="1.0" method="xml" encoding="UTF-8" indent="no"/>
	<xsl:param name="SV_OutputFormat" select="'PDF'"/>
	<xsl:variable name="XML" select="/"/>
	<xsl:variable name="fo:layout-master-set">
		<fo:layout-master-set>
			<fo:simple-page-master master-name="default-page" page-height="8.5in" page-width="11in" margin-left="0.2in" margin-right="0.65in">
				<fo:region-body margin-top="1.65in" margin-bottom="0.2in"/>
				<fo:region-before extent="1.65in"/>
				<fo:region-after extent="0.2in"/>
			</fo:simple-page-master>
		</fo:layout-master-set>
	</xsl:variable>
	<xsl:template match="/">
		<fo:root>
			<xsl:copy-of select="$fo:layout-master-set"/>
			<fo:page-sequence master-reference="default-page" initial-page-number="1" format="1">
				<xsl:call-template name="headerall"/>
				<xsl:call-template name="footerall"/>
				<fo:flow flow-name="xsl-region-body">
					<fo:block>
						<fo:block/>
						<xsl:for-each select="$XML">
							<xsl:for-each select="AwardNotice">
								<xsl:for-each select="MoneyHistoryReport">
									<fo:block/>
									<fo:inline font-family="Arial" font-size="9pt" font-weight="bold">
										<xsl:text>Award Sequence : </xsl:text>
									</fo:inline>
									<xsl:for-each select="MoneyHistorySeq">
										<xsl:variable name="value-of-template">
											<xsl:apply-templates/>
										</xsl:variable>
										<xsl:choose>
											<xsl:when test="contains(string($value-of-template),'&#x2029;')">
												<fo:block font-family="Arial" font-size="9pt" font-weight="bold">
													<xsl:copy-of select="$value-of-template"/>
												</fo:block>
											</xsl:when>
											<xsl:otherwise>
												<fo:inline font-family="Arial" font-size="9pt" font-weight="bold">
													<xsl:copy-of select="$value-of-template"/>
												</fo:inline>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
									<fo:block/>
									<xsl:for-each select="MoneyHistoryInfo">
										<fo:inline-container>
											<fo:block>
												<xsl:text>&#x2029;</xsl:text>
											</fo:block>
										</fo:inline-container>
										<fo:table table-layout="fixed" width="100%" border-spacing="2pt">
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-body start-indent="0pt">
												<fo:table-row font-family="Arial" font-size="9pt">
													<fo:table-cell padding="2pt" height="20" display-align="center">
														<fo:block>
															<xsl:for-each select="TreeLevel">
																<xsl:variable name="value-of-template">
																	<xsl:apply-templates/>
																</xsl:variable>
																<xsl:choose>
																	<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																		<fo:block padding-bottom="20pt">
																			<xsl:copy-of select="$value-of-template"/>
																		</fo:block>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:inline padding-bottom="20pt">
																			<xsl:copy-of select="$value-of-template"/>
																		</fo:inline>
																	</xsl:otherwise>
																</xsl:choose>
																<fo:inline font-family="Arial" font-size="9pt">
																	<xsl:text>&#160;</xsl:text>
																</fo:inline>
															</xsl:for-each>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding="0" height="20" display-align="center">
														<fo:block>
															<xsl:for-each select="ObligatedChangeDirect">
																<fo:inline>
																	<xsl:text>$</xsl:text>
																</fo:inline>
																<fo:inline>
																	<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																</fo:inline>
															</xsl:for-each>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding="0" height="20" display-align="center">
														<fo:block>
															<xsl:for-each select="ObligatedChangeIndirect">
																<fo:inline>
																	<xsl:text>$</xsl:text>
																</fo:inline>
																<fo:inline>
																	<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																</fo:inline>
															</xsl:for-each>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding="0" height="20" display-align="center">
														<fo:block>
															<xsl:for-each select="ObligatedChange">
																<fo:inline>
																	<xsl:text>$</xsl:text>
																</fo:inline>
																<fo:inline>
																	<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																</fo:inline>
															</xsl:for-each>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding="0" height="20" display-align="center">
														<fo:block>
															<xsl:for-each select="AmtObligatedToDate">
																<fo:inline>
																	<xsl:text>$</xsl:text>
																</fo:inline>
																<fo:inline>
																	<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																</fo:inline>
															</xsl:for-each>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell overflow="hidden" padding="0" padding-left="5pt" height="20" display-align="center">
														<fo:block>
															<xsl:for-each select="ObligatedDistributableAmt">
																<fo:inline>
																	<xsl:text>$</xsl:text>
																</fo:inline>
																<fo:inline>
																	<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																</fo:inline>
															</xsl:for-each>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell overflow="hidden" padding="0" padding-left="10pt" height="20" display-align="center">
														<fo:block>
															<xsl:for-each select="AnticipatedChangeDirect">
																<fo:inline>
																	<xsl:text>$</xsl:text>
																</fo:inline>
																<fo:inline>
																	<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																</fo:inline>
															</xsl:for-each>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding="0" padding-left="5pt" height="20" display-align="center">
														<fo:block>
															<xsl:for-each select="AnticipatedChangeIndirect">
																<fo:inline>
																	<xsl:text>$</xsl:text>
																</fo:inline>
																<fo:inline>
																	<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																</fo:inline>
															</xsl:for-each>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell overflow="hidden" padding="0" padding-left="5pt" height="20" display-align="center">
														<fo:block>
															<xsl:for-each select="AnticipatedChange">
																<fo:inline>
																	<xsl:text>$</xsl:text>
																</fo:inline>
																<fo:inline>
																	<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																</fo:inline>
															</xsl:for-each>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding="0" padding-left="10pt" height="20" display-align="center">
														<fo:block>
															<xsl:for-each select="AnticipatedTotalAmt">
																<fo:inline>
																	<xsl:text>$</xsl:text>
																</fo:inline>
																<fo:inline>
																	<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																</fo:inline>
															</xsl:for-each>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding="0" padding-left="15pt" height="20" display-align="center">
														<fo:block>
															<xsl:for-each select="AnticipatedDistributableAmt">
																<fo:inline>
																	<xsl:text>$</xsl:text>
																</fo:inline>
																<fo:inline>
																	<xsl:value-of select="format-number(number(string(.)), '#,###,###,##0.00')"/>
																</fo:inline>
															</xsl:for-each>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
										<fo:inline-container>
											<fo:block>
												<xsl:text>&#x2029;</xsl:text>
											</fo:block>
										</fo:inline-container>
										<fo:table table-layout="fixed" width="100%" border-spacing="2pt">
											<fo:table-column column-width="15%"/>
											<fo:table-column column-width="15%"/>
											<fo:table-column column-width="15%"/>
											<fo:table-column column-width="15%"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-column column-width="proportional-column-width(1)"/>
											<fo:table-body start-indent="0pt">
												<fo:table-row>
													<fo:table-cell padding="0" height="20" display-align="center">
														<fo:block>
															<fo:inline font-family="Arial" font-size="9pt" font-weight="bold">
																<xsl:text>Oblg. Eff : </xsl:text>
															</fo:inline>
															<xsl:for-each select="CurrentFundEffectiveDate">
																<fo:inline font-family="Arial" font-size="9pt">
																	<xsl:value-of select="format-number(number(substring(string(string(.)), 6, 2)), '00')"/>
																	<xsl:text>/</xsl:text>
																	<xsl:value-of select="format-number(number(substring(string(string(.)), 9, 2)), '00')"/>
																	<xsl:text>/</xsl:text>
																	<xsl:value-of select="format-number(number(substring(string(string(string(.))), 1, 4)), '0000')"/>
																</fo:inline>
															</xsl:for-each>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell padding="0" height="20" display-align="center">
														<fo:block>
															<fo:inline font-family="Arial" font-size="9pt" font-weight="bold">
																<xsl:text>Oblg. Exp :&#160; </xsl:text>
															</fo:inline>
															<xsl:for-each select="ObligationExpirationDate">
																<fo:inline font-family="Arial" font-size="9pt">
																	<xsl:value-of select="format-number(number(substring(string(string(.)), 6, 2)), '00')"/>
																	<xsl:text>/</xsl:text>
																	<xsl:value-of select="format-number(number(substring(string(string(.)), 9, 2)), '00')"/>
																	<xsl:text>/</xsl:text>
																	<xsl:value-of select="format-number(number(substring(string(string(string(.))), 1, 4)), '0000')"/>
																</fo:inline>
															</xsl:for-each>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell font-weight="bold" overflow="visible" padding="0" height="20" display-align="center">
														<fo:block>
															<fo:inline font-family="Arial" font-size="9pt" font-weight="bold">
																<xsl:text>Final Exp :&#160; </xsl:text>
															</fo:inline>
															<xsl:for-each select="FinalExpirationDate">
																<fo:inline font-family="Arial" font-size="9pt" font-weight="normal">
																	<xsl:value-of select="format-number(number(substring(string(string(.)), 6, 2)), '00')"/>
																	<xsl:text>/</xsl:text>
																	<xsl:value-of select="format-number(number(substring(string(string(.)), 9, 2)), '00')"/>
																	<xsl:text>/</xsl:text>
																	<xsl:value-of select="format-number(number(substring(string(string(string(.))), 1, 4)), '0000')"/>
																</fo:inline>
															</xsl:for-each>
															<fo:inline font-family="Arial" font-size="9pt" font-weight="bold">
																<xsl:text>&#160; </xsl:text>
															</fo:inline>
															<fo:inline>
																<xsl:text>&#160;</xsl:text>
															</fo:inline>
														</fo:block>
													</fo:table-cell>
													<fo:table-cell font-weight="bold" overflow="visible" padding="0" height="20" display-align="center">
														<fo:block/>
													</fo:table-cell>
													<fo:table-cell padding="2pt" display-align="center">
														<fo:block/>
													</fo:table-cell>
													<fo:table-cell padding="2pt" display-align="center">
														<fo:block/>
													</fo:table-cell>
													<fo:table-cell padding="2pt" display-align="center">
														<fo:block/>
													</fo:table-cell>
													<fo:table-cell padding="2pt" display-align="center">
														<fo:block/>
													</fo:table-cell>
													<fo:table-cell padding="2pt" display-align="center">
														<fo:block/>
													</fo:table-cell>
													<fo:table-cell padding="2pt" display-align="center">
														<fo:block/>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</xsl:for-each>
								</xsl:for-each>
							</xsl:for-each>
						</xsl:for-each>
						<fo:block/>
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
				<fo:table table-layout="fixed" width="100%" border-spacing="2pt">
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-body start-indent="0pt">
						<fo:table-row>
							<fo:table-cell padding="0" number-columns-spanned="2" height="30" display-align="center">
								<fo:block>
									<fo:inline-container>
										<fo:block>
											<xsl:text>&#x2029;</xsl:text>
										</fo:block>
									</fo:inline-container>
									<fo:table table-layout="fixed" width="100%" border-spacing="2pt">
										<fo:table-column column-width="proportional-column-width(1)"/>
										<fo:table-column column-width="proportional-column-width(1)"/>
										<fo:table-column column-width="proportional-column-width(1)"/>
										<fo:table-column column-width="proportional-column-width(1)"/>
										<fo:table-column column-width="proportional-column-width(1)"/>
										<fo:table-column column-width="proportional-column-width(1)"/>
										<fo:table-column column-width="proportional-column-width(1)"/>
										<fo:table-column column-width="proportional-column-width(1)"/>
										<fo:table-column column-width="proportional-column-width(1)"/>
										<fo:table-column column-width="proportional-column-width(1)"/>
										<fo:table-column column-width="proportional-column-width(1)"/>
										<fo:table-body start-indent="0pt">
											<fo:table-row>
												<fo:table-cell font-family="Verdana" font-size="9pt" padding-bottom="0" number-columns-spanned="11" padding="2pt" text-align="left" display-align="before">
													<fo:block>
														<fo:block>
															<fo:leader leader-pattern="space"/>
														</fo:block>
														<fo:inline-container>
															<fo:block>
																<xsl:text>&#x2029;</xsl:text>
															</fo:block>
														</fo:inline-container>
														<fo:block margin="0pt">
															<fo:block>
																<xsl:for-each select="AwardNotice">
																	<xsl:for-each select="SchoolInfo">
																		<xsl:for-each select="SchoolName">
																			<xsl:variable name="value-of-template">
																				<xsl:apply-templates/>
																			</xsl:variable>
																			<xsl:choose>
																				<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																					<fo:block font-family="Verdana" font-size="13pt" font-weight="bold">
																						<xsl:copy-of select="$value-of-template"/>
																					</fo:block>
																				</xsl:when>
																				<xsl:otherwise>
																					<fo:inline font-family="Verdana" font-size="13pt" font-weight="bold">
																						<xsl:copy-of select="$value-of-template"/>
																					</fo:inline>
																				</xsl:otherwise>
																			</xsl:choose>
																		</xsl:for-each>
																	</xsl:for-each>
																</xsl:for-each>
															</fo:block>
														</fo:block>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell font-family="Verdana" font-size="9pt" padding-bottom="0" number-columns-spanned="11" padding="2pt" text-align="left" display-align="before">
													<fo:block>
														<fo:inline font-family="Verdana" font-size="10pt" font-style="italic" font-weight="bold">
															<xsl:text>Money and End Dates History for award-</xsl:text>
														</fo:inline>
														<xsl:for-each select="AwardNotice">
															<xsl:for-each select="Award">
																<xsl:for-each select="AwardDetails">
																	<xsl:for-each select="AwardHeader">
																		<xsl:for-each select="AwardNumber">
																			<xsl:variable name="value-of-template">
																				<xsl:apply-templates/>
																			</xsl:variable>
																			<xsl:choose>
																				<xsl:when test="contains(string($value-of-template),'&#x2029;')">
																					<fo:block font-style="italic">
																						<xsl:copy-of select="$value-of-template"/>
																					</fo:block>
																				</xsl:when>
																				<xsl:otherwise>
																					<fo:inline font-style="italic">
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
										</fo:table-body>
									</fo:table>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				<fo:inline-container>
					<fo:block>
						<xsl:text>&#x2029;</xsl:text>
					</fo:block>
				</fo:inline-container>
				<fo:table table-layout="fixed" width="100%" border-spacing="2pt">
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-body start-indent="0pt">
						<fo:table-row font-family="Arial" font-size="9pt" font-weight="bold">
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell number-columns-spanned="3" padding="2pt" display-align="center">
								<fo:block>
									<fo:inline>
										<xsl:text>Obligated Amount</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell number-columns-spanned="3" padding="2pt" display-align="center">
								<fo:block>
									<fo:inline>
										<xsl:text>Anticipated Amount</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row font-family="Arial" font-size="9pt" font-weight="bold">
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block/>
							</fo:table-cell>
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block>
									<fo:inline>
										<xsl:text>Direct</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block>
									<fo:inline>
										<xsl:text>Indirect</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block>
									<fo:inline>
										<xsl:text>Change</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block>
									<fo:inline>
										<xsl:text>Total</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block>
									<fo:inline>
										<xsl:text>Distributable</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block>
									<fo:inline>
										<xsl:text>Direct</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block>
									<fo:inline>
										<xsl:text>Indirect</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block>
									<fo:inline>
										<xsl:text>change</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block>
									<fo:inline>
										<xsl:text>Total</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell padding="2pt" display-align="center">
								<fo:block>
									<fo:inline>
										<xsl:text>Distributable</xsl:text>
									</fo:inline>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
				<fo:inline-container>
					<fo:block>
						<xsl:text>&#x2029;</xsl:text>
					</fo:block>
				</fo:inline-container>
				<fo:table table-layout="fixed" width="100%" border-spacing="2pt">
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-body start-indent="0pt">
						<fo:table-row>
							<fo:table-cell padding="0" display-align="center">
								<fo:block>
									<fo:block text-align="center">
										<fo:leader leader-pattern="rule" rule-thickness="1" leader-length="100%" color="black"/>
									</fo:block>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:block>
		</fo:static-content>
	</xsl:template>
	<xsl:template name="footerall">
		<fo:static-content flow-name="xsl-region-after">
			<fo:block>
				<xsl:for-each select="$XML">
					<fo:inline-container>
						<fo:block>
							<xsl:text>&#x2029;</xsl:text>
						</fo:block>
					</fo:inline-container>
					<fo:table overflow="visible" table-layout="fixed" width="100%" border-spacing="2pt">
						<fo:table-column column-width="proportional-column-width(1)"/>
						<fo:table-column column-width="150"/>
						<fo:table-body start-indent="0pt">
							<fo:table-row>
								<fo:table-cell padding="0" number-columns-spanned="2" display-align="center">
									<fo:block/>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell font-family="Verdana" font-size="7pt" padding="0" text-align="left" display-align="before">
									<fo:block>
										<fo:block/>
										<fo:inline>
											<xsl:text>coeus: </xsl:text>
										</fo:inline>
										<xsl:for-each select="AwardNotice">
											<xsl:for-each select="PrintRequirement">
												<xsl:for-each select="CurrentDate">
													<fo:inline>
														<xsl:value-of select="format-number(number(substring(string(string(.)), 6, 2)), '00')"/>
														<xsl:text>/</xsl:text>
														<xsl:value-of select="format-number(number(substring(string(string(.)), 9, 2)), '00')"/>
														<xsl:text>/</xsl:text>
														<xsl:value-of select="format-number(number(substring(string(string(string(.))), 1, 4)), '0000')"/>
													</fo:inline>
												</xsl:for-each>
											</xsl:for-each>
										</xsl:for-each>
										<fo:inline>
											<xsl:text>&#160;</xsl:text>
										</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell font-family="Verdana" font-size="7pt" padding="0" text-align="right" display-align="before">
									<fo:block>
										<fo:block/>
										<fo:inline>
											<xsl:text>Page&#160; </xsl:text>
										</fo:inline>
										<fo:page-number/>
										<fo:inline>
											<xsl:text>&#160;&#160;&#160;&#160;&#160;&#160; </xsl:text>
										</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</xsl:for-each>
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
</xsl:stylesheet>
