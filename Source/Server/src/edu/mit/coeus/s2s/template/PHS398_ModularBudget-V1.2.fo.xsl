<?xml version="1.0" encoding="UTF-8"?>
<!-- $Revision: 1.0 $ -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:globLib="http://apply.grants.gov/system/GlobalLibrary-V2.0" 
	xmlns:att="http://apply.grants.gov/system/Attachments-V1.0" 
	xmlns:glob="http://apply.grants.gov/system/Global-V1.0"
	xmlns:PHS398_ModularBudget_1_2="http://apply.grants.gov/forms/PHS398_ModularBudget_1_2-V1.2"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" 
	xmlns:footer="http://apply.grants.gov/system/Footer-V1.0"
	xmlns:header="http://apply.grants.gov/system/Header-V1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema">

	<xsl:output method="xml" indent="yes" />

	<xsl:template match="PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2">
		<fo:root>
			<fo:layout-master-set>
				<fo:simple-page-master master-name="default"
					page-height="11in" page-width="8.5in" margin-left="0.34in"
					margin-right="0.34in">
					<fo:region-body margin-top="0.5in" margin-bottom="0.5in" />
                                        <fo:region-after extent=".3in"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="default" format="1">
				<fo:flow flow-name="xsl-region-body">
					<fo:block>
					<xsl:for-each
						select="//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:Periods">
											<xsl:choose>
											<xsl:when test="position()='1'">
					<fo:block-container position="absolute" left="0.25in" top="1.0in" height="9px">
						<fo:block text-align="end" font-size="6px" font-family="arialuni">OMB Number: 0925-0001</fo:block>
						<fo:block text-align="end" font-size="6px" font-family="arialuni">Expiration Date: 03/31/2020</fo:block>
					</fo:block-container>
											</xsl:when>
											</xsl:choose>

						<!-- Budget period label with the element number -->
					<fo:block-container position="absolute" left="0.25in" top="0.5in" height="15px">
						<fo:block font-size="12px" font-family="arialuni" font-weight="bold" text-align="center">PHS 398 Modular Budget</fo:block>
					</fo:block-container>
							<fo:block-container position="absolute" left="0.25in" top="1.25in" height="0.25in" border-style="solid" border-color="black" border-top-width="1px" border-bottom-width="0.5px" border-left-width="1px" border-right-width="1px">
								<fo:block font-size="10px" font-family="arialuni" font-weight="bold" padding-before="2px" text-align="center">
								Budget Period:
								<xsl:value-of select="position()" />
							</fo:block>
						</fo:block-container>

						<xsl:call-template name="modularBudgetPeriods" />
						<fo:block break-after="page" />
					</xsl:for-each>
					</fo:block>
					<xsl:call-template name="cumulativeBudgetInfo" />
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<!-- Template for all the budget periods -->
	<xsl:template name="modularBudgetPeriods">
            <fo:block-container position="absolute" left="0.25in" top="1.5in" height="auto" padding="0px" margin="0px">
                <fo:block>
                    <fo:table width="100%" table-layout="fixed" border="solid thin black" border-top="0">
                        <fo:table-column column-width="proportional-column-width(1)"/>
                        <fo:table-body start-indent="0pt">
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block>
                                        <fo:table width="100%">
                                            <fo:table-column column-width="proportional-column-width(1)"/>
                                            <fo:table-body start-indent="0pt">
                                                <fo:table-row line-height="20px">
                                                    <fo:table-cell>
                                                        <fo:block text-indent="3.0in">
                                                            <fo:inline font-size="9px" font-family="arialuni" font-weight="bold">Start Date:&#160;</fo:inline>
                                                            <fo:inline font-size="9px" font-family="arialuni">
                                                                <xsl:choose>
                                                                    <xsl:when
                                                                        test="not(PHS398_ModularBudget_1_2:BudgetPeriodStartDate) or PHS398_ModularBudget_1_2:BudgetPeriodStartDate = ''">
                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                    </xsl:when>
                                                                    <xsl:otherwise>
                                                                        <xsl:call-template name="formatDate">
                                                                            <xsl:with-param name="value" select="PHS398_ModularBudget_1_2:BudgetPeriodStartDate" />
                                                                        </xsl:call-template>
                                                                    </xsl:otherwise>
                                                                </xsl:choose>
                                                            </fo:inline>

                                                            <fo:inline>&#160;&#160;&#160;&#160;&#160;</fo:inline>

                                                            <fo:inline font-size="9px" font-family="arialuni" font-weight="bold">End Date:&#160;</fo:inline>
                                                            <fo:inline font-size="9px" font-family="arialuni">
                                                                <xsl:choose>
                                                                    <xsl:when
                                                                        test="not(PHS398_ModularBudget_1_2:BudgetPeriodEndDate) or PHS398_ModularBudget_1_2:BudgetPeriodEndDate = ''">
                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                    </xsl:when>
                                                                    <xsl:otherwise>
                                                                        <xsl:call-template name="formatDate">
                                                                            <xsl:with-param name="value" select="PHS398_ModularBudget_1_2:BudgetPeriodEndDate" />
                                                                        </xsl:call-template>
                                                                    </xsl:otherwise>
                                                                </xsl:choose>
                                                            </fo:inline>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            
                            <!-- A. Direct Cost -->
                            
                            <fo:table-row line-height="15px">
                                <fo:table-cell>
                                    <fo:block>
                                        <fo:table width="100%" border="solid thin black">
                                            <fo:table-column column-width="85%"/>
                                            <fo:table-column column-width="15%"/>
                                            <fo:table-body start-indent="0pt">
                                                <fo:table-row>
                                                    <fo:table-cell number-columns-spanned="2" padding-left="5px">
                                                        <fo:block font-size="10pt" font-family="arialuni" font-weight="bold">
                                                            A. Direct Costs
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                                <fo:table-row>
                                                    <fo:table-cell>
                                                        <fo:block>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell>
                                                        <fo:block text-align="center" font-size="9px" font-family="arialuni" font-weight="bold">
                                                            Funds Requested ($)
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                                <fo:table-row>
                                                    <fo:table-cell>
                                                        <fo:block text-align="end" font-size="9px" font-family="arialuni">
                                                            Direct Cost less Consortium Indirect (F&amp;A)
                                                        </fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell>
                                                        <fo:block text-align="center" font-size="9px" font-family="arialuni" font-weight="normal">
                                                            <xsl:choose>
                                                                <xsl:when
                                                                    test="not(PHS398_ModularBudget_1_2:DirectCost/PHS398_ModularBudget_1_2:DirectCostLessConsortiumFandA) or PHS398_ModularBudget_1_2:DirectCost/PHS398_ModularBudget_1_2:DirectCostLessConsortiumFandA = ''">
                                                                    <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                </xsl:when>
                                                                <xsl:otherwise>
                                                                    <xsl:value-of
                                                                        select="format-number(PHS398_ModularBudget_1_2:DirectCost/PHS398_ModularBudget_1_2:DirectCostLessConsortiumFandA, '#,##0.00')" />
                                                                </xsl:otherwise>
                                                            </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                                <fo:table-row>
                                                    <fo:table-cell>
                                                        <fo:block text-align="end" font-size="9px" font-family="arialuni">Consortium Indirect (F&amp;A)</fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell>
                                                        <fo:block text-align="center" font-size="9px" font-family="arialuni">
                                                            <xsl:choose>
                                                                <xsl:when
                                                                    test="not(PHS398_ModularBudget_1_2:DirectCost/PHS398_ModularBudget_1_2:ConsortiumFandA) or PHS398_ModularBudget_1_2:DirectCost/PHS398_ModularBudget_1_2:ConsortiumFandA = ''">
                                                                    <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                </xsl:when>
                                                                <xsl:otherwise>
                                                                    <xsl:value-of
                                                                        select="format-number(PHS398_ModularBudget_1_2:DirectCost/PHS398_ModularBudget_1_2:ConsortiumFandA, '#,##0.00')" />
                                                                </xsl:otherwise>
                                                            </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                                <fo:table-row>
                                                    <fo:table-cell>
                                                        <fo:block text-align="end" font-size="9px" font-family="arialuni" font-weight="bold" display-align="center">Total Direct Costs</fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell>
                                                        <fo:block text-align="center" font-size="9px" font-family="arialuni" font-weight="bold" display-align="center"> <!-- border-top-color="black" border-top-style="solid" border-top-width="1px"> -->
                                                            <xsl:choose>
                                                                <xsl:when
                                                                    test="not(PHS398_ModularBudget_1_2:DirectCost/PHS398_ModularBudget_1_2:TotalFundsRequestedDirectCosts) or PHS398_ModularBudget_1_2:DirectCost/PHS398_ModularBudget_1_2:TotalFundsRequestedDirectCosts = ''">
                                                                    <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                </xsl:when>
                                                                <xsl:otherwise>
                                                                    <xsl:value-of
                                                                        select="format-number(PHS398_ModularBudget_1_2:DirectCost/PHS398_ModularBudget_1_2:TotalFundsRequestedDirectCosts, '#,##0.00')" />
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
                            
                            <!-- B. Indirect Costs -->
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block>
                                        <fo:table table-layout="fixed" width="100%">
                                            <fo:table-column column-width="proportional-column-width(1)"/>
                                            <fo:table-body>
                                                <fo:table-row line-height="20px">
                                                    <fo:table-cell>
                                                        <fo:block>
                                                            <fo:table table-layout="fixed" width="100%">
                                                                <fo:table-column column-width="3%"/>
                                                                <fo:table-column column-width="37%"/>
                                                                <fo:table-column column-width="20%"/>
                                                                <fo:table-column column-width="20%"/>
                                                                <fo:table-column column-width="20%"/>
                                                                <fo:table-body>
                                                                    <fo:table-row>
                                                                        <fo:table-cell padding-left="5px">
                                                                            <fo:block font-size="10px" font-family="arialuni" font-weight="bold">B.</fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell number-columns-spanned="4">
                                                                            <fo:block font-size="10px" font-family="arialuni" font-weight="bold">Indirect (F&amp;A) Costs</fo:block>
                                                                        </fo:table-cell>
                                                                    </fo:table-row>
                                                                    <fo:table-row line-height="12px">
                                                                        <fo:table-cell>
                                                                            <fo:block font-size="9px" font-family="arialuni" font-weight="bold">
                                                                                <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni" >Indirect (F&amp;A) Type</fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="left" font-size="9px" font-family="arialuni">Indirect (F&amp;A) &#xD;Rate (%)</fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="left" font-size="9px" font-family="arialuni">Indirect (F&amp;A) &#xD;Base ($)</fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni">Funds Requested ($)</fo:block>
                                                                        </fo:table-cell>
                                                                    </fo:table-row>

                                                                    <xsl:if test="PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems/PHS398_ModularBudget_1_2:IndirectCostTypeDescription or not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems/PHS398_ModularBudget_1_2:IndirectCostTypeDescription = '')">
                                                                    <fo:table-row>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="right" font-size="9px" font-family="arialuni">1. </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell padding-left="5px">
                                                                            <fo:block font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems/PHS398_ModularBudget_1_2:IndirectCostTypeDescription) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems/PHS398_ModularBudget_1_2:IndirectCostTypeDescription = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems/PHS398_ModularBudget_1_2:IndirectCostTypeDescription" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems/PHS398_ModularBudget_1_2:IndirectCostRate) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems/PHS398_ModularBudget_1_2:IndirectCostRate = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="format-number(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems/PHS398_ModularBudget_1_2:IndirectCostRate, '#,##0.00')" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems/PHS398_ModularBudget_1_2:IndirectCostBase) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems/PHS398_ModularBudget_1_2:IndirectCostBase = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="format-number(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems/PHS398_ModularBudget_1_2:IndirectCostBase, '#,##0.00')" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems/PHS398_ModularBudget_1_2:IndirectCostFundsRequested) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems/PHS398_ModularBudget_1_2:IndirectCostFundsRequested = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="format-number(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems/PHS398_ModularBudget_1_2:IndirectCostFundsRequested, '#,##0.00')" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                    </fo:table-row>
                                                                    </xsl:if>
                                                                    <!--second row-->
                                                                    <xsl:if test="PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[2]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[2]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription != ''">
                                                                    <fo:table-row>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="right" font-size="9px" font-family="arialuni">2. </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell padding-left="5px">
                                                                            <fo:block font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[2]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[2]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[2]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[2]/PHS398_ModularBudget_1_2:IndirectCostRate) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[2]/PHS398_ModularBudget_1_2:IndirectCostRate = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="format-number(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[2]/PHS398_ModularBudget_1_2:IndirectCostRate, '#,##0.00')" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[2]/PHS398_ModularBudget_1_2:IndirectCostBase) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[2]/PHS398_ModularBudget_1_2:IndirectCostBase = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="format-number(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[2]/PHS398_ModularBudget_1_2:IndirectCostBase, '#,##0.00')" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[2]/PHS398_ModularBudget_1_2:IndirectCostFundsRequested) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[2]/PHS398_ModularBudget_1_2:IndirectCostFundsRequested = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="format-number(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[2]/PHS398_ModularBudget_1_2:IndirectCostFundsRequested, '#,##0.00')" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                    </fo:table-row>
                                                                    </xsl:if>
                                                                    <!--third row-->
                                                                    <xsl:if test="PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[3]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[3]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription != ''">
                                                                    <fo:table-row>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="right" font-size="9px" font-family="arialuni">3. </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell padding-left="5px">
                                                                            <fo:block font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[3]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[3]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[3]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[3]/PHS398_ModularBudget_1_2:IndirectCostRate) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[3]/PHS398_ModularBudget_1_2:IndirectCostRate = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="format-number(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[3]/PHS398_ModularBudget_1_2:IndirectCostRate, '#,##0.00')" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[3]/PHS398_ModularBudget_1_2:IndirectCostBase) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[3]/PHS398_ModularBudget_1_2:IndirectCostBase = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="format-number(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[3]/PHS398_ModularBudget_1_2:IndirectCostBase, '#,##0.00')" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[3]/PHS398_ModularBudget_1_2:IndirectCostFundsRequested) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[3]/PHS398_ModularBudget_1_2:IndirectCostFundsRequested = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="format-number(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[3]/PHS398_ModularBudget_1_2:IndirectCostFundsRequested, '#,##0.00')" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                    </fo:table-row>
                                                                    </xsl:if>
                                                                    <!--fourth row-->
                                                                    <xsl:if test="PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[4]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[4]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription != ''">
                                                                    <fo:table-row>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="right" font-size="9px" font-family="arialuni">4. </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell padding-left="5px">
                                                                            <fo:block font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[4]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[4]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[4]/PHS398_ModularBudget_1_2:IndirectCostTypeDescription" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[4]/PHS398_ModularBudget_1_2:IndirectCostRate) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[4]/PHS398_ModularBudget_1_2:IndirectCostRate = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="format-number(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[4]/PHS398_ModularBudget_1_2:IndirectCostRate, '#,##0.00')" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[4]/PHS398_ModularBudget_1_2:IndirectCostBase) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[4]/PHS398_ModularBudget_1_2:IndirectCostBase = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="format-number(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[4]/PHS398_ModularBudget_1_2:IndirectCostBase, '#,##0.00')" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[4]/PHS398_ModularBudget_1_2:IndirectCostFundsRequested) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[4]/PHS398_ModularBudget_1_2:IndirectCostFundsRequested = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="format-number(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostItems[4]/PHS398_ModularBudget_1_2:IndirectCostFundsRequested, '#,##0.00')" />
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                    </fo:table-row>
                                                                    </xsl:if>


                                                                </fo:table-body>
                                                            </fo:table>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                                
                                                <fo:table-row line-height="20px">
                                                    <fo:table-cell>
                                                        <fo:block>
                                                            <fo:table table-layout="fixed" width="100%">
                                                                <fo:table-column column-width="50%"/>
                                                                <fo:table-column column-width="50%"/>
                                                                <fo:table-body>
                                                                    <fo:table-row line-height="60px">
                                                                        <fo:table-cell padding-left="10px">
                                                                            <fo:block font-size="9px" font-family="arialuni" padding-left="10px" display-align="before">Cognizant Agency (Agency Name, POC Name and Phone Number)</fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="left" font-size="9px" font-family="arialuni">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:CognizantFederalAgency) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:CognizantFederalAgency = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:CognizantFederalAgency" />
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
                                                <fo:table-row line-height="15px">
                                                    <fo:table-cell>
                                                        <fo:block>
                                                            <fo:table table-layout="fixed" width="100%">
                                                                <fo:table-column column-width="30%"/>
                                                                <fo:table-column column-width="20%"/>
                                                                <fo:table-column column-width="40%"/>
                                                                <fo:table-column column-width="10%"/>
                                                                <fo:table-body>
                                                                    <fo:table-row>
                                                                        <fo:table-cell padding-left="10px">
                                                                            <fo:block position="absolute" width="2.5in" font-size="9px" font-family="arialuni">Indirect (F&amp;A) Rate Agreement Date</fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block font-size="9px" font-family="arialuni" text-align="left">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostAgreementDate) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostAgreementDate = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:call-template name="formatDate">
                                                                                            <xsl:with-param name="value"
                                                                                                            select="PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:IndirectCostAgreementDate" />
                                                                                        </xsl:call-template>
                                                                                    </xsl:otherwise>
                                                                                </xsl:choose>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="end" font-size="9px" font-family="arialuni" font-weight="bold">Total Indirect (F&amp;A) Costs</fo:block>
                                                                        </fo:table-cell>
                                                                        <fo:table-cell>
                                                                            <fo:block text-align="center" font-size="9px" font-family="arialuni" font-weight="bold" display-align="center">
                                                                                <xsl:choose>
                                                                                    <xsl:when
                                                                                        test="not(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:TotalFundsRequestedIndirectCost) or PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:TotalFundsRequestedIndirectCost = ''">
                                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                                    </xsl:when>
                                                                                    <xsl:otherwise>
                                                                                        <xsl:value-of
                                                                                            select="format-number(PHS398_ModularBudget_1_2:IndirectCost/PHS398_ModularBudget_1_2:TotalFundsRequestedIndirectCost, '#,##0.00')" />
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
                                            </fo:table-body>
                                        </fo:table>
                                        
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <!-- B. Indirect Costs END -->
                            
                            <!-- C. Total Direct and Indirect Costs (A + B) -->
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block>
                                        <fo:table table-layout="fixed" width="100%" border="solid thin black">
                                            <fo:table-column column-width="50%"/>
                                            <fo:table-column column-width="30%"/>
                                            <fo:table-column column-width="20%"/>
                                            <fo:table-body>
                                                <fo:table-row line-height="30px">
                                                    <fo:table-cell padding-left="10px">
                                                            <fo:block font-size="10px" font-family="arialuni" font-weight="bold">C. Total Direct and Indirect (F&amp;A) Costs (A + B)</fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell>
                                                            <fo:block text-align="right" font-size="9px" font-family="arialuni">Funds Requested ($)</fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell padding-left="10px">
                                                            <fo:block text-align="left" font-size="9px" font-family="arialuni">
                                                                <xsl:choose>
                                                                    <xsl:when
                                                                        test="not(PHS398_ModularBudget_1_2:TotalFundsRequestedDirectIndirectCosts) or PHS398_ModularBudget_1_2:TotalFundsRequestedDirectIndirectCosts = ''">
                                                                        <fo:inline color="#FFFFFF">&#160;</fo:inline>
                                                                    </xsl:when>
                                                                    <xsl:otherwise>
                                                                        <xsl:value-of
                                                                            select="format-number(PHS398_ModularBudget_1_2:TotalFundsRequestedDirectIndirectCosts, '#,##0.00')" />
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
                            
                            <!-- C. Total Direct and Indirect Costs (A + B) END-->
                            
                        </fo:table-body>
                    </fo:table>
                </fo:block>
            </fo:block-container>



		<!-- C. Total Direct and Indirect Costs (A + B) -->
