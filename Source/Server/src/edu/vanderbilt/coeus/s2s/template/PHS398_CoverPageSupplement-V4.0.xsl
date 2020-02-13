<?xml version="1.0" encoding="UTF-8"?><!-- $Revision: 1.4 $ -->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:PHS398_CoverPageSupplement_4_0="http://apply.grants.gov/forms/PHS398_CoverPageSupplement_4_0-V4.0"
	xmlns:footer="http://apply.grants.gov/system/Footer-V1.0" xmlns:att="http://apply.grants.gov/system/Attachments-V1.0"
	xmlns:glob="http://apply.grants.gov/system/Global-V1.0" xmlns:globLib="http://apply.grants.gov/system/GlobalLibrary-V2.0"
	xmlns:header="http://apply.grants.gov/system/Header-V1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xsl:output method="xml" indent="yes" />
	<xsl:template
		match="PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0">
		<fo:root>
			<fo:layout-master-set>
				<fo:simple-page-master master-name="first-page"
					page-height="11in" page-width="8.5in" margin-left="0.5in"
					margin-right="0.5in">
					<fo:region-body region-name="region-body"
						margin-top="1.2in" margin-bottom="0.4in" />
					<fo:region-before region-name="region-before-first"
						margin-top="0.2in" margin-bottom="0.2in" />
					<fo:region-after region-name="region-after-all"
						extent=".3in" />
				</fo:simple-page-master>
				<fo:simple-page-master master-name="other-page"
					page-height="11in" page-width="8.5in" margin-left="0.5in"
					margin-right="0.5in">
					<fo:region-body region-name="region-body"
						margin-top="0.8in" margin-bottom="0.4in" />
					<fo:region-before region-name="region-before-other"
						margin-top="0.2in" margin-bottom="0.2in" />
					<fo:region-after region-name="region-after-all"
						extent=".3in" />
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

			<fo:page-sequence master-reference="pages" format="1"
				initial-page-number="1">
				<fo:static-content flow-name="region-before-first">
					<fo:block-container position="absolute" left="0.2in"
						top="0.5in" height="15px">
						<fo:block text-align="center" font-size="12pt" font-weight="bold">
							PHS 398 Cover Page Supplement
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="0.2in" top="0.7in" height="12px">
						<fo:block text-align="end" font-size="6px">
							OMB Number: 0925-0001
						</fo:block>
						<fo:block text-align="end" font-size="6px">
							Expiration Date: 03/31/2020
						</fo:block>
					</fo:block-container>
				</fo:static-content>
				<fo:static-content flow-name="region-before-other">
					<fo:block-container position="absolute" left="0.2in"
						top="0.5in" height="15px">
						<fo:block text-align="center" font-size="12pt"
							 font-weight="bold">PHS 398 Cover Page Supplement
						</fo:block>
					</fo:block-container>
				</fo:static-content>

				<fo:flow flow-name="region-body">
					 
					<xsl:variable name="lineLen"
						select="string-length(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:EuthanasiaMethodDescription)" />
					<xsl:variable name="vertAnimHeight"
						select="2.1 + ceiling($lineLen div 105) * .13" />
					<xsl:variable name="top" select="1.9 + $vertAnimHeight" />
					
					<fo:table table-layout="fixed" border="1px solid black">
						<fo:table-column column-width="3.6in" />
						<fo:table-column column-width="0.25in" />
						<fo:table-column column-width="0.50in" />
						<fo:table-column column-width="0.25in" />
						<fo:table-column column-width="2.75in" />
						<fo:table-column column-width="0.15in" />
						<fo:table-body>
							
							<!-- Vertebrate Animals -->
							<fo:table-row>
								<fo:table-cell number-columns-spanned="6" keep-together="always" 
									height="20px" padding-top="10px" padding-left="10px" display-align="before">
									<fo:block font-size="10pt" font-weight="bold">
										1. Vertebrate Animals Section
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							<fo:table-row>
								<fo:table-cell keep-together="always"
									height="20px" padding-left="15px" display-align="before">
									<fo:block font-size="9pt">
										Are vertebrate animals euthanized?
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="before">
									<fo:block font-size="9pt" font-family="ZapfDingbats">
										<xsl:choose>
											<xsl:when
												test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:AnimalEuthanasiaIndicator) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:AnimalEuthanasiaIndicator = ''">
												<xsl:call-template name="radioButton">
													<xsl:with-param name="value">
														N: No
													</xsl:with-param>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="radioButton">
													<xsl:with-param name="value"
														select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:AnimalEuthanasiaIndicator" />
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="before">
									<fo:block font-size="9pt">Yes</fo:block>
								</fo:table-cell>

								<fo:table-cell keep-together="always" display-align="before">
									<fo:block font-size="9pt" font-family="ZapfDingbats">
										<xsl:choose>
											<xsl:when
												test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:AnimalEuthanasiaIndicator) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:AnimalEuthanasiaIndicator= ''">
												<xsl:call-template name="radioButton">
													<xsl:with-param name="value">
														N: No
													</xsl:with-param>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="radioButton">
													<xsl:with-param name="value"
														select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:AnimalEuthanasiaIndicator" />
													<xsl:with-param name="schemaChoice">
														N: No
													</xsl:with-param>
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="before">
									<fo:block font-size="9pt">No</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							<fo:table-row>
								<fo:table-cell keep-together="always" number-columns-spanned="6"
									height="20px" padding-left="20px" display-align="before">
									<fo:block font-size="9pt">
										If
										<fo:inline font-weight="bold">"Yes"</fo:inline>
										to euthanasia
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							<fo:table-row>
								<fo:table-cell keep-together="always"
									height="30px" padding-left="30px" display-align="before">
									<fo:block font-size="9pt">
										Is the method consistent with American Veterinary 
										<xsl:text>&#xa;</xsl:text>
										Medical Association (AVMA) guidelines?
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="before">
									<fo:block font-size="9pt" font-family="ZapfDingbats">
										<xsl:choose>
											<xsl:when
												test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:AVMAConsistentIndicator) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:AVMAConsistentIndicator= ''">
												<xsl:call-template name="radioButton">
													<xsl:with-param name="value">
														N: No
													</xsl:with-param>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="radioButton">
													<xsl:with-param name="value"
														select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:AVMAConsistentIndicator" />
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="before">
									<fo:block font-size="9pt">Yes</fo:block>
								</fo:table-cell>

								<fo:table-cell keep-together="always" display-align="before">
									<fo:block font-size="9pt" font-family="ZapfDingbats">
										<xsl:choose>
											<xsl:when
												test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:AVMAConsistentIndicator) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:AVMAConsistentIndicator= ''">
												<xsl:call-template name="radioButton">
													<xsl:with-param name="value">
														N: No
													</xsl:with-param>
												</xsl:call-template>
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="radioButton">
													<xsl:with-param name="value"
														select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:AVMAConsistentIndicator" />
													<xsl:with-param name="schemaChoice">
														N: No
													</xsl:with-param>
												</xsl:call-template>
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell keep-together="always" display-align="before">
									<fo:block font-size="9pt">No</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							<fo:table-row>
								<fo:table-cell height="40px" padding-left="15px" padding-right="10px"
									 display-align="before">
									<fo:block font-size="9pt">
										If
										<fo:inline font-weight="bold">"No"</fo:inline>
										to AVMA guidelines, describe method and proved scientific
										justification
									</fo:block>
								</fo:table-cell>
								<fo:table-cell number-columns-spanned="4" border="1px solid black" padding="2px">
									<fo:block font-size="9pt">
										<xsl:choose>
											<xsl:when
												test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:EuthanasiaMethodDescription) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:EuthanasiaMethodDescription = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of
													select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:VertebrateAnimals/PHS398_CoverPageSupplement_4_0:EuthanasiaMethodDescription" />
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell height="4px">
									<fo:block></fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							<!-- Program Income -->
							<fo:table-row>
								<fo:table-cell number-columns-spanned="6" keep-together="always" 
									height="20px" padding-top="10px" padding-left="10px" display-align="before"
									border-top="1px solid black">
									<fo:block font-size="10pt" font-weight="bold">
										2. *Program Income Section
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell keep-together="always" number-columns-spanned="6"
									height="16px" padding-left="15px" display-align="before">
									<fo:block font-size="9pt">
										*Is program income anticipated during the periods for which the grant
										support is 	requested?
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell height="35px" keep-together="always"
									display-align="before">
									<fo:block border="1px solid black" padding="4px" padding-left="14px" 
										margin-left="50px" margin-right="60px">
										<fo:inline font-size="9pt" font-family="ZapfDingbats">
											<xsl:choose>
												<xsl:when
													test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:ProgramIncome) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:ProgramIncome = ''">
													<xsl:call-template name="radioButton">
														<xsl:with-param name="value">
															N: No
														</xsl:with-param>
													</xsl:call-template>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="radioButton">
														<xsl:with-param name="value"
															select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:ProgramIncome" />
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:inline>
										<fo:inline font-size="9pt" padding-left="10px">Yes</fo:inline>
										<fo:inline font-size="9pt" font-style="normal" font-family="ZapfDingbats"
											padding-left="20px">
											<xsl:choose>
												<xsl:when
													test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:ProgramIncome) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:ProgramIncome = ''">
													<xsl:call-template name="radioButton">
														<xsl:with-param name="value">
															N: No
														</xsl:with-param>
													</xsl:call-template>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="radioButton">
														<xsl:with-param name="value"
															select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:ProgramIncome" />
														<xsl:with-param name="schemaChoice">
															N: No
														</xsl:with-param>
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:inline>
										<fo:inline font-size="9pt" padding-left="10px">No</fo:inline>
									</fo:block>
								</fo:table-cell>

							</fo:table-row>
							<fo:table-row>
								<fo:table-cell padding-left="15px" height="20px"
									number-columns-spanned="6">
									<fo:block font-size="9pt">
										If you checked "yes" above (indicating that program income is 
										anticipated), then use the format below to reflect the amount and
										source(s).  Otherwise, leave this section blank.
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
	
							<!-- Program income entries - MAX 10 for V3.0 -->
							<fo:table-row>
								<fo:table-cell keep-together="always" number-columns-spanned="6">					
									<xsl:call-template name="tableHeader">
										<xsl:with-param name="topBorderWidth" select="0.0" />
										<xsl:with-param name="rowMargin" select="0.1" />
									</xsl:call-template>
									<xsl:call-template name="incomeTable">
										<xsl:with-param name="beginPosition" select="1.6" />
										<xsl:with-param name="boxTop" select="$top" />
										<xsl:with-param name="items"
											select="PHS398_CoverPageSupplement_4_0:IncomeBudgetPeriod" />
									</xsl:call-template>
								</fo:table-cell>
							</fo:table-row>
							<!-- END program income entries -->

							<!-- Human Embryonic Stem Cells -->
							<fo:table-row>
								<fo:table-cell number-columns-spanned="6" keep-together="always" 
									height="20px" padding-top="10px" padding-left="10px" display-align="before"
									border-top="1px solid black">
									<fo:block font-size="10pt" font-weight="bold">
										3. Human Embryonic Stem Cells Section
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						
							<fo:table-row>
								<fo:table-cell keep-together="always"
									padding-left="15px" padding-right="0px" display-align="center"
									number-columns-spanned="2">
									<fo:block font-size="9pt">
										*Does the proposed project involve human embryonic stem cells?
									</fo:block>
								</fo:table-cell>
								<fo:table-cell height="20px" keep-together="always"
									display-align="center" number-columns-spanned="4">
									<fo:block border="1px solid black" padding="4px" padding-left="14px" 
										margin-left="50px" margin-right="70px">
										<fo:inline font-size="9pt" font-family="ZapfDingbats">
											<xsl:choose>
												<xsl:when
													test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:StemCells/PHS398_CoverPageSupplement_4_0:isHumanStemCellsInvolved) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:StemCells/PHS398_CoverPageSupplement_4_0:isHumanStemCellsInvolved = ''">
													<fo:inline color="#FFFFFF">&#160;</fo:inline>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="radioButton">
														<xsl:with-param name="value"
															select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:StemCells/PHS398_CoverPageSupplement_4_0:isHumanStemCellsInvolved" />
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:inline>
										<fo:inline font-size="9pt" padding-left="10px">Yes</fo:inline>
										<fo:inline font-size="9pt" font-family="ZapfDingbats" padding-left="20px">
											<xsl:choose>
												<xsl:when
													test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:StemCells/PHS398_CoverPageSupplement_4_0:isHumanStemCellsInvolved) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:StemCells/PHS398_CoverPageSupplement_4_0:isHumanStemCellsInvolved = ''">
													<fo:inline color="#FFFFFF">&#160;</fo:inline>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="radioButton">
														<xsl:with-param name="value"
															select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:StemCells/PHS398_CoverPageSupplement_4_0:isHumanStemCellsInvolved" />
														<xsl:with-param name="schemaChoice">
															N: No
														</xsl:with-param>
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:inline>
										<fo:inline font-size="9pt" padding-left="10px">No</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							<fo:table-row>
								<fo:table-cell number-columns-spanned="6" 
									padding-left="15px" padding-right="15px" 
									padding-top="10px" padding-bottom="5px">
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
								<fo:table-cell number-columns-spanned="6" height="20px" padding-left="50px">		
									<fo:block>
										<!-- Below block is to get checkbox image -->
										<fo:block-container background-color="transparent"
											border-style="none" position="absolute" left="15px"
											hyphenate="true" language="en" keep-together="always">
											<fo:block background-color="transparent" color="#000000"
												font-style="normal" font-family="ZapfDingbats" font-size="9pt"
												font-weight="normal" display-align="center">
												<xsl:choose>
													<xsl:when test="boolean(0)">
														<fo:inline color="#FFFFFF">&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<fo:inline font-family="ZapfDingbats" font-size="9pt">&#x274F;</fo:inline>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:block-container>
										<fo:block-container background-color="transparent"
											border-style="none" position="absolute" left="15px"
											hyphenate="true" language="en" keep-together="always">
											<fo:block background-color="transparent" color="#000000"
												font-style="normal" font-family="ZapfDingbats" font-size="9pt"
												font-weight="normal">
												<xsl:choose>
													<xsl:when
														test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:StemCells/PHS398_CoverPageSupplement_4_0:StemCellsIndicator) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:StemCells/PHS398_CoverPageSupplement_4_0:StemCellsIndicator = ''">
															<xsl:call-template name="checkboxN">
																<xsl:with-param name="value"
																	select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:StemCells/PHS398_CoverPageSupplement_4_0:StemCellsIndicator"/>
																<xsl:with-param name="schemaChoice">
																	N: No
																</xsl:with-param>
															</xsl:call-template>
													</xsl:when>
													<xsl:otherwise>
														<xsl:call-template name="checkboxY">
															<xsl:with-param name="value"
																select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:StemCells/PHS398_CoverPageSupplement_4_0:StemCellsIndicator"/>
															<xsl:with-param name="schemaChoice">
																Y: Yes
															</xsl:with-param>
														</xsl:call-template>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:block-container>
										<fo:block-container background-color="transparent"
											border-style="none" position="absolute" left="26px"
											hyphenate="true" language="en" keep-together="always"
											font-size="9pt">
											<fo:block>
												<fo:inline>
													<xsl:text>&#160;Specific stem cell line cannot be referenced at this 
													time. One from the registry will be used.</xsl:text>
												</fo:inline>
											</fo:block>
										</fo:block-container>
									</fo:block>
									<!-- end -->
								</fo:table-cell>
							</fo:table-row>
					
							<fo:table-row>
								<fo:table-cell number-columns-spanned="6" padding-left="15px">
									<fo:block font-size="9pt" font-weight="bold">Cell Line(s) (Example: 0004):</fo:block>
								</fo:table-cell>
							</fo:table-row>
					
							<fo:table-row>
								<fo:table-cell number-columns-spanned="6" height="100%" 
									padding-left="10px" padding-right="10px"
									padding-top="6px" padding-bottom="6px">
									<fo:block>
										<xsl:choose>
											<xsl:when 
												test="count(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:StemCells/PHS398_CoverPageSupplement_4_0:CellLines) > 0">
												<fo:table>
													<fo:table-body>
														<xsl:for-each
															select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:StemCells/PHS398_CoverPageSupplement_4_0:CellLines">
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

							<!-- Inventions and Patents -->
							<fo:table-row>
								<fo:table-cell number-columns-spanned="6" keep-together="always" 
										height="20px" padding-top="10px" padding-left="10px" display-align="before"
										border-top="1px solid black">
									<fo:block font-size="10pt"
										font-weight="bold">4. Inventions and Patents Section (for Renewal applications)
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							<fo:table-row>
								<fo:table-cell keep-together="always" height="20px" 
									padding-left="15px" padding-right="0px" display-align="center">
									<fo:block font-size="9pt">
										*Inventions and Patents:
									</fo:block>
								</fo:table-cell>
								<fo:table-cell height="20px" keep-together="always"
									display-align="center" number-columns-spanned="5">
									<fo:block padding="4px" padding-left="14px">
										<fo:inline font-size="9pt" font-family="ZapfDingbats">
											<xsl:choose>
												<xsl:when test="boolean(0)">
													<fo:inline color="#FFFFFF">&#160;</fo:inline>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="radioButton">
														<xsl:with-param name="value"
															select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:IsInventionsAndPatents" />
														<xsl:with-param name="schemaChoice">
															Y: Yes
														</xsl:with-param>
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:inline>
										<fo:inline font-size="9pt" padding-left="10px">Yes</fo:inline>
										<fo:inline font-size="9pt" font-family="ZapfDingbats" padding-left="20px">
											<xsl:choose>
												<xsl:when test="boolean(0)">
													<fo:inline color="#FFFFFF">&#160;</fo:inline>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="radioButton">
														<xsl:with-param name="value"
															select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:IsInventionsAndPatents" />
														<xsl:with-param name="schemaChoice">
															N: No
														</xsl:with-param>
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:inline>
										<fo:inline font-size="9pt" padding-left="10px">No</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							<fo:table-row>						
								<fo:table-cell keep-together="always"
									padding-left="15px" padding-right="0px" display-align="center"
									number-columns-spanned="6" height="26px">
									<fo:block font-size="9pt">
										If the answer is
										<fo:inline font-weight="bold">"Yes"</fo:inline>
										then please answer the following:
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell keep-together="always" height="24px"
									padding-left="15px" padding-right="0px" display-align="center">
									<fo:block font-size="9pt">
										*Previously Reported:
									</fo:block>
								</fo:table-cell>
								<fo:table-cell height="24px" keep-together="always"
									display-align="center" number-columns-spanned="5">
									<fo:block padding="4px" padding-left="14px" >
										<fo:inline font-size="9pt" font-family="ZapfDingbats">
											<xsl:choose>
												<xsl:when test="boolean(0)">
													<fo:inline color="#FFFFFF">&#160;</fo:inline>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="radioButton">
														<xsl:with-param name="value"
															select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:IsPreviouslyReported" />
														<xsl:with-param name="schemaChoice">
															Y: Yes
														</xsl:with-param>
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:inline>
										<fo:inline font-size="9pt" padding-left="10px">Yes</fo:inline>
										<fo:inline font-size="9pt" font-family="ZapfDingbats" padding-left="20px">
											<xsl:choose>
												<xsl:when test="boolean(0)">
													<fo:inline color="#FFFFFF">&#160;</fo:inline>
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="radioButton">
														<xsl:with-param name="value"
															select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:IsPreviouslyReported" />
														<xsl:with-param name="schemaChoice">
															N: No
														</xsl:with-param>
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</fo:inline>
										<fo:inline font-size="9pt" padding-left="10px">No</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					
					<fo:block break-after="page">
						<xsl:text>&#xA;</xsl:text>
					</fo:block>
							
					<fo:table table-layout="fixed" border="1px solid black">
						<fo:table-column column-width="3.6in" />
						<fo:table-column column-width="0.25in" />
						<fo:table-column column-width="0.50in" />
						<fo:table-column column-width="0.25in" />
						<fo:table-column column-width="2.75in" />
						<fo:table-column column-width="0.15in" />
						<fo:table-body>

							<!-- Change of Investigator / Change of Institution -->
							<fo:table-row>
								<fo:table-cell number-columns-spanned="6" keep-together="always" 
										height="20px" padding-top="10px" padding-left="10px" display-align="before">
									<fo:block font-size="10pt"
										font-weight="bold">5. Change of Investigator / Change of Institution Section
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							<fo:table-row>
								<fo:table-cell number-columns-spanned="6" padding-left="10px" height="20px">		
									<fo:block>
										<!-- Below block is to get checkbox image -->
										<fo:block-container background-color="transparent"
											border-style="none" position="absolute" left="15px"
											hyphenate="true" language="en" keep-together="always">
											<fo:block background-color="transparent" color="#000000"
												font-style="normal" font-family="ZapfDingbats" font-size="9pt"
												font-weight="normal" display-align="center">
												<xsl:choose>
													<xsl:when test="boolean(0)">
														<fo:inline color="#FFFFFF">&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<fo:inline font-family="ZapfDingbats" font-size="9pt">&#x274F;</fo:inline>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:block-container>
										<fo:block-container background-color="transparent"
											border-style="none" position="absolute" left="15px"
											hyphenate="true" language="en" keep-together="always">
											<fo:block background-color="transparent" color="#000000"
												font-style="normal" font-family="ZapfDingbats" font-size="9pt"
												font-weight="normal">
												<xsl:choose>
													<xsl:when
														test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:IsChangeOfPDPI) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:IsChangeOfPDPI = ''">
															<xsl:call-template name="checkboxN">
																<xsl:with-param name="value"
																	select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:IsChangeOfPDPI" />
																<xsl:with-param name="schemaChoice">
																	N: No
																</xsl:with-param>
															</xsl:call-template>
													</xsl:when>
													<xsl:otherwise>
														<xsl:call-template name="checkboxY">
															<xsl:with-param name="value"
																select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:IsChangeOfPDPI" />
															<xsl:with-param name="schemaChoice">
																Y: Yes
															</xsl:with-param>
														</xsl:call-template>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:block-container>
										<fo:block-container background-color="transparent"
											border-style="none" position="absolute" left="26px"
											hyphenate="true" language="en" keep-together="always"
											font-size="9pt">
											<fo:block>
												<fo:inline>
													<xsl:text>&#160;Change of Project Director / Principal Investigator</xsl:text>
												</fo:inline>
											</fo:block>
										</fo:block-container>
									</fo:block>
									<!-- end -->
								</fo:table-cell>
							</fo:table-row>
						
							<fo:table-row>
								<fo:table-cell number-columns-spanned="6" height="20px">
									<fo:block font-size="9pt" margin-left="40px">
										Name of former Project Director/Principal Investigator:
									</fo:block>
								</fo:table-cell>
							</fo:table-row>

							<fo:table-row>
								<fo:table-cell number-columns-spanned="6" padding-left="40px">
									<fo:block>
										<fo:table table-layout="fixed">
											<fo:table-column column-width="1.0in" />
											<fo:table-column column-width="1.5in" />
											<fo:table-column column-width="0.5in" />
											<fo:table-column column-width="0.5in" />
											<fo:table-column column-width="1.5in" />
											<fo:table-column column-width="1.5in" />
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell font-size="9pt" text-align="end"
														padding-right="10px" display-align="center">
														<fo:block>
															Prefix:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell font-size="9pt" height="10px">
														<fo:block border="1px solid black" margin="2px" 
															padding="2px" display-align="center">
															<xsl:choose>
																<xsl:when
																	test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:PrefixName) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:PrefixName = ''">
																	<fo:inline color="#FFFFFF">&#160;</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of
																		select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:PrefixName" />
																</xsl:otherwise>
															</xsl:choose>
														</fo:block>
													</fo:table-cell>
												</fo:table-row>
												
												<fo:table-row>
													<fo:table-cell font-size="9pt" text-align="end"
														padding-right="10px" display-align="center">
														<fo:block>
															*First Name:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell font-size="9pt" height="10px">
														<fo:block border="1px solid black" margin="2px" 
															padding="2px" display-align="center">
															<xsl:choose>
																<xsl:when
																	test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:FirstName) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:FirstName = ''">
																	<fo:inline color="#FFFFFF">&#160;</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of
																		select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:FirstName" />
																</xsl:otherwise>
															</xsl:choose>
														</fo:block>							
													</fo:table-cell>
												</fo:table-row>
												
												<fo:table-row>
													<fo:table-cell font-size="9pt" text-align="end"
														padding-right="10px" display-align="center">
														<fo:block>
															Middle Name:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell font-size="9pt" height="10px">
														<fo:block border="1px solid black" margin="2px" 
															padding="2px" display-align="center">
															<xsl:choose>
																<xsl:when
																	test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:MiddleName) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:MiddleName = ''">
																	<fo:inline color="#FFFFFF">&#160;</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of
																		select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:MiddleName" />
																</xsl:otherwise>
															</xsl:choose>
														</fo:block>						
													</fo:table-cell>
												</fo:table-row>
							
												<fo:table-row>
													<fo:table-cell font-size="9pt" text-align="end"
														padding-right="10px" display-align="center">
														<fo:block>							
															*Last Name:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell font-size="9pt" height="10px">
														<fo:block border="1px solid black" margin="2px" 
															padding="2px" display-align="center">
															<xsl:choose>
																<xsl:when
																	test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:LastName) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:LastName = ''">
																	<fo:inline color="#FFFFFF">&#160;</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of
																		select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:LastName" />
																</xsl:otherwise>
															</xsl:choose>
														</fo:block>							
													</fo:table-cell>
												</fo:table-row>
							
												<fo:table-row>
													<fo:table-cell font-size="9pt" text-align="end"
														padding-right="10px" display-align="center">
														<fo:block>		
															Suffix:
														</fo:block>
													</fo:table-cell>
													<fo:table-cell font-size="9pt" height="10px">
														<fo:block border="1px solid black" margin="2px" 
															padding="2px" display-align="center">
															<xsl:choose>
																<xsl:when
																	test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:SuffixName) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:SuffixName = ''">
																	<fo:inline color="#FFFFFF">&#160;</fo:inline>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of
																		select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerPD_Name/globLib:SuffixName" />
																</xsl:otherwise>
															</xsl:choose>
														</fo:block>							
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>							
								</fo:table-cell>
							</fo:table-row>
							
							<!-- spacer row -->
							<fo:table-row>
								<fo:table-cell height="14px">
									<fo:block>
										<xsl:text>&#xA;</xsl:text>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							
							<fo:table-row>
								<fo:table-cell number-columns-spanned="6" padding-left="10px" height="20px">		
									<fo:block>
										<!-- Below block is to get checkbox image -->
										<fo:block-container background-color="transparent"
											border-style="none" position="absolute" left="15px"
											hyphenate="true" language="en" keep-together="always">
											<fo:block background-color="transparent" color="#000000"
												font-style="normal" font-family="ZapfDingbats" font-size="9pt"
												font-weight="normal" display-align="center">
												<xsl:choose>
													<xsl:when test="boolean(0)">
														<fo:inline color="#FFFFFF">&#160;</fo:inline>
													</xsl:when>
													<xsl:otherwise>
														<fo:inline font-family="ZapfDingbats" font-size="9pt">&#x274F;</fo:inline>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:block-container>
										<fo:block-container background-color="transparent"
											border-style="none" position="absolute" left="15px"
											hyphenate="true" language="en" keep-together="always">
											<fo:block background-color="transparent" color="#000000"
												font-style="normal" font-family="ZapfDingbats" font-size="9pt"
												font-weight="normal">
												<xsl:choose>
													<xsl:when
														test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:IsChangeOfPDPI) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:IsChangeOfInstitution = ''">
															<xsl:call-template name="checkboxN">
																<xsl:with-param name="value"
																	select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:IsChangeOfInstitution" />
																<xsl:with-param name="schemaChoice">
																	N: No
																</xsl:with-param>
															</xsl:call-template>
													</xsl:when>
													<xsl:otherwise>
														<xsl:call-template name="checkboxY">
															<xsl:with-param name="value"
																select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:IsChangeOfInstitution" />
															<xsl:with-param name="schemaChoice">
																Y: Yes
															</xsl:with-param>
														</xsl:call-template>
													</xsl:otherwise>
												</xsl:choose>
											</fo:block>
										</fo:block-container>
										<fo:block-container background-color="transparent"
											border-style="none" position="absolute" left="26px"
											hyphenate="true" language="en" keep-together="always"
											font-size="9pt">
											<fo:block>
												<fo:inline>
													<xsl:text>&#160;Change of Grantee Institution</xsl:text>
												</fo:inline>
											</fo:block>
										</fo:block-container>
									</fo:block>
									<!-- end -->
								</fo:table-cell>
							</fo:table-row>
							
							<fo:table-row>
								<fo:table-cell number-columns-spanned="6" height="20px">
									<fo:block font-size="9pt" margin-left="40px">
										*Name of former institution:
									</fo:block>
								</fo:table-cell>
							</fo:table-row>	
							
							<fo:table-row>
								<fo:table-cell font-size="9pt" height="10px" padding-left="40px">
									<fo:block border="1px solid black" margin="2px" 
										padding="2px" display-align="center">
										<xsl:choose>
											<xsl:when
												test="not(//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerInstitutionName) or //PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerInstitutionName = ''">
												<fo:inline color="#FFFFFF">&#160;</fo:inline>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of
													select="//PHS398_CoverPageSupplement_4_0:PHS398_CoverPageSupplement_4_0/PHS398_CoverPageSupplement_4_0:FormerInstitutionName" />
											</xsl:otherwise>
										</xsl:choose>
									</fo:block>							
								</fo:table-cell>
							</fo:table-row>	
							
							<!-- spacer row -->
							<fo:table-row>
								<fo:table-cell height="10px">
									<fo:block>
										<xsl:text>&#xA;</xsl:text>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
													
						</fo:table-body>
					</fo:table>

				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
	<xsl:template name="radioButton">
		<xsl:param name="value" />
		<xsl:param name="schemaChoice">
			Y: Yes
		</xsl:param>
		<xsl:choose>
			<xsl:when test="normalize-space($value) = normalize-space($schemaChoice)">
				<fo:inline xmlns:fo="http://www.w3.org/1999/XSL/Format"
					xmlns:footer="http://apply.grants.gov/system/Footer-V1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema"
					xmlns:globLib="http://apply.grants.gov/system/GlobalLibrary-V2.0"
					xmlns:att="http://apply.grants.gov/system/Attachments-V1.0"
					xmlns:glob="http://apply.grants.gov/system/Global-V1.0"
					font-family="ZapfDingbats" font-size="10pt">&#x25cf;</fo:inline>
			</xsl:when>
			<xsl:otherwise>
				<fo:inline xmlns:fo="http://www.w3.org/1999/XSL/Format"
					xmlns:footer="http://apply.grants.gov/system/Footer-V1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema"
					xmlns:globLib="http://apply.grants.gov/system/GlobalLibrary-V2.0"
					xmlns:att="http://apply.grants.gov/system/Attachments-V1.0"
					xmlns:glob="http://apply.grants.gov/system/Global-V1.0"
					font-family="ZapfDingbats" font-size="10pt">&#x274d;</fo:inline>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="checkbox">
		<xsl:param name="value" />
		<xsl:param name="schemaChoice">
			Y: Yes
		</xsl:param>
		<xsl:if test="normalize-space($value) = normalize-space($schemaChoice)">
			<fo:inline xmlns:fo="http://www.w3.org/1999/XSL/Format"
				xmlns:footer="http://apply.grants.gov/system/Footer-V1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema"
				xmlns:globLib="http://apply.grants.gov/system/GlobalLibrary-V2.0"
				xmlns:att="http://apply.grants.gov/system/Attachments-V1.0"
				xmlns:glob="http://apply.grants.gov/system/Global-V1.0" font-family="ZapfDingbats"
				font-size="11pt">&#x2714;</fo:inline>
		</xsl:if>
	</xsl:template>
	<xsl:template name="formatDate">
		<xsl:param name="value" />
		<xsl:if test="$value != ''">
			<xsl:value-of select="format-number(substring($value,6,2), '00')" />
			<xsl:text>/</xsl:text>
			<xsl:value-of select="format-number(substring($value,9,2), '00')" />
			<xsl:text>/</xsl:text>
			<xsl:value-of select="format-number(substring($value,1,4), '0000')" />
		</xsl:if>
	</xsl:template>
	<xsl:template name="addBlankLines">
		<xsl:param name="numLines" />
		<xsl:if test="string($numLines) != ''">
			<xsl:if test="$numLines &gt; 0">
				<fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format"
					xmlns:footer="http://apply.grants.gov/system/Footer-V1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema"
					xmlns:globLib="http://apply.grants.gov/system/GlobalLibrary-V2.0"
					xmlns:att="http://apply.grants.gov/system/Attachments-V1.0"
					xmlns:glob="http://apply.grants.gov/system/Global-V1.0">
					<fo:leader leader-pattern="space" />
				</fo:block>
				<xsl:call-template name="addBlankLines">
					<xsl:with-param name="numLines" select="$numLines - 1" />
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- Program income table - MAX 10 for V3.0 -->
	<xsl:template name="incomeTable">
		<xsl:param name="beginPosition" />
		<xsl:param name="boxTop" />
		<xsl:param name="items" />
		<xsl:variable name="index" select="1" />
		<fo:table table-layout="fixed" width="7.4in" margin-left="0.05in" margin-right="0.05in">
			<fo:table-column column-width="1.1in" padding-top="0.2in"
				padding-left="0.1in" padding-before="0.2in" />
			<fo:table-column column-width="1.6in" padding-top="0.2in"
				padding-left="0.1in" padding-before="0.2in" />
			<fo:table-column column-width="4.7in" padding-top="0.2in"
				padding-left="0.1in" padding-before="0.2in" />
			<fo:table-body>
				<xsl:call-template name="incomeTableRow">
					<xsl:with-param name="beginPosition" select="$beginPosition + .2" />
					<xsl:with-param name="boxTop" select="$boxTop" />
					<xsl:with-param name="items" select="$items" />
					<xsl:with-param name="index" select="$index" />
					<xsl:with-param name="pageIndex" select="$index" />
				</xsl:call-template>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<xsl:template name="incomeTableRow">
		<xsl:param name="beginPosition" />
		<xsl:param name="boxTop" />
		<xsl:param name="items" />
		<xsl:param name="index" />
		<xsl:param name="pageIndex" />
		<xsl:variable name="lineLen"
			select="string-length($items[$index]/PHS398_CoverPageSupplement_4_0:Source)" />
		<xsl:variable name="sourceHeight">
			<xsl:value-of select="(ceiling($lineLen div 83) * .0855)" />
		</xsl:variable>
		<xsl:choose>
			<xsl:when
				test="ceiling($sourceHeight + $boxTop + $beginPosition) &gt; 9">
				<fo:table-row keep-together.within-page="always">
					<fo:table-cell number-columns-spanned="3" height=".3in">
						<fo:block />
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row keep-together.within-page="always">
					<fo:table-cell number-columns-spanned="3">
						<fo:block break-after="page">
							<xsl:text>&#xA;</xsl:text>
						</fo:block>
						<fo:block margin-left="-0.18in">
							<xsl:call-template name="tableHeader">
								<xsl:with-param name="topBorderWidth" select="1.0" />
								<xsl:with-param name="sideBorderWidth" select="0" />
							</xsl:call-template>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:call-template name="incomeTableRow">
					<xsl:with-param name="beginPosition" select="0.1" />
					<xsl:with-param name="boxTop" select="0.7" />
					<xsl:with-param name="items" select="$items" />
					<xsl:with-param name="index" select="$index" />
					<xsl:with-param name="pageIndex" select="1" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<fo:table-row keep-together.within-page="always">
					<fo:table-cell left="0.7in" display-align="after"
						text-align="right" height="auto" 
						padding-top="0.1in" padding-left="0.1in" padding-right="0.1in"
						padding-before="0.2in">
						<fo:block text-align="end" font-size="9pt"
							border-bottom-color="gray" border-bottom-style="dotted"
							border-bottom-width="1pt">
							<xsl:choose>
								<xsl:when
									test="not($items[$index]/PHS398_CoverPageSupplement_4_0:BudgetPeriod) or $items[$index]/PHS398_CoverPageSupplement_4_0:BudgetPeriod = ''">
									<fo:inline color="#FFFFFF">&#160;</fo:inline>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of
										select="format-number($items[$index]/PHS398_CoverPageSupplement_4_0:BudgetPeriod, '#,##0')" />
								</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell left="1.7in" display-align="after" text-align="right" height="auto" 
						padding-top="0.1in" padding-left="0.1in"
						padding-right="0.1in" padding-before="0.2in">
						<fo:block text-align="end" font-size="9pt"
							border-bottom-color="gray" border-bottom-style="dotted"
							border-bottom-width="1pt">
							<xsl:choose>
								<xsl:when
									test="not($items[$index]/PHS398_CoverPageSupplement_4_0:AnticipatedAmount) or $items[$index]/PHS398_CoverPageSupplement_4_0:AnticipatedAmount = ''">
									<fo:inline color="#FFFFFF">&#160;</fo:inline>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of
										select="format-number($items[$index]/PHS398_CoverPageSupplement_4_0:AnticipatedAmount, '#,##0.00')" />
								</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell left="3.4in" display-align="after" height="auto" 
						padding-top="0.1in" padding-left="0.1in" 
						padding-right="0.1in" padding-before="0.2in">
						<fo:block text-align="start" font-size="9pt"
							border-bottom-color="gray" border-bottom-style="dotted"
							border-bottom-width="1pt">
							<xsl:choose>
								<xsl:when
									test="not($items[$index]/PHS398_CoverPageSupplement_4_0:Source) or $items[$index]/PHS398_CoverPageSupplement_4_0:Source = ''">
									<fo:inline color="#FFFFFF">&#160;</fo:inline>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of
										select="$items[$index]/PHS398_CoverPageSupplement_4_0:Source" />
								</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:choose>
					<xsl:when test="$items[$index + 1]">
						<xsl:call-template name="incomeTableRow">
							<xsl:with-param name="beginPosition"
								select="$beginPosition + $sourceHeight + .1" />
							<xsl:with-param name="boxTop" select="$boxTop" />
							<xsl:with-param name="items" select="$items" />
							<xsl:with-param name="index" select="$index + 1" />
							<xsl:with-param name="pageIndex" select="$pageIndex + 1" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<fo:table-row keep-together.within-page="always">
							<fo:table-cell number-columns-spanned="3" height=".3in">
								<fo:block />
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="tableHeader">
		<xsl:param name="topBorderWidth" />
		<xsl:param name="sideBorderWidth">
			1.0
		</xsl:param>
		<xsl:param name="rowMargin">
			0.2
		</xsl:param>
		<fo:table table-layout="fixed" width="7.4in" margin-left="0.05in" margin-right="0.05in">
			<fo:table-column column-width="1.1in" padding-top="0.1in"
				padding-left="0.1in" padding-before="0.1in" />
			<fo:table-column column-width="1.6in" padding-top="0.1in"
				padding-left="0.1in" padding-before="0.1in" />
			<fo:table-column column-width="4.7in" padding-top="0.1in"
				padding-left="0.1in" padding-before="0.1in" />
			<fo:table-body>
				<fo:table-row margin-left="{normalize-space($rowMargin)}in">
					<fo:table-cell keep-together="always" height=".1in" padding-top=".1in">
						<fo:block font-size="9pt">
							*Budget Period
						</fo:block>
					</fo:table-cell>
					<fo:table-cell keep-together="always" height=".1in" padding-top=".1in">
						<fo:block font-size="9pt">
							*Anticipated Amount ($)
						</fo:block>
					</fo:table-cell>
					<fo:table-cell keep-together="always" height=".1in" padding-top=".1in">
						<fo:block font-size="9pt">
							*Source(s)
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<xsl:template name="stemCells">
		<xsl:param name="index" />
		<xsl:param name="value" />
		<xsl:variable name="colCount" select="12" />

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
	
	<xsl:template name="checkboxY">
		<xsl:param name="value" />
		<xsl:param name="schemaChoice">Y: Yes</xsl:param>
		<xsl:if test="normalize-space($value) = normalize-space($schemaChoice)">
			<fo:inline xmlns:fo="http://www.w3.org/1999/XSL/Format"
				xmlns:footer="http://apply.grants.gov/system/Footer-V1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema"
				xmlns:globLib="http://apply.grants.gov/system/GlobalLibrary-V2.0"
				xmlns:glob="http://apply.grants.gov/system/Global-V1.0" font-family="ZapfDingbats"
				font-size="8pt">&#x2714;</fo:inline>
		</xsl:if>
	</xsl:template>
		<xsl:template name="checkboxN">
		<xsl:param name="value" />
		<xsl:param name="schemaChoice">N: No</xsl:param>
		<xsl:if test="normalize-space($value) = normalize-space($schemaChoice)">
			<fo:inline xmlns:fo="http://www.w3.org/1999/XSL/Format"
				xmlns:footer="http://apply.grants.gov/system/Footer-V1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema"
				xmlns:globLib="http://apply.grants.gov/system/GlobalLibrary-V2.0"
				xmlns:glob="http://apply.grants.gov/system/Global-V1.0" font-family="ZapfDingbats"
				font-size="8pt">&#x2714;</fo:inline>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
