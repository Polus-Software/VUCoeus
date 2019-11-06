<?xml version="1.0" encoding="UTF-8"?><!-- $Revision: 1.4 $ -->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:PHS_AssignmentRequestForm="http://apply.grants.gov/forms/PHS_AssignmentRequestForm-V1.0"
	xmlns:footer="http://apply.grants.gov/system/Footer-V1.0" xmlns:glob="http://apply.grants.gov/system/Global-V1.0"
	xmlns:globLib="http://apply.grants.gov/system/GlobalLibrary-V2.0"
	xmlns:header="http://apply.grants.gov/system/Header-V1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xsl:output method="xml" indent="yes" />
	
	<xsl:template match="PHS_AssignmentRequestForm:PHS_AssignmentRequestForm">
		<fo:root>
			<fo:layout-master-set>
				<fo:simple-page-master master-name="first-page"
					page-height="8.5in" page-width="11in" margin-left="0.5in"
					margin-right="0.5in">
					<fo:region-body region-name="region-body"
						margin-top="1.0in" margin-bottom="0.5in" />
					<fo:region-before region-name="region-before-first"
						margin-top="0.2in" margin-bottom="0.2in" />
					<fo:region-after region-name="region-after-all" extent=".3in" />
				</fo:simple-page-master>
				<fo:simple-page-master master-name="other-page"
					page-height="8.5in" page-width="11in" margin-left="0.5in"
					margin-right="0.5in">
					<fo:region-body region-name="region-body"
						margin-top="0.8in" margin-bottom="0.4in" />
					<fo:region-before region-name="region-before-other"
						margin-top="0.2in" margin-bottom="0.2in" />
					<fo:region-after region-name="region-after-all" extent=".3in" />
				</fo:simple-page-master>
				<fo:page-sequence-master master-name="pages">
					<fo:repeatable-page-master-alternatives>
						<fo:conditional-page-master-reference
							page-position="first" master-reference="first-page" />
						<fo:conditional-page-master-reference
							master-reference="other-page" page-position="any" />
					</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="pages" format="1" initial-page-number="1">
				<!-- JM do not want to display the footer
				<fo:static-content flow-name="region-after-all">
					<fo:table width="100%" space-before.optimum="0pt"
						space-after.optimum="0pt" table-layout="fixed">
						<fo:table-column column-width="proportional-column-width(1)" />
						<fo:table-column column-width="proportional-column-width(1)" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell padding-start="0pt" padding-end="0pt"
									padding-before="1pt" padding-after="1pt" display-align="before"
									text-align="left" border-style="solid" border-width="0pt"
									border-color="white">
									<fo:block>
										<fo:inline font-size="8px">
											Tracking Number:
											<xsl:value-of select="/*/*/footer:Grants_govTrackingNumber" />
										</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell line-height="9pt" padding-start="0pt"
									padding-end="0pt" padding-before="1pt" padding-after="1pt"
									display-align="before" text-align="right" border-style="solid"
									border-width="0pt" border-color="white">
									<fo:block>
										<fo:inline font-size="8px">
											Funding Opportunity Number:
											<xsl:value-of select="/*/*/header:OpportunityID" />
										</fo:inline>
										<fo:inline font-size="8px">
											. Received Date:
											<xsl:value-of select="/*/*/footer:ReceivedDateTime" />
										</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:static-content>
				-->
				<fo:static-content flow-name="region-before-first">
					<fo:block-container position="absolute" top="0.5in" height="15px">
						<fo:block text-align="center" font-size="14pt" font-weight="bold">
							PHS Assignment Request Form
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" top="0.5in" height="12px">
						<fo:block text-align="end" font-size="9pt">
							OMB Number: 0925-0001
							<fo:block> </fo:block>
							Expiration Date: 10/31/2018
						</fo:block>
					</fo:block-container>
				</fo:static-content>
				<fo:static-content flow-name="region-before-other">
					<fo:block-container position="absolute" top="0.5in" height="15px">
						<fo:block text-align="center" font-size="14pt"
							font-weight="bold">PHS Assignment Request Form
						</fo:block>
					</fo:block-container>
				</fo:static-content>

				<fo:flow flow-name="region-body">
					<fo:table table-layout="fixed" border="none">
						<fo:table-column column-width="2.6in" />
						<fo:table-column column-width="1.8in" />
						<fo:table-column column-width="1.8in" />
						<fo:table-column column-width="1.8in" />
						<fo:table-column column-width="2.0in" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell height="12">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block font-size="9pt" font-weight="bold" padding="4px" padding-top="5px"
										display-align="before">
										Funding Opportunity Number:
									</fo:block>
								</fo:table-cell>
								<fo:table-cell number-columns-spanned="2" keep-together="always"
									border-width="1px" border-style="solid" border-color="black"
									padding="4px" background-color="yellow">
									<fo:block font-size="9pt" font-weight="normal">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:FundingOpportunityNumber) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:FundingOpportunityNumber = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of
													select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:FundingOpportunityNumber" />
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell height="6">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block font-size="9pt" font-weight="bold" padding="4px" padding-top="5px"
										display-align="before">
										Funding Opportunity Title:
									</fo:block>
								</fo:table-cell>
								<fo:table-cell number-columns-spanned="4" keep-together="always"
									border-width="1px" border-style="solid" border-color="black"
									padding="4px" background-color="yellow"  number-rows-spanned="2">
									<fo:block font-size="9pt" font-weight="normal">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:FundingOpportunityTitle) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:FundingOpportunityTitle = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of
													select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:FundingOpportunityTitle" />
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>

							<fo:table-row>
								<fo:table-cell>
									<fo:block>&#160;</fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							<fo:table-row>
								<fo:table-cell>
									<fo:block>&#160;</fo:block>
								</fo:table-cell>
							</fo:table-row>

							<!-- Awarding Component Assignment -->
							<fo:table-row>
								<fo:table-cell height="8">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="5">
									<fo:block font-size="11pt" font-weight="bold">
										Awarding Component Assignment Request
										<fo:inline font-style="italic" font-weight="normal"> (optional)</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell height="6">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="5">
									<fo:block font-size="9pt" font-weight="normal">
										If you have a preference for an Awarding Component (e.g., NIH
										Institute/Center) assignment, please use the link
										below to identify the most appropriate assignment then enter
										the short abbreviation (e.g., NCI or National Cancer Institute) in
										"Assign to/Do Not Assign To Awarding Component" sections
										below. Your first choice should be in column 1.  All requests
										will be considered; however, locus of review is predetermined
										for some applications and assignment requests cannot always be
										honored.
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell height="6">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="5" keep-together="always">
									<fo:block font-size="9pt" font-style="italic">
										Information about Awarding Components can be found here:
										<fo:inline text-decoration="underline" color="blue">
											https://grants.nih.gov/grants/phs_assignment_information.htm#AwardingComponents
										</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell height="10">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell keep-together="always">
									<fo:block>
										<fo:inline color="#FFFFFF">&#160;</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="center" text-align="center">
									<fo:block font-size="9pt" font-style="bold">
										1
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="center" text-align="center">
									<fo:block font-size="9pt" font-style="bold">
										2
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="center" text-align="center">
									<fo:block font-size="9pt" font-style="bold">
										3
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block>
										<fo:inline color="#FFFFFF">&#160;</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell keep-together="always">
									<fo:block font-size="9pt" display-align="center" 
										margin="4px" margin-left="0" margin-top="8px" 
										padding="2px" padding-left="0">
										Assign to Awarding Component:
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block font-size="9pt" font-weight="normal" text-align="center"
										 border="1px solid black" margin="4px" padding="4px">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToAwardingComponent1) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToAwardingComponent1 = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of
													select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToAwardingComponent1" />
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block font-size="9pt" font-weight="normal" text-align="center"
										border="1px solid black" margin="4px" padding="4px">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToAwardingComponent2) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToAwardingComponent2 = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of
													select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToAwardingComponent2" />
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block font-size="9pt" font-weight="normal" text-align="center"
										border="1px solid black" margin="4px" padding="4px">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToAwardingComponent3) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToAwardingComponent3 = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of
													select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToAwardingComponent3" />
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block>
										<fo:inline color="#FFFFFF">&#160;</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell keep-together="always">
									<fo:block font-size="9pt" display-align="center" 
										margin="4px" margin-left="0" margin-top="8px" 
										padding="2px" padding-left="0">
										Do Not Assign to Awarding Component:
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block font-size="9pt" font-weight="normal" text-align="center"
										 border="1px solid black" margin="4px" padding="4px">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToAwardingComponent1) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToAwardingComponent1 = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of
													select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToAwardingComponent1" />
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block font-size="9pt" font-weight="normal" text-align="center"
										 border="1px solid black" margin="4px" padding="4px">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToAwardingComponent2) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToAwardingComponent2 = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of
													select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToAwardingComponent2" />
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block font-size="9pt" font-weight="normal" text-align="center"
										 border="1px solid black" margin="4px" padding="4px">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToAwardingComponent3) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToAwardingComponent3 = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of
													select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToAwardingComponent3" />
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block>
										<fo:inline color="#FFFFFF">&#160;</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							
							<!-- Study Section Assignment -->
							<fo:table-row>
								<fo:table-cell height="20">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="5">
									<fo:block font-size="11pt" font-weight="bold">
										Study Section Assignment Request
										<fo:inline font-style="italic" font-weight="normal"> (optional)</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell height="6">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="5">
									<fo:block font-size="9pt" font-weight="normal">
										If you have a preference for a study section assignment, please
										use the link below to identify the most appropriate study
										section then enter the short abbreviation for that study
										section in the "Assign to/Do not Assign to Study Section"
										sections below. Your first choice should be in column 1.  All
										request will be considered; however, locus of review is
										predetermined for some applications and assignment request
										cannot always be honored.
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="5">
									<fo:block font-size="9pt" font-weight="normal">
										For example, you would enter "CAMP" if you wish to request assignment to
										the Cancer Molecular Pathobiology study section or enter "ZRG1 HDM-R" 
										if you wish to request assignment to the Healthcare Delivery 
										and Methodologies SBIR/STTR panel for informatics.  Be careful to 
										accurately capture all formatting (e.g., spaces, hyphens) when you 
										type in the request.
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell height="6">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell number-columns-spanned="5" keep-together="always">
									<fo:block font-size="9pt" font-style="italic">
										Information about Study Sections can be found here:
										<fo:inline text-decoration="underline" color="blue">
											https://grants.nih.gov/grants/phs_assignment_information.htm#StudySection
										</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell height="10">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell keep-together="always">
									<fo:block>
										<fo:inline color="#FFFFFF">&#160;</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="center" text-align="center">
									<fo:block font-size="9pt" font-style="bold">
										1
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="center" text-align="center">
									<fo:block font-size="9pt" font-style="bold">
										2
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="center" text-align="center">
									<fo:block font-size="9pt" font-style="bold">
										3
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block>
										<fo:inline color="#FFFFFF">&#160;</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell keep-together="always">
									<fo:block font-size="9pt" display-align="center" 
										margin-right="4px" margin-left="0" margin-top="2px" 
										margin-bottom="0" padding="0">
										Assign to Study Section:
										<fo:block> </fo:block>
										<fo:inline font-size="8px" font-style="italic" margin-top="0">
										(only 20 characters allowed)
										</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block font-size="8pt" font-weight="normal" text-align="center"
										 border="1px solid black" margin="4px" padding="4px" wrap-option="wrap">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToStudySection1) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToStudySection1 = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="intersperse-with-zero-spaces">
													<xsl:with-param name="str"
														select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToStudySection1" />
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block font-size="8pt" font-weight="normal" text-align="center"
										border="1px solid black" margin="4px" padding="4px" wrap-option="wrap">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToStudySection2) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToStudySection2 = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="intersperse-with-zero-spaces">
													<xsl:with-param name="str"
														select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToStudySection2" />
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block font-size="8pt" font-weight="normal" text-align="center"
										border="1px solid black" margin="4px" padding="4px" wrap-option="wrap">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToStudySection3) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToStudySection3 = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="intersperse-with-zero-spaces">
													<xsl:with-param name="str"
														select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:AssignToStudySection3" />
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block>
										<fo:inline color="#FFFFFF">&#160;</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell keep-together="always">
									<fo:block font-size="9pt" display-align="center"
										margin-right="4px" margin-left="0" margin-top="2px" 
										margin-bottom="0" padding="0">
										Do Not Assign to Study Section:
										<fo:block> </fo:block>
										<fo:inline font-size="8px" font-style="italic">
											(only 20 characters allowed)
										</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block font-size="8pt" font-weight="normal" text-align="center"
										 border="1px solid black" margin="4px" padding="4px" wrap-option="wrap">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToStudySection1) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToStudySection1 = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="intersperse-with-zero-spaces">
													<xsl:with-param name="str"
														select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToStudySection1" />
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block font-size="8pt" font-weight="normal" text-align="center"
										 border="1px solid black" margin="4px" padding="4px" wrap-option="wrap">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToStudySection2) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToStudySection2 = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="intersperse-with-zero-spaces">
													<xsl:with-param name="str"
														select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToStudySection2" />
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block font-size="8pt" font-weight="normal" text-align="center"
										 border="1px solid black" margin="4px" padding="4px" wrap-option="wrap">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToStudySection3) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToStudySection3 = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="intersperse-with-zero-spaces">
													<xsl:with-param name="str"
														select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:NotAssignToStudySection3" />
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block>
										<fo:inline color="#FFFFFF">&#160;</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>

					<fo:block break-after="page">
						<xsl:text>&#xA;</xsl:text>
					</fo:block>

					<!-- Individuals who should not review -->
					<fo:table table-layout="fixed" border="none">
						<fo:table-column column-width="proportional-column-width(1)" />
						<fo:table-column column-width="proportional-column-width(1)" />
						<fo:table-column column-width="proportional-column-width(1)" />
						<fo:table-column column-width="proportional-column-width(1)" />
						<fo:table-column column-width="proportional-column-width(1)" />
						<fo:table-column column-width="proportional-column-width(1)" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell height="16">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>	
							<fo:table-row>
								<fo:table-cell keep-together="always" number-columns-spanned="4">
									<fo:block font-size="9pt" font-weight="bold">
											List individuals who should not review your application and why
										<fo:inline font-size="9pt" font-style="italic" font-weight="normal">
											(optional)
										</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" number-columns-spanned="2">
									<fo:block text-align="right" font-size="9pt" font-style="italic" font-weight="normal">
										Only 1000 characters allowed
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell height="4">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>							
							<fo:table-row>
								<fo:table-cell height="auto" number-columns-spanned="6" border="1px solid black">
									<fo:block font-size="9pt" font-weight="normal" margin="4px" padding="4px">
										<xsl:choose>
											<xsl:when
												test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:IndividualsNotToReviewText) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:IndividualsNotToReviewText = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of
													select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:IndividualsNotToReviewText" />
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell height="16">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell keep-together="always" number-columns-spanned="6">
									<fo:block font-size="9pt" font-weight="bold">
										Identify Scientific areas of expertise needed to review your applications
										<fo:inline font-style="italic" font-size="9pt" font-weight="normal">
											(optional)
										</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell keep-together="always" number-columns-spanned="6">
									<fo:block font-size="9pt" font-style="italic">
										<fo:inline text-decoration="underline">Note:</fo:inline>
										Please do not provide names of individuals
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell height="10">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell keep-together="always">
									<fo:block>
										<fo:inline color="#FFFFFF">&#160;</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="center" text-align="center">
									<fo:block font-size="9pt">
										1
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="center" text-align="center">
									<fo:block font-size="9pt">
										2
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="center" text-align="center">
									<fo:block font-size="9pt">
										3
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="center" text-align="center">									<fo:block font-size="9pt">
										4
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="center" text-align="center">									<fo:block font-size="9pt">
										5
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell keep-together="always" display-align="center">
									<fo:block font-size="9pt" display-align="center" 
										margin-right="4px" margin-left="0" margin-top="2px" 
										margin-bottom="0" padding="0">
										Expertise:
										<fo:block> </fo:block>
										<fo:inline font-style="italic">
											Only 40 characters allowed
										</fo:inline>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block-container height="20px" margin="4px" padding="4px" border="1px solid black" >
										<fo:block font-size="8pt" font-weight="normal" text-align="center" wrap-option="wrap">
											<xsl:choose>
												<xsl:when
													test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise1) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise1 = ''">
													<fo:inline color="#FFFFFF">&#160;</fo:inline>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str"
															select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise1" />
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:block>
									</fo:block-container>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block-container height="20px" margin="4px" padding="4px" border="1px solid black" >
										<fo:block font-size="8pt" font-weight="normal" text-align="center" wrap-option="wrap">
											<xsl:choose>
												<xsl:when
													test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise2) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise2 = ''">
													<fo:inline color="#FFFFFF">&#160;</fo:inline>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str"
															select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise2" />
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:block>
									</fo:block-container>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block-container height="20px" margin="4px" padding="4px" border="1px solid black" >
										<fo:block font-size="8pt" font-weight="normal" text-align="center" wrap-option="wrap">
											<xsl:choose>
												<xsl:when
													test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise3) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise3 = ''">
													<fo:inline color="#FFFFFF">&#160;</fo:inline>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str"
															select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise3" />
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:block>
									</fo:block-container>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block-container height="20px" margin="4px" padding="4px" border="1px solid black" >
										<fo:block font-size="8pt" font-weight="normal" text-align="center" wrap-option="wrap">
											<xsl:choose>
												<xsl:when
													test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise4) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise4 = ''">
													<fo:inline color="#FFFFFF">&#160;</fo:inline>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str"
															select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise4" />
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:block>
									</fo:block-container>
								</fo:table-cell>
								<fo:table-cell keep-together="always">
									<fo:block-container height="20px" margin="4px" padding="4px" border="1px solid black" >
										<fo:block font-size="8pt" font-weight="normal" text-align="center" wrap-option="wrap">
											<xsl:choose>
												<xsl:when
													test="not(//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise5) or //PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise5 = ''">
													<fo:inline color="#FFFFFF">&#160;</fo:inline>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="intersperse-with-zero-spaces">
														<xsl:with-param name="str"
															select="//PHS_AssignmentRequestForm:PHS_AssignmentRequestForm/PHS_AssignmentRequestForm:Expertise5" />
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:block>
									</fo:block-container>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell height="10">
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
	<xsl:template name="intersperse-with-zero-spaces">
		<xsl:param name="str" />
		<xsl:variable name="spacechars">
		</xsl:variable>

		<xsl:if test="string-length($str) > 0">
			<xsl:variable name="c1" select="substring($str, 1, 1)" />

			<xsl:value-of select="$c1" />
			<xsl:text>​&#8203;</xsl:text>

			<xsl:call-template name="intersperse-with-zero-spaces">
				<xsl:with-param name="str" select="substring($str, 2)" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