<!--        		<fo:block-container position="absolute" left="0.25in" top="6.7in" height="0.5in" border-color="black" border-style="solid" border-top-width="0.5px">
                            <fo:block />
        		</fo:block-container>

		<fo:block-container position="absolute" left="0.5in" top="6.9in" height="12px">
						<fo:block font-size="10px" font-family="arialuni" font-weight="bold">C. Total Direct and Indirect (F&amp;A) Costs (A + B)</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="4.5in" top="6.9in" height="12px" width="2.0in">
						<fo:block text-align="right" font-size="9px" font-family="arialuni" font-weight="bold">Funds Requested ($)</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="6.75in" top="6.9in" height="12px" width="1.4in">
						<fo:block text-align="right" font-size="9px" font-family="arialuni" font-weight="bold">
				<xsl:choose>
					<xsl:when
						test="not(PHS398_ModularBudget_1_2:TotalFundsRequestedDirectIndirectCosts) or PHS398_ModularBudget_1_2:TotalFundsRequestedDirectIndirectCosts = ''">
						<fo:inline color="#FFFFFF">&#160;</fo:inline>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of
							select="format-number(PHS398_ModularBudget_1_2:TotalFundsRequestedDirectIndirectCosts, '#,##0.00')" />
					</xsl:otherwise>
				</xsl:choose>
			</fo:block>
		</fo:block-container>-->
	</xsl:template>

	<!-- Cumulative Budget Info section -->


	<xsl:template name="cumulativeBudgetInfo">
				<fo:block-container position="absolute" left="0.25in" top="0.5in" height="15px">
						<fo:block font-size="12px" font-family="arialuni" font-weight="bold" text-align="center">PHS 398 Modular Budget</fo:block>
					</fo:block-container>

		<fo:block-container position="absolute" left="0.25in" top="1.25in" height="0.25in" border-style="solid" border-color="black" border-top-width="1px"  border-bottom-width="0.5px" border-left-width="1px" border-right-width="1px">
			<fo:block font-size="10px" font-family="arialuni" font-weight="bold" text-align="center" padding-before="2px">
            	Cumulative Budget Information
			</fo:block>
		</fo:block-container>

		<!--  Total Costs, Entire Project Periods -->
                		<fo:block-container position="absolute" left="0.25in" top="1.5in" height="1.7in" border-color="black" border-style="solid" border-top-width="0.5px" border-bottom-width="0.5px" border-left-width="1px" border-right-width="1px">
                            <fo:block />
        		</fo:block-container>
					<fo:block-container position="absolute" left="0.5in" top="1.8in" height="15px">
						<fo:block font-size="10px" font-family="arialuni" font-weight="bold">Total Costs, Entire Project Period</fo:block>
					</fo:block-container>

       				<fo:block-container position="absolute" left="0.5in" top="2.1in" height="12px">
						<fo:block font-size="9px" font-family="arialuni">Section A, Total Direct Cost less Consortium Indirect (F&amp;A) for Entire Project Period ($)</fo:block>
					</fo:block-container>
                    <fo:block-container position="absolute" left="5.0in" top="2.1in" height="12px" width="1.45in">
					 <fo:block text-align="right" font-size="9px" font-family="arialuni">
				<xsl:choose>
					<xsl:when
						test="not(//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeDirectCostLessConsortiumFandA) or //PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeDirectCostLessConsortiumFandA = ''">
						<fo:inline color="#FFFFFF">&#160;</fo:inline>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of
							select="format-number(//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeDirectCostLessConsortiumFandA, '#,##0.00')" />
					</xsl:otherwise>
				</xsl:choose>
			</fo:block>
		</fo:block-container>

                    <fo:block-container position="absolute" left="0.5in" top="2.3in" height="12px">
						<fo:block font-size="9px" font-family="arialuni">Section A, Total Consortium Indirect (F&amp;A) for Entire Project Period ($)</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="5.0in" top="2.3in" height="12px" width="1.45in">
						<fo:block text-align="right" font-size="9px" font-family="arialuni">
				<xsl:choose>
					<xsl:when
						test="not(//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeConsortiumFandA) or //PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeConsortiumFandA = ''">
						<fo:inline color="#FFFFFF">&#160;</fo:inline>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of
							select="format-number(//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeConsortiumFandA, '#,##0.00')" />
					</xsl:otherwise>
				</xsl:choose>
               </fo:block>
		</fo:block-container>

                    <fo:block-container position="absolute" left="0.5in" top="2.5in" height="12px">
						<fo:block font-size="9px" font-family="arialuni">Section A, Total Direct Costs for Entire Project Period ($)</fo:block>
					</fo:block-container>
                    <fo:block-container position="absolute" left="5.0in" top="2.5in" height="12px" width="1.45in">
						<fo:block text-align="right" font-size="9px" font-family="arialuni">
				<xsl:choose>
					<xsl:when
						test="not(//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeTotalFundsRequestedDirectCosts) or //PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeTotalFundsRequestedDirectCosts = ''">
						<fo:inline color="#FFFFFF">&#160;</fo:inline>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of
							select="format-number(//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeTotalFundsRequestedDirectCosts, '#,##0.00')" />
					</xsl:otherwise>
				</xsl:choose>
			</fo:block>
		</fo:block-container>

                    <fo:block-container position="absolute" left="0.5in" top="2.7in" height="12px">
						<fo:block font-size="9px" font-family="arialuni">Section B, Total Indirect (F&amp;A) Costs for Entire Project Period ($)</fo:block>
					</fo:block-container>
                    <fo:block-container position="absolute" left="5.0in" top="2.7in" height="12px" width="1.45in">
						<fo:block text-align="right" font-size="9px" font-family="arialuni">
				<xsl:choose>
					<xsl:when
						test="not(//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeTotalFundsRequestedIndirectCost) or //PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeTotalFundsRequestedIndirectCost = ''">
						<fo:inline color="#FFFFFF">&#160;</fo:inline>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of
							select="format-number(//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeTotalFundsRequestedIndirectCost, '#,##0.00')" />
					</xsl:otherwise>
				</xsl:choose>
			</fo:block>
		</fo:block-container>


                    <fo:block-container position="absolute" left="0.5in" top="2.9in" height="12px">
						<fo:block font-size="9px" font-family="arialuni">Section C, Total Direct and Indirect (F&amp;A) Costs (A+B) for Entire Project Period ($)</fo:block>
					</fo:block-container>
       				<fo:block-container position="absolute" left="5.0in" top="2.9in" height="12px" width="1.45in">
						<fo:block text-align="right" font-size="9px" font-family="arialuni">
				<xsl:choose>
					<xsl:when
						test="not(//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeTotalFundsRequestedDirectIndirectCosts) or //PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeTotalFundsRequestedDirectIndirectCosts = ''">
						<fo:inline color="#FFFFFF">&#160;</fo:inline>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of
							select="format-number(//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:EntirePeriodTotalCost/PHS398_ModularBudget_1_2:CumulativeTotalFundsRequestedDirectIndirectCosts, '#,##0.00')" />
					</xsl:otherwise>
				</xsl:choose>
			</fo:block>
		</fo:block-container>

		<!-- 2. Budget Justifications -->

                		<fo:block-container position="absolute" left="0.25in" top="3.2in" height="1.5in" border-color="black" border-style="solid" border-top-width="0.5px" border-bottom-width="1px" border-left-width="1px" border-right-width="1px">
                            <fo:block />
        		</fo:block-container>
  					<fo:block-container position="absolute" left="0.5in" top="3.4in" height="15px">
						<fo:block font-size="10px" font-family="arialuni" font-weight="bold">Budget Justifications</fo:block>
					</fo:block-container>


					<fo:block-container position="absolute" left="0.5in" top="3.7in" height="12px">
						<fo:block font-size="9px" font-family="arialuni">Personnel Justification</fo:block>
                 	</fo:block-container>
                     <fo:block-container position="absolute" left="2.5in" top="3.7in" height="12px">
						<fo:block font-size="10px" font-family="arialuni">
				<xsl:if
					test="not(//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:BudgetJustifications/PHS398_ModularBudget_1_2:PersonnelJustification/PHS398_ModularBudget_1_2:attFile/att:FileName) or /PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:BudgetJustifications/PHS398_ModularBudget_1_2:PersonnelJustification/PHS398_ModularBudget_1_2:attFile/att:FileName = ''">
					<fo:inline color="#FFFFFF">&#160;</fo:inline>
				</xsl:if>
				<xsl:value-of
					select="//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:BudgetJustifications/PHS398_ModularBudget_1_2:PersonnelJustification/PHS398_ModularBudget_1_2:attFile/att:FileName" />
			</fo:block>
		</fo:block-container>


					<fo:block-container position="absolute" left="0.5in" top="3.9in" height="12px">
						<fo:block font-size="9px" font-family="arialuni">Consortium Justification</fo:block>
					</fo:block-container>
                    <fo:block-container position="absolute" left="2.5in" top="3.9in" height="12px">
						<fo:block font-size="10px" font-family="arialuni">
				<xsl:if
					test="not(//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:BudgetJustifications/PHS398_ModularBudget_1_2:ConsortiumJustification/PHS398_ModularBudget_1_2:attFile/att:FileName) or /PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:BudgetJustifications/PHS398_ModularBudget_1_2:ConsortiumJustification/PHS398_ModularBudget_1_2:attFile/att:FileName = ''">
					<fo:inline color="#FFFFFF">&#160;</fo:inline>
				</xsl:if>
				<xsl:value-of
					select="//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:BudgetJustifications/PHS398_ModularBudget_1_2:ConsortiumJustification/PHS398_ModularBudget_1_2:attFile/att:FileName" />
			</fo:block>
		</fo:block-container>

					<fo:block-container position="absolute" left="0.5in" top="4.1in" height="12px">
						<fo:block font-size="9px" font-family="arialuni">Additional Narrative Justification</fo:block>
					</fo:block-container>
                    <fo:block-container position="absolute" left="2.5in" top="4.1in" height="12px">
						<fo:block font-size="10px" font-family="arialuni">
				<xsl:if
					test="not(//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:BudgetJustifications/PHS398_ModularBudget_1_2:AdditionalNarrativeJustification/PHS398_ModularBudget_1_2:attFile/att:FileName) or /PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:BudgetJustifications/PHS398_ModularBudget_1_2:AdditionalNarrativeJustification/PHS398_ModularBudget_1_2:attFile/att:FileName = ''">
					<fo:inline color="#FFFFFF">&#160;</fo:inline>
				</xsl:if>
				<xsl:value-of
					select="//PHS398_ModularBudget_1_2:PHS398_ModularBudget_1_2/PHS398_ModularBudget_1_2:CummulativeBudgetInfo/PHS398_ModularBudget_1_2:BudgetJustifications/PHS398_ModularBudget_1_2:AdditionalNarrativeJustification/PHS398_ModularBudget_1_2:attFile/att:FileName" />
			</fo:block>
		</fo:block-container>
	</xsl:template>

	<xsl:template name="radioButton">
		<xsl:param name="value" />
		<xsl:param name="schemaChoice">Y: Yes</xsl:param>
		<xsl:choose>
			<xsl:when test="$value = $schemaChoice">
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
		<xsl:param name="schemaChoice">Y: Yes</xsl:param>
		<xsl:if test="$value = $schemaChoice">
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
</xsl:stylesheet>
